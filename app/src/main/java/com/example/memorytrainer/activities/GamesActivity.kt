package com.example.memorytrainer.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memorytrainer.R
import com.example.memorytrainer.adapters.GamesAdapter
import com.example.memorytrainer.databinding.ActivityGamesBinding
import com.example.memorytrainer.models.GameItem
import com.example.memorytrainer.utils.ThemeManager
import com.example.memorytrainer.utils.UserManager

class GamesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamesBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        binding = ActivityGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация UserManager
        userManager = UserManager(this)

        // Настройка ActionBar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false) // Отключаем кнопку "Назад"
            title = userManager.getCurrentUsername() ?: "Пользователь" // Устанавливаем имя пользователя
        }

        // Проверка авторизации
        if (!userManager.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        // Настройка списка игр
        val gamesList = listOf(
            GameItem(
                title = "Найди пары ▶",
                description = "Запоминай и находи пары",
                activityClass = FindPairsActivity::class.java,
                iconResId = R.drawable.baseline_question_mark_24
            ),
            GameItem(
                title = "Повтори последовательность ▶",
                description = "Следуй за последовательностью",
                activityClass = RepeatSequenceActivity::class.java,
                iconResId = R.drawable.baseline_tag_faces_24
            ),
            GameItem(
                title = "Запомни ритм ▶",
                description = "Запоминай ритм и проверяй себя",
                activityClass = TapTheRightRhythmActivity::class.java,
                iconResId = R.drawable.ic_find_pairs
            )
        )

        binding.recyclerViewGames.apply {
            layoutManager = LinearLayoutManager(this@GamesActivity)
            adapter = GamesAdapter(gamesList) { game ->
                startActivity(Intent(this@GamesActivity, game.activityClass))
            }
        }
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
}