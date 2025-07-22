package com.example.productivityapp.ui.theme
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.productivityapp.MainActivity
import com.example.productivityapp.R
import com.example.productivityapp.TimerRingView
import com.example.productivityapp.TimerState
import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class LevelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_levels)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewPager.adapter = LevelPagerAdapter()
    }

    inner class LevelPagerAdapter : RecyclerView.Adapter<LevelPagerAdapter.LevelViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.page_level, parent, false)
            return LevelViewHolder(view)
        }

        override fun getItemCount(): Int = 3

        override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
            val timerRing = holder.itemView.findViewById<TimerRingView>(R.id.timerRing)
            val levelButton = holder.itemView.findViewById<View>(R.id.levelButton)
            levelButton?.setOnClickListener {
                val intent = Intent(this@LevelsActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }

            timerRing.syncFromState = false
            timerRing.durationMillis = 10000L
            timerRing.isRunning = false
            timerRing.showFullRing()
            timerRing.completedCount = TimerState.completions
        }

        inner class LevelViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }
}