package com.example.features.electrician.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*

@Composable
fun LeaderboardScreen(
    onNavigateBack: () -> Unit,
    viewModel: LeaderboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Electricians Leaderboard",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("leaderboard_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
        ) {
            // Header Toggle
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WarmSandSurfaceContainer, RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { viewModel.setYearly(true) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (uiState.isYearly) ElectricianOrangePrimary else Color.Transparent,
                            contentColor = if (uiState.isYearly) Color.White else TextBrownSecondary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text("2026 Annual Scheme", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = { viewModel.setYearly(false) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!uiState.isYearly) ElectricianOrangePrimary else Color.Transparent,
                            contentColor = if (!uiState.isYearly) Color.White else TextBrownSecondary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text("August Month", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            // Top 3 Podium
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Top Achievers Podium",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDeepBrown
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Rank 2 (Silver)
                            PodiumColumn(
                                rank = 2,
                                name = "Pooja Wire",
                                amount = "₹72.5L",
                                color = Color(0xFF90A4AE),
                                height = 75
                            )

                            // Rank 1 (Gold)
                            PodiumColumn(
                                rank = 1,
                                name = "Sanjay & Bros",
                                amount = "₹84.2L",
                                color = ElectricianGold,
                                height = 100
                            )

                            // Rank 3 (Bronze)
                            PodiumColumn(
                                rank = 3,
                                name = "Vikram Patel",
                                amount = "₹68.4L",
                                color = Color(0xFFCD7F32),
                                height = 60
                            )
                        }
                    }
                }
            }

            item {
                SectionHeader(title = "All Registered Electricians (3,248)")
            }

            items(uiState.entries) { entry ->
                LeaderboardRow(entry = entry)
            }
        }
    }
}

@Composable
private fun PodiumColumn(
    rank: Int,
    name: String,
    amount: String,
    color: Color,
    height: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(96.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDeepBrown
                )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepBrown
            ),
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        Text(
            text = amount,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = ElectricianOrangePrimary
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .width(72.dp)
                .height(height.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(color.copy(alpha = 0.4f), color.copy(alpha = 0.8f))
                    ),
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
        )
    }
}
