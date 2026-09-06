package com.example.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.features.auth.LoginScreen
import com.example.features.auth.OtpScreen
import com.example.features.common.*
import com.example.features.electrician.ElectricianMainScreen
import com.example.features.electrician.activity.TransactionDetailScreen
import com.example.features.electrician.leaderboard.LeaderboardScreen
import com.example.features.electrician.rewards.RewardDetailScreen
import com.example.features.owner.*
import com.example.features.registration.RegistrationOtpScreen
import com.example.features.registration.RegistrationScreen
import com.example.features.splash.SplashScreen
import com.example.features.staff.*
import com.example.features.status.InactiveAccountScreen
import com.example.features.status.PendingApprovalScreen
import com.example.features.status.RejectedAccountScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 1. Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToElectrician = {
                    navController.navigate(Screen.ElectricianMain.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToStaff = {
                    navController.navigate(Screen.StaffMain.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToOwner = {
                    navController.navigate(Screen.OwnerMain.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToPending = {
                    navController.navigate(Screen.PendingApproval.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToRejected = {
                    navController.navigate(Screen.Rejected.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToInactive = {
                    navController.navigate(Screen.Inactive.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Authentication & Onboarding
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToOtp = { phone ->
                    navController.navigate(Screen.Otp.createRoute(phone))
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToSupport = {
                    navController.navigate(Screen.Support.route)
                },
                onNavigateToElectrician = {
                    navController.navigate(Screen.ElectricianMain.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToStaff = {
                    navController.navigate(Screen.StaffMain.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToOwner = {
                    navController.navigate(Screen.OwnerMain.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToPending = {
                    navController.navigate(Screen.PendingApproval.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRejected = {
                    navController.navigate(Screen.Rejected.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Otp.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OtpScreen(
                phoneNumber = phone,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToElectrician = {
                    navController.navigate(Screen.ElectricianMain.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToStaff = {
                    navController.navigate(Screen.StaffMain.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToOwner = {
                    navController.navigate(Screen.OwnerMain.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToPending = {
                    navController.navigate(Screen.PendingApproval.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRejected = {
                    navController.navigate(Screen.Rejected.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToInactive = {
                    navController.navigate(Screen.Inactive.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegistrationScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRegistrationOtp = { phone ->
                    navController.navigate(Screen.RegistrationOtp.createRoute(phone))
                },
                onNavigateToTerms = {
                    navController.navigate(Screen.Terms.route)
                }
            )
        }

        composable(
            route = Screen.RegistrationOtp.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            RegistrationOtpScreen(
                phoneNumber = phone,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPendingApproval = {
                    navController.navigate(Screen.PendingApproval.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // 3. Status Screens
        composable(Screen.PendingApproval.route) {
            PendingApprovalScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onNavigateToSupport = {
                    navController.navigate(Screen.Support.route)
                },
                onNavigateToElectrician = {
                    navController.navigate(Screen.ElectricianMain.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.Rejected.route) {
            RejectedAccountScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Rejected.route) { inclusive = true }
                    }
                },
                onNavigateToSupport = {
                    navController.navigate(Screen.Support.route)
                }
            )
        }

        composable(Screen.Inactive.route) {
            InactiveAccountScreen(
                onNavigateToSupport = {
                    navController.navigate(Screen.Support.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // 4. Electrician Main & Sub-screens
        composable(Screen.ElectricianMain.route) {
            ElectricianMainScreen(
                onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                onNavigateToRewardDetail = { rewardId -> navController.navigate(Screen.RewardDetail.createRoute(rewardId)) },
                onNavigateToTransactionDetail = { txId -> navController.navigate(Screen.TransactionDetail.createRoute(txId)) },
                onNavigateToLeaderboard = { navController.navigate(Screen.Leaderboard.route) },
                onNavigateToSupport = { navController.navigate(Screen.Support.route) },
                onNavigateToTerms = { navController.navigate(Screen.Terms.route) },
                onNavigateToPrivacy = { navController.navigate(Screen.Privacy.route) },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onNavigateToStaff = {
                    navController.navigate(Screen.StaffMain.route) {
                        popUpTo(Screen.ElectricianMain.route) { inclusive = true }
                    }
                },
                onNavigateToOwner = {
                    navController.navigate(Screen.OwnerMain.route) {
                        popUpTo(Screen.ElectricianMain.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.RewardDetail.route,
            arguments = listOf(navArgument("rewardId") { type = NavType.StringType })
        ) { backStackEntry ->
            val rewardId = backStackEntry.arguments?.getString("rewardId") ?: ""
            RewardDetailScreen(
                rewardId = rewardId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TransactionDetail.route,
            arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val txId = backStackEntry.arguments?.getString("transactionId") ?: ""
            TransactionDetailScreen(
                transactionId = txId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 5. Staff Main & Sub-screens
        composable(Screen.StaffMain.route) {
            StaffMainScreen(
                onNavigateToVerifyDetail = { txId ->
                    navController.navigate(Screen.StaffVerifyDetail.createRoute(txId))
                },
                onNavigateToElectricianDetail = { elId ->
                    navController.navigate(Screen.StaffElectricianDetail.createRoute(elId))
                },
                onNavigateToAddTransaction = {
                    navController.navigate(Screen.StaffAddTransaction.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onNavigateToElectrician = {
                    navController.navigate(Screen.ElectricianMain.route) {
                        popUpTo(Screen.StaffMain.route) { inclusive = true }
                    }
                },
                onNavigateToOwner = {
                    navController.navigate(Screen.OwnerMain.route) {
                        popUpTo(Screen.StaffMain.route) { inclusive = true }
                    }
                },
                onNavigateToSupport = {
                    navController.navigate(Screen.Support.route)
                }
            )
        }

        composable(
            route = Screen.StaffVerifyDetail.route,
            arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val txId = backStackEntry.arguments?.getString("transactionId") ?: ""
            StaffVerifyDetailScreen(
                transactionId = txId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.StaffElectricianDetail.route,
            arguments = listOf(navArgument("electricianId") { type = NavType.StringType })
        ) { backStackEntry ->
            val elId = backStackEntry.arguments?.getString("electricianId") ?: ""
            StaffElectricianDetailScreen(
                electricianId = elId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddInvoice = {
                    navController.navigate(Screen.StaffAddTransaction.route)
                }
            )
        }

        composable(Screen.StaffAddTransaction.route) {
            StaffAddTransactionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 6. Owner Main & Sub-screens
        composable(Screen.OwnerMain.route) {
            OwnerMainScreen(
                onNavigateToApprovals = {
                    navController.navigate(Screen.PendingRegistrations.route)
                },
                onNavigateToApprovalDetail = { reqId ->
                    navController.navigate(Screen.RegistrationDetail.createRoute(reqId))
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onNavigateToElectrician = {
                    navController.navigate(Screen.ElectricianMain.route) {
                        popUpTo(Screen.OwnerMain.route) { inclusive = true }
                    }
                },
                onNavigateToStaff = {
                    navController.navigate(Screen.StaffMain.route) {
                        popUpTo(Screen.OwnerMain.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.PendingRegistrations.route) {
            OwnerApprovalsScreen(
                onNavigateToDetail = { reqId ->
                    navController.navigate(Screen.RegistrationDetail.createRoute(reqId))
                }
            )
        }

        composable(
            route = Screen.RegistrationDetail.route,
            arguments = listOf(navArgument("requestId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reqId = backStackEntry.arguments?.getString("requestId") ?: ""
            OwnerApprovalDetailScreen(
                requestId = reqId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.SchemeManagement.route) {
            OwnerSchemeScreen()
        }

        // 7. General Screens
        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Support.route) {
            SupportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Terms.route) {
            TermsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Privacy.route) {
            PrivacyScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
