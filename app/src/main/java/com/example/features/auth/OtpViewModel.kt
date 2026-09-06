package com.example.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.AccountStatus
import com.example.domain.models.User
import com.example.domain.models.UserRole
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OtpUiState(
    val phoneNumber: String = "",
    val otp: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val timerSeconds: Int = 30,
    val canResend: Boolean = false,
    val authenticatedUser: User? = null
)

class OtpViewModel(
    private val authRepository: AuthRepository = RepositoryProvider.authRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState: StateFlow<OtpUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun initPhoneNumber(phone: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phone)
        startResendTimer()
    }

    fun onOtpChanged(otp: String) {
        _uiState.value = _uiState.value.copy(
            otp = otp,
            errorMessage = null
        )
        if (otp.length == 6) {
            verifyOtp()
        }
    }

    private fun startResendTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(timerSeconds = 30, canResend = false)
        timerJob = viewModelScope.launch {
            for (i in 30 downTo 1) {
                _uiState.value = _uiState.value.copy(timerSeconds = i)
                delay(1000)
            }
            _uiState.value = _uiState.value.copy(timerSeconds = 0, canResend = true)
        }
    }

    fun resendOtp() {
        if (!_uiState.value.canResend) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.sendOtp("+91${_uiState.value.phoneNumber}")
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false)
                startResendTimer()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Failed to resend OTP."
                )
            }
        }
    }

    fun verifyOtp() {
        val otp = _uiState.value.otp.trim()
        if (otp.length != 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter all 6 digits.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = authRepository.verifyOtp("+91${_uiState.value.phoneNumber}", otp)
            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    authenticatedUser = user
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Verification failed. Please try again."
                )
            }
        }
    }
}
