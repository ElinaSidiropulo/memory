package com.example.memorytrainer.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memorytrainer.adapters.GamesAdapter
import com.example.memorytrainer.databinding.ActivityGamesBinding
import com.example.memorytrainer.models.GameItem

import com.example.memorytrainer.activities.FindPairsActivity
import com.example.memorytrainer.activities.RepeatSequenceActivity
import com.example.memorytrainer.activities.MemorizeWordsActivity

class GamesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGamesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gamesList = listOf(
            GameItem("Найди пары", "Запоминай и находи пары", FindPairsActivity::class.java),
            GameItem("Повтори последовательность", "Следуй за последовательностью", RepeatSequenceActivity::class.java),
            GameItem("Запомни ритм", "Запоминай ритм и проверяй себя", TapTheRightRhythmActivity::class.java)
        )

        binding.recyclerViewGames.apply {
            layoutManager = LinearLayoutManager(this@GamesActivity)
            adapter = GamesAdapter(gamesList) { game ->
                startActivity(Intent(this@GamesActivity, game.activityClass))
            }
        }
    }
}
