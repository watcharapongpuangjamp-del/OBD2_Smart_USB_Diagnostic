package com.example

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Layers
import com.example.presentation.common.components.CyberCyan
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.UsbOff
import com.example.ui.components.ObdConnectionDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.components.UiCustomizationDialog
import com.example.ui.theme.CyberThemeWrapper
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppLanguage
import com.example.ui.MainViewModel
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AiMechanicScreen
import com.example.ui.screens.DiagnosticScannerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveTelemetryScreen
import com.example.ui.screens.MaintenanceScreen
import com.example.ui.screens.PredictiveMaintenanceScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReportDialog
import com.example.ui.screens.VehicleSpecsScreen
import com.example.ui.screens.WidgetsScreen
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.AutoCyan
import com.example.ui.theme.AutoGreen
import com.example.ui.theme.MyApplicationTheme

import androidx.compose.runtime.DisposableEffect
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.data.model.AppThemeMode
import java.util.Calendar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.FirebaseAuth
import com.example.ui.screens.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureAppCheck(this, intent)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val themeMode by mainViewModel.themeMode.collectAsState()
            
            var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

            DisposableEffect(Unit) {
                val listener = FirebaseAuth.AuthStateListener { auth ->
                    currentUser = auth.currentUser
                }
                Firebase.auth.addAuthStateListener(listener)
                onDispose {
                    Firebase.auth.removeAuthStateListener(listener)
                }
            }
            
            val isDarkTheme = when (themeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
                AppThemeMode.AUTO_TIME -> {
                    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    hour < 6 || hour >= 18 // Dark theme from 18:00 to 05:59
                }
            }

            var isGuestMode by remember { mutableStateOf(false) }
            val customizationState by mainViewModel.uiCustomizationState.collectAsState()

            CyberThemeWrapper(customizationState = customizationState) {
                MyApplicationTheme(darkTheme = isDarkTheme) {
                    if (currentUser == null && !isGuestMode) {
                        LoginScreen(
                            onLoginSuccess = { currentUser = Firebase.auth.currentUser },
                            onGuestContinue = { isGuestMode = true }
                        )
                    } else {
                        MainAppContent(viewModel = mainViewModel)
                    }
                }
            }
        }
    }

    private fun configureAppCheck(context: android.content.Context, intent: android.content.Intent?) {
        try {
            val appCheck = com.google.firebase.appcheck.FirebaseAppCheck.getInstance()
            val isDebug = (context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
            if (isDebug) {
                val providerFactory = com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory.getInstance()
                appCheck.installAppCheckProviderFactory(providerFactory)
                val debugToken = intent?.getStringExtra("FIREBASE_APPCHECK_DEBUG_TOKEN")
                if (!debugToken.isNullOrEmpty()) {
                    val prefs = context.getSharedPreferences("com.google.firebase.appcheck.debug.store", android.content.Context.MODE_PRIVATE)
                    prefs.edit().putString("F_AC_D_T", debugToken).commit()
                }
            } else {
                val resId = context.resources.getIdentifier("recaptcha_site_key", "string", context.packageName)
                val recaptchaSiteKey = if (resId != 0) context.getString(resId) else ""
                if (recaptchaSiteKey.isNotEmpty() && recaptchaSiteKey != "YOUR_RECAPTCHA_SITE_KEY") {
                    val providerFactory = com.google.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory.getInstance()
                    appCheck.installAppCheckProviderFactory(providerFactory)
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("AppCheck", "Failed to configure App Check", e)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val currentTab by viewModel.selectedTab.collectAsState()
    val language by viewModel.language.collectAsState()
    val isTh = language == AppLanguage.TH
    val activeDtcs by viewModel.activeDtcs.collectAsState()
    val isMilOn by viewModel.isMilOn.collectAsState()
    val vehicle by viewModel.selectedVehicle.collectAsState()

    var showReportDialog by remember { mutableStateOf(false) }
    var showConnectionDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showUiCustomizationDialog by remember { mutableStateOf(false) }
    var isCleanArchMode by remember { mutableStateOf(false) }
    val showHudDialog by viewModel.showHudDialog.collectAsState()
    val showBaselineDialog by viewModel.showBaselineDialog.collectAsState()
    val showSnapshotDialog by viewModel.showSnapshotDialog.collectAsState()
    val showSystemLogsDialog by viewModel.showSystemLogsDialog.collectAsState()

    val isUsbConnected by viewModel.isUsbConnected.collectAsState()
    val isUsbConnecting by viewModel.isUsbConnecting.collectAsState()

    val context = LocalContext.current
    var backPressedTime by remember { mutableLongStateOf(0L) }

    // Handle physical / gesture back button to return to Home tab if not on Home
    BackHandler(enabled = currentTab != 0) {
        viewModel.setTab(0)
    }

    // Double back press to exit app when on Home tab
    BackHandler(enabled = currentTab == 0) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < 2000L) {
            (context as? Activity)?.finish()
        } else {
            backPressedTime = currentTime
            Toast.makeText(
                context,
                if (isTh) "กดย้อนกลับอีกครั้งเพื่อออกจากแอป" else "Press back again to exit",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    if (showHudDialog) {
        com.example.ui.components.HudDisplayDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setShowHudDialog(false) }
        )
    }

    if (showBaselineDialog) {
        com.example.ui.components.OemBaselineComparisonDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setShowBaselineDialog(false) }
        )
    }

    if (showSnapshotDialog) {
        com.example.ui.components.TelemetrySnapshotDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setShowSnapshotDialog(false) }
        )
    }

    if (showSystemLogsDialog) {
        com.example.ui.screens.SystemLogsDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setShowSystemLogsDialog(false) }
        )
    }

    if (showReportDialog) {
        ReportDialog(
            viewModel = viewModel,
            onDismiss = { showReportDialog = false }
        )
    }

    if (showConnectionDialog) {
        ObdConnectionDialog(
            viewModel = viewModel,
            onDismiss = { showConnectionDialog = false }
        )
    }

    if (showSettingsDialog) {
        SettingsDialog(
            viewModel = viewModel,
            onDismiss = { showSettingsDialog = false }
        )
    }

    if (showUiCustomizationDialog) {
        UiCustomizationDialog(
            viewModel = viewModel,
            onDismiss = { showUiCustomizationDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(AutoGreen)
                        )
                        Text(
                            text = if (isTh) " สแกนรถยนต์ไทย OBD2" else " Thai Car OBD-II Pro",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    if (currentTab != 0) {
                        IconButton(
                            onClick = { viewModel.setTab(0) },
                            modifier = Modifier.testTag("btn_back_home")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to Home",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                actions = {
                    // Profile Button
                    IconButton(
                        onClick = { viewModel.setTab(8) },
                        modifier = Modifier.testTag("btn_open_profile")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = AutoGreen
                        )
                    }

                    // UI Customization Theme Palette Button
                    IconButton(
                        onClick = { showUiCustomizationDialog = true },
                        modifier = Modifier.testTag("btn_open_ui_customization")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Customize UI",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Clean Architecture Showcase Toggle Pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isCleanArchMode) CyberCyan.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (isCleanArchMode) CyberCyan.copy(alpha = 0.8f) else Color.Transparent),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .clickable { isCleanArchMode = !isCleanArchMode }
                            .testTag("btn_toggle_clean_arch")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = "Clean Arch",
                                tint = if (isCleanArchMode) CyberCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isCleanArchMode) "CleanArch" else "Standard",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                color = if (isCleanArchMode) CyberCyan else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Settings Button
                    IconButton(
                        onClick = { showSettingsDialog = true },
                        modifier = Modifier.testTag("btn_open_settings")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = AutoCyan
                        )
                    }

                    // OBD2 USB Hardware Connection Button & Status Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isUsbConnected) AutoGreen.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (isUsbConnected) AutoGreen.copy(alpha = 0.6f) else Color.Transparent),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .clickable { showConnectionDialog = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (isUsbConnected) Icons.Default.Usb else Icons.Default.UsbOff,
                                contentDescription = "USB OBD2",
                                tint = if (isUsbConnected) AutoGreen else (if (isUsbConnecting) AutoAmber else MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when {
                                    isUsbConnected -> "USB 115k"
                                    isUsbConnecting -> "CONN..."
                                    else -> "USB"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                color = if (isUsbConnected) AutoGreen else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Diagnostic Report Button
                    IconButton(
                        onClick = { showReportDialog = true },
                        modifier = Modifier.testTag("btn_open_report")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = "Diagnostic Report",
                            tint = AutoCyan
                        )
                    }

                    // Language Toggle EN / TH
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            IconButton(
                                onClick = { viewModel.toggleLanguage() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text(
                                    text = if (isTh) "TH" else "EN",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AutoCyan
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { viewModel.setTab(0) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(if (isTh) "หน้าแรก" else "Home", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AutoCyan,
                        selectedTextColor = AutoCyan,
                        indicatorColor = AutoCyan.copy(alpha = 0.2f)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { viewModel.setTab(1) },
                    icon = {
                        Box {
                            Icon(Icons.Default.Build, contentDescription = "Diagnostics")
                            if (activeDtcs.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE53935))
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }
                    },
                    label = { Text(if (isTh) "สแกนกล่อง" else "Modules", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AutoCyan,
                        selectedTextColor = AutoCyan,
                        indicatorColor = AutoCyan.copy(alpha = 0.2f)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { viewModel.setTab(2) },
                    icon = { Icon(Icons.Default.HourglassTop, contentDescription = "Predictive") },
                    label = { Text(if (isTh) "พยากรณ์" else "Predict", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AutoGreen,
                        selectedTextColor = AutoGreen,
                        indicatorColor = AutoGreen.copy(alpha = 0.2f)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { viewModel.setTab(3) },
                    icon = { Icon(Icons.Default.Speed, contentDescription = "Live Telemetry") },
                    label = { Text(if (isTh) "มาตรวัด" else "Live", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AutoCyan,
                        selectedTextColor = AutoCyan,
                        indicatorColor = AutoCyan.copy(alpha = 0.2f)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == 4,
                    onClick = { viewModel.setTab(4) },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Mechanic") },
                    label = { Text(if (isTh) "ช่าง AI" else "AI Help", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AutoAmber,
                        selectedTextColor = AutoAmber,
                        indicatorColor = AutoAmber.copy(alpha = 0.2f)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == 5,
                    onClick = { viewModel.setTab(5) },
                    icon = { Icon(Icons.Default.History, contentDescription = "Maintenance & Specs") },
                    label = { Text(if (isTh) "ประวัติ/VIN" else "History", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFB388FF),
                        selectedTextColor = Color(0xFFB388FF),
                        indicatorColor = Color(0xFFB388FF).copy(alpha = 0.2f)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == 6,
                    onClick = { viewModel.setTab(6) },
                    icon = { Icon(Icons.Default.Info, contentDescription = "About") },
                    label = { Text(if (isTh) "เกี่ยวกับ" else "About", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AutoCyan,
                        selectedTextColor = AutoCyan,
                        indicatorColor = AutoCyan.copy(alpha = 0.2f)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == 7,
                    onClick = { viewModel.setTab(7) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Widgets") },
                    label = { Text(if (isTh) "วิดเจ็ต" else "Widgets", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AutoGreen,
                        selectedTextColor = AutoGreen,
                        indicatorColor = AutoGreen.copy(alpha = 0.2f)
                    )
                )

                NavigationBarItem(
                    selected = currentTab == 8,
                    onClick = { viewModel.setTab(8) },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
                    label = { Text(if (isTh) "โปรไฟล์" else "Profile", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AutoCyan,
                        selectedTextColor = AutoCyan,
                        indicatorColor = AutoCyan.copy(alpha = 0.2f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentTab,
            modifier = Modifier.padding(innerPadding)
        ) { tab ->
            when (tab) {
                0 -> HomeScreen(viewModel = viewModel)
                1 -> {
                    if (isCleanArchMode) {
                        val cleanScannerVm = remember {
                            val repo = com.example.data.repository.ObdDiagnosticRepositoryImpl(
                                dtcDatabase = com.example.data.repository.DtcDatabase
                            )
                            val pdfRepo = com.example.data.repository.PdfReportRepositoryImpl(context)
                            com.example.presentation.screen.diagnostic.DiagnosticScannerViewModel(
                                scanDtcCodesUseCase = com.example.domain.usecase.ScanDtcCodesUseCase(repo),
                                clearDtcCodesUseCase = com.example.domain.usecase.ClearDtcCodesUseCase(repo),
                                exportDiagnosticPdfUseCase = com.example.domain.usecase.ExportDiagnosticPdfUseCase(pdfRepo)
                            )
                        }
                        val cleanState by cleanScannerVm.uiState.collectAsState()

                        androidx.compose.runtime.LaunchedEffect(cleanScannerVm) {
                            cleanScannerVm.uiEffect.collect { effect ->
                                when (effect) {
                                    is com.example.presentation.screen.diagnostic.DiagnosticScannerUiEffect.SharePdfFile -> {
                                        try {
                                            val uri = androidx.core.content.FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.fileprovider",
                                                effect.file
                                            )
                                            val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                                type = "application/pdf"
                                                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                                                putExtra(android.content.Intent.EXTRA_SUBJECT, "Thai Auto OBD-II Inspection Report")
                                                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(android.content.Intent.createChooser(shareIntent, "แชร์รายงานผลการตรวจสภาพ (PDF)"))
                                        } catch (e: Exception) {
                                            android.util.Log.e("PDF", "Failed to share PDF", e)
                                        }
                                    }
                                    is com.example.presentation.screen.diagnostic.DiagnosticScannerUiEffect.ShowToast -> {
                                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                                    }
                                    else -> Unit
                                }
                            }
                        }

                        com.example.presentation.screen.diagnostic.DiagnosticScannerScreen(
                            state = cleanState,
                            onEvent = cleanScannerVm::onEvent
                        )
                    } else {
                        DiagnosticScannerScreen(viewModel = viewModel)
                    }
                }
                2 -> PredictiveMaintenanceScreen(viewModel = viewModel)
                3 -> {
                    if (isCleanArchMode) {
                        val cleanTelemetryVm = remember {
                            val repo = com.example.data.repository.ObdDiagnosticRepositoryImpl()
                            com.example.presentation.screen.telemetry.LiveTelemetryViewModel(
                                streamLiveTelemetryUseCase = com.example.domain.usecase.StreamLiveTelemetryUseCase(repo)
                            )
                        }
                        val cleanState by cleanTelemetryVm.uiState.collectAsState()
                        com.example.presentation.screen.telemetry.LiveTelemetryScreen(
                            state = cleanState,
                            onEvent = cleanTelemetryVm::onEvent
                        )
                    } else {
                        LiveTelemetryScreen(viewModel = viewModel)
                    }
                }
                4 -> AiMechanicScreen(viewModel = viewModel)
                5 -> MaintenanceScreen(viewModel = viewModel)
                6 -> AboutScreen(viewModel = viewModel)
                7 -> WidgetsScreen(viewModel = viewModel)
                8 -> ProfileScreen(viewModel = viewModel)
                else -> HomeScreen(viewModel = viewModel)
            }
        }
    }
}
