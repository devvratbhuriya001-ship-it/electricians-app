"use strict";
/**
 * Electricians App - Production Cloud Functions Entry Point
 *
 * Modular structure:
 * - sendOtp: Dispatches SMS OTP via 2Factor.in
 * - verifyOtp: Verifies SMS OTP and generates Firebase Custom Token
 * - registerUser: Handles electrician onboarding SMS OTP
 * - verifyRegistrationOtp: Verifies registration OTP and provisions PENDING account
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
Object.defineProperty(exports, "__esModule", { value: true });
exports.verifyRegistrationOtp = exports.registerUser = exports.verifyOtp = exports.sendOtp = void 0;
const admin = __importStar(require("firebase-admin"));
// Initialize Firebase Admin SDK once at the root
if (!admin.apps.length) {
    admin.initializeApp();
}
var sendOtp_1 = require("./auth/sendOtp");
Object.defineProperty(exports, "sendOtp", { enumerable: true, get: function () { return sendOtp_1.sendOtpHandler; } });
var verifyOtp_1 = require("./auth/verifyOtp");
Object.defineProperty(exports, "verifyOtp", { enumerable: true, get: function () { return verifyOtp_1.verifyOtpHandler; } });
var registration_1 = require("./auth/registration");
Object.defineProperty(exports, "registerUser", { enumerable: true, get: function () { return registration_1.registerUserHandler; } });
Object.defineProperty(exports, "verifyRegistrationOtp", { enumerable: true, get: function () { return registration_1.verifyRegistrationOtpHandler; } });
//# sourceMappingURL=index.js.map