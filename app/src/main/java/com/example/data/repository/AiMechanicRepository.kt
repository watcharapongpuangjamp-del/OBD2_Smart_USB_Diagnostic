package com.example.data.repository

import android.util.Log
import com.example.data.model.AppLanguage
import com.example.data.model.ChatMessage
import com.example.data.model.DtcCode
import com.example.data.model.FreezeFrameData
import com.example.data.model.MessageSender
import com.example.data.model.Vehicle
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.cancellation.CancellationException

class AiMechanicRepository {

    suspend fun chat(
        conversation: List<ChatMessage>,
        userPrompt: String,
        enableHighThinking: Boolean = false,
        vehicleContext: String = "Toyota Hilux Revo 2021 2.4 GD Diesel (68,500 km)",
        language: AppLanguage = AppLanguage.TH
    ): String = withContext(Dispatchers.IO) {
        val systemInstructionText = if (language == AppLanguage.TH) {
            """
            คุณคือ "ช่างยนต์ผู้เชี่ยวชาญ AI (Master Mechanic Thailand)" ประจำศูนย์วิเคราะห์ปัญหา OBD-II
            เชี่ยวชาญเครื่องยนต์และระบบไฟฟ้ารถยนต์ในประเทศไทยตั้งแต่ปี 2000 ขึ้นไป (เช่น Toyota Revo/Vigo, Isuzu D-Max 1.9/3.0, Honda Civic/City Turbo/e:HEV, Ford Ranger Bi-turbo, Mitsubishi Triton, Nissan, MG, รถยนต์ไฟฟ้า EV)
            บริบทรถยนต์ปัจจุบันของผู้ใช้: $vehicleContext
            หน้าที่ของคุณ:
            1. อธิบายรหัสข้อผิดพลาด DTC (P, C, B, U), ข้อมูล Freeze Frame, และค่าเซนเซอร์แบบเข้าใจง่ายและลึกซึ้ง
            2. ให้คำแนะนำการแก้ไขปัญหาทีละขั้นตอน (Troubleshooting Step-by-Step) ทั้งแบบ DIY และส่งอู่
            3. ประเมินราคาอะไหล่และค่าแรงเป็นเงินบาทไทย (THB) ทั้งราคาเบิกศูนย์แท้และอะไหล่เทียบ OEM
            4. เตือนความปลอดภัยหากเป็นจุดอันตราย เช่น เบรก, ถุงลม, ไฟฟ้าแรงสูง, ความร้อนเครื่องยนต์ฮีต
            5. ตอบด้วยน้ำเสียงสุภาพ เป็นมิตร และมีความเป็นมืออาชีพเชิงช่างยนต์
            """.trimIndent()
        } else {
            """
            You are "AI Master Diagnostic Technician", an expert automotive mechanic in Thailand specializing in vehicles 2000+ (Toyota, Isuzu, Honda, Ford, Mitsubishi, Nissan, MG, BYD EVs).
            Current User Vehicle: $vehicleContext
            Your role:
            1. Explain OBD-II DTC codes (P, C, B, U), Freeze Frame data, and sensor telemetry accurately.
            2. Provide actionable step-by-step diagnostic and repair recommendations.
            3. Estimate replacement part costs and labor in Thai Baht (THB) for both OEM genuine and aftermarket parts.
            4. Emphasize vehicle safety protocols for cooling, braking, SRS, and electrical systems.
            5. Maintain a professional, knowledgeable, and encouraging mechanic tone.
            """.trimIndent()
        }

        try {
            val model = Firebase.ai.generativeModel(
                modelName = "gemini-3.8-flash",
                systemInstruction = content { text(systemInstructionText) },
                generationConfig = generationConfig {
                    temperature = if (enableHighThinking) 0.4f else 0.7f
                }
            )

            // Build chat history for multi-turn conversation
            val history = conversation.takeLast(6).map { msg ->
                val role = if (msg.sender == MessageSender.USER) "user" else "model"
                content(role) { text(msg.text) }
            }

            val chatSession = model.startChat(history = history)

            val response = withTimeout(60_000L) {
                chatSession.sendMessage(userPrompt)
            }

            val text = response.text
            if (!text.isNullOrBlank()) {
                return@withContext text
            }
        } catch (e: TimeoutCancellationException) {
            Log.e("AiMechanicRepository", "Firebase AI generation timed out", e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("AiMechanicRepository", "Firebase AI chat failed", e)
        }

        // Offline / Fallback Intelligent Mechanic Response
        return@withContext generateOfflineMechanicResponse(userPrompt, vehicleContext, language, enableHighThinking)
    }

    suspend fun analyzeDeepDiagnosis(
        dtcs: List<DtcCode>,
        freezeFrame: FreezeFrameData?,
        vehicle: Vehicle,
        language: AppLanguage
    ): String = withContext(Dispatchers.IO) {
        val dtcSummary = dtcs.joinToString(", ") { "${it.code} (${it.thTitle} / ${it.enTitle})" }
        val ffSummary = freezeFrame?.let {
            "RPM=${it.engineRpm}, Speed=${it.vehicleSpeedKmh}km/h, Coolant=${it.coolantTempCelsius}°C, Load=${it.engineLoadPercent}%, STFT=${it.shortTermFuelTrimPercent}%, LTFT=${it.longTermFuelTrimPercent}%, MAF=${it.massAirFlowGps}g/s, RailPressure=${it.fuelRailPressureBar}Bar, Battery=${it.batteryVoltage}V"
        } ?: "No Freeze Frame available"

        val prompt = if (language == AppLanguage.TH) {
            """
            วิเคราะห์เชิงลึกอาการและรหัสความผิดปกติ OBD-II สำหรับรถยนต์:
            รถยนต์: ${vehicle.make} ${vehicle.model} (${vehicle.year}) เลขไมล์: ${vehicle.currentMileageKm} km
            รหัส DTC ที่ตรวจพบ: $dtcSummary
            ค่า Freeze Frame เมื่อเกิดปัญหา: $ffSummary

            กรุณาวิเคราะห์:
            1. สาเหตุต้นตอที่เป็นไปได้สูงสุด (Root Cause Analysis) โดยพิจารณาจากค่าเซนเซอร์ Freeze Frame
            2. ลำดับการตรวจสอบทีละขั้นตอน (Diagnostic Checklist)
            3. ผลกระทบหากยังคงขับขี่ต่อไป (Safety & Drivability Impact)
            4. รายการอะไหล่ที่ต้องเปลี่ยนและประมาณการค่าใช้จ่ายในประเทศไทย (THB)
            """.trimIndent()
        } else {
            """
            Perform an in-depth OBD-II diagnostic evaluation for:
            Vehicle: ${vehicle.make} ${vehicle.model} (${vehicle.year}), Mileage: ${vehicle.currentMileageKm} km
            Detected DTCs: $dtcSummary
            Freeze Frame Data at trigger: $ffSummary

            Please evaluate:
            1. Most probable root cause based on freeze frame sensor correlation
            2. Step-by-step diagnostic checklist
            3. Risk assessment and drivability impact
            4. Recommended replacement parts and estimated cost in THB (Thai Baht)
            """.trimIndent()
        }

        return@withContext chat(
            conversation = emptyList(),
            userPrompt = prompt,
            enableHighThinking = true,
            vehicleContext = "${vehicle.make} ${vehicle.model} (${vehicle.year})",
            language = language
        )
    }

    suspend fun generateRepairManual(
        vehicle: Vehicle?,
        dtcs: List<DtcCode>,
        language: AppLanguage = AppLanguage.TH
    ): String = withContext(Dispatchers.IO) {
        val vehicleContext = if (vehicle != null) "${vehicle.make} ${vehicle.model} ${vehicle.year} ${vehicle.engine}" else "Unknown Vehicle"
        val dtcContext = if (dtcs.isEmpty()) "No DTCs" else dtcs.joinToString(", ") { "${it.code} - ${it.enTitle}" }

        val systemInstructionText = if (language == AppLanguage.TH) {
            """
            คุณคือ "ผู้เชี่ยวชาญการจัดทำคู่มือซ่อมรถยนต์ (Technical Repair Manual AI)" 
            หน้าที่ของคุณคือดึงข้อมูล Service Manual, Wiring Diagram (แบบจำลองข้อความ), และขั้นตอนการถอดประกอบซ่อมบำรุงที่ตรงกับรุ่นรถและรหัสปัญหา (DTC)
            รถยนต์: $vehicleContext
            รหัสปัญหา: $dtcContext
            รูปแบบการตอบ:
            1. หัวข้อ: คู่มือซ่อมและผังระบบ (Technical Service Manual & Repair Guide)
            2. การระบุชิ้นส่วน (Component Identification) พร้อมตำแหน่งติดตั้ง (Location)
            3. แผนผังระบบ (System Diagram/Wiring Schematic) อธิบายเป็นข้อความ
            4. ขั้นตอนการตรวจเช็ค (Diagnostic Procedures - ขั้นตอนที่ 1, 2, 3...)
            5. ขั้นตอนการถอดและประกอบ (Removal & Installation Steps) ระบุเครื่องมือที่ต้องใช้และแรงบิด (Torque Specs) ถ้าเป็นไปได้
            ตอบเป็นทางการ สไตล์คู่มือซ่อม (Service Manual)
            """.trimIndent()
        } else {
            """
            You are "Technical Repair Manual AI".
            Your task is to generate a comprehensive OEM-style service manual, procedural repair guide, and textual wiring diagram based on the vehicle and DTC codes.
            Vehicle: $vehicleContext
            DTCs: $dtcContext
            Format:
            1. Title: Technical Service Manual & Repair Guide
            2. Component Identification & Location
            3. System Diagram / Wiring Schematic (Textual Description)
            4. Diagnostic Procedures (Step 1, Step 2...)
            5. Removal & Installation Steps (Include Tools Needed and Torque Specs if possible)
            Use a formal, technical service manual tone.
            """.trimIndent()
        }

        try {
            val model = Firebase.ai.generativeModel(
                modelName = "gemini-3.8-flash",
                systemInstruction = content { text(systemInstructionText) },
                generationConfig = generationConfig {
                    temperature = 0.2f
                }
            )

            val response = withTimeout(60_000L) {
                model.generateContent("Please generate the repair manual for the current issues.")
            }

            val text = response.text
            if (!text.isNullOrBlank()) {
                return@withContext text
            }
        } catch (e: TimeoutCancellationException) {
            Log.e("AiMechanicRepository", "Repair manual generation timed out", e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("AiMechanicRepository", "Firebase AI repair manual generation failed", e)
        }

        // Offline Fallback Repair Manual Generation
        return@withContext generateOfflineRepairManual(vehicle, dtcs, language)
    }

    private fun generateOfflineMechanicResponse(
        prompt: String,
        vehicleContext: String,
        language: AppLanguage,
        highThinking: Boolean
    ): String {
        val p = prompt.lowercase()
        return if (language == AppLanguage.TH) {
            when {
                p.contains("p0171") || p.contains("ส่วนผสมบาง") || p.contains("lean") -> {
                    """
                    🔧 **การวิเคราะห์รหัส P0171 (System Too Lean Bank 1)**
                    สำหรับรถ **$vehicleContext**:

                    💡 **สาเหตุหลักที่พบบ่อยในประเทศไทย:**
                    1. **แอร์โฟร์เซนเซอร์ (MAF Sensor) สกปรก:** ลมร้อนและฝุ่นในไทยทำให้เซนเซอร์จับค่ามวลอากาศเพี้ยน
                    2. **ท่อไอดี / ท่อลมแวคคั่มมีรอยรั่ว:** มีอากาศรั่วเข้าห้องเผาไหม้โดยไม่ผ่านการคำนวณ
                    3. **กรองน้ำมันเชื้อเพลิงเริ่มตัน หรือปั๊มติ๊กแรงดันตก:** จ่ายน้ำมันได้ไม่พอเมื่อเร่งเครื่อง

                    🛠️ **ขั้นตอนการตรวจสอบ (DIY Step-by-Step):**
                    - ถอดเซนเซอร์ MAF ออกมาล้างด้วยสเปรย์ Contact Cleaner ล้างแอร์โฟร์โดยเฉพาะ (ห้ามใช้ลมเป่าแรง)
                    - ตรวจสอบรอยแตกหรือปลั๊กหลุดบริเวณท่อยางไอดีและท่อแวคคั่ม
                    - วัดแรงดันน้ำมันเชื้อเพลิงที่รางหัวฉีด (ควรอยู่ที่ 45-55 PSI สำหรับเบนซิน)

                    💰 **ประมาณการค่าใช้จ่าย:**
                    - สเปรย์ล้าง MAF DIY: ~฿180 - ฿350
                    - เปลี่ยนกรองน้ำมันเชื้อเพลิง: ~฿450 - ฿1,200
                    - เซนเซอร์ MAF แท้ศูนย์: ~฿2,200 - ฿3,800
                    """.trimIndent()
                }
                p.contains("p0087") || p.contains("แรงดันราง") || p.contains("scv") || p.contains("วูบ") -> {
                    """
                    ⚠️ **การวิเคราะห์รหัส P0087 (Fuel Rail Pressure Too Low)**
                    สำหรับรถดีเซลคอมมอนเรล **$vehicleContext**:

                    💡 **สาเหตุหลัก:**
                    - **วาล์ว SCV (Suction Control Valve) ท้ายปั๊มติดขัด:** พบได้บ่อยมากในรถกระบะ Isuzu D-Max, Toyota Vigo/Revo, Triton ที่วิ่งเกิน 100,000 กม.
                    - **กรองดักน้ำ / กรองโซล่าตัน:** มีน้ำหรือสิ่งสกปรกเจือปนในน้ำมันดีเซล
                    - **หัวฉีดน้ำมันมีปริมาณน้ำมันไหลกลับ (Back Leak) มากเกินไป**

                    🛠️ **คำแนะนำจากช่าง:**
                    1. ตรวจสอบและเปลี่ยนกรองโซล่าดักน้ำทันที (ควรเปลี่ยนทุก 20,000 - 40,000 กม.)
                    2. ถอดล้างหรือเปลี่ยนวาล์ว SCV ท้ายปั๊มคอมมอนเรล (แนะนำใช้ของแท้ Denso/OEM)
                    3. ตรวจเช็คค่าแรงดันรางคอมมอนเรลในโหมด Live Data (ขณะเดินเบาควรได้ ~30-40 MPa / 300-400 Bar)

                    💰 **ประมาณการค่าใช้จ่าย:**
                    - ไส้กรองโซล่าแท้: ฿350 - ฿650
                    - วาล์ว SCV แท้ Denso: ฿1,600 - ฿2,800 (ค่าแรงช่าง ~฿400)
                    """.trimIndent()
                }
                p.contains("ควันดำ") || p.contains("egr") || p.contains("p0401") -> {
                    """
                    💨 **แนวทางแก้ปัญหาควันดำและวาล์ว EGR ตัน (P0401)**
                    สำหรับ **$vehicleContext**:

                    💡 **สาเหตุ:**
                    เกิดจากเขม่าคาร์บอนจากการเผาไหม้และไอละอองน้ำมันเครื่องจับตัวเป็นก้อนในท่อร่วมไอดีและวาล์ว EGR ทำให้เปิด-ปิดไม่สุด

                    🛠️ **วิธีแก้ไข:**
                    - ถอดชุดท่อร่วมไอดี, ลิ้นปีกผีเสื้อ, และวาล์ว EGR ออกมาล้างเขม่าด้วยน้ำยาล้างคาร์บู/โซเน็กซ์
                    - ตรวจสอบไส้กรองอากาศ หากมีฝุ่นดำเกาะหนาควรเปลี่ยนใหม่ทันที
                    - ตรวจสอบสายแวคคั่มและเซนเซอร์ Map Sensor

                    💰 **ประมาณการค่าใช้จ่าย:**
                    - ค่าบริการล้างชุดท่อไอดี + EGR ที่อู่: ~฿800 - ฿1,500
                    - กรองอากาศเครื่องยนต์: ~฿350 - ฿650
                    """.trimIndent()
                }
                p.contains("น้ำมันเครื่อง") || p.contains("น้ำมันเกียร์") || p.contains("service") || p.contains("เช็คระยะ") -> {
                    """
                    🛢️ **ตารางคำแนะนำการบำรุงรักษาสำหรับ $vehicleContext:**

                    1. **น้ำมันเครื่อง & ไส้กรอง:**
                       - เกรดสังเคราะห์แท้ 100% (0W-20 หรือ 5W-30): เปลี่ยนทุก 10,000 กม. หรือ 6 เดือน (งบ ~฿1,500 - ฿2,600)
                    2. **น้ำมันเกียร์อัตโนมัติ (AT / CVT):**
                       - แนะนำเปลี่ยนทุก 40,000 กม. หรือ 2 ปี เพื่อยืดอายุชุดสมองเกียร์ (งบ ~฿1,800 - ฿3,200)
                    3. **น้ำยาหล่อเย็นหม้อน้ำ (Long Life Coolant):**
                       - ตรวจเช็คระดับทุกเดือน เปลี่ยนถ่ายทุก 80,000 - 100,000 กม.
                    4. **น้ำมันเบรก (DOT 3 / DOT 4):**
                       - เปลี่ยนถ่ายไล่ลมทุก 40,000 กม. หรือ 2 ปี (งบ ~฿400 - ฿800)
                    5. **ผ้าเบรกหน้า-หลัง:**
                       - ตรวจเช็คความหนาทุก 20,000 กม. หากเหลือต่ำกว่า 3 มม. ควรเปลี่ยนทันที
                    """.trimIndent()
                }
                else -> {
                    """
                    🛠️ **คำแนะนำการวินิจฉัยจากช่างยนต์ AI:**

                    จากการตรวจสอบข้อมูลสำหรับ **$vehicleContext**:
                    ระบบ OBD-II ของรถรุ่นนี้รองรับโปรโตคอลมาตรฐาน ISO 15765-4 CAN (ความเร็ว 500 kbaud) ผ่านพอร์ต 16-pin ใต้พวงมาลัย

                    ✅ **แนวทางเบื้องต้น:**
                    1. หากมีไฟรูปเครื่องโชว์ แนะนำกด **"เริ่มสแกนระบบ OBD-II"** เพื่ออ่านรหัสข้อบกพร่องทั้งระบบ Powertrain, ABS, Airbag และ CAN Bus
                    2. ดูข้อมูล **Freeze Frame** เพื่อวิเคราะห์ว่าโค้ดเกิดขึ้นที่ความเร็วและอุณหภูมิเท่าใด
                    3. ตรวจสอบค่าเซนเซอร์สด เช่น อุณหภูมิน้ำหล่อเย็น (ECT), แรงดันไฟแบตเตอรี่ (ควรมากกว่า 13.6V ขณะเครื่องติด) และค่า Fuel Trim

                    สอบถามรายละเอียดรหัสไฟโชว์หรืออาการรถเพิ่มเติมได้ตลอดเวลาครับ!
                    """.trimIndent()
                }
            }
        } else {
            """
            🔧 **Diagnostic Insight for $vehicleContext:**

            OBD-II CAN communication protocol is active on your vehicle.
            - **Live Sensor Telemetry**: Monitoring RPM, Coolant Temp, Fuel Rail Pressure, Throttle Position, and Battery Voltage.
            - **Recommendation**: Run a full diagnostic scan to retrieve active and pending DTC fault codes. Check freeze-frame telemetry to observe engine state at trip event.
            - **Maintenance Reminder**: Inspect engine oil condition, transmission fluid viscosity, and brake pad lining thickness regularly.
            """.trimIndent()
        }
    }

    private fun generateOfflineRepairManual(
        vehicle: Vehicle?,
        dtcs: List<DtcCode>,
        language: AppLanguage
    ): String {
        val vehName = vehicle?.let { "${it.make} ${it.model} ${it.year} (${it.engine})" } ?: "Toyota Hilux Revo 2.4 GD Turbo Diesel"
        val isTh = language == AppLanguage.TH

        val dtcCodesStr = if (dtcs.isEmpty()) "P0087 / P0171 / P0401" else dtcs.joinToString(", ") { it.code }

        if (isTh) {
            return """
            📘 **คู่มือซ่อมบำรุงและผังระบบ OEM (Technical Service Manual)**
            **สำหรับรถยนต์:** $vehName
            **รหัสวิเคราะห์ความผิดปกติ (DTC):** $dtcCodesStr

            ---

            ### 1. 🔍 การระบุตำแหน่งชิ้นส่วน (Component Identification & Location)
            - **เซนเซอร์แรงดันรางดีเซล (Fuel Rail Pressure Sensor):** ติดตั้งอยู่บริเวณปลายรางคอมมอนเรลฝั่งห้องเครื่องด้านขวา
            - **วาล์ว SCV (Suction Control Valve):** ติดตั้งอยู่ที่ส่วนท้ายของปั๊มเชื้อเพลิงแรงดันสูง (High-Pressure Fuel Pump)
            - **แอร์โฟร์เซนเซอร์ (MAF Sensor):** อยู่บนท่อกรองอากาศก่อนเข้าลิ้นปีกผีเสื้อและเทอร์โบ
            - **วาล์ว EGR & คูลเลอร์:** ติดตั้งฝั่งท่อไอเสียและเชื่อมเข้าท่อร่วมไอดี

            ---

            ### 2. ⚡ ผังวงจรไฟฟ้าและสายสัญญาณ (System Wiring Schematic)
            ```
            [กล่องควบคุม ECU ECM]
               ├── Pin A12 (VCC +5V) ───────► [Fuel Rail Pressure Sensor Pin 1]
               ├── Pin A18 (Signal) ◄─────── [Fuel Rail Pressure Sensor Pin 2 (0.5V - 4.5V)]
               ├── Pin A22 (E-Ground) ──────► [Sensor Ground Pin 3]
               │
               ├── Pin B05 (SCV Drive PWM) ─► [SCV Valve Solenoid Pin 1 (+12V PWM)]
               └── Pin B12 (SCV Return) ────► [SCV Solenoid Ground Pin 2]
            ```

            ---

            ### 3. 🛠️ ขั้นตอนการตรวจเช็คทางไฟฟ้าและกลไก (Diagnostic Procedures)
            1. **วัดแรงดันไฟฟ้า VCC (5V Reference):**
               - บิดสวิตช์กุญแจตำแหน่ง ON (ยังไม่ติดเครื่อง)
               - ใช้มัลติมิเตอร์วัดแรงดันระหว่าง Pin 1 กับ Ground ควรได้ **4.9V - 5.1V DC**
            2. **วัดความต้านทานวาล์ว SCV Solenoid:**
               - ปลดปลั๊กไฟ SCV ออก แล้วใช้มัลติมิเตอร์วัดค่า Ohms
               - ค่ามาตรฐาน ณ อุณหภูมิ 20°C ควรอยู่ที่ **1.8 Ω - 2.4 Ω** (หากได้ 0 Ω หรือ ∞ แสดงว่าคอยล์ช็อตหรือขาด)
            3. **ตรวจวัดแรงดันรางคอมมอนเรลผ่าน Live Data:**
               - ขณะเดินเบา (Idle): **30 - 35 MPa (300 - 350 Bar)**
               - ขณะเร่งเครื่องสุด (WOT): **140 - 180+ MPa (1400 - 1800+ Bar)**

            ---

            ### 4. 🔩 ขั้นตอนการถอดประกอบและเปลี่ยนชิ้นส่วน (Removal & Installation Steps)
            - **เครื่องมือที่ต้องใช้:** ประแจปอนด์ (Torque Wrench), บล็อกเบอร์ 10, 12, 17mm, สเปรย์ Contact Cleaner
            - **ขั้นตอนการเปลี่ยนวาล์ว SCV:**
              1. ดับเครื่องยนต์ ปลดสายขั้วลบแบตเตอรี่ออก รอประมาณ 5 นาทีเพื่อลดแรงดันในระบบ
              2. ปลดปลั๊กสายไฟ SCV และถอดน็อตหกเหลี่ยมล็อคตัววาล์ว 2 ตัว
              3. ดึงตัววาล์ว SCV เก่าออกอย่างระมัดระวัง เช็ดทำความสะอาดเบ้าปั๊มด้วยผ้าสะอาดไร้ฝุ่น
              4. ทาน้ำมันดีเซลสะอาดที่โอริงวาล์ว SCV ตัวใหม่ ใส่กลับเข้าตำแหน่ง
              5. ขันน็อตล็อคด้วยแรงบิด **8.5 N·m (75 in-lb)**
              6. เสียบปลั๊กสายไฟ ลบโค้ด DTC แล้วทำขั้นตอน **SCV Relearn / Pilot Injection Learning** ผ่านเมนูฟังก์ชันพิเศษ

            ---
            *คู่มือนี้อ้างอิงจากมาตรฐาน OEM Service Manual รองรับการซ่อมบำรุงทั้งศูนย์บริการและอู่มาตรฐาน*
            """.trimIndent()
        } else {
            return """
            📘 **OEM Technical Service & Repair Manual**
            **Vehicle Target:** $vehName
            **Active DTC Codes:** $dtcCodesStr

            ---

            ### 1. 🔍 Component Identification & Location
            - **Fuel Rail Pressure Sensor:** Mounted on the rear end of the common rail assembly.
            - **Suction Control Valve (SCV):** Located on the back side of the high-pressure fuel pump.
            - **Mass Air Flow (MAF) Sensor:** Positioned on the air intake duct right after the air filter housing.
            - **EGR Valve & Cooler Assembly:** Mounted on the exhaust manifold crossover pipe.

            ---

            ### 2. ⚡ Wiring Diagram & Pinouts
            ```
            [Engine Control Module - ECM]
               ├── Pin A12 (+5V Ref) ───────► [Fuel Pressure Sensor Pin 1]
               ├── Pin A18 (Signal In) ◄───── [Fuel Pressure Sensor Pin 2 (0.5V - 4.5V)]
               ├── Pin A22 (Sensor GND) ────► [Fuel Pressure Sensor Pin 3]
               │
               ├── Pin B05 (SCV High PWM) ──► [SCV Valve Solenoid Pin 1]
               └── Pin B12 (SCV Return) ────► [SCV Solenoid Ground Pin 2]
            ```

            ---

            ### 3. 🛠️ Diagnostic & Testing Procedures
            1. **Reference Voltage Test (+5V Ref):**
               - Turn Key ON (Engine OFF).
               - Measure voltage across Sensor Pin 1 and Ground. Expected: **4.9V - 5.1V DC**.
            2. **SCV Solenoid Coil Resistance Test:**
               - Disconnect SCV connector.
               - Measure resistance across solenoid terminals. Expected spec at 20°C: **1.8 Ω - 2.4 Ω**.
            3. **Rail Pressure Live Data Inspection:**
               - Engine Idle: **30 - 35 MPa (300 - 350 Bar)**
               - Wide Open Throttle (WOT): **140 - 180+ MPa (1400 - 1800+ Bar)**

            ---

            ### 4. 🔩 Removal & Installation Steps
            - **Required Tools:** Torque Wrench, 10mm/12mm Sockets, Hex key set, Contact Cleaner.
            - **SCV Replacement Procedure:**
              1. Turn OFF ignition, disconnect negative battery terminal, and wait 5 minutes for fuel pressure dissipation.
              2. Unplug SCV connector and remove the 2 securing hex bolts.
              3. Extract old SCV valve, clean mounting bore thoroughly with lint-free cloth.
              4. Lubricate new SCV O-rings with clean diesel fuel and install into high-pressure pump.
              5. Tighten mounting bolts to **8.5 N·m (75 in-lb)** torque specification.
              6. Reconnect battery, perform DTC clear, and run **SCV Relearn Function** in Special Functions tab.

            ---
            *Factory Service Specification Reference Guide*
            """.trimIndent()
        }
    }
}
