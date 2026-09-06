package com.example.data.repository

import com.example.domain.models.*
import com.example.domain.repository.StaffRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class FakeStaffRepository(
    private val store: AppDatabaseStore = AppDatabaseStore
) : StaffRepository {

    override fun getStaffProfile(): Flow<StaffProfile> {
        return store.transactions.map { list ->
            val pending = list.count { it.status == TransactionStatus.PENDING }
            val verifiedToday = list.count { it.status == TransactionStatus.VERIFIED }
            val rejectedToday = list.count { it.status == TransactionStatus.REJECTED }
            StaffProfile(
                uid = "usr_staff_001",
                staffId = "STF-MUM-09",
                fullName = "Karan Shah",
                phoneNumber = "+919811111111",
                department = "Verification Operations",
                verifiedCountToday = verifiedToday,
                pendingCount = pending,
                rejectedCountToday = rejectedToday,
                totalAssigned = pending + verifiedToday + rejectedToday
            )
        }
    }

    override fun getPendingVerifications(): Flow<List<Transaction>> {
        return store.transactions.map { list ->
            list.filter { it.status == TransactionStatus.PENDING }
        }
    }

    override suspend fun verifyTransaction(transactionId: String, staffId: String): Result<Unit> {
        delay(500)
        store.verifyTransaction(transactionId, "Karan Shah (Staff)")
        return Result.success(Unit)
    }

    override suspend fun rejectTransaction(transactionId: String, staffId: String, reason: String): Result<Unit> {
        delay(500)
        store.rejectTransaction(transactionId, "Karan Shah (Staff)", reason)
        return Result.success(Unit)
    }

    override fun searchElectricians(query: String): Flow<List<ElectricianProfile>> {
        return store.electricianProfiles.map { list ->
            if (query.isBlank()) list
            else list.filter {
                it.fullName.contains(query, ignoreCase = true) ||
                        it.phoneNumber.contains(query) ||
                        it.electricianId.contains(query, ignoreCase = true) ||
                        it.area.contains(query, ignoreCase = true)
            }
        }
    }

    override fun getElectricianDetail(electricianId: String): Flow<ElectricianProfile?> {
        return store.electricianProfiles.map { list ->
            list.find { it.electricianId.equals(electricianId, ignoreCase = true) || it.uid == electricianId }
        }
    }

    override fun getVerificationHistory(filter: String): Flow<List<Transaction>> {
        return store.transactions.map { list ->
            list.filter { it.status == TransactionStatus.VERIFIED || it.status == TransactionStatus.REJECTED }
        }
    }

    override suspend fun createTransaction(
        electricianId: String,
        amount: Long,
        invoiceNumber: String,
        invoiceDate: String,
        notes: String?
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
            createdBy = "Karan Shah (Staff)",
            verifiedBy = null,
            status = TransactionStatus.PENDING,
            notes = notes
        )
        store.addTransaction(newTx)
        return Result.success(newTx)
    }
}
