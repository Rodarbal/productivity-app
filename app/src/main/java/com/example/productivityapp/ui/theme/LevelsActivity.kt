package com.example.productivityapp.ui.theme
import android.widget.TextView
import android.annotation.SuppressLint
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
import com.example.productivityapp.LevelState

class LevelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_levels)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        viewPager.adapter = LevelPagerAdapter(this)
        val sortedLevels = LevelState.levels.sortedByDescending { it.location }
        val selectedIndex = sortedLevels.indexOfFirst { it.selected }
        viewPager.setCurrentItem(selectedIndex, false)

        // Set the timerButton click listener here (outside adapter)
        val timerButton = findViewById<View>(R.id.timerButton)
        timerButton?.setOnClickListener {
            val intent = Intent(this@LevelsActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }

    inner class LevelPagerAdapter(private val context: android.content.Context) : RecyclerView.Adapter<LevelPagerAdapter.LevelViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.page_level, parent, false)
            return LevelViewHolder(view)
        }

        override fun getItemCount(): Int = LevelState.levels.size

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
            val level = LevelState.levels.sortedByDescending { it.location }[position]
            val timerRing = holder.itemView.findViewById<TimerRingView>(R.id.timerRing)
            // val levelButton = holder.itemView.findViewById<View>(R.id.timerButton)
            // The timerButton click listener is now handled in onCreate()

            val selectButton = holder.itemView.findViewById<View>(R.id.selectLevel)
            selectButton?.setOnClickListener {
                LevelState.selectLevel(level.location)
                TimerState.initializeFromSelectedLevel(level)
                notifyDataSetChanged()
            }

            // dummy timer
            timerRing.syncFromState = false
            timerRing.durationMillis = level.timeMillis
            timerRing.isRunning = false
            timerRing.showFullRing()
            timerRing.completedCount = TimerState.currentTimer.completions

            // Set background color based solely on the colour property
            holder.itemView.setBackgroundColor(level.colour)

            timerRing.ringColor = level.uiColor
            timerRing.levelLabel = level.level
            timerRing.levelName = level.name

            val totalSeconds = level.timeMillis / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            timerRing.customTimeText = String.format("%02d:%02d", minutes, seconds)

        }

        inner class LevelViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }
}