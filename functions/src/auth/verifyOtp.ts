/**
 * verifyOtp Cloud Function
 * Verifies 6-digit SMS OTP with 2Factor.in, validates session and attempts,
 * loads user profile from Firestore, creates Firebase Custom Token, and returns token to Android.
 */

import * as admin from "firebase-admin";
import { onRequest } from "firebase-functions/v2/https";
import * as logger from "firebase-functions/logger";
import cors from "cors";
import { twoFactorApiKeySecret, TWO_FACTOR_CONFIG } from "../config";
import { normalizeIndianPhoneNumber } from "../utils/phoneUtils";
import { TwoFactorService } from "../services/twoFactorService";
import { FirebaseUserService } from "../services/firebaseUserService";

const corsHandler = cors({ origin: true });

export const verifyOtpHandler = onRequest(
  { secrets: [twoFactorApiKeySecret], region: "asia-south1", timeoutSeconds: 30 },
  (req, res) => {
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

        const normalized = normalizeIndianPhoneNumber(phoneNumber);
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

        const sessionData = sessionDoc.data()!;
        const attempts = sessionData.attempts || 0;

        // Abuse & Brute-Force Protection
        if (attempts >= TWO_FACTOR_CONFIG.MAX_ATTEMPTS_PER_SESSION) {
          await sessionRef.delete();
          res.status(429).json({
            success: false,
            code: "TOO_MANY_ATTEMPTS",
            message: "Too many incorrect attempts. Please request a fresh OTP."
          });
          return;
        }

        // Verify with 2Factor.in Official SMS API
        const apiKey = twoFactorApiKeySecret.value() || process.env.TWO_FACTOR_API_KEY || "";
        const verification = await TwoFactorService.verifySmsOtp(apiKey, sessionId, otp.trim());

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
        const user = await FirebaseUserService.findUserByPhone(db, e164, phoneKey);

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
        const customToken = await FirebaseUserService.createCustomTokenForUser(
          user.uid,
          user.role,
          user.accountStatus,
          user.phoneNumber
        );

        // Update last login
        await FirebaseUserService.updateLastLogin(db, user.uid);

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
      } catch (err: any) {
        logger.error("verifyOtp error:", err.message);
        res.status(500).json({
          success: false,
          code: "INTERNAL_ERROR",
          message: err.message || "Failed to verify OTP."
        });
      }
    });
  }
);
