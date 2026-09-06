/**
 * Server-Side Rate Limiting Service
 * Enforces per-phone cooldown and hourly OTP limits in Firestore.
 */

import * as admin from "firebase-admin";

export class RateLimitService {
  private static readonly COOLDOWN_SECONDS = 60;
  private static readonly MAX_REQUESTS_PER_HOUR = 5;

  public static async checkAndEnforceRateLimit(
    db: admin.firestore.Firestore,
    phoneKey: string
  ): Promise<{ allowed: boolean; waitSeconds?: number; reason?: string }> {
    const rateLimitRef = db.collection("otp_rate_limits").doc(phoneKey);
    const doc = await rateLimitRef.get();
    const now = Date.now();

    if (!doc.exists) {
      return { allowed: true };
    }

    const data = doc.data()!;
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

  public static async recordOtpSent(
    db: admin.firestore.Firestore,
    phoneKey: string
  ): Promise<void> {
    const rateLimitRef = db.collection("otp_rate_limits").doc(phoneKey);
    const doc = await rateLimitRef.get();
    const now = Date.now();

    let countHour = 1;
    let hourWindowStart = now;

    if (doc.exists) {
      const data = doc.data()!;
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
