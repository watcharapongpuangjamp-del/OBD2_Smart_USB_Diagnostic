package com.example.data.repository

import com.example.data.model.FreezeFrameData
import com.example.data.model.ThaiCarSpec
import com.example.data.model.ThaiEngineCategory
import com.example.data.model.VehicleDtcInterpretationInfo
import com.example.data.model.VehicleFreezeFrameAnalysis
import com.example.data.model.VehicleParameterBaseline

object ThaiCarDatabase {

    val popularThaiCars: List<ThaiCarSpec> = listOf(
        // ==================== TOYOTA ====================
        ThaiCarSpec(
            id = "toyota_hilux_revo",
            make = "Toyota",
            model = "Hilux Revo / Rocco / GR Sport",
            generationTh = "โฉม Revo (2015 - ปัจจุบัน)",
            yearRange = "2015 - 2026",
            engineCode = "2GD-FTV (2.4L) / 1GD-FTV (2.8L) VN Turbo",
            engineDisplacement = "2,393 - 2,755 cc",
            horsepower = "150 - 224 hp / 550 Nm",
            fuelType = "ดีเซล (Diesel B7/B10/B20)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 300f, normalFuelRailPressureMaxBar = 2000f, normalTurboBoostMaxBar = 1.8f),
            vinPrefixes = listOf("MR0BA", "MR0HA", "MR0KA", "MR0"),
            obdLocationTh = "ใต้คอนโซลพวงมาลัยฝั่งคนขับ เหนือแป้นคันเร่งประมาณ 15 ซม.",
            obdLocationEn = "Under driver side dashboard above accelerator pedal",
            engineOilSpec = "SAE 0W-20 / 5W-30 API CK-4 / ACEA C2",
            oilCapacityLiters = 7.5f,
            transmissionFluidSpec = "Toyota ATF WS (เกียร์ออโต้ 6 สปีด) / Gear Oil LV 75W",
            coolantCapacityLiters = 8.6f,
            tirePressurePsi = "หน้า 30-32 PSI / หลัง 36-40 PSI",
            sparkPlugOrGlowSpec = "หัวเผาเซรามิก 11V Glow Plug (Denso/Toyota OEM)",
            commonIssuesTh = listOf("ไฟกรองดักน้ำโชว์เมื่อมีน้ำผสมในโซล่า", "ท่ออินเตอร์คูลเลอร์รั่วเมื่อปรับบูสต์สูง", "วาล์ว EGR ตันเขม่า", "เซนเซอร์ท้ายรางคอมมอนเรลสกปรก"),
            commonIssuesEn = listOf("Fuel water separator warning light", "Intercooler hose leakage under high boost", "EGR valve carbon buildup", "Common rail pressure sensor drift")
        ),
        ThaiCarSpec(
            id = "toyota_vigo",
            make = "Toyota",
            model = "Hilux Vigo / Vigo Champ",
            generationTh = "โฉม Vigo (2004 - 2015)",
            yearRange = "2004 - 2015",
            engineCode = "1KD-FTV (3.0L) / 2KD-FTV (2.5L) / 2TR-FE (2.7L)",
            engineDisplacement = "2,494 - 2,982 cc",
            horsepower = "102 - 163 hp",
            fuelType = "ดีเซล / เบนซิน-LPG",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 850, normalFuelRailPressureMinBar = 250f, normalFuelRailPressureMaxBar = 1600f, normalTurboBoostMaxBar = 1.2f),
            vinPrefixes = listOf("MR0FZ", "MR0FR", "MR053"),
            obdLocationTh = "ใต้คอพวงมาลัย ด้านขวาใกล้คันดึงเปิดฝากระโปรง",
            obdLocationEn = "Under steering column right side near hood latch",
            engineOilSpec = "10W-30 / 15W-40 API CI-4/SL",
            oilCapacityLiters = 6.9f,
            transmissionFluidSpec = "Toyota ATF T-IV / Gear Oil 75W-90",
            coolantCapacityLiters = 8.2f,
            tirePressurePsi = "หน้า 30 PSI / หลัง 35 PSI",
            sparkPlugOrGlowSpec = "หัวเผา Toyota Genuine 11V / หัวเทียน K20HR-U11",
            commonIssuesTh = listOf("วาล์ว SCV ท้ายปั๊มติดขัดรอบสวิง", "โอริงปลายหัวฉีดรั่วไหลกลับ", "สายพานหน้าเครื่องมีเสียงดัง"),
            commonIssuesEn = listOf("SCV valve sticking causing idle surging", "Injector leak-back O-ring wear", "Serpentine belt squeal")
        ),
        ThaiCarSpec(
            id = "toyota_fortuner",
            make = "Toyota",
            model = "Fortuner / Legender / GR Sport",
            generationTh = "โฉม Gen 1 & Gen 2 (2005 - ปัจจุบัน)",
            yearRange = "2005 - 2026",
            engineCode = "1GD-FTV (2.8L) / 2GD-FTV (2.4L) / 1KD / 2TR",
            engineDisplacement = "2,393 - 2,982 cc",
            horsepower = "150 - 224 hp",
            fuelType = "ดีเซล (Diesel B7/B10/B20)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 300f, normalFuelRailPressureMaxBar = 2000f, normalTurboBoostMaxBar = 1.8f),
            vinPrefixes = listOf("MR0HA", "MR0FB", "MR0"),
            obdLocationTh = "ใต้แผงคอนโซลฝั่งคนขับ เหนือคันเร่ง",
            obdLocationEn = "Under driver console right above gas pedal",
            engineOilSpec = "0W-20 / 5W-30 API CK-4",
            oilCapacityLiters = 7.5f,
            transmissionFluidSpec = "Toyota ATF WS 6-Speed",
            coolantCapacityLiters = 8.8f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 32 PSI",
            sparkPlugOrGlowSpec = "Denso Ceramic Glow Plug 11V",
            commonIssuesTh = listOf("ปั๊มพาวเวอร์น้ำมันซึมในรุ่น Gen 1", "โช้คอัพหลังสว่างกระแทกแรง", "วาล์ว EGR สกปรก"),
            commonIssuesEn = listOf("Power steering pump fluid weep on Gen 1", "Rear shock absorber firmness", "EGR soot deposits")
        ),
        ThaiCarSpec(
            id = "toyota_corolla_altis",
            make = "Toyota",
            model = "Corolla Altis / GR Sport / Hybrid",
            generationTh = "โฉมหน้าหมู / หน้าแบน / Esport / TNGA (2001 - ปัจจุบัน)",
            yearRange = "2001 - 2026",
            engineCode = "2ZR-FBE (1.8L) / 2ZR-FXE (1.8 Hybrid) / 1ZZ-FE",
            engineDisplacement = "1,598 - 1,798 cc",
            horsepower = "122 - 140 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / E85 / Hybrid",
            engineCategory = ThaiEngineCategory.HYBRID_HEV_PHEV,
            baseline = VehicleParameterBaseline(idleRpmMin = 800, idleRpmMax = 1200, normalFuelRailPressureMinBar = 3.8f, normalFuelRailPressureMaxBar = 4.2f, normalTurboBoostMaxBar = 0.0f),
            vinPrefixes = listOf("MR0BR", "MR0BL", "MR0BD"),
            obdLocationTh = "ใต้แผงฟิวส์ในห้องโดยสาร ฝั่งขวาของพวงมาลัย",
            obdLocationEn = "Under fuse panel to the right of steering wheel",
            engineOilSpec = "Toyota Genuine 0W-20 / 5W-30 SP",
            oilCapacityLiters = 4.2f,
            transmissionFluidSpec = "Toyota CVT Fluid FE",
            coolantCapacityLiters = 5.8f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 32 PSI",
            sparkPlugOrGlowSpec = "Denso Iridium SC20HR11",
            commonIssuesTh = listOf("พัดลมหม้อน้ำเสื่อมสภาพทำให้ความร้อนขึ้นจอดติดไฟแดง", "คอยล์จุดระเบิดเสื่อมรอบเดินเบาสั่น", "แบตเตอรี่ไฮบริด 12V สื่อสารผิดพลาด"),
            commonIssuesEn = listOf("Radiator fan motor failure causing traffic overheat", "Ignition coil misfire at idle", "12V auxiliary battery discharge")
        ),
        ThaiCarSpec(
            id = "toyota_camry",
            make = "Toyota",
            model = "Camry / Hybrid (ACV30 / ACV40 / ACV50 / AXVH70)",
            generationTh = "โฉมผู้นำ / ตาเหยี่ยว / ไฮบริด (2002 - ปัจจุบัน)",
            yearRange = "2002 - 2026",
            engineCode = "2AZ-FE (2.4L) / 2AR-FE (2.5L) / A25A-FXS (2.5 Hybrid)",
            engineDisplacement = "1,998 - 2,494 cc",
            horsepower = "167 - 211 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / Hybrid",
            engineCategory = ThaiEngineCategory.HYBRID_HEV_PHEV,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 1000, normalFuelRailPressureMinBar = 4.0f, normalFuelRailPressureMaxBar = 150f),
            vinPrefixes = listOf("MR0AH", "MR0BA", "MR0"),
            obdLocationTh = "ใต้แผงคอนโซลพวงมาลัยฝั่งคนขับ",
            obdLocationEn = "Under driver side dash panel",
            engineOilSpec = "Toyota Genuine 0W-20 / 5W-30 Fully Synthetic",
            oilCapacityLiters = 4.5f,
            transmissionFluidSpec = "Toyota ATF WS / e-CVT Fluid",
            coolantCapacityLiters = 6.8f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 33 PSI",
            sparkPlugOrGlowSpec = "Denso FK16HR-A8 Iridium",
            commonIssuesTh = listOf("คอนโซลหน้าเหนียวลอกในรุ่น ACV40", "พัดลมระบายอากาศแบตเตอรี่ไฮบริดตันฝุ่น", "ยางแท่นเครื่องฝั่งขวาทรุดตัว"),
            commonIssuesEn = listOf("ACV40 sticky dashboard trim degradation", "Hybrid battery cooling fan dust restriction", "Right side hydraulic engine mount collapse")
        ),
        ThaiCarSpec(
            id = "toyota_yaris_ativ",
            make = "Toyota",
            model = "Yaris / Yaris ATIV",
            generationTh = "Eco Car โฉม XP150 / NGC102 (2013 - ปัจจุบัน)",
            yearRange = "2013 - 2026",
            engineCode = "3NR-FE / 3NR-FKE (1.2L Dual VVT-i)",
            engineDisplacement = "1,197 cc",
            horsepower = "86 - 94 hp",
            fuelType = "แก๊สโซฮอล์ 95 / 91 / E20",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 3.5f, normalFuelRailPressureMaxBar = 4.0f, normalTurboBoostMaxBar = 0.0f),
            vinPrefixes = listOf("MR0", "MR0NC", "MR0BC"),
            obdLocationTh = "ใต้แผงควบคุมฝั่งคนขับ ติดกับคอพวงมาลัยฝั่งซ้าย",
            obdLocationEn = "Under driver dash, to the left of steering column",
            engineOilSpec = "Toyota Genuine Motor Oil 0W-16 / 0W-20 SP",
            oilCapacityLiters = 3.6f,
            transmissionFluidSpec = "Toyota CVT Fluid FE",
            coolantCapacityLiters = 4.8f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "Denso Iridium SC20HR11",
            commonIssuesTh = listOf("น้ำมันเกียร์ CVT เสื่อมสภาพหากไม่เปลี่ยนทุก 40,000 กม.", "ยางแท่นเครื่องทรุดตัวสั่นจอดติดไฟแดง", "เซนเซอร์ออกซิเจนจับคราบเขม่า"),
            commonIssuesEn = listOf("CVT fluid degradation if not flushed regularly", "Hydraulic engine mount collapse idle vibration", "Upstream O2 sensor carbon coating")
        ),
        ThaiCarSpec(
            id = "toyota_vios",
            make = "Toyota",
            model = "Vios",
            generationTh = "โฉม Gen 1 (NCP42) / Gen 2 (NCP93) / Gen 3 (NCP150)",
            yearRange = "2002 - 2023",
            engineCode = "1NZ-FE (1.5L VVT-i) / 2NR-FBE (1.5L Dual VVT-i)",
            engineDisplacement = "1,496 - 1,497 cc",
            horsepower = "108 - 109 hp",
            fuelType = "แก๊สโซฮอล์ 95 / 91 / E20 / E85",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 680, idleRpmMax = 780, normalFuelRailPressureMinBar = 3.5f, normalFuelRailPressureMaxBar = 3.8f, normalTurboBoostMaxBar = 0.0f),
            vinPrefixes = listOf("MR0SK", "MR0BT", "MR052"),
            obdLocationTh = "ใต้คอนโซลหน้าฝั่งคนขับ ด้านซ้ายของแกนพวงมาลัย",
            obdLocationEn = "Under driver dash to the left of steering shaft",
            engineOilSpec = "Toyota Genuine 5W-30 / 10W-40 Semi-Synthetic",
            oilCapacityLiters = 3.7f,
            transmissionFluidSpec = "Toyota ATF Type T-IV (4-Speed) / CVT Fluid FE",
            coolantCapacityLiters = 5.0f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "Denso K16R-U11 / SC20HR11",
            commonIssuesTh = listOf("ปั๊มน้ำเครื่องยนต์ซึมคราบน้ำยาหล่อเย็นสีชมพู", "คอยล์จุดระเบิดเสื่อมตามอายุ", "ลูกปืนล้อหลังมีเสียงหอน"),
            commonIssuesEn = listOf("Water pump coolant pink residue weep", "Ignition coil breakdown", "Rear wheel bearing humming noise")
        ),
        ThaiCarSpec(
            id = "toyota_commuter",
            make = "Toyota",
            model = "Commuter / Ventury / HiAce",
            generationTh = "ตู้ Commuter หลังคาสูง / All-New (2005 - ปัจจุบัน)",
            yearRange = "2005 - 2026",
            engineCode = "2KD-FTV (2.5L) / 1KD-FTV (3.0L) / 1GD-FTV (2.8L)",
            engineDisplacement = "2,494 - 2,982 cc",
            horsepower = "102 - 177 hp",
            fuelType = "ดีเซล (Diesel B7/B10)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 280f, normalFuelRailPressureMaxBar = 1800f, normalTurboBoostMaxBar = 1.4f),
            vinPrefixes = listOf("MR0GX", "MR0GB", "MR0"),
            obdLocationTh = "อยู่ใต้เบาะนั่งฝั่งคนขับ/แผงฟิวส์ใต้พวงมาลัย",
            obdLocationEn = "Under driver seat access / dashboard fuse panel",
            engineOilSpec = "SAE 10W-30 / 5W-30 API CK-4",
            oilCapacityLiters = 7.0f,
            transmissionFluidSpec = "Toyota ATF WS / Gear Oil 75W-90",
            coolantCapacityLiters = 11.5f,
            tirePressurePsi = "หน้า 40 PSI / หลัง 50-55 PSI (ขนส่งผู้โดยสาร)",
            sparkPlugOrGlowSpec = "Toyota Ceramic Glow Plug 11V",
            commonIssuesTh = listOf("ความร้อนขึ้นเมื่อวิ่งขึ้นเขาบรรทุกหนัก", "ผ้าเบรกหน้าหมดไว", "วาล์ว SCV ติดขัดเมื่อวิ่งเกิน 200,000 กม."),
            commonIssuesEn = listOf("Engine overheating under high load hill climbs", "Front brake pad rapid wear", "SCV valve sticking high mileage")
        ),
        ThaiCarSpec(
            id = "toyota_corolla_cross",
            make = "Toyota",
            model = "Corolla Cross / Hybrid / GR Sport",
            generationTh = "โฉม Cross TNGA Platform (2020 - ปัจจุบัน)",
            yearRange = "2020 - 2026",
            engineCode = "2ZR-FBE (1.8L) / 2ZR-FXE (1.8L Hybrid)",
            engineDisplacement = "1,798 cc",
            horsepower = "122 - 140 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / Hybrid",
            engineCategory = ThaiEngineCategory.HYBRID_HEV_PHEV,
            baseline = VehicleParameterBaseline(idleRpmMin = 800, idleRpmMax = 1200, normalFuelRailPressureMinBar = 3.8f, normalFuelRailPressureMaxBar = 4.2f),
            vinPrefixes = listOf("MR0BG", "MR0"),
            obdLocationTh = "ใต้พวงมาลัย เหนือคันเร่ง",
            obdLocationEn = "Under driver dash above gas pedal",
            engineOilSpec = "Toyota Genuine 0W-16 / 0W-20 SP",
            oilCapacityLiters = 4.2f,
            transmissionFluidSpec = "Toyota e-CVT Fluid / CVT Fluid FE",
            coolantCapacityLiters = 5.6f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 33 PSI",
            sparkPlugOrGlowSpec = "Denso SC20HR11",
            commonIssuesTh = listOf("กลิ่นภายนอกเข้าห้องโดยสารเมื่อขับตามรถควันดำ", "เสียงลมเข้าขอบกระจกหน้าข้าง", "แบตเตอรี่ 12V ไฟตกสตาร์ทไม่ติด"),
            commonIssuesEn = listOf("Outside odor intrusion through climate flap", "Wind noise at front door seal", "12V auxiliary battery discharge")
        ),

        // ==================== ISUZU ====================
        ThaiCarSpec(
            id = "isuzu_dmax_bluepower",
            make = "Isuzu",
            model = "D-Max / V-Cross / Hi-Lander",
            generationTh = "โฉม Blue Power / X-Series (2016 - ปัจจุบัน)",
            yearRange = "2016 - 2026",
            engineCode = "RZ4E-TC (1.9 Ddi) / 4JJ3-TCX (3.0 Ddi)",
            engineDisplacement = "1,898 - 2,999 cc",
            horsepower = "150 - 190 hp / 450 Nm",
            fuelType = "ดีเซล (Diesel B7/B10/B20)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 720, idleRpmMax = 820, normalFuelRailPressureMinBar = 320f, normalFuelRailPressureMaxBar = 2000f, normalTurboBoostMaxBar = 1.75f),
            vinPrefixes = listOf("MP1", "MP2", "MP1TC", "MP1TF"),
            obdLocationTh = "ใต้พวงมาลัย ด้านขวาใกล้แผงฟิวส์ในห้องโดยสาร",
            obdLocationEn = "Under steering wheel, right side near fuse box panel",
            engineOilSpec = "Isuzu Besco Clean Eco 0W-20 / 5W-30 API CK-4",
            oilCapacityLiters = 6.2f,
            transmissionFluidSpec = "Isuzu Besco ATF W-3 / Transaxle 5W-30",
            coolantCapacityLiters = 7.8f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 35-42 PSI",
            sparkPlugOrGlowSpec = "หัวเผา Isuzu Genuine Glow Plug 4V/11V",
            commonIssuesTh = listOf("วาล์ว SCV ท้ายปั๊มติดขัดในรุ่นเกิน 150,000 กม.", "เซนเซอร์แอร์โฟร์สกปรกส่งผลให้อัตราเร่งวูบ", "สายแวคคั่มคุมแปรผันเทอร์โบปริแตก"),
            commonIssuesEn = listOf("SCV suction valve sticking", "Dirty MAF sensor low-end hesitation", "VNT turbocharger vacuum actuator hose split")
        ),
        ThaiCarSpec(
            id = "isuzu_dmax_gen1",
            make = "Isuzu",
            model = "D-Max / MU-7",
            generationTh = "โฉมไฟข้าวข้าวต้ม / ไฟข้าวเหนียว / i-TEQ (2002 - 2011)",
            yearRange = "2002 - 2011",
            engineCode = "4JH1-TC (3.0 Di) / 4JK1-TC (2.5 Ddi) / 4JJ1-TCX (3.0 Ddi)",
            engineDisplacement = "2,499 - 2,999 cc",
            horsepower = "120 - 163 hp",
            fuelType = "ดีเซล (Diesel B7)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 250f, normalFuelRailPressureMaxBar = 1600f, normalTurboBoostMaxBar = 1.2f),
            vinPrefixes = listOf("MP1T", "MP1S", "MP14"),
            obdLocationTh = "ใต้คอนโซลฝั่งขวาติดกับคันดึงเปิดฝากระโปรงหน้า",
            obdLocationEn = "Under right console near hood release handle",
            engineOilSpec = "Isuzu Besco Multi 15W-40 / 10W-30",
            oilCapacityLiters = 7.0f,
            transmissionFluidSpec = "Besco ATF III / Manual Gear Oil 5W-30",
            coolantCapacityLiters = 8.5f,
            tirePressurePsi = "หน้า 30 PSI / หลัง 35 PSI",
            sparkPlugOrGlowSpec = "Isuzu 11V Metal Glow Plug",
            commonIssuesTh = listOf("วาล์ว SCV ค้างยาวเร่งไม่ขึ้นเข้า Limp Mode", "กล่อง EDU คุมหัวฉีดลายวงจรหลุดร่อน", "เซนเซอร์วัดแรงดันคอมมอนเรลชำรุด"),
            commonIssuesEn = listOf("SCV valve sticking causing limp mode", "Injector EDU driver module circuit solder fracture", "Common rail pressure sensor drift")
        ),
        ThaiCarSpec(
            id = "isuzu_mux",
            make = "Isuzu",
            model = "MU-X",
            generationTh = "โฉม Gen 1 & Gen 2 The Ultimate (2013 - ปัจจุบัน)",
            yearRange = "2013 - 2026",
            engineCode = "RZ4E-TC (1.9L) / 4JJ3-TCX (3.0L) / 4JJ1-TCX",
            engineDisplacement = "1,898 - 2,999 cc",
            horsepower = "150 - 190 hp",
            fuelType = "ดีเซล (Diesel B7/B10/B20)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 720, idleRpmMax = 820, normalFuelRailPressureMinBar = 320f, normalFuelRailPressureMaxBar = 2000f),
            vinPrefixes = listOf("MP1", "MP2", "MP1TF"),
            obdLocationTh = "ใต้พวงมาลัย ด้านขวาใกล้แผงฟิวส์",
            obdLocationEn = "Under steering column right side near fuse block",
            engineOilSpec = "Isuzu Besco Clean Eco 0W-20 / 5W-30",
            oilCapacityLiters = 6.2f,
            transmissionFluidSpec = "Isuzu Besco ATF W-3 6-Speed",
            coolantCapacityLiters = 8.0f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 32 PSI",
            sparkPlugOrGlowSpec = "Isuzu Genuine Glow Plug 11V",
            commonIssuesTh = listOf("สวิตช์แรงดันน้ำมันเครื่องโชว์เมื่อใช้น้ำมันเครื่องหนืดไป", "ไฟเตือนดักน้ำโชว์", "เพลาขับหลังสั่นช่วงออกตัว"),
            commonIssuesEn = listOf("Oil pressure switch warning with heavy oil", "Water-in-fuel indicator alert", "Rear propeller shaft shudder on initial launch")
        ),

        // ==================== HONDA ====================
        ThaiCarSpec(
            id = "honda_civic_turbo_ehev",
            make = "Honda",
            model = "Civic (FD / FB / FC / FK / FE)",
            generationTh = "โฉมนางฟ้า / FB / FC / FE (2006 - ปัจจุบัน)",
            yearRange = "2006 - 2026",
            engineCode = "L15BG 1.5 VTEC Turbo / LFB 2.0 e:HEV / R18A 1.8L",
            engineDisplacement = "1,498 - 1,993 cc",
            horsepower = "141 - 182 hp",
            fuelType = "เบนซิน / แก๊สโซฮอล์ 95 / E20 / E85 / Hybrid",
            engineCategory = ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 40f, normalFuelRailPressureMaxBar = 200f, normalTurboBoostMaxBar = 1.35f),
            vinPrefixes = listOf("MHRFC", "MHRFE", "MHRFK", "MHR"),
            obdLocationTh = "ใต้คอนโซลฝั่งคนขับ เหนือคันโยกเปิดฝากระโปรงหน้า (สีขาว 16-pin)",
            obdLocationEn = "Under driver console right above hood release latch (White 16-pin)",
            engineOilSpec = "Honda Ultimate SN 0W-20 Fully Synthetic",
            oilCapacityLiters = 3.8f,
            transmissionFluidSpec = "Honda HCF-2 (เกียร์ CVT) / e-CVT Fluid",
            coolantCapacityLiters = 5.2f,
            tirePressurePsi = "หน้า 32-33 PSI / หลัง 32-33 PSI",
            sparkPlugOrGlowSpec = "NGK Laser Iridium ILZKAR8H8S / DILKAR7G11GS",
            commonIssuesTh = listOf("คอมเพรสเซอร์แอร์รั่วตรงซีลข้อต่อ", "คอยล์จุดระเบิดรั่วในรุ่นเทอร์โบเมื่อใช้เกิน 80,000 กม.", "แร็คพวงมาลัยไฟฟ้ามีเสียงดังกึกๆ"),
            commonIssuesEn = listOf("AC compressor shaft seal leak", "Ignition coil breakdown under boost", "EPS steering rack knocking noise")
        ),
        ThaiCarSpec(
            id = "honda_city_turbo",
            make = "Honda",
            model = "City / City Hatchback / e:HEV",
            generationTh = "โฉม ZX / GM2 / GM6 / GN1 (2002 - ปัจจุบัน)",
            yearRange = "2002 - 2026",
            engineCode = "L10RS 1.0L VTEC Turbo / LEB 1.5L e:HEV / L15Z1 1.5L",
            engineDisplacement = "988 - 1,498 cc",
            horsepower = "109 - 122 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / Hybrid",
            engineCategory = ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 35f, normalFuelRailPressureMaxBar = 180f, normalTurboBoostMaxBar = 1.1f),
            vinPrefixes = listOf("MHRGN", "MHRGM", "MHRGD"),
            obdLocationTh = "ใต้คอพวงมาลัยติดกับแผงฟิวส์ใต้คอนโซลฝั่งขวา",
            obdLocationEn = "Under steering column next to fuse panel",
            engineOilSpec = "Honda Genuine 0W-20 SN/SP",
            oilCapacityLiters = 3.5f,
            transmissionFluidSpec = "Honda HCF-2 CVT Fluid",
            coolantCapacityLiters = 4.5f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "NGK DILKAR7G11GS Iridium",
            commonIssuesTh = listOf("สายพานทุ่มน้ำมันเครื่องในรุ่น 1.0 Turbo เปื่อยหลุดเปรอะถาดน้ำมัน", "ไฟเตือน P0171 เมื่อเติม E85 คุณภาพต่ำ", "ลูกปืนล้อหลังหอน"),
            commonIssuesEn = listOf("1.0 Turbo wet timing belt rubber flaking", "P0171 Lean code with low quality ethanol fuel", "Rear hub bearing noise")
        ),
        ThaiCarSpec(
            id = "honda_hrv",
            make = "Honda",
            model = "HR-V / HR-V e:HEV",
            generationTh = "โฉม RU / RV (2014 - ปัจจุบัน)",
            yearRange = "2014 - 2026",
            engineCode = "LEC 1.5L e:HEV Hybrid / R18Z1 1.8L i-VTEC",
            engineDisplacement = "1,498 - 1,799 cc",
            horsepower = "131 - 141 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / E85 / Hybrid",
            engineCategory = ThaiEngineCategory.HYBRID_HEV_PHEV,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 950, normalFuelRailPressureMinBar = 3.8f, normalFuelRailPressureMaxBar = 4.2f),
            vinPrefixes = listOf("MHRRV", "MHRRU", "MHR"),
            obdLocationTh = "ใต้แผงคอนโซลพวงมาลัยฝั่งขวา",
            obdLocationEn = "Under steering column right side dash panel",
            engineOilSpec = "Honda Ultimate 0W-20 Fully Synthetic",
            oilCapacityLiters = 3.7f,
            transmissionFluidSpec = "Honda HCF-2 / e-CVT Fluid",
            coolantCapacityLiters = 5.0f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "NGK IZFR6K11S / DILKAR7G11GS",
            commonIssuesTh = listOf("ปั๊มเบรก ABS ทำงานมีเสียงดังปิ๊ดๆ", "หลังคาซันรูฟเสียงลมเข้าในรุ่น EL", "คอมแอร์มีเสียงตัดต่อดังแรง"),
            commonIssuesEn = listOf("ABS accumulator pump squeak sound", "Panoramic sunroof wind seal noise", "AC clutch engagement click noise")
        ),
        ThaiCarSpec(
            id = "honda_crv",
            make = "Honda",
            model = "CR-V / Turbo / e:HEV / DT",
            generationTh = "โฉม RD / RE / RM / RW / RS (2002 - ปัจจุบัน)",
            yearRange = "2002 - 2026",
            engineCode = "L15BG 1.5L Turbo / LFB 2.0L e:HEV / N22B 1.6L i-DTEC / K24A / R20A",
            engineDisplacement = "1,498 - 2,354 cc",
            horsepower = "150 - 193 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / ดีเซล B7 / Hybrid",
            engineCategory = ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 38f, normalFuelRailPressureMaxBar = 200f),
            vinPrefixes = listOf("MHRRW", "MHRRM", "MHRRE"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา เหนือแป้นเบรก",
            obdLocationEn = "Under steering column above brake pedal",
            engineOilSpec = "Honda Genuine 0W-20 SN / ACEA C3 (ดีเซล)",
            oilCapacityLiters = 4.2f,
            transmissionFluidSpec = "Honda HCF-2 / ATF DW-1 / 9-Speed ATF",
            coolantCapacityLiters = 6.5f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 33 PSI",
            sparkPlugOrGlowSpec = "NGK DILKAR7G11GS / Glow Plug i-DTEC",
            commonIssuesTh = listOf("หน้าคลัตช์คอมแอร์ห่างในรุ่น G3/G4 ทำให้แอร์ไม่เย็น", "ระบบเกียร์ 9-Speed ดีเซลเปลี่ยนเกียร์สะดุด", "ปั๊มเชื้อเพลิงไฮเพรสเชอร์เสียงดัง"),
            commonIssuesEn = listOf("G3/G4 AC compressor clutch clearance wide gap", "9-speed diesel transmission shift jerkiness", "High pressure fuel pump ticking noise")
        ),
        ThaiCarSpec(
            id = "honda_jazz",
            make = "Honda",
            model = "Jazz / Fit (GD / GE / GK)",
            generationTh = "โฉม Jazz GD (i-DSI/VTEC) / GE / GK (2003 - 2022)",
            yearRange = "2003 - 2022",
            engineCode = "L15A7 (1.5L i-VTEC) / L15Z1 / L13A (i-DSI 8 หัวเทียน)",
            engineDisplacement = "1,339 - 1,497 cc",
            horsepower = "88 - 117 hp",
            fuelType = "แก๊สโซฮอล์ 95 / 91 / E20 / E85",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 680, idleRpmMax = 780, normalFuelRailPressureMinBar = 3.5f, normalFuelRailPressureMaxBar = 3.8f, normalTurboBoostMaxBar = 0.0f),
            vinPrefixes = listOf("MHRGK", "MHRGE", "MHRGD"),
            obdLocationTh = "ใต้คอนโซลฝั่งคนขับ ติดกับคอพวงมาลัย",
            obdLocationEn = "Under driver console right near steering column",
            engineOilSpec = "Honda SN 0W-20 / 5W-30 Semi-Synthetic",
            oilCapacityLiters = 3.6f,
            transmissionFluidSpec = "Honda HCF-2 (GK) / ATF Z1 / HMMF (GD/GE)",
            coolantCapacityLiters = 4.8f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "NGK BKR6E-11 (GD) / IZFR6K13 (GE/GK)",
            commonIssuesTh = listOf("เกียร์ CVT กระตุกรอบกระชากหากใช้น้ำมันเกียร์ผิดประเภท (GD)", "คอยล์จุดระเบิดหลังรั่วในเครื่อง i-DSI", "ปั๊มน้ำรั่วซึม"),
            commonIssuesEn = listOf("GD CVT shudder with wrong fluid grade", "Rear ignition coil breakdown on 8-plug i-DSI", "Engine water pump coolant weep")
        ),
        ThaiCarSpec(
            id = "honda_accord",
            make = "Honda",
            model = "Accord / Hybrid / e:HEV",
            generationTh = "โฉม ปลาวาฬ (G7) / G8 / G9 / G10 / G11 (2003 - ปัจจุบัน)",
            yearRange = "2003 - 2026",
            engineCode = "L15BG 1.5L Turbo / LFB 2.0L e:HEV / K24A / R20A / J35A V6",
            engineDisplacement = "1,498 - 3,471 cc",
            horsepower = "150 - 275 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / E85 / Hybrid",
            engineCategory = ThaiEngineCategory.HYBRID_HEV_PHEV,
            baseline = VehicleParameterBaseline(idleRpmMin = 650, idleRpmMax = 800, normalFuelRailPressureMinBar = 38f, normalFuelRailPressureMaxBar = 200f),
            vinPrefixes = listOf("MHRCV", "MHRCR", "MHRCP", "MHRCM"),
            obdLocationTh = "ใต้คอนโซลพวงมาลัยฝั่งขวา",
            obdLocationEn = "Under driver steering column right side",
            engineOilSpec = "Honda Ultimate 0W-20 Fully Synthetic",
            oilCapacityLiters = 4.2f,
            transmissionFluidSpec = "Honda HCF-2 / e-CVT Fluid / ATF DW-1",
            coolantCapacityLiters = 6.2f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 33 PSI",
            sparkPlugOrGlowSpec = "NGK DILKAR7G11GS Iridium",
            commonIssuesTh = listOf("ปั๊มพาวเวอร์และสายฉีดน้ำมันพาวเวอร์รั่วในรุ่น G7/G8", "ปั๊มเบรกไฟฟ้า VSA มีเสียงดังปิ๊ด", "ปั๊มเชื้อเพลิงแรงดันสูงในรุ่น 1.5 Turbo"),
            commonIssuesEn = listOf("G7/G8 power steering pump hose leak", "VSA modulator pump noise", "High pressure direct injection pump wear")
        ),

        // ==================== FORD ====================
        ThaiCarSpec(
            id = "ford_ranger_everest",
            make = "Ford",
            model = "Ranger / Everest / Raptor",
            generationTh = "โฉม T6 / T6 Facelift / Next-Gen (2012 - ปัจจุบัน)",
            yearRange = "2012 - 2026",
            engineCode = "2.0L EcoBlue Single/Bi-Turbo / 3.0L V6 / 2.2L Duratorq",
            engineDisplacement = "1,996 - 2,993 cc",
            horsepower = "160 - 250 hp / 600 Nm",
            fuelType = "ดีเซล (Diesel B7/B20)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 300f, normalFuelRailPressureMaxBar = 2200f, normalTurboBoostMaxBar = 1.9f),
            vinPrefixes = listOf("AHT", "MNB", "AHTAA", "AHTBA"),
            obdLocationTh = "เปิดฝาช่องเก็บเหรียญ/กล่องฟิวส์ใต้พวงมาลัยฝั่งขวา พอร์ตอยู่ด้านใน",
            obdLocationEn = "Behind coin drawer/fuse cover door to the right below steering wheel",
            engineOilSpec = "Ford-Castrol Magnatec Professional 0W-30 / 5W-30 WSS-M2C950-A",
            oilCapacityLiters = 7.0f,
            transmissionFluidSpec = "Ford Mercon ULV (เกียร์ 10 สปีด) / Mercon LV (6 สปีด)",
            coolantCapacityLiters = 9.2f,
            tirePressurePsi = "หน้า 34-36 PSI / หลัง 38-42 PSI",
            sparkPlugOrGlowSpec = "Ford Genuine Ceramic Glow Plug",
            commonIssuesTh = listOf("ท่ออินเตอร์เทอร์โบคู่แตก (Bi-turbo silicone boot split)", "วาล์ว EGR ตันในรุ่น 2.0 Bi-Turbo", "โซลินอยด์เกียร์ 10 สปีด Solenoid shift delay", "เซนเซอร์ DPF ตัน"),
            commonIssuesEn = listOf("Bi-Turbo silicone intercooler charge pipe crack", "EGR cooler valve clogging", "10-Speed automatic transmission valve body solenoid adaptation", "DPF differential pressure sensor blockage")
        ),
        ThaiCarSpec(
            id = "ford_fiesta",
            make = "Ford",
            model = "Fiesta",
            generationTh = "โฉม Fiesta Hatchback / Sedan (2010 - 2019)",
            yearRange = "2010 - 2019",
            engineCode = "1.5L / 1.6L Ti-VCT / 1.0L EcoBoost",
            engineDisplacement = "998 - 1,596 cc",
            horsepower = "112 - 125 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 3.8f, normalFuelRailPressureMaxBar = 4.2f),
            vinPrefixes = listOf("MNB", "AHT"),
            obdLocationTh = "ช่องซ้ายใต้แผงควบคุมแอร์ฝั่งคนขับ ข้างใต้พวงมาลัย",
            obdLocationEn = "Left compartment below AC controls near steering shaft",
            engineOilSpec = "Ford Formula F 5W-30 WSS-M2C913-C",
            oilCapacityLiters = 4.0f,
            transmissionFluidSpec = "Ford Powershift Dual-Clutch Transmission Fluid (TCM)",
            coolantCapacityLiters = 5.5f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "Ford FoMoCo Copper/Iridium Plug",
            commonIssuesTh = listOf("ชุดคลัตช์และกล่อง TCM เกียร์ Powershift Dual-Clutch พังค้าง", "ท่อน้ำระบายความร้อนข้างเครื่องแตก", "มอเตอร์พัดลมหม้อน้ำไหม้"),
            commonIssuesEn = listOf("Powershift Dual-Clutch TCM module and clutch chatter failure", "Engine coolant bypass hose cracking", "Radiator fan motor resistor burn out")
        ),
        ThaiCarSpec(
            id = "ford_focus",
            make = "Ford",
            model = "Focus",
            generationTh = "โฉม Mk2 / Mk3 Ti-VCT / EcoBoost (2005 - 2018)",
            yearRange = "2005 - 2018",
            engineCode = "2.0L GDI Direct Injection / 1.5L EcoBoost / 1.8L Duratec",
            engineDisplacement = "1,498 - 1,999 cc",
            horsepower = "125 - 180 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20",
            engineCategory = ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 30f, normalFuelRailPressureMaxBar = 150f),
            vinPrefixes = listOf("AHT", "MNB"),
            obdLocationTh = "เปิดเก๊ะเล็กฝั่งขวาใต้คอพวงมาลัย พอร์ตอยู่ด้านใน",
            obdLocationEn = "Inside small coin cubby right of steering wheel",
            engineOilSpec = "Ford WSS-M2C948-B 5W-20 / 5W-30",
            oilCapacityLiters = 4.3f,
            transmissionFluidSpec = "Ford DPS6 Dual Clutch Fluid",
            coolantCapacityLiters = 6.2f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 31 PSI",
            sparkPlugOrGlowSpec = "Ford SP-530 Iridium",
            commonIssuesTh = listOf("เกียร์ DPS6 Dual Clutch สั่นกระตุกเข้าเกียร์ถอยหลังไม่ได้", "กล่อง TCM ร้อนจนชิปตัดการทำงาน", "ถังดักไอเบนซิน EVAP Solenoid ค้าง"),
            commonIssuesEn = listOf("DPS6 Dual Clutch transmission shudder reverse gear lockout", "TCM chip thermal shutdown", "EVAP purge valve stuck open causing rough idle")
        ),

        // ==================== MITSUBISHI ====================
        ThaiCarSpec(
            id = "mitsubishi_pajero_triton",
            make = "Mitsubishi",
            model = "Triton / Pajero Sport / Athlete",
            generationTh = "โฉม All-New & Athlete (2005 - ปัจจุบัน)",
            yearRange = "2005 - 2026",
            engineCode = "4N15 2.4L MIVEC / 4N16 Hyper Power (2024+) / 4D56 2.5L",
            engineDisplacement = "2,442 - 2,477 cc",
            horsepower = "181 - 204 hp / 470 Nm",
            fuelType = "ดีเซล (Diesel B7/B10/B20)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 300f, normalFuelRailPressureMaxBar = 2000f, normalTurboBoostMaxBar = 1.7f),
            vinPrefixes = listOf("MMT", "MMA", "MMTGN", "MMTJR"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา ตรงมุมใกล้คันดึงเปิดฝาถังน้ำมัน",
            obdLocationEn = "Under steering column right corner near fuel lid release pull",
            engineOilSpec = "Mitsubishi Eco Diesel DL-1 / API CK-4 5W-30",
            oilCapacityLiters = 8.3f,
            transmissionFluidSpec = "Mitsubishi ATF-MA1 / Diamond ATF SP-III",
            coolantCapacityLiters = 8.5f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 35-40 PSI",
            sparkPlugOrGlowSpec = "Mitsubishi OEM Glow Plug 12V",
            commonIssuesTh = listOf("น้ำยาแอร์ขาดจากตู้แอร์รั่วตามอายุ", "ลูกปืนประคองเพลากลางสั่นช่วงเร่ง 80-100 กม./ชม.", "วาล์วระบายแรงดัน SCV ตันน้ำมันสกปรก"),
            commonIssuesEn = listOf("Evaporator core micro-leaks", "Driveshaft center support bearing vibration", "SCV suction valve sticking from fuel contaminants")
        ),
        ThaiCarSpec(
            id = "mitsubishi_mirage_attrage",
            make = "Mitsubishi",
            model = "Mirage / Attrage",
            generationTh = "Eco Car โฉมแรก - ปัจจุบัน (2012 - 2026)",
            yearRange = "2012 - 2026",
            engineCode = "3A92 (1.2L MIVEC 3-Cylinder)",
            engineDisplacement = "1,193 cc",
            horsepower = "78 hp",
            fuelType = "แก๊สโซฮอล์ 95 / 91 / E20",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 3.5f, normalFuelRailPressureMaxBar = 3.8f, normalTurboBoostMaxBar = 0.0f),
            vinPrefixes = listOf("MMTA", "MMTB"),
            obdLocationTh = "ใต้คอนโซลหน้าฝั่งขวา ติดกับแกนพวงมาลัย",
            obdLocationEn = "Under right dash panel near steering column",
            engineOilSpec = "Mitsubishi Genuine Eco Oil 0W-20 SN",
            oilCapacityLiters = 3.0f,
            transmissionFluidSpec = "Mitsubishi DiaQueen CVTF-J4",
            coolantCapacityLiters = 4.2f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "NGK DILFR5A11 Iridium",
            commonIssuesTh = listOf("ยางแท่นเครื่องฝั่งขวาทรุดตัวสั่นสะท้านมาก", "พัดลมหม้อน้ำหมุนช้าทำความร้อนขึ้นสูง", "สายพานไดชาร์จมีเสียงดังร้องแป๊วๆ"),
            commonIssuesEn = listOf("Right engine mount sag causing severe idle shake", "Radiator fan motor speed drop overheat", "Alternator serpentine belt chirp")
        ),
        ThaiCarSpec(
            id = "mitsubishi_xpander",
            make = "Mitsubishi",
            model = "Xpander / Xpander Cross / HEV",
            generationTh = "โฉม Xpander / Cross / e:Motion HEV (2018 - ปัจจุบัน)",
            yearRange = "2018 - 2026",
            engineCode = "4A91 (1.5L MIVEC) / 4A92 1.6L Hybrid",
            engineDisplacement = "1,499 cc",
            horsepower = "105 - 116 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / Hybrid",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 3.5f, normalFuelRailPressureMaxBar = 3.8f),
            vinPrefixes = listOf("MMT", "MK2"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งคนขับ เหนือแป้นคันเร่ง",
            obdLocationEn = "Under driver side steering column above gas pedal",
            engineOilSpec = "Mitsubishi Genuine 0W-20 Fully Synthetic",
            oilCapacityLiters = 4.0f,
            transmissionFluidSpec = "Mitsubishi CVTF-J4 / Eco CVT Fluid",
            coolantCapacityLiters = 5.2f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 35 PSI",
            sparkPlugOrGlowSpec = "NGK LZFR6AI Iridium",
            commonIssuesTh = listOf("โช้คอัพหลังมีน้ำมันซึมตามซึมรั่วก่อนกำหนด", "เสียงคอมแอร์ดังขณะทำงานจอดนิ่ง", "น้ำมันเกียร์ CVT เสื่อมสภาพเร็วเมื่อบรรทุกหนัก"),
            commonIssuesEn = listOf("Rear shock absorber oil weeping", "AC compressor engagement noise at standstill", "CVT fluid thermal shear under heavy passenger loading")
        ),
        ThaiCarSpec(
            id = "mitsubishi_lancer",
            make = "Mitsubishi",
            model = "Lancer / Lancer EX",
            generationTh = "โฉม Cedia / ท้ายเบนซ์ / Lancer EX (2001 - 2017)",
            yearRange = "2001 - 2017",
            engineCode = "4B10 (1.8L) / 4B11 (2.0L MIVEC) / 4G18 (1.6L)",
            engineDisplacement = "1,584 - 1,998 cc",
            horsepower = "108 - 154 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / E85",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 650, idleRpmMax = 750, normalFuelRailPressureMinBar = 3.5f, normalFuelRailPressureMaxBar = 3.8f),
            vinPrefixes = listOf("MMT", "JMB"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวาติดกับแผงฟิวส์",
            obdLocationEn = "Under right dash panel near fuse box",
            engineOilSpec = "5W-30 / 10W-40 API SN",
            oilCapacityLiters = 4.3f,
            transmissionFluidSpec = "Mitsubishi DiaQueen CVTF-J1 / ATF SP-III",
            coolantCapacityLiters = 6.0f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "NGK FR6EI Iridium",
            commonIssuesTh = listOf("เกียร์ CVT ร้อนจัดขึ้นโชว์ Slow Down บนหน้าปัดขณะวิ่งความเร็วสูงยาวๆ", "ปั๊มพาวเวอร์รั่วซึม", "ลูกปืนล้อหน้าหอน"),
            commonIssuesEn = listOf("CVT fluid overheat Slow Down warning on high speed highway cruise", "Power steering pump seal leak", "Front wheel bearing wear")
        ),

        // ==================== NISSAN ====================
        ThaiCarSpec(
            id = "nissan_navara",
            make = "Nissan",
            model = "Navara / NP300 / PRO-4X",
            generationTh = "โฉม D40 / D23 NP300 / Minorchange (2007 - ปัจจุบัน)",
            yearRange = "2007 - 2026",
            engineCode = "YS23DDTT (2.3L Twin Turbo) / YD25DDTi (2.5L Turbo)",
            engineDisplacement = "2,298 - 2,488 cc",
            horsepower = "163 - 190 hp / 450 Nm",
            fuelType = "ดีเซล (Diesel B7/B10/B20)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 300f, normalFuelRailPressureMaxBar = 2000f, normalTurboBoostMaxBar = 1.8f),
            vinPrefixes = listOf("MNT", "MNTC", "MNTD"),
            obdLocationTh = "ใต้แผงคอนโซลฝั่งคนขับ ด้านขวาติดคันดึงเปิดฝากระโปรง",
            obdLocationEn = "Under driver side console right side near hood release",
            engineOilSpec = "Nissan Genuine 5W-30 ACEA C3 / API CK-4",
            oilCapacityLiters = 6.6f,
            transmissionFluidSpec = "Nissan Matic-S (เกียร์ออโต้ 7 สปีด) / MT Fluid 75W-85",
            coolantCapacityLiters = 8.2f,
            tirePressurePsi = "หน้า 35 PSI / หลัง 40 PSI",
            sparkPlugOrGlowSpec = "Nissan Metal Glow Plug 11V",
            commonIssuesTh = listOf("สายโซ่ราวลิ้นยืดมีเสียงดังแกร๊กๆ ในรุ่น D40 YD25", "อินเตอร์คูลเลอร์รั่วตามรอยตะเข็บ", "วาล์ว SCV ท้ายปั๊มติดขัด"),
            commonIssuesEn = listOf("D40 YD25 timing chain stretch and slap rattle", "Intercooler crimp seam oil/air leak", "SCV suction control valve sticking")
        ),
        ThaiCarSpec(
            id = "nissan_almera",
            make = "Nissan",
            model = "Almera",
            generationTh = "Eco Car โฉม N17 (1.2L) / N18 (1.0L Turbo) (2011 - ปัจจุบัน)",
            yearRange = "2011 - 2026",
            engineCode = "HRA0 (1.0L 3-Cylinder Turbo) / HR12DE (1.2L 3-Cylinder)",
            engineDisplacement = "999 - 1,198 cc",
            horsepower = "79 - 100 hp",
            fuelType = "แก๊สโซฮอล์ 95 / 91 / E20",
            engineCategory = ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 3.8f, normalFuelRailPressureMaxBar = 150f, normalTurboBoostMaxBar = 0.95f),
            vinPrefixes = listOf("MNTAL", "MNTN18", "MNTN17"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา พอร์ตอยู่ขอบล่างใต้ช่องแอร์ฝั่งขวา",
            obdLocationEn = "Under steering column right side below air vent trim",
            engineOilSpec = "Nissan Genuine 0W-20 SN Fully Synthetic",
            oilCapacityLiters = 3.6f,
            transmissionFluidSpec = "Nissan NS-3 CVT Fluid",
            coolantCapacityLiters = 4.8f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "NGK ILKAR7L11 Iridium",
            commonIssuesTh = listOf("เวสต์เกตเทอร์โบหลวมมีเสียงดังก๊องแก๊งในรุ่น 1.0 Turbo", "พัดลมหม้อน้ำเสื่อมสภาพ", "คอมแอร์ไม่ทำงานเมื่อจอดตากแดดร้อนจัด"),
            commonIssuesEn = listOf("1.0 Turbo electronic wastegate linkage rattle", "Radiator cooling fan motor brush wear", "AC compressor thermal cut-out in hot ambient")
        ),
        ThaiCarSpec(
            id = "nissan_kicks_epower",
            make = "Nissan",
            model = "Kicks e-POWER",
            generationTh = "โฉม e-POWER (2020 - ปัจจุบัน)",
            yearRange = "2020 - 2026",
            engineCode = "HR12DE (1.2L Generator) + EM57 Electric Drive Motor",
            engineDisplacement = "1,198 cc (Generator)",
            horsepower = "136 hp / 280 Nm (Electric Drive)",
            fuelType = "แก๊สโซฮอล์ 95 / E20 (ระบบขับเคลื่อนด้วยไฟฟ้า 100%)",
            engineCategory = ThaiEngineCategory.HYBRID_HEV_PHEV,
            baseline = VehicleParameterBaseline(idleRpmMin = 1200, idleRpmMax = 2200, normalFuelRailPressureMinBar = 3.5f, normalFuelRailPressureMaxBar = 3.8f),
            vinPrefixes = listOf("MNTP15", "MNT"),
            obdLocationTh = "ใต้คอนโซลพวงมาลัยฝั่งขวา",
            obdLocationEn = "Under driver console right side",
            engineOilSpec = "Nissan Genuine 0W-20 SN",
            oilCapacityLiters = 3.4f,
            transmissionFluidSpec = "Nissan Matic-S Fluid (Single Reduction Gear)",
            coolantCapacityLiters = 5.5f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "NGK DILKAR7GX11P",
            commonIssuesTh = listOf("แบตเตอรี่ 12V ลูกเล็กสำหรับระบบกล่องสตาร์ทหมดไว", "เสียงเครื่องยนต์ปั่นไฟดังช่วงแบตเตอรี่ตกลดต่ำ", "คอมแอร์ไฟฟ้าตัดการทำงานไฟโชว์"),
            commonIssuesEn = listOf("12V auxiliary starter battery rapid voltage drop", "ICE generator engine high RPM roar during low battery SoC", "Electric inverter AC compressor fault code")
        ),
        ThaiCarSpec(
            id = "nissan_march",
            make = "Nissan",
            model = "March (K13)",
            generationTh = "Eco Car รุ่นแรกของไทย (2010 - 2023)",
            yearRange = "2010 - 2023",
            engineCode = "HR12DE (1.2L 3-Cylinder)",
            engineDisplacement = "1,198 cc",
            horsepower = "79 hp",
            fuelType = "แก๊สโซฮอล์ 95 / 91 / E20",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 3.5f, normalFuelRailPressureMaxBar = 3.8f),
            vinPrefixes = listOf("MNTK13", "MNT"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา ติดกับแผงฟิวส์",
            obdLocationEn = "Under steering column right side near fuse block",
            engineOilSpec = "Nissan Genuine 0W-20 / 5W-30",
            oilCapacityLiters = 3.0f,
            transmissionFluidSpec = "Nissan NS-2 / NS-3 CVT Fluid",
            coolantCapacityLiters = 4.5f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "NGK DILKAR6A11",
            commonIssuesTh = listOf("พัดลมหม้อน้ำไหม้ทำให้ความร้อนขึ้นดับกลางทาง", "พูกก์ปั๊มน้ำสั่นมีเสียงดัง", "ลูกปืนล้อหลังหอน"),
            commonIssuesEn = listOf("Radiator cooling fan motor failure causing overheat stall", "Water pump pulley wobble noise", "Rear wheel hub bearing humming")
        ),

        // ==================== MAZDA ====================
        ThaiCarSpec(
            id = "mazda2",
            make = "Mazda",
            model = "Mazda 2 / Skyactiv-G / Skyactiv-D",
            generationTh = "โฉม DE / DJ Skyactiv (2009 - ปัจจุบัน)",
            yearRange = "2009 - 2026",
            engineCode = "Skyactiv-G 1.3L / Skyactiv-D 1.5L Diesel / MZR 1.5L",
            engineDisplacement = "1,299 - 1,499 cc",
            horsepower = "93 - 105 hp",
            fuelType = "เบนซิน E20 / ดีเซล B7",
            engineCategory = ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION,
            baseline = VehicleParameterBaseline(idleRpmMin = 650, idleRpmMax = 750, normalFuelRailPressureMinBar = 30f, normalFuelRailPressureMaxBar = 180f),
            vinPrefixes = listOf("MM8DJ", "MM8DE", "MM8"),
            obdLocationTh = "ใต้พวงมาลัย ด้านซ้ายใกล้แผงฟิวส์ในห้องโดยสาร",
            obdLocationEn = "Under steering column left side near interior fuse cover",
            engineOilSpec = "Mazda Genuine Supra 0W-20 / Supra D 0W-30 (ดีเซล)",
            oilCapacityLiters = 4.2f,
            transmissionFluidSpec = "Mazda ATF FZ (เกียร์ออโต้ 6 สปีด)",
            coolantCapacityLiters = 5.5f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "Mazda PE5R-18-110 Iridium / Glow Plug Skyactiv-D",
            commonIssuesTh = listOf("วาล์ว EGR ตันเขม่าในรุ่น Skyactiv-D 1.5 ดีเซล", "ปั๊มติ๊กเชื้อเพลิงในถังลัดวงจรตัดการทำงาน", "พัดลมหม้อน้ำหมุนช้ากว่าปกติ"),
            commonIssuesEn = listOf("Skyactiv-D 1.5 Diesel EGR carbon blockage", "In-tank fuel pump motor internal electrical short", "Radiator fan motor speed degradation")
        ),
        ThaiCarSpec(
            id = "mazda3",
            make = "Mazda",
            model = "Mazda 3 / Skyactiv-G",
            generationTh = "โฉม BK / BL / BM / BP Fastback (2004 - ปัจจุบัน)",
            yearRange = "2004 - 2026",
            engineCode = "Skyactiv-G 2.0L / MZR 1.6L / 2.0L",
            engineDisplacement = "1,598 - 1,998 cc",
            horsepower = "105 - 165 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / E85",
            engineCategory = ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION,
            baseline = VehicleParameterBaseline(idleRpmMin = 650, idleRpmMax = 750, normalFuelRailPressureMinBar = 35f, normalFuelRailPressureMaxBar = 200f),
            vinPrefixes = listOf("MM8BP", "MM8BM", "MM8BL"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งซ้ายของแผงฟิวส์",
            obdLocationEn = "Under steering column left of interior fuse block",
            engineOilSpec = "Mazda Supra 0W-20 / 5W-30 Fully Synthetic",
            oilCapacityLiters = 4.2f,
            transmissionFluidSpec = "Mazda ATF FZ / ATF M-V",
            coolantCapacityLiters = 6.0f,
            tirePressurePsi = "หน้า 36 PSI / หลัง 36 PSI",
            sparkPlugOrGlowSpec = "Mazda PE5R-18-110 Iridium",
            commonIssuesTh = listOf("ปั๊มติ๊กแรงดันสูงสั่นมีเสียงดังวี้ๆ", "กระจกมองข้างพับไฟฟ้าเฟืองพลาสติกรูด", "หน้าจอศูนย์กลาง MZD Connect ค้าง"),
            commonIssuesEn = listOf("High pressure fuel pump high pitch whine", "Electric folding side mirror plastic gear strip", "MZD Connect infotainment freeze")
        ),
        ThaiCarSpec(
            id = "mazdacx5",
            make = "Mazda",
            model = "CX-5 / CX-30 / CX-3",
            generationTh = "โฉม KE / KF Skyactiv Crossover (2013 - ปัจจุบัน)",
            yearRange = "2013 - 2026",
            engineCode = "Skyactiv-G 2.0L/2.5L / Skyactiv-D 2.2L Twin Turbo",
            engineDisplacement = "1,998 - 2,488 cc",
            horsepower = "156 - 190 hp",
            fuelType = "เบนซิน E20 / ดีเซล B7",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 300f, normalFuelRailPressureMaxBar = 2000f, normalTurboBoostMaxBar = 1.8f),
            vinPrefixes = listOf("MM8KF", "MM8KE", "JMZ"),
            obdLocationTh = "ใต้คอนโซลฝั่งคนขับ ด้านขวาเหนือคันเร่ง",
            obdLocationEn = "Under driver console right side above gas pedal",
            engineOilSpec = "Mazda Supra D 0W-30 / Supra 0W-20",
            oilCapacityLiters = 5.1f,
            transmissionFluidSpec = "Mazda ATF FZ 6-Speed",
            coolantCapacityLiters = 7.8f,
            tirePressurePsi = "หน้า 35 PSI / หลัง 35 PSI",
            sparkPlugOrGlowSpec = "Mazda SH01 Glow Plug / PE5R Iridium",
            commonIssuesTh = listOf("น้ำมันเครื่องเจือปนดีเซลเพิ่มขึ้นในรุ่น Skyactiv-D 2.2", "ฝาโก่งน้ำดันเมื่อวิ่งความเร็วสูงระยะยาว", "โซ่ราวลิ้นดันปั๊มน้ำมันเครื่องสึก"),
            commonIssuesEn = listOf("Skyactiv-D 2.2 Diesel oil dilution level rise", "Head gasket breach under sustained high load", "Oil pump drive chain wear")
        ),

        // ==================== MG (SAIC MOTOR) ====================
        ThaiCarSpec(
            id = "mg3",
            make = "MG",
            model = "MG 3 / Hybrid+",
            generationTh = "โฉมแรก / Hatchback Minorchange / Hybrid+ (2015 - ปัจจุบัน)",
            yearRange = "2015 - 2026",
            engineCode = "15S4C (1.5L) / 1.5L Hybrid+",
            engineDisplacement = "1,498 cc",
            horsepower = "106 - 194 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / Hybrid",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 3.8f, normalFuelRailPressureMaxBar = 4.2f),
            vinPrefixes = listOf("LGH", "LSJ", "LGH3"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งคนขับ ใกล้แผงฟิวส์ใต้คอนโซล",
            obdLocationEn = "Under driver steering column near fuse block",
            engineOilSpec = "SAE 5W-30 / 5W-40 API SN",
            oilCapacityLiters = 4.0f,
            transmissionFluidSpec = "MG Selemix E-Gear Fluid (เกียร์ Selex-AMT) / CVT",
            coolantCapacityLiters = 5.5f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "Torch / NGK BK6E Iridium",
            commonIssuesTh = listOf("เกียร์กึ่งอัตโนมัติ SeleMatic (AMT) เปลี่ยนเกียร์กระตุกโยก", "วาล์วปั๊มลมไอเสียค้าง", "เซนเซอร์เพลาข้อเหวี่ยงลัดวงจรดับเครื่อง"),
            commonIssuesEn = listOf("SeleMatic AMT robotic manual actuator shift jerk", "Secondary air injection valve stuck", "Crankshaft position sensor thermal breakdown")
        ),
        ThaiCarSpec(
            id = "mg_zs_ev",
            make = "MG",
            model = "MG ZS / ZS EV",
            generationTh = "โฉม ZS 1.5L / ZS EV รถไฟฟ้า 100% (2017 - ปัจจุบัน)",
            yearRange = "2017 - 2026",
            engineCode = "15S4U (1.5L) / TZ204XS Electric Drive Motor (EV 50.3 kWh)",
            engineDisplacement = "1,498 cc / Electric Motor",
            horsepower = "114 - 177 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / ไฟฟ้า 100% BEV",
            engineCategory = ThaiEngineCategory.ELECTRIC_BEV,
            baseline = VehicleParameterBaseline(idleRpmMin = 0, idleRpmMax = 0, normalBatteryVoltageMinV = 13.5f, normalBatteryVoltageMaxV = 14.5f),
            vinPrefixes = listOf("LGH", "LSJ", "LGHZS"),
            obdLocationTh = "ใต้แผงคอนโซลฝั่งขวาใกล้หัวคันเร่ง",
            obdLocationEn = "Under right console panel near pedal bracket",
            engineOilSpec = "MG Genuine 5W-30 / EV Reducer Gear Fluid 75W",
            oilCapacityLiters = 1.0f,
            transmissionFluidSpec = "EV Single Speed Gearbox Oil",
            coolantCapacityLiters = 5.8f,
            tirePressurePsi = "หน้า 35 PSI / หลัง 35 PSI",
            sparkPlugOrGlowSpec = "N/A (ระบบไฟฟ้า BEV) / Torch Iridium (รุ่นน้ำมัน)",
            commonIssuesTh = listOf("เกียร์ CVT 8-Speed ในรุ่นน้ำมันกระตุกสะดุด", "ระบบ i-SMART สั่งงานผ่านแอปหลุดการเชื่อมต่อ", "แบตเตอรี่ 12V ไฟหมดไวเปิดประตูไม่ได้"),
            commonIssuesEn = listOf("CVT 8-Speed gear hesitation", "i-SMART telematics cellular module drop", "12V auxiliary lead battery discharge")
        ),
        ThaiCarSpec(
            id = "mg_hs_phev",
            make = "MG",
            model = "MG HS / HS PHEV",
            generationTh = "โฉม HS / HS PHEV Plug-in Hybrid (2019 - ปัจจุบัน)",
            yearRange = "2019 - 2026",
            engineCode = "15E4E (1.5L Turbo) + Hairpin Electric Motor",
            engineDisplacement = "1,490 cc",
            horsepower = "162 - 284 hp / 480 Nm",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / PHEV Plug-in",
            engineCategory = ThaiEngineCategory.HYBRID_HEV_PHEV,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 40f, normalFuelRailPressureMaxBar = 200f, normalTurboBoostMaxBar = 1.25f),
            vinPrefixes = listOf("LGH", "LSJ", "LGHHS"),
            obdLocationTh = "ใต้คอพวงมาลัยฝั่งคนขับ",
            obdLocationEn = "Under steering column driver side",
            engineOilSpec = "MG Genuine 5W-30 API SN Fully Synthetic",
            oilCapacityLiters = 4.2f,
            transmissionFluidSpec = "EDU 10-Speed Hybrid Transmission Fluid",
            coolantCapacityLiters = 6.5f,
            tirePressurePsi = "หน้า 35 PSI / หลัง 35 PSI",
            sparkPlugOrGlowSpec = "Torch Iridium Turbo Plug",
            commonIssuesTh = listOf("ชุดเกียร์ EDU 10-Speed สับสนตำแหน่งเกียร์ช่วงสลับมอเตอร์-เครื่องยนต์", "หน้าจอไอคอนระบบไฮบริดเตือนสีส้ม", "คอยล์จุดระเบิดรั่วเมื่อร้อนจัด"),
            commonIssuesEn = listOf("EDU 10-speed hybrid transmission shift logic delay", "Hybrid system fault warning lamp", "Ignition coil thermal insulation breakdown")
        ),

        // ==================== SUZUKI ====================
        ThaiCarSpec(
            id = "suzuki_swift",
            make = "Suzuki",
            model = "Swift",
            generationTh = "Eco Car โฉม ZA (1.25L) / ZC33S Dualjet (2012 - ปัจจุบัน)",
            yearRange = "2012 - 2026",
            engineCode = "K12M (1.2L Dualjet) / K12B (1.25L 4-Cylinder)",
            engineDisplacement = "1,197 - 1,242 cc",
            horsepower = "83 - 91 hp",
            fuelType = "แก๊สโซฮอล์ 95 / 91 / E20",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 3.5f, normalFuelRailPressureMaxBar = 3.8f),
            vinPrefixes = listOf("TSM", "TSMZC", "TSMZA"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา เหนือคันดึงเปิดฝาถังน้ำมัน",
            obdLocationEn = "Under steering column right side above fuel door release",
            engineOilSpec = "Suzuki Genuine ECSTAR 0W-20 SN",
            oilCapacityLiters = 3.2f,
            transmissionFluidSpec = "Suzuki Green 1 / Green 2 CVT Fluid",
            coolantCapacityLiters = 4.2f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 30 PSI",
            sparkPlugOrGlowSpec = "NGK KR6AI Iridium",
            commonIssuesTh = listOf("ปั๊มน้ำเครื่องยนต์ซึมคราบน้ำยาหล่อเย็น", "เกียร์ CVT สั่นสะดุดรอบกระชากจอดออกตัว", "แร็คพวงมาลัยไฟฟ้าหลวมมีเสียงดัง"),
            commonIssuesEn = listOf("Water pump coolant crust weep", "CVT shudder on light throttle takeoff", "EPS steering column rack play noise")
        ),
        ThaiCarSpec(
            id = "suzuki_ertiga_xl7",
            make = "Suzuki",
            model = "Ertiga / XL7 / Smart Hybrid",
            generationTh = "โฉม Ertiga / XL7 Crossover (2015 - ปัจจุบัน)",
            yearRange = "2015 - 2026",
            engineCode = "K15B (1.5L 4-Cylinder) / K15C Smart Hybrid",
            engineDisplacement = "1,462 cc",
            horsepower = "105 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / Hybrid",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 700, idleRpmMax = 800, normalFuelRailPressureMinBar = 3.5f, normalFuelRailPressureMaxBar = 3.8f),
            vinPrefixes = listOf("TSM", "MHA"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา",
            obdLocationEn = "Under steering column right side",
            engineOilSpec = "Suzuki ECSTAR 0W-20 / 5W-30",
            oilCapacityLiters = 3.7f,
            transmissionFluidSpec = "Suzuki ATF 3317 4-Speed",
            coolantCapacityLiters = 5.0f,
            tirePressurePsi = "หน้า 35 PSI / หลัง 38 PSI",
            sparkPlugOrGlowSpec = "NGK KR6AI",
            commonIssuesTh = listOf("คอมแอร์มีเสียงดังขณะทำงานหนัก", "โช้คอัพคู่หลังรั่วซึมคราบน้ำมัน", "พัดลมหม้อน้ำเสียงดัง"),
            commonIssuesEn = listOf("AC compressor engagement noise", "Rear shock absorber oil leak", "Radiator fan motor bearing noise")
        ),

        // ==================== CHEVROLET ====================
        ThaiCarSpec(
            id = "chevrolet_colorado_trailblazer",
            make = "Chevrolet",
            model = "Colorado / Trailblazer",
            generationTh = "โฉม ตาจอด / ตาชั้น / Duramax (2004 - 2020)",
            yearRange = "2004 - 2020",
            engineCode = "Duramax 2.5L / 2.8L VN Turbo / 4JK1 / 4JJ1",
            engineDisplacement = "2,499 - 2,776 cc",
            horsepower = "163 - 200 hp / 500 Nm",
            fuelType = "ดีเซล (Diesel B7)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 300f, normalFuelRailPressureMaxBar = 2000f, normalTurboBoostMaxBar = 1.8f),
            vinPrefixes = listOf("KL1", "MMU", "KL11"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา พอร์ตสีดำติดข้างแผงฟิวส์",
            obdLocationEn = "Under steering column right side near fuse compartment",
            engineOilSpec = "GM Dexos2 5W-30 Fully Synthetic",
            oilCapacityLiters = 6.0f,
            transmissionFluidSpec = "GM Dexron VI Automatic Transmission Fluid",
            coolantCapacityLiters = 9.0f,
            tirePressurePsi = "หน้า 35 PSI / หลัง 40 PSI",
            sparkPlugOrGlowSpec = "GM Genuine Metal Glow Plug 11V",
            commonIssuesTh = listOf("เฟืองวาล์ว EGR เป็นพลาสติกูดรูดติดขัด (P0401)", "เซนเซอร์แรงดันรางน้ำมันดิบเสื่อม", "สายพานหน้าเครื่องกับรอกดันพังง่าย"),
            commonIssuesEn = listOf("EGR valve plastic internal gear stripping (P0401)", "Rail pressure sensor electrical resistance drift", "Serpentine belt tensioner pulley failure")
        ),
        ThaiCarSpec(
            id = "chevrolet_captiva",
            make = "Chevrolet",
            model = "Captiva",
            generationTh = "โฉม Gen 1 C100/C140 / All-New Turbo (2007 - 2022)",
            yearRange = "2007 - 2022",
            engineCode = "2.0L VCDi Diesel / 2.4L E85 / 1.5L Turbo (Baojun)",
            engineDisplacement = "1,451 - 2,384 cc",
            horsepower = "150 - 168 hp",
            fuelType = "ดีเซล B7 / เบนซิน E85",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 280f, normalFuelRailPressureMaxBar = 1600f),
            vinPrefixes = listOf("KL1", "LSG", "KL1C"),
            obdLocationTh = "ใต้พวงมาลัย ติดกับช่องเปิดฝากระโปรง",
            obdLocationEn = "Under driver dash near hood release",
            engineOilSpec = "GM Dexos2 5W-30 / 10W-40",
            oilCapacityLiters = 5.5f,
            transmissionFluidSpec = "GM Dexron VI 6-Speed ATF",
            coolantCapacityLiters = 8.0f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 33 PSI",
            sparkPlugOrGlowSpec = "GM Glow Plug / ACDelco Iridium",
            commonIssuesTh = listOf("ท่อแรงดันเทอร์โบปริแตกในรุ่นดีเซล", "กล่อง TCM เกียร์ 6T45/6T40 ร้อนแล้วตัดเข้า Limp Mode", "ปั๊มพาวเวอร์ไฟฟ้าเสีย"),
            commonIssuesEn = listOf("Turbocharger boost pressure hose cracking", "6T40/6T45 TCM transmission module thermal failure limp mode", "Electro-hydraulic power steering pump wear")
        ),

        // ==================== HYUNDAI & KIA ====================
        ThaiCarSpec(
            id = "hyundai_h1_staria",
            make = "Hyundai",
            model = "H-1 / Grand Starex / Staria",
            generationTh = "ตู้ VIP H-1 A2 / Staria D2.2 (2008 - ปัจจุบัน)",
            yearRange = "2008 - 2026",
            engineCode = "A2 2.5L CRDi VGT Diesel / Smartstream D2.2L",
            engineDisplacement = "2,199 - 2,497 cc",
            horsepower = "175 - 177 hp / 440 Nm",
            fuelType = "ดีเซล (Diesel B7/B10)",
            engineCategory = ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO,
            baseline = VehicleParameterBaseline(idleRpmMin = 750, idleRpmMax = 850, normalFuelRailPressureMinBar = 300f, normalFuelRailPressureMaxBar = 1800f, normalTurboBoostMaxBar = 1.6f),
            vinPrefixes = listOf("KM8", "KNA", "KM8HU"),
            obdLocationTh = "เปิดฝากล่องฟิวส์ใต้คอนโซลฝั่งขวาพวงมาลัย พอร์ตอยู่ภายใน",
            obdLocationEn = "Behind fuse box door under right driver console",
            engineOilSpec = "Hyundai Genuine 5W-30 / 10W-30 ACEA C3",
            oilCapacityLiters = 7.4f,
            transmissionFluidSpec = "Hyundai ATF SP-IV (5-Speed/8-Speed)",
            coolantCapacityLiters = 9.5f,
            tirePressurePsi = "หน้า 38 PSI / หลัง 42-45 PSI",
            sparkPlugOrGlowSpec = "Hyundai Mobis Glow Plug 11V",
            commonIssuesTh = listOf("แหวนรองปลายหัวฉีดรั่วทำให้น้ำมันเครื่องดำเป็นยางเหนียว", "เทอร์โบแปรผัน VGT ใบพัดติดขัด", "วาล์ว SCV ท้ายปั๊มเสื่อม"),
            commonIssuesEn = listOf("Injector copper washer seal leak causing oil sludge", "VGT turbocharger variable vane carbon sticking", "SCV suction control valve wear")
        ),

        // ==================== BMW & MERCEDES-BENZ ====================
        ThaiCarSpec(
            id = "bmw_3_5_series",
            make = "BMW",
            model = "3 Series / 5 Series (E90 / F30 / G20 / F10 / G30)",
            generationTh = "โฉม E90 / F30 / G20 / F10 / G30 (2005 - ปัจจุบัน)",
            yearRange = "2005 - 2026",
            engineCode = "B48 2.0L Turbo / N20 / B47 2.0L Diesel / N47",
            engineDisplacement = "1,995 - 1,998 cc",
            horsepower = "184 - 252 hp",
            fuelType = "แก๊สโซฮอล์ 95 / ดีเซล B7",
            engineCategory = ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION,
            baseline = VehicleParameterBaseline(idleRpmMin = 650, idleRpmMax = 750, normalFuelRailPressureMinBar = 50f, normalFuelRailPressureMaxBar = 200f, normalTurboBoostMaxBar = 1.4f),
            vinPrefixes = listOf("WBA", "WBY", "WBA8"),
            obdLocationTh = "ช่องใต้คอนโซลฝั่งขวา มีฝาปิดพลาสติกปั๊มตัวอักษร OBD",
            obdLocationEn = "Under driver side dash panel behind plastic door marked OBD",
            engineOilSpec = "BMW Longlife-04 / Longlife-01 0W-30 / 5W-30",
            oilCapacityLiters = 5.2f,
            transmissionFluidSpec = "ZF LifeguardFluid 8 (เกียร์ ZF 8-Speed)",
            coolantCapacityLiters = 7.2f,
            tirePressurePsi = "หน้า 32 PSI / หลัง 35 PSI",
            sparkPlugOrGlowSpec = "NGK SILZKFR8D7S Iridium / Bosch Glow Plug",
            commonIssuesTh = listOf("ข้อต่อน้ำหล่อเย็นพลาสติกข้างเครื่องกรอบแตก (Coolant Flange)", "โซลินอยด์วาล์วแปรผัน VANOS สกปรก", "ถุงลมโช้คหลังรั่วซึมในรุ่น Touring/5-Series"),
            commonIssuesEn = listOf("Plastic engine coolant outlet flange brittleness", "VANOS variable camshaft solenoid valve contamination", "Rear air suspension bag leak on 5-Series")
        ),
        ThaiCarSpec(
            id = "mercedes_benz_c_e_class",
            make = "Mercedes-Benz",
            model = "C-Class / E-Class (W204 / W205 / W206 / W212 / W213)",
            generationTh = "โฉม C-Class / E-Class / CGI / BlueTEC (2007 - ปัจจุบัน)",
            yearRange = "2007 - 2026",
            engineCode = "M274 / M264 2.0L Turbo / OM651 / OM654 2.0L Diesel",
            engineDisplacement = "1,950 - 1,991 cc",
            horsepower = "184 - 245 hp",
            fuelType = "แก๊สโซฮอล์ 95 / ดีเซล B7 / PHEV",
            engineCategory = ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION,
            baseline = VehicleParameterBaseline(idleRpmMin = 600, idleRpmMax = 700, normalFuelRailPressureMinBar = 45f, normalFuelRailPressureMaxBar = 200f),
            vinPrefixes = listOf("WDD", "W2K", "WDD205"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา มีฝาปิดพลาสติกสีม่วง/ดำ",
            obdLocationEn = "Under driver side dashboard behind purple/black plastic flap",
            engineOilSpec = "MB 229.5 / MB 229.51 5W-30 Fully Synthetic",
            oilCapacityLiters = 6.0f,
            transmissionFluidSpec = "MB 236.15 / MB 236.17 (7G-Tronic / 9G-Tronic)",
            coolantCapacityLiters = 8.0f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 36 PSI",
            sparkPlugOrGlowSpec = "NGK SILZKFR8E7S / Beru Glow Plug",
            commonIssuesTh = listOf("เฟืองราวลิ้น (Camshaft Adjuster) ล็อคมีเสียงดังตอนสตาร์ทเครื่องเย็น", "ปั๊ม AdBlue / NOx Sensor ในรุ่นดีเซล BlueTEC", "หม้อลมเบรกไฟฟ้า SBC/ESP"),
            commonIssuesEn = listOf("Camshaft adjuster phaser rattle on cold engine start", "AdBlue dosing pump and NOx sensor fault in BlueTEC", "Electro-hydraulic brake ESP module fault")
        ),

        // ==================== BYD & GWM (EV & HYBRID) ====================
        ThaiCarSpec(
            id = "byd_atto3_dolphin",
            make = "BYD",
            model = "ATTO 3 / Dolphin / Seal EV",
            generationTh = "รถยนต์ไฟฟ้า 100% Blade Battery (2022 - ปัจจุบัน)",
            yearRange = "2022 - 2026",
            engineCode = "TZ200XSQ Permanent Magnet Synchronous Motor (EV)",
            engineDisplacement = "Electric 44.9 - 82.5 kWh Blade Battery",
            horsepower = "95 - 530 hp",
            fuelType = "ไฟฟ้า 100% (BEV - CCS2 Fast Charging)",
            engineCategory = ThaiEngineCategory.ELECTRIC_BEV,
            baseline = VehicleParameterBaseline(idleRpmMin = 0, idleRpmMax = 0, normalCoolantTempMinC = 20, normalCoolantTempMaxC = 45, normalBatteryVoltageMinV = 13.5f, normalBatteryVoltageMaxV = 14.5f),
            vinPrefixes = listOf("LC0", "LGB", "BYD"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา มีฝาครอบพลาสติกปิดพอร์ต OBD-II",
            obdLocationEn = "Under driver console right side behind removable trim cover",
            engineOilSpec = "น้ำมันหล่อลื่นมอเตอร์ไฟฟ้า EV Reducer Fluid 75W (เปลี่ยนทุก 40,000 กม.)",
            oilCapacityLiters = 1.1f,
            transmissionFluidSpec = "Single Speed EV Reduction Gearbox Oil",
            coolantCapacityLiters = 6.0f,
            tirePressurePsi = "หน้า 36 PSI / หลัง 36 PSI",
            sparkPlugOrGlowSpec = "N/A (ระบบไฟฟ้า 100% - มอเตอร์ขับเคลื่อน)",
            commonIssuesTh = listOf("แรงดันแบตเตอรี่ 12V ลูกเล็กตกระบบชัตดาวน์ (12V Auxiliary)", "หน้าจอสัมผัสหมุนค้างขณะอัปเดต OTA", "เซนเซอร์เตือนลมยาง TPMS หลุดเชื่อมต่อ"),
            commonIssuesEn = listOf("12V auxiliary lead-acid battery sleep state", "Rotating infotainment screen sync glitch during OTA", "TPMS tire pressure Bluetooth sensor pairing drop")
        ),
        ThaiCarSpec(
            id = "gwm_haval_h6_jolion",
            make = "GWM",
            model = "Haval H6 / Jolion / Ora Good Cat",
            generationTh = "โฉม Haval DHT Hybrid / Ora EV (2021 - ปัจจุบัน)",
            yearRange = "2021 - 2026",
            engineCode = "1.5L Turbo DHT Hybrid / TZ180XS Electric Motor",
            engineDisplacement = "1,499 cc / Electric Motor",
            horsepower = "143 - 243 hp / 530 Nm",
            fuelType = "แก๊สโซฮอล์ 95 / E20 / Hybrid / BEV",
            engineCategory = ThaiEngineCategory.HYBRID_HEV_PHEV,
            baseline = VehicleParameterBaseline(idleRpmMin = 800, idleRpmMax = 1200, normalFuelRailPressureMinBar = 40f, normalFuelRailPressureMaxBar = 200f),
            vinPrefixes = listOf("LVR", "LGB", "GWM"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา",
            obdLocationEn = "Under steering column right side",
            engineOilSpec = "GWM Genuine 0W-20 SN Fully Synthetic",
            oilCapacityLiters = 4.0f,
            transmissionFluidSpec = "2-Speed DHT Transmission Fluid",
            coolantCapacityLiters = 6.2f,
            tirePressurePsi = "หน้า 35 PSI / หลัง 35 PSI",
            sparkPlugOrGlowSpec = "NGK Iridium Hybrid Plug",
            commonIssuesTh = listOf("ระบบช่วยเหลือการขับขี่ ADAS เตือนผิดพลาดขณะฝนตกหนัก", "เกียร์ DHT เปลี่ยนสลับโหมดช้า", "กล่องควบคุมแบตเตอรี่ 12V สื่อสารผิดปกติ"),
            commonIssuesEn = listOf("ADAS camera sensors false warning in torrential rain", "DHT hybrid transmission mode switch latency", "12V auxiliary battery control module sync fault")
        ),
        ThaiCarSpec(
            id = "subaru_xv_forester",
            make = "Subaru",
            model = "XV / Forester / Outback",
            generationTh = "โฉม Boxer Engine Symmetrical AWD (2012 - ปัจจุบัน)",
            yearRange = "2012 - 2026",
            engineCode = "FB20 (2.0L Boxer 4-Cylinder) / FB25 2.5L",
            engineDisplacement = "1,995 - 2,498 cc",
            horsepower = "150 - 182 hp",
            fuelType = "แก๊สโซฮอล์ 95 / E20",
            engineCategory = ThaiEngineCategory.GASOLINE_NATURALLY_ASPIRATED,
            baseline = VehicleParameterBaseline(idleRpmMin = 650, idleRpmMax = 750, normalFuelRailPressureMinBar = 38f, normalFuelRailPressureMaxBar = 150f),
            vinPrefixes = listOf("JF1", "JF1GP", "JF1SK"),
            obdLocationTh = "ใต้พวงมาลัยฝั่งขวา",
            obdLocationEn = "Under steering column right side",
            engineOilSpec = "Subaru Genuine 0W-20 SN Fully Synthetic",
            oilCapacityLiters = 4.8f,
            transmissionFluidSpec = "Subaru Lineartronic CVTF II / High Torque CVTF",
            coolantCapacityLiters = 7.0f,
            tirePressurePsi = "หน้า 33 PSI / หลัง 32 PSI",
            sparkPlugOrGlowSpec = "NGK SILZKAR7B11",
            commonIssuesTh = listOf("วาล์วควบคุมน้ำหล่อเย็น Thermo Valve ค้างความร้อนขึ้น", "ลูกปืนล้อหลังหอนเร็วมากเมื่อวิ่งเกิน 60,000 กม.", "ปั๊มติ๊กเชื้อเพลิงเสื่อม"),
            commonIssuesEn = listOf("Thermal control valve sticking causing sudden engine overheating", "Rear wheel hub bearing premature failure", "Low pressure fuel pump wear")
        )
    )

    fun decodeVin(vin: String): Map<String, String> {
        val clean = vin.trim().uppercase()
        if (clean.length < 10) {
            return mapOf(
                "status" to "INVALID",
                "messageTh" to "หมายเลข VIN ต้องมีอย่างน้อย 17 หลัก หรือระบุรหัสประจำตัวรถ",
                "messageEn" to "VIN must be at least 17 characters long"
            )
        }

        val wmi = clean.substring(0, minOf(3, clean.length))
        val country = when {
            wmi.startsWith("M") -> "ประเทศไทย (Thailand WMI)"
            wmi.startsWith("J") -> "ประเทศญี่ปุ่น (Japan WMI)"
            wmi.startsWith("L") -> "ประเทศจีน (China WMI)"
            wmi.startsWith("A") -> "ประเทศแอฟริกาใต้ / ออสเตรเลีย"
            wmi.startsWith("K") -> "ประเทศเกาหลีใต้ (South Korea)"
            wmi.startsWith("1") || wmi.startsWith("4") || wmi.startsWith("5") -> "สหรัฐอเมริกา (USA)"
            wmi.startsWith("W") -> "ประเทศเยอรมนี (Germany)"
            else -> "ภูมิภาคสากล (International)"
        }

        val make = when {
            wmi.startsWith("MR0") -> "Toyota (Thailand)"
            wmi.startsWith("MHR") -> "Honda (Thailand)"
            wmi.startsWith("MP1") || wmi.startsWith("MP2") -> "Isuzu (Thailand)"
            wmi.startsWith("MMT") || wmi.startsWith("MMA") -> "Mitsubishi (Thailand)"
            wmi.startsWith("AHT") || wmi.startsWith("MNB") -> "Ford (Thailand)"
            wmi.startsWith("MNT") -> "Nissan (Thailand)"
            wmi.startsWith("MM8") -> "Mazda (Thailand)"
            wmi.startsWith("LGH") || wmi.startsWith("LSJ") -> "MG (SAIC Motor CP Thailand)"
            wmi.startsWith("TSM") -> "Suzuki (Thailand)"
            wmi.startsWith("KL1") -> "Chevrolet (GM Thailand/Korea)"
            wmi.startsWith("KM8") || wmi.startsWith("KNA") -> "Hyundai / Kia (Korea)"
            wmi.startsWith("WBA") || wmi.startsWith("WBY") -> "BMW (Germany)"
            wmi.startsWith("WDD") || wmi.startsWith("W2K") -> "Mercedes-Benz (Germany)"
            wmi.startsWith("LC0") || wmi.startsWith("LGB") -> "BYD Auto (Thailand/China)"
            wmi.startsWith("LVR") -> "GWM Haval (Thailand/China)"
            wmi.startsWith("JF1") -> "Subaru (Japan)"
            else -> "รถยนต์ผู้ผลิตมาตรฐานสากล"
        }

        val yearChar = if (clean.length >= 10) clean[9] else '?'
        val modelYear = when (yearChar) {
            'Y' -> 2000; '1' -> 2001; '2' -> 2002; '3' -> 2003; '4' -> 2004; '5' -> 2005; '6' -> 2006; '7' -> 2007; '8' -> 2008; '9' -> 2009
            'A' -> 2010; 'B' -> 2011; 'C' -> 2012; 'D' -> 2013; 'E' -> 2014; 'F' -> 2015; 'G' -> 2016; 'H' -> 2017; 'J' -> 2018; 'K' -> 2019
            'L' -> 2020; 'M' -> 2021; 'N' -> 2022; 'P' -> 2023; 'R' -> 2024; 'S' -> 2025; 'T' -> 2026
            else -> 2022
        }

        return mapOf(
            "status" to "VALID",
            "vin" to clean,
            "wmi" to wmi,
            "country" to country,
            "make" to make,
            "modelYear" to modelYear.toString(),
            "plantCode" to if (clean.length >= 11) clean[10].toString() else "A",
            "serialNumber" to if (clean.length >= 17) clean.substring(11, 17) else clean.takeLast(6)
        )
    }

    fun findCarByVin(vin: String): ThaiCarSpec? {
        val clean = vin.trim().uppercase()
        return popularThaiCars.firstOrNull { car ->
            car.vinPrefixes.any { prefix -> clean.startsWith(prefix) }
        }
    }

    fun findCarByMakeModel(make: String, model: String): ThaiCarSpec? {
        val cleanMake = make.trim().lowercase()
        val cleanModel = model.trim().lowercase()
        return popularThaiCars.firstOrNull { car ->
            car.make.lowercase().contains(cleanMake) && (car.model.lowercase().contains(cleanModel) || cleanModel.contains(car.model.lowercase().take(5)))
        }
    }

    /**
     * Interpret DTC Code specifically tailored for a given vehicle model and fuel/engine category.
     */
    fun getDtcInterpretation(carSpec: ThaiCarSpec?, code: String): VehicleDtcInterpretationInfo {
        val carName = carSpec?.let { "${it.make} ${it.model}" } ?: "รถยนต์ทั่วไป"
        val engineType = carSpec?.engineCategory ?: ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO

        val defaultCausesTh = mutableListOf<String>()
        val defaultCausesEn = mutableListOf<String>()
        var locationTh = "ระบบเครื่องยนต์และเซนเซอร์ควบคุม"
        var locationEn = "Engine Control Module & Sensors"
        var severityTh = "ระดับปานกลาง - ควรตรวจสอบระบบ"

        when (code) {
            "P0087" -> {
                severityTh = "🚨 วิกฤต (Critical) - แรงดันคอมมอนเรลตก ตรวจสอบด่วน"
                if (engineType == ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO) {
                    defaultCausesTh.add("วาล์ว SCV (Suction Control Valve) ท้ายปั๊มคอมมอนเรลติดขัด/ค้าง (พบบ่อยมากใน $carName)")
                    defaultCausesTh.add("ไส้กรองดักน้ำ / กรองโซล่าอุดตันคราบตะกรัน")
                    defaultCausesTh.add("หัวฉีดดีเซลรั่วไหลกลับ (Injector Backleak) เกินมาตรฐาน")
                    locationTh = "ท้ายปั๊มเชื้อเพลิงแรงดันสูง (High Pressure Pump) และรางคอมมอนเรล"
                    locationEn = "Suction Control Valve (SCV) & Common Rail Pressure Sensor"
                } else {
                    defaultCausesTh.add("ปั๊มเชื้อเพลิงแรงดันสูง High Pressure Direct Injection Pump ตกแรงดัน")
                    defaultCausesTh.add("ปั๊มติ๊กในถังเชื้อเพลิงแรงดันตก")
                    locationTh = "ชุดปั๊มฉีดตรง High Pressure Fuel Pump บนฝาสูบ"
                    locationEn = "High Pressure Direct Injection Fuel Pump"
                }
            }
            "P0171" -> {
                severityTh = "⚠️ เตือน (Warning) - ส่วนผสมบาง เกินขีดชดเชย"
                if (carSpec?.make?.equals("Honda", ignoreCase = true) == true) {
                    defaultCausesTh.add("ใช้น้ำมัน E85 หรือแก๊สโซฮอล์คุณภาพต่ำ ทำให้ออกซิเจนเซนเซอร์อ่านค่า Lean")
                    defaultCausesTh.add("หัวฉีดตรง VTEC Turbo อุดตันคราบเขม่า")
                    defaultCausesTh.add("คอยล์จุดระเบิดรั่วภายใต้แรงดันบูสต์เทอร์โบ")
                    locationTh = "เซนเซอร์วัดปริมาณอากาศ MAF และเซนเซอร์ไอเสีย upstream O2"
                    locationEn = "Upstream Air-Fuel Ratio Sensor & MAF sensor"
                } else if (carSpec?.make?.equals("Nissan", ignoreCase = true) == true) {
                    defaultCausesTh.add("แอร์โฟร์เซนเซอร์ (MAF Sensor) สกปรกคราบฝุ่นละออง")
                    defaultCausesTh.add("ท่อลมแวคคั่มลิ้นผีเสื้อปริแตก")
                    locationTh = "เซนเซอร์แอร์โฟร์บนกรองอากาศ"
                    locationEn = "Mass Air Flow sensor on air cleaner duct"
                } else {
                    defaultCausesTh.add("แอร์โฟร์เซนเซอร์ (MAF Sensor) สกปรก")
                    defaultCausesTh.add("ท่อไอดีหรือท่อลมแวคคั่มรั่ว")
                    defaultCausesTh.add("กรองเบนซินอุดตัน")
                    locationTh = "ระบบท่อลมไอดี และเซนเซอร์ MAF"
                    locationEn = "Intake manifold and MAF sensor"
                }
            }
            "P0299" -> {
                severityTh = "⚠️ เตือน (Warning) - บูสต์เทอร์โบต่ำกว่าเกณฑ์"
                if (carSpec?.make?.equals("Ford", ignoreCase = true) == true) {
                    defaultCausesTh.add("ท่ออินเตอร์คูลเลอร์ซิลิโคนแตกชำรุด (พบบ่อยมากใน Ranger/Everest 2.0 Bi-Turbo)")
                    defaultCausesTh.add("โซลินอยด์ไฟฟ้าคุมเวสต์เกตเทอร์โบเสีย")
                    locationTh = "ท่อต่ออินเตอร์คูลเลอร์ฝั่งไอดี และเวสต์เกตเทอร์โบ"
                    locationEn = "Bi-Turbo Intercooler Silicone Charge Pipe & Wastegate Solenoid"
                } else {
                    defaultCausesTh.add("ท่อไอดีเทอร์โบรั่วซึม")
                    defaultCausesTh.add("สวิตช์สุญญากาศแวคคั่มเวสต์เกตเทอร์โบรั่ว")
                    locationTh = "ท่ออินเตอร์คูลเลอร์ และโข่งเทอร์โบแปรผัน"
                    locationEn = "Intercooler hoses & VNT turbocharger actuator"
                }
            }
            "P0401" -> {
                severityTh = "⚠️ เตือน (Warning) - วาล์ว EGR ตันเขม่า"
                if (carSpec?.make?.equals("Chevrolet", ignoreCase = true) == true) {
                    defaultCausesTh.add("เฟืองพลาสติกหมุนวาล์ว EGR รูดลัดวงจร (พบบ่อยใน Colorado/Trailblazer Duramax)")
                    locationTh = "มอเตอร์วาล์ว EGR ข้างเสื้อสูบ"
                    locationEn = "EGR Valve Motor Assembly"
                } else {
                    defaultCausesTh.add("เขม่าดำจากการเผาไหม้ดีเซลสะสมอุดตันช่องทางวาล์ว EGR")
                    locationTh = "วาล์ว EGR และท่อระบายไอเสียวนกลับ"
                    locationEn = "EGR Valve & Cooler bypass passage"
                }
            }
            "P0700", "P0741" -> {
                severityTh = "🚨 วิกฤต (Critical) - ระบบเกียร์อัตโนมัติขัดข้อง"
                defaultCausesTh.add("น้ำมันเกียร์เสื่อมสภาพหรือความร้อนเกียร์สูงเกินกำหนด")
                defaultCausesTh.add("โซลินอยด์วาล์วเปลี่ยนเกียร์ติดขัด")
                locationTh = "กล่องควบคุมเกียร์ TCM และสมองกลเกียร์ Valve Body"
                locationEn = "Transmission Control Module (TCM) & Valve Body"
            }
            else -> {
                defaultCausesTh.add("พบรหัสข้อผิดพลาดตรวจจับได้ในระบบ $carName")
                defaultCausesTh.addAll(carSpec?.commonIssuesTh?.take(2) ?: emptyList())
            }
        }

        return VehicleDtcInterpretationInfo(
            dtcCode = code,
            carModelName = carName,
            vehicleSpecificCausesTh = defaultCausesTh,
            vehicleSpecificCausesEn = defaultCausesEn,
            specificComponentLocationTh = locationTh,
            specificComponentLocationEn = locationEn,
            severityWarningTh = severityTh
        )
    }

    /**
     * Analyzes Freeze Frame data against specific vehicle baseline thresholds.
     */
    fun analyzeFreezeFrameForVehicle(carSpec: ThaiCarSpec?, freezeFrame: FreezeFrameData): VehicleFreezeFrameAnalysis {
        val vehicleName = carSpec?.let { "${it.make} ${it.model}" } ?: "รถยนต์ทั่วไป"
        val baseline = carSpec?.baseline ?: VehicleParameterBaseline()
        val engineCat = carSpec?.engineCategory ?: ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO

        val isFuelPressureAnomaly = when (engineCat) {
            ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO -> freezeFrame.fuelRailPressureBar < baseline.normalFuelRailPressureMinBar
            ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION -> freezeFrame.fuelRailPressureBar < 30f
            else -> freezeFrame.fuelRailPressureBar < 2.5f
        }

        val fuelPressureStatus = if (isFuelPressureAnomaly) {
            "⚠️ ต่ำกว่าเกณฑ์มาตรฐาน (${String.format("%.0f", freezeFrame.fuelRailPressureBar)} Bar vs ปกติ >${String.format("%.0f", baseline.normalFuelRailPressureMinBar)} Bar)"
        } else {
            "✅ ปกติ (${String.format("%.0f", freezeFrame.fuelRailPressureBar)} Bar)"
        }

        val isBoostAnomaly = (engineCat == ThaiEngineCategory.DIESEL_COMMONRAIL_TURBO || engineCat == ThaiEngineCategory.GASOLINE_TURBO_DIRECT_INJECTION) &&
                freezeFrame.turboBoostBar < 0.2f && freezeFrame.engineRpm > 2000

        val boostStatus = if (isBoostAnomaly) {
            "⚠️ บูสต์เทอร์โบรั่วตก (${String.format("%.2f", freezeFrame.turboBoostBar)} Bar ที่รอบ ${freezeFrame.engineRpm} RPM)"
        } else {
            "✅ บูสต์ปกติ (${String.format("%.2f", freezeFrame.turboBoostBar)} Bar)"
        }

        val isCoolantAnomaly = freezeFrame.coolantTempCelsius > baseline.normalCoolantTempMaxC
        val coolantStatus = if (isCoolantAnomaly) {
            "🚨 อุณหภูมิสูงผิดปกติ (${freezeFrame.coolantTempCelsius} °C - เกินเกณฑ์ ${baseline.normalCoolantTempMaxC} °C)"
        } else {
            "✅ ปกติ (${freezeFrame.coolantTempCelsius} °C)"
        }

        val isFuelTrimAnomaly = (freezeFrame.shortTermFuelTrimPercent > 12f || freezeFrame.longTermFuelTrimPercent > 15f)
        val fuelTrimStatus = if (isFuelTrimAnomaly) {
            "⚠️ การชดเชยน้ำมันสูง (STFT +${String.format("%.1f", freezeFrame.shortTermFuelTrimPercent)}% / LTFT +${String.format("%.1f", freezeFrame.longTermFuelTrimPercent)}%)"
        } else {
            "✅ ชดเชยปกติ (STFT ${String.format("%.1f", freezeFrame.shortTermFuelTrimPercent)}%)"
        }

        val overallTh = StringBuilder()
        overallTh.append("ผลวิเคราะห์ Freeze Frame สำหรับ $vehicleName:\n")
        if (isFuelPressureAnomaly) overallTh.append("• แรงดันเชื้อเพลิงตกขณะเกิด Fault Code (${freezeFrame.triggerDtc})\n")
        if (isBoostAnomaly) overallTh.append("• บูสต์เทอร์โบลดลง ตรวจสอบท่อไอดี/อินเตอร์คูลเลอร์\n")
        if (isCoolantAnomaly) overallTh.append("• ความร้อนเครื่องยนต์สูงเกินเกณฑ์ขณะบันทึกข้อมูล\n")
        if (isFuelTrimAnomaly) overallTh.append("• กล่อง ECU ชดเชยการจ่ายน้ำมันหนาขึ้นเนื่องจากส่วนผสมบาง\n")
        if (!isFuelPressureAnomaly && !isBoostAnomaly && !isCoolantAnomaly && !isFuelTrimAnomaly) {
            overallTh.append("• พารามิเตอร์หลักขณะเกิดเหตุอยู่ในเกณฑ์มาตรฐานเซนเซอร์ปกติ\n")
        }

        return VehicleFreezeFrameAnalysis(
            vehicleName = vehicleName,
            dtcCode = freezeFrame.triggerDtc,
            isFuelPressureAnomaly = isFuelPressureAnomaly,
            fuelPressureStatusTh = fuelPressureStatus,
            isBoostPressureAnomaly = isBoostAnomaly,
            boostPressureStatusTh = boostStatus,
            isCoolantTempAnomaly = isCoolantAnomaly,
            coolantTempStatusTh = coolantStatus,
            isFuelTrimAnomaly = isFuelTrimAnomaly,
            fuelTrimStatusTh = fuelTrimStatus,
            overallDiagnosisTh = overallTh.toString(),
            overallDiagnosisEn = "Freeze frame telemetry evaluated against $vehicleName baseline parameters."
        )
    }
}
