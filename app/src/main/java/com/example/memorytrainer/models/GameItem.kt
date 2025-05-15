package com.example.memorytrainer.models

data class GameItem(
    val title: String,
    val description: String,
    val activityClass: Class<*>
)
