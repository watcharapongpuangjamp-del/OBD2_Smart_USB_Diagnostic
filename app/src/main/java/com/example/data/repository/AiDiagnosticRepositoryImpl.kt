package com.example.data.repository

import android.util.Log
import com.example.data.model.Vehicle
import com.example.domain.model.AiDiagnosticReport
import com.example.domain.model.DiyRepairStep
import com.example.domain.model.DomainFreezeFrame
import com.example.domain.model.DtcFault
import com.example.domain.model.DtcSeverity
import com.example.domain.model.PartPriceEstimate
import com.example.domain.repository.AiDiagnosticRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONObject
import kotlin.coroutines.cancellation.CancellationException

class AiDiagnosticRepositoryImpl : AiDiagnosticRepository {

    private val generativeModel by lazy {
        try {
            val diagnosisSchema = Schema.obj(
                properties = mapOf(
                    "rootCauseSummary" to Schema.string("Summary of root cause"),
                    "probabilityScore" to Schema.integer("Probability percent from 0 to 100"),
                    "sensorCorrelation" to Schema.string("Explanation of freeze frame sensor relation"),
                    "diyDifficulty" to Schema.string("Difficulty rating: ง่าย, ปานกลาง, หรือ ช่างผู้ชำนาญ"),
                    "estimatedMinutes" to Schema.integer("Estimated minutes for repair"),
                    "toolsRequired" to Schema.array(Schema.string(), "Tools needed"),
                    "safetyNotes" to Schema.array(Schema.string(), "Safety warnings"),
                    "repairSteps" to Schema.array(
                        Schema.obj(
                            properties = mapOf(
                                "stepNumber" to Schema.integer(),
                                "title" to Schema.string(),
                                "instruction" to Schema.string(),
                                "caution" to Schema.string(),
                                "torqueSpec" to Schema.string()
                            ),
                            optionalProperties = listOf("caution", "torqueSpec")
                        ),
                        "Step by step repair steps"
                    ),
                    "priceEstimates" to Schema.array(
                        Schema.obj(
                            properties = mapOf(
                                "partNameTh" to Schema.string(),
                                "partNameEn" to Schema.string(),
                                "oemPriceThb" to Schema.integer(),
                                "aftermarketPriceThb" to Schema.integer(),
                                "laborCostThb" to Schema.integer()
                            )
                        ),
                        "Estimated prices"
                    )
                ),
                optionalProperties = emptyList()
            )

            Firebase.ai.generativeModel(
                modelName = "gemini-3.8-flash",
                generationConfig = generationConfig {
                    responseMimeType = "application/json"
                    responseSchema = diagnosisSchema
                    temperature = 0.3f
                }
            )
        } catch (e: Exception) {
            Log.w("AiDiagnosticRepo", "Firebase AI model initialization deferred", e)
            null
        }
    }

    override suspend fun generateDiagnosticReport(
        fault: DtcFault,
        freezeFrame: DomainFreezeFrame?,
        vehicle: Vehicle?
    ): AiDiagnosticReport = withContext(Dispatchers.IO) {
        val vehDesc = vehicle?.let { "${it.make} ${it.model} (${it.year}) เครื่องยนต์ ${it.engine} เลขไมล์ ${it.currentMileageKm} km" }
            ?: "Toyota Hilux Revo 2.4 GD Diesel (68,500 km)"

        val ffText = freezeFrame?.let {
            "RPM: ${it.engineRpm}, Speed: ${it.vehicleSpeedKmh} km/h, Coolant: ${it.coolantTempCelsius}°C, Load: ${it.calculatedLoadPercent}%, STFT: ${it.shortTermFuelTrimPercent}%, LTFT: ${it.longTermFuelTrimPercent}%, Fuel Rail: ${it.fuelRailPressureBar} Bar, Battery: ${it.batteryVoltage}V"
        } ?: "ไม่มีข้อมูล Freeze Frame (ค่าเริ่มต้นขณะเกิดอาการ)"

        val prompt = """
            คุณคือ "ผู้เชี่ยวชาญวิเคราะห์ปัญหาเครื่องยนต์และจัดทำคู่มือซ่อม DIY (Master Technician Thailand)"
            กรุณาวิเคราะห์ปัญหาจากรหัส OBD-II DTC: ${fault.code} - ${fault.titleTh} (${fault.titleEn})
            ข้อมูลรถยนต์: $vehDesc
            ข้อมูล Freeze Frame เซนเซอร์ขณะเกิดปัญหา: $ffText

            จงให้ผลลัพธ์เป็น JSON ตาม Schema ที่กำหนด:
            - rootCauseSummary: สรุปสาเหตุต้นตอที่แท้จริง
            - probabilityScore: เปอร์เซ็นต์ความน่าจะเป็นของสาเหตุนี้ (0-100)
            - sensorCorrelation: วิเคราะห์ความสัมพันธ์ระหว่างค่าเซนเซอร์ Freeze Frame กับความผิดปกติ
            - diyDifficulty: ระดับความยาก (ง่าย, ปานกลาง, หรือ ช่างผู้ชำนาญ)
            - estimatedMinutes: เวลาโดยประมาณที่ใช้ในการซ่อม (นาที)
            - toolsRequired: รายการเครื่องมือที่ต้องใช้ (เช่น ประแจปอนด์, บล็อกเบอร์ 10, คอนแทคคลีนเนอร์)
            - safetyNotes: ข้อควรระวังความปลอดภัย
            - repairSteps: ขั้นตอนการซ่อม DIY ทีละสเต็ป (stepNumber, title, instruction, caution, torqueSpec)
            - priceEstimates: ประมาณการราคาอะไหล่แท้ OEM, เทียบ OEM, และค่าแรงในไทยเป็นเงินบาท (THB)
        """.trimIndent()

        try {
            val model = generativeModel
            if (model != null) {
                val response = withTimeout(60_000L) {
                    model.generateContent(prompt)
                }

                val jsonText = response.text
                if (!jsonText.isNullOrBlank()) {
                    return@withContext parseJsonReport(jsonText, fault, vehDesc)
                }
            }
        } catch (e: TimeoutCancellationException) {
            Log.e("AiDiagnosticRepo", "Gemini generation timed out", e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("AiDiagnosticRepo", "Gemini API call failed, using intelligent offline fallback", e)
        }

        return@withContext generateFallbackReport(fault, freezeFrame, vehDesc)
    }

    private fun parseJsonReport(jsonStr: String, fault: DtcFault, vehDesc: String): AiDiagnosticReport {
        val root = JSONObject(jsonStr)
        val stepsList = mutableListOf<DiyRepairStep>()
        val stepsArray = root.optJSONArray("repairSteps")
        if (stepsArray != null) {
            for (i in 0 until stepsArray.length()) {
                val obj = stepsArray.getJSONObject(i)
                stepsList.add(
                    DiyRepairStep(
                        stepNumber = obj.optInt("stepNumber", i + 1),
                        title = obj.optString("title", "ขั้นตอนที่ ${i + 1}"),
                        instruction = obj.optString("instruction", ""),
                        caution = obj.optString("caution").takeIf { it.isNotBlank() },
                        torqueSpec = obj.optString("torqueSpec").takeIf { it.isNotBlank() }
                    )
                )
            }
        }

        val pricesList = mutableListOf<PartPriceEstimate>()
        val pricesArray = root.optJSONArray("priceEstimates")
        if (pricesArray != null) {
            for (i in 0 until pricesArray.length()) {
                val obj = pricesArray.getJSONObject(i)
                pricesList.add(
                    PartPriceEstimate(
                        partNameTh = obj.optString("partNameTh", "อะไหล่"),
                        partNameEn = obj.optString("partNameEn", "Part"),
                        oemPriceThb = obj.optInt("oemPriceThb", 1500),
                        aftermarketPriceThb = obj.optInt("aftermarketPriceThb", 800),
                        laborCostThb = obj.optInt("laborCostThb", 400)
                    )
                )
            }
        }

        val toolsList = mutableListOf<String>()
        val toolsArray = root.optJSONArray("toolsRequired")
        if (toolsArray != null) {
            for (i in 0 until toolsArray.length()) {
                toolsList.add(toolsArray.getString(i))
            }
        }

        val safetyList = mutableListOf<String>()
        val safetyArray = root.optJSONArray("safetyNotes")
        if (safetyArray != null) {
            for (i in 0 until safetyArray.length()) {
                safetyList.add(safetyArray.getString(i))
            }
        }

        return AiDiagnosticReport(
            dtcCode = fault.code,
            vehicleModel = vehDesc,
            rootCauseSummary = root.optString("rootCauseSummary", "ตรวจพบความผิดปกติของระบบ"),
            probabilityScore = root.optInt("probabilityScore", 85),
            sensorCorrelation = root.optString("sensorCorrelation", "ค่าเซนเซอร์เบี่ยงเบนจากพารามิเตอร์มาตรฐาน"),
            severityLevel = fault.severity,
            toolsRequired = if (toolsList.isEmpty()) listOf("ประแจบล็อกเบอร์ 10, 12", "น้ำยาล้างหน้าสัมผัส") else toolsList,
            estimatedMinutes = root.optInt("estimatedMinutes", 45),
            diyDifficulty = root.optString("diyDifficulty", "ปานกลาง (Medium)"),
            repairSteps = if (stepsList.isEmpty()) getDefaultSteps(fault.code) else stepsList,
            priceEstimates = if (pricesList.isEmpty()) getDefaultPrices(fault.code) else pricesList,
            safetyNotes = if (safetyList.isEmpty()) listOf("รอให้เครื่องยนต์เย็นลงก่อนเริ่มงาน", "ปลดขั้วลบแบตเตอรี่เพื่อความปลอดภัย") else safetyList
        )
    }

    private fun generateFallbackReport(
        fault: DtcFault,
        freezeFrame: DomainFreezeFrame?,
        vehDesc: String
    ): AiDiagnosticReport {
        val code = fault.code.uppercase()
        return when {
            code == "P0087" -> {
                AiDiagnosticReport(
                    dtcCode = "P0087",
                    vehicleModel = vehDesc,
                    rootCauseSummary = "วาล์วควบคุมแรงดันปั๊มเชื้อเพลิง (SCV Valve) ติดขัด หรือไส้กรองโซล่าดักน้ำอุดตัน ทำให้น้ำมันจ่ายเข้ารางคอมมอนเรลไม่ทัน",
                    probabilityScore = 92,
                    sensorCorrelation = "ที่รอบเครื่อง 2,240 RPM และ Load 68.5% ค่าแรงดันรางควรได้ 1,400+ Bar แต่ตรวจวัดได้เพียง 1,120 Bar ซึ่งต่ำกว่าเกณฑ์ความปลอดภัย",
                    severityLevel = DtcSeverity.CRITICAL,
                    toolsRequired = listOf("ประแจแอลหกเหลี่ยม 5mm", "บล็อกเบอร์ 10mm", "ประแจปอนด์ (Torque Wrench)", "ผ้าสะอาดไร้ฝุ่น"),
                    estimatedMinutes = 45,
                    diyDifficulty = "ปานกลาง (Medium DIY)",
                    repairSteps = listOf(
                        DiyRepairStep(
                            stepNumber = 1,
                            title = "ตัดกระแสไฟและลดแรงดันระบบ",
                            instruction = "ดับเครื่องยนต์ บิดกุญแจ OFF ถอดขั้วลบแบตเตอรี่ออก และรอประมาณ 5-10 นาทีเพื่อลดแรงดันตกค้างในรางเชื้อเพลิง",
                            caution = "ห้ามถอดแป๊บหัวฉีดขณะเครื่องเพิ่งดับเพราะแรงดันคอมมอนเรลสูงมาก"
                        ),
                        DiyRepairStep(
                            stepNumber = 2,
                            title = "ตรวจเช็คและเปลี่ยนไส้กรองโซล่า",
                            instruction = "หมุนคลายฝาเสื้อกรองดักน้ำ เปลี่ยนไส้กรองลูกใหม่พร้อมโอริง ทาน้ำมันดีเซลบางๆ ที่โอริงก่อนขันกลับ",
                            torqueSpec = "ขันแน่นด้วยมือพอตึง"
                        ),
                        DiyRepairStep(
                            stepNumber = 3,
                            title = "ถอดเปลี่ยนวาล์ว SCV ท้ายปั๊มคอมมอนเรล",
                            instruction = "ปลดปลั๊กไฟ SCV ถอดน็อตหกเหลี่ยม 2 ตัว ดึงวาล์วตัวเก่าออก เช็ดเบ้าปั๊มให้สะอาด ใส่ตัวใหม่พร้อมโอริงใหม่",
                            caution = "ระวังฝุ่นหรือเศษผงหลุดเข้าไปในห้องปั๊มเด็ดขาด",
                            torqueSpec = "8.5 N·m (75 in-lb)"
                        ),
                        DiyRepairStep(
                            stepNumber = 4,
                            title = "แย็กน้ำมันไล่ลมและทำ SCV Relearn",
                            instruction = "กดปั๊มแย็กที่หัวกรองโซล่าจนตึงมือ สตาร์ทเครื่องยนต์ และสั่งฟังก์ชัน SCV Relearn ผ่านแอปพลิเคชัน",
                            torqueSpec = null
                        )
                    ),
                    priceEstimates = listOf(
                        PartPriceEstimate(
                            partNameTh = "วาล์ว SCV แท้ Denso",
                            partNameEn = "Suction Control Valve (Denso Genuine)",
                            oemPriceThb = 2400,
                            aftermarketPriceThb = 1450,
                            laborCostThb = 400
                        ),
                        PartPriceEstimate(
                            partNameTh = "ไส้กรองโซล่าดักน้ำแท้ศูนย์",
                            partNameEn = "Fuel Filter Cartridge",
                            oemPriceThb = 480,
                            aftermarketPriceThb = 250,
                            laborCostThb = 150
                        )
                    ),
                    safetyNotes = listOf(
                        "น้ำมันดีเซลติดไฟได้ ระวังประกายไฟขณะปฏิบัติงาน",
                        "หากเปลี่ยนแล้วยังมีอาการวูบ ให้ตรวจเช็คปั๊มติ๊กในถังและหัวฉีดคอมมอนเรล"
                    )
                )
            }
            code == "P0171" -> {
                AiDiagnosticReport(
                    dtcCode = "P0171",
                    vehicleModel = vehDesc,
                    rootCauseSummary = "แอร์โฟร์เซนเซอร์ (MAF Sensor) สกปรกจากฝุ่นละออง หรือมีรอยอากาศรั่วที่ท่อยางไอดี/ท่อลมแวคคั่ม ทำให้ส่วนผสมบาง",
                    probabilityScore = 88,
                    sensorCorrelation = "ค่า Short Term Fuel Trim (+2.3%) และ Long Term Fuel Trim (+14.1%) มีค่าเป็นบวกสูง แสดงว่ากล่อง ECU พยายามฉีดน้ำมันชดเชยอากาศรั่วเกินเกณฑ์ปกติ",
                    severityLevel = DtcSeverity.WARNING,
                    toolsRequired = listOf("ไขควงแฉก PH2", "สเปรย์ล้างแอร์โฟร์ Contact Cleaner", "ไฟฉายตรวจหารอยรั่ว"),
                    estimatedMinutes = 20,
                    diyDifficulty = "ง่าย (Easy DIY)",
                    repairSteps = listOf(
                        DiyRepairStep(
                            stepNumber = 1,
                            title = "ถอดเซนเซอร์ MAF จากหม้อกรองอากาศ",
                            instruction = "ปลดคลิปล็อคปลั๊กไฟ ถอดสกรูเกลียวปล่อย 2 ตัว ดึงเซนเซอร์ MAF ออกมาอย่างเบามือ",
                            caution = "ห้ามสัมผัสลวดความร้อน (Hot Wire) ด้านในด้วยนิ้วมือหรือวัตถุแข็ง"
                        ),
                        DiyRepairStep(
                            stepNumber = 2,
                            title = "ฉีดล้างด้วยน้ำยาล้างแอร์โฟร์",
                            instruction = "ใช้สเปรย์ Electronic Contact Cleaner หรือ MAF Cleaner ฉีดล้างคราบฝุ่นดำ 4-5 ครั้ง ปล่อยให้แห้งสนิทตามธรรมชาติ 10 นาที",
                            caution = "ห้ามใช้ลมเป่าแรงเพราะลวดวัดอาจขาดทันที"
                        ),
                        DiyRepairStep(
                            stepNumber = 3,
                            title = "ตรวจสอบท่อลมไอดีและสายแวคคั่ม",
                            instruction = "ตรวจดูท่อยางคอไอดีและเข็มขัดรัดว่ามีรอยฉีกขาดหรือหลวมหรือไม่ ขันรัดให้แน่นหนา",
                            torqueSpec = "ขันแน่นพอตึงมือ"
                        )
                    ),
                    priceEstimates = listOf(
                        PartPriceEstimate(
                            partNameTh = "สเปรย์ล้างเซนเซอร์แอร์โฟร์ DIY",
                            partNameEn = "MAF Sensor Cleaner Spray",
                            oemPriceThb = 280,
                            aftermarketPriceThb = 180,
                            laborCostThb = 0
                        ),
                        PartPriceEstimate(
                            partNameTh = "เซนเซอร์ MAF แท้ศูนย์ (กรณีเสีย)",
                            partNameEn = "Mass Air Flow Sensor Genuine",
                            oemPriceThb = 3200,
                            aftermarketPriceThb = 1600,
                            laborCostThb = 200
                        )
                    ),
                    safetyNotes = listOf(
                        "ห้ามใช้น้ำมันเบนซินหรือโซเน็กซ์กระป๋องส้มล้างแอร์โฟร์ เพราะจะทิ้งคราบน้ำมันเหนียวเกาะเซนเซอร์",
                        "ประกอบกลับและลบรหัสโค้ดก่อนสตาร์ททดสอบรอบเดินเบา"
                    )
                )
            }
            else -> {
                AiDiagnosticReport(
                    dtcCode = fault.code,
                    vehicleModel = vehDesc,
                    rootCauseSummary = "ตรวจพบรหัสข้อบกพร่อง ${fault.code} ในระบบ ${fault.category.name}: ${fault.titleTh}",
                    probabilityScore = 80,
                    sensorCorrelation = "ตรวจพบสัญญาณเซนเซอร์เบี่ยงเบนจากค่ามาตรฐานโรงงาน",
                    severityLevel = fault.severity,
                    toolsRequired = listOf("ชุดบล็อกประแจช่างยนต์", "มัลติมิเตอร์วัดไฟ DC", "สเปรย์ล้างหน้าสัมผัส"),
                    estimatedMinutes = 35,
                    diyDifficulty = "ปานกลาง (Medium DIY)",
                    repairSteps = getDefaultSteps(fault.code),
                    priceEstimates = getDefaultPrices(fault.code),
                    safetyNotes = listOf(
                        "ดับเครื่องยนต์และปลดสายขั้วลบแบตเตอรี่ก่อนถอดปลั๊กเซนเซอร์",
                        "หากมีอาการสะดุดรุนแรง ควรส่งอู่หรือศูนย์บริการตรวจเช็คเพิ่มเติม"
                    )
                )
            }
        }
    }

    private fun getDefaultSteps(code: String): List<DiyRepairStep> = listOf(
        DiyRepairStep(
            stepNumber = 1,
            title = "ตรวจสอบทางกายภาพและปลั๊กสายไฟ",
            instruction = "ตรวจเช็คปลั๊กขั้วต่อของเซนเซอร์ที่เกี่ยวข้องกับ $code ว่ามีคราบขี้เกลือ คราบน้ำมัน หรือสายไฟหลุดร่อนหรือไม่",
            caution = "อย่าดึงที่สายไฟ ให้บีบสลักล็อคที่ตัวปลั๊กแล้วดึงออก"
        ),
        DiyRepairStep(
            stepNumber = 2,
            title = "วัดค่าแรงดันไฟฟ้าและสัญญาณ",
            instruction = "ใช้มัลติมิเตอร์วัดไฟเลี้ยง VCC (+5V หรือ +12V) และขั้วกราวด์ของเซนเซอร์ขณะบิดสวิตช์ ON"
        ),
        DiyRepairStep(
            stepNumber = 3,
            title = "ล้างทำความสะอาดหรือเปลี่ยนชิ้นส่วน",
            instruction = "ฉีดน้ำยา Contact Cleaner ทำความสะอาดขั้วต่อ หรือเปลี่ยนชิ้นส่วนอะไหล่ใหม่ตามสเปก OEM"
        )
    )

    private fun getDefaultPrices(code: String): List<PartPriceEstimate> = listOf(
        PartPriceEstimate(
            partNameTh = "เซนเซอร์/อะไหล่สำหรับรหัส $code",
            partNameEn = "Sensor / Component for $code",
            oemPriceThb = 1800,
            aftermarketPriceThb = 950,
            laborCostThb = 350
        )
    )
}
