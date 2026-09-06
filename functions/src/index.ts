/**
 * Electricians App - Production Cloud Functions Entry Point
 * 
 * Modular structure:
 * - sendOtp: Dispatches SMS OTP via 2Factor.in
 * - verifyOtp: Verifies SMS OTP and generates Firebase Custom Token
 * - registerUser: Handles electrician onboarding SMS OTP
 * - verifyRegistrationOtp: Verifies registration OTP and provisions PENDING account
 */

import * as admin from "firebase-admin";

// Initialize Firebase Admin SDK once at the root
if (!admin.apps.length) {
  admin.initializeApp();
}

export { sendOtpHandler as sendOtp } from "./auth/sendOtp";
export { verifyOtpHandler as verifyOtp } from "./auth/verifyOtp";
export { registerUserHandler as registerUser, verifyRegistrationOtpHandler as verifyRegistrationOtp } from "./auth/registration";
