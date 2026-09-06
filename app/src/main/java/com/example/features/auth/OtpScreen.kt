package com.example.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.AppPrimaryButton
import com.example.core.components.AppTopBar
import com.example.core.components.OtpInput
import com.example.core.theme.*
import com.example.domain.models.AccountStatus
import com.example.domain.models.UserRole

@Composable
fun OtpScreen(
    phoneNumber: String,
    onNavigateBack: () -> Unit,
    onNavigateToElectrician: () -> Unit,
    onNavigateToStaff: () -> Unit,
    onNavigateToOwner: () -> Unit,
    onNavigateToPending: () -> Unit,
    onNavigateToRejected: () -> Unit,
    onNavigateToInactive: () -> Unit,
    viewModel: OtpViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(phoneNumber) {
        viewModel.initPhoneNumber(phoneNumber)
    }

    LaunchedEffect(uiState.authenticatedUser) {
        val user = uiState.authenticatedUser
        if (user != null) {
            when (user.accountStatus) {
                AccountStatus.PENDING -> onNavigateToPending()
                AccountStatus.REJECTED -> onNavigateToRejected()
                AccountStatus.INACTIVE -> onNavigateToInactive()
                AccountStatus.APPROVED -> {
                    when (user.role) {
                        UserRole.ELECTRICIAN -> onNavigateToElectrician()
                        UserRole.STAFF -> onNavigateToStaff()
                        UserRole.OWNER -> onNavigateToOwner()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Verification",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("otp_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Enter 6-Digit OTP",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepBrown
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "A verification code has been sent via SMS to",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextBrownSecondary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "+91 $phoneNumber",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit phone number",
                            tint = ElectricianOrangePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // OTP Input Field
                OtpInput(
                    otpValue = uiState.otp,
                    onOtpChange = { viewModel.onOtpChanged(it) },
                    isError = uiState.errorMessage != null
                )

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = uiState.errorMessage ?: "",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = StatusRejectedRed,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Resend Timer Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.canResend) {
                        Text(
                            text = "Didn't receive the code? ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextBrownSecondary
                            )
                        )
                        Text(
                            text = "Resend OTP",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ElectricianOrangePrimary
                            ),
                            modifier = Modifier
                                .clickable { viewModel.resendOtp() }
                                .padding(4.dp)
                                .testTag("resend_otp_button")
                        )
                    } else {
                        Text(
                            text = "Resend OTP in 00:${String.format("%02d", uiState.timerSeconds)}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextBrownSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                AppPrimaryButton(
                    text = "Verify & Proceed",
                    onClick = { viewModel.verifyOtp() },
                    isLoading = uiState.isLoading,
                    enabled = uiState.otp.length == 6,
                    testTag = "verify_otp_button"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SecondaryBackground, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "For your security, never share this OTP with anyone.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = SecondaryText,
                            textAlign = TextAlign.Center,
                            fontSize = 11.5.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
