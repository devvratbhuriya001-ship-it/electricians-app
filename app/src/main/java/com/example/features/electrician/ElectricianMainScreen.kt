package com.example.features.electrician

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.theme.*
import com.example.features.electrician.activity.ElectricianActivityScreen
import com.example.features.electrician.home.ElectricianHomeScreen
import com.example.features.electrician.profile.ElectricianProfileScreen
import com.example.features.electrician.progress.ElectricianProgressScreen
import com.example.features.electrician.rewards.ElectricianRewardsScreen

enum class ElectricianTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, "tab_electrician_home"),
    PROGRESS("Progress", Icons.Filled.TrendingUp, Icons.Outlined.TrendingUp, "tab_electrician_progress"),
    REWARDS("Rewards", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents, "tab_electrician_rewards"),
    ACTIVITY("Activity", Icons.Filled.ReceiptLong, Icons.Outlined.ReceiptLong, "tab_electrician_activity"),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person, "tab_electrician_profile")
}

@Composable
fun ElectricianMainScreen(
    onNavigateToNotifications: () -> Unit,
    onNavigateToRewardDetail: (String) -> Unit,
    onNavigateToTransactionDetail: (String) -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToStaff: () -> Unit,
    onNavigateToOwner: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(ElectricianTab.HOME) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = WarmSandSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("electrician_bottom_nav")
            ) {
                ElectricianTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ElectricianOrangePrimary,
                            selectedTextColor = ElectricianOrangePrimary,
                            indicatorColor = WarmSandSurfaceVariant,
                            unselectedIconColor = TextBrownSecondary,
                            unselectedTextColor = TextBrownSecondary
                        ),
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("electrician_main_container")
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = WarmSandBackground
        ) {
            when (selectedTab) {
                ElectricianTab.HOME -> ElectricianHomeScreen(
                    onNavigateToNotifications = onNavigateToNotifications,
                    onNavigateToRewardDetail = onNavigateToRewardDetail,
                    onNavigateToTransactionDetail = onNavigateToTransactionDetail,
                    onNavigateToAllActivity = { selectedTab = ElectricianTab.ACTIVITY },
                    onNavigateToLeaderboard = onNavigateToLeaderboard
                )
                ElectricianTab.PROGRESS -> ElectricianProgressScreen(
                    onNavigateToRewardDetail = onNavigateToRewardDetail
                )
                ElectricianTab.REWARDS -> ElectricianRewardsScreen(
                    onNavigateToRewardDetail = onNavigateToRewardDetail
                )
                ElectricianTab.ACTIVITY -> ElectricianActivityScreen(
                    onNavigateToTransactionDetail = onNavigateToTransactionDetail
                )
                ElectricianTab.PROFILE -> ElectricianProfileScreen(
                    onNavigateToSupport = onNavigateToSupport,
                    onNavigateToTerms = onNavigateToTerms,
                    onNavigateToPrivacy = onNavigateToPrivacy,
                    onNavigateToLogin = onNavigateToLogin,
                    onNavigateToStaff = onNavigateToStaff,
                    onNavigateToOwner = onNavigateToOwner
                )
            }
        }
    }
}
