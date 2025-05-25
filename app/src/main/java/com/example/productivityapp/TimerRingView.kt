package com.example.productivityapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.min

class TimerRingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    var durationMillis: Long = 10000L // default 10 seconds
    var isRunning: Boolean = false
        set(value) {
            field = value
            if (value) startTimer() else stopTimer()
        }

    private var remainingTime: Long = durationMillis
    private var startTime: Long = 0L
    private var angle: Float = 0f

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 30f
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 30f
        strokeCap = Paint.Cap.ROUND
    }

    private val updateInterval: Long = 16L // ~60fps
    private val updateRunnable = object : Runnable {
        override fun run() {
            val elapsed = System.currentTimeMillis() - startTime
            remainingTime = (durationMillis - elapsed).coerceAtLeast(0)
            angle = 360f * remainingTime.toFloat() / durationMillis
            invalidate()
            if (remainingTime > 0 && isRunning) {
                postDelayed(this, updateInterval)
            }
        }
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis() - (durationMillis - remainingTime)
        removeCallbacks(updateRunnable)
        post(updateRunnable)
    }

    private fun stopTimer() {
        removeCallbacks(updateRunnable)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val padding = 40f
        val size = min(width, height).toFloat()
        val rect = RectF(padding, padding, size - padding, size - padding)

        // Draw the base ring
        canvas.drawOval(rect, ringPaint)

        // Draw the progress arc
        canvas.drawArc(rect, -90f, -angle, false, progressPaint)
    }

    fun resetTimer() {
        stopTimer()
        remainingTime = durationMillis
        angle = 360f
        invalidate()
    }
}
