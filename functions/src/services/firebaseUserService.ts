/**
 * Firebase User & Firestore Profile Management Service
 * Manages Firebase Auth user resolution and Firestore profile document.
 */

import * as admin from "firebase-admin";
import { UserRole, AccountStatus, UserProfileDocument } from "../types";

export class FirebaseUserService {
  /**
   * Look up existing user in Firestore by phone number.
   */
  public static async findUserByPhone(
    db: admin.firestore.Firestore,
    e164: string,
    phoneKey: string
  ): Promise<UserProfileDocument | null> {
    const snapshot = await db.collection("users")
      .where("phoneNumber", "in", [e164, phoneKey])
      .limit(1)
      .get();

    if (snapshot.empty) {
      return null;
    }

    const doc = snapshot.docs[0];
    const data = doc.data();
    return {
      uid: doc.id,
      fullName: data.fullName || "",
      phoneNumber: data.phoneNumber || e164,
      role: (data.role as UserRole) || "ELECTRICIAN",
      accountStatus: (data.accountStatus as AccountStatus) || "PENDING",
      electricianId: data.electricianId || null,
      area: data.area || "",
      businessName: data.businessName || null,
      profileImageUrl: data.profileImageUrl || null,
      createdAt: data.createdAt,
      approvedAt: data.approvedAt || null,
      lastLoginAt: data.lastLoginAt
    };
  }

  /**
   * Creates or resolves a Firebase Auth user, and mints a Custom Token.
   */
  public static async createCustomTokenForUser(
    uid: string,
    role: UserRole,
    accountStatus: AccountStatus,
    phoneNumber: string
  ): Promise<string> {
    // Ensure Firebase Auth user exists
    try {
      await admin.auth().getUser(uid);
    } catch (err: any) {
      if (err.code === "auth/user-not-found") {
        await admin.auth().createUser({
          uid,
          phoneNumber
        });
      } else {
        throw err;
      }
    }

    // Mint Firebase Custom Token with claims
    return await admin.auth().createCustomToken(uid, {
      role,
      accountStatus
    });
  }

  /**
   * Updates user lastLoginAt in Firestore.
   */
  public static async updateLastLogin(
    db: admin.firestore.Firestore,
    uid: string
  ): Promise<void> {
    await db.collection("users").doc(uid).update({
      lastLoginAt: admin.firestore.FieldValue.serverTimestamp()
    });
  }
}
