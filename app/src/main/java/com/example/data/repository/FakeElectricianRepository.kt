package com.example.data.repository

import com.example.domain.models.*
import com.example.domain.repository.ElectricianRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class FakeElectricianRepository(
    private val store: AppDatabaseStore = AppDatabaseStore
) : ElectricianRepository {

    override fun getDashboardSummary(): Flow<DashboardSummary> {
        return combine(store.transactions, store.rewards) { transactions, rewards ->
            val verifiedTotal = transactions
                .filter { it.status == TransactionStatus.VERIFIED && it.electricianId == "ELC-2026-047" }
                .sumOf { it.amount }

            val totalAchievement = if (verifiedTotal > 0) verifiedTotal + 2346500L else 3275000L
            val targetAmount = 5000000L // ₹50L for Bike
            val progressPercent = ((totalAchievement.toDouble() / targetAmount.toDouble()) * 100).toInt().coerceIn(0, 100)
            val remaining = (targetAmount - totalAchievement).coerceAtLeast(0L)

            DashboardSummary(
                electricianId = "ELC-2026-047",
                currentAchievement = totalAchievement,
                targetAmount = targetAmount,
                nextRewardName = "Brand New 125cc Motorbike",
                nextRewardImageUrl = null,
                progressPercentage = progressPercent,
                monthlyAchievement = 485000L,
                monthlyGrowthPercent = 18,
                rank = 47,
                totalElectriciansCount = 3248,
                remainingAmount = remaining,
                schemeYear = 2026,
                motivationalQuote = "You're ₹${com.example.core.utils.CurrencyFormatter.formatInr(remaining, false)} away from your next reward."
            )
        }
    }

    override fun getRewards(): Flow<List<Reward>> = store.rewards

    override fun getRewardDetail(rewardId: String): Flow<Reward?> {
        return store.rewards.map { list -> list.find { it.rewardId == rewardId } }
    }

    override fun getTransactions(statusFilter: TransactionStatus?, searchQuery: String): Flow<List<Transaction>> {
        return store.transactions.map { list ->
            list.filter { tx ->
                val matchesUser = tx.electricianId == "ELC-2026-047"
                val matchesStatus = statusFilter == null || tx.status == statusFilter
                val matchesQuery = searchQuery.isBlank() ||
                        tx.invoiceNumber.contains(searchQuery, ignoreCase = true) ||
                        tx.notes?.contains(searchQuery, ignoreCase = true) == true
                matchesUser && matchesStatus && matchesQuery
            }
        }
    }

    override fun getTransactionDetail(transactionId: String): Flow<Transaction?> {
        return store.transactions.map { list -> list.find { it.transactionId == transactionId } }
    }

    override fun getMonthlyBreakdown(): Flow<List<MonthlyBreakdown>> {
        return kotlinx.coroutines.flow.flowOf(store.monthlyBreakdowns)
    }

    override fun getLeaderboard(isYearly: Boolean): Flow<List<LeaderboardEntry>> {
        return kotlinx.coroutines.flow.flowOf(store.leaderboardEntries)
    }

    override fun getProfile(): Flow<ElectricianProfile> {
        return store.electricianProfiles.map { list ->
            list.find { it.electricianId == "ELC-2026-047" } ?: list.first()
        }
    }

    override suspend fun refreshData() {
        delay(400)
    }
}
