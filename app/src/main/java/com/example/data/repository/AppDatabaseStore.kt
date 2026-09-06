package com.example.data.repository

import com.example.domain.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object AppDatabaseStore {

    // Default Logged In User State
    private val _currentUser = MutableStateFlow<User?>(
        User(
            uid = "usr_ramesh_101",
            fullName = "Ramesh Kumar",
            phoneNumber = "+919876543210",
            role = UserRole.ELECTRICIAN,
            accountStatus = AccountStatus.APPROVED,
            electricianId = "ELC-2026-047",
            area = "Andheri East, Mumbai",
            businessName = "Ramesh Electrical Works",
            profileImageUrl = null,
            createdAt = 1735689600000L,
            approvedAt = 1735776000000L
        )
    )
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Rewards List
    private val _rewards = MutableStateFlow<List<Reward>>(
        listOf(
            Reward(
                rewardId = "rew_01",
                name = "Silver Toolkit & Smart Meter",
                description = "High precision digital multimeter with heavy-duty electrician insulation toolkit set.",
                targetAmount = 1000000L, // ₹10,00,000
                status = RewardStatus.CLAIMED,
                displayOrder = 1,
                eligibility = "Unlocked at ₹10L verified purchases.",
                validityPeriod = "Valid throughout 2026"
            ),
            Reward(
                rewardId = "rew_02",
                name = "55-inch 4K Smart TV",
                description = "Ultra HD 4K Smart Android TV with Dolby Atmos & Home Audio soundbar.",
                targetAmount = 2500000L, // ₹25,00,000
                status = RewardStatus.CLAIMED,
                displayOrder = 2,
                eligibility = "Unlocked at ₹25L verified purchases.",
                validityPeriod = "Valid throughout 2026"
            ),
            Reward(
                rewardId = "rew_03",
                name = "Brand New 125cc Motorbike",
                description = "High-efficiency commercial motorcycle with luggage rack and on-road insurance included.",
                targetAmount = 5000000L, // ₹50,00,000
                status = RewardStatus.IN_PROGRESS,
                displayOrder = 3,
                eligibility = "Unlocked upon achieving ₹50L verified purchases.",
                validityPeriod = "Valid till 31 Dec 2026"
            ),
            Reward(
                rewardId = "rew_04",
                name = "All-Expense Paid International Holiday Trip",
                description = "5-Day 4-Night luxury holiday package for 2 persons with flights, 5-star hotel & sightseeing.",
                targetAmount = 7500000L, // ₹75,00,000
                status = RewardStatus.LOCKED,
                displayOrder = 4,
                eligibility = "Unlocked at ₹75L verified purchases.",
                validityPeriod = "Valid till 31 Dec 2026"
            ),
            Reward(
                rewardId = "rew_05",
                name = "Luxury SUV Car / ₹15 Lakh Gold Voucher",
                description = "Grand annual champion prize: Premium Compact SUV or ₹15,00,000 pure gold voucher.",
                targetAmount = 10000000L, // ₹1,00,00,000 (₹1 Crore)
                status = RewardStatus.LOCKED,
                displayOrder = 5,
                eligibility = "Unlocked at ₹1 Cr verified purchases.",
                validityPeriod = "Valid till 31 Dec 2026"
            )
        )
    )
    val rewards: StateFlow<List<Reward>> = _rewards.asStateFlow()

    // Transactions List
    private val _transactions = MutableStateFlow<List<Transaction>>(
        listOf(
            Transaction(
                transactionId = "tx_01",
                electricianId = "ELC-2026-047",
                electricianName = "Ramesh Kumar",
                amount = 128500L,
                invoiceNumber = "INV-2026-10234",
                invoiceDate = "27 Aug 2026",
                createdAt = System.currentTimeMillis() - 86400000L * 3,
                createdBy = "Staff Verifier (Karan)",
                verifiedBy = "Karan Shah (Staff)",
                verifiedAt = System.currentTimeMillis() - 86400000L * 2,
                status = TransactionStatus.VERIFIED,
                notes = "Switches, conduits and heavy copper wiring purchase verified with tax invoice."
            ),
            Transaction(
                transactionId = "tx_02",
                electricianId = "ELC-2026-047",
                electricianName = "Ramesh Kumar",
                amount = 350000L,
                invoiceNumber = "INV-2026-09842",
                invoiceDate = "15 Aug 2026",
                createdAt = System.currentTimeMillis() - 86400000L * 15,
                createdBy = "Staff Verifier (Karan)",
                verifiedBy = "Karan Shah (Staff)",
                verifiedAt = System.currentTimeMillis() - 86400000L * 14,
                status = TransactionStatus.VERIFIED,
                notes = "Commercial warehouse distribution boards and high-grade MCB panel order."
            ),
            Transaction(
                transactionId = "tx_03",
                electricianId = "ELC-2026-047",
                electricianName = "Ramesh Kumar",
                amount = 85000L,
                invoiceNumber = "INV-2026-10499",
                invoiceDate = "29 Aug 2026",
                createdAt = System.currentTimeMillis() - 3600000L * 5,
                createdBy = "Ramesh Kumar",
                verifiedBy = null,
                verifiedAt = null,
                status = TransactionStatus.PENDING,
                notes = "Under review by verification desk."
            ),
            Transaction(
                transactionId = "tx_04",
                electricianId = "ELC-2026-047",
                electricianName = "Ramesh Kumar",
                amount = 450000L,
                invoiceNumber = "INV-2026-08110",
                invoiceDate = "22 Jul 2026",
                createdAt = System.currentTimeMillis() - 86400000L * 38,
                createdBy = "Owner Desk",
                verifiedBy = "Owner Admin",
                verifiedAt = System.currentTimeMillis() - 86400000L * 37,
                status = TransactionStatus.VERIFIED,
                notes = "Bulk project cables and modular architectural plates."
            ),
            Transaction(
                transactionId = "tx_05",
                electricianId = "ELC-2026-012",
                electricianName = "Vikram Patel",
                amount = 210000L,
                invoiceNumber = "INV-2026-10512",
                invoiceDate = "28 Aug 2026",
                createdAt = System.currentTimeMillis() - 86400000L * 2,
                createdBy = "Vikram Patel",
                verifiedBy = null,
                status = TransactionStatus.PENDING,
                notes = "Awaiting invoice receipt cross-check."
            ),
            Transaction(
                transactionId = "tx_06",
                electricianId = "ELC-2026-089",
                electricianName = "Rajesh Sharma",
                amount = 95000L,
                invoiceNumber = "INV-2026-10530",
                invoiceDate = "29 Aug 2026",
                createdAt = System.currentTimeMillis() - 3600000L * 10,
                createdBy = "Rajesh Sharma",
                verifiedBy = null,
                status = TransactionStatus.PENDING,
                notes = "Residential flat electrical wiring package."
            )
        )
    )
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    // Electrician Profiles List
    private val _electricianProfiles = MutableStateFlow<List<ElectricianProfile>>(
        listOf(
            ElectricianProfile(
                uid = "usr_ramesh_101",
                electricianId = "ELC-2026-047",
                fullName = "Ramesh Kumar",
                phoneNumber = "+919876543210",
                area = "Andheri East, Mumbai",
                businessName = "Ramesh Electrical Works",
                accountStatus = AccountStatus.APPROVED,
                memberSince = "Jan 2024",
                lifetimeAchievement = 7850000L,
                currentYearAchievement = 3275000L,
                rewardsWonCount = 4,
                currentRank = 47
            ),
            ElectricianProfile(
                uid = "usr_vikram_102",
                electricianId = "ELC-2026-012",
                fullName = "Vikram Patel",
                phoneNumber = "+919811223344",
                area = "Ahmedabad, Gujarat",
                businessName = "Patel Power Solutions",
                accountStatus = AccountStatus.APPROVED,
                memberSince = "Mar 2023",
                lifetimeAchievement = 14500000L,
                currentYearAchievement = 6840000L,
                rewardsWonCount = 8,
                currentRank = 3
            ),
            ElectricianProfile(
                uid = "usr_sunita_103",
                electricianId = "ELC-2026-024",
                fullName = "Sunita Electricals (Pooja)",
                phoneNumber = "+919765432109",
                area = "Pune, Maharashtra",
                businessName = "Sunita Electricals",
                accountStatus = AccountStatus.APPROVED,
                memberSince = "Feb 2024",
                lifetimeAchievement = 9200000L,
                currentYearAchievement = 5120000L,
                rewardsWonCount = 6,
                currentRank = 11
            ),
            ElectricianProfile(
                uid = "usr_rajesh_104",
                electricianId = "ELC-2026-089",
                fullName = "Rajesh Sharma",
                phoneNumber = "+919988776655",
                area = "Jaipur, Rajasthan",
                businessName = "Sharma Electric Co.",
                accountStatus = AccountStatus.APPROVED,
                memberSince = "Aug 2024",
                lifetimeAchievement = 4100000L,
                currentYearAchievement = 2150000L,
                rewardsWonCount = 2,
                currentRank = 89
            ),
            ElectricianProfile(
                uid = "usr_amit_105",
                electricianId = "ELC-2026-140",
                fullName = "Amit Verma",
                phoneNumber = "+919123456780",
                area = "Lucknow, Uttar Pradesh",
                businessName = "Verma Wire & Power",
                accountStatus = AccountStatus.PENDING,
                memberSince = "Aug 2026",
                lifetimeAchievement = 0L,
                currentYearAchievement = 0L,
                rewardsWonCount = 0,
                currentRank = 3120
            )
        )
    )
    val electricianProfiles: StateFlow<List<ElectricianProfile>> = _electricianProfiles.asStateFlow()

    // Registration Requests List
    private val _registrationRequests = MutableStateFlow<List<RegistrationRequest>>(
        listOf(
            RegistrationRequest(
                requestId = "req_101",
                fullName = "Amit Verma",
                phoneNumber = "+919123456780",
                area = "Lucknow, Uttar Pradesh",
                businessName = "Verma Wire & Power",
                electricianId = "ELC-2026-140",
                referralCode = "REF-MUM-44",
                submissionDate = "29 Aug 2026",
                status = AccountStatus.PENDING
            ),
            RegistrationRequest(
                requestId = "req_102",
                fullName = "Deepak Joshi",
                phoneNumber = "+919876500112",
                area = "Surat, Gujarat",
                businessName = "Joshi Electric Fitting",
                electricianId = null,
                referralCode = null,
                submissionDate = "28 Aug 2026",
                status = AccountStatus.PENDING
            ),
            RegistrationRequest(
                requestId = "req_103",
                fullName = "Suresh Chand",
                phoneNumber = "+919456781234",
                area = "Delhi NCR",
                businessName = "Chand Electricals",
                electricianId = null,
                referralCode = null,
                submissionDate = "27 Aug 2026",
                status = AccountStatus.PENDING
            )
        )
    )
    val registrationRequests: StateFlow<List<RegistrationRequest>> = _registrationRequests.asStateFlow()

    // Schemes List
    private val _schemes = MutableStateFlow<List<Scheme>>(
        listOf(
            Scheme(
                schemeId = "sch_2026",
                title = "2026 Electricians Annual Rewards Scheme",
                year = 2026,
                startDate = "01 Jan 2026",
                endDate = "31 Dec 2026",
                active = true,
                description = "Annual loyalty and purchase rewards milestone program for registered professional electricians."
            ),
            Scheme(
                schemeId = "sch_2025",
                title = "2025 Electricians Loyalty Program",
                year = 2025,
                startDate = "01 Jan 2025",
                endDate = "31 Dec 2025",
                active = false,
                description = "Previous year rewards program successfully completed."
            )
        )
    )
    val schemes: StateFlow<List<Scheme>> = _schemes.asStateFlow()

    // Notifications List
    private val _notifications = MutableStateFlow<List<AppNotification>>(
        listOf(
            AppNotification(
                notificationId = "notif_01",
                userId = "usr_ramesh_101",
                title = "Purchase Credited & Verified!",
                message = "₹1,28,500 has been added to your 2026 achievement for invoice #INV-2026-10234.",
                type = NotificationType.TRANSACTION_VERIFIED,
                createdAt = System.currentTimeMillis() - 86400000L * 2,
                read = false,
                targetRoute = "electrician_activity"
            ),
            AppNotification(
                notificationId = "notif_02",
                userId = "usr_ramesh_101",
                title = "You're getting closer!",
                message = "You have completed 65% of your Bike reward target. Only ₹17,25,000 left to unlock!",
                type = NotificationType.REWARD_PROGRESS,
                createdAt = System.currentTimeMillis() - 86400000L * 4,
                read = false,
                targetRoute = "electrician_rewards"
            ),
            AppNotification(
                notificationId = "notif_03",
                userId = "usr_ramesh_101",
                title = "Rank Update!",
                message = "You moved up 6 positions this month to Rank #47 out of 3,248 electricians.",
                type = NotificationType.RANK_UPDATE,
                createdAt = System.currentTimeMillis() - 86400000L * 6,
                read = true,
                targetRoute = "leaderboard"
            )
        )
    )
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    // Monthly breakdown data for current user
    val monthlyBreakdowns = listOf(
        MonthlyBreakdown("January", "Jan", 320000L, 4),
        MonthlyBreakdown("February", "Feb", 410000L, 5),
        MonthlyBreakdown("March", "Mar", 580000L, 7),
        MonthlyBreakdown("April", "Apr", 390000L, 4),
        MonthlyBreakdown("May", "May", 450000L, 6),
        MonthlyBreakdown("June", "Jun", 310000L, 3),
        MonthlyBreakdown("July", "Jul", 330000L, 4),
        MonthlyBreakdown("August", "Aug", 485000L, 6, isCurrentMonth = true)
    )

    // Leaderboard list
    val leaderboardEntries = listOf(
        LeaderboardEntry(1, "ELC-2026-001", "Sanjay & Brothers Electrical", "Surat", "Champion Diamond", achievementAmountFormatted = "₹84,20,000"),
        LeaderboardEntry(2, "ELC-2026-009", "Pooja Wire & Fittings", "Delhi", "Champion Diamond", achievementAmountFormatted = "₹72,50,000"),
        LeaderboardEntry(3, "ELC-2026-012", "Vikram Patel", "Ahmedabad", "Diamond Star", achievementAmountFormatted = "₹68,40,000"),
        LeaderboardEntry(4, "ELC-2026-018", "Royal Electrical Works", "Bengaluru", "Diamond Star", achievementAmountFormatted = "₹62,10,000"),
        LeaderboardEntry(5, "ELC-2026-024", "Sunita Electricals", "Pune", "Gold Elite", achievementAmountFormatted = "₹51,20,000"),
        LeaderboardEntry(47, "ELC-2026-047", "Ramesh Kumar", "Mumbai", "Gold Elite", isCurrentUser = true, achievementAmountFormatted = "₹32,75,000"),
        LeaderboardEntry(48, "ELC-2026-052", "Krishna Electric Centre", "Indore", "Silver Pro", achievementAmountFormatted = "₹31,90,000"),
        LeaderboardEntry(49, "ELC-2026-061", "Om Sai Electric Works", "Nagpur", "Silver Pro", achievementAmountFormatted = "₹30,80,000")
    )

    // Actions
    fun setCurrentUser(user: User?) {
        _currentUser.value = user
    }

    fun verifyTransaction(txId: String, staffName: String) {
        _transactions.value = _transactions.value.map { tx ->
            if (tx.transactionId == txId) {
                tx.copy(
                    status = TransactionStatus.VERIFIED,
                    verifiedBy = staffName,
                    verifiedAt = System.currentTimeMillis()
                )
            } else tx
        }
    }

    fun rejectTransaction(txId: String, staffName: String, reason: String) {
        _transactions.value = _transactions.value.map { tx ->
            if (tx.transactionId == txId) {
                tx.copy(
                    status = TransactionStatus.REJECTED,
                    verifiedBy = staffName,
                    verifiedAt = System.currentTimeMillis(),
                    rejectionReason = reason
                )
            } else tx
        }
    }

    fun approveRegistration(reqId: String, role: UserRole) {
        val req = _registrationRequests.value.find { it.requestId == reqId }
        _registrationRequests.value = _registrationRequests.value.map {
            if (it.requestId == reqId) it.copy(status = AccountStatus.APPROVED) else it
        }
        if (req != null) {
            val newProfile = ElectricianProfile(
                uid = "usr_${UUID.randomUUID().toString().take(6)}",
                electricianId = req.electricianId ?: "ELC-2026-${(100..999).random()}",
                fullName = req.fullName,
                phoneNumber = req.phoneNumber,
                area = req.area,
                businessName = req.businessName,
                accountStatus = AccountStatus.APPROVED,
                memberSince = "Aug 2026",
                lifetimeAchievement = 0L,
                currentYearAchievement = 0L,
                rewardsWonCount = 0,
                currentRank = 3249
            )
            _electricianProfiles.value = _electricianProfiles.value + newProfile
        }
    }

    fun rejectRegistration(reqId: String, reason: String) {
        _registrationRequests.value = _registrationRequests.value.map {
            if (it.requestId == reqId) it.copy(status = AccountStatus.REJECTED, rejectionReason = reason) else it
        }
    }

    fun addReward(reward: Reward) {
        _rewards.value = _rewards.value + reward
    }

    fun updateReward(reward: Reward) {
        _rewards.value = _rewards.value.map { if (it.rewardId == reward.rewardId) reward else it }
    }

    fun deleteReward(rewardId: String) {
        _rewards.value = _rewards.value.filterNot { it.rewardId == rewardId }
    }

    fun addTransaction(tx: Transaction) {
        _transactions.value = listOf(tx) + _transactions.value
    }

    fun markNotificationRead(id: String) {
        _notifications.value = _notifications.value.map { if (it.notificationId == id) it.copy(read = true) else it }
    }

    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(read = true) }
    }
}
