package com.example.features.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.RegistrationRequest
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegistrationUiState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val area: String = "",
    val electricianId: String = "",
    val businessName: String = "",
    val referralCode: String = "",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successRequest: RegistrationRequest? = null
)

class RegistrationViewModel(
    private val authRepository: AuthRepository = RepositoryProvider.authRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onFullNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value, errorMessage = null)
    }

    fun onPhoneNumberChanged(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value, errorMessage = null)
    }

    fun onAreaChanged(value: String) {
        _uiState.value = _uiState.value.copy(area = value, errorMessage = null)
    }

    fun onElectricianIdChanged(value: String) {
        _uiState.value = _uiState.value.copy(electricianId = value)
    }

    fun onBusinessNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(businessName = value)
    }

    fun onReferralCodeChanged(value: String) {
        _uiState.value = _uiState.value.copy(referralCode = value)
    }

    fun onTermsAcceptedChanged(value: Boolean) {
        _uiState.value = _uiState.value.copy(termsAccepted = value, errorMessage = null)
    }

    fun submitRegistration() {
        val state = _uiState.value
        if (state.fullName.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter your full name.")
            return
        }
        if (state.phoneNumber.trim().length < 10) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid 10-digit mobile number.")
            return
        }
        if (state.area.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter your area or city.")
            return
        }
        if (!state.termsAccepted) {
            _uiState.value = state.copy(errorMessage = "Please accept the Terms & Scheme Conditions to continue.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            val result = authRepository.registerUser(
                fullName = state.fullName,
                phoneNumber = "+91${state.phoneNumber}",
                area = state.area,
                electricianId = state.electricianId.ifBlank { null },
                businessName = state.businessName.ifBlank { null },
                referralCode = state.referralCode.ifBlank { null }
            )
            result.onSuccess { req ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successRequest = req
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Failed to submit registration."
                )
            }
        }
    }

    fun resetSuccessState() {
        _uiState.value = _uiState.value.copy(successRequest = null)
    }
}
