package com.example.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter
import com.example.domain.models.*

@Composable
fun StatusChip(
    statusText: String,
    statusColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
        )
    }
}

@Composable
fun TransactionStatusChip(
    status: TransactionStatus,
    modifier: Modifier = Modifier
) {
    val (text, color, bg) = when (status) {
        TransactionStatus.VERIFIED -> Triple("Verified", StatusApprovedGreen, StatusApprovedGreenBg)
        TransactionStatus.PENDING -> Triple("Pending", StatusPendingOrange, StatusPendingOrangeBg)
        TransactionStatus.REJECTED -> Triple("Rejected", StatusRejectedRed, StatusRejectedRedBg)
        TransactionStatus.ADJUSTED -> Triple("Adjusted", ElectricianOrangePrimary, WarmSandSurfaceVariant)
    }
    StatusChip(statusText = text, statusColor = color, backgroundColor = bg, modifier = modifier)
}

@Composable
fun AccountStatusChip(
    status: AccountStatus,
    modifier: Modifier = Modifier
) {
    val (text, color, bg) = when (status) {
        AccountStatus.APPROVED -> Triple("Approved", StatusApprovedGreen, StatusApprovedGreenBg)
        AccountStatus.PENDING -> Triple("Pending Approval", StatusPendingOrange, StatusPendingOrangeBg)
        AccountStatus.REJECTED -> Triple("Rejected", StatusRejectedRed, StatusRejectedRedBg)
        AccountStatus.INACTIVE -> Triple("Inactive", StatusInactiveGray, StatusInactiveGrayBg)
    }
    StatusChip(statusText = text, statusColor = color, backgroundColor = bg, modifier = modifier)
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepBrown
            )
        )
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = ElectricianOrangePrimary
                ),
                modifier = Modifier
                    .clickable { onActionClick() }
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun UserAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Int = 44
) {
    val initials = name.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
        .ifEmpty { "E" }

    Box(
        modifier = modifier
            .size(size.dp)
            .background(WarmSandSurfaceVariant, CircleShape)
            .border(1.5.dp, ElectricianAmberLight, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = ElectricianOrangePrimary,
                fontSize = (size / 2.6).sp
            )
        )
    }
}

@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_search_bar"),
        placeholder = { Text(placeholder, style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = TextBrownSecondary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = TextBrownSecondary
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = WarmSandSurface,
            unfocusedContainerColor = WarmSandSurface,
            focusedBorderColor = ElectricianOrangePrimary,
            unfocusedBorderColor = WarmSandBorder,
            cursorColor = ElectricianOrangePrimary
        )
    )
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("transaction_item_${transaction.transactionId}"),
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
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        when (transaction.status) {
                            TransactionStatus.VERIFIED -> StatusApprovedGreenBg
                            TransactionStatus.REJECTED -> StatusRejectedRedBg
                            else -> WarmSandSurfaceVariant
                        },
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (transaction.status) {
                        TransactionStatus.VERIFIED -> Icons.Outlined.CheckCircle
                        TransactionStatus.REJECTED -> Icons.Outlined.Cancel
                        else -> Icons.Outlined.ReceiptLong
                    },
                    contentDescription = null,
                    tint = when (transaction.status) {
                        TransactionStatus.VERIFIED -> StatusApprovedGreen
                        TransactionStatus.REJECTED -> StatusRejectedRed
                        else -> ElectricianOrangePrimary
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Invoice #${transaction.invoiceNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "+${CurrencyFormatter.formatInr(transaction.amount)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (transaction.status == TransactionStatus.VERIFIED) StatusApprovedGreen else TextDeepBrown
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = transaction.invoiceDate,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted
                        )
                    )
                    TransactionStatusChip(status = transaction.status)
                }
            }
        }
    }
}

@Composable
fun LeaderboardRow(
    entry: LeaderboardEntry,
    modifier: Modifier = Modifier
) {
    val isTop3 = entry.rank in 1..3
    val rankBadgeColor = when (entry.rank) {
        1 -> ElectricianGold
        2 -> Color(0xFFB0BEC5)
        3 -> Color(0xFFCD7F32)
        else -> WarmSandSurfaceContainer
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("leaderboard_row_${entry.rank}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (entry.isCurrentUser) WarmSandSurfaceVariant else WarmSandSurface
        ),
        border = if (entry.isCurrentUser) androidx.compose.foundation.BorderStroke(1.5.dp, ElectricianOrangePrimary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (entry.isCurrentUser) 2.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(rankBadgeColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#${entry.rank}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isTop3) TextDeepBrown else TextBrownSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = entry.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (entry.isCurrentUser) FontWeight.Bold else FontWeight.SemiBold,
                            color = TextDeepBrown
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (entry.isCurrentUser) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(You)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ElectricianOrangePrimary
                            )
                        )
                    }
                }
                Text(
                    text = "${entry.area} • ${entry.achievementTier}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextBrownSecondary
                    )
                )
            }

            if (entry.achievementAmountFormatted != null) {
                Text(
                    text = entry.achievementAmountFormatted,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepBrown
                    )
                )
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: AppNotification,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("notification_item_${notification.notificationId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.read) WarmSandSurface else WarmSandSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notification.read) 1.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (notification.read) WarmSandSurfaceContainer else ElectricianOrangePrimary.copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (notification.type) {
                        NotificationType.ACHIEVEMENT_UPDATE, NotificationType.TRANSACTION_VERIFIED -> Icons.Outlined.CheckCircle
                        NotificationType.REWARD_PROGRESS, NotificationType.REWARD_UNLOCKED -> Icons.Outlined.Star
                        NotificationType.RANK_UPDATE -> Icons.Outlined.TrendingUp
                        else -> Icons.Default.Notifications
                    },
                    contentDescription = null,
                    tint = if (notification.read) TextBrownSecondary else ElectricianOrangePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (notification.read) FontWeight.SemiBold else FontWeight.Bold,
                        color = TextDeepBrown
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextBrownSecondary
                    )
                )
            }
        }
    }
}
