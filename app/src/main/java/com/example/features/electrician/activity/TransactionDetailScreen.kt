package com.example.features.electrician.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter
import com.example.domain.models.TransactionStatus

@Composable
fun TransactionDetailScreen(
    transactionId: String,
    onNavigateBack: () -> Unit,
    viewModel: ElectricianActivityViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(transactionId) {
        viewModel.loadTransactionDetail(transactionId)
    }

    val tx = uiState.selectedTransaction ?: uiState.transactions.find { it.transactionId == transactionId }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Invoice Details",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("transaction_detail_screen")
    ) { innerPadding ->
        if (tx == null) {
            FullScreenLoading(message = "Loading invoice records...")
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
            // Main Amount Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TransactionStatusChip(status = tx.status)

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = CurrencyFormatter.formatInr(tx.amount),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (tx.status == TransactionStatus.VERIFIED) StatusApprovedGreen else TextDeepBrown
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Invoice #${tx.invoiceNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextBrownSecondary
                        )
                    )
                }
            }

            // Information details
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
                        text = "Invoice Breakdown",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Invoice Date", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(tx.invoiceDate, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
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
                        Text("Verified By", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(tx.verifiedBy ?: "Pending Verification Desk", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                    }

                    if (tx.notes != null) {
                        HorizontalDivider(color = DividerWarm)
                        Text(
                            text = "Itemized Description",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDeepBrown
                            )
                        )
                        Text(
                            text = tx.notes,
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary)
                        )
                    }

                    if (tx.status == TransactionStatus.REJECTED && tx.rejectionReason != null) {
                        HorizontalDivider(color = DividerWarm)
                        Text(
                            text = "Rejection Reason",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = StatusRejectedRed
                            )
                        )
                        Text(
                            text = tx.rejectionReason,
                            style = MaterialTheme.typography.bodyMedium.copy(color = StatusRejectedRed)
                        )
                    }
                }
            }
        }
    }
}
