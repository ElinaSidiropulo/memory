package com.example.memorytrainer.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytrainer.R
import com.example.memorytrainer.utils.UserManager
import com.example.memorytrainer.dialogs.RulesDialog;

class MainActivity : AppCompatActivity() {

    private lateinit var userManager: UserManager
    private val FIRST_RUN_KEY = "firstRun"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")

        userManager = UserManager(this)

        // Проверяем, авторизован ли пользователь
        if (!userManager.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        checkFirstRun()

        findViewById<Button>(R.id.btn_games).setOnClickListener {
            startActivity(Intent(this, GamesActivity::class.java))
        }

        findViewById<Button>(R.id.btn_rules).setOnClickListener {
            val dialog = RulesDialog(this)
            dialog.show()
        }

        findViewById<Button>(R.id.btn_reminders).setOnClickListener {
            startActivity(Intent(this, ReminderActivity::class.java))
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val userManager = UserManager(this)

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
                AlertDialog.Builder(this)
                    .setTitle("Выбор темы")
                    .setMessage("Вы выбрали Светлую тему (пока без функционала).")
                    .setPositiveButton("OK", null)
                    .show()
                true
            }

            R.id.menu_theme_dark -> {
                AlertDialog.Builder(this)
                    .setTitle("Выбор темы")
                    .setMessage("Вы выбрали Тёмную тему (пока без функционала).")
                    .setPositiveButton("OK", null)
                    .show()
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

}
