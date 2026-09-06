/**
 * Electricians App - Shared Backend Types
 */

export type UserRole = "ELECTRICIAN" | "STAFF" | "OWNER";
export type AccountStatus = "PENDING" | "APPROVED" | "REJECTED" | "INACTIVE";

export interface UserProfileDocument {
  uid: string;
  fullName: string;
  phoneNumber: string;
  role: UserRole;
  accountStatus: AccountStatus;
  electricianId?: string | null;
  area: string;
  businessName?: string | null;
  profileImageUrl?: string | null;
  createdAt: any;
  approvedAt?: any | null;
  lastLoginAt: any;
}

export interface RegistrationRequestDocument {
  registrationId: string;
  uid: string;
  fullName: string;
  phoneNumber: string;
  area: string;
  electricianId?: string | null;
  businessName?: string | null;
  referralCode?: string | null;
  status: "PENDING" | "APPROVED" | "REJECTED";
  createdAt: any;
  reviewedAt?: any | null;
  reviewedBy?: string | null;
  rejectionReason?: string | null;
}

export interface TwoFactorApiResponse {
  Status: "Success" | "Error";
  Details: string;
}

export interface SendOtpRequestBody {
  phoneNumber: string;
  isRegistration?: boolean;
}

export interface VerifyOtpRequestBody {
  phoneNumber: string;
  otp: string;
  sessionId: string;
}

export interface RegisterUserRequestBody {
  fullName: string;
  phoneNumber: string;
  area: string;
  electricianId?: string;
  businessName?: string;
  referralCode?: string;
}

export interface VerifyRegistrationOtpRequestBody {
  otp: string;
  sessionId: string;
}
