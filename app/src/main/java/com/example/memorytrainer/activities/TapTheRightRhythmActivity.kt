package com.example.memorytrainer.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytrainer.R
import com.example.memorytrainer.utils.ThemeManager
import com.example.memorytrainer.utils.UserManager

class TapTheRightRhythmActivity : AppCompatActivity() {

    private lateinit var rhythmText: TextView
    private lateinit var frameLayout: FrameLayout
    private lateinit var vibrator: Vibrator
    private lateinit var userManager: UserManager

    private val handler = Handler(Looper.getMainLooper())

    private val gridSize = 4
    private val totalCubes = gridSize * gridSize
    private lateinit var cubes: Array<Button>

    private val rhythmSequence = mutableListOf<Int>()
    private var currentStep = 0
    private var isPlayerTurn = false

    // Новые переменные для прогрессии сложности
    private var currentLength = 2
    private var repeatCount = 0
    private val maxLength = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tap_the_right_rhythm)

        userManager = UserManager(this)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = userManager.getCurrentUsername() ?: "Пользователь"
        }

        if (!userManager.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        rhythmText = findViewById(R.id.rhythmText)
        frameLayout = findViewById(R.id.frameLayout)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        initializeCubes()
        // Remove startNewGame() from here
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

    private fun initializeCubes() {
        frameLayout.post {
            // Convert dp to pixels
            val spacingDp = 8f // Spacing between squares
            val bufferDp = 4f // Small buffer margin around the grid
            val spacingPx = (spacingDp * resources.displayMetrics.density).toInt()
            val bufferPx = (bufferDp * resources.displayMetrics.density).toInt()

            // Get available width and height, accounting for FrameLayout padding and buffer
            val availableWidth = frameLayout.width - frameLayout.paddingLeft - frameLayout.paddingRight - 2 * bufferPx
            val availableHeight = frameLayout.height - frameLayout.paddingTop - frameLayout.paddingBottom - 2 * bufferPx

            // Calculate total spacing: (gridSize - 1) gaps between squares
            val totalSpacing = spacingPx * (gridSize - 1)

            // Calculate square size: min of width and height, minus spacing, divided by gridSize
            val size = minOf(
                (availableWidth - totalSpacing) / gridSize,
                (availableHeight - totalSpacing) / gridSize
            )

            // Calculate grid dimensions (squares + spacing)
            val gridWidth = size * gridSize + totalSpacing
            val gridHeight = size * gridSize + totalSpacing

            // Calculate starting offsets to center the grid
            val startX = (frameLayout.width - gridWidth) / 2
            val startY = (frameLayout.height - gridHeight) / 2

            cubes = Array(totalCubes) { index ->
                Button(this).apply {
                    val row = index / gridSize
                    val col = index % gridSize

                    layoutParams = FrameLayout.LayoutParams(size, size).apply {
                        leftMargin = startX + col * (size + spacingPx)
                        topMargin = startY + row * (size + spacingPx)
                    }

                    setBackgroundResource(R.drawable.button_shape)
                    setOnClickListener {
                        if (isPlayerTurn) {
                            onPlayerTap(index)
                            animate().scaleX(0.9f).scaleY(0.9f).setDuration(100)
                                .withEndAction {
                                    animate().scaleX(1f).scaleY(1f).setDuration(100)
                                }
                        }
                    }
                    frameLayout.addView(this)
                }
            }
            startNewGame()
        }
    }
    private fun startNewGame() {
        rhythmSequence.clear()
        currentStep = 0
        isPlayerTurn = false

        rhythmText.text = "Запоминайте ритм..."
        generateRhythm()
    }

    private fun generateRhythm() {
        rhythmSequence.clear()
        for (i in 0 until currentLength) {
            rhythmSequence.add((0 until totalCubes).random())
        }

        Toast.makeText(this, "Длина ритма: $currentLength", Toast.LENGTH_SHORT).show()
        showRhythm()
    }

    private fun showRhythm() {
        var delay = 0L

        for (step in rhythmSequence) {
            handler.postDelayed({
                highlightCube(step)
            }, delay)
            delay += 800L
        }

        handler.postDelayed({
            resetCubes()
            rhythmText.text = "Ваша очередь!"
            isPlayerTurn = true
        }, delay)
    }

    private fun highlightCube(step: Int) {
        resetCubes()
        cubes[step].setBackgroundResource(R.drawable.button_highlight)
    }

    private fun resetCubes() {
        cubes.forEach { it.setBackgroundResource(R.drawable.button_shape) }
    }

    private fun onPlayerTap(step: Int) {
        if (step == rhythmSequence[currentStep]) {
            currentStep++
            if (currentStep == rhythmSequence.size) {
                Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
                isPlayerTurn = false
                repeatCount++

                handler.postDelayed({
                    if (repeatCount == 5) {
                        currentLength++
                        repeatCount = 0
                        if (currentLength > maxLength) {
                            Toast.makeText(this, "Потрясающе! Вы прошли все уровни!", Toast.LENGTH_LONG).show()
                            currentLength = 2
                        }
                    }
                    startNewGame()
                }, 1000)
            }
        } else {
            Toast.makeText(this, "Неверно, попробуйте снова!", Toast.LENGTH_SHORT).show()
            isPlayerTurn = false
            repeatCount = 0 // сброс прогресса по текущей длине
            handler.postDelayed({ startNewGame() }, 1000)
        }
    }
}