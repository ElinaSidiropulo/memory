// Key changes:
// - Added delayed hiding of single cards if no match
// - Ensured only matched cards stay flipped
// - Tracked game start time and calculated total duration

package com.example.memorytrainer.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.memorytrainer.R
import com.example.memorytrainer.models.Card
import com.example.memorytrainer.databinding.ActivityFindPairsBinding
import com.example.memorytrainer.adapters.GameAdapter

class FindPairsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindPairsBinding
    private val cardList = mutableListOf<Card>()
    private val flippedCards = mutableListOf<Card>()
    private lateinit var gameAdapter: GameAdapter
    private var startTime = 0L

    private val handler = Handler(Looper.getMainLooper())
    private val pendingHideTasks = mutableMapOf<Card, Runnable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPairsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initGame()
    }

    private fun initGame() {
        startTime = System.currentTimeMillis()

        cardList.clear()
        for (i in 1..8) {
            cardList.add(Card(i, "Card $i"))
            cardList.add(Card(i, "Card $i"))
        }
        cardList.shuffle()

        // Initialize match status for each card
        cardList.forEach { it.isMatched = false; it.isFlipped = false }

        gameAdapter = GameAdapter(cardList) { card ->
            onCardClicked(card)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        binding.recyclerView.adapter = gameAdapter
    }

    private fun onCardClicked(card: Card) {
        if (card.isFlipped || card.isMatched || flippedCards.contains(card)) return

        card.isFlipped = true
        gameAdapter.notifyItemChanged(cardList.indexOf(card))
        flippedCards.add(card)

        // Schedule hide after 3 seconds if no match is found
        val hideTask = Runnable {
            if (!card.isMatched) {
                card.isFlipped = false
                gameAdapter.notifyItemChanged(cardList.indexOf(card))
                flippedCards.remove(card)
            }
        }
        handler.postDelayed(hideTask, 3000)
        pendingHideTasks[card] = hideTask

        if (flippedCards.size == 2) {
            val card1 = flippedCards[0]
            val card2 = flippedCards[1]

            if (card1.value == card2.value) {
                // Match found
                card1.isMatched = true
                card2.isMatched = true

                flippedCards.clear()

                // Cancel hide tasks
                pendingHideTasks[card1]?.let { handler.removeCallbacks(it) }
                pendingHideTasks[card2]?.let { handler.removeCallbacks(it) }

                pendingHideTasks.remove(card1)
                pendingHideTasks.remove(card2)

                checkGameStatus()
            }
        }
    }

    private fun checkGameStatus() {
        if (cardList.all { it.isMatched }) {
            val elapsedTime = (System.currentTimeMillis() - startTime) / 1000
            showGameEndDialog(elapsedTime)
        }
    }

    private fun showGameEndDialog(elapsedSeconds: Long) {
        AlertDialog.Builder(this)
            .setTitle("Потрясающе!")
            .setMessage("Вы нашли все пары за $elapsedSeconds секунд! Хотите сыграть снова?")
            .setPositiveButton("Да") { _, _ -> initGame() }
            .setNegativeButton("Нет") { _, _ -> finish() }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Очищаем все таймеры при выходе
        pendingHideTasks.values.forEach { handler.removeCallbacks(it) }
        pendingHideTasks.clear()
    }
}
