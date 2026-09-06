package com.example.features.electrician.rewards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.domain.models.RewardStatus

@Composable
fun RewardDetailScreen(
    rewardId: String,
    onNavigateBack: () -> Unit,
    viewModel: ElectricianRewardsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(rewardId) {
        viewModel.loadRewardDetail(rewardId)
    }

    val reward = uiState.selectedReward ?: uiState.rewards.find { it.rewardId == rewardId }
    val currentAchievement = uiState.summary?.currentAchievement ?: 3275000L

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Reward Details",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("reward_detail_screen")
    ) { innerPadding ->
        if (reward == null) {
            FullScreenLoading(message = "Loading prize information...")
            return@Scaffold
        }

        val progress = ((currentAchievement.toDouble() / reward.targetAmount.toDouble()) * 100).toInt().coerceIn(0, 100)
        val remaining = (reward.targetAmount - currentAchievement).coerceAtLeast(0L)
        val isUnlocked = reward.status == RewardStatus.UNLOCKED || reward.status == RewardStatus.CLAIMED

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Reward Banner Hero
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(WarmSandSurfaceVariant, CircleShape)
                            .border(2.dp, ElectricianAmberLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                isUnlocked -> Icons.Filled.CheckCircle
                                reward.status == RewardStatus.IN_PROGRESS -> Icons.Outlined.TwoWheeler
                                else -> Icons.Filled.Lock
                            },
                            contentDescription = null,
                            tint = if (isUnlocked) StatusApprovedGreen else ElectricianOrangePrimary,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = reward.name,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Target: ${CurrencyFormatter.formatInr(reward.targetAmount)}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = ElectricianOrangePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    StatusChip(
                        statusText = when (reward.status) {
                            RewardStatus.CLAIMED -> "Claimed Prize"
                            RewardStatus.UNLOCKED -> "Unlocked!"
                            RewardStatus.IN_PROGRESS -> "In Progress ($progress%)"
                            RewardStatus.LOCKED -> "Locked Milestone"
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

            // Target Progress Breakdown
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Progress Towards This Prize",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = if (isUnlocked) StatusApprovedGreen else ElectricianOrangePrimary,
                        trackColor = WarmSandSurfaceContainer
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Current Achievement", style = MaterialTheme.typography.bodySmall.copy(color = TextBrownSecondary))
                            Text(CurrencyFormatter.formatInr(currentAchievement), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Target Amount", style = MaterialTheme.typography.bodySmall.copy(color = TextBrownSecondary))
                            Text(CurrencyFormatter.formatInr(reward.targetAmount), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                        }
                    }

                    if (!isUnlocked) {
                        HorizontalDivider(color = DividerWarm)
                        Text(
                            text = "You need ${CurrencyFormatter.formatInr(remaining)} more verified purchases before 31 Dec 2026 to claim this prize.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = ElectricianOrangeDark,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // Description & Terms
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Prize Specifications",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Text(
                        text = reward.description,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary)
                    )

                    HorizontalDivider(color = DividerWarm)

                    Text(
                        text = "Eligibility & Validity",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Text(
                        text = "• ${reward.eligibility}\n• ${reward.validityPeriod}\n• Only invoices verified by the administrative verification team count towards this reward.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextBrownSecondary, lineHeight = 22.sp)
                    )
                }
            }
        }
    }
}
