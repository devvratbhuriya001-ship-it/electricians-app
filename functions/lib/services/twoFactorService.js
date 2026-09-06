"use strict";
/**
 * Electricians App - 2Factor.in Official SMS OTP Service
 *
 * Strict SMS OTP Only. No Voice OTP.
 * API Key is provided only from secure server-side secrets.
 */
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.TwoFactorService = void 0;
const axios_1 = __importDefault(require("axios"));
const config_1 = require("../config");
class TwoFactorService {
    /**
     * Calls 2Factor.in Official SMS OTP endpoint:
     * GET https://2factor.in/API/V1/{API_KEY}/SMS/{phone_number}/AUTOGEN
     *
     * Returns the official Session ID (Details) on success.
     */
    static async sendSmsOtp(apiKey, phoneNumber10Digits) {
        if (!apiKey) {
            throw new Error("2Factor API key is not configured on the server.");
        }
        const endpoint = config_1.TWO_FACTOR_CONFIG.SMS_AUTOGEN_URL(apiKey, phoneNumber10Digits);
        try {
            const response = await axios_1.default.get(endpoint, {
                timeout: 10000,
                headers: {
                    "Accept": "application/json"
                }
            });
            const data = response.data;
            if (data.Status === "Success" && data.Details) {
                return { sessionId: data.Details };
            }
            else {
                // Sanitize error without exposing endpoint or apiKey
                const errorMessage = data.Details || "Failed to send SMS OTP.";
                throw new Error(errorMessage);
            }
        }
        catch (err) {
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
    static async verifySmsOtp(apiKey, sessionId, otp) {
        if (!apiKey) {
            throw new Error("2Factor API key is not configured on the server.");
        }
        const endpoint = config_1.TWO_FACTOR_CONFIG.SMS_VERIFY_URL(apiKey, sessionId, otp.trim());
        try {
            const response = await axios_1.default.get(endpoint, {
                timeout: 10000,
                headers: {
                    "Accept": "application/json"
                }
            });
            const data = response.data;
            if (data.Status === "Success" && data.Details === "OTP Matched") {
                return { matched: true };
            }
            else {
                const details = data.Details || "";
                if (details.toLowerCase().includes("mismatch")) {
                    return { matched: false, errorReason: "Incorrect OTP. Please check and try again." };
                }
                else if (details.toLowerCase().includes("expired")) {
                    return { matched: false, errorReason: "OTP has expired. Please request a new code." };
                }
                else {
                    return { matched: false, errorReason: details || "Verification failed." };
                }
            }
        }
        catch (err) {
            const details = err.response?.data?.Details || "";
            if (details.toLowerCase().includes("mismatch")) {
                return { matched: false, errorReason: "Incorrect OTP. Please check and try again." };
            }
            else if (details.toLowerCase().includes("expired")) {
                return { matched: false, errorReason: "OTP has expired. Please request a new code." };
            }
            throw new Error("Unable to verify OTP with provider. Please try again.");
        }
    }
}
exports.TwoFactorService = TwoFactorService;
//# sourceMappingURL=twoFactorService.js.map