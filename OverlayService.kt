package com.ytshortsspeed

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout

class OverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var holdMs = 2000L
    private var speedPct = 200
    private var isSpeedActive = false

    private val handler = Handler(Looper.getMainLooper())
    private var holdRunnable: Runnable? = null
    private var pressStartTime = 0L

    override fun onCreate() {
        super.onCreate()
        createOverlay()
    }

    private fun createOverlay() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Load settings
        val prefs = getSharedPreferences("YTShortsSpeed", Context.MODE_PRIVATE)
        holdMs = prefs.getInt("holdMs", 2000).toLong()
        speedPct = prefs.getInt("speedPct", 200)

        // Create transparent full-screen overlay
        overlayView = FrameLayout(this).apply {
            setBackgroundColor(0x00000000) // Fully transparent
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START

        overlayView?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    pressStartTime = System.currentTimeMillis()
                    scheduleSpeedChange()
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val pressDuration = System.currentTimeMillis() - pressStartTime
                    cancelSpeedChange()

                    if (isSpeedActive) {
                        // Was at 2× speed — restore normal
                        restoreNormalSpeed()
                    } else if (pressDuration < holdMs) {
                        // Short tap — let YouTube handle it (pause/play)
                        // Do nothing, pass through
                    }
                    false
                }
                else -> false
            }
        }

        windowManager?.addView(overlayView, params)
    }

    private fun scheduleSpeedChange() {
        holdRunnable = Runnable {
            if (!isSpeedActive) {
                activateSpeed()
            }
        }
        handler.postDelayed(holdRunnable!!, holdMs)
    }

    private fun cancelSpeedChange() {
        holdRunnable?.let {
            handler.removeCallbacks(it)
            holdRunnable = null
        }
    }

    private fun activateSpeed() {
        isSpeedActive = true
        // Here you would inject JavaScript or use accessibility actions
        // to change YouTube's playback speed. This requires more advanced
        // techniques like using AccessibilityNodeInfo to find and click
        // the speed button, or injecting JavaScript via WebView intercept.
        
        // For now, this is a placeholder.
        // Real implementation would require:
        // 1. Root access + shell commands to simulate taps on speed button
        // 2. Or Xposed/Magisk module to hook YouTube's player
        // 3. Or reverse-engineering YouTube's WebView to inject JS
        
        // As a demonstration, we'll just log it:
        android.util.Log.d("YTShortsSpeed", "Speed activated at $speedPct%")
    }

    private fun restoreNormalSpeed() {
        isSpeedActive = false
        // Restore to 1× speed
        android.util.Log.d("YTShortsSpeed", "Speed restored to normal")
    }

    override fun onDestroy() {
        super.onDestroy()
        overlayView?.let {
            windowManager?.removeView(it)
        }
        cancelSpeedChange()
        if (isSpeedActive) {
            restoreNormalSpeed()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
