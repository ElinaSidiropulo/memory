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

class TapTheRightRhythmActivity : AppCompatActivity() {

    private lateinit var rhythmText: TextView
    private lateinit var frameLayout: FrameLayout

    private val rhythmSequence = mutableListOf<Int>()
    private var currentStep = 0

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var vibrator: Vibrator

    private val gridSize = 4
    private val totalCubes = gridSize * gridSize
    private var isPlayerTurn = false

    private lateinit var cubes: Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
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
                        // Сделать кнопку темнее при клике
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

        rhythmText.text = "Запоминай ритм..."
        generateRhythm()

        showRhythm()
    }

    private fun generateRhythm() {
        rhythmSequence.clear()
        currentStep = 0
        isPlayerTurn = false

        rhythmText.text = "Запоминай ритм..."

        // Уменьшаем количество шагов в последовательности (например, до 3)
        for (i in 0 until 4) {
            rhythmSequence.add((0 until totalCubes).random())
        }

        showRhythm()
    }


    private fun showRhythm() {
        var delay = 0L

        // Увеличиваем задержку между подсветкой шагов (например, до 1500 миллисекунд)
        for (step in rhythmSequence) {
            handler.postDelayed({
                highlightCube(step)
            }, delay)
            delay += 1500L // Задержка между шагами увеличена
        }

        handler.postDelayed({
            rhythmText.text = "Твоя очередь!"
            isPlayerTurn = true
        }, delay)
    }

    private fun highlightCube(step: Int) {
        // Сброс всех кубиков в серый цвет
        resetCubes()

        // Подсветить выбранный кубик
        cubes[step].setBackgroundColor(Color.GREEN)
    }

    private fun resetCubes() {
        // Восстановить все кубики в серый цвет
        cubes.forEach { it.setBackgroundColor(Color.GRAY) }
    }

    private fun onPlayerTap(step: Int) {
        if (step == rhythmSequence[currentStep]) {
            currentStep++
            if (currentStep == rhythmSequence.size) {
                Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
                handler.postDelayed({ startNewGame() }, 1000)
            }
        } else {
            Toast.makeText(this, "Неверно, попробуй снова!", Toast.LENGTH_SHORT).show()
            handler.postDelayed({ startNewGame() }, 1000)
        }
    }

    override fun onPause() {
        super.onPause()
        // Сохраняем прогресс игры, если нужно
    }

    override fun onResume() {
        super.onResume()
        // Восстанавливаем прогресс игры
    }
}
