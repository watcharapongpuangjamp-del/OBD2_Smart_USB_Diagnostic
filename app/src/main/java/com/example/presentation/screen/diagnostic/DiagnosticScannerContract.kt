package com.example.presentation.screen.diagnostic

import com.example.domain.model.AiDiagnosticReport
import com.example.domain.model.DiagnosticPdfResult
import com.example.domain.model.DiagnosticSession
import com.example.domain.model.DomainFreezeFrame
import com.example.domain.model.DtcFault
import com.example.domain.model.VehicleSystemCategory
import com.example.presentation.common.UiEffect
import com.example.presentation.common.UiEvent
import com.example.presentation.common.UiState
import java.io.File

data class DiagnosticScannerUiState(
    val isScanning: Boolean = false,
    val scanProgress: Float = 0f,
    val currentProgressTask: String = "",
    val detectedFaults: List<DtcFault> = emptyList(),
    val selectedFault: DtcFault? = null,
    val activeSession: DiagnosticSession? = null,
    val freezeFrame: DomainFreezeFrame? = null,
    val selectedCategoryFilter: VehicleSystemCategory? = null,
    val isClearing: Boolean = false,
    val errorMessage: String? = null,
    val aiInsightText: String? = null,
    val isAiThinking: Boolean = false,
    val aiReport: AiDiagnosticReport? = null,
    val showDiyGuideDialog: Boolean = false,
    val isGeneratingPdf: Boolean = false,
    val pdfResult: DiagnosticPdfResult? = null
) : UiState {
    val filteredFaults: List<DtcFault>
        get() = if (selectedCategoryFilter == null) {
            detectedFaults
        } else {
            detectedFaults.filter { it.category == selectedCategoryFilter }
        }

    val criticalCount: Int
        get() = detectedFaults.count { it.severity == com.example.domain.model.DtcSeverity.CRITICAL }

    val warningCount: Int
        get() = detectedFaults.count { it.severity == com.example.domain.model.DtcSeverity.WARNING }
}

sealed class DiagnosticScannerUiEvent : UiEvent {
    object StartScan : DiagnosticScannerUiEvent()
    object ClearFaultCodes : DiagnosticScannerUiEvent()
    data class SelectCategoryFilter(val category: VehicleSystemCategory?) : DiagnosticScannerUiEvent()
    data class SelectFault(val fault: DtcFault?) : DiagnosticScannerUiEvent()
    data class RequestAiDiagnosis(val fault: DtcFault) : DiagnosticScannerUiEvent()
    object OpenDiyRepairGuide : DiagnosticScannerUiEvent()
    object DismissDiyRepairGuide : DiagnosticScannerUiEvent()
    object ExportPdfReport : DiagnosticScannerUiEvent()
    object DismissPdfDialog : DiagnosticScannerUiEvent()
    object DismissError : DiagnosticScannerUiEvent()
}

sealed class DiagnosticScannerUiEffect : UiEffect {
    data class ShowToast(val message: String) : DiagnosticScannerUiEffect()
    data class PlayAlertSound(val isCritical: Boolean) : DiagnosticScannerUiEffect()
    data class SharePdfFile(val file: File) : DiagnosticScannerUiEffect()
    data class NavigateToAiDetail(val dtcCode: String) : DiagnosticScannerUiEffect()
}
