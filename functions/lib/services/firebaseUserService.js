"use strict";
/**
 * Firebase User & Firestore Profile Management Service
 * Manages Firebase Auth user resolution and Firestore profile document.
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
exports.FirebaseUserService = void 0;
const admin = __importStar(require("firebase-admin"));
class FirebaseUserService {
    /**
     * Look up existing user in Firestore by phone number.
     */
    static async findUserByPhone(db, e164, phoneKey) {
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
            role: data.role || "ELECTRICIAN",
            accountStatus: data.accountStatus || "PENDING",
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
    static async createCustomTokenForUser(uid, role, accountStatus, phoneNumber) {
        // Ensure Firebase Auth user exists
        try {
            await admin.auth().getUser(uid);
        }
        catch (err) {
            if (err.code === "auth/user-not-found") {
                await admin.auth().createUser({
                    uid,
                    phoneNumber
                });
            }
            else {
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
    static async updateLastLogin(db, uid) {
        await db.collection("users").doc(uid).update({
            lastLoginAt: admin.firestore.FieldValue.serverTimestamp()
        });
    }
}
exports.FirebaseUserService = FirebaseUserService;
//# sourceMappingURL=firebaseUserService.js.map