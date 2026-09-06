package com.example.features.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*

@Composable
fun RegistrationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRegistrationOtp: (String) -> Unit,
    onNavigateToTerms: () -> Unit,
    viewModel: RegistrationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.successRequest) {
        val req = uiState.successRequest
        if (req != null) {
            val phone = uiState.phoneNumber.trim()
            viewModel.resetSuccessState()
            onNavigateToRegistrationOtp(phone)
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Electrician Registration",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("registration_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmSandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Join Electricians Rewards Scheme",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )
                    Text(
                        text = "Register with your verified mobile number to start accumulating milestone rewards.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextBrownSecondary
                        )
                    )

                    HorizontalDivider(color = DividerWarm)

                    AppTextField(
                        value = uiState.fullName,
                        onValueChange = { viewModel.onFullNameChanged(it) },
                        label = "Full Name *",
                        placeholder = "e.g. Ramesh Kumar",
                        testTag = "reg_full_name_input"
                    )

                    PhoneNumberField(
                        value = uiState.phoneNumber,
                        onValueChange = { viewModel.onPhoneNumberChanged(it) }
                    )

                    AppTextField(
                        value = uiState.area,
                        onValueChange = { viewModel.onAreaChanged(it) },
                        label = "Area / City / District *",
                        placeholder = "e.g. Andheri East, Mumbai",
                        testTag = "reg_area_input"
                    )

                    AppTextField(
                        value = uiState.businessName,
                        onValueChange = { viewModel.onBusinessNameChanged(it) },
                        label = "Business / Shop Name (Optional)",
                        placeholder = "e.g. Ramesh Electrical Works",
                        testTag = "reg_business_name_input"
                    )

                    AppTextField(
                        value = uiState.electricianId,
                        onValueChange = { viewModel.onElectricianIdChanged(it) },
                        label = "Existing Electrician ID (Optional)",
                        placeholder = "e.g. ELC-2026-047",
                        testTag = "reg_electrician_id_input"
                    )

                    AppTextField(
                        value = uiState.referralCode,
                        onValueChange = { viewModel.onReferralCodeChanged(it) },
                        label = "Referral Code (Optional)",
                        placeholder = "e.g. REF-MUM-44",
                        testTag = "reg_referral_code_input"
                    )
                }
            }

            // Terms & Conditions Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.termsAccepted,
                    onCheckedChange = { viewModel.onTermsAcceptedChanged(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = ElectricianOrangePrimary,
                        uncheckedColor = TextBrownSecondary
                    ),
                    modifier = Modifier.testTag("terms_checkbox")
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "I agree to the Scheme Rules & Terms.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextDeepBrown,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "View Electricians App Scheme Terms",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = ElectricianOrangePrimary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.clickable { onNavigateToTerms() }
                    )
                }
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = StatusRejectedRed,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AppPrimaryButton(
                text = "Proceed to Verification",
                onClick = { viewModel.submitRegistration() },
                isLoading = uiState.isLoading,
                testTag = "submit_registration_button"
            )
        }
    }
}
