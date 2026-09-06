/**
 * sendOtp Cloud Function
 * Validates 10-digit Indian phone number, checks rate limit, verifies registration status,
 * dispatches SMS OTP via 2Factor.in official API, and stores secure session reference.
 */

import * as admin from "firebase-admin";
import { onRequest } from "firebase-functions/v2/https";
import * as logger from "firebase-functions/logger";
import cors from "cors";
import { twoFactorApiKeySecret, TWO_FACTOR_CONFIG } from "../config";
import { normalizeIndianPhoneNumber } from "../utils/phoneUtils";
import { RateLimitService } from "../services/rateLimitService";
import { TwoFactorService } from "../services/twoFactorService";
import { FirebaseUserService } from "../services/firebaseUserService";

const corsHandler = cors({ origin: true });

export const sendOtpHandler = onRequest(
  { secrets: [twoFactorApiKeySecret], region: "asia-south1", timeoutSeconds: 30 },
  (req, res) => {
    corsHandler(req, res, async () => {
      if (req.method !== "POST") {
        res.status(405).json({ success: false, message: "Method Not Allowed" });
        return;
      }

      try {
        const db = admin.firestore();
        const { phoneNumber, isRegistration = false } = req.body;

        const normalized = normalizeIndianPhoneNumber(phoneNumber);
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
        const rateCheck = await RateLimitService.checkAndEnforceRateLimit(db, phoneKey);
        if (!rateCheck.allowed) {
          res.status(429).json({
            success: false,
            code: "TOO_MANY_REQUESTS",
            message: rateCheck.reason
          });
          return;
        }

        // 2. Existing User Registration Check
        const existingUser = await FirebaseUserService.findUserByPhone(db, e164, phoneKey);

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
        const apiKey = twoFactorApiKeySecret.value() || process.env.TWO_FACTOR_API_KEY || "";
        const { sessionId } = await TwoFactorService.sendSmsOtp(apiKey, phoneKey);

        // 4. Save Session Reference in Firestore
        const now = Date.now();
        await db.collection("otp_sessions").doc(sessionId).set({
          sessionId,
          phoneNumber: e164,
          phoneKey,
          isRegistration,
          attempts: 0,
          createdAt: admin.firestore.FieldValue.serverTimestamp(),
          expiresAt: new Date(now + TWO_FACTOR_CONFIG.SESSION_EXPIRY_MINUTES * 60 * 1000)
        });

        // 5. Record Rate Limit
        await RateLimitService.recordOtpSent(db, phoneKey);

        res.status(200).json({
          success: true,
          sessionId,
          message: "SMS verification code sent successfully."
        });
      } catch (err: any) {
        logger.error("sendOtp error:", err.message);
        res.status(500).json({
          success: false,
          code: "INTERNAL_ERROR",
          message: err.message || "Failed to send SMS verification code."
        });
      }
    });
  }
);
