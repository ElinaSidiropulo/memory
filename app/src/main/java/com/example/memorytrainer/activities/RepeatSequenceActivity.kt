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
    private var isInputEnabled = false  // 🔒 Флаг блокировки

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repeat_sequence)

        sequenceText = findViewById(R.id.sequenceText)
        buttonContainer = findViewById(R.id.buttonContainer)

        generateButtons()
        startNewRound()
    }

    private fun generateButtons() {
        // Используем смайлики вместо чисел
        val emojis = listOf("😊", "😎", "😜", "🤩")

        for (i in 0 until buttonCount) {
            val button = Button(this).apply {
                text = emojis[i]  // Отображаем смайлик на кнопке
                textSize = 20f
                setOnClickListener {
                    if (isInputEnabled) onUserClick(emojis[i])
                }
            }
            buttonContainer.addView(button)
        }
    }

    private fun startNewRound() {
        isInputEnabled = false  // 🔒 Пока нельзя нажимать
        userInput.clear()
        val emojis = listOf("😊", "😎", "😜", "🤩")
        sequence.add(emojis.random())  // Добавляем случайный смайлик в последовательность

        sequenceText.text = "Запоминай..."
        showSequence()
    }

    private fun onUserClick(selectedEmoji: String) {
        userInput.add(selectedEmoji)

        val currentIndex = userInput.lastIndex
        if (currentIndex >= sequence.size || userInput[currentIndex] != sequence[currentIndex]) {
            Toast.makeText(this, "Неверно! Попробуй снова.", Toast.LENGTH_SHORT).show()
            sequence.clear()
            handler.postDelayed({ startNewRound() }, 1000)
            return
        }

        if (userInput.size == sequence.size) {
            Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
            isInputEnabled = false
            handler.postDelayed({ startNewRound() }, 1000)
        }
    }

    private fun showSequence() {
        val colors = listOf(Color.BLACK, Color.BLUE)  // Цвета для чередования
        var delay = 0L

        for ((index, emoji) in sequence.withIndex()) {
            handler.postDelayed({
                sequenceText.setTextColor(colors[index % colors.size]) // меняем цвет
                sequenceText.text = emoji  // Отображаем смайлик вместо числа
            }, delay)
            delay += 1000L
        }

        handler.postDelayed({
            sequenceText.setTextColor(Color.DKGRAY)  // вернём нейтральный цвет
            sequenceText.text = "Теперь твоя очередь!"
            isInputEnabled = true
        }, delay)
    }

}
