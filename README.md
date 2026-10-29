# AI System Optimizer & OBD2 Diagnostic (Android)

[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-blue.svg)](https://developer.android.com/jetpack/compose)
[![Android Min SDK](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-green.svg)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-MIT-orange.svg)](LICENSE)

**AI System Optimizer** คือแอปพลิเคชัน Android สำหรับตรวจสอบ วิเคราะห์ และปรับปรุงประสิทธิภาพของยานพาหนะผ่านพอร์ต OBD-II (USB OTG / Serial) และระบบวิเคราะห์อัจฉริยะด้วย AI Mechanic

---

## 🌟 ฟีเจอร์หลัก (Key Features)

### 1. 🔌 Real Hardware USB OTG Connection Driver
- เชื่อมต่อสายสแกนเนอร์ OBD2 USB ผ่าน OTG อ่านข้อมูลสดจากกล่อง ECU โดยตรง
- รองรับชิปเปลี่ยนผ่าน USB Serial ยอดนิยม: **FTDI, CP2102, CH340, PL2303, CDC-ACM**
- ปรับตั้งค่า Baud Rate ได้หลากหลาย (`115200`, `38400`, `230400`, `500000`)
- บังคับเลือกโปรโตคอลการสื่อสาร OBD2 (`AUTO`, `ISO 15765-4 CAN 11/29bit`, `ISO 9141-2`, `KWP2000`)

### 2. 🔍 Multi-ECU Diagnostic Scanner
- สแกนลึก 5 โมดูลหลัก:
  - **ECM** (Engine Control Module - เครื่องยนต์)
  - **TCM** (Transmission Control Module - เกียร์ออโต้ / CVT)
  - **ABS / EBCM** (Anti-Lock Brake System - ระบบเบรก)
  - **SRS** (Airbag System - ถุงลมนิรภัย)
  - **BCM** (Body Control Module - ไฟฟ้าตัวถัง & ระบบกุญแจ Smart Key)
- อ่านรหัสข้อบกพร่อง (DTC) พร้อมคำอธิบายภาษาไทย สาเหตุ วิธีแก้ไข และประมาณการราคาซ่อม/อะไหล่ในไทย
- สั่งลบไฟเตือน (Clear Check Engine Light & Freeze Frame)

### 3. 📊 Real-Time Engine Telemetry & Widgets
- แสดงผลเซนเซอร์สด: RPM, อุณหภูมิน้ำหล่อเย็น (Coolant Temp), แรงดันเทอร์โบ (Boost Pressure), แรงดันไฟแบตเตอรี่ (12V Voltage), มวลอากาศ (MAF), แรงดันรางคอมมอนเรล (Fuel Rail Pressure)
- แดชบอร์ดแบบคัสตอมพร้อมวิทเจ็ตการขับขี่ real-time

### 4. 🤖 AI Mechanic Assistant & Predictive Maintenance
- ผู้ช่วย AI วิเคราะห์ปัญหาและตอบคำถามเชิงช่างยนต์ภาษาไทย
- คำนวณดรรชนีอายุการใช้งานที่เหลืออยู่ (RUL Index - Remaining Useful Life) สำหรับชิ้นส่วนสำคัญ

### 5. 🚘 Thailand Vehicle Specs & VIN Decoder
- รองรับการลงทะเบียนรถยนต์และถอดรหัส VIN 17 หลักของรถในไทย (Toyota, Isuzu, Honda, Ford, Mazda, Mitsubishi, Nissan, BYD, MG ฯลฯ)
- ระบบบันทึกประวัติการบำรุงรักษา (Maintenance Logbook)

### 6. 🧪 Virtual CAN Simulator Engine (Demo Mode)
- โหมดจำลองสัญญาณเซนเซอร์และการขับขี่ เพื่อทดลองใช้งานแอปโดยไม่ต้องเชื่อมต่อสายจริง

---

## 🛠️ โครงสร้างโปรเจกต์ (Project Architecture)

```
app/src/main/java/com/example/
├── data/
│   ├── driver/           # UsbObdDriver, Serial Drivers (FTDI, CP2102, CH340)
│   ├── model/            # Data Models (LiveSensorData, DtcCode, Vehicle)
│   └── repository/       # ThaiCarDatabase, DtcDatabase, PredictiveEngine
├── service/              # Background Telemetry Stream & Service
├── ui/
│   ├── components/       # ObdConnectionDialog, VinScannerDialog, Widgets
│   ├── screens/          # HomeScreen, LiveTelemetryScreen, DiagnosticScannerScreen, AI Assistant, etc.
│   ├── theme/            # Material 3 Auto Dark/Light Color Scheme
│   └── MainViewModel.kt  # Centralized State Management (StateFlow & Coroutines)
└── MainActivity.kt       # Single Activity Architecture with Compose Navigation
```

---

## 🚀 วิธีเปิดและ Build โปรเจกต์ใน Android Studio

1. **Clone repository:**
   ```bash
   git clone https://github.com/YOUR_USERNAME/AI-System-Optimizer.git
   ```
2. **เปิดด้วย Android Studio:**
   - เลือก **Open** แล้วเปิดโฟลเดอร์โปรเจกต์
3. **Build & Run:**
   - ใช้ Gradle JDK 17
   - เลือกรันบน Android Device จริง (รองรับ USB OTG) หรือ Android Emulator (Android 8.0 ขึ้นไป / API 26+)

---

## 📜 License
โปรเจกต์นี้เปิดให้ใช้งานและพัฒนาต่อภายใต้เงื่อนไข [MIT License](LICENSE)
