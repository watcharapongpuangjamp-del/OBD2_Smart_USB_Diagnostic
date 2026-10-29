package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.R

object NotificationHelper {
    const val CHANNEL_MAINTENANCE = "auto_service_intervals"
    const val CHANNEL_CRITICAL_ALERTS = "auto_critical_alerts"
    const val CHANNEL_DIAGNOSTIC_FINDINGS = "auto_diagnostic_findings"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Maintenance channel
            val maintenanceChannel = NotificationChannel(
                CHANNEL_MAINTENANCE,
                "Scheduled Maintenance & Service",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders for scheduled car maintenance intervals, oil changes, and fluid flushes"
            }

            // Critical alerts channel (High importance)
            val criticalChannel = NotificationChannel(
                CHANNEL_CRITICAL_ALERTS,
                "Critical Vehicle Faults & Telemetry Hazards",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Urgent alerts for DTC check engine faults, coolant overheating, and electrical warnings"
                enableVibration(true)
            }

            // Diagnostic findings channel
            val findingsChannel = NotificationChannel(
                CHANNEL_DIAGNOSTIC_FINDINGS,
                "New Diagnostic Findings & Reports",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications when OBD-II system scans complete and diagnostic reports are ready"
            }

            notificationManager.createNotificationChannels(listOf(maintenanceChannel, criticalChannel, findingsChannel))
        }
    }

    fun sendScheduledMaintenanceNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = 1001
    ) {
        try {
            createNotificationChannels(context)
            val builder = NotificationCompat.Builder(context, CHANNEL_MAINTENANCE)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, builder.build())
        } catch (e: Exception) {
            // Gracefully ignore in sandbox/emulator
        }
    }

    fun sendCriticalAlertNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = 1002
    ) {
        try {
            createNotificationChannels(context)
            val builder = NotificationCompat.Builder(context, CHANNEL_CRITICAL_ALERTS)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, builder.build())
        } catch (e: Exception) {
            // Gracefully ignore
        }
    }

    fun sendDiagnosticFindingNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = 1003
    ) {
        try {
            createNotificationChannels(context)
            val builder = NotificationCompat.Builder(context, CHANNEL_DIAGNOSTIC_FINDINGS)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, builder.build())
        } catch (e: Exception) {
            // Gracefully ignore
        }
    }

    // Backwards-compatible alias
    fun sendServiceReminder(context: Context, title: String, message: String, notificationId: Int = 1001) {
        sendScheduledMaintenanceNotification(context, title, message, notificationId)
    }
}

