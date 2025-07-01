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

    var durationMillis: Long = 10000L // default 10 seconds
    var isRunning: Boolean = true
        set(value) {
            field = value
            if (value) {
                startTimer()
            } else {
                stopTimer()
            }
        }

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
        initFromState()
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
        canvas.drawText(timeText, centerX, centerY, textPaint)

        drawProgressCircles(canvas, centerX, centerY)
    }

    private fun drawProgressCircles(canvas: Canvas, centerX: Float, centerY: Float) {
        val circleRadius = 18f
        val spacing = 24f
        val totalWidth = (circleRadius * 2 * 5) + (spacing * 4)
        val startX = centerX - (totalWidth / 2) + 20f
        val circleY = centerY + 70f

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
        for (i in 0 until TimerState.completions) {
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
