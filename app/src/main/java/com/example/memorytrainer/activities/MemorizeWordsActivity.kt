package com.example.memorytrainer.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytrainer.databinding.ActivityMemorizeWordsBinding
import com.example.memorytrainer.utils.ThemeManager

class MemorizeWordsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMemorizeWordsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMemorizeWordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewTitle.text = "Запомни слова"
    }
}
