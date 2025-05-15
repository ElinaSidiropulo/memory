package com.example.memorytrainer.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.memorytrainer.R
import com.example.memorytrainer.utils.UserManager

class RegisterFragment : Fragment() {
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        usernameInput = view.findViewById(R.id.edit_new_username)
        passwordInput = view.findViewById(R.id.edit_new_password)
        val btnRegister = view.findViewById<Button>(R.id.btn_register)
        val btnGoLogin = view.findViewById<Button>(R.id.btn_go_login)

        val userManager = UserManager(requireContext())

        btnRegister.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Введите имя и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = userManager.registerUser(username, password)
            if (success) {
                Toast.makeText(context, "Пользователь зарегистрирован!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() // Возврат к экрану входа
            } else {
                Toast.makeText(context, "Пользователь уже существует!", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoLogin.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
