package com.example.memorytrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memorytrainer.R
import com.example.memorytrainer.models.Card
import com.example.memorytrainer.utils.ThemeManager

class GameAdapter(
    private val cards: MutableList<Card>,
    private val onCardClick: (Card) -> Unit
) : RecyclerView.Adapter<GameAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.cardText)

        fun bind(card: Card) {
            // Показываем текст на карточке, если она перевернута
            textView.text = if (card.isFlipped) card.name else "?" // Показываем имя карточки или знак вопроса

            // Устанавливаем обработчик нажатия на карточку
            itemView.setOnClickListener {
                // Проверяем, перевернута ли карточка
                if (!card.isFlipped) {
                    onCardClick(card)
                    // После того как карточка была нажата, уведомляем адаптер о изменении
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int = cards.size
}
