package com.example.domain.repository

import com.example.domain.models.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun sendOtp(phoneNumber: String): Result<Boolean>
    suspend fun verifyOtp(phoneNumber: String, otp: String): Result<User>
    suspend fun registerUser(
        fullName: String,
        phoneNumber: String,
        area: String,
        electricianId: String?,
        businessName: String?,
        referralCode: String?
    ): Result<RegistrationRequest>
    suspend fun verifyRegistrationOtp(phoneNumber: String, otp: String): Result<RegistrationRequest>
    suspend fun refreshUserStatus(): Result<User?>
    suspend fun logout()
    suspend fun switchRoleForTesting(role: UserRole, status: AccountStatus = AccountStatus.APPROVED)
}

interface ElectricianRepository {
    fun getDashboardSummary(): Flow<DashboardSummary>
    fun getRewards(): Flow<List<Reward>>
    fun getRewardDetail(rewardId: String): Flow<Reward?>
    fun getTransactions(statusFilter: TransactionStatus? = null, searchQuery: String = ""): Flow<List<Transaction>>
    fun getTransactionDetail(transactionId: String): Flow<Transaction?>
    fun getMonthlyBreakdown(): Flow<List<MonthlyBreakdown>>
    fun getLeaderboard(isYearly: Boolean): Flow<List<LeaderboardEntry>>
    fun getProfile(): Flow<ElectricianProfile>
    suspend fun refreshData()
}

interface StaffRepository {
    fun getStaffProfile(): Flow<StaffProfile>
    fun getPendingVerifications(): Flow<List<Transaction>>
    suspend fun verifyTransaction(transactionId: String, staffId: String): Result<Unit>
    suspend fun rejectTransaction(transactionId: String, staffId: String, reason: String): Result<Unit>
    fun searchElectricians(query: String): Flow<List<ElectricianProfile>>
    fun getElectricianDetail(electricianId: String): Flow<ElectricianProfile?>
    fun getVerificationHistory(filter: String = "ALL"): Flow<List<Transaction>>
    suspend fun createTransaction(
        electricianId: String,
        amount: Long,
        invoiceNumber: String,
        invoiceDate: String,
        notes: String?
    ): Result<Transaction>
}

interface OwnerRepository {
    fun getOwnerDashboardMetrics(): Flow<OwnerDashboardMetrics>
    fun getAllElectricians(searchQuery: String = "", statusFilter: AccountStatus? = null): Flow<List<ElectricianProfile>>
    fun getElectricianDetail(electricianId: String): Flow<ElectricianProfile?>
    fun getPendingRegistrations(): Flow<List<RegistrationRequest>>
    suspend fun approveRegistration(requestId: String, assignedRole: UserRole): Result<Unit>
    suspend fun rejectRegistration(requestId: String, reason: String): Result<Unit>
    fun getSchemes(): Flow<List<Scheme>>
    suspend fun createScheme(title: String, year: Int, startDate: String, endDate: String, description: String): Result<Scheme>
    fun getRewards(): Flow<List<Reward>>
    suspend fun addReward(name: String, targetAmount: Long, description: String, eligibility: String): Result<Reward>
    suspend fun updateReward(reward: Reward): Result<Reward>
    suspend fun deleteReward(rewardId: String): Result<Unit>
    suspend fun adjustElectricianAchievement(electricianId: String, adjustedAmount: Long, reason: String): Result<Unit>
    suspend fun toggleUserStatus(uid: String, activate: Boolean): Result<Unit>
    suspend fun changeUserRole(uid: String, newRole: UserRole): Result<Unit>
    suspend fun addOwnerTransaction(
        electricianId: String,
        amount: Long,
        invoiceNumber: String,
        invoiceDate: String,
        notes: String?,
        autoVerify: Boolean
    ): Result<Transaction>
}

interface NotificationRepository {
    fun getNotifications(userId: String): Flow<List<AppNotification>>
    suspend fun markAsRead(notificationId: String): Result<Unit>
    suspend fun markAllAsRead(userId: String): Result<Unit>
}
