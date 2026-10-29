package com.example.domain.repository

import com.example.domain.model.DiagnosticResult
import com.example.domain.model.DiagnosticSession
import com.example.domain.model.DtcFault
import com.example.domain.model.SensorReading
import kotlinx.coroutines.flow.Flow

/**
 * Domain boundary interface for OBD-II Diagnostic Hardware & Protocol operations.
 * Clean, abstract contract completely decoupled from ELM327 Bluetooth/USB implementations.
 */
interface ObdDiagnosticRepository {

    /**
     * Executes an OBD-II diagnostic fault code scan across all vehicle ECUs (Engine, Transmission, ABS, Body).
     * Emits scan progress ([DiagnosticResult.InProgress]) and final results ([DiagnosticResult.Success]).
     */
    fun performFullSystemScan(): Flow<DiagnosticResult<DiagnosticSession>>

    /**
     * Issues an OBD-II Mode 04 command to clear DTC codes and reset the Malfunction Indicator Lamp (MIL).
     */
    suspend fun clearDtcCodes(): DiagnosticResult<Boolean>

    /**
     * Continuous stream of real-time sensor metrics (RPM, Speed, Coolant, Rail Pressure, Voltages).
     */
    fun observeLiveTelemetry(): Flow<List<SensorReading>>

    /**
     * Fetches current connection state of the hardware adapter (Disconnected, Connecting, Connected, Streaming).
     */
    fun observeConnectionStatus(): Flow<String>

    /**
     * Fetches details and Thai translation for a given DTC code.
     */
    suspend fun lookupDtcFault(code: String): DtcFault?
}
