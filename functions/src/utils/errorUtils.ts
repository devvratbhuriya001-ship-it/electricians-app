/**
 * Backend Error Normalization Utilities
 * Sanitizes errors so that internal secrets, 2Factor raw responses,
 * and stack traces are never exposed to Android or client callers.
 */

export const ErrorCodes = {
  INVALID_PHONE: "INVALID_PHONE",
  USER_NOT_REGISTERED: "USER_NOT_REGISTERED",
  USER_ALREADY_REGISTERED: "USER_ALREADY_REGISTERED",
  INVALID_OTP: "INVALID_OTP",
  OTP_EXPIRED: "OTP_EXPIRED",
  TOO_MANY_ATTEMPTS: "TOO_MANY_ATTEMPTS",
  TOO_MANY_REQUESTS: "TOO_MANY_REQUESTS",
  OTP_PROVIDER_UNAVAILABLE: "OTP_PROVIDER_UNAVAILABLE",
  INTERNAL_ERROR: "INTERNAL_ERROR"
} as const;

export function sanitizeError(err: any): { code: string; message: string } {
  const msg = (err?.message || "").toLowerCase();

  if (msg.includes("not registered")) {
    return {
      code: ErrorCodes.USER_NOT_REGISTERED,
      message: "This mobile number is not registered. Please register first."
    };
  }

  if (msg.includes("already registered")) {
    return {
      code: ErrorCodes.USER_ALREADY_REGISTERED,
      message: "An account with this mobile number already exists. Please log in."
    };
  }

  if (msg.includes("mismatch") || msg.includes("incorrect")) {
    return {
      code: ErrorCodes.INVALID_OTP,
      message: "The OTP you entered is incorrect."
    };
  }

  if (msg.includes("expired")) {
    return {
      code: ErrorCodes.OTP_EXPIRED,
      message: "This OTP has expired. Please request a new one."
    };
  }

  if (msg.includes("too many attempts") || msg.includes("attempt limit")) {
    return {
      code: ErrorCodes.TOO_MANY_ATTEMPTS,
      message: "Too many incorrect attempts. Please request a fresh OTP."
    };
  }

  if (msg.includes("rate limit") || msg.includes("too many requests") || msg.includes("wait")) {
    return {
      code: ErrorCodes.TOO_MANY_REQUESTS,
      message: "Too many requests. Please wait before requesting another OTP."
    };
  }

  if (msg.includes("unavailable") || msg.includes("timeout") || msg.includes("network")) {
    return {
      code: ErrorCodes.OTP_PROVIDER_UNAVAILABLE,
      message: "SMS delivery service is currently unavailable. Please try again shortly."
    };
  }

  return {
    code: ErrorCodes.INTERNAL_ERROR,
    message: "An unexpected error occurred. Please try again."
  };
}
