package com.example.features.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PauseCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.components.AppOutlinedButton
import com.example.core.components.AppPrimaryButton
import com.example.core.theme.*
import com.example.data.repository.RepositoryProvider
import kotlinx.coroutines.launch

@Composable
fun InactiveAccountScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToSupport: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("inactive_account_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(StatusInactiveGrayBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PauseCircleOutline,
                        contentDescription = "Inactive Account",
                        tint = StatusInactiveGray,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Account Temporarily Inactive",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepBrown
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Your Electricians App account has been temporarily put on hold by the administrator. Please reach out to our desk for reactivation.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextBrownSecondary,
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppPrimaryButton(
                    text = "Contact Support Helpdesk",
                    onClick = onNavigateToSupport
                )

                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            RepositoryProvider.authRepository.logout()
                            onNavigateToLogin()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = TextBrownSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}
