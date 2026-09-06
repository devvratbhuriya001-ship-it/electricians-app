package com.example.features.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
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
fun OwnerApprovalsScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: OwnerApprovalsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("owner_approvals_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registration Approvals",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepBrown
            )
        )
        Text(
            text = "Review and approve new electrician onboarding requests.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextBrownSecondary
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (uiState.pendingRegistrations.isEmpty()) {
            EmptyState(
                title = "No Pending Registrations",
                subtitle = "All incoming electrician applications have been processed.",
                icon = Icons.Outlined.CheckCircle
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(uiState.pendingRegistrations) { req ->
                    RegistrationRequestCard(
                        request = req,
                        onClick = { onNavigateToDetail(req.requestId) }
                    )
                }
            }
        }
    }
}
