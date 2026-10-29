# AI Predictive Diagnostics & Professional PDF Inspection Report Engine

A comprehensive implementation plan integrating Gemini Deep-Reasoning Predictive Diagnostics, Step-by-Step DIY Repair Guides with Thai Baht cost estimation, and a native exportable PDF Diagnostic Inspection Report generator for Thai Auto OBD-II Pro.

## User Review & Critical Decisions

> [!IMPORTANT]
> **Confirmed Strategic Priorities**:
> - **Primary Innovation Track**: Track B — AI Predictive Diagnostics powered by Gemini (`gemini-3.8-flash`) deep reasoning, correlating DTCs with Freeze Frame sensor anomalies.
> - **Actionable Repair Guides**: Step-by-step DIY repair checklists with tool requirements, difficulty ratings, and itemized Thai Baht (THB) part/labor cost estimates.
> - **Branded PDF Export**: Native high-resolution PDF Diagnostic Inspection Report generation designed for Thai mechanics, garages, and car owners, shareable via LINE, Gmail, or print.

---

## 1. Overview & Core Concept

- **What It Does**:
  1. **Gemini Deep Reasoning Engine**: Evaluates active DTC codes in combination with physical Freeze Frame telemetry (e.g. Coolant 98°C, RPM 2240, Fuel Rail 1120 Bar, Fuel Trim +14%) to identify root causes rather than generic code definitions.
  2. **DIY Repair & Cost Estimator**: Breaks down repair procedures into structured steps with required socket/wrench sizes, torque specifications, and estimated Thai Baht replacement costs (comparing OEM genuine vs aftermarket parts).
  3. **Native PDF Inspection Report**: Uses Android's native `PdfDocument` engine to render a vector-crisp, branded diagnostic sheet complete with vehicle VIN, freeze-frame sensor telemetry, fault summary, technician notes, and customer sign-off.
- **Target Audience / Persona**: Automotive technicians, garage owners, used car inspectors, and DIY car enthusiasts in Thailand.
- **Key Value**: Delivers actionable repair guidance that saves money, identifies hidden sensor faults, and produces customer-facing repair documentation.

---

## 2. User Experience & Visual Design

- **Key User Flows**:
  1. **Diagnostic Scan & AI Deep Dive**: User scans vehicle -> detected DTCs render with a glowing "วิเคราะห์เชิงลึก AI (Gemini)" button -> taps button -> Gemini generates root cause analysis, freeze frame sensor correlation, and DIY steps.
  2. **DIY Repair Checklist Modal**: User views interactive step-by-step checklist with tool requirements (e.g. Socket 10mm, Torque wrench 8.5 N·m) and interactive price breakdown in THB.
  3. **Generate & Share PDF Report**: User taps "พิมพ์รายงานตรวจสภาพ (Export PDF)" -> native PDF generator builds the report in background -> previews report summary -> opens Android share sheet (LINE, Gmail, Drive, Print).
- **Visual Identity**:
  - Deep carbon theme (`#0C1017`) with cyan (`#00E5FF`), amber (`#FFB300`), and emerald accents (`#00E676`).
  - Formatted PDF layout: Clean white document background, dark slate header with neon cyan accents, clear tabular grid, and official automotive badge styling.

---

## 3. Technical Architecture & Component Flow

```
┌────────────────────────────────────────────────────────┐
│                   Presentation Layer                   │
│                                                        │
│   DiagnosticScannerScreen     DiagnosticReportDialog   │
│             ▲                           ▲              │
│             │ (MVI Events & Effects)    │              │
│             ▼                           ▼              │
│   DiagnosticScannerViewModel ◄─── BaseViewModel        │
└───────────────────────────┬────────────────────────────┘
                            │ executes UseCases
┌───────────────────────────▼────────────────────────────┐
│                      Domain Layer                      │
│                                                        │
│   UseCases:                                            │
│   - GenerateAiDiagnosticReportUseCase                  │
│   - ExportDiagnosticPdfUseCase                         │
│                                                        │
│   Domain Models:                                       │
│   - AiDiagnosisReport (RootCauses, Steps, Costs, Specs)│
│   - DiagnosticPdfDocument (FileUri, PageCount, Size)   │
└───────────────────────────▲────────────────────────────┘
                            │ coordinates
┌───────────────────────────┴────────────────────────────┐
│                       Data Layer                       │
│                                                        │
│   AiMechanicRepository:                                │
│   - Firebase AI Logic (`gemini-3.8-flash`)             │
│   - Structured Output Schema (JSON via Schema.obj)     │
│   - Thai Vehicle Fallback Knowledge Base               │
│                                                        │
│   PdfReportGenerator:                                  │
│   - Android Native `android.graphics.pdf.PdfDocument`  │
│   - FileProvider URI sharing via Intent.ACTION_SEND    │
└────────────────────────────────────────────────────────┘
```

---

## 4. Implementation Phasing

1. **Step 1: Domain Models & Structured AI Diagnostic Schema**
   - Create `AiDiagnosticModels.kt` in `domain/model/` with `DiyRepairStep`, `PartPriceEstimate`, and `AiDiagnosisReport`.
   - Add `GenerateAiDiagnosticReportUseCase` and `ExportDiagnosticPdfUseCase`.

2. **Step 2: AI Diagnostic Engine Enhancement (`AiMechanicRepository`)**
   - Implement structured prompt and Gemini `gemini-3.8-flash` reasoning using `Firebase.ai` with coroutine timeouts and fallback.
   - Correlate Freeze Frame parameters with vehicle specific known failure modes (SCV valve, MAF contamination, O2 heater circuit, turbo boost leak).

3. **Step 3: Native PDF Report Generator (`PdfReportGenerator`)**
   - Implement `PdfReportGenerator` using Android `PdfDocument` and `Canvas`.
   - Render header, vehicle metadata, active DTCs table, Freeze Frame snapshot, AI diagnostic advice, and Thai Baht price estimate table.
   - Configure FileProvider and share intent.

4. **Step 4: Presentation & UI Integration**
   - Add "วิเคราะห์เชิงลึก AI" modal and "ส่งออก PDF รายงานตรวจเช็ค" action in `DiagnosticScannerScreen` and `DiagnosticScannerViewModel`.
   - Add unit tests for PDF generator and AI response parsing.
   - Verify build with `compile_applet`.
