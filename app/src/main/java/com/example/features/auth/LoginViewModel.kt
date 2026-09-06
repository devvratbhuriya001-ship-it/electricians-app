package com.example.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.data.repository.UnregisteredUserException
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isNotRegistered: Boolean = false,
    val otpSentSuccess: Boolean = false
)

class LoginViewModel(
    private val authRepository: AuthRepository = RepositoryProvider.authRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onPhoneNumberChanged(phone: String) {
        val filtered = phone.filter { it.isDigit() }.take(10)
        _uiState.value = _uiState.value.copy(
            phoneNumber = filtered,
            errorMessage = null,
            isNotRegistered = false
        )
    }

    fun requestOtp() {
        val phone = _uiState.value.phoneNumber.trim()
        if (phone.length != 10) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please enter a valid 10-digit mobile number."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                isNotRegistered = false
            )
            val result = authRepository.sendOtp("+91$phone")
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    otpSentSuccess = true
                )
            }.onFailure { error ->
                val isNotReg = error is UnregisteredUserException ||
                        (error.message?.contains("not registered", ignoreCase = true) == true)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isNotRegistered = isNotReg,
                    errorMessage = error.localizedMessage ?: "Failed to send OTP. Please try again."
                )
            }
        }
    }

    fun resetOtpState() {
        _uiState.value = _uiState.value.copy(otpSentSuccess = false)
    }
}

