"use strict";
/**
 * Registration Handlers (registerUser & verifyRegistrationOtp)
 * Handles new electrician onboarding flow with SMS OTP verification,
 * creating a RegistrationRequest and User profile with status = PENDING.
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
exports.verifyRegistrationOtpHandler = exports.registerUserHandler = void 0;
const admin = __importStar(require("firebase-admin"));
const https_1 = require("firebase-functions/v2/https");
const logger = __importStar(require("firebase-functions/logger"));
const cors_1 = __importDefault(require("cors"));
const config_1 = require("../config");
const phoneUtils_1 = require("../utils/phoneUtils");
const twoFactorService_1 = require("../services/twoFactorService");
const firebaseUserService_1 = require("../services/firebaseUserService");
const rateLimitService_1 = require("../services/rateLimitService");
const corsHandler = (0, cors_1.default)({ origin: true });
exports.registerUserHandler = (0, https_1.onRequest)({ secrets: [config_1.twoFactorApiKeySecret], region: "asia-south1", timeoutSeconds: 30 }, (req, res) => {
    corsHandler(req, res, async () => {
        if (req.method !== "POST") {
            res.status(405).json({ success: false, message: "Method Not Allowed" });
            return;
        }
        try {
            const db = admin.firestore();
            const { fullName, phoneNumber, area, electricianId, businessName, referralCode } = req.body;
            if (!fullName || fullName.trim().length < 2) {
                res.status(400).json({ success: false, message: "Please enter your full name." });
                return;
            }
            const normalized = (0, phoneUtils_1.normalizeIndianPhoneNumber)(phoneNumber);
            if (!normalized.isValid) {
                res.status(400).json({
                    success: false,
                    code: "INVALID_PHONE",
                    message: "Please enter a valid 10-digit mobile number."
                });
                return;
            }
            if (!area || area.trim().length < 2) {
                res.status(400).json({ success: false, message: "Please enter your area or city." });
                return;
            }
            // Check if phone already registered
            const existing = await firebaseUserService_1.FirebaseUserService.findUserByPhone(db, normalized.e164, normalized.clean10Digits);
            if (existing) {
                res.status(409).json({
                    success: false,
                    code: "USER_ALREADY_REGISTERED",
                    message: "An account with this mobile number already exists. Please log in."
                });
                return;
            }
            // Rate limit check
            const rateCheck = await rateLimitService_1.RateLimitService.checkAndEnforceRateLimit(db, normalized.clean10Digits);
            if (!rateCheck.allowed) {
                res.status(429).json({
                    success: false,
                    code: "TOO_MANY_REQUESTS",
                    message: rateCheck.reason
                });
                return;
            }
            // Dispatch 2Factor SMS OTP
            const apiKey = config_1.twoFactorApiKeySecret.value() || process.env.TWO_FACTOR_API_KEY || "";
            const { sessionId } = await twoFactorService_1.TwoFactorService.sendSmsOtp(apiKey, normalized.clean10Digits);
            // Store registration draft securely
            await db.collection("registration_drafts").doc(sessionId).set({
                sessionId,
                fullName: fullName.trim(),
                phoneNumber: normalized.e164,
                phoneKey: normalized.clean10Digits,
                area: area.trim(),
                electricianId: electricianId ? electricianId.trim() : null,
                businessName: businessName ? businessName.trim() : null,
                referralCode: referralCode ? referralCode.trim() : null,
                createdAt: admin.firestore.FieldValue.serverTimestamp(),
                expiresAt: new Date(Date.now() + config_1.TWO_FACTOR_CONFIG.SESSION_EXPIRY_MINUTES * 60 * 1000)
            });
            await rateLimitService_1.RateLimitService.recordOtpSent(db, normalized.clean10Digits);
            res.status(200).json({
                success: true,
                sessionId,
                message: "Registration OTP sent via SMS."
            });
        }
        catch (err) {
            logger.error("registerUser error:", err.message);
            res.status(500).json({
                success: false,
                code: "INTERNAL_ERROR",
                message: err.message || "Failed to initiate registration."
            });
        }
    });
});
exports.verifyRegistrationOtpHandler = (0, https_1.onRequest)({ secrets: [config_1.twoFactorApiKeySecret], region: "asia-south1", timeoutSeconds: 30 }, (req, res) => {
    corsHandler(req, res, async () => {
        if (req.method !== "POST") {
            res.status(405).json({ success: false, message: "Method Not Allowed" });
            return;
        }
        try {
            const db = admin.firestore();
            const { otp, sessionId } = req.body;
            if (!otp || otp.trim().length !== 6) {
                res.status(400).json({
                    success: false,
                    code: "INVALID_OTP",
                    message: "Please enter the complete 6-digit OTP."
                });
                return;
            }
            if (!sessionId) {
                res.status(400).json({
                    success: false,
                    code: "OTP_EXPIRED",
                    message: "Registration session has expired. Please register again."
                });
                return;
            }
            const draftRef = db.collection("registration_drafts").doc(sessionId);
            const draftDoc = await draftRef.get();
            if (!draftDoc.exists) {
                res.status(400).json({
                    success: false,
                    code: "OTP_EXPIRED",
                    message: "Session expired. Please restart registration."
                });
                return;
            }
            const draft = draftDoc.data();
            // Verify with 2Factor.in SMS OTP
            const apiKey = config_1.twoFactorApiKeySecret.value() || process.env.TWO_FACTOR_API_KEY || "";
            const verification = await twoFactorService_1.TwoFactorService.verifySmsOtp(apiKey, sessionId, otp);
            if (!verification.matched) {
                res.status(400).json({
                    success: false,
                    code: "INVALID_OTP",
                    message: verification.errorReason || "The OTP you entered is incorrect."
                });
                return;
            }
            // Clean up draft session
            await draftRef.delete();
            // Generate identifiers
            const uid = `usr_${Date.now()}_${Math.random().toString(36).substring(2, 8)}`;
            const requestId = `req_${Date.now()}_${Math.random().toString(36).substring(2, 8)}`;
            const batch = db.batch();
            // 1. Create registration request
            const registrationRef = db.collection("registration_requests").doc(requestId);
            batch.set(registrationRef, {
                registrationId: requestId,
                uid,
                fullName: draft.fullName,
                phoneNumber: draft.phoneNumber,
                area: draft.area,
                electricianId: draft.electricianId || null,
                businessName: draft.businessName || null,
                referralCode: draft.referralCode || null,
                status: "PENDING",
                createdAt: admin.firestore.FieldValue.serverTimestamp(),
                reviewedAt: null,
                reviewedBy: null,
                rejectionReason: null
            });
            // 2. Create User document with status = PENDING and role = ELECTRICIAN
            const userRef = db.collection("users").doc(uid);
            batch.set(userRef, {
                uid,
                fullName: draft.fullName,
                phoneNumber: draft.phoneNumber,
                role: "ELECTRICIAN",
                accountStatus: "PENDING",
                electricianId: draft.electricianId || null,
                area: draft.area,
                businessName: draft.businessName || null,
                profileImageUrl: null,
                createdAt: admin.firestore.FieldValue.serverTimestamp(),
                approvedAt: null,
                lastLoginAt: admin.firestore.FieldValue.serverTimestamp()
            });
            await batch.commit();
            // 3. Generate Firebase Custom Token
            const customToken = await firebaseUserService_1.FirebaseUserService.createCustomTokenForUser(uid, "ELECTRICIAN", "PENDING", draft.phoneNumber);
            res.status(200).json({
                success: true,
                customToken,
                registrationId: requestId,
                accountStatus: "PENDING",
                message: "Registration submitted successfully. Pending approval."
            });
        }
        catch (err) {
            logger.error("verifyRegistrationOtp error:", err.message);
            res.status(500).json({
                success: false,
                code: "INTERNAL_ERROR",
                message: err.message || "Failed to complete registration."
            });
        }
    });
});
//# sourceMappingURL=registration.js.map