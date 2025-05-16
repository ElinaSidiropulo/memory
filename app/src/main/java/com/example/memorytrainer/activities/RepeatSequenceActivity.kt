package com.example.memorytrainer.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytrainer.R
import kotlin.random.Random

class RepeatSequenceActivity : AppCompatActivity() {

    private lateinit var sequenceText: TextView
    private lateinit var buttonContainer: LinearLayout

    private val sequence = mutableListOf<String>()
    private val userInput = mutableListOf<String>()
    private val handler = Handler(Looper.getMainLooper())

    private val buttonCount = 4
    private var isInputEnabled = false

    private var currentLength = 1 // текущая длина последовательности
    private var repeatCount = 0   // сколько раз уже прошли эту длину
    private val maxLength = 10    // максимальная длина

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repeat_sequence)

        sequenceText = findViewById(R.id.sequenceText)
        buttonContainer = findViewById(R.id.buttonContainer)

        generateButtons()
        startNewRound()
    }

    private fun generateButtons() {
        val emojis = listOf("😊", "😎", "😜", "🤩")
        for (i in 0 until buttonCount) {
            val button = Button(this).apply {
                text = emojis[i]
                textSize = 20f
                setOnClickListener {
                    if (isInputEnabled) onUserClick(emojis[i])
                }
            }
            buttonContainer.addView(button)
        }
    }

    private fun startNewRound() {
        isInputEnabled = false
        userInput.clear()
        sequence.clear()

        // Генерируем новую последовательность нужной длины
        val emojis = listOf("😊", "😎", "😜", "🤩")
        repeat(currentLength) {
            sequence.add(emojis.random())
        }

        // Показываем длину перед каждым раундом из 3 попыток
        if (repeatCount == 0) {
            Toast.makeText(this, "Длина последовательности: $currentLength", Toast.LENGTH_SHORT).show()
        }

        sequenceText.text = "Запоминайте..."
        showSequence()
    }

    private fun onUserClick(selectedEmoji: String) {
        userInput.add(selectedEmoji)
        val currentIndex = userInput.lastIndex

        if (currentIndex >= sequence.size || userInput[currentIndex] != sequence[currentIndex]) {
            Toast.makeText(this, "Неверно! Попробуй снова.", Toast.LENGTH_SHORT).show()
            repeatCount = 0 // сбрасываем прогресс по этой длине
            handler.postDelayed({ startNewRound() }, 1000)
            return
        }

        if (userInput.size == sequence.size) {
            Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
            isInputEnabled = false
            repeatCount++

            if (repeatCount == 3) {
                // Переходим к следующей длине
                currentLength++
                repeatCount = 0

                if (currentLength > maxLength) {
                    // Победа!
                    Toast.makeText(this, "Потрясающе! 🎉 Вы прошли все уровни!", Toast.LENGTH_LONG).show()
                    handler.postDelayed({
                        currentLength = 1
                        repeatCount = 0
                        startNewRound()
                    }, 2000)
                    return
                }
            }

            handler.postDelayed({ startNewRound() }, 1000)
        }
    }

    private fun showSequence() {
        val colors = listOf(Color.BLACK, Color.BLUE)
        var delay = 0L

        for ((index, emoji) in sequence.withIndex()) {
            handler.postDelayed({
                sequenceText.setTextColor(colors[index % colors.size])
                sequenceText.text = "${index + 1}: $emoji" // 👈 Добавили номер
            }, delay)
            delay += 1200L
        }

        handler.postDelayed({
            sequenceText.setTextColor(Color.DKGRAY)
            sequenceText.text = "Теперь ваша очередь!"
            isInputEnabled = true
        }, delay)
    }

}
