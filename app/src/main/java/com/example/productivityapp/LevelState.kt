package com.example.productivityapp

import android.graphics.Color
import androidx.core.graphics.toColorInt

object LevelState {
    data class Level(
        val colour: Int,
        val timeMillis: Long,
        val location: Int,
        val uiColor: Int,
        val name: String,
        val level: String,
        var completions: Int,
        var selected: Boolean
    )

    val levels = mutableListOf(
        Level(
            colour = "#ccccff".toColorInt(),
            timeMillis = 60000L,
            location = 0,
            uiColor = "#0000ff".toColorInt(),
            name = "Monkey Mind",
            level = "Level 1",
            completions = 0,
            selected = true
        ),
        Level(
            colour = "#ffe5e5".toColorInt(),
            timeMillis = 600000L,
            location = 1,
            uiColor = "#ff4d4d".toColorInt(),
            name = "Noob",
            level = "Level 2",
            completions = 0,
            selected = false
        ),
        Level(
            colour = "#e6ffe6".toColorInt(),
            timeMillis = 1800000L,
            location = 2,
            uiColor = "#00cc44".toColorInt(),
            name = "Average",
            level = "Level 3",
            completions = 0,
            selected = false
        ),
        Level(
            colour = "#ffffff".toColorInt(),
            timeMillis = 10000L,
            location = 3,
            uiColor = "#000000".toColorInt(),
            name = "Developer",
            level = "Level Dev",
            completions = 0,
            selected = false
        )
    )

    fun getSelectedLevel(): Level? = levels.find { it.selected }

    fun selectLevel(index: Int) {
        levels.forEachIndexed { i, level -> level.selected = (i == index) }
    }
}