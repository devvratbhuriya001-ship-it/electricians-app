package com.example.features.electrician.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.AccountStatus
import com.example.domain.models.ElectricianProfile
import com.example.domain.models.UserRole
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.ElectricianRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ElectricianProfileUiState(
    val isLoading: Boolean = true,
    val profile: ElectricianProfile? = null,
    val showLogoutDialog: Boolean = false,
    val isLoggedOut: Boolean = false
)

class ElectricianProfileViewModel(
    private val electricianRepository: ElectricianRepository = RepositoryProvider.electricianRepository,
    private val authRepository: AuthRepository = RepositoryProvider.authRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElectricianProfileUiState())
    val uiState: StateFlow<ElectricianProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            electricianRepository.getProfile().collect { profile ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    profile = profile
                )
            }
        }
    }

    fun showLogoutDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showLogoutDialog = show)
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = _uiState.value.copy(isLoggedOut = true, showLogoutDialog = false)
        }
    }

    fun switchRoleForTesting(role: UserRole, status: AccountStatus = AccountStatus.APPROVED) {
        viewModelScope.launch {
            authRepository.switchRoleForTesting(role, status)
        }
    }
}
