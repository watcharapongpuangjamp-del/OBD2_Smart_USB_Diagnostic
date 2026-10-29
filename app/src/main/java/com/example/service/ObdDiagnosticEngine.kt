package com.example.service

import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import com.example.data.model.TerminalLogEntry
import com.example.data.repository.DtcDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID

enum class ObdConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    PROTOCOL_DETECTING,
    ERROR
}

enum class DiagnosticScanStatus {
    IDLE,
    SCANNING,
    SUCCESS,
    NO_DTC,
    TIMEOUT,
    UNSUPPORTED,
    COMMUNICATION_ERROR
}

enum class DtcStatus(val labelTh: String, val labelEn: String) {
    CONFIRMED("ยืนยันแล้ว (Mode 03)", "Confirmed (Mode 03)"),
    PENDING("รอดำเนินการ (Mode 07)", "Pending (Mode 07)"),
    PERMANENT("ถาวร (Mode 0A)", "Permanent (Mode 0A)"),
    UNKNOWN("ไม่ระบุ", "Unknown")
}

data class AgonisticDtcResult(
    val code: String,
    val status: DtcStatus,
    val ecuHeader: String = "7E0",
    val rawHexBytes: String = "",
    val descriptionTh: String = "",
    val descriptionEn: String = ""
)

data class DiagnosticScanResult(
    val sessionId: String,
    val timestamp: Long,
    val protocol: String,
    val status: DiagnosticScanStatus,
    val dtcList: List<AgonisticDtcResult>,
    val rawEvidence: List<TerminalLogEntry>,
    val durationMs: Long
)

class ObdDiagnosticEngine {
    private val _connectionState = MutableStateFlow(ObdConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ObdConnectionState> = _connectionState.asStateFlow()

    private val _scanStatus = MutableStateFlow(DiagnosticScanStatus.IDLE)
    val scanStatus: StateFlow<DiagnosticScanStatus> = _scanStatus.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _currentScanStep = MutableStateFlow("Ready")
    val currentScanStep: StateFlow<String> = _currentScanStep.asStateFlow()

    private val _detectedProtocol = MutableStateFlow("UNKNOWN")
    val detectedProtocol: StateFlow<String> = _detectedProtocol.asStateFlow()

    /**
     * Executes a vehicle-agnostic diagnostic scan using physical USB connection (ELM327 protocol).
     */
    suspend fun executeDiagnosticScan(
        connection: UsbDeviceConnection?,
        outEp: UsbEndpoint?,
        inEp: UsbEndpoint?,
        timeoutMs: Int = 2000,
        maxRetries: Int = 2,
        onProgress: (Float, String) -> Unit = { _, _ -> }
    ): DiagnosticScanResult = withContext(Dispatchers.IO) {
        val sessionId = "SESSION-${System.currentTimeMillis()}-${UUID.randomUUID().toString().take(6).uppercase()}"
        val startTime = System.currentTimeMillis()
        val evidenceList = mutableListOf<TerminalLogEntry>()
        val detectedDtcs = mutableMapOf<String, AgonisticDtcResult>()

        if (connection == null || outEp == null || inEp == null) {
            _scanStatus.value = DiagnosticScanStatus.COMMUNICATION_ERROR
            return@withContext DiagnosticScanResult(
                sessionId = sessionId,
                timestamp = startTime,
                protocol = "NONE",
                status = DiagnosticScanStatus.COMMUNICATION_ERROR,
                dtcList = emptyList(),
                rawEvidence = listOf(
                    TerminalLogEntry(
                        isTx = false,
                        command = "DIAG_INIT",
                        rawResponse = "COMM_ERROR_NULL_CONNECTION",
                        parsedMeaningTh = "ไม่พบการเชื่อมต่อฮาร์ดแวร์ USB ที่ใช้งานได้",
                        parsedMeaningEn = "No active USB hardware connection found",
                        isSuccess = false
                    )
                ),
                durationMs = System.currentTimeMillis() - startTime
            )
        }

        _scanStatus.value = DiagnosticScanStatus.SCANNING
        _connectionState.value = ObdConnectionState.PROTOCOL_DETECTING

        // Step 1: Detect Protocol using AT DP / AT DPN
        updateProgress(0.1f, "Detecting OBD Protocol (AT DP)...", onProgress)
        val protoResp = sendWithRetry(connection, outEp, inEp, "AT DP\r", timeoutMs, maxRetries)
        evidenceList.add(protoResp.second)
        val protocolName = cleanResponse(protoResp.first)
        _detectedProtocol.value = if (protocolName.isNotBlank()) protocolName else "AUTO ISO 15765-4"

        // Step 2: Validate ECU Link & Supported PIDs (0100)
        updateProgress(0.3f, "Validating ECU Response (0100)...", onProgress)
        val pid00 = sendWithRetry(connection, outEp, inEp, "0100\r", timeoutMs, maxRetries)
        evidenceList.add(pid00.second)
        
        if (!pid00.second.isSuccess || pid00.first.contains("NO DATA", true) || pid00.first.contains("ERROR", true)) {
            _scanStatus.value = DiagnosticScanStatus.TIMEOUT
            return@withContext DiagnosticScanResult(
                sessionId = sessionId,
                timestamp = startTime,
                protocol = _detectedProtocol.value,
                status = DiagnosticScanStatus.TIMEOUT,
                dtcList = emptyList(),
                rawEvidence = evidenceList,
                durationMs = System.currentTimeMillis() - startTime
            )
        }

        _connectionState.value = ObdConnectionState.CONNECTED

        // Step 3: Scan Confirmed DTCs (Mode 03)
        updateProgress(0.5f, "Scanning Confirmed DTCs (Mode 03)...", onProgress)
        val mode03 = sendWithRetry(connection, outEp, inEp, "03\r", timeoutMs, maxRetries)
        evidenceList.add(mode03.second)
        parseDtcResponse(mode03.first, DtcStatus.CONFIRMED, detectedDtcs)

        // Step 4: Scan Pending DTCs (Mode 07)
        updateProgress(0.7f, "Scanning Pending DTCs (Mode 07)...", onProgress)
        val mode07 = sendWithRetry(connection, outEp, inEp, "07\r", timeoutMs, maxRetries)
        evidenceList.add(mode07.second)
        parseDtcResponse(mode07.first, DtcStatus.PENDING, detectedDtcs)

        // Step 5: Scan Permanent DTCs (Mode 0A)
        updateProgress(0.9f, "Scanning Permanent DTCs (Mode 0A)...", onProgress)
        val mode0A = sendWithRetry(connection, outEp, inEp, "0A\r", timeoutMs, maxRetries)
        evidenceList.add(mode0A.second)
        parseDtcResponse(mode0A.first, DtcStatus.PERMANENT, detectedDtcs)

        updateProgress(1.0f, "Scan Complete", onProgress)

        val dtcList = detectedDtcs.values.toList()
        val finalStatus = if (dtcList.isNotEmpty()) {
            DiagnosticScanStatus.SUCCESS
        } else {
            DiagnosticScanStatus.NO_DTC
        }

        _scanStatus.value = finalStatus

        DiagnosticScanResult(
            sessionId = sessionId,
            timestamp = startTime,
            protocol = _detectedProtocol.value,
            status = finalStatus,
            dtcList = dtcList,
            rawEvidence = evidenceList,
            durationMs = System.currentTimeMillis() - startTime
        )
    }

    private fun updateProgress(progress: Float, step: String, callback: (Float, String) -> Unit) {
        _scanProgress.value = progress
        _currentScanStep.value = step
        callback(progress, step)
    }

    private fun sendWithRetry(
        connection: UsbDeviceConnection,
        outEp: UsbEndpoint,
        inEp: UsbEndpoint,
        command: String,
        timeoutMs: Int,
        maxRetries: Int
    ): Pair<String, TerminalLogEntry> {
        var attempt = 0
        var lastResp = ""
        val startTime = System.currentTimeMillis()

        while (attempt <= maxRetries) {
            try {
                val writeBytes = command.toByteArray(Charsets.US_ASCII)
                val written = connection.bulkTransfer(outEp, writeBytes, writeBytes.size, timeoutMs)
                if (written < 0) {
                    attempt++
                    continue
                }

                val buffer = ByteArray(512)
                val sb = StringBuilder()
                val readStart = System.currentTimeMillis()

                while (System.currentTimeMillis() - readStart < timeoutMs) {
                    val readCount = connection.bulkTransfer(inEp, buffer, buffer.size, 100)
                    if (readCount > 0) {
                        val chunk = String(buffer, 0, readCount, Charsets.US_ASCII)
                        sb.append(chunk)
                        if (chunk.contains(">")) break
                    }
                }

                lastResp = sb.toString().trim()
                val latency = System.currentTimeMillis() - startTime
                val success = lastResp.isNotBlank() && !lastResp.contains("ERROR", true)

                if (success || attempt == maxRetries) {
                    val entry = TerminalLogEntry(
                        isTx = false,
                        command = command.trim(),
                        rawResponse = lastResp,
                        parsedMeaningTh = if (success) "สำเร็จ (Latency: ${latency}ms)" else "คำสั่งไม่สำเร็จหรือเกิดข้อผิดพลาดจากฮาร์ดแวร์",
                        parsedMeaningEn = if (success) "Success (Latency: ${latency}ms)" else "Command failed or hardware error",
                        latencyMs = latency,
                        isSuccess = success
                    )
                    return Pair(lastResp, entry)
                }
            } catch (e: Exception) {
                // Retry on exception
            }
            attempt++
            Thread.sleep(100)
        }

        val latency = System.currentTimeMillis() - startTime
        val entry = TerminalLogEntry(
            isTx = false,
            command = command.trim(),
            rawResponse = lastResp.ifBlank { "TIMEOUT" },
            parsedMeaningTh = "หมดเวลาการรอตอบสนองจาก ECU (Timeout)",
            parsedMeaningEn = "ECU Response Timeout",
            latencyMs = latency,
            isSuccess = false
        )
        return Pair(lastResp, entry)
    }

    private fun cleanResponse(raw: String): String {
        return raw.replace(">", "").replace("\r", " ").replace("\n", " ").trim()
    }

    private fun parseDtcResponse(raw: String, status: DtcStatus, outMap: MutableMap<String, AgonisticDtcResult>) {
        val cleaned = cleanResponse(raw)
        val tokens = cleaned.split(Regex("\\s+"))
        var i = 0
        while (i < tokens.size) {
            val token = tokens[i].uppercase()
            if (token == "43" || token == "47" || token == "4A") {
                i++
                while (i + 1 < tokens.size) {
                    val b1Str = tokens[i]
                    val b2Str = tokens[i + 1]
                    if (b1Str.length == 2 && b2Str.length == 2) {
                        try {
                            val b1 = b1Str.toInt(16)
                            val b2 = b2Str.toInt(16)
                            if (b1 == 0 && b2 == 0) {
                                i += 2
                                continue
                            }
                            val code = decodeDtcBytes(b1, b2)
                            if (code.isNotBlank()) {
                                val info = DtcDatabase.findByCode(code)
                                outMap[code] = AgonisticDtcResult(
                                    code = code,
                                    status = status,
                                    rawHexBytes = "$b1Str $b2Str",
                                    descriptionTh = info?.thDescription ?: "รหัสข้อผิดพลาดมาตรฐาน OBD-II ($code)",
                                    descriptionEn = info?.enDescription ?: "Standard OBD-II Diagnostic Trouble Code ($code)"
                                )
                            }
                        } catch (e: Exception) {
                            // Parsing error for this pair
                        }
                        i += 2
                    } else {
                        break
                    }
                }
            } else {
                i++
            }
        }
    }

    private fun decodeDtcBytes(b1: Int, b2: Int): String {
        val typeBits = (b1 shr 6) and 0x03
        val typeChar = when (typeBits) {
            0 -> 'P'
            1 -> 'C'
            2 -> 'B'
            3 -> 'U'
            else -> 'P'
        }
        val firstDigit = (b1 shr 4) and 0x03
        val secondDigit = b1 and 0x0F
        val thirdDigit = (b2 shr 4) and 0x0F
        val fourthDigit = b2 and 0x0F
        return "$typeChar$firstDigit$secondDigit$thirdDigit$fourthDigit"
    }
}
