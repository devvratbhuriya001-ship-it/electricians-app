"use strict";
/**
 * Server-Side Rate Limiting Service
 * Enforces per-phone cooldown and hourly OTP limits in Firestore.
 */
Object.defineProperty(exports, "__esModule", { value: true });
exports.RateLimitService = void 0;
class RateLimitService {
    static async checkAndEnforceRateLimit(db, phoneKey) {
        const rateLimitRef = db.collection("otp_rate_limits").doc(phoneKey);
        const doc = await rateLimitRef.get();
        const now = Date.now();
        if (!doc.exists) {
            return { allowed: true };
        }
        const data = doc.data();
        const lastSent = data.lastSent || 0;
        const countHour = data.countHour || 0;
        const hourWindowStart = data.hourWindowStart || 0;
        // 1. Cooldown check (60s)
        const elapsedSeconds = (now - lastSent) / 1000;
        if (elapsedSeconds < this.COOLDOWN_SECONDS) {
            const waitSeconds = Math.ceil(this.COOLDOWN_SECONDS - elapsedSeconds);
            return {
                allowed: false,
                waitSeconds,
                reason: `Please wait ${waitSeconds}s before requesting another OTP.`
            };
        }
        // 2. Hourly check (max 5)
        if (now - hourWindowStart < 3600 * 1000) {
            if (countHour >= this.MAX_REQUESTS_PER_HOUR) {
                return {
                    allowed: false,
                    reason: "Maximum OTP requests reached for this hour. Please try again later."
                };
            }
        }
        return { allowed: true };
    }
    static async recordOtpSent(db, phoneKey) {
        const rateLimitRef = db.collection("otp_rate_limits").doc(phoneKey);
        const doc = await rateLimitRef.get();
        const now = Date.now();
        let countHour = 1;
        let hourWindowStart = now;
        if (doc.exists) {
            const data = doc.data();
            if (now - (data.hourWindowStart || 0) < 3600 * 1000) {
                countHour = (data.countHour || 0) + 1;
                hourWindowStart = data.hourWindowStart || now;
            }
        }
        await rateLimitRef.set({
            phoneKey,
            lastSent: now,
            countHour,
            hourWindowStart
        });
    }
}
exports.RateLimitService = RateLimitService;
RateLimitService.COOLDOWN_SECONDS = 60;
RateLimitService.MAX_REQUESTS_PER_HOUR = 5;
//# sourceMappingURL=rateLimitService.js.map