package com.example.domain.repository

import com.example.data.model.Vehicle
import com.example.domain.model.AiDiagnosticReport
import com.example.domain.model.DomainFreezeFrame
import com.example.domain.model.DtcFault

/**
 * Domain repository contract for AI Predictive Diagnostics.
 */
interface AiDiagnosticRepository {

    suspend fun generateDiagnosticReport(
        fault: DtcFault,
        freezeFrame: DomainFreezeFrame?,
        vehicle: Vehicle?
    ): AiDiagnosticReport
}
