package com.example.memorytrainer.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytrainer.databinding.ActivityMemorizeWordsBinding

class MemorizeWordsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMemorizeWordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemorizeWordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewTitle.text = "Запомни слова"
    }
}
