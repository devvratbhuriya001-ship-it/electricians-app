package com.example.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.AccountStatus
import com.example.domain.models.User
import com.example.domain.models.UserRole
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SplashDestination {
    object Idle : SplashDestination()
    object Login : SplashDestination()
    object PendingApproval : SplashDestination()
    object Rejected : SplashDestination()
    object Inactive : SplashDestination()
    object ElectricianDashboard : SplashDestination()
    object StaffDashboard : SplashDestination()
    object OwnerDashboard : SplashDestination()
    data class Error(val message: String) : SplashDestination()
}

class SplashViewModel(
    private val authRepository: AuthRepository = RepositoryProvider.authRepository
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination>(SplashDestination.Idle)
    val destination: StateFlow<SplashDestination> = _destination.asStateFlow()

    init {
        checkAuthenticationAndRoute()
    }

    fun checkAuthenticationAndRoute() {
        viewModelScope.launch {
            _destination.value = SplashDestination.Idle
            delay(1200) // Brief smooth splash reveal

            try {
                val result = authRepository.refreshUserStatus()
                val user = result.getOrNull()

                if (user == null) {
                    _destination.value = SplashDestination.Login
                } else {
                    when (user.accountStatus) {
                        AccountStatus.PENDING -> _destination.value = SplashDestination.PendingApproval
                        AccountStatus.REJECTED -> _destination.value = SplashDestination.Rejected
                        AccountStatus.INACTIVE -> _destination.value = SplashDestination.Inactive
                        AccountStatus.APPROVED -> {
                            when (user.role) {
                                UserRole.ELECTRICIAN -> _destination.value = SplashDestination.ElectricianDashboard
                                UserRole.STAFF -> _destination.value = SplashDestination.StaffDashboard
                                UserRole.OWNER -> _destination.value = SplashDestination.OwnerDashboard
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _destination.value = SplashDestination.Error(e.message ?: "Failed to verify profile session.")
            }
        }
    }
}
