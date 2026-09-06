package com.example.features.electrician.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.LeaderboardEntry
import com.example.domain.repository.ElectricianRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LeaderboardUiState(
    val isLoading: Boolean = true,
    val isYearly: Boolean = true,
    val entries: List<LeaderboardEntry> = emptyList(),
    val currentUserEntry: LeaderboardEntry? = null,
    val errorMessage: String? = null
)

class LeaderboardViewModel(
    private val electricianRepository: ElectricianRepository = RepositoryProvider.electricianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        loadLeaderboard()
    }

    fun setYearly(isYearly: Boolean) {
        _uiState.value = _uiState.value.copy(isYearly = isYearly)
        loadLeaderboard()
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            electricianRepository.getLeaderboard(_uiState.value.isYearly).collect { list ->
                val currentUser = list.find { it.isCurrentUser }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    entries = list,
                    currentUserEntry = currentUser
                )
            }
        }
    }
}
