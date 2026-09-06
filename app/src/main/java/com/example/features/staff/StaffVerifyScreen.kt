package com.example.features.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*

@Composable
fun StaffVerifyScreen(
    onNavigateToVerifyDetail: (String) -> Unit,
    viewModel: StaffVerifyViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("staff_verify_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pending Invoices Verification",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepBrown
            )
        )
        Text(
            text = "Review submitted tax invoices and approve credits for electricians.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextBrownSecondary
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (uiState.pendingList.isEmpty()) {
            EmptyState(
                title = "Verification Queue Empty",
                subtitle = "All pending transactions have been processed.",
                icon = Icons.Outlined.CheckCircle
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(uiState.pendingList) { tx ->
                    TransactionItem(
                        transaction = tx,
                        onClick = { onNavigateToVerifyDetail(tx.transactionId) }
                    )
                }
            }
        }
    }
}
