package com.example.features.electrician.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ReceiptLong
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter
import com.example.domain.models.TransactionStatus

@Composable
fun ElectricianActivityScreen(
    onNavigateToTransactionDetail: (String) -> Unit,
    viewModel: ElectricianActivityViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("electrician_activity_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Purchase Activity & Invoices",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepBrown
            )
        )
        Text(
            text = "Track the approval status and credited amounts of your billing invoices.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextBrownSecondary
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        AppSearchBar(
            query = uiState.searchQuery,
            onQueryChange = { viewModel.onSearchQueryChanged(it) },
            placeholder = "Search by invoice # or notes..."
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Status Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = uiState.selectedStatus == null,
                onClick = { viewModel.onStatusFilterSelected(null) },
                label = { Text("All (${uiState.transactions.size})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ElectricianOrangePrimary,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = uiState.selectedStatus == TransactionStatus.VERIFIED,
                onClick = { viewModel.onStatusFilterSelected(TransactionStatus.VERIFIED) },
                label = { Text("Verified") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StatusApprovedGreen,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = uiState.selectedStatus == TransactionStatus.PENDING,
                onClick = { viewModel.onStatusFilterSelected(TransactionStatus.PENDING) },
                label = { Text("Under Review") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StatusPendingOrange,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = uiState.selectedStatus == TransactionStatus.REJECTED,
                onClick = { viewModel.onStatusFilterSelected(TransactionStatus.REJECTED) },
                label = { Text("Rejected") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StatusRejectedRed,
                    selectedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.transactions.isEmpty()) {
            EmptyState(
                title = "No transactions found",
                subtitle = if (uiState.searchQuery.isNotEmpty()) "Try a different invoice search query." else "No invoices match the selected filter.",
                icon = Icons.Outlined.ReceiptLong
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(uiState.transactions) { tx ->
                    TransactionItem(
                        transaction = tx,
                        onClick = { onNavigateToTransactionDetail(tx.transactionId) }
                    )
                }
            }
        }
    }
}
