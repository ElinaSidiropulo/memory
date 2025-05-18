package com.example.memorytrainer.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.memorytrainer.R

class RulesDialog(context: Context) : Dialog(context) {

    private lateinit var selectedCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_rules)

        val tvRules = findViewById<TextView>(R.id.tv_rules)

        val cardGame1 = findViewById<CardView>(R.id.card_game1)
        val cardGame2 = findViewById<CardView>(R.id.card_game2)
        val cardGame3 = findViewById<CardView>(R.id.card_game3)

        val allCards = listOf(cardGame1, cardGame2, cardGame3)

        // Функция обновления фона карточек
        fun selectCard(card: CardView, rulesTextResId: Int) {
            // Сброс фона у всех
            allCards.forEach {
                it.setCardBackgroundColor(context.getColor(android.R.color.white))
            }

            // Установка серого фона выбранной
            card.setCardBackgroundColor(context.getColor(android.R.color.darker_gray))

            // Установка текста правил
            tvRules.text = context.getString(rulesTextResId)

            selectedCard = card
        }

        // Инициализация: Игра 1 выбрана по умолчанию
        selectCard(cardGame1, R.string.rules_game1)

        cardGame1.setOnClickListener {
            selectCard(cardGame1, R.string.rules_game1)
        }

        cardGame2.setOnClickListener {
            selectCard(cardGame2, R.string.rules_game2)
        }

        cardGame3.setOnClickListener {
            selectCard(cardGame3, R.string.rules_game3)
        }

        findViewById<TextView>(R.id.btn_close).setOnClickListener {
            dismiss()
        }
    }
}
