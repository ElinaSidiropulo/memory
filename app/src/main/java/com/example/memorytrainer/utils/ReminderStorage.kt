package com.example.memorytrainer.utils

import android.content.Context
import com.example.memorytrainer.models.Reminder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ReminderStorage {

    private const val PREFS_NAME = "reminders"

    private fun getKeyForUser(context: Context): String {
        val userManager = UserManager(context)
        val username = userManager.getCurrentUsername()
        return "reminder_list_${username ?: "guest"}"
    }

    fun saveReminder(context: Context, reminder: Reminder) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val reminders = getReminders(context).toMutableList()
        reminders.add(reminder)
        val json = Gson().toJson(reminders)
        prefs.edit().putString(getKeyForUser(context), json).apply()
    }

    fun getReminders(context: Context): List<Reminder> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(getKeyForUser(context), null) ?: return emptyList()
        val type = object : TypeToken<List<Reminder>>() {}.type
        val allReminders: List<Reminder> = Gson().fromJson(json, type)

        // Удаляем просроченные
        val validReminders = allReminders.filter { it.timeInMillis > System.currentTimeMillis() }

        // Обновляем SharedPreferences (удаляем просроченные)
        val updatedJson = Gson().toJson(validReminders)
        prefs.edit().putString(getKeyForUser(context), updatedJson).apply()

        return validReminders
    }

    fun deleteReminder(context: Context, id: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val updated = getReminders(context).filter { it.id != id }
        val json = Gson().toJson(updated)
        prefs.edit().putString(getKeyForUser(context), json).apply()
    }

    fun updateReminder(context: Context, updatedReminder: Reminder) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val reminders = getReminders(context).toMutableList()
        val index = reminders.indexOfFirst { it.id == updatedReminder.id }
        if (index != -1) {
            reminders[index] = updatedReminder
            val json = Gson().toJson(reminders)
            prefs.edit().putString(getKeyForUser(context), json).apply()
        }
    }
}
