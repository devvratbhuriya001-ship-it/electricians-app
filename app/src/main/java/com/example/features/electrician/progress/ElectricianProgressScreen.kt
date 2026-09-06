package com.example.features.electrician.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter
import com.example.domain.models.RewardStatus

@Composable
fun ElectricianProgressScreen(
    onNavigateToRewardDetail: (String) -> Unit,
    viewModel: ElectricianProgressViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading && uiState.summary == null) {
        FullScreenLoading(message = "Calculating your milestone trajectory...")
        return
    }

    val summary = uiState.summary

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("electrician_progress_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            Text(
                text = "Performance & Milestone Progress",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDeepBrown
                )
            )
            Text(
                text = "Track your annual volume, monthly growth, and milestone unlocking tier.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextBrownSecondary
                )
            )
        }

        // Summary Card
        if (summary != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Current Scheme Pace",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextDeepBrown
                                )
                            )
                            StatusChip(
                                statusText = "Gold Tier (${summary.progressPercentage}%)",
                                statusColor = ElectricianOrangePrimary,
                                backgroundColor = WarmSandSurfaceVariant
                            )
                        }

                        LinearProgressIndicator(
                            progress = { (summary.progressPercentage / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            color = ElectricianOrangePrimary,
                            trackColor = WarmSandSurfaceContainer
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Achieved", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                                Text(
                                    CurrencyFormatter.formatInr(summary.currentAchievement),
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown)
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Remaining Target", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                                Text(
                                    CurrencyFormatter.formatInr(summary.remainingAmount),
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = ElectricianOrangePrimary)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Monthly Breakdown Chart / List
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Monthly Verified Purchases",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDeepBrown
                            )
                        )
                        Text(
                            text = "Year 2026",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TextBrownSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    // Monthly List Visualizer
                    val maxMonthAmount = uiState.monthlyBreakdowns.maxOfOrNull { it.amount } ?: 1L
                    uiState.monthlyBreakdowns.forEach { month ->
                        val ratio = (month.amount.toFloat() / maxMonthAmount.toFloat()).coerceIn(0.1f, 1f)
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${month.monthName} (${month.invoiceCount} invoices)",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (month.isCurrentMonth) FontWeight.Bold else FontWeight.Medium,
                                        color = if (month.isCurrentMonth) ElectricianOrangePrimary else TextDeepBrown
                                    )
                                )
                                Text(
                                    text = CurrencyFormatter.formatInr(month.amount),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextDeepBrown
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

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
                                            if (month.isCurrentMonth) ElectricianOrangePrimary else ElectricianAmber,
                                            RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Milestone Progress Ladder
        item {
            SectionHeader(title = "Scheme Milestones Ladder")
        }

        items(uiState.rewards) { reward ->
            val isClaimed = reward.status == RewardStatus.CLAIMED || reward.status == RewardStatus.UNLOCKED
            val isCurrent = reward.status == RewardStatus.IN_PROGRESS

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("milestone_reward_${reward.rewardId}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrent) WarmSandSurfaceVariant else WarmSandSurface
                ),
                border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.5.dp, ElectricianOrangePrimary) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                when {
                                    isClaimed -> StatusApprovedGreenBg
                                    isCurrent -> ElectricianOrangePrimary.copy(alpha = 0.15f)
                                    else -> WarmSandSurfaceContainer
                                },
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                isClaimed -> Icons.Filled.Check
                                isCurrent -> Icons.Outlined.TrendingUp
                                else -> Icons.Filled.Lock
                            },
                            contentDescription = null,
                            tint = when {
                                isClaimed -> StatusApprovedGreen
                                isCurrent -> ElectricianOrangePrimary
                                else -> TextMuted
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = reward.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDeepBrown
                            )
                        )
                        Text(
                            text = "Target: ${CurrencyFormatter.formatInr(reward.targetAmount)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextBrownSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    StatusChip(
                        statusText = when (reward.status) {
                            RewardStatus.CLAIMED -> "Claimed"
                            RewardStatus.UNLOCKED -> "Unlocked"
                            RewardStatus.IN_PROGRESS -> "In Progress"
                            RewardStatus.LOCKED -> "Locked"
                        },
                        statusColor = when (reward.status) {
                            RewardStatus.CLAIMED, RewardStatus.UNLOCKED -> StatusApprovedGreen
                            RewardStatus.IN_PROGRESS -> ElectricianOrangePrimary
                            RewardStatus.LOCKED -> TextMuted
                        },
                        backgroundColor = when (reward.status) {
                            RewardStatus.CLAIMED, RewardStatus.UNLOCKED -> StatusApprovedGreenBg
                            RewardStatus.IN_PROGRESS -> WarmSandSurfaceVariant
                            RewardStatus.LOCKED -> WarmSandSurfaceContainer
                        }
                    )
                }
            }
        }
    }
}
