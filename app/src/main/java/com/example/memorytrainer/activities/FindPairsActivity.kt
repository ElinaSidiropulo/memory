package com.example.memorytrainer.activities

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPairsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initGame()

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }

        checkGameStatus()
    }

    private fun initGame() {
        cardList.clear()
        for (i in 1..8) {
            cardList.add(Card(i, "Card $i"))
            cardList.add(Card(i, "Card $i"))
        }
        cardList.shuffle()

        gameAdapter = GameAdapter(cardList) { card ->
            onCardClicked(card)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        binding.recyclerView.adapter = gameAdapter
    }

    private fun onCardClicked(card: Card) {
        if (card.isFlipped || flippedCards.contains(card)) return

        card.isFlipped = true
        flippedCards.add(card)
        gameAdapter.notifyItemChanged(cardList.indexOf(card))

        if (flippedCards.size == 2) {
            binding.recyclerView.postDelayed({
                checkForMatch()
            }, 1000)
        }
    }

    private fun checkForMatch() {
        if (flippedCards.size != 2) return

        val (card1, card2) = flippedCards
        if (card1.value == card2.value) {
            // Совпадение
            flippedCards.clear()
            checkGameStatus()
        } else {
            card1.isFlipped = false
            card2.isFlipped = false
            gameAdapter.notifyItemChanged(cardList.indexOf(card1))
            gameAdapter.notifyItemChanged(cardList.indexOf(card2))
            flippedCards.clear()
        }
    }

    private fun checkGameStatus() {
        if (cardList.all { it.isFlipped }) {
            showGameEndDialog()
        }
    }

    private fun showGameEndDialog() {
        AlertDialog.Builder(this)
            .setTitle("Поздравляем!")
            .setMessage("Вы нашли все пары! Хотите начать заново?")
            .setPositiveButton("Да") { _, _ -> initGame() }
            .setNegativeButton("Нет") { _, _ -> finish() }
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("cards", ArrayList(cardList))
    }

    private fun restoreState(savedInstanceState: Bundle) {
        val savedCards: List<Card>? = savedInstanceState.getParcelableArrayList("cards")
        savedCards?.let {
            cardList.clear()
            cardList.addAll(it)
            gameAdapter.notifyDataSetChanged()
        }
    }
}
