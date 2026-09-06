package com.example.features.electrician.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.DashboardSummary
import com.example.domain.models.MonthlyBreakdown
import com.example.domain.models.Reward
import com.example.domain.repository.ElectricianRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ElectricianProgressUiState(
    val isLoading: Boolean = true,
    val summary: DashboardSummary? = null,
    val monthlyBreakdowns: List<MonthlyBreakdown> = emptyList(),
    val rewards: List<Reward> = emptyList(),
    val errorMessage: String? = null
)

class ElectricianProgressViewModel(
    private val electricianRepository: ElectricianRepository = RepositoryProvider.electricianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElectricianProgressUiState())
    val uiState: StateFlow<ElectricianProgressUiState> = _uiState.asStateFlow()

    init {
        loadProgressData()
    }

    fun loadProgressData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                combine(
                    electricianRepository.getDashboardSummary(),
                    electricianRepository.getMonthlyBreakdown(),
                    electricianRepository.getRewards()
                ) { summary, monthly, rewards ->
                    ElectricianProgressUiState(
                        isLoading = false,
                        summary = summary,
                        monthlyBreakdowns = monthly,
                        rewards = rewards
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Failed to load progress."
                )
            }
        }
    }
}
