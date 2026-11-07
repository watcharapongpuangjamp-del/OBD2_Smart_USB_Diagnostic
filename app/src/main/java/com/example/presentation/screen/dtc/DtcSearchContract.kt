package com.example.presentation.screen.dtc

import com.example.domain.model.DiagnosticCode
import com.example.domain.model.DomainDtcCategory
import com.example.domain.model.DomainDtcSeverity
import com.example.presentation.common.UiEffect
import com.example.presentation.common.UiEvent
import com.example.presentation.common.UiState

data class DtcSearchUiState(
    val searchQuery: String = "",
    val selectedCategory: DomainDtcCategory? = null,
    val selectedSeverity: DomainDtcSeverity? = null,
    val dtcList: List<DiagnosticCode> = emptyList(),
    val selectedDtc: DiagnosticCode? = null,
    val isLoading: Boolean = false,
    val totalDatabaseCount: Int = 0,
    val isOfflineReady: Boolean = true,
    val isThaiLanguage: Boolean = true
) : UiState {
    val filteredCount: Int get() = dtcList.size
}

sealed interface DtcSearchUiEvent : UiEvent {
    data class SearchQueryChanged(val query: String) : DtcSearchUiEvent
    data class CategorySelected(val category: DomainDtcCategory?) : DtcSearchUiEvent
    data class SeveritySelected(val severity: DomainDtcSeverity?) : DtcSearchUiEvent
    data class DtcClicked(val dtc: DiagnosticCode) : DtcSearchUiEvent
    object DismissDetail : DtcSearchUiEvent
    object ClearFilters : DtcSearchUiEvent
    object ToggleLanguage : DtcSearchUiEvent
}

sealed interface DtcSearchUiEffect : UiEffect {
    data class ShowToast(val message: String) : DtcSearchUiEffect
}
