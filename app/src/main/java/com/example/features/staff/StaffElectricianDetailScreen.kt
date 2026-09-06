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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter

@Composable
fun StaffElectricianDetailScreen(
    electricianId: String,
    onNavigateBack: () -> Unit,
    onNavigateToAddInvoice: (String) -> Unit,
    viewModel: StaffElectriciansViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(electricianId) {
        viewModel.loadElectricianDetail(electricianId)
    }

    val profile = uiState.selectedElectrician ?: uiState.electricians.find { it.electricianId == electricianId }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Electrician Record",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("staff_electrician_detail_screen")
    ) { innerPadding ->
        if (profile == null) {
            FullScreenLoading(message = "Loading electrician details...")
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
            // Header Card
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
                    UserAvatar(name = profile.fullName, size = 68)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = profile.fullName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Text(
                        text = profile.businessName ?: "Electrical Services",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusChip(
                            statusText = profile.electricianId,
                            statusColor = TextDeepBrown,
                            backgroundColor = WarmSandSurfaceContainer
                        )
                        AccountStatusChip(status = profile.accountStatus)
                    }
                }
            }

            // Stats Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Purchasing & Verification Metrics",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("2026 Year Achievement", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(CurrencyFormatter.formatInr(profile.currentYearAchievement), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = ElectricianOrangePrimary))
                    }

                    HorizontalDivider(color = DividerWarm)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Lifetime Verified", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(CurrencyFormatter.formatInr(profile.lifetimeAchievement), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                    }

                    HorizontalDivider(color = DividerWarm)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Leaderboard Position", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text("#${profile.currentRank}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                    }

                    HorizontalDivider(color = DividerWarm)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Registered Contact", style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary))
                        Text(profile.phoneNumber, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AppPrimaryButton(
                text = "Add Invoice for ${profile.fullName.split(" ").firstOrNull() ?: "Electrician"}",
                onClick = { onNavigateToAddInvoice(profile.electricianId) }
            )
        }
    }
}
