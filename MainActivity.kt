package com.ytshortsspeed

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.slider.Slider
import com.ytshortsspeed.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("YTShortsSpeed", Context.MODE_PRIVATE)

        // Load saved values
        val isEnabled = prefs.getBoolean("isEnabled", false)
        val holdMs = prefs.getInt("holdMs", 2000)
        val speedPct = prefs.getInt("speedPct", 200)

        binding.switchEnable.isChecked = isEnabled
        binding.sliderHold.value = holdMs.toFloat()
        binding.sliderSpeed.value = speedPct.toFloat()

        updateHoldLabel(holdMs)
        updateSpeedLabel(speedPct)
        updateUI(isEnabled)

        // Switch toggle
        binding.switchEnable.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean("isEnabled", checked).apply()
            updateUI(checked)
        }

        // Hold duration slider
        binding.sliderHold.addOnChangeListener { _, value, _ ->
            val ms = value.toInt()
            prefs.edit().putInt("holdMs", ms).apply()
            updateHoldLabel(ms)
        }

        // Speed slider
        binding.sliderSpeed.addOnChangeListener { _, value, _ ->
            val pct = value.toInt()
            prefs.edit().putInt("speedPct", pct).apply()
            updateSpeedLabel(pct)
        }

        // Permission buttons
        binding.btnAccessibility.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        binding.btnOverlay.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    private fun updateUI(enabled: Boolean) {
        if (enabled) {
            binding.tvToggleSub.text = getString(R.string.enabled_msg)
            binding.tvToggleSub.setTextColor(ContextCompat.getColor(this, R.color.green_active))
            binding.tvStatusDot.setTextColor(ContextCompat.getColor(this, R.color.green_active))
            binding.tvStatus.text = getString(R.string.status_active)
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.text_white))
        } else {
            binding.tvToggleSub.text = getString(R.string.disabled_tap)
            binding.tvToggleSub.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.tvStatusDot.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
            binding.tvStatus.text = getString(R.string.status_inactive)
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.text_gray))
        }
    }

    private fun updateHoldLabel(ms: Int) {
        binding.tvHoldValue.text = "${ms / 1000.0} seconds"
    }

    private fun updateSpeedLabel(pct: Int) {
        binding.tvSpeedValue.text = "${pct / 100.0}×"
    }

    private fun checkPermissions() {
        // Check accessibility
        val accessEnabled = isAccessibilityServiceEnabled()
        if (accessEnabled) {
            binding.tvAccessStatus.text = getString(R.string.granted)
            binding.tvAccessStatus.setTextColor(ContextCompat.getColor(this, R.color.green_active))
        } else {
            binding.tvAccessStatus.text = getString(R.string.not_granted)
            binding.tvAccessStatus.setTextColor(ContextCompat.getColor(this, R.color.red_inactive))
        }

        // Check overlay
        val overlayEnabled = Settings.canDrawOverlays(this)
        if (overlayEnabled) {
            binding.tvOverlayStatus.text = getString(R.string.granted)
            binding.tvOverlayStatus.setTextColor(ContextCompat.getColor(this, R.color.green_active))
        } else {
            binding.tvOverlayStatus.text = getString(R.string.not_granted)
            binding.tvOverlayStatus.setTextColor(ContextCompat.getColor(this, R.color.red_inactive))
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabledServices.contains(packageName)
    }
}
