package com.example.memorytrainer.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

object ThemeManager {
    private const val PREFS_NAME = "MemoryTrainerPrefs"
    private const val THEME_KEY = "theme_mode"

    // Возможные режимы темы
    enum class ThemeMode(val value: Int) {
        LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
        DARK(AppCompatDelegate.MODE_NIGHT_YES)
    }

    // Применить тему
    fun applyTheme(context: Context) {
        val themeMode = getThemeMode(context)
        AppCompatDelegate.setDefaultNightMode(themeMode.value)
    }

    // Получить текущую тему
    fun getThemeMode(context: Context): ThemeMode {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val themeValue = prefs.getInt(THEME_KEY, ThemeMode.LIGHT.value)
        return ThemeMode.values().firstOrNull { it.value == themeValue } ?: ThemeMode.LIGHT
    }

    // Установить тему
    fun setThemeMode(context: Context, themeMode: ThemeMode) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putInt(THEME_KEY, themeMode.value)
        }
        AppCompatDelegate.setDefaultNightMode(themeMode.value)
    }
}