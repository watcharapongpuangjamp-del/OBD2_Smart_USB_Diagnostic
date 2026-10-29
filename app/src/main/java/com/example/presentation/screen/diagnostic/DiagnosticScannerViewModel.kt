package com.example.presentation.screen.diagnostic

import androidx.lifecycle.viewModelScope
import com.example.data.repository.AiDiagnosticRepositoryImpl
import com.example.data.repository.AiMechanicRepository
import com.example.domain.model.DiagnosticResult
import com.example.domain.model.DtcFault
import com.example.domain.model.DtcSeverity
import com.example.domain.usecase.ClearDtcCodesUseCase
import com.example.domain.usecase.ExportDiagnosticPdfUseCase
import com.example.domain.usecase.GenerateAiDiagnosticReportUseCase
import com.example.domain.usecase.ScanDtcCodesUseCase
import com.example.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * MVI ViewModel powering the OBD-II Diagnostic & DTC Scanner Screen.
 * Implements strict Clean Architecture by executing domain UseCases.
 */
class DiagnosticScannerViewModel(
    private val scanDtcCodesUseCase: ScanDtcCodesUseCase,
    private val clearDtcCodesUseCase: ClearDtcCodesUseCase,
    private val generateAiDiagnosticReportUseCase: GenerateAiDiagnosticReportUseCase = GenerateAiDiagnosticReportUseCase(AiDiagnosticRepositoryImpl()),
    private val exportDiagnosticPdfUseCase: ExportDiagnosticPdfUseCase? = null,
    private val aiMechanicRepository: AiMechanicRepository = AiMechanicRepository()
) : BaseViewModel<DiagnosticScannerUiState, DiagnosticScannerUiEvent, DiagnosticScannerUiEffect>(
    initialState = DiagnosticScannerUiState()
) {

    init {
        // Auto-run initial system scan on creation
        onEvent(DiagnosticScannerUiEvent.StartScan)
    }

    override fun onEvent(event: DiagnosticScannerUiEvent) {
        when (event) {
            is DiagnosticScannerUiEvent.StartScan -> performScan()
            is DiagnosticScannerUiEvent.ClearFaultCodes -> clearFaults()
            is DiagnosticScannerUiEvent.SelectCategoryFilter -> {
                setState { copy(selectedCategoryFilter = event.category) }
            }
            is DiagnosticScannerUiEvent.SelectFault -> {
                setState { copy(selectedFault = event.fault) }
            }
            is DiagnosticScannerUiEvent.RequestAiDiagnosis -> generateAiInsight(event.fault)
            is DiagnosticScannerUiEvent.OpenDiyRepairGuide -> {
                setState { copy(showDiyGuideDialog = true) }
            }
            is DiagnosticScannerUiEvent.DismissDiyRepairGuide -> {
                setState { copy(showDiyGuideDialog = false) }
            }
            is DiagnosticScannerUiEvent.ExportPdfReport -> exportPdf()
            is DiagnosticScannerUiEvent.DismissPdfDialog -> {
                setState { copy(pdfResult = null) }
            }
            is DiagnosticScannerUiEvent.DismissError -> {
                setState { copy(errorMessage = null) }
            }
        }
    }

    private fun performScan() {
        viewModelScope.launch {
            setState {
                copy(
                    isScanning = true,
                    scanProgress = 0f,
                    currentProgressTask = "กำลังเริ่มต้นสแกน...",
                    errorMessage = null
                )
            }

            scanDtcCodesUseCase().collectLatest { result ->
                when (result) {
                    is DiagnosticResult.InProgress -> {
                        setState {
                            copy(
                                scanProgress = result.progressPercent,
                                currentProgressTask = result.currentTask
                            )
                        }
                    }
                    is DiagnosticResult.Success -> {
                        val session = result.data
                        val firstFault = session.faults.firstOrNull()
                        setState {
                            copy(
                                isScanning = false,
                                scanProgress = 1.0f,
                                detectedFaults = session.faults,
                                activeSession = session,
                                freezeFrame = session.freezeFrame,
                                selectedFault = firstFault
                            )
                        }
                        if (session.faults.any { it.severity == DtcSeverity.CRITICAL }) {
                            sendEffect(DiagnosticScannerUiEffect.PlayAlertSound(isCritical = true))
                        }
                        sendEffect(DiagnosticScannerUiEffect.ShowToast("สแกนระบบเรียบร้อย: พบ ${session.faults.size} รายการ"))

                        // Proactively prepare initial AI Diagnosis for the primary critical fault
                        if (firstFault != null) {
                            generateAiReportSilent(firstFault, session.freezeFrame)
                        }
                    }
                    is DiagnosticResult.Error -> {
                        setState {
                            copy(
                                isScanning = false,
                                errorMessage = result.message
                            )
                        }
                        sendEffect(DiagnosticScannerUiEffect.ShowToast("การสแกนขัดข้อง: ${result.message}"))
                    }
                }
            }
        }
    }

    private fun clearFaults() {
        viewModelScope.launch {
            setState { copy(isClearing = true) }
            when (val result = clearDtcCodesUseCase()) {
                is DiagnosticResult.Success -> {
                    setState {
                        copy(
                            isClearing = false,
                            detectedFaults = emptyList(),
                            selectedFault = null,
                            aiReport = null,
                            aiInsightText = null
                        )
                    }
                    sendEffect(DiagnosticScannerUiEffect.ShowToast("ลบรหัสข้อผิดพลาดและรีเซ็ตไฟเครื่องยนต์เรียบร้อย"))
                }
                is DiagnosticResult.Error -> {
                    setState {
                        copy(
                            isClearing = false,
                            errorMessage = result.message
                        )
                    }
                    sendEffect(DiagnosticScannerUiEffect.ShowToast(result.message))
                }
                is DiagnosticResult.InProgress -> Unit
            }
        }
    }

    private fun generateAiReportSilent(fault: DtcFault, freezeFrame: com.example.domain.model.DomainFreezeFrame?) {
        viewModelScope.launch {
            try {
                val report = generateAiDiagnosticReportUseCase(fault, freezeFrame, null)
                setState {
                    copy(
                        aiReport = report,
                        aiInsightText = report.rootCauseSummary
                    )
                }
            } catch (e: Exception) {
                // Non-blocking background preparation
            }
        }
    }

    private fun generateAiInsight(fault: DtcFault) {
        viewModelScope.launch {
            setState { copy(isAiThinking = true, showDiyGuideDialog = true) }
            try {
                val report = generateAiDiagnosticReportUseCase(fault, currentState.freezeFrame, null)
                setState {
                    copy(
                        isAiThinking = false,
                        aiReport = report,
                        aiInsightText = report.rootCauseSummary,
                        showDiyGuideDialog = true
                    )
                }
                sendEffect(DiagnosticScannerUiEffect.ShowToast("วิเคราะห์เชิงลึก AI สำเร็จ: ${report.rootCauseSummary.take(35)}..."))
            } catch (e: Exception) {
                setState { copy(isAiThinking = false) }
                sendEffect(DiagnosticScannerUiEffect.ShowToast("การวิเคราะห์ขัดข้อง: ${e.localizedMessage}"))
            }
        }
    }

    private fun exportPdf() {
        val session = currentState.activeSession
        if (session == null) {
            sendEffect(DiagnosticScannerUiEffect.ShowToast("ไม่พบข้อมูลผลการสแกน กรุณาสแกนก่อนพิมพ์รายงาน"))
            return
        }

        val pdfUseCase = exportDiagnosticPdfUseCase
        if (pdfUseCase == null) {
            sendEffect(DiagnosticScannerUiEffect.ShowToast("กำลังเตรียมเครื่องพิมพ์ PDF..."))
            return
        }

        viewModelScope.launch {
            setState { copy(isGeneratingPdf = true) }
            try {
                val pdfResult = pdfUseCase(
                    session = session,
                    aiReport = currentState.aiReport,
                    vehicle = null
                )
                setState {
                    copy(
                        isGeneratingPdf = false,
                        pdfResult = pdfResult
                    )
                }
                sendEffect(DiagnosticScannerUiEffect.SharePdfFile(pdfResult.file))
                sendEffect(DiagnosticScannerUiEffect.ShowToast("สร้างเอกสาร PDF เรียบร้อย: ${pdfResult.fileName}"))
            } catch (e: Exception) {
                setState { copy(isGeneratingPdf = false) }
                sendEffect(DiagnosticScannerUiEffect.ShowToast("สร้างเอกสาร PDF ล้มเหลว: ${e.localizedMessage}"))
            }
        }
    }
}
