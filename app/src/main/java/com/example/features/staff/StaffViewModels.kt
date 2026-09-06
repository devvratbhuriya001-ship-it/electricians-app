package com.example.features.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.RepositoryProvider
import com.example.domain.models.*
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.StaffRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// 1. Staff Home ViewModel
data class StaffHomeUiState(
    val isLoading: Boolean = true,
    val profile: StaffProfile? = null,
    val pendingVerifications: List<Transaction> = emptyList(),
    val recentVerified: List<Transaction> = emptyList()
)

class StaffHomeViewModel(
    private val staffRepository: StaffRepository = RepositoryProvider.staffRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StaffHomeUiState())
    val uiState: StateFlow<StaffHomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            combine(
                staffRepository.getStaffProfile(),
                staffRepository.getPendingVerifications(),
                staffRepository.getVerificationHistory()
            ) { profile, pending, history ->
                StaffHomeUiState(
                    isLoading = false,
                    profile = profile,
                    pendingVerifications = pending,
                    recentVerified = history.take(5)
                )
            }.collect { _uiState.value = it }
        }
    }
}

// 2. Staff Verify ViewModel
data class StaffVerifyUiState(
    val isLoading: Boolean = true,
    val pendingList: List<Transaction> = emptyList(),
    val selectedTransaction: Transaction? = null,
    val isActionLoading: Boolean = false,
    val actionSuccessMessage: String? = null,
    val errorMessage: String? = null
)

class StaffVerifyViewModel(
    private val staffRepository: StaffRepository = RepositoryProvider.staffRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StaffVerifyUiState())
    val uiState: StateFlow<StaffVerifyUiState> = _uiState.asStateFlow()

    init {
        loadPending()
    }

    fun loadPending() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            staffRepository.getPendingVerifications().collect { list ->
                _uiState.value = _uiState.value.copy(isLoading = false, pendingList = list)
            }
        }
    }

    fun loadDetail(txId: String) {
        viewModelScope.launch {
            staffRepository.getPendingVerifications().collect { list ->
                val tx = list.find { it.transactionId == txId }
                _uiState.value = _uiState.value.copy(selectedTransaction = tx)
            }
        }
    }

    fun verifyTransaction(txId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isActionLoading = true, errorMessage = null)
            val result = staffRepository.verifyTransaction(txId, "Karan Shah (Staff)")
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isActionLoading = false,
                    actionSuccessMessage = "Invoice approved and amount credited to electrician!"
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isActionLoading = false, errorMessage = it.localizedMessage)
            }
        }
    }

    fun rejectTransaction(txId: String, reason: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isActionLoading = true, errorMessage = null)
            val result = staffRepository.rejectTransaction(txId, "Karan Shah (Staff)", reason)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isActionLoading = false,
                    actionSuccessMessage = "Invoice has been marked as rejected."
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

// 3. Staff Electricians Search ViewModel
data class StaffElectriciansUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val electricians: List<ElectricianProfile> = emptyList(),
    val selectedElectrician: ElectricianProfile? = null
)

class StaffElectriciansViewModel(
    private val staffRepository: StaffRepository = RepositoryProvider.staffRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StaffElectriciansUiState())
    val uiState: StateFlow<StaffElectriciansUiState> = _uiState.asStateFlow()

    init {
        search("")
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        search(query)
    }

    fun search(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            staffRepository.searchElectricians(query).collect { list ->
                _uiState.value = _uiState.value.copy(isLoading = false, electricians = list)
            }
        }
    }

    fun loadElectricianDetail(id: String) {
        viewModelScope.launch {
            staffRepository.getElectricianDetail(id).collect { profile ->
                _uiState.value = _uiState.value.copy(selectedElectrician = profile)
            }
        }
    }
}

// 4. Staff Add Transaction ViewModel
data class StaffAddTxUiState(
    val electricianId: String = "ELC-2026-047",
    val amount: String = "",
    val invoiceNumber: String = "",
    val invoiceDate: String = "Today",
    val notes: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

class StaffAddTxViewModel(
    private val staffRepository: StaffRepository = RepositoryProvider.staffRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StaffAddTxUiState())
    val uiState: StateFlow<StaffAddTxUiState> = _uiState.asStateFlow()

    fun onElectricianIdChange(id: String) { _uiState.value = _uiState.value.copy(electricianId = id) }
    fun onAmountChange(amt: String) { _uiState.value = _uiState.value.copy(amount = amt.filter { it.isDigit() }) }
    fun onInvoiceNumberChange(num: String) { _uiState.value = _uiState.value.copy(invoiceNumber = num) }
    fun onNotesChange(notes: String) { _uiState.value = _uiState.value.copy(notes = notes) }

    fun submit() {
        val state = _uiState.value
        val amtLong = state.amount.toLongOrNull()
        if (amtLong == null || amtLong <= 0) {
            _uiState.value = state.copy(errorMessage = "Enter a valid purchase amount.")
            return
        }
        if (state.invoiceNumber.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Enter tax invoice number.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            val result = staffRepository.createTransaction(
                electricianId = state.electricianId.trim(),
                amount = amtLong,
                invoiceNumber = state.invoiceNumber.trim(),
                invoiceDate = state.invoiceDate,
                notes = state.notes.ifBlank { null }
            )
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, success = true)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = it.localizedMessage)
            }
        }
    }

    fun reset() {
        _uiState.value = StaffAddTxUiState()
    }
}
