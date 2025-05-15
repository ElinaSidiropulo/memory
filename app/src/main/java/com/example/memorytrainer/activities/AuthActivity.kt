package com.example.memorytrainer.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytrainer.R
import com.example.memorytrainer.fragments.LoginFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Загружаем фрагмент логина по умолчанию
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment_container, LoginFragment())
                .commit()
        }
    }
}
