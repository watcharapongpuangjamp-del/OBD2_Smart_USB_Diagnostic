package com.example.data.model

data class PinoutMapping(
    val obd1PinName: String,
    val obd1PinDescTh: String,
    val elm327Obd2Pin: Int, // e.g. 16 for B+, 4 for Chassis GND, 5 for Signal GND, 7 for K-Line
    val wireColorSuggestion: String
)

data class Obd1AdapterGuide(
    val id: String,
    val makeTh: String,
    val makeEn: String,
    val portNameTh: String,
    val portNameEn: String,
    val portLocationTh: String,
    val connectorType: String, // e.g. "Toyota 17-Pin DLC1 / 22-Pin DLC2", "Honda 3-Pin / 5-Pin SCC"
    val flashCodeMethodTh: String,
    val flashCodeMethodEn: String,
    val elm327SupportLevelTh: String,
    val pinMappings: List<PinoutMapping>,
    val applicableThaiModels: List<String>,
    val diagramNotesTh: String
)

data class CustomVehicleSpecTemplate(
    val make: String,
    val model: String,
    val yearRange: String,
    val engineCode: String,
    val displacementCc: String,
    val fuelType: String,
    val idleRpmMin: Int,
    val idleRpmMax: Int,
    val normalFuelRailPressureMinBar: Float,
    val normalFuelRailPressureMaxBar: Float,
    val normalCoolantTempMaxC: Int,
    val diagnosticProtocolTh: String,
    val adapterNeededTh: String
)
