package com.example.features.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun OwnerAnalyticsScreen(
    viewModel: OwnerHomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("owner_analytics_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            Text(
                text = "Sales Volume & Channel Analytics",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDeepBrown
                )
            )
            Text(
                text = "Track aggregate revenue growth, monthly inflows, and regional distribution.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextBrownSecondary
                )
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Monthly Performance Trends",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    val maxAmount = uiState.monthlyTrends.maxOfOrNull { it.amount } ?: 1L
                    uiState.monthlyTrends.forEach { m ->
                        val ratio = (m.amount.toFloat() / maxAmount.toFloat()).coerceIn(0.1f, 1f)
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${m.monthName} (${m.invoiceCount} invoices)",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (m.isCurrentMonth) ElectricianOrangePrimary else TextDeepBrown,
                                        fontWeight = if (m.isCurrentMonth) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                                Text(
                                    text = CurrencyFormatter.formatInr(m.amount),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .background(WarmSandSurfaceContainer, RoundedCornerShape(4.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(ratio)
                                        .fillMaxHeight()
                                        .background(
                                            if (m.isCurrentMonth) ElectricianOrangePrimary else ElectricianAmber,
                                            RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            SectionHeader(title = "Top Electrician Partners")
        }

        items(uiState.topElectricians) { entry ->
            LeaderboardRow(entry = entry)
        }
    }
}
