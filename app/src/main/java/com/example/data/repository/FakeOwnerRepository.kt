package com.example.data.repository

import com.example.domain.models.*
import com.example.domain.repository.OwnerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.UUID

class FakeOwnerRepository(
    private val store: AppDatabaseStore = AppDatabaseStore
) : OwnerRepository {

    override fun getOwnerDashboardMetrics(): Flow<OwnerDashboardMetrics> {
        return combine(store.electricianProfiles, store.transactions, store.registrationRequests) { profiles, txs, reqs ->
            val totalElec = profiles.size + 3240
            val approved = profiles.count { it.accountStatus == AccountStatus.APPROVED } + 3180
            val pendingReq = reqs.count { it.status == AccountStatus.PENDING }
            val pendingTxs = txs.count { it.status == TransactionStatus.PENDING }
            val totalAch = txs.filter { it.status == TransactionStatus.VERIFIED }.sumOf { it.amount } + 425000000L

            OwnerDashboardMetrics(
                totalElectricians = totalElec,
                approvedElectricians = approved,
                pendingRegistrations = pendingReq,
                activeElectricians = approved - 350,
                totalAnnualAchievement = totalAch,
                pendingVerifications = pendingTxs,
                rewardsUnlocked = 412
            )
        }
    }

    override fun getAllElectricians(searchQuery: String, statusFilter: AccountStatus?): Flow<List<ElectricianProfile>> {
        return store.electricianProfiles.map { list ->
            list.filter {
                val matchesStatus = statusFilter == null || it.accountStatus == statusFilter
                val matchesSearch = searchQuery.isBlank() ||
                        it.fullName.contains(searchQuery, ignoreCase = true) ||
                        it.electricianId.contains(searchQuery, ignoreCase = true) ||
                        it.phoneNumber.contains(searchQuery) ||
                        it.area.contains(searchQuery, ignoreCase = true)
                matchesStatus && matchesSearch
            }
        }
    }

    override fun getElectricianDetail(electricianId: String): Flow<ElectricianProfile?> {
        return store.electricianProfiles.map { list ->
            list.find { it.electricianId.equals(electricianId, ignoreCase = true) || it.uid == electricianId }
        }
    }

    override fun getPendingRegistrations(): Flow<List<RegistrationRequest>> {
        return store.registrationRequests.map { list ->
            list.filter { it.status == AccountStatus.PENDING }
        }
    }

    override suspend fun approveRegistration(requestId: String, assignedRole: UserRole): Result<Unit> {
        delay(500)
        store.approveRegistration(requestId, assignedRole)
        return Result.success(Unit)
    }

    override suspend fun rejectRegistration(requestId: String, reason: String): Result<Unit> {
        delay(500)
        store.rejectRegistration(requestId, reason)
        return Result.success(Unit)
    }

    override fun getSchemes(): Flow<List<Scheme>> = store.schemes

    override suspend fun createScheme(title: String, year: Int, startDate: String, endDate: String, description: String): Result<Scheme> {
        delay(500)
        val scheme = Scheme(
            schemeId = "sch_${UUID.randomUUID().toString().take(6)}",
            title = title,
            year = year,
            startDate = startDate,
            endDate = endDate,
            active = true,
            description = description
        )
        return Result.success(scheme)
    }

    override fun getRewards(): Flow<List<Reward>> = store.rewards

    override suspend fun addReward(name: String, targetAmount: Long, description: String, eligibility: String): Result<Reward> {
        delay(500)
        val newReward = Reward(
            rewardId = "rew_${UUID.randomUUID().toString().take(6)}",
            name = name,
            description = description,
            targetAmount = targetAmount,
            status = RewardStatus.LOCKED,
            displayOrder = store.rewards.value.size + 1,
            eligibility = eligibility
        )
        store.addReward(newReward)
        return Result.success(newReward)
    }

    override suspend fun updateReward(reward: Reward): Result<Reward> {
        delay(400)
        store.updateReward(reward)
        return Result.success(reward)
    }

    override suspend fun deleteReward(rewardId: String): Result<Unit> {
        delay(400)
        store.deleteReward(rewardId)
        return Result.success(Unit)
    }

    override suspend fun adjustElectricianAchievement(electricianId: String, adjustedAmount: Long, reason: String): Result<Unit> {
        delay(500)
        val adjTx = Transaction(
            transactionId = "tx_adj_${UUID.randomUUID().toString().take(6)}",
            electricianId = electricianId,
            amount = adjustedAmount,
            invoiceNumber = "ADJUSTMENT-AUDIT",
            invoiceDate = "Today",
            createdAt = System.currentTimeMillis(),
            createdBy = "Owner Management",
            verifiedBy = "Owner",
            verifiedAt = System.currentTimeMillis(),
            status = TransactionStatus.ADJUSTED,
            notes = "Owner adjustment: $reason"
        )
        store.addTransaction(adjTx)
        return Result.success(Unit)
    }

    override suspend fun toggleUserStatus(uid: String, activate: Boolean): Result<Unit> {
        delay(400)
        return Result.success(Unit)
    }

    override suspend fun changeUserRole(uid: String, newRole: UserRole): Result<Unit> {
        delay(400)
        return Result.success(Unit)
    }

    override suspend fun addOwnerTransaction(
        electricianId: String,
        amount: Long,
        invoiceNumber: String,
        invoiceDate: String,
        notes: String?,
        autoVerify: Boolean
    ): Result<Transaction> {
        delay(600)
        val electrician = store.electricianProfiles.value.find { it.electricianId == electricianId }
        val newTx = Transaction(
            transactionId = "tx_${UUID.randomUUID().toString().take(6)}",
            electricianId = electricianId,
            electricianName = electrician?.fullName ?: "Electrician",
            amount = amount,
            invoiceNumber = invoiceNumber,
            invoiceDate = invoiceDate,
            createdAt = System.currentTimeMillis(),
            createdBy = "Owner Admin",
            verifiedBy = if (autoVerify) "Owner Admin" else null,
            verifiedAt = if (autoVerify) System.currentTimeMillis() else null,
            status = if (autoVerify) TransactionStatus.VERIFIED else TransactionStatus.PENDING,
            notes = notes
        )
        store.addTransaction(newTx)
        return Result.success(newTx)
    }
}
