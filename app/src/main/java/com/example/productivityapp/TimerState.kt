package com.example.productivityapp

object TimerState {
    var durationMillis: Long = 60000L
    var remainingTime: Long = 60000L
    var isRunning: Boolean = false
    var lastStartTime: Long = 0L
    var completions: Int = 0
}