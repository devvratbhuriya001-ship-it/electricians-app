package com.example.features.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@Composable
fun StaffHomeScreen(
    onNavigateToVerify: () -> Unit,
    onNavigateToVerifyDetail: (String) -> Unit,
    onNavigateToElectricians: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    viewModel: StaffHomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("staff_home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Staff Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UserAvatar(name = profile?.fullName ?: "Karan Shah", size = 46)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Staff Desk: ${profile?.fullName ?: "Karan Shah"}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDeepBrown
                            )
                        )
                        Text(
                            text = "${profile?.department ?: "Verification Team"} • ${profile?.staffId ?: "STF-MUM-09"}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextBrownSecondary
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onNavigateToAddTransaction,
                    modifier = Modifier
                        .background(ElectricianOrangePrimary, CircleShape)
                        .size(40.dp)
                        .testTag("staff_add_invoice_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "New Invoice Entry",
                        tint = Color.White
                    )
                }
            }
        }

        // Summary Metric Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Pending Verification",
                        value = "${profile?.pendingCount ?: 8} Invoices",
                        subtitle = "Awaiting review",
                        icon = Icons.Outlined.PendingActions,
                        accentColor = StatusPendingOrange,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToVerify
                    )
                    MetricCard(
                        title = "Verified Today",
                        value = "${profile?.verifiedCountToday ?: 14} Invoices",
                        subtitle = "Approved & credited",
                        icon = Icons.Filled.CheckCircle,
                        accentColor = StatusApprovedGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Action CTA Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmSandBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Quick Verification Queue",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )
                    Text(
                        text = "There are ${uiState.pendingVerifications.size} electrician invoices waiting for authenticity cross-verification.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary)
                    )
                    AppPrimaryButton(
                        text = "Open Verification Queue (${uiState.pendingVerifications.size})",
                        onClick = onNavigateToVerify
                    )
                }
            }
        }

        // Pending Invoices Queue Preview
        item {
            SectionHeader(
                title = "Pending Invoices",
                actionText = "View All (${uiState.pendingVerifications.size})",
                onActionClick = onNavigateToVerify
            )
        }

        if (uiState.pendingVerifications.isEmpty()) {
            item {
                EmptyState(
                    title = "All caught up!",
                    subtitle = "No pending invoices currently in the verification queue."
                )
            }
        } else {
            items(uiState.pendingVerifications.take(3)) { tx ->
                TransactionItem(
                    transaction = tx,
                    onClick = { onNavigateToVerifyDetail(tx.transactionId) }
                )
            }
        }
    }
}
