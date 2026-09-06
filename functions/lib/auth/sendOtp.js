"use strict";
/**
 * sendOtp Cloud Function
 * Validates 10-digit Indian phone number, checks rate limit, verifies registration status,
 * dispatches SMS OTP via 2Factor.in official API, and stores secure session reference.
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
exports.sendOtpHandler = void 0;
const admin = __importStar(require("firebase-admin"));
const https_1 = require("firebase-functions/v2/https");
const logger = __importStar(require("firebase-functions/logger"));
const cors_1 = __importDefault(require("cors"));
const config_1 = require("../config");
const phoneUtils_1 = require("../utils/phoneUtils");
const rateLimitService_1 = require("../services/rateLimitService");
const twoFactorService_1 = require("../services/twoFactorService");
const firebaseUserService_1 = require("../services/firebaseUserService");
const corsHandler = (0, cors_1.default)({ origin: true });
exports.sendOtpHandler = (0, https_1.onRequest)({ secrets: [config_1.twoFactorApiKeySecret], region: "asia-south1", timeoutSeconds: 30 }, (req, res) => {
    corsHandler(req, res, async () => {
        if (req.method !== "POST") {
            res.status(405).json({ success: false, message: "Method Not Allowed" });
            return;
        }
        try {
            const db = admin.firestore();
            const { phoneNumber, isRegistration = false } = req.body;
            const normalized = (0, phoneUtils_1.normalizeIndianPhoneNumber)(phoneNumber);
            if (!normalized.isValid) {
                res.status(400).json({
                    success: false,
                    code: "INVALID_PHONE",
                    message: "Please enter a valid 10-digit mobile number."
                });
                return;
            }
            const phoneKey = normalized.clean10Digits;
            const e164 = normalized.e164;
            // 1. Server-side Rate Limiting
            const rateCheck = await rateLimitService_1.RateLimitService.checkAndEnforceRateLimit(db, phoneKey);
            if (!rateCheck.allowed) {
                res.status(429).json({
                    success: false,
                    code: "TOO_MANY_REQUESTS",
                    message: rateCheck.reason
                });
                return;
            }
            // 2. Existing User Registration Check
            const existingUser = await firebaseUserService_1.FirebaseUserService.findUserByPhone(db, e164, phoneKey);
            if (!isRegistration && !existingUser) {
                res.status(404).json({
                    success: false,
                    notRegistered: true,
                    code: "USER_NOT_REGISTERED",
                    message: "This mobile number is not registered. Please register first."
                });
                return;
            }
            if (isRegistration && existingUser) {
                res.status(409).json({
                    success: false,
                    alreadyRegistered: true,
                    code: "USER_ALREADY_REGISTERED",
                    message: "An account with this mobile number already exists. Please log in."
                });
                return;
            }
            // 3. Dispatch SMS OTP via 2Factor.in Official SMS API
            const apiKey = config_1.twoFactorApiKeySecret.value() || process.env.TWO_FACTOR_API_KEY || "";
            const { sessionId } = await twoFactorService_1.TwoFactorService.sendSmsOtp(apiKey, phoneKey);
            // 4. Save Session Reference in Firestore
            const now = Date.now();
            await db.collection("otp_sessions").doc(sessionId).set({
                sessionId,
                phoneNumber: e164,
                phoneKey,
                isRegistration,
                attempts: 0,
                createdAt: admin.firestore.FieldValue.serverTimestamp(),
                expiresAt: new Date(now + config_1.TWO_FACTOR_CONFIG.SESSION_EXPIRY_MINUTES * 60 * 1000)
            });
            // 5. Record Rate Limit
            await rateLimitService_1.RateLimitService.recordOtpSent(db, phoneKey);
            res.status(200).json({
                success: true,
                sessionId,
                message: "SMS verification code sent successfully."
            });
        }
        catch (err) {
            logger.error("sendOtp error:", err.message);
            res.status(500).json({
                success: false,
                code: "INTERNAL_ERROR",
                message: err.message || "Failed to send SMS verification code."
            });
        }
    });
});
//# sourceMappingURL=sendOtp.js.map