package com.example.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.core.components.*
import com.example.core.constants.AppConstants
import com.example.core.theme.*
import com.example.domain.models.AccountStatus
import com.example.domain.models.UserRole

@Composable
fun LoginScreen(
    onNavigateToOtp: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToElectrician: () -> Unit,
    onNavigateToStaff: () -> Unit,
    onNavigateToOwner: () -> Unit,
    onNavigateToPending: () -> Unit,
    onNavigateToRejected: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.otpSentSuccess) {
        if (uiState.otpSentSuccess) {
            val phone = uiState.phoneNumber.trim()
            viewModel.resetOtpState()
            onNavigateToOtp(phone)
        }
    }

    Scaffold(
        containerColor = MainBackground,
        modifier = Modifier.testTag("login_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ==================================================
            // TOP SECTION: WARM SAND HERO ARTWORK (~30–35% height)
            // ==================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                // Dedicated decorative Warm Sand hero artwork
                ElectricianHeroArtwork(
                    modifier = Modifier.fillMaxSize()
                )

                // Top Branding Overlay
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 16.dp, start = 20.dp, end = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ELECTRICIANS APP",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryText,
                            letterSpacing = 2.sp,
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Powering Progress, Lighting Lives",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = SecondaryText,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .background(SoftSand.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                            .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "ELECTRICIAN SCHEME",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryOrange,
                                letterSpacing = 1.2.sp
                            )
                        )
                    }
                }
            }

            // ==================================================
            // LOGIN CARD (Overlaps hero art slightly)
            // ==================================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp)
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    // Heading
                    Text(
                        text = "Welcome Back!",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryText,
                            fontSize = 26.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Subtitle
                    Text(
                        text = "Login to track your progress, achieve targets and earn exciting rewards.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = SecondaryText,
                            lineHeight = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Development Firebase Connection Test Banner
                    FirebaseConnectionTestBanner()

                    Spacer(modifier = Modifier.height(14.dp))

                    // Phone Number Field [phone icon] [+91] [divider] [Enter your mobile number]
                    PhoneNumberField(
                        value = uiState.phoneNumber,
                        onValueChange = { viewModel.onPhoneNumberChanged(it) },
                        isError = uiState.errorMessage != null && !uiState.isNotRegistered,
                        errorMessage = if (!uiState.isNotRegistered) uiState.errorMessage else null,
                        onDone = { viewModel.requestOtp() }
                    )

                    // Guided Banner for Unregistered Users
                    if (uiState.isNotRegistered) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(SoftPeach)
                                .border(1.dp, PrimaryOrange.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .clickable { onNavigateToRegister() }
                                .padding(14.dp)
                                .testTag("unregistered_user_banner")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(PrimaryOrange, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.PersonAdd,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Account Not Found",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryOrange
                                        )
                                    )
                                    Text(
                                        text = "New to Electricians App? Register now to get started ->",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = PrimaryText,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Register",
                                    tint = PrimaryOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Primary Button: SEND OTP
                    AppPrimaryButton(
                        text = "SEND OTP",
                        onClick = { viewModel.requestOtp() },
                        isLoading = uiState.isLoading,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        testTag = "get_otp_button"
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Security & Trust Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SecondaryBackground, RoundedCornerShape(14.dp))
                            .border(1.dp, BorderColor.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(SoftPeach, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = PrimaryOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "Your security is our priority",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryText
                                    )
                                )
                                Text(
                                    text = "Login securely using your registered mobile number.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SecondaryText,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // New User Registration Row (Clickable Secondary Action)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SoftSand.copy(alpha = 0.4f))
                            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                            .clickable { onNavigateToRegister() }
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                            .testTag("navigate_to_register"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(CardBackground, CircleShape)
                                    .border(1.dp, BorderColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PersonAdd,
                                    contentDescription = null,
                                    tint = PrimaryOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "New to Electricians App?",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryText
                                    )
                                )
                                Text(
                                    text = "Register now to get started",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SecondaryText
                                    )
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Register",
                            tint = PrimaryOrange,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // ==================================================
            // HELP SECTION & FOOTER
            // ==================================================
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onNavigateToSupport() }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .testTag("login_support_button"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.HeadsetMic,
                    contentDescription = null,
                    tint = SecondaryText,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Need Help? ",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SecondaryText)
                )
                Text(
                    text = "Contact Support",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = PrimaryOrange,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "By signing in, you agree to Electricians App Terms of Service & Privacy Policy.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MutedText,
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                ),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }
    }
}
