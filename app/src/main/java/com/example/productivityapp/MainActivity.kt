package com.example.productivityapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton


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

        val pauseButton = findViewById<ImageButton>(R.id.pauseButton)
        pauseButton.setOnClickListener {
            timerRing.isRunning = !timerRing.isRunning
            val iconRes = if (timerRing.isRunning) R.drawable.ic_pause else R.drawable.ic_play
            pauseButton.setImageResource(iconRes)
        }

        val timerRingView = findViewById<TimerRingView>(R.id.timerRing)
        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            val wasRunning = timerRingView.isRunning
            timerRingView.resetTimer()
            timerRingView.isRunning = wasRunning
        }

        // OPTIONAL: Stop it later
        // timerRing.isRunning = false

        // OPTIONAL: Reset the timer
        // timerRing.resetTimer()
    }
}
