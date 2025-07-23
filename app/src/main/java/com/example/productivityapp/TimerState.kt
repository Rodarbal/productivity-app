package com.example.productivityapp

data class TimerData(
    var durationMillis: Long = 60000L,
    var remainingTime: Long = 60000L,
    var isRunning: Boolean = false,
    var lastStartTime: Long = 0L,
    //var completions: Int = 0
)

object TimerState {
    var currentTimer = TimerData()

    fun initializeFromSelectedLevel(level: LevelState.Level) {
        currentTimer.durationMillis = level.timeMillis
        currentTimer.remainingTime = level.timeMillis
        currentTimer.isRunning = false
        currentTimer.lastStartTime = 0L
        //currentTimer.completions = 0
    }
}