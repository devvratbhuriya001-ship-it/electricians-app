package com.example.features.electrician.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.Transaction
import com.example.domain.models.TransactionStatus
import com.example.domain.repository.ElectricianRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class ElectricianActivityUiState(
    val isLoading: Boolean = true,
    val transactions: List<Transaction> = emptyList(),
    val searchQuery: String = "",
    val selectedStatus: TransactionStatus? = null,
    val selectedTransaction: Transaction? = null,
    val totalVerifiedAmount: Long = 0L,
    val errorMessage: String? = null
)

class ElectricianActivityViewModel(
    private val electricianRepository: ElectricianRepository = RepositoryProvider.electricianRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElectricianActivityUiState())
    val uiState: StateFlow<ElectricianActivityUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        loadTransactions()
    }

    fun onStatusFilterSelected(status: TransactionStatus?) {
        _uiState.value = _uiState.value.copy(selectedStatus = status)
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            electricianRepository.getTransactions(
                statusFilter = _uiState.value.selectedStatus,
                searchQuery = _uiState.value.searchQuery
            ).collect { list ->
                val verifiedTotal = list.filter { it.status == TransactionStatus.VERIFIED }.sumOf { it.amount }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    transactions = list,
                    totalVerifiedAmount = verifiedTotal
                )
            }
        }
    }

    fun loadTransactionDetail(transactionId: String) {
        viewModelScope.launch {
            electricianRepository.getTransactionDetail(transactionId).collect { tx ->
                _uiState.value = _uiState.value.copy(selectedTransaction = tx)
            }
        }
    }
}
