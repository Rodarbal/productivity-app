package com.example.productivityapp

import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.example.productivityapp.ui.theme.LevelsActivity
import android.app.NotificationChannel
import androidx.core.app.NotificationCompat
import android.content.Context
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


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

        createNotificationChannel(this)
        // OPTIONAL: Stop it later
        // timerRing.isRunning = false

        // OPTIONAL: Reset the timer
        // timerRing.resetTimer()
    }
}

// Create notification channel and request notification permission if needed
private fun createNotificationChannel(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Timer Notifications"
        val descriptionText = "Notifies when timer completes"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("timer_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Android 13+ needs POST_NOTIFICATIONS permission
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }
}