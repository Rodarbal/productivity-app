package com.example.productivityapp.ui.theme
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.productivityapp.MainActivity
import com.example.productivityapp.R

class LevelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.productivityapp.R.layout.activity_levels)

        val timerButton = findViewById<ImageView>(R.id.timerButton)
        timerButton?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}