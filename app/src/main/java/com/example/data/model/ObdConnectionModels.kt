package com.example.data.model

enum class AppOperationMode(
    val titleTh: String,
    val titleEn: String,
    val subtitleTh: String,
    val subtitleEn: String,
    val descriptionTh: String,
    val descriptionEn: String
) {
    REAL_HARDWARE(
        titleTh = "โหมดใช้งานจริง (ฮาร์ดแวร์จริง)",
        titleEn = "Real Hardware Mode",
        subtitleTh = "เชื่อมต่อสาย USB OTG / กล่องสแกนเนอร์จริง",
        subtitleEn = "Physical USB OTG / Bluetooth Adapter",
        descriptionTh = "อ่านค่าสดและวินิจฉัยจากกล่อง ECU รถยนต์จริงผ่านพอร์ต USB OTG หรือบลูทูธ (จะไม่แสดงข้อมูลจำลองหากไม่ได้ต่อสาย)",
        descriptionEn = "Live stream & scan directly from vehicle ECU via physical USB OTG or Bluetooth adapter."
    ),
    SIMULATOR(
        titleTh = "โหมดจำลอง (Demo Simulator)",
        titleEn = "Simulator Demo Mode",
        subtitleTh = "จำลองการส่งข้อมูล CAN และโค้ดข้อบกพร่อง",
        subtitleEn = "Virtual CAN Engine & Fault Scenarios",
        descriptionTh = "จำลองการขับขี่ อุณหภูมิ และโค้ดไฟเตือน เพื่อทดสอบฟังก์ชันและปรึกษา AI Mechanic โดยไม่ต้องต่อสายฮาร์ดแวร์",
        descriptionEn = "Simulate vehicle telemetry, sensor streams, and diagnostic fault scenarios for testing without a physical car."
    )
}

enum class ObdConnectionType(
    val titleTh: String,
    val titleEn: String,
    val descriptionTh: String,
    val descriptionEn: String
) {
    USB_OTG(
        titleTh = "สาย USB (OTG / FTDI / CH340)",
        titleEn = "USB Cable (OTG / FTDI / CH340)",
        descriptionTh = "เชื่อมต่อผ่านสาย USB OTG เสถียรและเร็วที่สุด ตอบสนองไว เหมาะกับงานจูนและสแกนลึก",
        descriptionEn = "High-speed wired USB OTG connection. Ultra-low latency for tuning & deep scan."
    ),
    BLUETOOTH(
        titleTh = "บลูทูธคลาสสิก (Bluetooth 2.0/3.0)",
        titleEn = "Bluetooth Classic (ELM327 / vLinker)",
        descriptionTh = "รองรับหัวสแกน ELM327, vLinker FD+, OBDLink LX ทั่วไป",
        descriptionEn = "Standard Bluetooth SPP connection for ELM327 & vLinker adapters."
    ),
    BLE(
        titleTh = "บลูทูธพลังงานต่ำ (Bluetooth Low Energy / BLE 4.0+)",
        titleEn = "Bluetooth Low Energy (BLE 4.0/5.0)",
        descriptionTh = "สำหรับหัวสแกนรุ่นใหม่ ประหยัดพลังงาน (vLinker MC+, Carista)",
        descriptionEn = "Low-energy Bluetooth for modern adapters like vLinker MC+ & Carista."
    ),
    WIFI(
        titleTh = "ไวไฟ (Wi-Fi TCP/IP)",
        titleEn = "Wi-Fi Adapter (TCP/IP)",
        descriptionTh = "เชื่อมต่อผ่านเครือข่าย Wi-Fi เฉพาะตัวหัวสแกน (192.168.0.10:35000)",
        descriptionEn = "Direct Wi-Fi access point connection to OBD-II adapter."
    ),
    SIMULATOR(
        titleTh = "โหมดจำลองสถานการณ์ (Virtual CAN Engine)",
        titleEn = "Demo Simulator (Virtual CAN Engine)",
        descriptionTh = "จำลองข้อมูลเซนเซอร์สดและการขับขี่จริง สำหรับทดสอบฟังก์ชัน",
        descriptionEn = "Simulated high-frequency CAN bus telemetry for demo and testing."
    )
}

enum class ObdProtocol(
    val code: String,
    val displayNameTh: String,
    val displayNameEn: String,
    val atCommand: String
) {
    AUTO("0", "อัตโนมัติ (Auto Detect All Protocols)", "Automatic (Auto Search)", "ATSP0"),
    CAN_11_500("6", "ISO 15765-4 CAN (11 bit ID, 500 kbaud)", "ISO 15765-4 CAN (11 bit, 500k)", "ATSP6"),
    CAN_29_500("7", "ISO 15765-4 CAN (29 bit ID, 500 kbaud)", "ISO 15765-4 CAN (29 bit, 500k)", "ATSP7"),
    CAN_11_250("8", "ISO 15765-4 CAN (11 bit ID, 250 kbaud)", "ISO 15765-4 CAN (11 bit, 250k)", "ATSP8"),
    CAN_29_250("9", "ISO 15765-4 CAN (29 bit ID, 250 kbaud)", "ISO 15765-4 CAN (29 bit, 250k)", "ATSP9"),
    KWP_FAST("5", "ISO 14230-4 KWP (Fast Init, 10.4 kbaud)", "ISO 14230-4 KWP (Fast Init)", "ATSP5"),
    KWP_5BAUD("4", "ISO 14230-4 KWP (5 baud Init, 10.4 kbaud)", "ISO 14230-4 KWP (5 Baud)", "ATSP4"),
    ISO9141("3", "ISO 9141-2 (5 baud Init, 10.4 kbaud)", "ISO 9141-2 (K-Line 5 Baud)", "ATSP3")
}

data class UsbSerialDeviceInfo(
    val deviceId: Int,
    val deviceName: String,
    val vendorId: Int,
    val productId: Int,
    val chipsetName: String,
    val manufacturerName: String,
    val productName: String,
    val serialNumber: String,
    val hasPermission: Boolean,
    val isRecommendedForObd: Boolean = true
)

data class TerminalLogEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val isTx: Boolean, // true = sent to OBD2, false = received from OBD2
    val command: String,
    val rawResponse: String = "",
    val parsedMeaningTh: String = "",
    val parsedMeaningEn: String = "",
    val latencyMs: Long = 0,
    val isSuccess: Boolean = true
)

data class UsbConnectionStats(
    val isConnected: Boolean = false,
    val activeChipset: String = "Not Connected",
    val activePort: String = "-",
    val baudRate: Int = 115200,
    val activeProtocol: String = "ISO 15765-4 (CAN 11/500k)",
    val pidsPerSecond: Float = 0.0f,
    val totalBytesReceived: Long = 0,
    val totalPacketsSent: Long = 0,
    val totalPacketsReceived: Long = 0,
    val averageLatencyMs: Int = 12,
    val bufferErrors: Int = 0,
    val busLoadPercent: Float = 0.0f
)
