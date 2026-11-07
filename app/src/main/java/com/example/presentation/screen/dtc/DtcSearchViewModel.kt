package com.example.presentation.screen.dtc

import androidx.lifecycle.viewModelScope
import com.example.domain.model.DomainDtcCategory
import com.example.domain.model.DomainDtcSeverity
import com.example.domain.usecase.GetDtcDetailsUseCase
import com.example.domain.usecase.SearchDtcUseCase
import com.example.domain.usecase.SeedDtcDatabaseUseCase
import com.example.presentation.common.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel managing offline Room SQLite DTC queries, filters, and detail presentations.
 */
class DtcSearchViewModel(
    private val searchDtcUseCase: SearchDtcUseCase,
    private val getDtcDetailsUseCase: GetDtcDetailsUseCase,
    private val seedDtcDatabaseUseCase: SeedDtcDatabaseUseCase
) : BaseViewModel<DtcSearchUiState, DtcSearchUiEvent, DtcSearchUiEffect>(DtcSearchUiState()) {

    private var searchJob: Job? = null

    init {
        initializeDatabase()
    }

    private fun initializeDatabase() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val count = seedDtcDatabaseUseCase()
            setState {
                copy(
                    totalDatabaseCount = count,
                    isOfflineReady = true,
                    isLoading = false
                )
            }
            triggerSearch()
        }
    }

    override fun onEvent(event: DtcSearchUiEvent) {
        when (event) {
            is DtcSearchUiEvent.SearchQueryChanged -> {
                setState { copy(searchQuery = event.query) }
                triggerSearch()
            }
            is DtcSearchUiEvent.CategorySelected -> {
                val newCategory = if (currentState.selectedCategory == event.category) null else event.category
                setState { copy(selectedCategory = newCategory) }
                triggerSearch()
            }
            is DtcSearchUiEvent.SeveritySelected -> {
                val newSeverity = if (currentState.selectedSeverity == event.severity) null else event.severity
                setState { copy(selectedSeverity = newSeverity) }
                triggerSearch()
            }
            is DtcSearchUiEvent.DtcClicked -> {
                setState { copy(selectedDtc = event.dtc) }
            }
            is DtcSearchUiEvent.DismissDetail -> {
                setState { copy(selectedDtc = null) }
            }
            is DtcSearchUiEvent.ClearFilters -> {
                setState {
                    copy(
                        searchQuery = "",
                        selectedCategory = null,
                        selectedSeverity = null
                    )
                }
                triggerSearch()
            }
            is DtcSearchUiEvent.ToggleLanguage -> {
                setState { copy(isThaiLanguage = !isThaiLanguage) }
            }
        }
    }

    private fun triggerSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchDtcUseCase(
                query = currentState.searchQuery,
                category = currentState.selectedCategory,
                severity = currentState.selectedSeverity
            ).catch { e ->
                sendEffect(DtcSearchUiEffect.ShowToast("เกิดข้อผิดพลาดในการค้นหา: ${e.message}"))
            }.collectLatest { results ->
                setState {
                    copy(
                        dtcList = results,
                        isLoading = false
                    )
                }
            }
        }
    }
}
