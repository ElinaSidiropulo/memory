package com.example.memorytrainer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memorytrainer.R
import com.example.memorytrainer.databinding.ItemGameBinding
import com.example.memorytrainer.models.GameItem

class GamesAdapter(
    private val games: List<GameItem>,
    private val onClick: (GameItem) -> Unit
) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    inner class GameViewHolder(val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        with(holder.binding) {
            textViewTitle.text = game.title
            textViewDescription.text = game.description
            gameIcon.setImageResource(game.iconResId ?: R.drawable.ic_games)
            root.setOnClickListener { onClick(game) }
        }
    }

    override fun getItemCount(): Int = games.size
}