# Cyberpunk Tech Neon UI Redesign & Personalization System

A high-performance Cyberpunk Tech Neon aesthetic upgrade for the Thai Auto OBD-II diagnostic suite. Introduces dark glowing neon themes, customizable accent colors, a reconfigurable drag-and-drop/grid gauge dashboard, and dynamic digital typography choices for speed/RPM gauges.

## User Review & Critical Decisions

> [!IMPORTANT]
> The user confirmed the following UI design preferences in Phase 1:
> - **Visual Aesthetic**: Cyberpunk Tech Neon — high-contrast dark theme with glowing neon green (`#00FF66`), cyan (`#00E5FF`), amber (`#FF9E00`), and magenta (`#FF0055`) accents.
> - **Custom Accent Color Picker**: Users can customize primary accent colors, glow intensities, and ambient card dark tones.
> - **Customizable Dashboard Grid**: Drag-and-drop / re-orderable grid layouts for live telemetry widgets (RPM, Speed, Boost, ECT, Rail Pressure).
> - **Digital Gauge Typography Options**: Selectable digital gauge typography (Monospace / Seven-Segment Tech / Cyber bold) and scale factors for speed/RPM displays.

---

## 1. Overview & Core Concept

- **What It Does**: Transforms the application into a visual Cyberpunk-inspired digital cockpit with neon glowing gauges, sci-fi HUD elements, and customizable telemetry widgets.
- **Target Audience**: Automotive enthusiasts, car tuners, and drivers seeking a futuristic digital gauge cluster experience with real OBD-II telemetry and diagnostic tools.
- **Key Value**: Delivers professional, high-fidelity visuals with personal customization for dashboard layouts, color palettes, and digital font styles without sacrificing diagnostic functionality.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **Cyberpunk Dashboard & Live Cockpit**:
   - High-contrast glowing neon gauges with animated sweep indicators, peak hold bars, and ambient background grid patterns.
   - Live telemetry cards with custom accent glow borders and status indicators.
2. **Dashboard Customization Mode**:
   - Long-press or tap "Customize Layout" on Live Telemetry screen to re-order, toggle visibility, and resize gauge tiles (1x1 mini, 2x1 wide gauge, 2x2 circular arc meter).
3. **Theme & Color Customization Panel**:
   - Instant live color theme selector in Settings: Neon Matrix (Green/Cyan), Cyber Racing (Red/Amber), Synthetic Sunset (Purple/Magenta), and Custom RGB Accent.
4. **Digital Font & Gauge Scaling Settings**:
   - Switch gauge font styles (Digital Monospace, Sci-Fi HUD Bold, Minimalist Tech) and adjust font sizing for low/high visibility day and night driving.

### Visual Identity & Theme
- **Color Palette**:
  - Background: Deep Void Dark (`#0B0E14`), Obsidian Slate (`#121824`)
  - Cyber Green: `#00FF66` (RPM safe zone / System OK)
  - Cyber Cyan: `#00E5FF` (Boost / Telemetry active)
  - Cyber Amber: `#FF9E00` (Warnings / Mid PIDs)
  - Cyber Crimson: `#FF0055` (Critical DTCs / Redline)
- **Typography**:
  - Monospaced / Digital HUD Display numbers (downloaded locally via `font-util install "Share Tech Mono"` / `Roboto Mono`).
  - Clean Thai-compatible M3 body typography with high legibility.
- **Interactive Feedback**:
  - Pulsing status badges, glowing gauge needles, animated progress sweeps, and tactile Material ripple feedback.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Custom Theme State Persistence in DataStore**
  - *Chosen Approach*: Store theme preferences (primary neon color, accent color, selected font index, gauge grid layout order) in Android `DataStore` preferences.
  - *Why*: Ensures user custom color choices and dashboard grid layouts persist seamlessly across app restarts.
- **Decision 2: Flexible Telemetry Widget Grid System**
  - *Chosen Approach*: Use a Compose `LazyVerticalGrid` / re-orderable layout state in `MainViewModel` for live telemetry cards.
  - *Why*: Provides clean responsiveness across vertical phones and landscape tablet/HUD modes while allowing users to reconfigure tile priority.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        User Preference State                           │
│                      (UiThemePreferencesStore)                         │
│  - Neon Primary Accent (#00FF66 / #00E5FF / #FF0055 / Custom)          │
│  - Digital Gauge Font Family (Share Tech Mono / Standard Mono)         │
│  - Telemetry Widget Grid Order & Visibility Array                       │
└──────────────────────────────────┬─────────────────────────────────────┘
                                   │
                                   ▼
┌────────────────────────────────────────────────────────────────────────┐
│                             MainViewModel                              │
│  - Holds uiCustomizationState: StateFlow<UiCustomizationState>          │
│  - Provides updateNeonAccentColor(), setGaugeFont(), reorderGrid()     │
└──────────────────────────────────┬─────────────────────────────────────┘
                                   │
                                   ▼
┌────────────────────────────────────────────────────────────────────────┐
│                          Jetpack Compose UI                            │
│  ┌─────────────────────────┐         ┌──────────────────────────────┐  │
│  │ CyberpunkTheme Wrapper  │         │ Customized Gauge Dashboard   │  │
│  │  - Custom ColorScheme   │         │  - Re-orderable Grid Cards   │  │
│  │  - Ambient Glow Shadows │         │  - Dynamic Font Scale PIDs   │  │
│  └─────────────────────────┘         └──────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

### Planned Implementation Steps

1. **Install Digital HUD Font**:
   - Run `font-util install "Share Tech Mono" --target app/src/main/res/font` and `font-util install "Roboto Mono" --target app/src/main/res/font`.
2. **Create Theme & UI Customization Model/Store**:
   - Define `UiCustomizationState` with neon accent presets (Neon Matrix, Cyber Racing, Synth Sunset, Custom RGB).
   - Add preferences storage in `MainViewModel` / `ThemePreferences.kt`.
3. **Enhance Color Tokens & `Theme.kt`**:
   - Update Compose theme to support dynamic glowing borders (`Modifier.drawBehind` / radial gradient glow) and Cyberpunk neon palettes.
4. **Build UI Customization Sheet / Dialog**:
   - Accent color picker with real-time preview.
   - Font style & gauge text size slider.
   - Grid layout edit toggle for telemetry widgets.
5. **Update Live Telemetry & Gauge Screens**:
   - Apply Cyberpunk digital gauge aesthetics with glowing arc indicators, peak hold lines, and customizable fonts to `LiveTelemetryScreen.kt`, `HomeScreen.kt`, and `WidgetsScreen.kt`.
6. **Compile & Verify**:
   - Run `compile_applet` to verify zero errors and seamless execution.
