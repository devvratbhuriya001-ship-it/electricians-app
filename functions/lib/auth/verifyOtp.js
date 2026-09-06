"use strict";
/**
 * verifyOtp Cloud Function
 * Verifies 6-digit SMS OTP with 2Factor.in, validates session and attempts,
 * loads user profile from Firestore, creates Firebase Custom Token, and returns token to Android.
 */
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.verifyOtpHandler = void 0;
const admin = __importStar(require("firebase-admin"));
const https_1 = require("firebase-functions/v2/https");
const logger = __importStar(require("firebase-functions/logger"));
const cors_1 = __importDefault(require("cors"));
const config_1 = require("../config");
const phoneUtils_1 = require("../utils/phoneUtils");
const twoFactorService_1 = require("../services/twoFactorService");
const firebaseUserService_1 = require("../services/firebaseUserService");
const corsHandler = (0, cors_1.default)({ origin: true });
exports.verifyOtpHandler = (0, https_1.onRequest)({ secrets: [config_1.twoFactorApiKeySecret], region: "asia-south1", timeoutSeconds: 30 }, (req, res) => {
    corsHandler(req, res, async () => {
        if (req.method !== "POST") {
            res.status(405).json({ success: false, message: "Method Not Allowed" });
            return;
        }
        try {
            const db = admin.firestore();
            const { phoneNumber, otp, sessionId } = req.body;
            if (!otp || otp.trim().length !== 6 || !/^\d{6}$/.test(otp.trim())) {
                res.status(400).json({
                    success: false,
                    code: "INVALID_OTP",
                    message: "Please enter the complete 6-digit numeric OTP."
                });
                return;
            }
            if (!sessionId) {
                res.status(400).json({
                    success: false,
                    code: "OTP_EXPIRED",
                    message: "Session reference missing or expired. Please request a new OTP."
                });
                return;
            }
            const normalized = (0, phoneUtils_1.normalizeIndianPhoneNumber)(phoneNumber);
            const sessionRef = db.collection("otp_sessions").doc(sessionId);
            const sessionDoc = await sessionRef.get();
            if (!sessionDoc.exists) {
                res.status(400).json({
                    success: false,
                    code: "OTP_EXPIRED",
                    message: "This OTP has expired. Please request a new one."
                });
                return;
            }
            const sessionData = sessionDoc.data();
            const attempts = sessionData.attempts || 0;
            // Abuse & Brute-Force Protection
            if (attempts >= config_1.TWO_FACTOR_CONFIG.MAX_ATTEMPTS_PER_SESSION) {
                await sessionRef.delete();
                res.status(429).json({
                    success: false,
                    code: "TOO_MANY_ATTEMPTS",
                    message: "Too many incorrect attempts. Please request a fresh OTP."
                });
                return;
            }
            // Verify with 2Factor.in Official SMS API
            const apiKey = config_1.twoFactorApiKeySecret.value() || process.env.TWO_FACTOR_API_KEY || "";
            const verification = await twoFactorService_1.TwoFactorService.verifySmsOtp(apiKey, sessionId, otp.trim());
            if (!verification.matched) {
                await sessionRef.update({ attempts: attempts + 1 });
                res.status(400).json({
                    success: false,
                    code: "INVALID_OTP",
                    message: verification.errorReason || "The OTP you entered is incorrect."
                });
                return;
            }
            // Clean up session immediately upon success
            await sessionRef.delete();
            // Query Firestore profile
            const e164 = normalized.e164 || sessionData.phoneNumber;
            const phoneKey = normalized.clean10Digits || sessionData.phoneKey;
            const user = await firebaseUserService_1.FirebaseUserService.findUserByPhone(db, e164, phoneKey);
            if (!user) {
                res.status(404).json({
                    success: false,
                    notRegistered: true,
                    code: "USER_NOT_REGISTERED",
                    message: "This mobile number is not registered. Please register first."
                });
                return;
            }
            // Generate Firebase Custom Token
            const customToken = await firebaseUserService_1.FirebaseUserService.createCustomTokenForUser(user.uid, user.role, user.accountStatus, user.phoneNumber);
            // Update last login
            await firebaseUserService_1.FirebaseUserService.updateLastLogin(db, user.uid);
            res.status(200).json({
                success: true,
                customToken,
                user: {
                    uid: user.uid,
                    fullName: user.fullName,
                    phoneNumber: user.phoneNumber,
                    role: user.role,
                    accountStatus: user.accountStatus,
                    electricianId: user.electricianId,
                    area: user.area,
                    businessName: user.businessName,
                    profileImageUrl: user.profileImageUrl
                }
            });
        }
        catch (err) {
            logger.error("verifyOtp error:", err.message);
            res.status(500).json({
                success: false,
                code: "INTERNAL_ERROR",
                message: err.message || "Failed to verify OTP."
            });
        }
    });
});
//# sourceMappingURL=verifyOtp.js.map