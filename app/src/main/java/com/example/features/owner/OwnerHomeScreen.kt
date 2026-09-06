package com.example.features.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
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
import com.example.domain.models.RegistrationRequest

@Composable
fun OwnerHomeScreen(
    onNavigateToApprovals: () -> Unit,
    onNavigateToApprovalDetail: (String) -> Unit,
    onNavigateToScheme: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    viewModel: OwnerHomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val stats = uiState.stats

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("owner_home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Owner Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UserAvatar(name = "Rajesh Gupta", size = 46)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Executive Board: Rajesh Gupta",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDeepBrown
                            )
                        )
                        Text(
                            text = "Electricians App • Owner Console",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextBrownSecondary
                            )
                        )
                    }
                }

                StatusChip(
                    statusText = "2026 ACTIVE",
                    statusColor = ElectricianOrangePrimary,
                    backgroundColor = WarmSandSurfaceVariant
                )
            }
        }

        // Business Revenue Hero Card
        if (stats != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
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
                                text = "Scheme 2026 Target Realization",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextDeepBrown
                                )
                            )
                            Text(
                                text = "${(stats.totalVerifiedVolume.toDouble() / stats.totalTargetVolume.toDouble() * 100).toInt()}% Target Met",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricianOrangePrimary
                                )
                            )
                        }

                        LinearProgressIndicator(
                            progress = { (stats.totalVerifiedVolume.toFloat() / stats.totalTargetVolume.toFloat()).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = ElectricianOrangePrimary,
                            trackColor = WarmSandSurfaceContainer
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Verified Volume", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                                Text(
                                    CurrencyFormatter.formatCompactInr(stats.totalVerifiedVolume),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextDeepBrown
                                    )
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Annual Scheme Target", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                                Text(
                                    CurrencyFormatter.formatCompactInr(stats.totalTargetVolume),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ElectricianOrangePrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2x2 Metric Grid
        if (stats != null) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            title = "Active Electricians",
                            value = "${stats.totalElectricians}",
                            subtitle = "+12% Growth this scheme",
                            icon = Icons.Filled.Group,
                            accentColor = ElectricianOrangePrimary,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "Pending Registrations",
                            value = "${stats.pendingRegistrationsCount} Applicants",
                            subtitle = "Requires owner approval",
                            icon = Icons.Filled.PersonAdd,
                            accentColor = StatusPendingOrange,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToApprovals
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            title = "August Volume",
                            value = CurrencyFormatter.formatCompactInr(stats.currentMonthVolume),
                            subtitle = "Run-rate steady",
                            icon = Icons.Outlined.TrendingUp,
                            accentColor = StatusApprovedGreen,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToAnalytics
                        )
                        MetricCard(
                            title = "Rewards Liability",
                            value = CurrencyFormatter.formatCompactInr(stats.rewardsClaimedCost),
                            subtitle = "Unlocked prizes budget",
                            icon = Icons.Outlined.CardGiftcard,
                            accentColor = ElectricianAmber,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToScheme
                        )
                    }
                }
            }
        }

        // Registration Approvals Queue Preview
        item {
            SectionHeader(
                title = "Pending Electrician Registrations",
                actionText = "Review All (${uiState.pendingRegistrations.size})",
                onActionClick = onNavigateToApprovals
            )
        }

        if (uiState.pendingRegistrations.isEmpty()) {
            item {
                EmptyState(
                    title = "No pending approvals",
                    subtitle = "All registration requests have been cleared."
                )
            }
        } else {
            items(uiState.pendingRegistrations.take(3)) { req ->
                RegistrationRequestCard(
                    request = req,
                    onClick = { onNavigateToApprovalDetail(req.requestId) }
                )
            }
        }
    }
}

@Composable
fun RegistrationRequestCard(
    request: RegistrationRequest,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("reg_request_card_${request.requestId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(name = request.fullName, size = 44)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = request.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepBrown
                    )
                )
                Text(
                    text = "${request.phoneNumber} • ${request.area}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextBrownSecondary)
                )
                if (!request.businessName.isNullOrBlank()) {
                    Text(
                        text = request.businessName,
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                }
            }

            StatusChip(
                statusText = "Pending",
                statusColor = StatusPendingOrange,
                backgroundColor = StatusPendingOrangeBg
            )
        }
    }
}
