package com.example.features.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.*
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.OwnerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// 1. Owner Home ViewModel
data class OwnerHomeUiState(
    val isLoading: Boolean = true,
    val stats: BusinessStats? = null,
    val pendingRegistrations: List<RegistrationRequest> = emptyList(),
    val monthlyTrends: List<MonthlyBreakdown> = emptyList(),
    val topElectricians: List<LeaderboardEntry> = emptyList(),
    val errorMessage: String? = null
)

class OwnerHomeViewModel(
    private val ownerRepository: OwnerRepository = RepositoryProvider.ownerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OwnerHomeUiState())
    val uiState: StateFlow<OwnerHomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            combine(
                ownerRepository.getOwnerDashboardMetrics(),
                ownerRepository.getPendingRegistrations(),
                RepositoryProvider.electricianRepository.getMonthlyBreakdown(),
                RepositoryProvider.electricianRepository.getLeaderboard(true)
            ) { stats, registrations, trends, top ->
                OwnerHomeUiState(
                    isLoading = false,
                    stats = stats,
                    pendingRegistrations = registrations,
                    monthlyTrends = trends,
                    topElectricians = top
                )
            }.collect { _uiState.value = it }
        }
    }
}

// 2. Owner Approvals ViewModel
data class OwnerApprovalsUiState(
    val isLoading: Boolean = true,
    val pendingRegistrations: List<RegistrationRequest> = emptyList(),
    val selectedRequest: RegistrationRequest? = null,
    val isActionLoading: Boolean = false,
    val actionSuccessMessage: String? = null,
    val errorMessage: String? = null
)

class OwnerApprovalsViewModel(
    private val ownerRepository: OwnerRepository = RepositoryProvider.ownerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OwnerApprovalsUiState())
    val uiState: StateFlow<OwnerApprovalsUiState> = _uiState.asStateFlow()

    init {
        loadPending()
    }

    fun loadPending() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            ownerRepository.getPendingRegistrations().collect { list ->
                _uiState.value = _uiState.value.copy(isLoading = false, pendingRegistrations = list)
            }
        }
    }

    fun loadDetail(requestId: String) {
        viewModelScope.launch {
            ownerRepository.getPendingRegistrations().collect { list ->
                val item = list.find { it.requestId == requestId }
                _uiState.value = _uiState.value.copy(selectedRequest = item)
            }
        }
    }

    fun approveRegistration(requestId: String, role: UserRole = UserRole.ELECTRICIAN) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isActionLoading = true, errorMessage = null)
            val result = ownerRepository.approveRegistration(requestId, role)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isActionLoading = false,
                    actionSuccessMessage = "Electrician registration approved successfully!"
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isActionLoading = false, errorMessage = it.localizedMessage)
            }
        }
    }

    fun rejectRegistration(requestId: String, reason: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isActionLoading = true, errorMessage = null)
            val result = ownerRepository.rejectRegistration(requestId, reason)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isActionLoading = false,
                    actionSuccessMessage = "Electrician application rejected."
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isActionLoading = false, errorMessage = it.localizedMessage)
            }
        }
    }

    fun clearActionState() {
        _uiState.value = _uiState.value.copy(actionSuccessMessage = null, errorMessage = null)
    }
}

