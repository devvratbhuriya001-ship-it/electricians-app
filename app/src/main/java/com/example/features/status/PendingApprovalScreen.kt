package com.example.features.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.*
import com.example.core.constants.AppConstants
import com.example.core.theme.*
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.AccountStatus
import com.example.domain.models.UserRole
import kotlinx.coroutines.launch

@Composable
fun PendingApprovalScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToElectrician: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isChecking by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    fun checkStatus() {
        coroutineScope.launch {
            isChecking = true
            statusMessage = null
            val result = RepositoryProvider.authRepository.refreshUserStatus()
            val user = result.getOrNull()
            isChecking = false
            if (user?.accountStatus == AccountStatus.APPROVED) {
                onNavigateToElectrician()
            } else {
                statusMessage = "Application status: Verification in progress by Admin Desk."
            }
        }
    }

    Scaffold(
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("pending_approval_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(StatusPendingOrangeBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = "Pending Approval",
                        tint = StatusPendingOrange,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "REGISTRATION UNDER REVIEW",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDeepBrown,
                        fontSize = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Your registration has been submitted successfully.\nOur team will verify your details.\nYou'll be notified once your account is approved.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextBrownSecondary,
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Verification Timeline",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDeepBrown
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estimated Review Time", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                            Text("24 - 48 Hours", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                        }

                        HorizontalDivider(color = DividerWarm)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Current Status", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                            StatusChip(
                                statusText = "Pending Verification",
                                statusColor = StatusPendingOrange,
                                backgroundColor = StatusPendingOrangeBg
                            )
                        }

                        HorizontalDivider(color = DividerWarm)

                        Text(
                            text = "Once approved, you will receive an SMS confirmation and gain instant access to your purchase tracker and rewards catalog.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }
                }

                if (statusMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = statusMessage ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = ElectricianOrangePrimary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppPrimaryButton(
                    text = "Check Status Again",
                    onClick = { checkStatus() },
                    isLoading = isChecking,
                    testTag = "check_approval_status_button"
                )

                AppOutlinedButton(
                    text = "Contact Support Helpdesk",
                    onClick = onNavigateToSupport,
                    contentColor = TextDeepBrown
                )

                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            RepositoryProvider.authRepository.logout()
                            onNavigateToLogin()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = StatusRejectedRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}
