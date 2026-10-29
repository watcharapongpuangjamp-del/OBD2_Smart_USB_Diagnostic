package com.example.data.repository

import com.example.data.model.AftermarketPart
import com.example.data.model.OemPart
import com.example.data.model.OemPartCategory

object OemPartsDatabase {

    val ALL_OEM_PARTS = listOf(
        // ==================== TOYOTA (REVO / FORTUNER / YARIS / CIVIC / ETC) ====================
        OemPart(
            id = "toyota_oil_filter_revo",
            partNameTh = "ไส้กรองน้ำมันเครื่องแท้ศูนย์ (Toyota Engine Oil Filter)",
            partNameEn = "Genuine Toyota Engine Oil Filter Element",
            category = OemPartCategory.FILTERS_FLUIDS,
            oemPartNumber = "04152-YZZA6",
            carBrand = "Toyota",
            applicableModelsTh = listOf("Hilux Revo 2.4/2.8", "Fortuner 2.4/2.8", "Innova Crysta", "Majesty 2.8"),
            applicableEngineCodes = listOf("1GD-FTV", "2GD-FTV"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Denso", "15601-0001", "คุณภาพเทียบเท่าแท้ติดรถ"),
                AftermarketPart("Bosch", "0986AF0058", "มาตรฐานเยอรมนี"),
                AftermarketPart("Vic", "O-119", "แบรนด์ญี่ปุ่นยอดนิยม")
            ),
            estimatedPriceThb = "220 - 320 ฿",
            dtcCodesRelated = listOf("P0198", "P0299"),
            usageDescriptionTh = "ไส้กรองน้ำมันเครื่องกระดาษ OEM สำหรับเครื่องยนต์ดีเซล GD Turbo แนะนำเปลี่ยนทุก 10,000 กม."
        ),
        OemPart(
            id = "toyota_fuel_filter_revo",
            partNameTh = "กรองน้ำมันโซล่าแท้ศูนย์ (Toyota Fuel Filter Element)",
            partNameEn = "Genuine Toyota Diesel Fuel Filter Element",
            category = OemPartCategory.FILTERS_FLUIDS,
            oemPartNumber = "23390-0L070",
            carBrand = "Toyota",
            applicableModelsTh = listOf("Hilux Revo", "Fortuner", "Innova Crysta", "Vigo Champ"),
            applicableEngineCodes = listOf("1GD-FTV", "2GD-FTV", "1KD-FTV", "2KD-FTV"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Denso", "260300-0100", "ผลิตส่งศูนย์บริการ Toyota"),
                AftermarketPart("Vic", "FC-118", "ไส้กรองญี่ปุ่นดักน้ำและฝุ่นละออง 2 ไมครอน")
            ),
            estimatedPriceThb = "380 - 520 ฿",
            dtcCodesRelated = listOf("P0087", "P0088", "P0093"),
            usageDescriptionTh = "ไส้กรองน้ำมันดีเซลคอมมอนเรล ป้องกันเศษขี้ผงและน้ำเข้าหัวฉีด ป้องกันปั๊มพัง เปลี่ยนทุก 20,000 กม."
        ),
        OemPart(
            id = "toyota_scv_valve_revo",
            partNameTh = "วาล์ว SCV ควบคุมแรงดันปั๊มโซล่าแท้ (Suction Control Valve)",
            partNameEn = "Genuine Toyota Suction Control Valve (SCV)",
            category = OemPartCategory.TURBO_FUEL_SYSTEM,
            oemPartNumber = "04226-0L010",
            carBrand = "Toyota",
            applicableModelsTh = listOf("Hilux Revo", "Fortuner", "Commuter", "Vigo Champ"),
            applicableEngineCodes = listOf("1GD-FTV", "2GD-FTV", "1KD-FTV", "2KD-FTV"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Denso First Time Fit", "294200-0360", "ผลิตตรงจากผู้ผลิตเดิม Denso Japan")
            ),
            estimatedPriceThb = "1,850 - 2,600 ฿",
            dtcCodesRelated = listOf("P0087", "P0088", "P0093", "P1229"),
            usageDescriptionTh = "ตัวควบคุมปริมาณน้ำมันเข้าปั๊มคอมมอนเรล แก้อาการเครื่องสะดุด วูบดับกลางอากาศ หรือเร่งไม่ขึ้น"
        ),
        OemPart(
            id = "toyota_rail_sensor_revo",
            partNameTh = "เซนเซอร์แรงดันรางดีเซลคอมมอนเรล (Rail Pressure Sensor)",
            partNameEn = "Genuine Toyota Commonrail Fuel Rail Pressure Sensor",
            category = OemPartCategory.ENGINE_SENSORS,
            oemPartNumber = "89458-71010",
            carBrand = "Toyota",
            applicableModelsTh = listOf("Hilux Revo", "Fortuner 2.4/2.8", "Innova Crysta"),
            applicableEngineCodes = listOf("1GD-FTV", "2GD-FTV"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Denso", "89458-71010", "Denso Genuine Sensor Component")
            ),
            estimatedPriceThb = "2,800 - 3,900 ฿",
            dtcCodesRelated = listOf("P0190", "P0191", "P0192", "P0193"),
            usageDescriptionTh = "วัดแรงดันน้ำมันในรางดีเซล 35-180 MPa ส่งสัญญาณ 0.5-4.5V เข้ากล่อง ECU ปรับจูนหัวฉีด"
        ),
        OemPart(
            id = "toyota_front_brake_pads_revo",
            partNameTh = "ผ้าเบรกหน้าแท้ศูนย์ (Toyota Genuine Front Brake Pads)",
            partNameEn = "Genuine Toyota Front Disc Brake Pads",
            category = OemPartCategory.BRAKES_CHASSIS,
            oemPartNumber = "04465-0K360",
            carBrand = "Toyota",
            applicableModelsTh = listOf("Hilux Revo 4WD/Prerunner", "Fortuner TRD/Legender"),
            applicableEngineCodes = listOf("1GD-FTV", "2GD-FTV", "1GR-FE"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Advics", "A1N158", "ผู้ผลิตเบรกOEMให้ Toyota Japan"),
                AftermarketPart("Akebono", "AN-792WK", "ผ้าเบรกเซรามิกเสียงเงียบ ฝุ่นน้อย"),
                AftermarketPart("Brembo", "P 83 152", "ผ้าเบรกเกรดสปอร์ตทนความร้อนสูง")
            ),
            estimatedPriceThb = "1,650 - 2,400 ฿",
            dtcCodesRelated = listOf("C0035", "C0040", "C1201"),
            usageDescriptionTh = "ผ้าเบรกหน้าเนื้อคอมปาวด์ OEM ระยะเบรกสั้น ไม่กัดจานเบรก ไม่ส่งเสียงดังยามเบรก"
        ),

        // ==================== ISUZU (D-MAX / MU-X / 1.9 & 3.0) ====================
        OemPart(
            id = "isuzu_oil_filter_1_9",
            partNameTh = "ไส้กรองน้ำมันเครื่อง Isuzu D-Max 1.9 Ddi Blue Power",
            partNameEn = "Genuine Isuzu D-Max 1.9 Ddi Oil Filter Element",
            category = OemPartCategory.FILTERS_FLUIDS,
            oemPartNumber = "8-98270524-0",
            carBrand = "Isuzu",
            applicableModelsTh = listOf("D-Max 1.9 Blue Power", "MU-X 1.9 Ddi (2016-2026)"),
            applicableEngineCodes = listOf("RZ4E-TC"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Aisin", "OFLIS-4011", "ผู้ผลิตชิ้นส่วนมาตรฐานญี่ปุ่น"),
                AftermarketPart("Bosch", "0986AF1142", "แผ่นกรองเส้นใยสังเคราะห์ทนแรงดันสูง"),
                AftermarketPart("Vic", "O-507", "ไส้กรองแท้ Vic Japan")
            ),
            estimatedPriceThb = "250 - 360 ฿",
            dtcCodesRelated = listOf("P0198", "P0251"),
            usageDescriptionTh = "กรองเครื่องกระดาษทรงกระบอกสวมถ้วย ออกแบบเฉพาะเครื่องยนต์ RZ4E 1.9L เปลี่ยนทุก 10,000 กม."
        ),
        OemPart(
            id = "isuzu_fuel_filter_dmax",
            partNameTh = "กรองโซล่าดักน้ำ Isuzu D-Max All New / MU-X",
            partNameEn = "Genuine Isuzu Diesel Fuel Filter Element",
            category = OemPartCategory.FILTERS_FLUIDS,
            oemPartNumber = "8-98165071-0",
            carBrand = "Isuzu",
            applicableModelsTh = listOf("D-Max All New 1.9 / 2.5 / 3.0", "MU-X 1.9 / 3.0"),
            applicableEngineCodes = listOf("RZ4E-TC", "4JJ1-TCX", "4JJ3-TCX", "4JK1-TCX"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Denso", "260300-0210", "ไส้กรองโซล่า Denso Genuine"),
                AftermarketPart("Sakura", "FC-15260", "แบรนด์มาตรฐานส่งออก")
            ),
            estimatedPriceThb = "390 - 550 ฿",
            dtcCodesRelated = listOf("P0087", "P0088", "P0093", "P1093"),
            usageDescriptionTh = "กรองดักน้ำละอองน้ำมันดีเซล ป้องกันหัวฉีดตด หัวฉีดรั่วไหล หรือปั๊มโซล่าติดขัด เปลี่ยนทุก 20,000 กม."
        ),
        OemPart(
            id = "isuzu_scv_valve_dmax",
            partNameTh = "วาล์ว SCV ปั๊มดีเซล Isuzu D-Max / MU-X แท้ศูนย์",
            partNameEn = "Genuine Isuzu D-Max Suction Control Valve",
            category = OemPartCategory.TURBO_FUEL_SYSTEM,
            oemPartNumber = "8-98043687-0",
            carBrand = "Isuzu",
            applicableModelsTh = listOf("D-Max 2.5 / 3.0 / 1.9", "MU-X 2.5 / 3.0"),
            applicableEngineCodes = listOf("4JJ1-TCX", "4JK1-TCX", "RZ4E-TC"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Denso", "294200-2760", "SCV Valve ปลั๊กตรงรุ่น Denso Japan")
            ),
            estimatedPriceThb = "1,900 - 2,750 ฿",
            dtcCodesRelated = listOf("P0087", "P0088", "P1093", "P0093"),
            usageDescriptionTh = "แก้ปัญหาเครื่องไฟ engine โชว์ ปั๊มไม่จ่ายน้ำมัน รถไม่มีแรง ถอดเปลี่ยนตัวเก่าบนปั๊มดีเซลได้ทันที"
        ),

        // ==================== HONDA (CIVIC / CITY / CR-V / HR-V) ====================
        OemPart(
            id = "honda_oil_filter_generic",
            partNameTh = "ไส้กรองน้ำมันเครื่องเหล็กแท้ศูนย์ Honda (ทุกรุ่น)",
            partNameEn = "Genuine Honda Spin-On Engine Oil Filter",
            category = OemPartCategory.FILTERS_FLUIDS,
            oemPartNumber = "15400-RTA-003",
            carBrand = "Honda",
            applicableModelsTh = listOf("Civic FC/FK/FE", "City 1.0 Turbo / 1.5", "CR-V G5/G6", "HR-V e:HEV", "Jazz/Brio"),
            applicableEngineCodes = listOf("P10A2", "L15B7", "L15BG", "R18A", "R20A", "LEB-H1"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Honeywell Filtech", "15400-PLM-A02", "ผู้ผลิต OEM โรงงาน Honda USA"),
                AftermarketPart("Bosch", "0986AF0059", "กรองน้ำมันเครื่องมาตรฐานเยอรมนี"),
                AftermarketPart("Denso", "260300-0040", "กรอง Denso แบบกระป๋องเหล็ก")
            ),
            estimatedPriceThb = "220 - 290 ฿",
            dtcCodesRelated = listOf("P0340", "P0300"),
            usageDescriptionTh = "ไส้กรองเหล็กกักเก็บแรงดันน้ำมันเครื่อง มีวาล์วกันย้อน (Check Valve) เปลี่ยนพร้อมน้ำมันเครื่องทุก 10,000 กม."
        ),
        OemPart(
            id = "honda_spark_plugs_civic_turbo",
            partNameTh = "หัวเทียนอิริเดียมแท้ศูนย์ Honda Civic 1.5 Turbo / City 1.0 Turbo",
            partNameEn = "Genuine Honda Iridium Spark Plug Set",
            category = OemPartCategory.IGNITION_ELECTRICAL,
            oemPartNumber = "12290-59B-003",
            carBrand = "Honda",
            applicableModelsTh = listOf("Civic FC/FK/FE 1.5 Turbo", "CR-V 1.5 Turbo", "Accord 1.5 Turbo"),
            applicableEngineCodes = listOf("L15B7", "L15BG"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("NGK Laser Iridium", "DILKAR8A8", "NGK พาร์ทตรงรุ่นเครื่อง Turbo"),
                AftermarketPart("Denso Iridium TT", "DXE22HCR11S", "Denso เข็มคู่อายุปการใช้งานยาวนาน")
            ),
            estimatedPriceThb = "850 - 1,200 ฿ / หัว",
            dtcCodesRelated = listOf("P0300", "P0301", "P0302", "P0303", "P0304"),
            usageDescriptionTh = "หัวเทียนอิริเดียมทนความร้อนสูงพิเศษสำหรับเครื่องยนต์เบนซินเทอร์โบ ทนแรงอัดสูง แก้ปัญหาจุดระเบิดสะดุด เปลี่ยนทุก 100,000 กม."
        ),

        // ==================== FORD (RANGER / EVEREST / PANTHER 2.0 & 2.2) ====================
        OemPart(
            id = "ford_oil_filter_panther_2_0",
            partNameTh = "ไส้กรองน้ำมันเครื่อง Ford Ranger / Everest 2.0 Bi-Turbo & Turbo",
            partNameEn = "Genuine Ford Motorcraft 2.0L Diesel Oil Filter Element",
            category = OemPartCategory.FILTERS_FLUIDS,
            oemPartNumber = "LU2Z-6731-A",
            carBrand = "Ford",
            applicableModelsTh = listOf("Ranger 2.0 Single/Bi-Turbo", "Everest 2.0 Bi-Turbo", "Ranger Raptor 2.0"),
            applicableEngineCodes = listOf("Panther 2.0L Diesel"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Motorcraft", "FL-2077", "แบรนด์อะไหล่มาตรฐานฟอร์ดโรงงาน"),
                AftermarketPart("Sakura", "EO-19040", "ไส้กรองเกรดทดแทน")
            ),
            estimatedPriceThb = "320 - 450 ฿",
            dtcCodesRelated = listOf("P0198", "P0299"),
            usageDescriptionTh = "ไส้กรองน้ำมันเครื่องสำหรับเครื่องยนต์ Panther 2.0L ดีเซลเทอร์โบคู่ กรองฝุ่นโลหะละเอียด เปลี่ยนทุก 15,000 กม."
        ),
        OemPart(
            id = "ford_maf_sensor_ranger",
            partNameTh = "เซนเซอร์แอร์โฟร์วัดปริมาณอากาศ (MAF Sensor) Ford Ranger / Everest",
            partNameEn = "Genuine Ford Mass Air Flow (MAF) Sensor Assembly",
            category = OemPartCategory.ENGINE_SENSORS,
            oemPartNumber = "AB31-12B579-AA",
            carBrand = "Ford",
            applicableModelsTh = listOf("Ranger 2.2 / 3.2 / 2.0", "Everest 2.2 / 3.2 / 2.0", "Mazda BT-50 Pro"),
            applicableEngineCodes = listOf("P4AT 2.2L", "P5AT 3.2L", "Panther 2.0L"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Continental / VDO", "5WK98101", "ผู้ผลิต OEM ส่งโรงงาน Ford"),
                AftermarketPart("Bosch", "0281006029", "เซนเซอร์วัดการไหลอากาศมาตรฐานสูง")
            ),
            estimatedPriceThb = "2,400 - 3,500 ฿",
            dtcCodesRelated = listOf("P0100", "P0101", "P0102", "P0103"),
            usageDescriptionTh = "วัดปริมาณมวลอากาศเข้าเครื่องยนต์ แก้ปัญหาควันดำ รถไม่มีแรง วูบดับ หรือรอบสวิง"
        ),

        // ==================== MITSUBISHI (TRITON / PAJERO SPORT 2.4 MIVEC) ====================
        OemPart(
            id = "mitsubishi_fuel_filter_triton",
            partNameTh = "กรองโซล่าแท้ศูนย์ Mitsubishi Triton / Pajero Sport 2.4 MIVEC",
            partNameEn = "Genuine Mitsubishi Diesel Fuel Filter Element",
            category = OemPartCategory.FILTERS_FLUIDS,
            oemPartNumber = "1770A338",
            carBrand = "Mitsubishi",
            applicableModelsTh = listOf("Triton 2.4 MIVEC (2015-2026)", "Pajero Sport 2.4 MIVEC"),
            applicableEngineCodes = listOf("4N15 2.4L Clean Diesel"),
            aftermarketCrossReferences = listOf(
                AftermarketPart("Denso", "260300-0450", "ไส้กรอง Denso OEM Japan"),
                AftermarketPart("Aisin", "FLFMI-4002", "คุณภาพเทียบเท่าแท้ศูนย์")
            ),
            estimatedPriceThb = "420 - 580 ฿",
            dtcCodesRelated = listOf("P0087", "P0088", "P0093"),
            usageDescriptionTh = "กรองดักสิ่งสกปรกในระบบดีเซลดีโซล่าร์คอมมอนเรลสำหรับเครื่องอัลลอยด์ 4N15 เปลี่ยนทุก 20,000 กม."
        )
    )

    fun searchParts(
        query: String = "",
        brand: String = "All",
        category: OemPartCategory? = null,
        dtcCode: String = ""
    ): List<OemPart> {
        return ALL_OEM_PARTS.filter { part ->
            val matchesQuery = query.isBlank() ||
                    part.partNameTh.contains(query, ignoreCase = true) ||
                    part.partNameEn.contains(query, ignoreCase = true) ||
                    part.oemPartNumber.contains(query, ignoreCase = true) ||
                    part.carBrand.contains(query, ignoreCase = true) ||
                    part.applicableModelsTh.any { it.contains(query, ignoreCase = true) } ||
                    part.applicableEngineCodes.any { it.contains(query, ignoreCase = true) } ||
                    part.aftermarketCrossReferences.any { it.partNumber.contains(query, ignoreCase = true) || it.brandName.contains(query, ignoreCase = true) }

            val matchesBrand = brand == "All" || part.carBrand.equals(brand, ignoreCase = true)
            val matchesCategory = category == null || part.category == category
            val matchesDtc = dtcCode.isBlank() || part.dtcCodesRelated.any { it.equals(dtcCode, ignoreCase = true) }

            matchesQuery && matchesBrand && matchesCategory && matchesDtc
        }
    }
}
