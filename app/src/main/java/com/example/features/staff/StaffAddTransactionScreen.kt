package com.example.features.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.components.*
import com.example.core.theme.*

@Composable
fun StaffAddTransactionScreen(
    initialElectricianId: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: StaffAddTxViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(initialElectricianId) {
        if (!initialElectricianId.isNullOrBlank()) {
            viewModel.onElectricianIdChange(initialElectricianId)
        }
    }

    if (uiState.success) {
        SuccessDialog(
            title = "Invoice Logged Successfully",
            message = "The invoice has been entered into the system and sent for milestone crediting.",
            onDismiss = {
                viewModel.reset()
                onNavigateBack()
            }
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Log New Invoice",
                onBackClick = onNavigateBack
            )
        },
        containerColor = WarmSandBackground,
        modifier = Modifier.testTag("staff_add_tx_screen")
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
                        text = "Invoice Entry Form",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDeepBrown
                        )
                    )

                    AppTextField(
                        value = uiState.electricianId,
                        onValueChange = { viewModel.onElectricianIdChange(it) },
                        label = "Electrician ID *",
                        placeholder = "e.g. ELC-2026-047",
                        testTag = "add_tx_electrician_id"
                    )

                    AppTextField(
                        value = uiState.invoiceNumber,
                        onValueChange = { viewModel.onInvoiceNumberChange(it) },
                        label = "Tax Invoice Number *",
                        placeholder = "e.g. INV-2026-8891",
                        testTag = "add_tx_invoice_num"
                    )

                    AppTextField(
                        value = uiState.amount,
                        onValueChange = { viewModel.onAmountChange(it) },
                        label = "Amount (₹ INR) *",
                        placeholder = "e.g. 75000",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        testTag = "add_tx_amount"
                    )

                    AppTextField(
                        value = uiState.notes,
                        onValueChange = { viewModel.onNotesChange(it) },
                        label = "Items Description / Billing Notes (Optional)",
                        placeholder = "e.g. 25 Coils 2.5sqmm Copper Cable",
                        testTag = "add_tx_notes"
                    )
                }
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = StatusRejectedRed,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AppPrimaryButton(
                text = "Submit Invoice",
                onClick = { viewModel.submit() },
                isLoading = uiState.isLoading,
                testTag = "submit_add_tx_button"
            )
        }
    }
}
