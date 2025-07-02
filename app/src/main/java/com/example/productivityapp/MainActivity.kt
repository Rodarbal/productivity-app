package com.example.productivityapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.example.productivityapp.ui.theme.LevelsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var timerRing: TimerRingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Make sure this matches your layout filename

        // Get reference to the view
        timerRing = findViewById(R.id.timerRing)

        // Sync timerRing from shared state instead of overriding duration/isRunning
        timerRing.syncFromState = true

        val pauseButton = findViewById<ImageButton>(R.id.pauseButton)
        val initialIcon = if (TimerState.isRunning) R.drawable.ic_pause else R.drawable.ic_play
        pauseButton.setImageResource(initialIcon)
        pauseButton.setOnClickListener {
            timerRing.isRunning = !timerRing.isRunning
            val iconRes = if (timerRing.isRunning) R.drawable.ic_pause else R.drawable.ic_play
            pauseButton.setImageResource(iconRes)
        }



        val timerRingView = findViewById<TimerRingView>(R.id.timerRing)
        timerRing.syncFromState = true // <- default anyway
        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            // Always stop the timer and reset it
            timerRingView.resetTimer()

            // Update the pause button to show play icon
            pauseButton.setImageResource(R.drawable.ic_play)
        }

        val levelButton = findViewById<ImageView?>(R.id.levelButton)
        levelButton?.setOnClickListener {
            timerRingView.isRunning = false
            val intent = Intent(this, LevelsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        timerRing.onTimerFinished = {
            pauseButton.setImageResource(R.drawable.ic_play)
        }
        // OPTIONAL: Stop it later
        // timerRing.isRunning = false

        // OPTIONAL: Reset the timer
        // timerRing.resetTimer()
    }
}
