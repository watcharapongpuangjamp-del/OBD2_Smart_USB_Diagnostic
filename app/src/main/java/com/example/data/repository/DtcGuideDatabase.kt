package com.example.data.repository

import com.example.data.model.DiagnosticStep
import com.example.data.model.DiagnosticStepType
import com.example.data.model.DtcSeverity
import com.example.data.model.DtcTroubleshootingGuide
import com.example.data.model.MultimeterTestDetail

object DtcGuideDatabase {

    val guides: Map<String, DtcTroubleshootingGuide> = mapOf(
        // ================= P0087: Common Rail Pressure Too Low =================
        "P0087" to DtcTroubleshootingGuide(
            dtcCode = "P0087",
            titleTh = "คู่มือตรวจเช็ก: แรงดันในรางคอมมอนเรลต่ำกว่าเกณฑ์ (Fuel Rail Pressure Low)",
            titleEn = "Diagnostic Guide: Fuel Rail Pressure Below Minimum Threshold",
            severity = DtcSeverity.CRITICAL,
            affectedSystemTh = "ระบบฉีดจ่ายน้ำมันดีเซลคอมมอนเรลแรงดันสูง (Common Rail System)",
            requiredToolsTh = listOf(
                "มัลติมิเตอร์แบบดิจิตอล (DMM) พร้อมหัวโพรบแหลม (Back-probe pins)",
                "ประแจเบอร์ 10 และ 12 mm สำหรับถอดฝาครอบวาล์ว SCV",
                "เกจวัดแรงดันต่ำ (0-10 Bar) สำหรับวัดแรงดันปั๊มติ๊กในถัง/ท่อดูด",
                "น้ำยาล้างเบรกหรือคอนแทคคลีนเนอร์สำหรับทำความสะอาดปลั๊ก"
            ),
            safetyPrecautionsTh = "ห้ามคลายแป๊บน้ำมันแรงดันสูงขณะเครื่องยนต์กำลังทำงานหรือเพิ่งดับใหม่ๆ เนื่องจากแรงดันสูงกว่า 1,600 - 2,000 Bar อาจฉีดทะลุผิวหนังได้",
            steps = listOf(
                DiagnosticStep(
                    stepNumber = 1,
                    titleTh = "ตรวจสอบกรองดักน้ำและกรองโซล่า (Fuel Filter Check)",
                    titleEn = "Visual & Fuel Filter Inspection",
                    stepType = DiagnosticStepType.VISUAL_MECHANICAL,
                    descriptionTh = "ตรวจสอบไฟเตือนกรองดักน้ำที่หน้าปัด เปิดวาล์วเดรนน้ำใต้กรองดักน้ำเพื่อระบายสิ่งปนเปื้อน และตรวจดูระยะการเปลี่ยนกรองโซล่า (หากเกิน 20,000 กม. ในไทยมักเกิดจากคราบตะกอนไบโอดีเซล B10/B20 อุดตัน)",
                    descriptionEn = "Inspect fuel sedimenter warning light and drain water separator. Check fuel filter replacement mileage.",
                    actionTipsTh = listOf(
                        "บีบปั๊มแย๊กที่หัวกรองโซล่าดูว่าแข็งตึงมือหรือไม่ หากปั๊มแล้วยวบตลอดแสดงว่ามีอากาศรั่วในระบบท่อดูด",
                        "ตรวจหารอยแตกลายงาที่ท่อยางน้ำมันไหลกลับหัวฉีด"
                    )
                ),
                DiagnosticStep(
                    stepNumber = 2,
                    titleTh = "วัดค่าความต้านทานขดลวดวาล์ว SCV ท้ายปั๊ม (SCV Resistance Test)",
                    titleEn = "SCV Solenoid Coil Resistance Measurement",
                    stepType = DiagnosticStepType.MULTIMETER_ELECTRICAL,
                    descriptionTh = "ปลดปลั๊กขั้วต่อ 2 พินที่ตัววาล์ว SCV (Suction Control Valve) ท้ายปั๊มคอมมอนเรล ตั้งมัลติมิเตอร์ที่ย่านวัดความต้านทาน (Ω)",
                    descriptionEn = "Disconnect 2-pin connector at SCV valve and measure internal coil resistance.",
                    multimeterTest = MultimeterTestDetail(
                        componentName = "วาล์วควบคุมแรงดันดูดน้ำมัน SCV (Denso / Bosch)",
                        harnessConnectorTh = "ขั้วต่อ 2 พินที่ตัว SCV ท้ายปั๊มแรงดันสูง",
                        harnessConnectorEn = "2-pin harness connector on SCV pump rear",
                        testMode = "ความต้านทานไฟฟ้า (Resistance Ω)",
                        redProbeLocationTh = "พินที่ 1 ของตัววาล์ว SCV",
                        blackProbeLocationTh = "พินที่ 2 ของตัววาล์ว SCV",
                        testConditionTh = "ดับเครื่องยนต์ บิดกุญแจ OFF ปลดปลั๊กวัดที่ตัวอุปกรณ์ (อุณหภูมิห้อง 20°C - 30°C)",
                        normalRange = "1.5 Ω - 2.6 Ω (รุ่น SCV สั้น) หรือ 6.8 Ω - 7.8 Ω (รุ่น SCV ยาว)",
                        abnormalDiagnosisTh = "หากวัดได้ 0 Ω (ลัดวงจรภายใน) หรือค่าสูงเกิน 10 Ω / OL (ขดลวดโซลินอยด์ขาด) วาล์วจะไม่เปิดจ่ายน้ำมันเข้าปั๊ม ทำให้สตาร์ทไม่ติดหรือเร่งวูบ",
                        abnormalDiagnosisEn = "Reading 0 ohm indicates short circuit; OL or >10 ohm indicates open coil."
                    ),
                    actionTipsTh = listOf(
                        "ในรถ Isuzu D-Max (4JJ1/4JK1) และ Vigo (1KD/2KD) วาล์ว SCV มักติดขัดจากคราบเขม่า แม้ค่าความต้านทานปกติ หากล้างด้วยน้ำยาแล้วยังไม่หายควรเปลี่ยนตัวใหม่"
                    )
                ),
                DiagnosticStep(
                    stepNumber = 3,
                    titleTh = "วัดแรงดันไฟสัญญาณเซนเซอร์รางคอมมอนเรล (Rail Pressure Sensor Signal)",
                    titleEn = "Rail Pressure Sensor Signal Voltage Check",
                    stepType = DiagnosticStepType.MULTIMETER_ELECTRICAL,
                    descriptionTh = "ใช้เข็มแหลมเสียบหลังปลั๊ก (Back-probe) ที่สายสัญญาณของเซนเซอร์หัวรางคอมมอนเรล (3 พิน) ตั้งมัลติมิเตอร์ย่าน DC Voltage 20V",
                    descriptionEn = "Back-probe signal wire on 3-pin rail pressure sensor with DMM set to DC Volts.",
                    multimeterTest = MultimeterTestDetail(
                        componentName = "เซนเซอร์แรงดันรางคอมมอนเรล (Rail Pressure Sensor)",
                        harnessConnectorTh = "ปลั๊ก 3 พินท้ายราง: พิน 1 (GND), พิน 2 (Signal ออกเข้า ECU), พิน 3 (ไฟเลี้ยง 5V Ref)",
                        harnessConnectorEn = "3-pin rail sensor: Pin 1 GND, Pin 2 Signal to ECU, Pin 3 5V Ref",
                        testMode = "แรงดันไฟฟ้ากระแสตรง (DC Voltage)",
                        redProbeLocationTh = "เข็มเสียบหลังพินสัญญาณ (Signal Pin 2)",
                        blackProbeLocationTh = "กราวด์ตัวถัง หรือ ขั้วลบแบตเตอรี่",
                        testConditionTh = "บิดกุญแจ ON เครื่องยนต์ยังไม่สตาร์ท (KOEO) และขณะเดินเบา (Idle)",
                        normalRange = "KOEO: 0.95V - 1.05V (เทียบเท่า 0-5 Bar) | รอบเดินเบา: 1.30V - 1.45V (300-350 Bar)",
                        abnormalDiagnosisTh = "หากไฟตกต่ำกว่า 0.8V ขณะเดินเบา หรือค้างที่ 0V สายไฟอาจขาดหรือปั๊มสร้างแรงดันไม่พอ หากขึ้น 4.9V แสดงว่าสายกราวด์หลุด",
                        abnormalDiagnosisEn = "Below 0.8V at idle indicates low fuel pressure; 4.9V indicates broken ground."
                    ),
                    pidName = "FRP (Fuel Rail Pressure)",
                    pidExpectedValue = "300 - 350 Bar ที่เดินเบา, 1,400 - 2,000 Bar ขณะเร่งรอบเต็มที่"
                ),
                DiagnosticStep(
                    stepNumber = 4,
                    titleTh = "ทดสอบการรั่วไหลกลับของหัวฉีด (Injector Back-Leak Test)",
                    titleEn = "Injector Return Line Flow Leak-Off Test",
                    stepType = DiagnosticStepType.ACTUATOR_FUNCTIONAL_TEST,
                    descriptionTh = "ถอดสายไหลกลับน้ำมันของหัวฉีดทั้ง 4 สูบ แล้วต่อขวดตวงวัดปริมาณน้ำมันไหลกลับขณะสตาร์ทหรือเดินเบา 1 นาที",
                    descriptionEn = "Disconnect injector spill return pipes and connect graduated test tubes for 1 minute idle.",
                    actionTipsTh = listOf(
                        "ปริมาณน้ำมันไหลกลับปกติไม่ควรเกิน 15-20 มล. ต่อสูบใน 1 นาที",
                        "หากมีสูบใดสูบหนึ่งพ่นน้ำมันไหลกลับออกมาเกิน 35 มล. แสดงว่าวาล์วปลายหัวฉีดปิดไม่สนิท ทำให้แรงดันในรางรั่วทิ้งลงถัง"
                    )
                ),
                DiagnosticStep(
                    stepNumber = 5,
                    titleTh = "ลบโค้ดและวิ่งทดสอบ Drive Cycle",
                    titleEn = "Clear DTC & Drive Cycle Reset",
                    stepType = DiagnosticStepType.DRIVE_CYCLE_VERIFICATION,
                    descriptionTh = "หลังแก้ไข ให้ใช้เมนูลบโค้ด แล้วสตาร์ทเครื่องยนต์อุ่นเครื่องจนถึงอุณหภูมิปกติ (85°C - 90°C) จากนั้นเร่งคันเร่งขึ้นลงที่เกียร์ว่าง 3 ครั้ง และนำรถไปขับขี่ทดสอบช่วงความเร็ว 80 - 100 กม./ชม. พร้อมกดคันเร่งคิกดาวน์ดูว่าไฟ Check Engine กลับมาอีกหรือไม่",
                    descriptionEn = "Clear DTCs, bring engine to operating temperature (85°C+), and perform wide-open throttle acceleration runs to verify fuel rail recovery."
                )
            ),
            thaiVehicleNotesTh = "ในประเทศไทย น้ำมันดีเซลผสมไบโอดีเซล B10 และ B20 มีความชื้นและยางเหนียวสูงกว่าดีเซลยุโรป ทำให้วาล์ว SCV และกรองดักน้ำใน Toyota Vigo/Fortuner, Isuzu D-Max, Mitsubishi Triton อุดตันเร็วกว่าปกติ ควรเปลี่ยนกรองโซล่าทุก 20,000 กม.",
            postRepairDriveCycleTh = "ขับขี่คงที่ความเร็ว 90 กม./ชม. ต่อเนื่อง 10 นาที จากนั้นเหยียบคันเร่งแซง 100-120 กม./ชม. 2 รอบ หากระบบปกติ Ready Monitor สำหรับ Fuel System จะเปลี่ยนเป็น Passed"
        ),

        // ================= P0171: System Too Lean =================
        "P0171" to DtcTroubleshootingGuide(
            dtcCode = "P0171",
            titleTh = "คู่มือตรวจเช็ก: ส่วนผสมน้ำมันบางเกินไป Bank 1 (System Too Lean)",
            titleEn = "Diagnostic Guide: Air-Fuel Ratio Too Lean on Bank 1",
            severity = DtcSeverity.WARNING,
            affectedSystemTh = "ระบบจ่ายเชื้อเพลิงและไอดี (Fuel Trim & Air Intake System)",
            requiredToolsTh = listOf(
                "มัลติมิเตอร์แบบดิจิตอล (DMM)",
                "สเปรย์ล้างแอร์โฟร์ (MAF Cleaner ปลอดภัยต่อพลาสติก)",
                "สเปรย์ควันหาจุดรั่วไอดี (Smoke Machine หรือ สเปรย์น้ำสบู่/ควันธูป)",
                "ไขควงและประแจบล็อกเบอร์ 10"
            ),
            safetyPrecautionsTh = "ห้ามใช้สเปรย์คาร์บูเรเตอร์หรือน้ำยาล้างเบรกทำความสะอาดขดลวดแอร์โฟร์ (MAF) เพราะสารเคมีที่รุนแรงจะกัดสารเคลือบเซรามิกจนเซนเซอร์พังเสียหาย",
            steps = listOf(
                DiagnosticStep(
                    stepNumber = 1,
                    titleTh = "ตรวจรอยรั่วท่อไอดีและท่อลมแวคคั่ม (Vacuum Leak Inspection)",
                    titleEn = "Intake Boot & Vacuum Line Leak Inspection",
                    stepType = DiagnosticStepType.VISUAL_MECHANICAL,
                    descriptionTh = "ตรวจท่อยางไอดีขนาดใหญ่หลังกรองอากาศไปยังลิ้นปีกผีเสื้อ รอยพับมักฉีกขาดที่มองไม่เห็น ตรวจท่อลมหายใจฝาวาล์ว (PCV) และท่อลมหม้อลมเบรก",
                    descriptionEn = "Inspect rubber intake boot accordion ribs for splits. Check PCV breather hoses and brake booster vacuum lines.",
                    actionTipsTh = listOf(
                        "ในรถที่ติดแก๊ส LPG/CNG ให้ตรวจเช็กท่อเจาะฝังแก๊สที่คอไอดีว่าโอริงเสื่อมหรือหลุดหรือไม่",
                        "ขณะเครื่องเดินเบา ให้ลองขยับท่อยางไอดี หากรอบเครื่องสะดุดเปลี่ยนแสดงว่ามีรอยรั่วดูดอากาศนอกระบบ"
                    )
                ),
                DiagnosticStep(
                    stepNumber = 2,
                    titleTh = "วัดค่าแรงดันไฟสัญญาณแอร์โฟร์ MAF (MAF Sensor Signal Check)",
                    titleEn = "MAF Sensor Signal Voltage Measurement",
                    stepType = DiagnosticStepType.MULTIMETER_ELECTRICAL,
                    descriptionTh = "ตรวจสายสัญญาณเซนเซอร์วัดมวลอากาศ (MAF 5 พิน) เพื่อดูว่ารายงานปริมาณอากาศต่ำกว่าความเป็นจริงหรือไม่",
                    descriptionEn = "Measure MAF sensor output signal voltage at idle and 2500 RPM.",
                    multimeterTest = MultimeterTestDetail(
                        componentName = "แอร์โฟร์เซนเซอร์ (Mass Air Flow Sensor)",
                        harnessConnectorTh = "ปลั๊ก 5 พิน: สายไฟเลี้ยง 12V, กราวด์, สัญญาณ MAF (Out), สัญญาณ IAT (Temp)",
                        harnessConnectorEn = "5-pin MAF plug: 12V B+, Ground, MAF Signal, IAT Signal",
                        testMode = "แรงดันไฟฟ้ากระแสตรง (DC Voltage)",
                        redProbeLocationTh = "พินสัญญาณ MAF (มักเป็นสายสีเขียวหรือเหลืองคาดดำ)",
                        blackProbeLocationTh = "ขั้วลบแบตเตอรี่ หรือสายกราวด์เซนเซอร์",
                        testConditionTh = "เครื่องยนต์อุณหภูมิทำงานปกติ (85°C) สตาร์ทเดินเบา (Idle) ปิดแอร์",
                        normalRange = "เดินเบา: 1.1V - 1.4V (ประมาณ 1.8 - 2.5 g/s) | เร่ง 2500 RPM: 1.7V - 2.1V",
                        abnormalDiagnosisTh = "หากเดินเบาต่ำกว่า 0.9V แสดงว่าขดลวดฮอทไวร์สกปรกด้วยคราบฝุ่นน้ำมันเครื่อง ทำให้กล่อง ECU คิดว่าอากาศเข้าน้อย จึงจ่ายน้ำมันบางจนเกิด P0171",
                        abnormalDiagnosisEn = "Below 0.9V at warm idle indicates dirty hot-wire element causing ECU under-fueling."
                    )
                ),
                DiagnosticStep(
                    stepNumber = 3,
                    titleTh = "ตรวจสอบค่า Short-Term & Long-Term Fuel Trim (STFT / LTFT)",
                    titleEn = "Fuel Trim Live Parameter Evaluation",
                    stepType = DiagnosticStepType.LIVE_PID_TELEMETRY,
                    descriptionTh = "เปิดหน้า Live Telemetry ตรวจสอบค่าการชดเชยการจ่ายน้ำมันของ ECU ในหน่วยเปอร์เซ็นต์ (%)",
                    descriptionEn = "Observe STFT and LTFT values on OBD telemetry stream.",
                    pidName = "STFT & LTFT",
                    pidExpectedValue = "-5% ถึง +5% (หากเกิน +15% ถือว่าชดเชยหนาผิดปกติ)",
                    actionTipsTh = listOf(
                        "หากค่า LTFT สูง (+20%) ที่รอบเดินเบา แต่พอกดคันเร่ง 2500 RPM แล้วค่าลดลงเหลือ +5% แสดงว่าสาเหตุเกิดจาก 'ท่อลมรั่ว (Vacuum Leak)'",
                        "หากค่า LTFT สูงทั้งรอบเดินเบาและรอบสูง แสดงว่าสาเหตุเกิดจาก 'แรงดันปั๊มติ๊กตก หรือ หัวฉีดตัน'"
                    )
                ),
                DiagnosticStep(
                    stepNumber = 4,
                    titleTh = "วัดค่าความต้านทานฮีตเตอร์ออกซิเจนเซนเซอร์ (O2 Sensor Heater Test)",
                    titleEn = "Upstream O2 Sensor Heater Resistance Check",
                    stepType = DiagnosticStepType.MULTIMETER_ELECTRICAL,
                    descriptionTh = "ปลดปลั๊ก O2 Sensor ตัวหน้า (Upstream Bank 1 Sensor 1) วัดความต้านทานขดลวดทำความร้อนเพื่อดูว่าขาดหรือไม่",
                    descriptionEn = "Measure heater circuit resistance across the two matching black/white wires.",
                    multimeterTest = MultimeterTestDetail(
                        componentName = "ออกซิเจนเซนเซอร์ตัวหน้า (Air-Fuel / O2 Sensor B1S1)",
                        harnessConnectorTh = "ปลั๊ก 4 พิน: ขั้วฮีตเตอร์ 2 พิน (สายสีดำหรือขาวคู่)",
                        harnessConnectorEn = "4-pin connector: Two heater terminals (usually matching colored wires)",
                        testMode = "ความต้านทานไฟฟ้า (Resistance Ω)",
                        redProbeLocationTh = "ขั้วฮีตเตอร์พิน A",
                        blackProbeLocationTh = "ขั้วฮีตเตอร์พิน B",
                        testConditionTh = "ดับเครื่อง บิดกุญแจ OFF ปลดปลั๊กวัดที่ตัวเซนเซอร์ (ขณะเซนเซอร์เย็น)",
                        normalRange = "11.0 Ω - 16.5 Ω (Denso / Toyota) หรือ 2.5 Ω - 4.5 Ω (A/F Sensor รุ่นใหม่)",
                        abnormalDiagnosisTh = "หากวัดได้ค่าอนันต์ (OL / ขาด) ฮีตเตอร์ไม่ทำงาน ทำให้เซนเซอร์ไม่ร้อนพอที่จะอ่านค่าไอเสียที่ถูกต้อง กล่องจะค้างสัญญาณว่าส่วนผสมบาง",
                        abnormalDiagnosisEn = "Reading OL indicates burned out heater circuit preventing proper closed-loop fuel metering."
                    )
                ),
                DiagnosticStep(
                    stepNumber = 5,
                    titleTh = "ล้างแอร์โฟร์ ลบโค้ด และทดสอบขับขี่",
                    titleEn = "Clean MAF, Reset Adaptations & Road Test",
                    stepType = DiagnosticStepType.DRIVE_CYCLE_VERIFICATION,
                    descriptionTh = "ฉีดสเปรย์ล้าง MAF ทิ้งให้แห้งสนิท 15 นาที ติดตั้งกลับ ลบโค้ดในแอป และสตาร์ทรถเดินเบา 10 นาทีเพื่อให้ ECU เรียนรู้ค่าใหม่ (Idle Re-learn)",
                    descriptionEn = "Clean MAF element, reinstall, clear codes, and let vehicle idle 10 minutes for adaptive fuel trim reset."
                )
            ),
            thaiVehicleNotesTh = "รถยอดนิยมในไทย เช่น Toyota Vios, Yaris, Altis, Honda City, Civic หากใช้กรองอากาศแต่งราคาถูกหรือไม่ได้เปลี่ยนกรองตามระยะ ละอองฝุ่นจะเกาะขดลวด MAF จนขึ้น P0171 บ่อยมาก การฉีดล้าง MAF และเปลี่ยนกรองอากาศแท้มักแก้ปัญหาได้ทันทีโดยไม่ต้องเปลี่ยนอะไหล่แพง",
            postRepairDriveCycleTh = "ขับขี่ในเมืองมีหยุดนิ่งและเร่งออกตัว 15 นาที ค่า LTFT ควรลดลงมาอยู่ในช่วง -5% ถึง +7%"
        ),

        // ================= P0300: Random / Multiple Cylinder Misfire =================
        "P0300" to DtcTroubleshootingGuide(
            dtcCode = "P0300",
            titleTh = "คู่มือตรวจเช็ก: การจุดระเบิดผิดพลาดหลายสูบพร้อมกัน (Random Misfire)",
            titleEn = "Diagnostic Guide: Random / Multiple Cylinder Misfire Detected",
            severity = DtcSeverity.CRITICAL,
            affectedSystemTh = "ระบบจุดระเบิด กำลังอัด และจ่ายเชื้อเพลิง (Ignition & Combustion)",
            requiredToolsTh = listOf(
                "บล็อกขันหัวเทียน 14 หรือ 16 mm พร้อมด้ามต่อ",
                "ฟิลเลอร์เกจ (Feeler Gauge) สำหรับวัดระยะห่างเขี้ยวหัวเทียน",
                "มัลติมิเตอร์สำหรับวัดความต้านทานคอยล์จุดระเบิด (Ignition Coil)",
                "เกจวัดกำลังอัดกระบอกสูบ (Compression Tester 0-300 PSI)"
            ),
            safetyPrecautionsTh = "ขณะตรวจสอบประกายไฟหัวเทียน ห้ามจับปลั๊กคอยล์ด้วยมือเปล่าเพราะมีแรงดันไฟสูงถึง 25,000 - 40,000 โวลต์ อาจถูกไฟดูดอย่างรุนแรง",
            steps = listOf(
                DiagnosticStep(
                    stepNumber = 1,
                    titleTh = "ถอดตรวจสภาพหัวเทียนทุกสูบ (Spark Plug Inspection)",
                    titleEn = "Remove & Inspect All Spark Plugs",
                    stepType = DiagnosticStepType.VISUAL_MECHANICAL,
                    descriptionTh = "ถอดหัวเทียนทั้ง 4 หรือ 6 สูบออกมาเรียงตามลำดับ ตรวจดูคราบที่เขี้ยวหัวเทียน (คราบน้ำมันเครื่อง, คราบเขม่าดำเปียก, หรือเขี้ยวสึกกุด)",
                    descriptionEn = "Inspect spark plug tips for oil fouling, carbon bridging, fuel wetness, or widened electrode gap.",
                    actionTipsTh = listOf(
                        "เขี้ยวแห้งสีน้ำตาลอ่อนแกมเทา = เผาไหม้สมบูรณ์",
                        "เขม่าดำแห้ง = ส่วนผสมหนา ขาดอากาศ หรือไฟอ่อน",
                        "คราบน้ำมันเครื่องเปียก = ยางหมวกวาล์วหรือแหวนลูกสูบรั่ว"
                    )
                ),
                DiagnosticStep(
                    stepNumber = 2,
                    titleTh = "วัดไฟเลี้ยงและสัญญาณสั่งยิงคอยล์จุดระเบิด (Ignition Coil Power & Trigger)",
                    titleEn = "Ignition Coil Power, Ground & Trigger Signal Test",
                    stepType = DiagnosticStepType.MULTIMETER_ELECTRICAL,
                    descriptionTh = "วัดไฟที่ปลั๊กคอยล์จุดระเบิด (แบบ 3 พิน หรือ 4 พิน) เพื่อยืนยันว่ามีไฟบวก 12V และกราวด์มาครบถ้วน",
                    descriptionEn = "Back-probe coil connector to verify 12V B+, clean ground, and 5V trigger pulse from ECM.",
                    multimeterTest = MultimeterTestDetail(
                        componentName = "คอยล์จุดระเบิดแยกสูบ (Direct Ignition Coil COP)",
                        harnessConnectorTh = "ปลั๊ก 4 พิน: พิน 1 (ไฟบวก 12V B+), พิน 2 (IGF สัญญาณยืนยัน), พิน 3 (IGT สัญญาณจุดระเบิด 5V), พิน 4 (GND)",
                        harnessConnectorEn = "4-pin COP harness: 12V B+, IGF feedback, IGT trigger, Ground",
                        testMode = "แรงดันไฟฟ้ากระแสตรง (DC Voltage)",
                        redProbeLocationTh = "พิน 1 (สายไฟเลี้ยง 12V จากสวิตช์กุญแจ)",
                        blackProbeLocationTh = "พิน 4 (สายกราวด์คอยล์ หรือตัวถังเครื่อง)",
                        testConditionTh = "บิดกุญแจ ON เครื่องยนต์ยังไม่สตาร์ท (KOEO)",
                        normalRange = "11.8V - 12.6V (เท่ากับแรงดันแบตเตอรี่)",
                        abnormalDiagnosisTh = "หากวัดได้ 0V แสดงว่าฟิวส์ระบบจุดระเบิด (IGN Fuse) ขาด หรือรีเลย์เมนไม่ทำงาน หากไฟมาไม่สม่ำเสมอสายไฟเมนอาจขาดใน",
                        abnormalDiagnosisEn = "0V indicates blown ignition fuse or faulty main relay."
                    )
                ),
                DiagnosticStep(
                    stepNumber = 3,
                    titleTh = "สลับตำแหน่งคอยล์จุดระเบิดเพื่อระบุสูบที่มีปัญหา (Coil Swap Test)",
                    titleEn = "Swap Ignition Coils to Confirm Failing Unit",
                    stepType = DiagnosticStepType.ACTUATOR_FUNCTIONAL_TEST,
                    descriptionTh = "หากสังเกตว่ามีสูบหนึ่งสะดุดเป็นพิเศษ ให้สลับคอยล์ของสูบนั้น (เช่น สูบ 1) ไปใส่สูบข้างเคียง (เช่น สูบ 2) แล้วดูว่าอาการสะดุดย้ายตามคอยล์ไปสูบ 2 หรือไม่",
                    descriptionEn = "Swap suspect coil to adjacent cylinder and observe if misfire DTC tracks the moved coil.",
                    actionTipsTh = listOf(
                        "หากโค้ดเปลี่ยนจาก P0301 ไปเป็น P0302 ยืนยันได้ 100% ว่าคอยล์ตัวนั้นรั่วหรือเสื่อม ต้องเปลี่ยนคอยล์ตัวใหม่"
                    )
                ),
                DiagnosticStep(
                    stepNumber = 4,
                    titleTh = "วัดกำลังอัดกระบอกสูบ (Engine Cylinder Compression Test)",
                    titleEn = "Mechanical Cylinder Compression Test",
                    stepType = DiagnosticStepType.VISUAL_MECHANICAL,
                    descriptionTh = "ถอดหัวเทียนทุกสูบ ปลดปลั๊กปั๊มติ๊กหรือถอดฟิวส์หัวฉีด ต่อเกจวัดกำลังอัดเข้าช่องหัวเทียน เหยียบคันเร่งสุดแล้วบิดสตาร์ทเครื่องยนต์ 4-5 วินาที",
                    descriptionEn = "Screw in compression tester gauge, depress accelerator to wide open, crank engine for 4-5 compression strokes.",
                    actionTipsTh = listOf(
                        "เครื่องยนต์เบนซินทั่วไปควรอ่านได้ 160 - 210 PSI (ความแตกต่างระหว่างสูบไม่ควรเกิน 10%)",
                        "หากมีสูบใดได้ต่ำกว่า 120 PSI หรือได้ 0 PSI แสดงว่าวาล์วยัน/วาล์วรั่ว หรือประเก็นฝาสูบแตกทะลุระหว่างสูบ"
                    )
                ),
                DiagnosticStep(
                    stepNumber = 5,
                    titleTh = "ลบโค้ดและวิ่งรอบเพื่อบันทึก Misfire Counter",
                    titleEn = "Reset DTC & Check Misfire PID Counter",
                    stepType = DiagnosticStepType.DRIVE_CYCLE_VERIFICATION,
                    descriptionTh = "ลบรหัสข้อผิดพลาด สตาร์ทรถและสังเกตค่า Misfire Cylinder Count บนแอป ให้เป็น 0 ครั้งต่อเนื่องทุกช่วงความเร็ว",
                    descriptionEn = "Clear fault codes and monitor live misfire counters across all cylinders during idle and cruise."
                )
            ),
            thaiVehicleNotesTh = "ในเครื่องยนต์ติดแก๊ส LPG เช่น Toyota Altis, Vios, Honda City การใช้งานระยะยาวทำให้บ่าวาล์วไอเสียทรุด (วาล์วยัน) จนกำลังอัดรั่วในรอบเดินเบา ทำให้เกิด P0300 บ่อยมาก หากเปลี่ยนหัวเทียนและคอยล์แล้วไม่หาย ควรนำรถไปเช็กและตั้งระยะห่างวาล์ว (Shim valve clearance)",
            postRepairDriveCycleTh = "สตาร์ทเครื่องเดินเบา 5 นาที เร่งเครื่อง 3000 RPM ค้าง 1 นาที และทดสอบเร่งขึ้นสะพานชัน เพื่อทดสอบว่าไม่มีอาการสะดุดเมื่อเครื่องรับโหลดหนัก"
        ),

        // ================= P0420: Catalyst System Efficiency Below Threshold =================
        "P0420" to DtcTroubleshootingGuide(
            dtcCode = "P0420",
            titleTh = "คู่มือตรวจเช็ก: ประสิทธิภาพหม้อกรองไอเสียแคตาไลติกต่ำกว่าเกณฑ์ (Catalyst Efficiency)",
            titleEn = "Diagnostic Guide: Catalyst System Efficiency Below Threshold (Bank 1)",
            severity = DtcSeverity.WARNING,
            affectedSystemTh = "ระบบบำบัดมลพิษไอเสีย (Exhaust & Emissions)",
            requiredToolsTh = listOf(
                "มัลติมิเตอร์ หรือสแกนเนอร์ดูกราฟแรงดัน O2 Sensor สด",
                "กล้องส่องตรวจภายในท่อไอเสีย (Borescope Inspection Camera)",
                "ปืนวัดอุณหภูมิอินฟราเรด (Infrared Thermometer Gun)"
            ),
            safetyPrecautionsTh = "ท่อร่วมไอเสียและหม้อแคทมีอุณหภูมิสูงกว่า 400°C - 600°C ขณะใช้งาน ห้ามสัมผัสโดยเด็ดขาดก่อนปล่อยให้เครื่องยนต์เย็นลง",
            steps = listOf(
                DiagnosticStep(
                    stepNumber = 1,
                    titleTh = "ตรวจรอยรั่วท่อไอเสียก่อนถึงหม้อแคท (Exhaust Leak Check)",
                    titleEn = "Inspect Upstream Exhaust Gasket & Flex Pipe",
                    stepType = DiagnosticStepType.VISUAL_MECHANICAL,
                    descriptionTh = "ตรวจหารอยแตกร้าวที่เฮดเดอร์ ประเก็นคอท่อไอเสีย และท่ออ่อน (Flex pipe) อากาศภายนอกที่ถูกดูดเข้าไปจากรอยรั่วจะหลอกให้เซนเซอร์ตัวล่างจับค่าอ็อกซิเจนสูงขึ้น",
                    descriptionEn = "Inspect exhaust manifold cracks, rusted flange bolts, and punctured flex pipe weaves."
                ),
                DiagnosticStep(
                    stepNumber = 2,
                    titleTh = "วัดเปรียบเทียบรูปคลื่น O2 Sensor ตัวหน้า vs ตัวหลัง (O2 Downstream Waveform Test)",
                    titleEn = "Upstream vs Downstream O2 Sensor Switching Comparison",
                    stepType = DiagnosticStepType.LIVE_PID_TELEMETRY,
                    descriptionTh = "ต่อแอป OBD ดูค่าแรงดันไฟของ O2S1 (ตัวหน้า) และ O2S2 (ตัวหลัง) ขณะขับขี่ความเร็วคงที่ 80 กม./ชม.",
                    descriptionEn = "Compare voltage fluctuation of upstream sensor vs downstream sensor at 2500 RPM cruise.",
                    pidName = "O2S11 (Front) & O2S12 (Rear)",
                    pidExpectedValue = "ตัวหน้าแกว่งสลับ 0.1V - 0.9V รวดเร็ว | ตัวหลังนิ่งคงที่ 0.5V - 0.7V",
                    actionTipsTh = listOf(
                        "หากตัวหลังแกว่งสลับขึ้นลง 0.1V ถึง 0.9V ตามตัวหน้าอย่างรวดเร็ว แสดงว่าไส้เซรามิกในหม้อแคทหมดสภาพ หรือไส้แตกละลายหายไปแล้ว",
                        "หากตัวหลังนิ่งเรียบที่ประมาณ 0.6V แสดงว่าแคททำงานดักก๊าซได้ดี โค้ดอาจมาจากเซนเซอร์ตัวหลังเพี้ยนเอง"
                    )
                ),
                DiagnosticStep(
                    stepNumber = 3,
                    titleTh = "วัดอุณหภูมิทางเข้าและทางออกหม้อแคทด้วยปืนอินฟราเรด (Cat Temperature Delta Test)",
                    titleEn = "Catalytic Converter Inlet vs Outlet Temperature Test",
                    stepType = DiagnosticStepType.VISUAL_MECHANICAL,
                    descriptionTh = "ใช้ปืนวัดอุณหภูมิอินฟราเรด ยิงวัดที่ผิวท่อไอเสียก่อนเข้าหม้อแคท และหลังออกจากหม้อแคท",
                    descriptionEn = "Measure surface temperature of catalytic converter inlet cone versus outlet cone.",
                    actionTipsTh = listOf(
                        "หม้อแคทที่ทำงานปกติ ปฏิกิริยาเคมีจะคายความร้อน ทำให้ทางออก (Outlet) ร้อนกว่าทางเข้า (Inlet) อย่างน้อย 30°C - 50°C",
                        "หากทางออกเย็นกว่าหรือเท่ากับทางเข้า แสดงว่าสารเคลือบโลหะมีค่า (Platinum/Rhodium) หมดประสิทธิภาพแล้ว"
                    )
                ),
                DiagnosticStep(
                    stepNumber = 4,
                    titleTh = "ลบโค้ดและทดสอบขับขี่ Catalyst Monitor Drive Cycle",
                    titleEn = "Reset DTC & Complete Catalyst Readiness Cycle",
                    stepType = DiagnosticStepType.DRIVE_CYCLE_VERIFICATION,
                    descriptionTh = "ลบรหัสข้อผิดพลาด ขับขี่บนทางหลวงด้วยความเร็ว 70-90 กม./ชม. คงที่เป็นเวลา 15-20 นาที โดยไม่กดคิกดาวน์รุนแรง แล้วปล่อยไหลชะลอความเร็วเพื่อให้กล่องรัน Catalyst Monitor",
                    descriptionEn = "Perform 15-minute steady cruise at 80 km/h followed by decel fuel cut-off coasting."
                )
            ),
            thaiVehicleNotesTh = "ในตลาดรถมือสองของไทย รถหลายคันถูกถอดไส้แคทไปขายหรือใส่ท่อตรง (Catless Downpipe) หากรถของคุณถูกทะลวงแคทออก กล่อง ECU จะฟ้อง P0420 เสมอ ทางแก้ที่ถูกกฎหมายคือเปลี่ยนหม้อแคท OEM หรือแคทไฮโฟลว์ที่ได้มาตรฐาน",
            postRepairDriveCycleTh = "วิ่งทางไกล 50 กม. หากค่า Catalyst Monitor เปลี่ยนเป็น 'Ready' และไม่มีไฟโชว์ ถือว่าผ่านเกณฑ์ตรวจสภาพรถ (ตรอ.)"
        ),

        // ================= P0101: Mass Air Flow (MAF) Circuit Range/Performance =================
        "P0101" to DtcTroubleshootingGuide(
            dtcCode = "P0101",
            titleTh = "คู่มือตรวจเช็ก: สัญญาณแอร์โฟร์เซนเซอร์อยู่นอกช่วงมาตรฐาน (MAF Range/Performance)",
            titleEn = "Diagnostic Guide: Mass Air Flow Sensor Circuit Range / Performance",
            severity = DtcSeverity.WARNING,
            affectedSystemTh = "ระบบวัดปริมาณอากาศไอดี (Air Intake Measurement)",
            requiredToolsTh = listOf("มัลติมิเตอร์แบบดิจิตอล", "สเปรย์ล้างเซนเซอร์แอร์โฟร์ (CRC MAF Cleaner)", "ประแจปากตายเบอร์ 8 และ 10"),
            safetyPrecautionsTh = "ห้ามใช้ลมแรงจากสายเป่าลมโรงงานเป่าอัดใส่ขดลวดแอร์โฟร์เด็ดขาด เพราะลวดไมโครไฟเบอร์จะขาดทันที",
            steps = listOf(
                DiagnosticStep(
                    stepNumber = 1,
                    titleTh = "ตรวจสอบความสะอาดของกรองอากาศและท่อไอดี",
                    titleEn = "Air Filter & Duct Integrity Check",
                    stepType = DiagnosticStepType.VISUAL_MECHANICAL,
                    descriptionTh = "เปิดหม้อกรองอากาศดูว่าแผ่นกรองอุดตัน มีใบไม้ หรือมีคราบน้ำมันเครื่องดูดเข้าไปเกาะหรือไม่",
                    descriptionEn = "Check air cleaner box for leaves, rodent debris, or engine oil blow-by."
                ),
                DiagnosticStep(
                    stepNumber = 2,
                    titleTh = "วัดไฟเลี้ยงและกราวด์ที่ปลั๊กเซนเซอร์ MAF",
                    titleEn = "MAF Harness Supply & Reference Voltage Check",
                    stepType = DiagnosticStepType.MULTIMETER_ELECTRICAL,
                    descriptionTh = "ปลดปลั๊กแอร์โฟร์ วัดแรงดันไฟเลี้ยง 12V และไฟเลี้ยงวงจรอิเล็กทรอนิกส์ 5V",
                    descriptionEn = "Measure 12V B+ battery feed, 5V reference wire, and chassis ground continuity.",
                    multimeterTest = MultimeterTestDetail(
                        componentName = "เซนเซอร์แอร์โฟร์ (MAF)",
                        harnessConnectorTh = "ปลั๊กแอร์โฟร์ 5 พิน",
                        harnessConnectorEn = "5-pin MAF connector",
                        testMode = "แรงดันไฟ DC Voltage & ความต่อเนื่อง Ground",
                        redProbeLocationTh = "พินไฟเลี้ยง 12V B+ และพิน 5V Ref",
                        blackProbeLocationTh = "พินกราวด์ (ความต้านทานลงตัวถังต้องน้อยกว่า 0.5 Ω)",
                        testConditionTh = "บิดกุญแจ ON เครื่องยังไม่สตาร์ท (KOEO)",
                        normalRange = "พินไฟเมน: 12.0V - 12.6V | พิน Ref: 4.95V - 5.05V",
                        abnormalDiagnosisTh = "หากไฟเลี้ยง 12V หายไป ให้ตรวจเช็กฟิวส์ EFI หรือฟิวส์ ECU ในกล่องฟิวส์หน้ารถ",
                        abnormalDiagnosisEn = "Missing 12V suggests open EFI fuse or corroded relay contact."
                    )
                ),
                DiagnosticStep(
                    stepNumber = 3,
                    titleTh = "ล้างทำความสะอาดเซนเซอร์แอร์โฟร์และลบโค้ด",
                    titleEn = "Spray Clean MAF Sensor & Clear DTC",
                    stepType = DiagnosticStepType.DRIVE_CYCLE_VERIFICATION,
                    descriptionTh = "ถอดน็อต 2 ตัวดึงตัวเซนเซอร์ออกมา ฉีดสเปรย์ล้าง MAF บริเวณขดลวดด้านใน 10-15 วินาที ทิ้งให้แห้งสนิท ติดตั้งกลับ ลบโค้ดและทดสอบขับขี่",
                    descriptionEn = "Remove sensor, spray clean internal hot wire and platinum sensing element, reassemble and clear codes."
                )
            ),
            thaiVehicleNotesTh = "ในเครื่องยนต์ดีเซลคอมมอนเรลของ Isuzu D-Max และ Toyota Revo แอร์โฟร์ที่จับเขม่าคราบน้ำมันเครื่องจากท่อหายใจฝาวาล์วจะทำให้เครื่องตอบสนองคันเร่งช้าและกินน้ำมัน",
            postRepairDriveCycleTh = "ขับขี่ทดสอบ 10 นาที อัตราเร่งและรอบเดินเบาจะกลับมาตอบสนองไวขึ้น"
        )
    )

    fun getGuideForDtc(code: String): DtcTroubleshootingGuide? {
        val cleanCode = code.trim().uppercase()
        return guides[cleanCode] ?: generateGenericGuide(cleanCode)
    }

    private fun generateGenericGuide(code: String): DtcTroubleshootingGuide {
        return DtcTroubleshootingGuide(
            dtcCode = code,
            titleTh = "คู่มือตรวจเช็กและซ่อมบำรุงรหัส $code",
            titleEn = "Diagnostic & Repair Procedure for $code",
            severity = if (code.startsWith("P03") || code.startsWith("P00") || code.startsWith("P02")) DtcSeverity.CRITICAL else DtcSeverity.WARNING,
            affectedSystemTh = when {
                code.startsWith("P0") || code.startsWith("P1") || code.startsWith("P2") -> "ระบบเครื่องยนต์และระบบส่งกำลัง (Powertrain)"
                code.startsWith("C0") || code.startsWith("C1") -> "ระบบเบรก ABS และแชสซีส์ (Chassis)"
                code.startsWith("B0") || code.startsWith("B1") -> "ระบบตัวถังและถุงลมนิรภัย (Body & SRS)"
                else -> "ระบบเครือข่ายสื่อสาร CAN-Bus (Network)"
            },
            requiredToolsTh = listOf(
                "มัลติมิเตอร์แบบดิจิตอล (DMM)",
                "ชุดประแจและบล็อกเบอร์มาตรฐาน (8, 10, 12, 14 mm)",
                "น้ำยาล้างหน้าสัมผัสไฟฟ้า (Electronic Contact Cleaner)",
                "สแกนเนอร์ OBD-II สำหรับตรวจสอบ Freeze Frame"
            ),
            safetyPrecautionsTh = "ดับเครื่องยนต์และถอดขั้วลบแบตเตอรี่ออกทุกครั้งก่อนถอดเปลี่ยนอุปกรณ์ไฟฟ้าเพื่อป้องกันการลัดวงจร",
            steps = listOf(
                DiagnosticStep(
                    stepNumber = 1,
                    titleTh = "ตรวจสอบทางกายภาพ สายไฟ และขั้วต่อ (Visual Inspection)",
                    titleEn = "Harness & Connector Physical Inspection",
                    stepType = DiagnosticStepType.VISUAL_MECHANICAL,
                    descriptionTh = "ตรวจดูสายไฟและปลั๊กขั้วต่อของเซนเซอร์ที่เกี่ยวข้อง ตรวจหารอยหนูกัดสายไฟ สายไฟเสียดสีกับตัวถัง หรือปลั๊กหลวม/มีคราบขี้เกลือ",
                    descriptionEn = "Inspect wiring harness for rodent damage, pin corrosion, loose clips, or burnt insulation."
                ),
                DiagnosticStep(
                    stepNumber = 2,
                    titleTh = "วัดไฟเลี้ยงและสายกราวด์ของวงจร (Power & Ground Multimeter Test)",
                    titleEn = "Power Feed & Ground Circuit Verification",
                    stepType = DiagnosticStepType.MULTIMETER_ELECTRICAL,
                    descriptionTh = "ใช้มัลติมิเตอร์วัดไฟเลี้ยง (12V หรือ 5V) และตรวจสอบว่าสายกราวด์มีความต่อเนื่องลงตัวถังสมบูรณ์ (ความต้านทาน < 1.0 Ω)",
                    descriptionEn = "Verify 12V/5V reference voltage and ensure low resistance to chassis ground (< 1.0 ohm).",
                    multimeterTest = MultimeterTestDetail(
                        componentName = "วงจรเซนเซอร์ที่เกี่ยวข้องกับ $code",
                        harnessConnectorTh = "ปลั๊กขั้วต่อของเซนเซอร์หรือโซลินอยด์",
                        harnessConnectorEn = "Sensor harness pin connector",
                        testMode = "แรงดันไฟ DC Voltage & ความต้านทาน Ground (Ω)",
                        redProbeLocationTh = "พินไฟเลี้ยงจากกล่อง ECU หรือฟิวส์",
                        blackProbeLocationTh = "กราวด์ตัวถังรถ",
                        testConditionTh = "บิดสวิตช์กุญแจตำแหน่ง ON (KOEO)",
                        normalRange = "ไฟเลี้ยง: 12.0V - 12.6V หรือ 4.9V - 5.1V | กราวด์: < 0.5 Ω",
                        abnormalDiagnosisTh = "หากไม่มีไฟเลี้ยง ให้ตรวจสอบฟิวส์ที่เกี่ยวข้องและสายไฟเมน หากกราวด์ลอยให้ทำความสะอาดจุดต่อกราวด์ตัวถัง",
                        abnormalDiagnosisEn = "Open ground or missing reference voltage causes persistent fault flags."
                    )
                ),
                DiagnosticStep(
                    stepNumber = 3,
                    titleTh = "ลบโค้ดและทดสอบขับขี่ (Clear DTC & Verify)",
                    titleEn = "Clear Code & Road Test Verification",
                    stepType = DiagnosticStepType.DRIVE_CYCLE_VERIFICATION,
                    descriptionTh = "ลบรหัสข้อผิดพลาดผ่านแอป สตาร์ทเครื่องยนต์และทดสอบขับขี่ตามเงื่อนไขที่ตรวจพบรหัส เพื่อยืนยันว่าปัญหาได้รับการแก้ไขแล้ว",
                    descriptionEn = "Clear codes, perform relevant drive cycle, and confirm that the DTC does not return."
                )
            ),
            thaiVehicleNotesTh = "ควรตรวจสอบประวัติการเปลี่ยนถ่ายของเหลวและอะไหล่สิ้นเปลืองตามคู่มือศูนย์บริการในประเทศไทย",
            postRepairDriveCycleTh = "ขับขี่ทดสอบ 15-20 นาที ให้เครื่องยนต์เข้าสู่ Closed Loop เพื่อให้ระบบประเมินค่าเซนเซอร์ใหม่"
        )
    }
}
