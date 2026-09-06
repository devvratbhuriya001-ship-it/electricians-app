package com.example.features.electrician.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.constants.AppConstants
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter
import com.example.domain.models.AccountStatus
import com.example.domain.models.UserRole

@Composable
fun ElectricianProfileScreen(
    onNavigateToSupport: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToStaff: () -> Unit,
    onNavigateToOwner: () -> Unit,
    viewModel: ElectricianProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onNavigateToLogin()
        }
    }

    if (uiState.showLogoutDialog) {
        ConfirmationDialog(
            title = "Sign Out",
            message = "Are you sure you want to sign out from Electricians App?",
            confirmText = "Sign Out",
            isDestructive = true,
            onConfirm = { viewModel.logout() },
            onDismiss = { viewModel.showLogoutDialog(false) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .testTag("electrician_profile_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Avatar & Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserAvatar(
                    name = profile?.fullName ?: "Ramesh Kumar",
                    size = 72
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = profile?.fullName ?: "Ramesh Kumar",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepBrown
                    )
                )

                Text(
                    text = profile?.businessName ?: "Ramesh Electrical Works",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextBrownSecondary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusChip(
                        statusText = "ID: ${profile?.electricianId ?: "ELC-2026-047"}",
                        statusColor = TextDeepBrown,
                        backgroundColor = WarmSandSurfaceContainer
                    )
                    AccountStatusChip(status = profile?.accountStatus ?: AccountStatus.APPROVED)
                }
            }
        }

        // Electrician Summary Stats
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Account Information",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepBrown
                    )
                )

                ProfileInfoRow(label = "Mobile Number", value = profile?.phoneNumber ?: "+91 98765 43210")
                HorizontalDivider(color = DividerWarm)
                ProfileInfoRow(label = "Registered Area", value = profile?.area ?: "Andheri East, Mumbai")
                HorizontalDivider(color = DividerWarm)
                ProfileInfoRow(label = "Member Since", value = profile?.memberSince ?: "Jan 2024")
                HorizontalDivider(color = DividerWarm)
                ProfileInfoRow(label = "Lifetime Purchases", value = CurrencyFormatter.formatInr(profile?.lifetimeAchievement ?: 7850000L))
            }
        }

        // Settings and Navigation Links
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                ProfileOptionItem(
                    icon = Icons.Outlined.HeadsetMic,
                    title = "Help & Support Desk",
                    subtitle = "Contact verification team / queries",
                    onClick = onNavigateToSupport
                )
                HorizontalDivider(color = DividerWarm, modifier = Modifier.padding(horizontal = 16.dp))
                ProfileOptionItem(
                    icon = Icons.Outlined.Description,
                    title = "Scheme Terms & Rules",
                    subtitle = "2026 milestone eligibility guidelines",
                    onClick = onNavigateToTerms
                )
                HorizontalDivider(color = DividerWarm, modifier = Modifier.padding(horizontal = 16.dp))
                ProfileOptionItem(
                    icon = Icons.Outlined.Shield,
                    title = "Privacy Policy",
                    subtitle = "Data security & terms",
                    onClick = onNavigateToPrivacy
                )
            }
        }

        // Testing Role Switcher in Profile
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = WarmSandSurfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚡ Switch Role (Tester/Review Mode):",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepBrown
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.switchRoleForTesting(UserRole.STAFF)
                            onNavigateToStaff()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Staff Mode", fontSize = 13.sp)
                    }
                    Button(
                        onClick = {
                            viewModel.switchRoleForTesting(UserRole.OWNER)
                            onNavigateToOwner()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A148C)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Owner Mode", fontSize = 13.sp)
                    }
                }
            }
        }

        // Sign Out Button
        AppOutlinedButton(
            text = "Sign Out",
            onClick = { viewModel.showLogoutDialog(true) },
            contentColor = StatusRejectedRed,
            testTag = "profile_logout_button"
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
    }
}

@Composable
private fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(WarmSandSurfaceContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ElectricianOrangePrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDeepBrown
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = TextBrownSecondary)
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(16.dp)
        )
    }
}
