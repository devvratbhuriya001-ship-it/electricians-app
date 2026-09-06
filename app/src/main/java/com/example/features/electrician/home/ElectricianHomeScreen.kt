package com.example.features.electrician.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.*
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
import com.example.core.constants.AppConstants
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter
import com.example.core.utils.DateUtils

@Composable
fun ElectricianHomeScreen(
    onNavigateToNotifications: () -> Unit,
    onNavigateToRewardDetail: (String) -> Unit,
    onNavigateToTransactionDetail: (String) -> Unit,
    onNavigateToAllActivity: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    viewModel: ElectricianHomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.celebrationReward != null) {
        CelebrationDialog(
            rewardName = uiState.celebrationReward ?: "",
            onViewReward = {
                uiState.nextReward?.let { onNavigateToRewardDetail(it.rewardId) }
            },
            onDismiss = { viewModel.dismissCelebration() }
        )
    }

    if (uiState.isLoading && uiState.dashboardSummary == null) {
        FullScreenLoading(message = "Fetching your rewards dashboard...")
        return
    }

    val summary = uiState.dashboardSummary
    val profile = uiState.electricianProfile

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("electrician_home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Personalized Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    UserAvatar(
                        name = profile?.fullName ?: "Ramesh Kumar",
                        size = 46
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = DateUtils.getGreetingMessage(profile?.fullName?.split(" ")?.firstOrNull() ?: "Ramesh"),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDeepBrown
                            )
                        )
                        Text(
                            text = "ID: ${profile?.electricianId ?: "ELC-2026-047"}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextBrownSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                // Notification Bell with unread badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(WarmSandSurface, CircleShape)
                        .clickable { onNavigateToNotifications() }
                        .testTag("notification_bell_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = TextDeepBrown,
                        modifier = Modifier.size(24.dp)
                    )
                    if (uiState.unreadNotificationsCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 6.dp, end = 6.dp)
                                .size(10.dp)
                                .background(StatusRejectedRed, CircleShape)
                        )
                    }
                }
            }
        }

        // Hero Achievement Card
        if (summary != null) {
            item {
                ProgressCard(
                    schemeYear = summary.schemeYear,
                    currentAchievement = summary.currentAchievement,
                    targetAmount = summary.targetAmount,
                    progressPercentage = summary.progressPercentage,
                    remainingAmount = summary.remainingAmount
                )
            }
        }

        // Next Reward Card
        if (summary != null) {
            item {
                NextRewardCard(
                    rewardName = summary.nextRewardName,
                    targetAmount = summary.targetAmount,
                    remainingAmount = summary.remainingAmount,
                    onViewRewardClick = {
                        uiState.nextReward?.let { onNavigateToRewardDetail(it.rewardId) }
                    }
                )
            }
        }

        // 2x2 Metric Summary Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Monthly Target",
                        value = CurrencyFormatter.formatCompactInr(summary?.monthlyAchievement ?: 485000L),
                        subtitle = "+18% from last month",
                        icon = Icons.Outlined.TrendingUp,
                        accentColor = ElectricianOrangePrimary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Leaderboard Rank",
                        value = "#${summary?.rank ?: 47}",
                        subtitle = "Top 2% in Region",
                        icon = Icons.Filled.EmojiEvents,
                        accentColor = ElectricianGold,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToLeaderboard
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Lifetime Verified",
                        value = CurrencyFormatter.formatCompactInr(profile?.lifetimeAchievement ?: 7850000L),
                        subtitle = "Since Jan 2024",
                        icon = Icons.Outlined.Verified,
                        accentColor = StatusApprovedGreen,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Prizes Claimed",
                        value = "${profile?.rewardsWonCount ?: 4} Rewards",
                        subtitle = "Milestone badges",
                        icon = Icons.Outlined.CardGiftcard,
                        accentColor = ElectricianAmber,
                        modifier = Modifier.weight(1f),
                        onClick = { uiState.nextReward?.let { onNavigateToRewardDetail(it.rewardId) } }
                    )
                }
            }
        }

        // Recent Transactions Section
        item {
            SectionHeader(
                title = "Recent Purchase Invoices",
                actionText = "View All Activity",
                onActionClick = onNavigateToAllActivity
            )
        }

        if (uiState.recentTransactions.isEmpty()) {
            item {
                EmptyState(
                    title = "No invoices yet",
                    subtitle = "New verified purchases will appear here automatically."
                )
            }
        } else {
            items(uiState.recentTransactions) { tx ->
                TransactionItem(
                    transaction = tx,
                    onClick = { onNavigateToTransactionDetail(tx.transactionId) }
                )
            }
        }
    }
}
