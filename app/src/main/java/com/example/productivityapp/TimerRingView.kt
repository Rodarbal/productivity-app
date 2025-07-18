package com.example.productivityapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class TimerRingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    var onTimerFinished: (() -> Unit)? = null

    var durationMillis: Long = 10000L // default 10 seconds
    var isRunning: Boolean = true
        set(value) {
            field = value
            if (syncFromState) {
                if (value) {
                    startTimer()
                } else {
                    stopTimer()
                }
            } else {
                // If not syncing, just stop updates but do not touch shared state
                if (value) {
                    removeCallbacks(updateRunnable)
                }
            }
        }
    var completedCount : Int = TimerState.completions

    var syncFromState: Boolean = true

    private var remainingTime: Long = durationMillis
    private var startTime: Long = 0L
    private var angle: Float = 0f

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#ccccff") //c7dbff or ccccff
        style = Paint.Style.STROKE
        strokeWidth = 30f
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 30f
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 200f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, 500, false) // 500 = Medium weight
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
            } else if (remainingTime <= 0 && TimerState.completions < 5) {
                TimerState.completions++
                completedCount = TimerState.completions
                invalidate()
                onTimerFinished?.invoke()
            }
        }
    }

    fun showFullRing() {
        angle = 360f
        invalidate()
    }

    private fun startTimer() {
        if (remainingTime <= 0) {
            isRunning = false
            return
        }
        startTime = System.currentTimeMillis() - (durationMillis - remainingTime)
        removeCallbacks(updateRunnable)
        post(updateRunnable)
    }

    fun stopTimer() {
        removeCallbacks(updateRunnable)
        TimerState.remainingTime = remainingTime
        TimerState.isRunning = isRunning
        TimerState.lastStartTime = startTime
    }
    fun initFromState() {
        durationMillis = TimerState.durationMillis
        remainingTime = TimerState.remainingTime

        if (TimerState.isRunning) {
            val elapsed = System.currentTimeMillis() - TimerState.lastStartTime
            remainingTime = (TimerState.remainingTime - elapsed).coerceAtLeast(0)
        }

        isRunning = TimerState.isRunning
        angle = 360f * remainingTime.toFloat() / durationMillis
        invalidate()

        if (isRunning) {
            startTimer()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (syncFromState) {
            initFromState()
        }
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

        val centerX = width / 2f
        val centerY = height / 2f
        val secondsLeft = (remainingTime / 1000).toInt()
        val minutes = secondsLeft / 60
        val seconds = secondsLeft % 60
        val timeText = String.format("%02d:%02d", minutes, seconds)

        // Level Line
        val levelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.DKGRAY
            textSize = 48f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        canvas.drawText("Level 1", centerX, centerY - 230f, levelPaint)

        // Second line: "Beginner Mind"
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.DKGRAY
            textSize = 64f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("Beginner Mind", centerX, centerY - 160f, titlePaint)

        // Third line: timer text (already in place)
        canvas.drawText(timeText, centerX, centerY + 20f, textPaint)

        drawProgressCircles(canvas, centerX, centerY, completedCount)
    }

    private fun drawProgressCircles(canvas: Canvas, centerX: Float, centerY: Float, completes : Int ) {
        val circleRadius = 18f
        val spacing = 24f
        val totalWidth = (circleRadius * 2 * 5) + (spacing * 4)
        val startX = centerX - (totalWidth / 2) + 20f
        val circleY = centerY + 120f

        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = Color.parseColor("#0000FF")
        }

        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#0000FF")
        }

        val hollowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }

        // Draw all hollow circles
        for (i in 0 until 5) {
            val cx = startX + i * (circleRadius * 2 + spacing)
            canvas.drawCircle(cx, circleY, circleRadius, hollowPaint)
            canvas.drawCircle(cx, circleY, circleRadius, strokePaint)
        }

        // Fill in first 5 (temporary placeholder)
        for (i in 0 until completes) {
            val cx = startX + i * (circleRadius * 2 + spacing)
            canvas.drawCircle(cx, circleY, circleRadius, fillPaint)
        }
    }

    fun resetTimer() {
        stopTimer()
        remainingTime = durationMillis
        angle = 360f
        TimerState.remainingTime = remainingTime
        TimerState.isRunning = false
        TimerState.lastStartTime = 0L
        invalidate()
    }
}
