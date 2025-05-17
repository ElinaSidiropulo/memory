package com.example.memorytrainer.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorytrainer.R
import com.example.memorytrainer.adapters.ReminderAdapter
import com.example.memorytrainer.models.Reminder
import com.example.memorytrainer.utils.ReminderStorage
import com.example.memorytrainer.utils.ThemeManager
import com.example.memorytrainer.utils.UserManager
import java.util.*

class ReminderActivity : AppCompatActivity() {

    private lateinit var etReminderTitle: EditText
    private lateinit var tvDateTime: TextView
    private lateinit var btnSelectDateTime: Button
    private lateinit var btnSetReminder: Button
    private lateinit var rvReminders: RecyclerView
    private lateinit var userManager: UserManager

    private var selectedCalendar: Calendar? = null
    private var editingReminder: Reminder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        // Инициализация UserManager
        userManager = UserManager(this)

        // Настройка ActionBar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = userManager.getCurrentUsername() ?: "Пользователь"
        }

        // Проверка авторизации
        if (!userManager.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        // Инициализация UI элементов
        etReminderTitle = findViewById(R.id.et_reminder_title)
        tvDateTime = findViewById(R.id.tv_datetime)
        btnSelectDateTime = findViewById(R.id.btn_select_datetime)
        btnSetReminder = findViewById(R.id.btn_set_reminder)
        rvReminders = findViewById(R.id.rv_reminders)

        rvReminders.layoutManager = LinearLayoutManager(this)

        btnSelectDateTime.setOnClickListener {
            showDateTimePicker()
        }

        btnSetReminder.setOnClickListener {
            setReminder()
        }

        loadReminders()
    }

    // Создание меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Обработка выбора пунктов меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_about -> {
                AlertDialog.Builder(this)
                    .setTitle("О приложении")
                    .setMessage("Memory Trainer — приложение для тренировки памяти.\n\nВы можете играть в мини-игры, устанавливать напоминания и отслеживать прогресс.")
                    .setPositiveButton("OK", null)
                    .show()
                true
            }
            R.id.menu_theme_light -> {
                ThemeManager.setThemeMode(this, ThemeManager.ThemeMode.LIGHT)
                true
            }
            R.id.menu_theme_dark -> {
                ThemeManager.setThemeMode(this, ThemeManager.ThemeMode.DARK)
                true
            }
            R.id.menu_logout -> {
                userManager.logoutUser()
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = android.app.DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePickerDialog = android.app.TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        selectedCalendar = calendar
                        tvDateTime.text = "Дата и время: ${calendar.time}"
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun setReminder() {
        val reminderTitle = etReminderTitle.text.toString()
        val calendar = selectedCalendar ?: return Toast.makeText(this, "Выберите дату и время", Toast.LENGTH_SHORT).show()

        if (reminderTitle.isEmpty()) {
            Toast.makeText(this, "Введите заголовок напоминания", Toast.LENGTH_SHORT).show()
            return
        }

        val existing = editingReminder
        if (existing != null) {
            // Обновляем будильник
            cancelReminder(existing.id)

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, ReminderReceiver::class.java).apply {
                putExtra("title", reminderTitle)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                existing.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            val updatedReminder = existing.copy(title = reminderTitle, timeInMillis = calendar.timeInMillis)
            ReminderStorage.updateReminder(this, updatedReminder)

            Toast.makeText(this, "Напоминание обновлено!", Toast.LENGTH_SHORT).show()
            editingReminder = null
            btnSetReminder.text = "Установить напоминание"
        } else {
            // Новое напоминание
            val id = (System.currentTimeMillis() / 1000).toInt()
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, ReminderReceiver::class.java).apply {
                putExtra("title", reminderTitle)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            val reminder = Reminder(id, reminderTitle, calendar.timeInMillis)
            ReminderStorage.saveReminder(this, reminder)

            Toast.makeText(this, "Напоминание установлено!", Toast.LENGTH_SHORT).show()
        }

        etReminderTitle.text.clear()
        tvDateTime.text = ""
        selectedCalendar = null

        loadReminders()
    }

    private fun cancelReminder(id: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    private fun loadReminders() {
        val reminders = ReminderStorage.getReminders(this)
        rvReminders.adapter = ReminderAdapter(
            reminders,
            onDelete = { reminder ->
                ReminderStorage.deleteReminder(this, reminder.id)
                cancelReminder(reminder.id)
                loadReminders()
            },
            onEdit = { reminder ->
                editingReminder = reminder
                etReminderTitle.setText(reminder.title)
                selectedCalendar = Calendar.getInstance().apply {
                    timeInMillis = reminder.timeInMillis
                }
                tvDateTime.text = "Дата и время: ${selectedCalendar?.time}"
                btnSetReminder.text = "Сохранить изменения"
            }
        )
    }
}