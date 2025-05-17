package com.example.memorytrainer.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytrainer.R
import com.example.memorytrainer.utils.ThemeManager
import com.example.memorytrainer.utils.UserManager
import com.example.memorytrainer.dialogs.RulesDialog

class MainActivity : AppCompatActivity() {

    private lateinit var userManager: UserManager
    private val FIRST_RUN_KEY = "firstRun"

    override fun onCreate(savedInstanceState: Bundle?) {
        // Применяем тему перед super.onCreate
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userManager = UserManager(this)

        if (!userManager.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        val username = userManager.getCurrentUsername()
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = username ?: "Пользователь"
        }


        if (!userManager.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        checkFirstRun()

        findViewById<LinearLayout>(R.id.ll_games).setOnClickListener {
            Log.d("MainActivity", "Clicked on Games")
            try {
                startActivity(Intent(this, GamesActivity::class.java))
            } catch (e: Exception) {
                Log.e("MainActivity", "Error starting GamesActivity", e)
                Toast.makeText(this, "Ошибка при открытии Мини-игр", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<LinearLayout>(R.id.ll_rules).setOnClickListener {
            Log.d("MainActivity", "Clicked on Rules")
            try {
                val dialog = RulesDialog(this)
                dialog.show()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error showing RulesDialog", e)
                Toast.makeText(this, "Ошибка при открытии Правил", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<LinearLayout>(R.id.ll_reminders).setOnClickListener {
            Log.d("MainActivity", "Clicked on Reminders")
            try {
                startActivity(Intent(this, ReminderActivity::class.java))
            } catch (e: Exception) {
                Log.e("MainActivity", "Error starting ReminderActivity", e)
                Toast.makeText(this, "Ошибка при открытии Напоминаний", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkFirstRun() {
        val sharedPreferences = getSharedPreferences("MemoryTrainerPrefs", MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean(FIRST_RUN_KEY, true)
        if (isFirstRun) {
            AlertDialog.Builder(this)
                .setTitle("Добро пожаловать!")
                .setMessage("Это приложение поможет вам тренировать память и отслеживать прогресс.")
                .setPositiveButton("Поехали!") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

            sharedPreferences.edit().putBoolean(FIRST_RUN_KEY, false).apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        Log.d("MainActivity", "Menu inflated")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("MainActivity", "Menu item selected: ${item.itemId}")
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

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy")
    }
}