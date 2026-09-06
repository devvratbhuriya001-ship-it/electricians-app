/**
 * Electricians App - Cloud Functions Configuration
 * Production SMS OTP Architecture via 2Factor.in
 */

import { defineSecret } from "firebase-functions/params";

// Secure Firebase Functions Secret definition
// The 2Factor API Key NEVER leaves the server-side runtime
export const twoFactorApiKeySecret = defineSecret("TWO_FACTOR_API_KEY");

// Official 2Factor.in SMS OTP API Endpoints
// Note: ONLY SMS OTP is used. Voice OTP is strictly forbidden.
export const TWO_FACTOR_CONFIG = {
  BASE_URL: "https://2factor.in/API/V1",
  // SMS OTP generation endpoint:
  // GET https://2factor.in/API/V1/{API_KEY}/SMS/{phone_number}/AUTOGEN
  SMS_AUTOGEN_URL: (apiKey: string, phoneNumber: string) =>
    `https://2factor.in/API/V1/${apiKey}/SMS/${phoneNumber}/AUTOGEN`,
  
  // SMS OTP verification endpoint:
  // GET https://2factor.in/API/V1/{API_KEY}/SMS/VERIFY/{session_id}/{otp_value}
  SMS_VERIFY_URL: (apiKey: string, sessionId: string, otp: string) =>
    `https://2factor.in/API/V1/${apiKey}/SMS/VERIFY/${sessionId}/${otp}`,

  // Rate Limiting & Cooldown rules
  COOLDOWN_SECONDS: 60,
  MAX_REQUESTS_PER_HOUR: 5,
  MAX_ATTEMPTS_PER_SESSION: 5,
  SESSION_EXPIRY_MINUTES: 10
};

/**
 * Normalizes and strictly validates Indian mobile numbers.
 * Allowed: 10-digit number optionally prefixed with +91, 91, or 0.
 * Must start with 6, 7, 8, or 9.
 */
export function normalizeIndianPhoneNumber(rawPhone: string): { isValid: boolean; clean10Digits: string; e164: string } {
  if (!rawPhone) return { isValid: false, clean10Digits: "", e164: "" };

  // Remove whitespace, dashes, parens, plus
  let digits = rawPhone.replace(/[\s\-\(\)\+]/g, "");

  // Remove leading 91 or 0 if present
  if (digits.startsWith("91") && digits.length === 12) {
    digits = digits.substring(2);
  } else if (digits.startsWith("0") && digits.length === 11) {
    digits = digits.substring(1);
  }

  // Validate exact 10 digits starting with 6, 7, 8, or 9
  const isValid = /^[6-9]\d{9}$/.test(digits);

  return {
    isValid,
    clean10Digits: isValid ? digits : "",
    e164: isValid ? `+91${digits}` : ""
  };
}
