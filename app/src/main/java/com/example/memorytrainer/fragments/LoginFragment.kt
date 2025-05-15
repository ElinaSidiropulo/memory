package com.example.memorytrainer.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.memorytrainer.R
import com.example.memorytrainer.activities.MainActivity
import com.example.memorytrainer.utils.UserManager

class LoginFragment : Fragment() {
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        usernameInput = view.findViewById(R.id.edit_username)
        passwordInput = view.findViewById(R.id.edit_password)
        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val btnGoRegister = view.findViewById<Button>(R.id.btn_go_register)

        val userManager = UserManager(requireContext())

        btnLogin.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (userManager.loginUser(username, password)) {
                Toast.makeText(context, "Успешный вход!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(context, "Неверные данные!", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
