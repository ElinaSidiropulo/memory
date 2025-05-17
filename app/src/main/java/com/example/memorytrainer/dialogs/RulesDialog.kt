package com.example.memorytrainer.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.memorytrainer.R

class RulesDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_rules)

        val tvRules = findViewById<TextView>(R.id.tv_rules)
        tvRules.text = context.getString(R.string.rules_game1)

        // Загрузка текста правил при выборе карточки
        findViewById<CardView>(R.id.card_game1).setOnClickListener {
            tvRules.text = context.getString(R.string.rules_game1)
        }

        findViewById<CardView>(R.id.card_game2).setOnClickListener {
            tvRules.text = context.getString(R.string.rules_game2)
        }

        findViewById<CardView>(R.id.card_game3).setOnClickListener {
            tvRules.text = context.getString(R.string.rules_game3)
        }

        findViewById<TextView>(R.id.btn_close).setOnClickListener {
            dismiss()
        }
    }
}
