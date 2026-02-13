package com.ytshortsspeed

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class ShortsDetectorService : AccessibilityService() {

    private var isOnShorts = false
    private var isEnabled = false

    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Check if we're in YouTube
        val packageName = event.packageName?.toString() ?: return
        if (packageName != "com.google.android.youtube") {
            if (isOnShorts) {
                isOnShorts = false
                stopOverlayService()
            }
            return
        }

        // Check if user enabled the feature
        val prefs = getSharedPreferences("YTShortsSpeed", Context.MODE_PRIVATE)
        isEnabled = prefs.getBoolean("isEnabled", false)

        if (!isEnabled) {
            if (isOnShorts) {
                isOnShorts = false
                stopOverlayService()
            }
            return
        }

        // Detect if we're on Shorts by checking window content
        val className = event.className?.toString() ?: ""
        val contentDesc = event.contentDescription?.toString() ?: ""

        // YouTube Shorts detection heuristics
        val onShortsNow = className.contains("Shorts", ignoreCase = true) ||
                contentDesc.contains("Shorts", ignoreCase = true) ||
                event.text?.any { it.contains("Shorts", ignoreCase = true) } == true

        if (onShortsNow && !isOnShorts) {
            isOnShorts = true
            startOverlayService()
        } else if (!onShortsNow && isOnShorts) {
            isOnShorts = false
            stopOverlayService()
        }
    }

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        startService(intent)
    }

    private fun stopOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        stopService(intent)
    }

    override fun onInterrupt() {
        stopOverlayService()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopOverlayService()
    }
}
