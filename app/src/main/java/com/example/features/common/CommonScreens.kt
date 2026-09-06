package com.example.features.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.AppTopBar
import com.example.core.components.EmptyState
import com.example.core.theme.*

// 1. Support & Help Center Screen
@Composable
fun SupportScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Help & Support",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("support_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(WarmSandSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.HeadsetMic,
                            contentDescription = "Support",
                            tint = ElectricianOrangePrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Electrician Loyalty Helpdesk",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Text(
                        text = "We are here to assist with bill verification, milestone reward claims, and account status inquiries.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextBrownSecondary
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Direct Contact Channels",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Phone, contentDescription = null, tint = ElectricianOrangePrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Toll-Free Helpline", style = MaterialTheme.typography.labelMedium.copy(color = TextMuted))
                            Text("+91 1800-266-9999", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                        }
                    }

                    HorizontalDivider(color = DividerWarm)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Email, contentDescription = null, tint = ElectricianOrangePrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Dedicated Email Support", style = MaterialTheme.typography.labelMedium.copy(color = TextMuted))
                            Text("loyalty.support@electricianapp.in", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown))
                        }
                    }

                    HorizontalDivider(color = DividerWarm)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.AccessTime, contentDescription = null, tint = ElectricianOrangePrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Support Working Hours", style = MaterialTheme.typography.labelMedium.copy(color = TextMuted))
                            Text("Monday to Saturday, 9:00 AM – 7:00 PM IST", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, color = TextDeepBrown))
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Frequently Asked Questions",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Text(
                        text = "Q: How long does purchase verification take?",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown)
                    )
                    Text(
                        text = "A: Counter staff verify purchases within 24–48 hours of invoice submission at the store.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextBrownSecondary)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Q: When can I claim my milestone prize?",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDeepBrown)
                    )
                    Text(
                        text = "A: Once your verified aggregate purchase volume reaches the milestone threshold, tap 'Claim Reward' in the Rewards tab.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextBrownSecondary)
                    )
                }
            }
        }
    }
}

// 2. Notifications Screen
data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val isUnread: Boolean
)

@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit
) {
    val sampleNotifications = listOf(
        NotificationItem(
            id = "n1",
            title = "Invoice Verified: ₹35,000",
            description = "Invoice #TX-88219 has been approved by Rahul (Staff) and added to your 2026 scheme volume.",
            time = "2 hours ago",
            isUnread = true
        ),
        NotificationItem(
            id = "n2",
            title = "Silver Toolkit Milestone Unlocked!",
            description = "Congratulations! You crossed the ₹10 Lakhs mark. Head to Rewards tab to redeem.",
            time = "Yesterday",
            isUnread = true
        ),
        NotificationItem(
            id = "n3",
            title = "Scheme 2026 Double Points Weekend",
            description = "Special festive promotion: Extra 5% bonus volume multiplier on all wire and switch purchases this weekend.",
            time = "3 days ago",
            isUnread = false
        ),
        NotificationItem(
            id = "n4",
            title = "Account Registration Approved",
            description = "Welcome to the Electricians Loyalty Scheme 2026! Your account is now fully active.",
            time = "12 Aug 2026",
            isUnread = false
        )
    )

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Notifications",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("notifications_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            items(sampleNotifications) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.isUnread) WarmSandSurface else WarmSandSurfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (item.isUnread) 2.dp else 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (item.isUnread) ElectricianOrangePrimary.copy(alpha = 0.15f) else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (item.isUnread) Icons.Filled.NotificationsActive else Icons.Outlined.Notifications,
                                contentDescription = null,
                                tint = if (item.isUnread) ElectricianOrangePrimary else TextBrownSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = if (item.isUnread) FontWeight.Bold else FontWeight.SemiBold,
                                        color = TextDeepBrown
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = item.time,
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodySmall.copy(color = TextBrownSecondary)
                            )
                        }
                    }
                }
            }
        }
    }
}

// 3. Terms & Conditions Screen
@Composable
fun TermsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Terms & Conditions",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("terms_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Electrician Loyalty Scheme 2026 Rules",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Text(
                        text = "1. Scheme Duration & Eligibility\n" +
                                "The 2026 Loyalty Scheme is valid from 01 January 2026 to 31 December 2026. Only registered electricians with verified account status approved by the business management are eligible to accrue scheme volume.\n\n" +
                                "2. Purchase Verification & Invoicing\n" +
                                "All transactions submitted for scheme benefits must represent genuine wholesale/retail electrical goods purchases. Transactions are subject to physical invoice verification and counter-signing by authorized store staff.\n\n" +
                                "3. Milestone Rewards & Redemption\n" +
                                "Rewards cannot be transferred to third parties or exchanged for cash alternatives unless explicitly authorized in writing by the business owner. Tax liabilities (if applicable) shall be handled per standard commercial guidelines.\n\n" +
                                "4. Termination & Amendments\n" +
                                "Management reserves the right to modify scheme parameters, milestone thresholds, or reward allocations with 14 days prior notice via the mobile application notification board.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextBrownSecondary,
                            lineHeight = 22.sp
                        )
                    )
                }
            }
        }
    }
}

// 4. Privacy Policy Screen
@Composable
fun PrivacyScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Privacy Policy",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("privacy_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Privacy & Data Protection Notice",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    Text(
                        text = "We collect your contact details (Full Name, Phone Number, Operating Area) solely for managing your loyalty account, verifying purchase volume, and distributing scheme rewards.\n\n" +
                                "• Data Storage: Your account credentials and transaction summaries are stored securely and never sold to third-party advertisers.\n" +
                                "• Communications: We send SMS OTPs and push notifications strictly related to transaction confirmations, security alerts, and milestone updates.\n" +
                                "• Contact Us: If you have questions regarding your stored profile data, contact loyalty.support@electricianapp.in.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextBrownSecondary,
                            lineHeight = 22.sp
                        )
                    )
                }
            }
        }
    }
}
