package com.example.features.staff

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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter

@Composable
fun StaffVerifyDetailScreen(
    transactionId: String,
    onNavigateBack: () -> Unit,
    viewModel: StaffVerifyViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showRejectDialog by remember { mutableStateOf(false) }
    var rejectReason by remember { mutableStateOf("") }

    LaunchedEffect(transactionId) {
        viewModel.loadDetail(transactionId)
    }

    if (uiState.actionSuccessMessage != null) {
        SuccessDialog(
            title = "Action Completed",
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
            title = {
                Text(
                    "Reject Invoice",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Please provide a reason for rejecting this purchase invoice:",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary)
                    )
                    AppTextField(
                        value = rejectReason,
                        onValueChange = { rejectReason = it },
                        label = "Rejection Reason",
                        placeholder = "e.g. Duplicate invoice / illegible tax seal"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRejectDialog = false
                        viewModel.rejectTransaction(transactionId, rejectReason.ifBlank { "Invoice verification failed." })
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusRejectedRed)
                ) {
                    Text("Confirm Rejection", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text("Cancel", color = TextBrownSecondary)
                }
            },
            shape = RoundedCornerShape(18.dp),
            containerColor = WarmSandSurface
        )
    }

    val tx = uiState.selectedTransaction ?: uiState.pendingList.find { it.transactionId == transactionId }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Verify Invoice",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("staff_verify_detail_screen")
    ) { innerPadding ->
        if (tx == null) {
            FullScreenLoading(message = "Fetching invoice...")
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
            // Amount Summary Card
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
                    StatusChip(
                        statusText = "Awaiting Verification",
                        statusColor = StatusPendingOrange,
                        backgroundColor = StatusPendingOrangeBg
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = CurrencyFormatter.formatInr(tx.amount),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDeepBrown
                        )
                    )

                    Text(
                        text = "Invoice #${tx.invoiceNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextBrownSecondary
                        )
                    )
                }
            }

            // Invoice details breakdown
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
                        text = "Electrician & Invoice Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Electrician Name", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(tx.electricianName.ifBlank { "Ramesh Kumar" }, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                    }

                    HorizontalDivider(color = DividerWarm)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Electrician ID", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(tx.electricianId, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                    }

                    HorizontalDivider(color = DividerWarm)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Invoice Date", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(tx.invoiceDate, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                    }

                    if (tx.notes != null) {
                        HorizontalDivider(color = DividerWarm)
                        Text("Description & Items", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                        Text(tx.notes, style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
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
                        .testTag("staff_reject_tx_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StatusRejectedRedBg,
                        contentColor = StatusRejectedRed
                    )
                ) {
                    Text("Reject", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { viewModel.verifyTransaction(transactionId) },
                    modifier = Modifier
                        .weight(2f)
                        .height(52.dp)
                        .testTag("staff_approve_tx_button"),
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
                        Text("Verify & Credit Amount", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
