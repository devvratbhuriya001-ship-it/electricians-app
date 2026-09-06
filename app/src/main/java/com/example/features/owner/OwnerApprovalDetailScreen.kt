package com.example.features.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*

@Composable
fun OwnerApprovalDetailScreen(
    requestId: String,
    onNavigateBack: () -> Unit,
    viewModel: OwnerApprovalsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showRejectDialog by remember { mutableStateOf(false) }
    var rejectReason by remember { mutableStateOf("") }

    LaunchedEffect(requestId) {
        viewModel.loadDetail(requestId)
    }

    if (uiState.actionSuccessMessage != null) {
        SuccessDialog(
            title = "Status Updated",
            message = uiState.actionSuccessMessage ?: "",
            onDismiss = {
                viewModel.clearActionState()
                onNavigateBack()
            }
        )
    }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Reject Application", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Provide reason for application rejection:", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                    AppTextField(
                        value = rejectReason,
                        onValueChange = { rejectReason = it },
                        label = "Rejection Reason",
                        placeholder = "e.g. Unverified electrician identity"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRejectDialog = false
                        viewModel.rejectRegistration(requestId, rejectReason.ifBlank { "Application rejected." })
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusRejectedRed)
                ) {
                    Text("Confirm Rejection", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) { Text("Cancel", color = TextBrownSecondary) }
            },
            containerColor = WarmSandSurface,
            shape = RoundedCornerShape(18.dp)
        )
    }

    val req = uiState.selectedRequest ?: uiState.pendingRegistrations.find { it.requestId == requestId }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Review Applicant",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("owner_approval_detail_screen")
    ) { innerPadding ->
        if (req == null) {
            FullScreenLoading(message = "Loading application...")
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserAvatar(name = req.fullName, size = 68)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = req.fullName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Text(
                        text = req.businessName ?: "Independent Electrician",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StatusChip(
                        statusText = "Application Pending",
                        statusColor = StatusPendingOrange,
                        backgroundColor = StatusPendingOrangeBg
                    )
                }
            }

            // Application Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Applicant Form Submission",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Mobile Number", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(req.phoneNumber, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                    }

                    HorizontalDivider(color = DividerWarm)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Operating Territory", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(req.area, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                    }

                    HorizontalDivider(color = DividerWarm)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Submission Date", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(req.submissionDate, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                    }

                    if (!req.electricianId.isNullOrBlank()) {
                        HorizontalDivider(color = DividerWarm)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Existing ID", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                            Text(req.electricianId, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                        }
                    }

                    if (!req.referralCode.isNullOrBlank()) {
                        HorizontalDivider(color = DividerWarm)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Referral Code", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                            Text(req.referralCode, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showRejectDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("owner_reject_reg_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StatusRejectedRedBg,
                        contentColor = StatusRejectedRed
                    )
                ) {
                    Text("Reject", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { viewModel.approveRegistration(requestId) },
                    modifier = Modifier
                        .weight(2f)
                        .height(52.dp)
                        .testTag("owner_approve_reg_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StatusApprovedGreen,
                        contentColor = Color.White
                    ),
                    enabled = !uiState.isActionLoading
                ) {
                    if (uiState.isActionLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Approve & Activate", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
