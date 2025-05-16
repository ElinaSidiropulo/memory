package com.example.memorytrainer.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytrainer.R
import com.example.memorytrainer.utils.ThemeManager

class TapTheRightRhythmActivity : AppCompatActivity() {

    private lateinit var rhythmText: TextView
    private lateinit var frameLayout: FrameLayout
    private lateinit var vibrator: Vibrator

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

        rhythmText = findViewById(R.id.rhythmText)
        frameLayout = findViewById(R.id.frameLayout)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        initializeCubes()
        startNewGame()
    }

    private fun initializeCubes() {
        cubes = Array(totalCubes) { index ->
            Button(this).apply {
                val row = index / gridSize
                val col = index % gridSize
                layoutParams = FrameLayout.LayoutParams(100, 100).apply {
                    leftMargin = col * 150 + 50
                    topMargin = row * 150 + 50
                }
                setBackgroundColor(Color.GRAY)
                setOnClickListener {
                    if (isPlayerTurn) {
                        onPlayerTap(index)
                        animate().alpha(0.5f).setDuration(100).withEndAction {
                            animate().alpha(1f).setDuration(100)
                        }
                    }
                }
                frameLayout.addView(this)
            }
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
        cubes[step].setBackgroundColor(Color.GREEN)
    }

    private fun resetCubes() {
        cubes.forEach { it.setBackgroundColor(Color.GRAY) }
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
