package com.example.productivityapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button


class MainActivity : AppCompatActivity() {

    private lateinit var timerRing: TimerRingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Make sure this matches your layout filename

        // Get reference to the view
        timerRing = findViewById(R.id.timerRing)

        // Set how long the timer should run (e.g. 10 seconds = 10,000 milliseconds)
        timerRing.durationMillis = 100000L // 10 seconds

        // Start the timer
        timerRing.isRunning = true

        val pauseButton = findViewById<Button>(R.id.pauseButton)
        pauseButton.setOnClickListener {
            timerRing.isRunning = !timerRing.isRunning
            pauseButton.text = if (timerRing.isRunning) "Pause" else "Resume"
        }

        // OPTIONAL: Stop it later
        // timerRing.isRunning = false

        // OPTIONAL: Reset the timer
        // timerRing.resetTimer()
    }
}
