package com.example.features.electrician.rewards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter
import com.example.domain.models.Reward
import com.example.domain.models.RewardStatus

@Composable
fun ElectricianRewardsScreen(
    onNavigateToRewardDetail: (String) -> Unit,
    viewModel: ElectricianRewardsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading && uiState.rewards.isEmpty()) {
        FullScreenLoading(message = "Loading rewards catalog...")
        return
    }

    val currentAchievement = uiState.summary?.currentAchievement ?: 3275000L

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("electrician_rewards_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            Column {
                Text(
                    text = "2026 Rewards Catalog",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepBrown
                    )
                )
                Text(
                    text = "Unlock prestigious prizes as your verified purchase volume climbs throughout the year.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextBrownSecondary
                    )
                )
            }
        }

        // Active Achievement Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmSandBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Your 2026 Verified Total",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextBrownSecondary)
                        )
                        Text(
                            text = CurrencyFormatter.formatInr(currentAchievement),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDeepBrown
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(ElectricianOrangePrimary, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "ACTIVE SCHEME",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        items(uiState.rewards) { reward ->
            val isUnlocked = reward.status == RewardStatus.UNLOCKED || reward.status == RewardStatus.CLAIMED
            val isCurrent = reward.status == RewardStatus.IN_PROGRESS
            val progress = ((currentAchievement.toDouble() / reward.targetAmount.toDouble()) * 100).toInt().coerceIn(0, 100)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToRewardDetail(reward.rewardId) }
                    .testTag("reward_card_${reward.rewardId}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.5.dp, ElectricianOrangePrimary) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        when {
                                            isUnlocked -> StatusApprovedGreenBg
                                            isCurrent -> WarmSandSurfaceVariant
                                            else -> WarmSandSurfaceContainer
                                        },
                                        RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when {
                                        isUnlocked -> Icons.Filled.CheckCircle
                                        isCurrent -> Icons.Outlined.TwoWheeler
                                        else -> Icons.Filled.Lock
                                    },
                                    contentDescription = null,
                                    tint = when {
                                        isUnlocked -> StatusApprovedGreen
                                        isCurrent -> ElectricianOrangePrimary
                                        else -> TextMuted
                                    },
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = reward.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextDeepBrown
                                    )
                                )
                                Text(
                                    text = "Milestone: ${CurrencyFormatter.formatInr(reward.targetAmount)}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextBrownSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
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

                    Text(
                        text = reward.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextBrownSecondary
                        )
                    )

                    // Linear progress bar for this reward
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Progress: $progress%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) ElectricianOrangePrimary else TextBrownSecondary
                                )
                            )
                            if (!isUnlocked) {
                                val rem = (reward.targetAmount - currentAchievement).coerceAtLeast(0L)
                                Text(
                                    text = "${CurrencyFormatter.formatInr(rem)} remaining",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { progress / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (isUnlocked) StatusApprovedGreen else ElectricianOrangePrimary,
                            trackColor = WarmSandSurfaceContainer
                        )
                    }
                }
            }
        }
    }
}
