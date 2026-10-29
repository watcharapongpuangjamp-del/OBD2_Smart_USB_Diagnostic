package com.example.data.model

enum class BaselineCategory {
    POWERTRAIN,
    TRANSMISSION,
    ABS_CHASSIS,
    BODY_ELECTRICAL
}

enum class SensorComparisonStatus {
    NORMAL,
    LOW_WARNING,
    HIGH_WARNING,
    CRITICAL
}

enum class BaselineUnitType {
    TEMPERATURE,
    PRESSURE,
    VOLTAGE,
    PERCENT,
    RPM,
    SPEED,
    RESISTANCE,
    FLOW_RATE,
    GENERIC
}

data class BaselineSensorParameter(
    val id: String,
    val titleTh: String,
    val titleEn: String,
    val category: BaselineCategory,
    val minStandard: Float,
    val maxStandard: Float,
    val idealStandard: Float,
    val unitType: BaselineUnitType,
    val rawUnitLabel: String,
    val descriptionTh: String,
    val descriptionEn: String,
    val aiTipTh: String,
    val aiTipEn: String
)

data class EvaluatedSensorResult(
    val parameter: BaselineSensorParameter,
    val liveRawValue: Float,
    val displayLiveValueFormatted: String,
    val displayMinFormatted: String,
    val displayMaxFormatted: String,
    val displayIdealFormatted: String,
    val unitSymbol: String,
    val status: SensorComparisonStatus,
    val deviationPercent: Float // % deviation from ideal
)

object OemBaselineDatabase {
    val allBaselineParameters = listOf(
        BaselineSensorParameter(
            id = "coolant_temp",
            titleTh = "อุณหภูมิน้ำหล่อเย็น (Coolant Temp)",
            titleEn = "Engine Coolant Temp (ECT)",
            category = BaselineCategory.POWERTRAIN,
            minStandard = 82.0f,
            maxStandard = 95.0f,
            idealStandard = 89.0f,
            unitType = BaselineUnitType.TEMPERATURE,
            rawUnitLabel = "°C",
            descriptionTh = "อุณหภูมิการทำงานของเครื่องยนต์ตามมาตรฐาน OEM (สภาวะปกติ 82-95°C)",
            descriptionEn = "Standard engine operating temperature (Normal 82-95°C)",
            aiTipTh = "หากอุณหภูมิเกิน 98°C ควรสั่งเช็กระดับน้ำคูลแลนท์, พัดลมหม้อน้ำ และวาล์วน้ำ",
            aiTipEn = "If temperature exceeds 98°C, inspect coolant level, radiator fan, and thermostat."
        ),
        BaselineSensorParameter(
            id = "battery_voltage",
            titleTh = "แรงดันไฟฟ้าแบตเตอรี่ (12V Battery)",
            titleEn = "Battery Supply Voltage",
            category = BaselineCategory.BODY_ELECTRICAL,
            minStandard = 13.5f,
            maxStandard = 14.6f,
            idealStandard = 14.1f,
            unitType = BaselineUnitType.VOLTAGE,
            rawUnitLabel = "V",
            descriptionTh = "แรงดันไฟขณะเครื่องยนต์ติดและไดชาร์จ (Alternator) ทำงานปกติ (13.5 - 14.6V)",
            descriptionEn = "Operating charging voltage while engine is running (13.5 - 14.6V)",
            aiTipTh = "หากต่ำกว่า 12.8V ไดชาร์จอาจเสื่อมหรือสายพานหย่อน / หากเกิน 15.0V ไดชาร์จจ่ายไฟเกิน",
            aiTipEn = "Below 12.8V indicates weak alternator / Above 15.0V indicates overcharging."
        ),
        BaselineSensorParameter(
            id = "turbo_boost",
            titleTh = "แรงดันเทอร์โบ (Turbo Boost Pressure)",
            titleEn = "Turbocharger Boost Pressure",
            category = BaselineCategory.POWERTRAIN,
            minStandard = 0.0f,
            maxStandard = 1.6f,
            idealStandard = 0.8f,
            unitType = BaselineUnitType.PRESSURE,
            rawUnitLabel = "Bar",
            descriptionTh = "แรงดันอัดอากาศของเทอร์โบชาร์จเจอร์ (เครื่องยนต์ดีเซลคอมมอนเรล/เบนซินเทอร์โบ)",
            descriptionEn = "Turbocharger intake manifold boost pressure range",
            aiTipTh = "หากแรงดันบูสท์ตกเกินไป ควรเช็กสายเวสเกต ท่ออินเตอร์คูลเลอร์รั่ว หรือใบเทอร์โบ",
            aiTipEn = "Low boost indicates intercooler hose leak, wastegate fault, or turbo wear."
        ),
        BaselineSensorParameter(
            id = "fuel_rail_pressure",
            titleTh = "แรงดันรางคอมมอนเรล (Fuel Rail Pressure)",
            titleEn = "Commonrail Fuel Rail Pressure",
            category = BaselineCategory.POWERTRAIN,
            minStandard = 250.0f,
            maxStandard = 1600.0f,
            idealStandard = 400.0f,
            unitType = BaselineUnitType.PRESSURE,
            rawUnitLabel = "Bar",
            descriptionTh = "แรงดันน้ำมันเชื้อเพลิงในรางคอมมอนเรลแรงดันสูง (เดินเบา ~300-400 Bar, เร่งแซง ~1400+ Bar)",
            descriptionEn = "High pressure commonrail fuel rail pressure range",
            aiTipTh = "หากแรงดันตกขณะเร่งแซง ควรเช็กกรองดีเซล ปั๊มโซล่า หรือตัวแรงดัน SCV Valve",
            aiTipEn = "Pressure drops under acceleration indicate clogged fuel filter or SCV valve fault."
        ),
        BaselineSensorParameter(
            id = "engine_oil_pressure",
            titleTh = "แรงดันน้ำมันเครื่อง (Engine Oil Pressure)",
            titleEn = "Engine Oil Pressure",
            category = BaselineCategory.POWERTRAIN,
            minStandard = 2.0f,
            maxStandard = 5.5f,
            idealStandard = 3.8f,
            unitType = BaselineUnitType.PRESSURE,
            rawUnitLabel = "Bar",
            descriptionTh = "แรงดันการหมุนเวียนน้ำมันเครื่องปั๊มเข้าเลี้ยงชิ้นส่วนภายในเครื่องยนต์",
            descriptionEn = "Oil pump lubrication feed pressure across engine journals",
            aiTipTh = "หากต่ำกว่า 1.5 Bar เสี่ยงเครื่องยนต์สึกหรอรุนแรง ต้องดับเครื่องยนต์ตรวจเช็กทันที",
            aiTipEn = "Below 1.5 Bar poses immediate engine failure risk. Check oil level immediately."
        ),
        BaselineSensorParameter(
            id = "transmission_temp",
            titleTh = "อุณหภูมิน้ำมันเกียร์ออโต้ (TCM Fluid Temp)",
            titleEn = "Automatic Transmission Temp",
            category = BaselineCategory.TRANSMISSION,
            minStandard = 70.0f,
            maxStandard = 92.0f,
            idealStandard = 82.0f,
            unitType = BaselineUnitType.TEMPERATURE,
            rawUnitLabel = "°C",
            descriptionTh = "อุณหภูมิน้ำมันเกียร์อัตโนมัติ/CVT ขณะขับขี่ใช้งานปกติ",
            descriptionEn = "Automatic / CVT transmission fluid operating temperature",
            aiTipTh = "หากเกิน 100°C คลัตช์เกียร์เสี่ยงลื่นและน้ำมันเกียร์เสื่อมสภาพเร็ว ควรติดตั้งออยคูลเลอร์เกียร์",
            aiTipEn = "Above 100°C causes fluid degradation and clutch slip. Consider transmission cooler."
        ),
        BaselineSensorParameter(
            id = "short_term_fuel_trim",
            titleTh = "ค่าชดเชยน้ำมันระยะสั้น (STFT)",
            titleEn = "Short Term Fuel Trim (STFT)",
            category = BaselineCategory.POWERTRAIN,
            minStandard = -10.0f,
            maxStandard = 10.0f,
            idealStandard = 0.0f,
            unitType = BaselineUnitType.PERCENT,
            rawUnitLabel = "%",
            descriptionTh = "การปรับจ่ายน้ำมันเชื้อเพลิงของ ECU แบบทันทีทันใด (+ คือจ่ายหนาขึ้น, - คือจ่ายบางลง)",
            descriptionEn = "Immediate ECU fuel delivery compensation rate (+ Richer, - Leaner)",
            aiTipTh = "หาก STFT บวกเกิน +15% แสดงว่ามีอากาศรั่วเข้าท่อไอดี (Vacuum Leak) หรือแรงดันดันน้ำมันตก",
            aiTipEn = "STFT above +15% indicates vacuum leak, low fuel pressure, or dirty MAF."
        ),
        BaselineSensorParameter(
            id = "long_term_fuel_trim",
            titleTh = "ค่าชดเชยน้ำมันระยะยาว (LTFT)",
            titleEn = "Long Term Fuel Trim (LTFT)",
            category = BaselineCategory.POWERTRAIN,
            minStandard = -10.0f,
            maxStandard = 10.0f,
            idealStandard = 0.0f,
            unitType = BaselineUnitType.PERCENT,
            rawUnitLabel = "%",
            descriptionTh = "การปรับสะสมของระบบจ่ายน้ำมันเชื้อเพลิง หากเกิน ±25% จะจุดไฟเตือน Check Engine (P0171/P0172)",
            descriptionEn = "Accumulated fuel trim map. Exceeding ±25% triggers P0171 / P0172 DTC.",
            aiTipTh = "หาก LTFT ลบเกิน -15% อาจเกิดจากหัวฉีดรั่ว หรือเซนเซอร์ออกซิเจน (O2 Sensor) เสื่อมสภาพ",
            aiTipEn = "LTFT below -15% indicates leaking fuel injector or degraded O2 sensor."
        ),
        BaselineSensorParameter(
            id = "intake_air_temp",
            titleTh = "อุณหภูมิอากาศไอดี (Intake Air Temp - IAT)",
            titleEn = "Intake Air Temp (IAT)",
            category = BaselineCategory.POWERTRAIN,
            minStandard = 20.0f,
            maxStandard = 50.0f,
            idealStandard = 32.0f,
            unitType = BaselineUnitType.TEMPERATURE,
            rawUnitLabel = "°C",
            descriptionTh = "อุณหภูมิอากาศที่ไหลผ่านแอร์โฟร์มิตเตอร์เข้าสู่ห้องเผาไหม้",
            descriptionEn = "Temperature of air drawn into intake manifold via MAF sensor",
            aiTipTh = "อุณหภูมิไอดีสูงเกินไปจะลดกำลังเครื่องยนต์และเพิ่มโอกาสเกิดการชิงจุดระเบิด (Knocking)",
            aiTipEn = "Excessive IAT reduces air density and horsepower, increasing knock risk."
        ),
        BaselineSensorParameter(
            id = "srs_loop_resistance",
            titleTh = "ความต้านทานวงจรถุงลม SRS (Airbag Loop)",
            titleEn = "Airbag Driver Loop Resistance",
            category = BaselineCategory.BODY_ELECTRICAL,
            minStandard = 1.8f,
            maxStandard = 3.2f,
            idealStandard = 2.4f,
            unitType = BaselineUnitType.RESISTANCE,
            rawUnitLabel = "Ω",
            descriptionTh = "ค่าความต้านทานไฟฟ้าวงจรจุดชนวนถุงลมนิรภัยฝั่งคนขับ/ผู้โดยสาร",
            descriptionEn = "Airbag inflator igniter coil circuit resistance",
            aiTipTh = "หากความต้านทานสูงเกิน 3.5Ω มักเกิดจากลานข้าวต้ม (Clockspring / Cable Reel) ตรงพวงมาลัยขาด",
            aiTipEn = "Resistance >3.5Ω typically indicates broken clockspring / spiral cable behind steering wheel."
        )
    )

    fun evaluate(
        param: BaselineSensorParameter,
        rawValue: Float,
        speedUnit: SpeedUnit,
        tempUnit: TemperatureUnit,
        pressUnit: PressureUnit
    ): EvaluatedSensorResult {
        val status = when {
            rawValue < param.minStandard -> SensorComparisonStatus.LOW_WARNING
            rawValue > param.maxStandard -> {
                if (param.id == "coolant_temp" && rawValue > 105f) SensorComparisonStatus.CRITICAL
                else if (param.id == "engine_oil_pressure" && rawValue < 1.5f) SensorComparisonStatus.CRITICAL
                else SensorComparisonStatus.HIGH_WARNING
            }
            else -> SensorComparisonStatus.NORMAL
        }

        val formattedLive = formatValueWithUnit(rawValue, param.unitType, speedUnit, tempUnit, pressUnit)
        val formattedMin = formatValueWithUnit(param.minStandard, param.unitType, speedUnit, tempUnit, pressUnit)
        val formattedMax = formatValueWithUnit(param.maxStandard, param.unitType, speedUnit, tempUnit, pressUnit)
        val formattedIdeal = formatValueWithUnit(param.idealStandard, param.unitType, speedUnit, tempUnit, pressUnit)
        val unitSymbol = getUnitSymbol(param.unitType, param.rawUnitLabel, speedUnit, tempUnit, pressUnit)

        val deviation = if (param.idealStandard != 0f) {
            ((rawValue - param.idealStandard) / param.idealStandard) * 100f
        } else {
            rawValue * 10f
        }

        return EvaluatedSensorResult(
            parameter = param,
            liveRawValue = rawValue,
            displayLiveValueFormatted = formattedLive,
            displayMinFormatted = formattedMin,
            displayMaxFormatted = formattedMax,
            displayIdealFormatted = formattedIdeal,
            unitSymbol = unitSymbol,
            status = status,
            deviationPercent = deviation
        )
    }

    private fun formatValueWithUnit(
        value: Float,
        type: BaselineUnitType,
        speedUnit: SpeedUnit,
        tempUnit: TemperatureUnit,
        pressUnit: PressureUnit
    ): String {
        return when (type) {
            BaselineUnitType.TEMPERATURE -> String.format("%.1f", UnitConverter.convertTemp(value, tempUnit))
            BaselineUnitType.PRESSURE -> String.format("%.2f", UnitConverter.convertPressure(value, pressUnit))
            BaselineUnitType.SPEED -> String.format("%.0f", UnitConverter.convertSpeed(value, speedUnit))
            BaselineUnitType.VOLTAGE -> String.format("%.1f", value)
            BaselineUnitType.PERCENT -> String.format("%.1f", value)
            BaselineUnitType.RPM -> String.format("%.0f", value)
            BaselineUnitType.RESISTANCE -> String.format("%.1f", value)
            BaselineUnitType.FLOW_RATE -> String.format("%.1f", value)
            BaselineUnitType.GENERIC -> String.format("%.1f", value)
        }
    }

    private fun getUnitSymbol(
        type: BaselineUnitType,
        rawLabel: String,
        speedUnit: SpeedUnit,
        tempUnit: TemperatureUnit,
        pressUnit: PressureUnit
    ): String {
        return when (type) {
            BaselineUnitType.TEMPERATURE -> tempUnit.symbolTh
            BaselineUnitType.PRESSURE -> pressUnit.symbolTh
            BaselineUnitType.SPEED -> speedUnit.symbolEn
            BaselineUnitType.VOLTAGE -> "V"
            BaselineUnitType.PERCENT -> "%"
            BaselineUnitType.RPM -> "RPM"
            BaselineUnitType.RESISTANCE -> "Ω"
            else -> rawLabel
        }
    }
}
