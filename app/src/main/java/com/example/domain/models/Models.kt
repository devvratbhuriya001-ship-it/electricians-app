package com.example.domain.models

enum class UserRole {
    ELECTRICIAN,
    STAFF,
    OWNER
}

enum class AccountStatus {
    PENDING,
    APPROVED,
    REJECTED,
    INACTIVE
}

enum class RewardStatus {
    LOCKED,
    IN_PROGRESS,
    UNLOCKED,
    CLAIMED
}

enum class TransactionStatus {
    PENDING,
    VERIFIED,
    REJECTED,
    ADJUSTED
}

enum class NotificationType {
    ACHIEVEMENT_UPDATE,
    REWARD_PROGRESS,
    REWARD_UNLOCKED,
    TRANSACTION_VERIFIED,
    TRANSACTION_REJECTED,
    RANK_UPDATE,
    SCHEME_UPDATE,
    GENERAL,
    ACCOUNT_APPROVED
}

data class User(
    val uid: String,
    val fullName: String,
    val phoneNumber: String,
    val role: UserRole,
    val accountStatus: AccountStatus,
    val electricianId: String? = null,
    val area: String,
    val businessName: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val approvedAt: Long? = null,
    val lastLoginAt: Long = System.currentTimeMillis(),
    val rejectionReason: String? = null
)

data class DashboardSummary(
    val electricianId: String,
    val currentAchievement: Long, // in INR (e.g. 3275000)
    val targetAmount: Long, // in INR (e.g. 5000000)
    val nextRewardName: String,
    val nextRewardImageUrl: String? = null,
    val progressPercentage: Int, // e.g. 65
    val monthlyAchievement: Long, // e.g. 485000
    val monthlyGrowthPercent: Int, // e.g. 18
    val rank: Int, // e.g. 47
    val totalElectriciansCount: Int = 3248,
    val remainingAmount: Long, // e.g. 1725000
    val schemeYear: Int = 2026,
    val motivationalQuote: String = "Keep going! Every purchase takes you closer to your next reward."
)

data class ElectricianProfile(
    val uid: String,
    val electricianId: String,
    val fullName: String,
    val phoneNumber: String,
    val area: String,
    val businessName: String?,
    val accountStatus: AccountStatus,
    val memberSince: String,
    val profileImageUrl: String? = null,
    val lifetimeAchievement: Long,
    val currentYearAchievement: Long,
    val rewardsWonCount: Int,
    val currentRank: Int
)

data class StaffProfile(
    val uid: String,
    val staffId: String,
    val fullName: String,
    val phoneNumber: String,
    val department: String = "Verification Team",
    val verifiedCountToday: Int = 14,
    val pendingCount: Int = 8,
    val rejectedCountToday: Int = 1,
    val totalAssigned: Int = 23
)

data class Scheme(
    val schemeId: String,
    val title: String,
    val year: Int,
    val startDate: String,
    val endDate: String,
    val active: Boolean,
    val description: String
)

data class Reward(
    val rewardId: String,
    val name: String,
    val description: String,
    val targetAmount: Long,
    val imageUrl: String? = null,
    val status: RewardStatus = RewardStatus.LOCKED,
    val displayOrder: Int = 1,
    val year: Int = 2026,
    val eligibility: String = "Applicable on verified billing purchases in scheme year.",
    val validityPeriod: String = "Valid till 31 Dec 2026"
)

data class Transaction(
    val transactionId: String,
    val electricianId: String,
    val electricianName: String = "",
    val amount: Long,
    val invoiceNumber: String,
    val invoiceDate: String,
    val createdAt: Long = System.currentTimeMillis(),
    val createdBy: String,
    val verifiedBy: String? = null,
    val verifiedAt: Long? = null,
    val status: TransactionStatus,
    val rejectionReason: String? = null,
    val notes: String? = null,
    val proofImageUrl: String? = null
)

data class MonthlyBreakdown(
    val monthName: String,
    val shortName: String,
    val amount: Long,
    val invoiceCount: Int,
    val isCurrentMonth: Boolean = false
)

data class LeaderboardEntry(
    val rank: Int,
    val electricianId: String,
    val name: String,
    val area: String,
    val achievementTier: String,
    val isCurrentUser: Boolean = false,
    val achievementAmountFormatted: String? = null
)

data class AppNotification(
    val notificationId: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val createdAt: Long = System.currentTimeMillis(),
    val read: Boolean = false,
    val targetRoute: String? = null
)

data class RegistrationRequest(
    val requestId: String,
    val fullName: String,
    val phoneNumber: String,
    val area: String,
    val businessName: String?,
    val electricianId: String?,
    val referralCode: String?,
    val submissionDate: String,
    val status: AccountStatus = AccountStatus.PENDING,
    val rejectionReason: String? = null
)

data class OwnerDashboardMetrics(
    val totalElectricians: Int = 3248,
    val approvedElectricians: Int = 3190,
    val pendingRegistrations: Int = 18,
    val activeElectricians: Int = 2840,
    val totalAnnualAchievement: Long = 428000000L, // ₹42.80 Cr
    val pendingVerifications: Int = 24,
    val rewardsUnlocked: Int = 412,
    val totalTargetVolume: Long = 600000000L,
    val currentMonthVolume: Long = 48500000L,
    val rewardsClaimedCost: Long = 18500000L
) {
    val totalVerifiedVolume: Long get() = totalAnnualAchievement
    val pendingRegistrationsCount: Int get() = pendingRegistrations
}

typealias BusinessStats = OwnerDashboardMetrics

