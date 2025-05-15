package com.example.memorytrainer.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytrainer.R
import java.util.*

class ReminderActivity : AppCompatActivity() {

    private lateinit var etReminderTitle: EditText
    private lateinit var tvDateTime: TextView
    private lateinit var btnSelectDateTime: Button
    private lateinit var btnSetReminder: Button

    private var selectedCalendar: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        etReminderTitle = findViewById(R.id.et_reminder_title)
        tvDateTime = findViewById(R.id.tv_datetime)
        btnSelectDateTime = findViewById(R.id.btn_select_datetime)
        btnSetReminder = findViewById(R.id.btn_set_reminder)

        btnSelectDateTime.setOnClickListener {
            // Покажем диалог для выбора даты и времени
            showDateTimePicker()
        }

        btnSetReminder.setOnClickListener {
            setReminder()
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = android.app.DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                val timePickerDialog = android.app.TimePickerDialog(this,
                    { _, hourOfDay, minute ->
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        selectedCalendar = calendar
                        tvDateTime.text = "Дата и время: ${calendar.time}"
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                timePickerDialog.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }

    private fun setReminder() {
        val reminderTitle = etReminderTitle.text.toString()
        val calendar = selectedCalendar ?: return Toast.makeText(this, "Выберите дату и время", Toast.LENGTH_SHORT).show()

        if (reminderTitle.isEmpty()) {
            Toast.makeText(this, "Введите заголовок напоминания", Toast.LENGTH_SHORT).show()
            return
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("title", reminderTitle)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Устанавливаем напоминание
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(this, "Напоминание установлено!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
