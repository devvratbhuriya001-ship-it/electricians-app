package com.example.features.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*
import com.example.core.utils.CurrencyFormatter
import com.example.domain.models.ElectricianProfile

@Composable
fun StaffElectriciansScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: StaffElectriciansViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmSandBackground)
            .padding(horizontal = 16.dp)
            .testTag("staff_electricians_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Electricians Directory",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextDeepBrown
            )
        )
        Text(
            text = "Search across registered electrician accounts and inspect verified purchasing records.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextBrownSecondary
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        AppSearchBar(
            query = uiState.searchQuery,
            onQueryChange = { viewModel.onSearchQueryChange(it) },
            placeholder = "Search by name, ID, phone or area..."
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.electricians.isEmpty()) {
            EmptyState(
                title = "No electricians found",
                subtitle = "Try searching with a different name or electrician ID.",
                icon = Icons.Outlined.PersonSearch
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(uiState.electricians) { electrician ->
                    StaffElectricianCard(
                        profile = electrician,
                        onClick = { onNavigateToDetail(electrician.electricianId) }
                    )
                }
            }
        }
    }
}

@Composable
fun StaffElectricianCard(
    profile: ElectricianProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("electrician_row_${profile.electricianId}"),
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
            UserAvatar(name = profile.fullName, size = 44)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = profile.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepBrown
                    )
                )
                Text(
                    text = "${profile.electricianId} • ${profile.area}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextBrownSecondary)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "2026 Achieved: ${CurrencyFormatter.formatInr(profile.currentYearAchievement)}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = ElectricianOrangePrimary
                    )
                )
            }

            AccountStatusChip(status = profile.accountStatus)
        }
    }
}
