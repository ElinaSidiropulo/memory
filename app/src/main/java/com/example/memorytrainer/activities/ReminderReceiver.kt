package com.example.memorytrainer.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.memorytrainer.R
import com.example.memorytrainer.utils.ThemeManager

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderTitle = intent.getStringExtra("title") ?: return

        // Создаем уведомление
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Напоминания",
                NotificationManager.IMPORTANCE_HIGH // ВАЖНО
            ).apply {
                description = "Канал для срочных напоминаний"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }


        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setContentTitle("Напоминание")
            .setContentText(reminderTitle)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // ВАЖНО
            .setDefaults(Notification.DEFAULT_ALL) // звук, вибрация и т.д.
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}
