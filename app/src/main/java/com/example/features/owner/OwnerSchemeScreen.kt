package com.example.features.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.*
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter
import com.example.domain.models.Reward
import com.example.domain.models.RewardStatus

@Composable
fun OwnerSchemeScreen() {
    val sampleRewards = listOf(
        Reward(
            rewardId = "rew_1",
            name = "Professional Silver Toolkit",
            description = "Heavy-duty 108-piece insulated tool set with hard case.",
            targetAmount = 1000000L,
            status = RewardStatus.CLAIMED,
            eligibility = "100% verified invoices within scheme duration",
            validityPeriod = "Valid until 31 Dec 2026"
        ),
        Reward(
            rewardId = "rew_2",
            name = "55\" Ultra HD 4K Smart TV",
            description = "Cinema display with Dolby Atmos surround audio.",
            targetAmount = 2500000L,
            status = RewardStatus.CLAIMED,
            eligibility = "Total verified purchases of ₹25L in 2026",
            validityPeriod = "Valid until 31 Dec 2026"
        ),
        Reward(
            rewardId = "rew_3",
            name = "Brand New 125cc Motorbike",
            description = "Top-spec fuel-injected commuter motorcycle with helmet & on-road registration.",
            targetAmount = 5000000L,
            status = RewardStatus.IN_PROGRESS,
            eligibility = "Accumulate ₹50,00,000 verified purchase total.",
            validityPeriod = "Valid until 31 Dec 2026"
        ),
        Reward(
            rewardId = "rew_4",
            name = "All-Inclusive Family International Holiday",
            description = "5 Days & 4 Nights 5-star tour package for family of 4.",
            targetAmount = 7500000L,
            status = RewardStatus.LOCKED,
            eligibility = "Reach ₹75,00,000 milestone before 31 Dec 2026.",
            validityPeriod = "Valid until 31 Dec 2026"
        ),
        Reward(
            rewardId = "rew_5",
            name = "Brand New Luxury SUV",
            description = "Flagship compact SUV car with comprehensive insurance and on-road delivery.",
            targetAmount = 10000000L,
            status = RewardStatus.LOCKED,
            eligibility = "Top milestone: ₹1,00,00,000 in scheme year.",
            validityPeriod = "Valid until 31 Dec 2026"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("owner_scheme_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Scheme 2026 Milestones & Budget",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepBrown
            )
        )
        Text(
            text = "Configured milestone tiers and rewards budget liability for the active scheme year.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextBrownSecondary
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            items(sampleRewards) { rew ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = rew.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextDeepBrown
                                )
                            )
                            Text(
                                text = "Milestone Threshold: ${CurrencyFormatter.formatInr(rew.targetAmount)}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = ElectricianOrangePrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = rew.description,
                                style = MaterialTheme.typography.bodySmall.copy(color = TextBrownSecondary)
                            )
                        }
                    }
                }
            }
        }
    }
}
