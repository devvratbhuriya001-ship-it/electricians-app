package com.example.features.registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.AppPrimaryButton
import com.example.core.components.AppTopBar
import com.example.core.components.OtpInput
import com.example.core.theme.*

@Composable
fun RegistrationOtpScreen(
    phoneNumber: String,
    onNavigateBack: () -> Unit,
    onNavigateToPendingApproval: () -> Unit,
    viewModel: RegistrationOtpViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(phoneNumber) {
        viewModel.initPhone(phoneNumber)
    }

    LaunchedEffect(uiState.isVerified) {
        if (uiState.isVerified) {
            onNavigateToPendingApproval()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Verify Registration",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("registration_otp_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Verify Your Mobile",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDeepBrown
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter the 6-digit OTP sent to +91 $phoneNumber to finalize your registration request.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextBrownSecondary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(36.dp))

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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.canResend) {
                        Text(
                            text = "Didn't get the code? ",
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
                    text = "Confirm & Submit Request",
                    onClick = { viewModel.verifyOtp() },
                    isLoading = uiState.isLoading,
                    enabled = uiState.otp.length == 6,
                    testTag = "confirm_registration_otp_button"
                )
            }
        }
    }
}
