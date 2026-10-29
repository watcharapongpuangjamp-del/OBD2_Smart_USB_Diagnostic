package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

/**
 * DiagnosticNotificationHelper
 * Manages push notification channels and dispatches system notifications for:
 * 1. Critical vehicle telemetry alerts (overheating > 105°C, low voltage < 11.5V, high rail pressure)
 * 2. Scheduled maintenance & service countdowns (oil, brake pads, timing belt)
 * 3. Diagnostic scan reports & export generation
 */
object DiagnosticNotificationHelper {

    const val CHANNEL_CRITICAL_ALERTS = "channel_critical_alerts"
    const val CHANNEL_SERVICE_REMINDERS = "channel_service_reminders"
    const val CHANNEL_DIAGNOSTIC_REPORTS = "channel_diagnostic_reports"

    private const val NOTIFICATION_ID_CRITICAL = 2001
    private const val NOTIFICATION_ID_SERVICE = 2002
    private const val NOTIFICATION_ID_REPORT = 2003

    fun registerNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val criticalChannel = NotificationChannel(
                CHANNEL_CRITICAL_ALERTS,
                "🚨 Critical Vehicle Faults & Telemetry Hazards",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Urgent alerts for engine overheating, critical DTCs, battery failure, or high rail pressure"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 400, 200, 400)
            }

            val serviceChannel = NotificationChannel(
                CHANNEL_SERVICE_REMINDERS,
                "🔧 Scheduled Service & Maintenance Intervals",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders for engine oil change, timing belt, brake fluid, and OEM filter replacements"
            }

            val reportChannel = NotificationChannel(
                CHANNEL_DIAGNOSTIC_REPORTS,
                "📊 Diagnostic Scan Reports & Findings",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications when comprehensive vehicle diagnostic reports are generated and ready to share"
            }

            notificationManager.createNotificationChannels(
                listOf(criticalChannel, serviceChannel, reportChannel)
            )
        }
    }

    fun dispatchCriticalSensorAlert(
        context: Context,
        sensorName: String,
        currentValue: String,
        threshold: String,
        recommendationTh: String
    ) {
        registerNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_CRITICAL,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "🚨 คำเตือนวิกฤต: $sensorName สูงเกินเกณฑ์!"
        val message = "ค่าปัจจุบัน: $currentValue (เกณฑ์เตือน: $threshold)\nคำแนะนำ: $recommendationTh"

        val notification = NotificationCompat.Builder(context, CHANNEL_CRITICAL_ALERTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText("ตรวจพบค่าผิดปกติ: $currentValue")
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            manager.notify(NOTIFICATION_ID_CRITICAL, notification)
        } catch (_: SecurityException) {
            // Permission not granted or running in headless testing
        }
    }

    fun dispatchServiceReminder(
        context: Context,
        vehicleName: String,
        serviceItemTh: String,
        remainingDaysOrKm: String
    ) {
        registerNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_SERVICE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "🔧 แจ้งเตือนบำรุงรักษา: $vehicleName"
        val message = "ถึงรอบตรวจเช็ก: $serviceItemTh (เหลืออีก $remainingDaysOrKm)\nแตะเพื่อเปิดตารางการบำรุงรักษาและบันทึกประวัติ"

        val notification = NotificationCompat.Builder(context, CHANNEL_SERVICE_REMINDERS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText("ถึงรอบบำรุงรักษา: $serviceItemTh ($remainingDaysOrKm)")
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            manager.notify(NOTIFICATION_ID_SERVICE, notification)
        } catch (_: SecurityException) {
            // Permission safe fallback
        }
    }

    fun dispatchReportReadyNotification(
        context: Context,
        vehicleName: String,
        dtcCount: Int,
        reportId: String
    ) {
        registerNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_REPORT,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "📊 รายงานวิเคราะห์ระบบ OBD-II พร้อมแล้ว"
        val message = "รายงานการสแกนของ $vehicleName (ตรวจพบรหัส $dtcCount โค้ด) ได้รับการประมวลผลเรียบร้อย รหัสรายงาน: $reportId"

        val notification = NotificationCompat.Builder(context, CHANNEL_DIAGNOSTIC_REPORTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText("ผลการสแกน $vehicleName ตรวจพบ $dtcCount รายการ")
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            manager.notify(NOTIFICATION_ID_REPORT, notification)
        } catch (_: SecurityException) {
            // Permission safe fallback
        }
    }
}
