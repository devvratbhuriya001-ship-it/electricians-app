package com.example.features.electrician.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.DashboardSummary
import com.example.domain.models.Reward
import com.example.domain.repository.ElectricianRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ElectricianRewardsUiState(
    val isLoading: Boolean = true,
    val rewards: List<Reward> = emptyList(),
    val summary: DashboardSummary? = null,
    val selectedReward: Reward? = null,
    val errorMessage: String? = null
)

class ElectricianRewardsViewModel(
    private val electricianRepository: ElectricianRepository = RepositoryProvider.electricianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElectricianRewardsUiState())
    val uiState: StateFlow<ElectricianRewardsUiState> = _uiState.asStateFlow()

    init {
        loadRewards()
    }

    fun loadRewards() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                combine(
                    electricianRepository.getRewards(),
                    electricianRepository.getDashboardSummary()
                ) { rewards, summary ->
                    ElectricianRewardsUiState(
                        isLoading = false,
                        rewards = rewards,
                        summary = summary
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Failed to load rewards."
                )
            }
        }
    }

    fun loadRewardDetail(rewardId: String) {
        viewModelScope.launch {
            electricianRepository.getRewardDetail(rewardId).collect { reward ->
                _uiState.value = _uiState.value.copy(selectedReward = reward)
            }
        }
    }
}
