package com.example.features.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.*
import com.example.core.theme.*
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.UserRole
import kotlinx.coroutines.launch

@Composable
fun StaffProfileScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToElectrician: () -> Unit,
    onNavigateToOwner: () -> Unit,
    onNavigateToSupport: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        ConfirmationDialog(
            title = "Sign Out",
            message = "Are you sure you want to sign out from the Staff verification console?",
            confirmText = "Sign Out",
            isDestructive = true,
            onConfirm = {
                coroutineScope.launch {
                    RepositoryProvider.authRepository.logout()
                    onNavigateToLogin()
                }
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .testTag("staff_profile_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

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
                UserAvatar(name = "Karan Shah", size = 70)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Karan Shah", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                Text("Verification Team • STF-MUM-09", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                Spacer(modifier = Modifier.height(8.dp))
                StatusChip(statusText = "STAFF ROLE", statusColor = Color(0xFF1976D2), backgroundColor = Color(0xFFE3F2FD))
            }
        }

        // Switch role tester link
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = WarmSandSurfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚡ Switch Role (Tester/Review Mode):",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                RepositoryProvider.authRepository.switchRoleForTesting(UserRole.ELECTRICIAN)
                                onNavigateToElectrician()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricianOrangePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Electrician", fontSize = 13.sp)
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                RepositoryProvider.authRepository.switchRoleForTesting(UserRole.OWNER)
                                onNavigateToOwner()
                            }
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

        AppOutlinedButton(
            text = "Sign Out Staff Console",
            onClick = { showLogoutDialog = true },
            contentColor = StatusRejectedRed
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}
