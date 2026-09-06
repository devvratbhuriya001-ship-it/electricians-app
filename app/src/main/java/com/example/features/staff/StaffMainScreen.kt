package com.example.features.staff

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

enum class StaffTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    HOME("Home", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "tab_staff_home"),
    VERIFY("Verify", Icons.Filled.CheckCircle, Icons.Outlined.CheckCircle, "tab_staff_verify"),
    ELECTRICIANS("Electricians", Icons.Filled.Group, Icons.Outlined.Group, "tab_staff_electricians"),
    HISTORY("History", Icons.Filled.History, Icons.Outlined.History, "tab_staff_history"),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person, "tab_staff_profile")
}

@Composable
fun StaffMainScreen(
    onNavigateToVerifyDetail: (String) -> Unit,
    onNavigateToElectricianDetail: (String) -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToElectrician: () -> Unit,
    onNavigateToOwner: () -> Unit,
    onNavigateToSupport: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(StaffTab.HOME) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = WarmSandSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("staff_bottom_nav")
            ) {
                StaffTab.values().forEach { tab ->
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
        modifier = Modifier.testTag("staff_main_container")
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = WarmSandBackground
        ) {
            when (selectedTab) {
                StaffTab.HOME -> StaffHomeScreen(
                    onNavigateToVerify = { selectedTab = StaffTab.VERIFY },
                    onNavigateToVerifyDetail = onNavigateToVerifyDetail,
                    onNavigateToElectricians = { selectedTab = StaffTab.ELECTRICIANS },
                    onNavigateToAddTransaction = onNavigateToAddTransaction
                )
                StaffTab.VERIFY -> StaffVerifyScreen(
                    onNavigateToVerifyDetail = onNavigateToVerifyDetail
                )
                StaffTab.ELECTRICIANS -> StaffElectriciansScreen(
                    onNavigateToDetail = onNavigateToElectricianDetail
                )
                StaffTab.HISTORY -> StaffHistoryScreen(
                    onNavigateToDetail = onNavigateToVerifyDetail
                )
                StaffTab.PROFILE -> StaffProfileScreen(
                    onNavigateToLogin = onNavigateToLogin,
                    onNavigateToElectrician = onNavigateToElectrician,
                    onNavigateToOwner = onNavigateToOwner,
                    onNavigateToSupport = onNavigateToSupport
                )
            }
        }
    }
}
