package com.example.productivityapp

import android.graphics.Color
import androidx.core.graphics.toColorInt

object LevelState {
    data class Level(
        val colour: Int,
        val timeMillis: Long,
        val location: Int,
        var selected: Boolean
    )

    val levels = mutableListOf(
        Level(colour = "#ccccff".toColorInt(), timeMillis = 10000L, location = 0, selected = true),
        Level(colour = android.graphics.Color.RED, timeMillis = 15000L, location = 1, selected = false),
        Level(colour = android.graphics.Color.GREEN, timeMillis = 20000L, location = 2, selected = false)
    )

    fun getSelectedLevel(): Level? = levels.find { it.selected }

    fun selectLevel(index: Int) {
        levels.forEachIndexed { i, level -> level.selected = (i == index) }
    }
}