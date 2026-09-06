package com.example.features.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*

@Composable
fun StaffHistoryScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: StaffHomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("staff_history_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Verification Audit Log",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepBrown
            )
        )
        Text(
            text = "Chronological record of invoices approved or rejected by the verification desk.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextBrownSecondary
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (uiState.recentVerified.isEmpty()) {
            EmptyState(
                title = "No history records",
                subtitle = "Processed invoices will show up here."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(uiState.recentVerified) { tx ->
                    TransactionItem(
                        transaction = tx,
                        onClick = { onNavigateToDetail(tx.transactionId) }
                    )
                }
            }
        }
    }
}
