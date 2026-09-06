package com.example.features.owner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.theme.*

enum class OwnerTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    HOME("Home", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "tab_owner_home"),
    APPROVALS("Approvals", Icons.Filled.HowToReg, Icons.Outlined.HowToReg, "tab_owner_approvals"),
    SCHEME("Scheme", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents, "tab_owner_scheme"),
    ANALYTICS("Analytics", Icons.Filled.Assessment, Icons.Outlined.Assessment, "tab_owner_analytics"),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person, "tab_owner_profile")
}

@Composable
fun OwnerMainScreen(
    onNavigateToApprovals: () -> Unit,
    onNavigateToApprovalDetail: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToElectrician: () -> Unit,
    onNavigateToStaff: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(OwnerTab.HOME) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = WarmSandSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("owner_bottom_nav")
            ) {
                OwnerTab.values().forEach { tab ->
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
        modifier = Modifier.testTag("owner_main_container")
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = WarmSandBackground
        ) {
            when (selectedTab) {
                OwnerTab.HOME -> OwnerHomeScreen(
                    onNavigateToApprovals = { selectedTab = OwnerTab.APPROVALS },
                    onNavigateToApprovalDetail = onNavigateToApprovalDetail,
                    onNavigateToScheme = { selectedTab = OwnerTab.SCHEME },
                    onNavigateToAnalytics = { selectedTab = OwnerTab.ANALYTICS }
                )
                OwnerTab.APPROVALS -> OwnerApprovalsScreen(
                    onNavigateToDetail = onNavigateToApprovalDetail
                )
                OwnerTab.SCHEME -> OwnerSchemeScreen()
                OwnerTab.ANALYTICS -> OwnerAnalyticsScreen()
                OwnerTab.PROFILE -> OwnerProfileScreen(
                    onNavigateToLogin = onNavigateToLogin,
                    onNavigateToElectrician = onNavigateToElectrician,
                    onNavigateToStaff = onNavigateToStaff
                )
            }
        }
    }
}
