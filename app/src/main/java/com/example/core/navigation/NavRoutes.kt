package com.example.core.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Otp : Screen("otp/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "otp/$phoneNumber"
    }
    object Register : Screen("register")
    object RegistrationOtp : Screen("registration_otp/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "registration_otp/$phoneNumber"
    }
    object PendingApproval : Screen("pending_approval")
    object Rejected : Screen("rejected")
    object Inactive : Screen("inactive")

    // Electrician Module
    object ElectricianMain : Screen("electrician_main")
    object RewardDetail : Screen("reward_detail/{rewardId}") {
        fun createRoute(rewardId: String) = "reward_detail/$rewardId"
    }
    object TransactionDetail : Screen("transaction_detail/{transactionId}") {
        fun createRoute(transactionId: String) = "transaction_detail/$transactionId"
    }
    object Leaderboard : Screen("leaderboard")

    // Staff Module
    object StaffMain : Screen("staff_main")
    object StaffVerifyDetail : Screen("staff_verify_detail/{transactionId}") {
        fun createRoute(transactionId: String) = "staff_verify_detail/$transactionId"
    }
    object StaffElectricianDetail : Screen("staff_electrician_detail/{electricianId}") {
        fun createRoute(electricianId: String) = "staff_electrician_detail/$electricianId"
    }
    object StaffAddTransaction : Screen("staff_add_transaction")

    // Owner Module
    object OwnerMain : Screen("owner_main")
    object OwnerElectricianDetail : Screen("owner_electrician_detail/{electricianId}") {
        fun createRoute(electricianId: String) = "owner_electrician_detail/$electricianId"
    }
    object PendingRegistrations : Screen("pending_registrations")
    object RegistrationDetail : Screen("registration_detail/{requestId}") {
        fun createRoute(requestId: String) = "registration_detail/$requestId"
    }
    object SchemeManagement : Screen("scheme_management")
    object RewardManagement : Screen("reward_management")
    object AddReward : Screen("add_reward")
    object EditReward : Screen("edit_reward/{rewardId}") {
        fun createRoute(rewardId: String) = "edit_reward/$rewardId"
    }
    object OwnerAddTransaction : Screen("owner_add_transaction")

    // Common
    object Notifications : Screen("notifications")
    object Support : Screen("support")
    object Terms : Screen("terms")
    object Privacy : Screen("privacy")
}
