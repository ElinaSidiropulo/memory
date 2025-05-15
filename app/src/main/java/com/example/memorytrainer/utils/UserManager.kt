package com.example.memorytrainer.utils

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

class UserManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("MemoryTrainerPrefs", Context.MODE_PRIVATE)

    private val USERS_KEY = "users"
    private val CURRENT_USER_KEY = "current_user"

    fun registerUser(username: String, password: String): Boolean {
        val users = getUsersMap()
        if (users.containsKey(username)) return false // Уже зарегистрирован

        users[username] = password
        saveUsersMap(users)
        return true
    }

    fun loginUser(username: String, password: String): Boolean {
        val users = getUsersMap()
        val storedPassword = users[username]

        return if (storedPassword != null && storedPassword == password) {
            prefs.edit().putString(CURRENT_USER_KEY, username).apply()
            true
        } else {
            false
        }
    }

    fun logoutUser() {
        prefs.edit().remove(CURRENT_USER_KEY).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.contains(CURRENT_USER_KEY)
    }

    fun getCurrentUsername(): String? {
        return prefs.getString(CURRENT_USER_KEY, null)
    }

    private fun getUsersMap(): MutableMap<String, String> {
        val jsonString = prefs.getString(USERS_KEY, "{}")
        val jsonObject = JSONObject(jsonString!!)
        val map = mutableMapOf<String, String>()
        jsonObject.keys().forEach { key ->
            map[key] = jsonObject.getString(key)
        }
        return map
    }

    private fun saveUsersMap(map: Map<String, String>) {
        val jsonObject = JSONObject(map)
        prefs.edit().putString(USERS_KEY, jsonObject.toString()).apply()
    }
}
