package com.example.data.repository

import com.example.data.model.DiagnosticSystem
import com.example.data.model.DtcCategory
import com.example.data.model.DtcCode
import com.example.data.model.DtcSeverity

object DtcDatabase {

    val allCodes: List<DtcCode> = listOf(
        // ================= POWERTRAIN / ENGINE =================
        DtcCode(
            code = "P0171",
            category = DtcCategory.POWERTRAIN,
            enTitle = "System Too Lean (Bank 1)",
            thTitle = "ส่วนผสมน้ำมันกับอากาศบางเกินไป (Bank 1)",
            enDescription = "The Engine Control Module (ECM) detects that the air-fuel mixture is too lean (too much air or not enough fuel).",
            thDescription = "กล่องควบคุมเครื่องยนต์ตรวจพบว่าส่วนผสมน้ำมันและอากาศบางเกินไป (อากาศมากเกินไป หรือน้ำมันจ่ายไม่พอ)",
            severity = DtcSeverity.WARNING,
            commonCausesTh = listOf(
                "แอร์โฟร์เซนเซอร์ (MAF Sensor) สกปรกหรือเสื่อม",
                "มีรอยรั่วที่ท่อไอดี หรือท่อลมแวคคั่มแตก",
                "ปั๊มเชื้อเพลิงหรือกรองเบนซิน/ดีเซลอุดตัน แรงดันตก",
                "หัวฉีดอุดตัน ฉีดจ่ายน้ำมันได้ไม่เต็มที่",
                "ออกซิเจนเซนเซอร์ (O2 Sensor) เสื่อมสภาพ"
            ),
            commonCausesEn = listOf(
                "Dirty or faulty Mass Air Flow (MAF) sensor",
                "Vacuum leak or cracked intake hose",
                "Clogged fuel filter or weak fuel pump",
                "Clogged or dirty fuel injectors",
                "Faulty Upstream Oxygen Sensor (O2)"
            ),
            symptomsTh = listOf(
                "ไฟรูปเครื่องยนต์ (Check Engine) โชว์ที่หน้าปัด",
                "เครื่องยนต์สะดุด รอบเดินเบาไม่นิ่ง",
                "อัตราเร่งอืดลง สิ้นเปลืองน้ำมันมากขึ้น",
                "สตาร์ทติดยากตอนเช้า"
            ),
            symptomsEn = listOf(
                "Check Engine Light (MIL) illuminated",
                "Rough engine idle and hesitation",
                "Loss of power and reduced fuel economy",
                "Hard starting when cold"
            ),
            estimatedCostThb = "฿500 - ฿3,500",
            diyDifficulty = "ปานกลาง (Medium DIY)",
            affectedSystem = "ระบบจ่ายเชื้อเพลิงและไอดี (Fuel & Air Intake)"
        ),
        DtcCode(
            code = "P0087",
            category = DtcCategory.POWERTRAIN,
            enTitle = "Fuel Rail / System Pressure - Too Low",
            thTitle = "แรงดันในรางคอมมอนเรลต่ำเกินไป",
            enDescription = "Fuel rail pressure is below the minimum calibrated threshold for the common-rail diesel / direct injection system.",
            thDescription = "แรงดันน้ำมันในรางคอมมอนเรลต่ำกว่าค่ามาตรฐาน เครื่องยนต์ดีเซลคอมมอนเรลเร่งไม่ขึ้นหรือดับวูบ",
            severity = DtcSeverity.CRITICAL,
            commonCausesTh = listOf(
                "วาล์ว SCV (Suction Control Valve) ติดขัด/ค้าง (พบบ่อยมากใน D-Max, Vigo, Triton, Revo)",
                "กรองดักน้ำ / กรองโซล่าตัน สกปรก",
                "ปั๊มเชื้อเพลิงแรงดันสูง (High Pressure Pump) สึกหรอ",
                "หัวฉีดรั่วไหลกลับ (Injector Backleak) มากเกินไป",
                "เซนเซอร์แรงดันรางคอมมอนเรล (Rail Pressure Sensor) ผิดพลาด"
            ),
            commonCausesEn = listOf(
                "Sticky Suction Control Valve (SCV Valve)",
                "Clogged fuel/diesel filter cartridge",
                "Worn High Pressure Common Rail Fuel Pump",
                "Excessive injector back-leak return flow",
                "Faulty Fuel Rail Pressure Sensor"
            ),
            symptomsTh = listOf(
                "เร่งแซงแล้วเครื่องวูบ ดับกลางอากาศ หรือเข้า Safe Mode",
                "ไฟรูปเครื่องโชว์ รอบเครื่องถูกล็อคไม่เกิน 2,000 RPM",
                "สตาร์ทยาวกว่าปกติจะติด"
            ),
            symptomsEn = listOf(
                "Engine stalls or enters limp mode under hard acceleration",
                "Check Engine light on, RPM restricted to limp home speed",
                "Extended cranking before engine starts"
            ),
            estimatedCostThb = "฿1,200 - ฿8,500",
            diyDifficulty = "ช่างเฉพาะทาง (Mechanic Pro)",
            affectedSystem = "ระบบคอมมอนเรลแรงดันสูง (Commonrail Fuel System)"
        ),
        DtcCode(
            code = "P0420",
            category = DtcCategory.POWERTRAIN,
            enTitle = "Catalyst System Efficiency Below Threshold (Bank 1)",
            thTitle = "ประสิทธิภาพแคตตาไลติกคอนเวอร์เตอร์ต่ำกว่าเกณฑ์",
            enDescription = "The catalytic converter is not breaking down exhaust emissions efficiently, or downstream O2 sensor is degraded.",
            thDescription = "หม้อกรองไอเสียแคทตาไลติกกรองมลพิษได้ลดลง หรือเซนเซอร์ตรวจจับไอเสียตัวล่างเสื่อมสภาพ",
            severity = DtcSeverity.WARNING,
            commonCausesTh = listOf(
                "ไส้แคทตาไลติกเสื่อมสภาพหรือแตกร้าวตามอายุการใช้งาน",
                "ออกซิเจนเซนเซอร์ตัวหลังแคท (Downstream O2 Sensor) เสื่อม",
                "มีรอยรั่วที่หน้าแปลนท่อไอเสียก่อนถึงตัวเซนเซอร์",
                "น้ำมันเครื่องหรือสารหล่อเย็นรั่วเข้าไปเผาไหม้ในห้องเครื่อง"
            ),
            commonCausesEn = listOf(
                "Aged, deteriorated or cracked catalytic converter substrate",
                "Faulty downstream heated oxygen sensor (HO2S)",
                "Exhaust leak near the converter flange",
                "Engine burning oil or coolant contaminating catalyst"
            ),
            symptomsTh = listOf(
                "ไฟเครื่องโชว์ถาวร",
                "ไอเสียมีกลิ่นฉุนผิดปกติเหมือนกำมะถัน",
                "ตรวจสภาพ ตรอ. ควันดำหรือไอเสียไม่ผ่าน"
            ),
            symptomsEn = listOf(
                "Constant Check Engine light",
                "Unusual sulfur/rotten egg exhaust smell",
                "Failed emissions inspection"
            ),
            estimatedCostThb = "฿2,500 - ฿18,000",
            diyDifficulty = "ช่างเฉพาะทาง (Professional)",
            affectedSystem = "ระบบบำบัดไอเสีย (Emissions & Catalyst)"
        ),
        DtcCode(
            code = "P0401",
            category = DtcCategory.POWERTRAIN,
            enTitle = "Exhaust Gas Recirculation (EGR) Flow Insufficient",
            thTitle = "อัตราการไหลของวาล์ว EGR ต่ำกว่ามาตรฐาน (EGR ตัน)",
            enDescription = "EGR system has insufficient flow of exhaust gas recirculating into the intake manifold due to carbon soot buildup.",
            thDescription = "ระบบหมุนเวียนไอเสีย EGR ไหลเวียนไม่สะดวก เนื่องจากมีเขม่าควันจับตัวหนาแน่นในวาล์วและท่อร่วมไอดี",
            severity = DtcSeverity.WARNING,
            commonCausesTh = listOf(
                "เขม่าควันดำและคราบน้ำมันเครื่องเกาะตันวาล์ว EGR และท่อไอดี",
                "มอเตอร์ควบคุมวาล์ว EGR สเต็ปมอเตอร์ติดขัด",
                "ท่อคูลเลอร์ระบายความร้อน EGR อุดตัน",
                "เซนเซอร์จับตำแหน่งวาล์ว EGR เสีย"
            ),
            commonCausesEn = listOf(
                "Heavy carbon soot buildup in EGR valve and intake manifold",
                "Stuck EGR stepper motor or actuator",
                "Clogged EGR cooler passages",
                "Faulty EGR position sensor"
            ),
            symptomsTh = listOf(
                "ไฟเครื่องยนต์โชว์",
                "อัตราเร่งต้นอืด มีควันดำออกท้ายรถขณะเร่งเครื่อง",
                "เครื่องยนต์กระตุกช่วงรอบต่ำ 1,500 - 2,000 RPM"
            ),
            symptomsEn = listOf(
                "Check Engine Light on",
                "Sluggish low-end acceleration with black smoke puff",
                "Stumble or pinging under light throttle load"
            ),
            estimatedCostThb = "฿800 - ฿3,500",
            diyDifficulty = "ปานกลาง (Medium DIY - ล้างเขม่า)",
            affectedSystem = "ระบบไอดีและควบคุมมลพิษ EGR"
        ),
        DtcCode(
            code = "P0300",
            category = DtcCategory.POWERTRAIN,
            enTitle = "Random / Multiple Cylinder Misfire Detected",
            thTitle = "ตรวจพบอาการจุดระเบิดผิดพลาดหลายสูบ (เครื่องเดินไม่เต็มสูบ)",
            enDescription = "Engine detects random combustion misfires across multiple cylinders.",
            thDescription = "ระบบตรวจพบการจุดระเบิดไม่สมบูรณ์ หรือสูบใดสูบหนึ่งไม่จุดระเบิด ทำให้เครื่องยนต์สั่นกระพือ",
            severity = DtcSeverity.CRITICAL,
            commonCausesTh = listOf(
                "หัวเทียนหมดอายุ หรือคอยล์จุดระเบิด (Ignition Coil) รั่ว/ขาด",
                "สายหัวเทียน หรือปลั๊กคอยล์กรอบแตก",
                "แรงดันน้ำมันเชื้อเพลิงตก หรือหัวฉีดจ่ายน้ำมันไม่สม่ำเสมอ",
                "กำลังอัดในกระบอกสูบต่ำ หรือวาล์วยัน/รั่ว"
            ),
            commonCausesEn = listOf(
                "Worn spark plugs or failed ignition coils",
                "Cracked ignition coil boot or wiring harness",
                "Low fuel pressure or clogged fuel injectors",
                "Low engine cylinder compression or tight valves"
            ),
            symptomsTh = listOf(
                "ไฟเครื่องยนต์กะพริบ (Blinking MIL) แสดงถึงอันตรายต่อเครื่องยนต์",
                "ตัวรถสั่นแรงมาก กำลังเครื่องตกอย่างเห็นได้ชัด",
                "ท่อไอเสียมีเสียงปุๆ และกลิ่นน้ำมันดิบเผาไหม้ไม่หมด"
            ),
            symptomsEn = listOf(
                "Flashing Check Engine Light (severe engine risk)",
                "Severe engine shaking and major loss of power",
                "Popping exhaust sound and unburned fuel smell"
            ),
            estimatedCostThb = "฿1,500 - ฿6,000",
            diyDifficulty = "ปานกลาง (Medium DIY)",
            affectedSystem = "ระบบจุดระเบิด (Ignition & Combustion)"
        ),
        DtcCode(
            code = "P0115",
            category = DtcCategory.POWERTRAIN,
            enTitle = "Engine Coolant Temperature Circuit Malfunction",
            thTitle = "วงจรเซนเซอร์วัดอุณหภูมิน้ำหล่อเย็น (ECT) ผิดปกติ",
            enDescription = "ECT sensor voltage signal is out of normal range, causing incorrect coolant temperature reading.",
            thDescription = "สัญญาณจากเซนเซอร์อุณหภูมิน้ำหล่อเย็นผิดปกติ ทำให้พัดลมหม้อน้ำทำงานตลอดเวลาหรือเครื่องฮีต",
            severity = DtcSeverity.CRITICAL,
            commonCausesTh = listOf(
                "เซนเซอร์ ECT (Engine Coolant Temp Sensor) เสีย",
                "ปลั๊กเซนเซอร์หลวม ขั้วขึ้นขี้เกลือ หรือสายไฟขาดหนูกัด",
                "วาล์วน้ำ (Thermostat) ค้างเปิด หรือค้างปิด",
                "ระดับน้ำหล่อเย็นในหม้อน้ำแห้ง/ขาด"
            ),
            commonCausesEn = listOf(
                "Faulty Engine Coolant Temperature (ECT) sensor",
                "Corroded or broken wiring harness to the sensor",
                "Stuck open or closed thermostat",
                "Low engine coolant level in radiator"
            ),
            symptomsTh = listOf(
                "เกจวัดความร้อนที่หน้าปัดไม่ขึ้น หรือขึ้นสุดตลอดเวลา",
                "พัดลมหม้อน้ำหมุนแรงสุดทันทีที่เปิดสวิตช์กุญแจ",
                "กินน้ำมันเชื้อเพลิงมากผิดปกติเนื่องจากกล่องคิดว่าเครื่องเย็น"
            ),
            symptomsEn = listOf(
                "Temperature gauge reads cold or maximum pegged",
                "Radiator cooling fan running at high speed constantly",
                "Excessive fuel consumption as ECM stays in cold enrichment"
            ),
            estimatedCostThb = "฿600 - ฿2,200",
            diyDifficulty = "ง่าย - ปานกลาง (Easy-Medium)",
            affectedSystem = "ระบบระบายความร้อน (Cooling System)"
        ),

        // ================= TRANSMISSION (TCM) =================
        DtcCode(
            code = "P0700",
            category = DtcCategory.POWERTRAIN,
            enTitle = "Transmission Control System (MIL Request)",
            thTitle = "กล่องควบคุมระบบเกียร์ส่งสัญญาณขอเปิดไฟเตือน (TCM MIL Request)",
            enDescription = "The TCM detected a fault within the automatic transmission or CVT system and requested the ECM to illuminate the MIL lamp.",
            thDescription = "กล่องเกียร์ออโต้ตรวจพบความผิดปกติในระบบเกียร์และส่งคำสั่งขอเปิดไฟเตือนที่หน้าปัด",
            severity = DtcSeverity.WARNING,
            commonCausesTh = listOf(
                "มีรหัสข้อผิดพลาดเฉพาะเจาะจงบันทึกอยู่ในกล่อง TCM (เช่น โซลินอยด์เกียร์ หรืออัตราทด)",
                "น้ำมันเกียร์ออโต้ (ATF/CVTF) เสื่อมสภาพ หรือระดับน้ำมันเกียร์ต่ำเกินไป",
                "ปลั๊กสายไฟชุดสายไฟเกียร์หลวม หรือมีน้ำมันเกียร์ซึมเข้าปลั๊ก"
            ),
            commonCausesEn = listOf(
                "Sub-fault stored inside TCM (Solenoid, slip or pressure fault)",
                "Degraded transmission fluid or low fluid level",
                "Wiring harness connector loose or fluid intrusion"
            ),
            symptomsTh = listOf(
                "ไฟรูปเครื่องยนต์โชว์",
                "เกียร์เปลี่ยนกระตุก หรือเข้าสู่ Limp Home Mode (ล็อคเกียร์ 3)",
                "จังหวะเปลี่ยนเกียร์ช้าหรือลากรอบสูง"
            ),
            symptomsEn = listOf(
                "Check Engine Light illuminated",
                "Harsh gear shifting or locked in limp mode 3rd gear",
                "Delayed gear engagement or high rev slipping"
            ),
            estimatedCostThb = "฿1,500 - ฿12,000",
            diyDifficulty = "ช่างเฉพาะทาง (Transmission Specialist)",
            affectedSystem = "ระบบส่งกำลังและเกียร์อัตโนมัติ (TCM Transmission)"
        ),
        DtcCode(
            code = "P0730",
            category = DtcCategory.POWERTRAIN,
            enTitle = "Incorrect Gear Ratio (Transmission Slip)",
            thTitle = "อัตราทดเกียร์ไม่ถูกต้อง (เกียร์ลื่น / คลัตช์ส่งกำลังไม่เต็มที่)",
            enDescription = "Transmission speed sensors indicate the output speed doesn't match expected gear ratio due to slipping clutch packs or belt.",
            thDescription = "ความเร็วเพลาขาเข้าและขาออกของชุดเกียร์ไม่ตรงกับอัตราทดที่คำนวณไว้ เกิดจากแผ่นคลัตช์เกียร์ลื่นหรือสายพาน CVT สลิป",
            severity = DtcSeverity.CRITICAL,
            commonCausesTh = listOf(
                "แผ่นคลัตช์เกียร์ออโต้สึกหรอไหม้ จากการลากจูงหนักหรือขาดการเปลี่ยนน้ำมันเกียร์",
                "สายพานหรือพูลเลย์เกียร์ CVT สึกหรอ เป็นรอย",
                "โซลินอยด์ควบคุมแรงดันน้ำมันเกียร์ (EPC Solenoid) เสื่อม",
                "กรองน้ำมันเกียร์ในอ่างตัน ทำให้แรงดันน้ำมันตก"
            ),
            commonCausesEn = listOf(
                "Worn or burnt internal clutch plates",
                "Worn CVT metallic push belt or scored pulley cones",
                "Faulty Electronic Pressure Control (EPC) Solenoid",
                "Clogged internal transmission filter cartridge"
            ),
            symptomsTh = listOf(
                "เหยียบคันเร่งแล้วรอบเครื่องขึ้นสูงแต่ความเร็วรถไม่ขึ้นตาม (เกียร์วืด)",
                "กลิ่นไหม้น้ำมันเกียร์",
                "กระตุกรุนแรงเวลาเข้าเกียร์ D หรือ R"
            ),
            symptomsEn = listOf(
                "Engine revs high without vehicle accelerating (slipping)",
                "Burnt transmission fluid odor",
                "Violent clunk when shifting into D or R"
            ),
            estimatedCostThb = "฿4,500 - ฿28,000",
            diyDifficulty = "ช่างเฉพาะทาง (Transmission Overhaul)",
            affectedSystem = "ระบบส่งกำลังและชุดคลัตช์เกียร์ (Transmission Clutches)"
        ),
        DtcCode(
            code = "P0741",
            category = DtcCategory.POWERTRAIN,
            enTitle = "Torque Converter Clutch Circuit Performance or Stuck Off",
            thTitle = "วงจรคลัตช์ทอร์คคอนเวอร์เตอร์ไม่จับ (TCC Lockup Stuck Off)",
            enDescription = "The torque converter lockup clutch fails to lock at highway cruise speeds, leading to excessive fuel consumption and overheating.",
            thDescription = "คลัตช์ล็อคอัพในทอร์คคอนเวอร์เตอร์ไม่ทำงานขณะขับขี่ความเร็วคงที่ ทำให้กินน้ำมันมากขึ้นและความร้อนเกียร์สูง",
            severity = DtcSeverity.WARNING,
            commonCausesTh = listOf(
                "โซลินอยด์ TCC Lockup เสื่อมหรือมีสิ่งสกปรกอุดตัน",
                "น้ำมันเกียร์เก่าเสื่อมสภาพ ความหนืดลดลง",
                "วาล์วบอดี้ (Valve Body) ติดขัด",
                "แผ่นคลัตช์ภายในลูกทอร์คคอนเวอร์เตอร์หมดสภาพ"
            ),
            commonCausesEn = listOf(
                "Faulty or clogged TCC Lockup solenoid",
                "Degraded or burnt automatic transmission fluid",
                "Stuck hydraulic valve inside the valve body",
                "Worn internal torque converter lockup clutch"
            ),
            symptomsTh = listOf(
                "ขับความเร็ว 100-120 km/h รอบเครื่องยนต์สูงกว่าปกติ 300-500 RPM",
                "อุณหภูมิน้ำมันเกียร์สูงขึ้นกว่าปกติ",
                "กินน้ำมันเชื้อเพลิงเพิ่มขึ้น 10-15%"
            ),
            symptomsEn = listOf(
                "Engine RPM 300-500 RPM higher than normal at 100-120 km/h",
                "Elevated transmission fluid operating temperatures",
                "10-15% increase in highway fuel consumption"
            ),
            estimatedCostThb = "฿2,500 - ฿9,500",
            diyDifficulty = "ช่างเฉพาะทาง (Valve Body Service)",
            affectedSystem = "ระบบทอร์คคอนเวอร์เตอร์และวาล์วบอดี้ (TCC Lockup)"
        ),
        DtcCode(
            code = "P0750",
            category = DtcCategory.POWERTRAIN,
            enTitle = "Shift Solenoid 'A' Malfunction",
            thTitle = "โซลินอยด์เปลี่ยนเกียร์ชุด 'A' (Shift Solenoid A) ขัดข้อง",
            enDescription = "Shift solenoid valve A failed to respond electrically or hydraulically when requested by the transmission computer.",
            thDescription = "โซลินอยด์เปลี่ยนเกียร์ตัวที่ 1 (Solenoid A) ไม่ตอบสนองคำสั่งไฟฟ้า ทำให้เข้าเกียร์ 1 หรือ 2 ไม่ได้",
            severity = DtcSeverity.WARNING,
            commonCausesTh = listOf(
                "คอยล์แม่เหล็กไฟฟ้าของโซลินอยด์ A ขาดหรือช็อต",
                "เศษผงโลหะจากแผ่นคลัตช์เข้าไปติดขัดในแกนโซลินอยด์",
                "ชุดสายไฟวาล์วบอดี้ภายในอ่างเกียร์กรอบแตก"
            ),
            commonCausesEn = listOf(
                "Open or shorted coil winding in shift solenoid A",
                "Metallic debris lodged inside solenoid plunger",
                "Internal transmission wiring harness harness fault"
            ),
            symptomsTh = listOf(
                "เกียร์ไม่ยอมเปลี่ยนขึ้นเกียร์ถัดไป หรือข้ามเกียร์",
                "ไฟเตือนระบบเกียร์ / ไฟเครื่องโชว์",
                "รถออกตัวอืดเนื่องจากเริ่มออกตัวที่เกียร์ 3"
            ),
            symptomsEn = listOf(
                "Transmission skips gear or will not shift into next gear",
                "Check Engine and A/T warning light active",
                "Sluggish takeoff as car starts in 3rd gear limp mode"
            ),
            estimatedCostThb = "฿1,800 - ฿5,500",
            diyDifficulty = "ช่างเฉพาะทาง (Solenoid Replacement)",
            affectedSystem = "ชุดวาล์วบอดี้และโซลินอยด์เกียร์ (Shift Solenoids)"
        ),

        // ================= ABS & CHASSIS =================
        DtcCode(
            code = "C0035",
            category = DtcCategory.CHASSIS,
            enTitle = "Left Front Wheel Speed Sensor Circuit",
            thTitle = "วงจรเซนเซอร์ความเร็วล้อหน้าซ้าย (ABS Sensor) ขัดข้อง",
            enDescription = "ABS/ESP control module lost signal or detects improper pulse from front-left wheel speed sensor.",
            thDescription = "กล่องควบคุมระบบเบรก ABS ไม่ได้รับสัญญาณความเร็วจากล้อหน้าซ้าย ไฟเตือน ABS โชว์",
            severity = DtcSeverity.WARNING,
            commonCausesTh = listOf(
                "เซนเซอร์ความเร็วล้อ ABS สกปรก มีเศษผงเหล็กหรือโคลนเกาะ",
                "สายไฟเซนเซอร์ ABS บริเวณซุ้มล้อขาดหรือเปื่อยจากการเลี้ยว",
                "แถบแม่เหล็กที่ลูกปืนล้อชำรุด หรือลูกปืนล้อหลวมคลอน",
                "ตัวเซนเซอร์ ABS ขาดวงจรภายใน"
            ),
            commonCausesEn = listOf(
                "Dirt, brake dust or metallic debris on ABS wheel sensor tip",
                "Broken or frayed ABS wheel harness in fender well",
                "Damaged magnetic encoder ring on wheel bearing",
                "Internal open circuit in ABS speed sensor"
            ),
            symptomsTh = listOf(
                "ไฟเตือนระบบเบรก ABS และไฟเตือนกันลื่น (Traction Control) โชว์",
                "ระบบเบรกป้องกันล้อล็อคไม่ทำงาน (ระบบเบรกธรรมดายังใช้ได้)",
                "ระบบช่วยออกตัวบนทางลาดชัน (HSA) ปิดการทำงาน"
            ),
            symptomsEn = listOf(
                "ABS and Traction Control / VSC warning lights lit",
                "Anti-lock braking feature disabled (standard brakes work)",
                "Hill Start Assist unavailable"
            ),
            estimatedCostThb = "฿800 - ฿3,200",
            diyDifficulty = "ปานกลาง (Medium DIY)",
            affectedSystem = "ระบบเบรก ABS และความปลอดภัยช่วงล่าง"
        ),
        DtcCode(
            code = "C0200",
            category = DtcCategory.CHASSIS,
            enTitle = "Right Front Wheel Speed Sensor Signal Malfunction",
            thTitle = "สัญญาณเซนเซอร์ความเร็วล้อหน้าขวาขัดข้อง",
            enDescription = "ABS module detected erratic signal pulse from the right front ABS sensor.",
            thDescription = "กล่องควบคุม ABS ตรวจพบคลื่นสัญญาณกระตุกหรือไม่สม่ำเสมอจากล้อหน้าขวา",
            severity = DtcSeverity.WARNING,
            commonCausesTh = listOf(
                "เซนเซอร์ ABS ล้อหน้าขวาเสื่อม",
                "จานฟันเฟืองหรือแถบแม่เหล็กที่ดุมล้อสกปรกหรือบิ่น",
                "ระยะห่าง (Air Gap) ระหว่างเซนเซอร์กับจานหมุนไม่ถูกต้อง"
            ),
            commonCausesEn = listOf(
                "Right front ABS sensor degraded",
                "Damaged tone ring / reluctor on wheel hub",
                "Incorrect air gap between sensor and reluctor"
            ),
            symptomsTh = listOf(
                "ไฟ ABS / VSC โชว์",
                "แป้นเบรกเต้นสะท้านช่วงความเร็วต่ำก่อนหยุดรถ"
            ),
            symptomsEn = listOf(
                "ABS / VSC light active",
                "False ABS pulsation at low speeds just before stopping"
            ),
            estimatedCostThb = "฿800 - ฿2,800",
            diyDifficulty = "ปานกลาง (Medium)",
            affectedSystem = "ระบบช่วงล่างและเบรก ABS"
        ),
        DtcCode(
            code = "C0110",
            category = DtcCategory.CHASSIS,
            enTitle = "ABS Pump Motor Circuit Malfunction",
            thTitle = "วงจรมอเตอร์ปั๊มไฮดรอลิก ABS ขัดข้อง (ABS Pump Motor Fault)",
            enDescription = "The ABS Hydraulic Electronic Control Unit (HECU) detected that the pump motor circuit is open, shorted, or the motor is jammed.",
            thDescription = "กล่องปั๊มเบรก ABS ไฮดรอลิกตรวจพบว่ามอเตอร์ปั๊มเบรกไม่หมุนหรือแปรงถ่านมอเตอร์หมด",
            severity = DtcSeverity.CRITICAL,
            commonCausesTh = listOf(
                "แปรงถ่านมอเตอร์ปั๊ม ABS หมดสภาพหรือขี้เกลือขึ้นแกนหมุน",
                "รีเลย์ปั๊ม ABS เสีย หรือฟิวส์ปั๊ม ABS 40A ขาด",
                "กล่องวงจรควบคุม HECU ชำรุด",
                "สายกราวด์ปั๊ม ABS หลวม"
            ),
            commonCausesEn = listOf(
                "Worn carbon brushes in ABS pump motor or seized motor shaft",
                "Faulty ABS pump relay or blown 40A ABS maxi fuse",
                "Internal circuit solder joint failure inside HECU module",
                "Corroded or loose main ABS chassis ground strap"
            ),
            symptomsTh = listOf(
                "ไฟ ABS และไฟเตือนเบรกมือสีแดงโชว์พร้อมกัน",
                "ระบบช่วยกระจายแรงเบรก EBD และ ABS ไม่ทำงาน",
                "ระยะเบรกอาจยาวขึ้นเมื่อเบรกฉุกเฉินบนถนนเปียก"
            ),
            symptomsEn = listOf(
                "ABS warning light and Red Brake indicator stay illuminated",
                "EBD Electronic Brakeforce Distribution and ABS inactive",
                "Increased stopping distance on wet pavement under hard braking"
            ),
            estimatedCostThb = "฿2,800 - ฿14,000",
            diyDifficulty = "ช่างเฉพาะทาง (ABS Hydraulic Unit Rebuild)",
            affectedSystem = "ปั๊มไฮดรอลิกเบรก ABS (HECU Pump Unit)"
        ),

        // ================= AIRBAG & RESTRAINTS (SRS) =================
        DtcCode(
            code = "B1000",
            category = DtcCategory.BODY,
            enTitle = "ECU Malfunction / SRS Airbag Module Fault",
            thTitle = "กล่องควบคุมถุงลมนิรภัย (SRS Airbag) ขัดข้อง",
            enDescription = "Airbag Supplemental Restraint System module detected internal fault or low deployment loop voltage.",
            thDescription = "ระบบถุงลมนิรภัยตรวจพบความผิดปกติในวงจรจุดระเบิด หรือสายแพรพวงมาลัยขาด",
            severity = DtcSeverity.CRITICAL,
            commonCausesTh = listOf(
                "สายแพรพวงมาลัย (Clock Spring / Spiral Cable) ขาดจากการเลี้ยวสุดบ่อยครั้ง",
                "ปลั๊กต่อสายถุงลมนิรภัยใต้เบาะคนขับหลวมเมื่อเลื่อนเบาะ",
                "กล่องควบคุม SRS ตรวจพบแรงดันไฟต่ำกว่าเกณฑ์",
                "เซนเซอร์ตรวจจับการชนหน้ารถ (Crash Sensor) ชำรุด"
            ),
            commonCausesEn = listOf(
                "Broken steering wheel clock spring / spiral cable",
                "Loose SRS harness connector under driver/passenger seat",
                "Low battery voltage or SRS module internal fault",
                "Damaged front impact crash sensor"
            ),
            symptomsTh = listOf(
                "ไฟเตือนรูปถุงลมนิรภัย (Airbag) สีแดงโชว์ค้างที่หน้าปัด",
                "ปุ่มกดแตร หรือปุ่มควบคุมเครื่องเสียงที่พวงมาลัยอาจใช้งานไม่ได้ (กรณีสายแพรขาด)"
            ),
            symptomsEn = listOf(
                "Red SRS Airbag warning light stays on continuously",
                "Steering wheel horn or audio buttons inoperative if clockspring is broken"
            ),
            estimatedCostThb = "฿900 - ฿4,500",
            diyDifficulty = "ช่างเฉพาะทาง (Professional Only)",
            affectedSystem = "ระบบความปลอดภัยตัวถัง (SRS Airbag)"
        ),
        DtcCode(
            code = "B0001",
            category = DtcCategory.BODY,
            enTitle = "Driver Frontal Stage 1 Deployment Control",
            thTitle = "วงจรจุดระเบิดถุงลมนิรภัยฝั่งคนขับ (Driver Airbag Squib) ขัดข้อง",
            enDescription = "Resistance of driver airbag inflator circuit is out of safe range (normal 2.0 - 3.0 Ohms).",
            thDescription = "ค่าความต้านทานวงจรจุดระเบิดถุงลมพวงมาลัยคนขับสูงหรือต่ำเกินไป (สายแพรพวงมาลัยเสื่อม)",
            severity = DtcSeverity.CRITICAL,
            commonCausesTh = listOf(
                "สายแพรพวงมาลัย (Spiral Cable Clock Spring) ขาดใน",
                "ขั้วต่อสายสีเหลืองหลังลูกถุงลมพวงมาลัยหลวม",
                "ลูกถุงลมนิรภัยเสื่อมสภาพ"
            ),
            commonCausesEn = listOf(
                "Broken internal ribbon trace in steering clock spring",
                "Loose yellow SRS squib safety clip connector",
                "Degraded driver airbag squib initiator"
            ),
            symptomsTh = listOf(
                "ไฟเตือนถุงลมนิรภัยสีแดงโชว์ค้าง",
                "ถุงลมฝั่งคนขับอาจไม่ทำงานเมื่อเกิดอุบัติเหตุชนหนัก"
            ),
            symptomsEn = listOf(
                "Continuous red SRS warning light",
                "Driver airbag disabled in severe frontal collision"
            ),
            estimatedCostThb = "฿1,200 - ฿3,800",
            diyDifficulty = "ช่างเฉพาะทาง (Clock Spring Replacement)",
            affectedSystem = "ระบบถุงลมคนขับและสายแพรพวงมาลัย (Driver SRS)"
        ),

        // ================= BODY CONTROL & ELECTRICAL (BCM) =================
        DtcCode(
            code = "B1318",
            category = DtcCategory.BODY,
            enTitle = "Battery Voltage Low to Body Control Module",
            thTitle = "แรงดันไฟฟ้าเข้ากล่องควบคุมตัวถัง (BCM) ต่ำกว่าเกณฑ์",
            enDescription = "BCM measured main battery feed voltage below 10.5V, causing accessory brownout or erratic electronic behavior.",
            thDescription = "กล่อง BCM ตรวจพบแรงดันไฟแบตเตอรี่ต่ำกว่า 10.5V ทำให้ระบบไฟฟ้าในห้องโดยสารทำงานรวน",
            severity = DtcSeverity.WARNING,
            commonCausesTh = listOf(
                "แบตเตอรี่ 12V เสื่อมสภาพ เก็บประจุไม่อยู่ (อายุเกิน 2 ปี)",
                "ไดชาร์จ (Alternator) จ่ายไฟไม่พอ หรือสายพานไดชาร์จหย่อน",
                "ขั้วแบตเตอรี่หลวม มีคราบขี้เกลือขาวจับหนาแน่น",
                "มีอุปกรณ์เสริมแอบกินกระแสไฟขณะจอดดับเครื่อง (Parasitic Draw)"
            ),
            commonCausesEn = listOf(
                "Aged 12V lead-acid / EFB battery unable to hold charge",
                "Undercharging alternator or loose alternator serpentine belt",
                "Corroded or loose battery terminal clamps",
                "Excessive parasitic dark current draw while parked"
            ),
            symptomsTh = listOf(
                "สตาร์ทรถเสียงมอเตอร์หมุนช้า อืด",
                "ไฟหน้าปัดหรี่ลง หรือระบบเครื่องเสียงรีเซ็ตตัวเอง",
                "ระบบกระจกไฟฟ้าหรือเซ็นทรัลล็อคทำงานช้าลง"
            ),
            symptomsEn = listOf(
                "Slow cranking when starting engine",
                "Dashboard lights dim or infotainment screen reboots",
                "Sluggish power windows or central lock actuators"
            ),
            estimatedCostThb = "฿1,800 - ฿3,800",
            diyDifficulty = "ง่าย - ปานกลาง (Battery Replacement)",
            affectedSystem = "ระบบไฟฟ้าและกล่องควบคุมตัวถัง (BCM & 12V Battery)"
        ),
        DtcCode(
            code = "B1600",
            category = DtcCategory.BODY,
            enTitle = "Smart Key / Immobilizer Transponder Signal Missing",
            thTitle = "ไม่พบสัญญาณชิพอิมโมบิไลเซอร์ / Smart Key (Immobilizer Fault)",
            enDescription = "BCM/Immobilizer unit received no valid transponder key code upon ignition cycle, inhibiting engine start.",
            thDescription = "ระบบกันขโมยและกุญแจรีโมทไม่สามารถอ่านรหัสชิพจากกุญแจได้ ระบบจะตัดการจ่ายน้ำมันและสตาร์ทเครื่องยนต์",
            severity = DtcSeverity.CRITICAL,
            commonCausesTh = listOf(
                "ถ่านรีโมท Smart Key หมด (CR2032)",
                "เสาอากาศรับสัญญาณกุญแจในห้องโดยสารขัดข้อง",
                "ชิพอิมโมบิไลเซอร์ในดอกกุญแจตกหล่นหรือเสียหาย",
                "กล่องอ่านสัญญาณกุญแจรอบคอกุญแจหลวม"
            ),
            commonCausesEn = listOf(
                "Depleted key fob battery (CR2032)",
                "Faulty keyless entry cabin antenna",
                "Damaged RFID transponder chip inside key housing",
                "Defective immobilizer transponder coil ring"
            ),
            symptomsTh = listOf(
                "เหยียบเบรกกดปุ่มสตาร์ทแล้วเครื่องไม่ติด หรือบิดกุญแจแล้วเงียบ",
                "ไฟรูปกุญแจสีส้ม/แดงกะพริบที่หน้าปัด",
                "แตะดอกกุญแจแนบกับปุ่ม Push Start แล้วอาจติด"
            ),
            symptomsEn = listOf(
                "Engine no-start / no-crank when pressing Push Start",
                "Security / Key indicator light flashing on dash",
                "Vehicle starts only when holding key directly against Start button"
            ),
            estimatedCostThb = "฿150 - ฿3,500",
            diyDifficulty = "ง่าย (เปลี่ยนถ่านรีโมท) - ช่างกุญแจ",
            affectedSystem = "ระบบกุญแจอัจฉริยะและกันขโมย (Smart Key & Immobilizer)"
        ),

        // ================= NETWORK & CAN BUS =================
        DtcCode(
            code = "U0100",
            category = DtcCategory.NETWORK,
            enTitle = "Lost Communication with ECM / PCM 'A'",
            thTitle = "ขาดการสื่อสารกับกล่องควบคุมเครื่องยนต์ (Lost ECM Comm)",
            enDescription = "CAN bus network nodes lost serial communication with the primary Engine Control Module.",
            thDescription = "กล่องควบคุมต่างๆ ในระบบเครือข่าย CAN Bus สื่อสารติดต่อกับกล่องหลักเครื่องยนต์ไม่ได้",
            severity = DtcSeverity.CRITICAL,
            commonCausesTh = listOf(
                "แบตเตอรี่รถยนต์เสื่อมสภาพ แรงดันตกขณะสตาร์ทต่ำกว่า 10V",
                "ขั้วต่อสายไฟกล่อง ECU หลวม มีน้ำเข้า หรือขี้เกลือขึ้น",
                "สายไฟเครือข่าย CAN-High หรือ CAN-Low ขาดหรือลัดวงจร",
                "ฟิวส์กล่องควบคุมเครื่องยนต์ (ECM Fuse) ขาด หรือรีเลย์เมนเสีย"
            ),
            commonCausesEn = listOf(
                "Weak or discharged 12V battery causing voltage drop under cranking",
                "Corroded or wet ECM harness multi-pin connector",
                "CAN-High or CAN-Low data bus line shorted or severed",
                "Blown ECM main power fuse or faulty main relay"
            ),
            symptomsTh = listOf(
                "รถสตาร์ทไม่ติด หรือมอเตอร์สตาร์ทหมุนแต่เครื่องไม่ติด",
                "ไฟเตือนหลายดวงโชว์พร้อมกันที่หน้าปัด (เครื่องยนต์, ABS, พวงมาลัยไฟฟ้า)",
                "เกียร์ออโต้เข้าสู่เกียร์ฉุกเฉิน (Limp Mode)"
            ),
            symptomsEn = listOf(
                "No-crank or crank with no-start condition",
                "Multiple dash warning lights illuminated simultaneously",
                "Automatic transmission locked in limp mode gear"
            ),
            estimatedCostThb = "฿1,500 - ฿7,500",
            diyDifficulty = "ช่างเฉพาะทาง (Professional)",
            affectedSystem = "ระบบเครือข่ายสื่อสาร CAN Bus & ECM"
        ),
        DtcCode(
            code = "U0101",
            category = DtcCategory.NETWORK,
            enTitle = "Lost Communication with Transmission Control Module (TCM)",
            thTitle = "ขาดการสื่อสารกับกล่องควบคุมเกียร์ออโต้ (Lost TCM Comm)",
            enDescription = "CAN bus nodes lost high-speed communication link with the Transmission Control Module.",
            thDescription = "เครือข่าย CAN Bus ขาดการติดต่อกับกล่องสมองกลเกียร์ออโต้",
            severity = DtcSeverity.CRITICAL,
            commonCausesTh = listOf(
                "ปลั๊กสายไฟกล่องเกียร์ TCM หลวมหรือชื้น",
                "ฟิวส์ระบบควบคุมเกียร์ขาด",
                "สาย CAN Bus บัสเกียร์ขาดวงจร"
            ),
            commonCausesEn = listOf(
                "Loose or water-damaged TCM connector",
                "Blown TCM control fuse",
                "Open CAN communication bus line to TCM"
            ),
            symptomsTh = listOf(
                "ไฟเกียร์ไม่โชว์ตำแหน่ง P/R/N/D",
                "เกียร์ล็อคเข้าเกียร์ 3 ฉุกเฉิน",
                "หน้าปัดเตือน Check Transmission"
            ),
            symptomsEn = listOf(
                "Gear position display blank on dashboard",
                "Transmission locked in 3rd gear limp home",
                "Check Transmission warning active"
            ),
            estimatedCostThb = "฿1,500 - ฿6,500",
            diyDifficulty = "ช่างเฉพาะทาง (Professional)",
            affectedSystem = "เครือข่ายสื่อสารกล่องเกียร์ (CAN Bus TCM)"
        ),
        DtcCode(
            code = "U0121",
            category = DtcCategory.NETWORK,
            enTitle = "Lost Communication with Anti-Lock Brake System (ABS) Module",
            thTitle = "ขาดการสื่อสารกับกล่องควบคุมระบบเบรก ABS",
            enDescription = "Communication over CAN bus data network to the ABS control module was interrupted.",
            thDescription = "เครือข่าย CAN Bus สูญเสียการเชื่อมต่อกับกล่องปั๊ม ABS",
            severity = DtcSeverity.WARNING,
            commonCausesTh = listOf(
                "ปลั๊กไฟปั๊ม ABS หลวมหรือสายดินหลุด",
                "ฟิวส์ระบบเบรก ABS ขาด",
                "กล่องไฮดรอลิก ABS ชำรุด"
            ),
            commonCausesEn = listOf(
                "Loose ABS pump harness or poor ground connection",
                "Blown ABS power fuse",
                "Faulty ABS hydraulic control unit"
            ),
            symptomsTh = listOf(
                "ไฟเตือน ABS, VSC, Handbrake โชว์พร้อมกัน",
                "ไมล์ความเร็วอาจไม่ขึ้นในรถบางรุ่นที่รับค่าจากปั๊ม ABS"
            ),
            symptomsEn = listOf(
                "ABS, VSC, and brake indicator lights on",
                "Speedometer may stop functioning on vehicles taking VSS from ABS"
            ),
            estimatedCostThb = "฿1,200 - ฿6,000",
            diyDifficulty = "ช่างเฉพาะทาง (Professional)",
            affectedSystem = "ระบบเครือข่าย CAN Bus (ABS Communication)"
        )
    )

    fun findByCode(code: String): DtcCode? {
        return allCodes.find { it.code.equals(code.trim(), ignoreCase = true) }
    }

    fun getCodesBySystem(system: DiagnosticSystem): List<DtcCode> {
        return when (system) {
            DiagnosticSystem.ALL -> allCodes
            DiagnosticSystem.ENGINE -> allCodes.filter {
                it.category == DtcCategory.POWERTRAIN && !it.code.startsWith("P07") && !it.code.startsWith("P08")
            }
            DiagnosticSystem.POWERTRAIN -> allCodes.filter {
                it.category == DtcCategory.POWERTRAIN || it.code == "U0101"
            }
            DiagnosticSystem.CHASSIS -> allCodes.filter {
                it.category == DtcCategory.CHASSIS || it.code == "U0121"
            }
            DiagnosticSystem.ELECTRICAL -> allCodes.filter {
                it.category == DtcCategory.BODY || it.category == DtcCategory.NETWORK
            }
            DiagnosticSystem.TRANSMISSION -> allCodes.filter {
                it.code.startsWith("P07") || it.code.startsWith("P08") || it.code == "U0101"
            }
            DiagnosticSystem.ABS -> allCodes.filter {
                it.category == DtcCategory.CHASSIS || it.code == "U0121"
            }
            DiagnosticSystem.AIRBAG -> allCodes.filter {
                it.category == DtcCategory.BODY && (it.code.startsWith("B0") || it.code == "B1000")
            }
            DiagnosticSystem.BODY -> allCodes.filter {
                it.category == DtcCategory.BODY && (it.code.startsWith("B1") && it.code != "B1000" || it.code.startsWith("B2"))
            }
            DiagnosticSystem.NETWORK -> allCodes.filter {
                it.category == DtcCategory.NETWORK
            }
            DiagnosticSystem.IMMOBILIZER -> allCodes.filter {
                it.code.startsWith("B1000") || it.code.startsWith("B16") || it.code.startsWith("P16") || it.category == DtcCategory.BODY
            }
        }
    }

    fun search(query: String): List<DtcCode> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return allCodes
        return allCodes.filter {
            it.code.lowercase().contains(q) ||
            it.thTitle.lowercase().contains(q) ||
            it.enTitle.lowercase().contains(q) ||
            it.thDescription.lowercase().contains(q) ||
            it.enDescription.lowercase().contains(q) ||
            it.affectedSystem.lowercase().contains(q)
        }
    }
}
