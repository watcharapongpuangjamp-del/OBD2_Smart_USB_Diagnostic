# 🚀 คู่มือการใช้งาน GitHub Actions Workflow สำหรับ Build Android APK

ระบบได้จัดทำ **GitHub Actions CI/CD Workflow** สำเร็จรูปไว้ให้ในโฟลเดอร์ `.github/workflows/` เพื่อให้สามารถ Compile และสร้างไฟล์ติดตั้ง **APK** ได้โดยอัตโนมัติบน GitHub

---

## 📁 ไฟล์ Workflow ที่ถูกสร้างขึ้น

| ชื่อไฟล์ | วัตถุประสงค์ | การทริกเกอร์ (Trigger) |
|---|---|---|
| `.github/workflows/build-apk.yml` | **Build APK หลัก (Debug & Release)** | เมื่อมีการ `git push` หรือสร้าง PR ไปยัง `main`/`master` หรือกดสั่งรันด้วยตัวเองผ่านหน้าเว็บ GitHub |
| `.github/workflows/release-apk.yml` | **สร้าง GitHub Release พร้อมแนบไฟล์ APK** | เมื่อมีการ Push Git Tag (เช่น `v2.5.0`) หรือกดสร้างผ่านแท็บ Releases |

---

## 📲 วิธีดาวน์โหลดไฟล์ APK ที่สร้างเสร็จแล้วบน GitHub

1. เข้าไปที่ Repository ของคุณบน **GitHub**
2. คลิกที่แท็บ **"Actions"** ด้านบน
3. เลือกรอบการทำงานล่าสุดในรายการ (เช่น `Build Android APK`)
4. เลื่อนลงมาที่หัวข้อ **"Artifacts"** ด้านล่างสุด
5. คลิกที่ **`ThaiAutoOBD-APKs`** เพื่อดาวน์โหลดไฟล์ ZIP ภายในจะมีไฟล์ติดตั้ง `.apk` พร้อมนำไปติดตั้งบนมือถือ Android หรือแท็บเล็ตได้ทันที

---

## 🔘 วิธีกดสั่ง Build APK ด้วยตนเอง (Manual Trigger)

หากต้องการสั่ง Build โดยไม่ต้อง push โค้ดใหม่:
1. ไปที่แท็บ **Actions** บน GitHub
2. เลือกเมนู **"Build Android APK"** ทางแถบซ้ายมือ
3. คลิกปุ่ม **"Run workflow"** ทางขวามือ
4. เลือกตัวเลือก:
   - **Branch:** `main` หรือ `master`
   - **Build Variant:** เลือก `debug` (แนะนำสำหรับการทดสอบ) หรือ `release` หรือ `all`
   - **Run Unit Tests:** ติ๊กถูกเพื่อรันเทสต์ก่อน
5. กด **"Run workflow"** รอประมาณ 3-5 นาที ระบบจะสร้าง APK ให้เรียบร้อย

---

## 🏷️ วิธีปล่อยเวอร์ชันใหม่ (Release APK บน GitHub)

เมื่อต้องการแจกจ่าย APK ให้ผู้ใช้งานดาวน์โหลดโดยตรงผ่านหน้า GitHub Releases:

```bash
# 1. สร้าง Tag เวอร์ชัน
git tag v2.5.0

# 2. Push Tag ขึ้นไปยัง GitHub
git push origin v2.5.0
```

ระบบจะรัน Workflow `Release APK on Tag` โดยอัตโนมัติ พร้อมสร้างหน้า Release และแนบไฟล์:
- `ThaiAutoOBD-debug.apk`
- `ThaiAutoOBD-release.apk`

---

## 🔑 การตั้งค่า GitHub Secrets (ทางเลือกเสริม - Optional)

Workflow นี้ได้รับการออกแบบให้ **"ทำงานได้ทันที 100% แม้ไม่ได้ตั้งค่า Secret ใดๆ"** เพราะมีระบบ Fallback อัตโนมัติ:
- หากไม่มี Keystore ส่วนตัว ระบบจะใช้ `debug.keystore` ที่สร้างขึ้นให้โดยอัตโนมัติ
- หากไม่มี `.env` ระบบจะคัดลอกจาก `.env.example` ให้โดยอัตโนมัติ

หากต้องการใส่ Key จริงสำหรับการลงนาม Release หรือเปิดใช้ฟีเจอร์ AI:
1. ไปที่ GitHub Repository ของคุณ -> **Settings**
2. เลือก **Secrets and variables** -> **Actions**
3. คลิก **"New repository secret"** แล้วใส่ตัวแปรที่ต้องการ:
   - `GEMINI_API_KEY`: API Key สำหรับเชื่อมต่อ Google Gemini
   - `RELEASE_KEYSTORE_BASE64`: ไฟล์ Keystore แปลงเป็น Base64 (`base64 -w 0 your-key.jks`)
   - `RELEASE_KEYSTORE_PASSWORD`: รหัสผ่าน Keystore
   - `RELEASE_KEY_PASSWORD`: รหัสผ่าน Key Alias

---

## ⚙️ คุณสมบัติเด่นของ Workflow นี้
1. **ใช้ JDK 21 (Temurin)**: รองรับ Android Gradle Plugin (AGP 9.x) และ Kotlin 2.2 ได้อย่างสมบูรณ์แบบ
2. **ระบบ Gradle Cache**: แคช Dependency และ Gradle Wrapper เพื่อให้การ Build ครั้งถัดไปเสร็จเร็วขึ้นกว่า 60%
3. **Artifact Retention 30 วัน**: เก็บไฟล์ APK ให้ดาวน์โหลดได้นานถึง 30 วัน
4. **ความปลอดภัย**: ไม่มีการฮาร์ดโค้ดรหัสผ่านหรือความลับลงในโค้ด
