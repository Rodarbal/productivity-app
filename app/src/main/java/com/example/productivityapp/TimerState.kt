package com.example.productivityapp

object TimerState {
    var durationMillis: Long = 10000L
    var remainingTime: Long = 10000L
    var isRunning: Boolean = false
    var lastStartTime: Long = 0L
}