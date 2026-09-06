package com.example.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.theme.*

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    testTag: String = "text_field"
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            label = { Text(label, style = MaterialTheme.typography.bodyMedium) },
            placeholder = { if (placeholder.isNotEmpty()) Text(placeholder, style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = WarmSandSurface,
                unfocusedContainerColor = WarmSandSurface,
                focusedBorderColor = ElectricianOrangePrimary,
                unfocusedBorderColor = WarmSandBorder,
                focusedLabelColor = ElectricianOrangePrimary,
                unfocusedLabelColor = TextBrownSecondary,
                cursorColor = ElectricianOrangePrimary
            )
        )
        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = StatusRejectedRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun PhoneNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    onDone: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                val filtered = input.filter { it.isDigit() }.take(10)
                onValueChange(filtered)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .testTag("phone_number_input"),
            placeholder = {
                Text(
                    "Enter your mobile number",
                    style = MaterialTheme.typography.bodyLarge.copy(color = MutedText, fontSize = 15.sp)
                )
            },
            leadingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 14.dp, end = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = null,
                        tint = PrimaryOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "+91",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText,
                            fontSize = 15.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .height(26.dp)
                            .width(1.dp)
                            .background(BorderColor)
                    )
                }
            },
            textStyle = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = PrimaryText,
                fontSize = 16.sp
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            singleLine = true,
            isError = isError,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = BorderColor,
                cursorColor = PrimaryOrange,
                errorBorderColor = StatusErrorRed
            )
        )
        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = StatusErrorRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 14.dp, top = 6.dp)
            )
        }
    }
}

@Composable
fun OtpInput(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    isError: Boolean = false,
    focusRequester: FocusRequester = FocusRequester()
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("otp_input_box"),
        contentAlignment = Alignment.Center
    ) {
        // Hidden BasicTextField for receiving keyboard events
        BasicTextField(
            value = otpValue,
            onValueChange = { input ->
                val filtered = input.filter { it.isDigit() }.take(length)
                onOtpChange(filtered)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .focusRequester(focusRequester)
                .matchParentSize()
                .testTag("hidden_otp_input"),
            textStyle = TextStyle(color = Color.Transparent),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.Transparent)
        )

        // Visual 6 individual OTP boxes
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until length) {
                val char = if (i < otpValue.length) otpValue[i].toString() else ""
                val isCurrent = i == otpValue.length
                val borderColor = when {
                    isError -> StatusRejectedRed
                    isCurrent -> ElectricianOrangePrimary
                    char.isNotEmpty() -> ElectricianOrangeLight
                    else -> WarmSandBorder
                }
                val bgColor = when {
                    isCurrent -> WarmSandSurfaceVariant
                    char.isNotEmpty() -> WarmSandSurface
                    else -> WarmSandSurfaceContainer
                }

                Box(
                    modifier = Modifier
                        .size(width = 46.dp, height = 56.dp)
                        .background(bgColor, RoundedCornerShape(12.dp))
                        .border(
                            width = if (isCurrent || isError) 2.dp else 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = TextDeepBrown,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}
