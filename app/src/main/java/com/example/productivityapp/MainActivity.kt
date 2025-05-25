package com.example.productivityapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerRing: TimerRingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Make sure this matches your layout filename

        // Get reference to the view
        timerRing = findViewById(R.id.timerRing)

        // Set how long the timer should run (e.g. 10 seconds = 10,000 milliseconds)
        timerRing.durationMillis = 10000L // 10 seconds

        // Start the timer
        timerRing.isRunning = true

        // OPTIONAL: Stop it later
        // timerRing.isRunning = false

        // OPTIONAL: Reset the timer
        // timerRing.resetTimer()
    }
}
