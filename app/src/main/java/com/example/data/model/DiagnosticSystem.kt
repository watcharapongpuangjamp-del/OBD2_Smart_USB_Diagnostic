package com.example.data.model

enum class DiagnosticSystem(
    val id: String,
    val titleTh: String,
    val titleEn: String,
    val dtcCategory: DtcCategory,
    val ecuName: String,
    val protocol: String
) {
    ALL(
        id = "all",
        titleTh = "ทุกระบบ (Master Vehicle Scan)",
        titleEn = "All Vehicle Systems",
        dtcCategory = DtcCategory.POWERTRAIN,
        ecuName = "Full CAN Gateway / Multiplex",
        protocol = "ISO 15765-4 CAN (500 kbps)"
    ),
    ENGINE(
        id = "engine",
        titleTh = "โมดูลเครื่องยนต์ (Engine System ECM)",
        titleEn = "Engine System (ECM/PCM)",
        dtcCategory = DtcCategory.POWERTRAIN,
        ecuName = "Engine Control Module (ECM)",
        protocol = "CAN 11-bit 500k / UDS 0x7E0"
    ),
    POWERTRAIN(
        id = "powertrain",
        titleTh = "โมดูลระบบส่งกำลัง (Powertrain & TCM)",
        titleEn = "Powertrain System (TCM & Drivetrain)",
        dtcCategory = DtcCategory.POWERTRAIN,
        ecuName = "Transmission & Powertrain Module (TCM)",
        protocol = "CAN 11-bit 500k / UDS 0x7E1"
    ),
    CHASSIS(
        id = "chassis",
        titleTh = "โมดูลแชสซีส์ & เบรก (Chassis ABS/ESC)",
        titleEn = "Chassis System (ABS, ESC, SAS)",
        dtcCategory = DtcCategory.CHASSIS,
        ecuName = "Electronic Brake & Chassis Module (EBCM)",
        protocol = "CAN 11-bit 500k / UDS 0x7E2"
    ),
    ELECTRICAL(
        id = "electrical",
        titleTh = "โมดูลไฟฟ้า & ตัวถัง (Electrical & BCM)",
        titleEn = "Electrical System (BCM & Network)",
        dtcCategory = DtcCategory.BODY,
        ecuName = "Body & Electrical Control Module (BCM)",
        protocol = "CAN 11-bit 500k / UDS 0x7E4"
    ),
    TRANSMISSION(
        id = "transmission",
        titleTh = "ระบบเกียร์ออโต้ / CVT (TCM)",
        titleEn = "Transmission Control (TCM)",
        dtcCategory = DtcCategory.POWERTRAIN,
        ecuName = "Transmission Control Module (TCM)",
        protocol = "CAN 11-bit 500k / UDS 0x7E1"
    ),
    ABS(
        id = "abs",
        titleTh = "ระบบเบรก ABS & ควบคุมการทรงตัว",
        titleEn = "Anti-Lock Brakes & ESC (ABS)",
        dtcCategory = DtcCategory.CHASSIS,
        ecuName = "Electronic Brake Control Module (EBCM)",
        protocol = "CAN 11-bit 500k / UDS 0x7E2"
    ),
    AIRBAG(
        id = "airbag",
        titleTh = "ระบบถุงลมนิรภัย (SRS)",
        titleEn = "Airbag & Restraints (SRS)",
        dtcCategory = DtcCategory.BODY,
        ecuName = "Supplemental Restraint System (SRS)",
        protocol = "CAN 11-bit 500k / UDS 0x7E3"
    ),
    BODY(
        id = "body",
        titleTh = "ระบบควบคุมตัวถังและไฟฟ้า (BCM)",
        titleEn = "Body Control Module (BCM)",
        dtcCategory = DtcCategory.BODY,
        ecuName = "Body Control Module (BCM)",
        protocol = "CAN 11-bit 500k / UDS 0x7E4"
    ),
    NETWORK(
        id = "network",
        titleTh = "ระบบเครือข่ายสื่อสาร CAN Bus",
        titleEn = "CAN Network & Gateway",
        dtcCategory = DtcCategory.NETWORK,
        ecuName = "Central Gateway Module (CGW)",
        protocol = "CAN High-Speed 500k Gateway"
    ),
    IMMOBILIZER(
        id = "immobilizer",
        titleTh = "ระบบอิมโมบิไลเซอร์ & กุญแจชิพ (IMMO)",
        titleEn = "Immobilizer & Smart Key (IMMO)",
        dtcCategory = DtcCategory.BODY,
        ecuName = "Immobilizer Control Unit (ICU)",
        protocol = "CAN 11-bit 500k / UDS 0x7E5"
    )
}

