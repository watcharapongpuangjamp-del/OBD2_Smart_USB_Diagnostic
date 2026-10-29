package com.example.data.repository

import com.example.data.model.DiagnosticSystem
import com.example.data.model.DtcCode
import com.example.data.model.LiveSensorData
import com.example.data.model.MaintenanceLog
import com.example.data.model.PredictiveComponent
import com.example.data.model.PredictiveStatus
import com.example.data.model.PredictiveVehicleHealthSummary
import com.example.data.model.UrgencyLevel
import com.example.data.model.Vehicle
import com.example.service.DrivingMode
import java.util.Calendar

object PredictiveMaintenanceEngine {

    fun analyzeVehicleHealth(
        vehicle: Vehicle?,
        logs: List<MaintenanceLog>,
        sensors: LiveSensorData,
        activeDtcs: List<DtcCode>,
        drivingMode: DrivingMode
    ): PredictiveVehicleHealthSummary {
        val currentMileage = vehicle?.currentMileageKm ?: 68500
        val vehicleYear = vehicle?.year ?: 2021
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val vehicleAgeYears = (currentYear - vehicleYear).coerceAtLeast(1)

        val components = mutableListOf<PredictiveComponent>()

        // ----------------------------------------------------
        // 1. 12V Battery & Charging System Analysis
        // ----------------------------------------------------
        val hasBatteryLog = logs.any {
            it.serviceTypeTh.contains("แบตเตอรี่", ignoreCase = true) ||
            it.serviceTypeEn.contains("battery", ignoreCase = true)
        }
        val isVoltLow = sensors.batteryVoltage < 13.8f
        val isSeverelyLow = sensors.batteryVoltage < 12.5f
        val batteryAgeYears = if (hasBatteryLog) 1 else vehicleAgeYears.coerceAtLeast(2)

        val batteryHealthScore = when {
            isSeverelyLow -> 32
            isVoltLow && batteryAgeYears >= 2 -> 48
            batteryAgeYears >= 3 -> 62
            else -> 86
        }

        val batteryStatus = when {
            batteryHealthScore < 45 -> PredictiveStatus.IMMINENT_FAILURE
            batteryHealthScore < 70 -> PredictiveStatus.ATTENTION
            batteryHealthScore < 85 -> PredictiveStatus.MONITORING
            else -> PredictiveStatus.HEALTHY
        }

        components.add(
            PredictiveComponent(
                id = "battery_12v",
                nameTh = "แบตเตอรี่ 12V และระบบไดชาร์จ",
                nameEn = "12V Battery & Alternator System",
                system = DiagnosticSystem.BODY,
                healthScore = batteryHealthScore,
                status = batteryStatus,
                failureRiskProbability = if (batteryHealthScore < 45) 0.85f else if (batteryHealthScore < 70) 0.45f else 0.12f,
                estimatedRemainingKm = if (batteryHealthScore < 45) 600 else 4500,
                estimatedRemainingDays = if (batteryHealthScore < 45) 14 else 90,
                monitoredSensors = listOf(
                    "แรงดันไฟชาร์จ: ${String.format("%.2f", sensors.batteryVoltage)} V",
                    "ไดชาร์จ Duty: ${String.format("%.0f", sensors.alternatorDutyPercent)} %",
                    "อายุแบตเตอรี่ประเมิน: $batteryAgeYears ปี (สภาพอากาศร้อนจัด)"
                ),
                reasonTh = if (batteryHealthScore < 45) {
                    "แรงดันไฟตกต่ำกว่า 13.8V ขณะติดเครื่อง และแบตเตอรี่ผ่านการใช้งานมานานกว่า 2-3 ปี ในสภาพอากาศร้อนของไทย เสี่ยงสตาร์ทไม่ติดกะทันหัน"
                } else {
                    "ระบบชาร์จไฟปกติ แบตเตอรี่จ่ายไฟสม่ำเสมอ แนะนำตรวจเช็คค่าน้ำกลั่น/ความถ่วงจำเพาะตามรอบ"
                },
                reasonEn = if (batteryHealthScore < 45) {
                    "Charging voltage dropped below threshold with battery age > 2 years in tropical heat. Imminent no-crank failure risk."
                } else {
                    "Alternator output stable. Standard preventative battery inspection recommended."
                },
                recommendationTh = if (batteryHealthScore < 45) {
                    "ตรวจเช็คค่า CCA ด้วยเครื่องวัด หรือเปลี่ยนแบตเตอรี่ลูกใหม่ขนาด 75-85 Ah (ชนิด Maintenance Free / EFB)"
                } else {
                    "ตรวจเช็คขั้วแบตเตอรี่ ทำความสะอาดขี้เกลือ และขันยึดให้แน่น"
                },
                recommendationEn = if (batteryHealthScore < 45) {
                    "Perform CCA load test or replace 12V battery with 75-85 Ah Maintenance-Free / EFB unit."
                } else {
                    "Inspect battery terminal clamps and clean corrosion buildup."
                },
                estimatedPartCostThb = "฿2,200 - ฿3,600",
                urgencyLevel = if (batteryHealthScore < 45) UrgencyLevel.CRITICAL_IMMEDIATE else UrgencyLevel.NORMAL,
                isImminentAlert = batteryHealthScore < 45
            )
        )

        // ----------------------------------------------------
        // 2. Engine Oil & Lubrication System Analysis
        // ----------------------------------------------------
        val lastOilLog = logs.firstOrNull {
            it.serviceTypeTh.contains("น้ำมันเครื่อง", ignoreCase = true) ||
            it.serviceTypeEn.contains("oil", ignoreCase = true)
        }
        val kmSinceLastOil = if (lastOilLog != null) (currentMileage - lastOilLog.mileageAtServiceKm).coerceAtLeast(0) else 8500
        val oilInterval = 10000
        val remainingOilKm = (oilInterval - kmSinceLastOil).coerceAtLeast(0)

        val oilHealthScore = when {
            kmSinceLastOil >= oilInterval -> 38
            kmSinceLastOil >= 8000 -> 58
            kmSinceLastOil >= 5000 -> 78
            else -> 95
        }

        val oilStatus = when {
            oilHealthScore < 45 -> PredictiveStatus.IMMINENT_FAILURE
            oilHealthScore < 70 -> PredictiveStatus.ATTENTION
            oilHealthScore < 85 -> PredictiveStatus.MONITORING
            else -> PredictiveStatus.HEALTHY
        }

        components.add(
            PredictiveComponent(
                id = "engine_oil",
                nameTh = "น้ำมันเครื่องสังเคราะห์ & ไส้กรอง",
                nameEn = "Synthetic Engine Oil & Filter",
                system = DiagnosticSystem.ENGINE,
                healthScore = oilHealthScore,
                status = oilStatus,
                failureRiskProbability = if (oilHealthScore < 45) 0.78f else if (oilHealthScore < 70) 0.35f else 0.08f,
                estimatedRemainingKm = remainingOilKm,
                estimatedRemainingDays = (remainingOilKm / 35).coerceAtLeast(5),
                monitoredSensors = listOf(
                    "ระยะใช้งานหลังเปลี่ยน: ${kmSinceLastOil} / $oilInterval km",
                    "แรงดันน้ำมันเครื่อง: ${String.format("%.1f", sensors.engineOilPressureBar)} Bar",
                    "อุณหภูมิน้ำมันเครื่อง: ${sensors.oilTempC} °C"
                ),
                reasonTh = if (oilHealthScore < 70) {
                    "น้ำมันเครื่องใช้งานมาแล้ว $kmSinceLastOil กม. ความหนืดและสารชะล้างเริ่มเสื่อมสภาพจากการขับขี่ในเมืองและการจราจรติดขัด"
                } else {
                    "ฟิล์มน้ำมันหล่อลื่นและแรงดันปั๊มน้ำมันเครื่องอยู่ในเกณฑ์สมบูรณ์"
                },
                reasonEn = if (oilHealthScore < 70) {
                    "Oil has accumulated $kmSinceLastOil km in stop-and-go conditions. Viscosity shear and detergent additive depletion detected."
                } else {
                    "Oil pressure and lubricant film protection within optimal thresholds."
                },
                recommendationTh = if (oilHealthScore < 70) {
                    "เตรียมเปลี่ยนถ่ายน้ำมันเครื่องสังเคราะห์แท้ 5W-30 หรือ 0W-20 พร้อมเปลี่ยนไส้กรองน้ำมันเครื่องแท้ศูนย์"
                } else {
                    "ตรวจเช็คก้านวัดระดับน้ำมันเครื่องทุกๆ 2 สัปดาห์"
                },
                recommendationEn = if (oilHealthScore < 70) {
                    "Schedule Fully Synthetic 5W-30 / 0W-20 engine oil & OEM spin-on filter change."
                } else {
                    "Inspect dipstick oil level bi-weekly."
                },
                estimatedPartCostThb = "฿1,800 - ฿2,800",
                urgencyLevel = if (oilHealthScore < 45) UrgencyLevel.HIGH_PRIORITY else if (oilHealthScore < 70) UrgencyLevel.PLANNED_SERVICE else UrgencyLevel.NORMAL,
                isImminentAlert = oilHealthScore < 45
            )
        )

        // ----------------------------------------------------
        // 3. Common Rail Fuel System & SCV Valve (Powertrain)
        // ----------------------------------------------------
        val hasFuelRailDtc = activeDtcs.any { it.code == "P0087" || it.code == "P0171" }
        val lastFuelFilterLog = logs.firstOrNull {
            it.serviceTypeTh.contains("กรองโซล่า", ignoreCase = true) ||
            it.serviceTypeEn.contains("fuel filter", ignoreCase = true)
        }
        val kmSinceFuelFilter = if (lastFuelFilterLog != null) (currentMileage - lastFuelFilterLog.mileageAtServiceKm).coerceAtLeast(0) else 18500

        val fuelSystemHealthScore = when {
            hasFuelRailDtc -> 35
            kmSinceFuelFilter > 18000 -> 55
            kmSinceFuelFilter > 10000 -> 78
            else -> 92
        }

        val fuelStatus = when {
            fuelSystemHealthScore < 45 -> PredictiveStatus.IMMINENT_FAILURE
            fuelSystemHealthScore < 70 -> PredictiveStatus.ATTENTION
            else -> PredictiveStatus.HEALTHY
        }

        components.add(
            PredictiveComponent(
                id = "fuel_scv",
                nameTh = "ระบบจ่ายน้ำมันคอมมอนเรล & วาล์ว SCV",
                nameEn = "Common Rail Fuel System & SCV Valve",
                system = DiagnosticSystem.ENGINE,
                healthScore = fuelSystemHealthScore,
                status = fuelStatus,
                failureRiskProbability = if (fuelSystemHealthScore < 45) 0.90f else if (fuelSystemHealthScore < 70) 0.40f else 0.10f,
                estimatedRemainingKm = if (fuelSystemHealthScore < 45) 850 else 3200,
                estimatedRemainingDays = if (fuelSystemHealthScore < 45) 18 else 75,
                monitoredSensors = listOf(
                    "แรงดันรางคอมมอนเรล: ${String.format("%.0f", sensors.fuelRailPressureBar)} Bar",
                    "ระยะกรองโซล่า: ${kmSinceFuelFilter} km (เกณฑ์เปลี่ยน 20,000 km)",
                    "สถานะรหัสผิดปกติ: ${if (hasFuelRailDtc) "พบ P0087 แรงดันตก" else "ปกติ"}"
                ),
                reasonTh = if (fuelSystemHealthScore < 45) {
                    "ตรวจพบแรงดันรางคอมมอนเรลผันผวน under-boost/load สัมพันธ์กับวาล์ว SCV ติดขัด หรือกรองดักน้ำเริ่มอุดตัน เสี่ยงเครื่องวูบขณะเร่งแซง"
                } else {
                    "แรงดันน้ำมันในรางคอมมอนเรลเสถียร หัวฉีดและปั๊มแรงดันสูงทำงานปกติ"
                },
                reasonEn = if (fuelSystemHealthScore < 45) {
                    "Fuel rail pressure fluctuations match sticky SCV Suction Control Valve or clogged fuel filter cartridge. Risk of limp mode stall."
                } else {
                    "Common rail fuel rail pressure stable across rev ranges."
                },
                recommendationTh = if (fuelSystemHealthScore < 45) {
                    "ถอดล้าง/เปลี่ยนวาล์ว SCV ตัวสั้น/ยาว (แท้ Denso) และเปลี่ยนไส้กรองโซล่าดักน้ำ"
                } else {
                    "เปลี่ยนกรองน้ำมันดีเซลตามรอบ 20,000 กม. เพื่อป้องกันสิ่งสกปรกอุดตันหัวฉีด"
                },
                recommendationEn = if (fuelSystemHealthScore < 45) {
                    "Clean or replace Denso SCV Valve and replace diesel fuel filter element."
                } else {
                    "Replace diesel fuel filter every 20,000 km."
                },
                estimatedPartCostThb = "฿1,200 - ฿4,500",
                urgencyLevel = if (fuelSystemHealthScore < 45) UrgencyLevel.CRITICAL_IMMEDIATE else UrgencyLevel.PLANNED_SERVICE,
                isImminentAlert = fuelSystemHealthScore < 45
            )
        )

        // ----------------------------------------------------
        // 4. Automatic / CVT Transmission Fluid & Clutch (TCM)
        // ----------------------------------------------------
        val hasTransDtc = activeDtcs.any { it.code.startsWith("P07") || it.code.startsWith("P08") }
        val isTransHot = sensors.transmissionFluidTempC > 95
        val isSlipHigh = sensors.torqueConverterSlipRpm > 100 && drivingMode != DrivingMode.IDLE

        val transHealthScore = when {
            hasTransDtc -> 30
            isTransHot || isSlipHigh -> 52
            currentMileage > 60000 -> 68
            else -> 89
        }

        val transStatus = when {
            transHealthScore < 45 -> PredictiveStatus.IMMINENT_FAILURE
            transHealthScore < 70 -> PredictiveStatus.ATTENTION
            else -> PredictiveStatus.HEALTHY
        }

        components.add(
            PredictiveComponent(
                id = "transmission_fluid",
                nameTh = "น้ำมันเกียร์ออโต้ (ATF / CVTF) & ชุดคลัตช์",
                nameEn = "Automatic Transmission Fluid & Clutches",
                system = DiagnosticSystem.TRANSMISSION,
                healthScore = transHealthScore,
                status = transStatus,
                failureRiskProbability = if (transHealthScore < 45) 0.82f else if (transHealthScore < 70) 0.32f else 0.06f,
                estimatedRemainingKm = if (transHealthScore < 45) 1200 else 6000,
                estimatedRemainingDays = if (transHealthScore < 45) 25 else 140,
                monitoredSensors = listOf(
                    "ความร้อนน้ำมันเกียร์: ${sensors.transmissionFluidTempC} °C",
                    "แรงดันเกียร์ (Line Pressure): ${String.format("%.1f", sensors.tcmLinePressurePsi)} PSI",
                    "สลิปคลัตช์ทอร์ค: ${sensors.torqueConverterSlipRpm} RPM"
                ),
                reasonTh = if (transHealthScore < 70) {
                    "อุณหภูมิน้ำมันเกียร์สูงสะสมและเลขไมล์เข้าใกล้ระยะ 70,000-80,000 กม. เสี่ยงต่อการเกิดคราบยางเหนียวในวาล์วบอดี้และเกียร์กระตุก"
                } else {
                    "แรงดันไฮดรอลิกเกียร์และการล็อคอัพของทอร์คคอนเวอร์เตอร์ทำงานปกติ"
                },
                reasonEn = if (transHealthScore < 70) {
                    "Accumulated thermal cycles near 70k km interval. Risk of valve body varnish and shift shudder."
                } else {
                    "TCM hydraulic line pressure and torque converter lockup within OEM specs."
                },
                recommendationTh = if (transHealthScore < 70) {
                    "เปลี่ยนถ่ายหรือฟลัชชิ่งน้ำมันเกียร์ออโต้แท้ศูนย์ พร้อมเปลี่ยนปะเก็นและกรองน้ำมันเกียร์ก้นอ่าง"
                } else {
                    "ตรวจเช็คสีและกลิ่นของน้ำมันเกียร์ในก้านวัด"
                },
                recommendationEn = if (transHealthScore < 70) {
                    "Perform full ATF/CVTF fluid drain & flush with pan gasket & filter replacement."
                } else {
                    "Inspect transmission dipstick fluid color and smell."
                },
                estimatedPartCostThb = "฿1,800 - ฿4,200",
                urgencyLevel = if (transHealthScore < 45) UrgencyLevel.CRITICAL_IMMEDIATE else UrgencyLevel.PLANNED_SERVICE,
                isImminentAlert = transHealthScore < 45
            )
        )

        // ----------------------------------------------------
        // 5. Radiator & Engine Cooling System
        // ----------------------------------------------------
        val isOverheat = sensors.coolantTempC > 102
        val isHighTemp = sensors.coolantTempC > 94

        val coolingHealthScore = when {
            isOverheat -> 25
            isHighTemp -> 56
            vehicleAgeYears >= 4 -> 72
            else -> 94
        }

        val coolingStatus = when {
            coolingHealthScore < 45 -> PredictiveStatus.IMMINENT_FAILURE
            coolingHealthScore < 70 -> PredictiveStatus.ATTENTION
            else -> PredictiveStatus.HEALTHY
        }

        components.add(
            PredictiveComponent(
                id = "cooling_system",
                nameTh = "หม้อน้ำ วาล์วน้ำ และสารหล่อเย็น",
                nameEn = "Radiator, Thermostat & Coolant",
                system = DiagnosticSystem.ENGINE,
                healthScore = coolingHealthScore,
                status = coolingStatus,
                failureRiskProbability = if (coolingHealthScore < 45) 0.88f else if (coolingHealthScore < 70) 0.28f else 0.05f,
                estimatedRemainingKm = if (coolingHealthScore < 45) 400 else 8000,
                estimatedRemainingDays = if (coolingHealthScore < 45) 10 else 180,
                monitoredSensors = listOf(
                    "ความร้อนน้ำหล่อเย็น: ${sensors.coolantTempC} °C",
                    "ความร้อนน้ำมันเครื่อง: ${sensors.oilTempC} °C",
                    "อุณหภูมิอากาศไอดี: ${sensors.intakeAirTempC} °C"
                ),
                reasonTh = if (coolingHealthScore < 45) {
                    "อุณหภูมิน้ำหล่อเย็นสูงเกิน 102°C วาล์วน้ำอาจเปิดไม่สุด หม้อน้ำตัน หรือพัดลมไฟฟ้าหมุนช้า เสี่ยงเครื่องยนต์ฮีตฝาสูบโก่ง"
                } else {
                    "ระบบระบายความร้อนควบคุมอุณหภูมิตามย่านมาตรฐาน 88 - 92°C ได้อย่างมีประสิทธิภาพ"
                },
                reasonEn = if (coolingHealthScore < 45) {
                    "Coolant temperature exceeded 102°C threshold. Possible sticky thermostat or clogged radiator core. Severe engine overheat risk."
                } else {
                    "Cooling loop operating stably within standard 88 - 92°C band."
                },
                recommendationTh = if (coolingHealthScore < 45) {
                    "หยุดการใช้งานหนักทันที ตรวจเช็คระดับน้ำพัก หม้อน้ำ วาล์วน้ำ และฝาหม้อน้ำแรงดัน 1.1 บาร์"
                } else {
                    "เปลี่ยนถ่ายน้ำยาหล่อเย็น Super Long Life Coolant ทุกๆ 80,000 - 100,000 กม."
                },
                recommendationEn = if (coolingHealthScore < 45) {
                    "Inspect thermostat opening, radiator core flow, and 1.1 bar radiator pressure cap."
                } else {
                    "Flush & refill Super Long Life Coolant every 80,000 km."
                },
                estimatedPartCostThb = "฿650 - ฿3,800",
                urgencyLevel = if (coolingHealthScore < 45) UrgencyLevel.CRITICAL_IMMEDIATE else UrgencyLevel.NORMAL,
                isImminentAlert = coolingHealthScore < 45
            )
        )

        // ----------------------------------------------------
        // 6. Brake Pads & ABS Wheel Speed Sensors (Chassis)
        // ----------------------------------------------------
        val hasAbsDtc = activeDtcs.any { it.code.startsWith("C0") }
        val brakeHealthScore = when {
            hasAbsDtc -> 42
            currentMileage > 65000 -> 64
            else -> 88
        }

        val brakeStatus = when {
            brakeHealthScore < 45 -> PredictiveStatus.IMMINENT_FAILURE
            brakeHealthScore < 70 -> PredictiveStatus.ATTENTION
            else -> PredictiveStatus.HEALTHY
        }

        components.add(
            PredictiveComponent(
                id = "brake_abs",
                nameTh = "ผ้าเบรก จานเบรก และเซนเซอร์ ABS",
                nameEn = "Brake Pads, Rotors & ABS Sensors",
                system = DiagnosticSystem.ABS,
                healthScore = brakeHealthScore,
                status = brakeStatus,
                failureRiskProbability = if (brakeHealthScore < 45) 0.80f else if (brakeHealthScore < 70) 0.35f else 0.08f,
                estimatedRemainingKm = if (brakeHealthScore < 45) 1500 else 8500,
                estimatedRemainingDays = if (brakeHealthScore < 45) 30 else 200,
                monitoredSensors = listOf(
                    "ความเร็วล้อหน้า FL/FR: ${sensors.wheelSpeedFlKmh} / ${sensors.wheelSpeedFrKmh} km/h",
                    "แรงดันน้ำมันเบรก: ${sensors.brakeFluidPressureBar} Bar",
                    "ความหนาผ้าเบรกประเมิน: 3.5 mm (เกณฑ์เปลี่ยน < 3 mm)"
                ),
                reasonTh = if (brakeHealthScore < 70) {
                    "ผ้าเบรกหน้าผ่านการใช้งานมาเกิน 65,000 กม. ความหนาเนื้อผ้าเบรกใกล้ถึงสะพานเตือน และเซนเซอร์ ABS ล้อหน้าเริ่มมีคราบขี้เบรกเกาะ"
                } else {
                    "สัญญาณเซนเซอร์ความเร็วทั้ง 4 ล้อส่งค่าสมดุล ผ้าเบรกจับตัวแน่นหนา"
                },
                reasonEn = if (brakeHealthScore < 70) {
                    "Front friction pads have exceeded 65,000 km. Pad thickness approaching acoustic wear indicator tab."
                } else {
                    "All 4-wheel speed pulse outputs in sync. Hydraulic braking response nominal."
                },
                recommendationTh = if (brakeHealthScore < 70) {
                    "ตรวจเช็คความหนาผ้าเบรกหน้า-หลัง เจียรจานเบรก และทำความสะอาดหัวเซนเซอร์ ABS"
                } else {
                    "ตรวจเช็คระดับน้ำมันเบรก DOT 4 ในกระปุกทุกๆ 10,000 กม."
                },
                recommendationEn = if (brakeHealthScore < 70) {
                    "Inspect pad thickness, resurface rotors, and clean magnetic ABS sensor tips."
                } else {
                    "Check DOT 4 brake fluid reservoir level every 10,000 km."
                },
                estimatedPartCostThb = "฿1,200 - ฿3,200",
                urgencyLevel = if (brakeHealthScore < 45) UrgencyLevel.HIGH_PRIORITY else UrgencyLevel.PLANNED_SERVICE,
                isImminentAlert = brakeHealthScore < 45
            )
        )

        // ----------------------------------------------------
        // Aggregate Summary
        // ----------------------------------------------------
        val avgScore = (components.map { it.healthScore }.average()).toInt()
        val alertCount = components.count { it.status == PredictiveStatus.ATTENTION || it.status == PredictiveStatus.IMMINENT_FAILURE }
        val imminentCount = components.count { it.status == PredictiveStatus.IMMINENT_FAILURE }

        val riskCategoryTh = when {
            imminentCount > 0 -> "⚠️ ตรวจพบชิ้นส่วนเสี่ยงชำรุดฉุกเฉิน ($imminentCount รายการ)"
            alertCount > 0 -> "⚡ มีชิ้นส่วนที่ควรตรวจเช็คตามรอบ ($alertCount รายการ)"
            else -> "✅ ระบบรถยนต์อยู่ในเกณฑ์สมบูรณ์มาก"
        }

        val riskCategoryEn = when {
            imminentCount > 0 -> "⚠️ Imminent Component Failure Alerts ($imminentCount items)"
            alertCount > 0 -> "⚡ Preventative Maintenance Due ($alertCount items)"
            else -> "✅ Vehicle in Peak Optimal Condition"
        }

        val drivingStressTh = when (drivingMode) {
            DrivingMode.IDLE -> "ขับขี่เบา / รอบเดินเบา"
            DrivingMode.CRUISE -> "ขับขี่ความเร็วคงที่ทางไกล (ความเค้นปานกลาง)"
            DrivingMode.HARD_ACCEL -> "ขับขี่เร่งแซง / บรรทุกหนัก (ความเค้นสูง)"
            DrivingMode.OVERHEAT_WARNING -> "สภาวะความร้อนสูงผิดปกติ (ความเค้นวิกฤต)"
        }

        val drivingStressEn = when (drivingMode) {
            DrivingMode.IDLE -> "Light / Idle Profile"
            DrivingMode.CRUISE -> "Highway Cruising (Moderate Stress)"
            DrivingMode.HARD_ACCEL -> "High Load / Hard Acceleration (High Stress)"
            DrivingMode.OVERHEAT_WARNING -> "Severe Thermal Load (Critical Stress)"
        }

        return PredictiveVehicleHealthSummary(
            overallHealthScore = avgScore,
            riskCategoryTh = riskCategoryTh,
            riskCategoryEn = riskCategoryEn,
            activeAlertCount = alertCount,
            imminentFailureCount = imminentCount,
            components = components,
            lastAnalysisTimestamp = System.currentTimeMillis(),
            mileageAnalyzedKm = currentMileage,
            drivingStressFactorTh = drivingStressTh,
            drivingStressFactorEn = drivingStressEn
        )
    }
}
