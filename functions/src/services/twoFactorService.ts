/**
 * Electricians App - 2Factor.in Official SMS OTP Service
 * 
 * Strict SMS OTP Only. No Voice OTP.
 * API Key is provided only from secure server-side secrets.
 */

import axios from "axios";
import { TWO_FACTOR_CONFIG } from "../config";

interface TwoFactorResponse {
  Status: "Success" | "Error";
  Details: string;
}

export class TwoFactorService {
  /**
   * Calls 2Factor.in Official SMS OTP endpoint:
   * GET https://2factor.in/API/V1/{API_KEY}/SMS/{phone_number}/AUTOGEN
   * 
   * Returns the official Session ID (Details) on success.
   */
  public static async sendSmsOtp(
    apiKey: string,
    phoneNumber10Digits: string
  ): Promise<{ sessionId: string }> {
    if (!apiKey) {
      throw new Error("2Factor API key is not configured on the server.");
    }

    const endpoint = TWO_FACTOR_CONFIG.SMS_AUTOGEN_URL(apiKey, phoneNumber10Digits);

    try {
      const response = await axios.get<TwoFactorResponse>(endpoint, {
        timeout: 10000,
        headers: {
          "Accept": "application/json"
        }
      });

      const data = response.data;

      if (data.Status === "Success" && data.Details) {
        return { sessionId: data.Details };
      } else {
        // Sanitize error without exposing endpoint or apiKey
        const errorMessage = data.Details || "Failed to send SMS OTP.";
        throw new Error(errorMessage);
      }
    } catch (err: any) {
      // Never log or leak full URL or apiKey
      if (err.response?.data?.Details) {
        throw new Error(err.response.data.Details);
      }
      throw new Error("SMS delivery service is currently unavailable. Please try again shortly.");
    }
  }

  /**
   * Calls 2Factor.in Official SMS OTP Verification endpoint:
   * GET https://2factor.in/API/V1/{API_KEY}/SMS/VERIFY/{session_id}/{otp}
   * 
   * Validates official OTP match response.
   */
  public static async verifySmsOtp(
    apiKey: string,
    sessionId: string,
    otp: string
  ): Promise<{ matched: boolean; errorReason?: string }> {
    if (!apiKey) {
      throw new Error("2Factor API key is not configured on the server.");
    }

    const endpoint = TWO_FACTOR_CONFIG.SMS_VERIFY_URL(apiKey, sessionId, otp.trim());

    try {
      const response = await axios.get<TwoFactorResponse>(endpoint, {
        timeout: 10000,
        headers: {
          "Accept": "application/json"
        }
      });

      const data = response.data;

      if (data.Status === "Success" && data.Details === "OTP Matched") {
        return { matched: true };
      } else {
        const details = data.Details || "";
        if (details.toLowerCase().includes("mismatch")) {
          return { matched: false, errorReason: "Incorrect OTP. Please check and try again." };
        } else if (details.toLowerCase().includes("expired")) {
          return { matched: false, errorReason: "OTP has expired. Please request a new code." };
        } else {
          return { matched: false, errorReason: details || "Verification failed." };
        }
      }
    } catch (err: any) {
      const details = err.response?.data?.Details || "";
      if (details.toLowerCase().includes("mismatch")) {
        return { matched: false, errorReason: "Incorrect OTP. Please check and try again." };
      } else if (details.toLowerCase().includes("expired")) {
        return { matched: false, errorReason: "OTP has expired. Please request a new code." };
      }
      throw new Error("Unable to verify OTP with provider. Please try again.");
    }
  }
}
