package com.example.memorytrainer.models

import androidx.annotation.DrawableRes

data class GameItem(
    val title: String,
    val description: String,
    val activityClass: Class<*>,
    @DrawableRes val iconResId: Int? = null
)