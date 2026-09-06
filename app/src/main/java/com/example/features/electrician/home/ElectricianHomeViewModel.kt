package com.example.features.electrician.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.*
import com.example.domain.repository.ElectricianRepository
import com.example.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ElectricianHomeUiState(
    val isLoading: Boolean = true,
    val electricianProfile: ElectricianProfile? = null,
    val dashboardSummary: DashboardSummary? = null,
    val recentTransactions: List<Transaction> = emptyList(),
    val nextReward: Reward? = null,
    val unreadNotificationsCount: Int = 0,
    val celebrationReward: String? = null,
    val errorMessage: String? = null
)

class ElectricianHomeViewModel(
    private val electricianRepository: ElectricianRepository = RepositoryProvider.electricianRepository,
    private val notificationRepository: NotificationRepository = RepositoryProvider.notificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElectricianHomeUiState())
    val uiState: StateFlow<ElectricianHomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                combine(
                    electricianRepository.getProfile(),
                    electricianRepository.getDashboardSummary(),
                    electricianRepository.getTransactions(),
                    electricianRepository.getRewards(),
                    notificationRepository.getNotifications("usr_ramesh_101")
                ) { profile, summary, transactions, rewards, notifs ->
                    val unread = notifs.count { !it.read }
                    val nextRew = rewards.find { it.status == RewardStatus.IN_PROGRESS } ?: rewards.find { it.status == RewardStatus.LOCKED }

                    ElectricianHomeUiState(
                        isLoading = false,
                        electricianProfile = profile,
                        dashboardSummary = summary,
                        recentTransactions = transactions.take(3),
                        nextReward = nextRew,
                        unreadNotificationsCount = unread,
                        errorMessage = null
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Failed to load dashboard."
                )
            }
        }
    }

    fun dismissCelebration() {
        _uiState.value = _uiState.value.copy(celebrationReward = null)
    }

    fun triggerCelebrationForTest(rewardName: String) {
        _uiState.value = _uiState.value.copy(celebrationReward = rewardName)
    }
}
