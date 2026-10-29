package com.example.data.model

enum class SpeedUnit(val symbolTh: String, val symbolEn: String) {
    KMH("กม./ชม.", "km/h"),
    MPH("ไมล์/ชม.", "mph")
}

enum class TemperatureUnit(val symbolTh: String, val symbolEn: String) {
    CELSIUS("°C", "°C"),
    FAHRENHEIT("°F", "°F")
}

enum class PressureUnit(val symbolTh: String, val symbolEn: String) {
    BAR("Bar", "Bar"),
    PSI("PSI", "PSI"),
    KPA("kPa", "kPa")
}

enum class FuelEconomyUnit(val symbolTh: String, val symbolEn: String) {
    KM_PER_L("กม./ลิตร", "km/L"),
    L_PER_100KM("ลิตร/100กม.", "L/100km"),
    MPG_US("MPG", "MPG (US)")
}

object UnitConverter {
    fun convertSpeed(kmh: Float, unit: SpeedUnit): Float {
        return when (unit) {
            SpeedUnit.KMH -> kmh
            SpeedUnit.MPH -> kmh * 0.621371f
        }
    }

    fun convertTemp(celsius: Float, unit: TemperatureUnit): Float {
        return when (unit) {
            TemperatureUnit.CELSIUS -> celsius
            TemperatureUnit.FAHRENHEIT -> (celsius * 9f / 5f) + 32f
        }
    }

    fun convertPressure(bar: Float, unit: PressureUnit): Float {
        return when (unit) {
            PressureUnit.BAR -> bar
            PressureUnit.PSI -> bar * 14.5038f
            PressureUnit.KPA -> bar * 100f
        }
    }

    fun convertFuelEconomy(kmL: Float, unit: FuelEconomyUnit): Float {
        return when (unit) {
            FuelEconomyUnit.KM_PER_L -> kmL
            FuelEconomyUnit.L_PER_100KM -> if (kmL > 0) 100f / kmL else 0f
            FuelEconomyUnit.MPG_US -> kmL * 2.35215f
        }
    }
}
