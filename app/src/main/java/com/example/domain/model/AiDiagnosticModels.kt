package com.example.domain.model

import java.io.File

/**
 * Step in a structured DIY automotive repair guide.
 */
data class DiyRepairStep(
    val stepNumber: Int,
    val title: String,
    val instruction: String,
    val caution: String? = null,
    val torqueSpec: String? = null
)

/**
 * Price estimate for vehicle replacement parts and labor in Thai Baht (THB).
 */
data class PartPriceEstimate(
    val partNameTh: String,
    val partNameEn: String,
    val oemPriceThb: Int,
    val aftermarketPriceThb: Int,
    val laborCostThb: Int
) {
    val totalEstimatedOemThb: Int
        get() = oemPriceThb + laborCostThb

    val totalEstimatedAftermarketThb: Int
        get() = aftermarketPriceThb + laborCostThb
}

/**
 * Comprehensive AI diagnostic report produced by Gemini Deep-Reasoning engine.
 */
data class AiDiagnosticReport(
    val dtcCode: String,
    val vehicleModel: String,
    val rootCauseSummary: String,
    val probabilityScore: Int, // 0 - 100%
    val sensorCorrelation: String,
    val severityLevel: DtcSeverity,
    val toolsRequired: List<String>,
    val estimatedMinutes: Int,
    val diyDifficulty: String, // ง่าย (Easy), ปานกลาง (Medium), ช่างผู้ชำนาญ (Pro)
    val repairSteps: List<DiyRepairStep>,
    val priceEstimates: List<PartPriceEstimate>,
    val safetyNotes: List<String>,
    val generatedAtMillis: Long = System.currentTimeMillis()
)

/**
 * Exported PDF diagnostic document details.
 */
data class DiagnosticPdfResult(
    val file: File,
    val fileName: String,
    val fileSizeFormatted: String,
    val pageCount: Int,
    val timestamp: Long = System.currentTimeMillis()
)
