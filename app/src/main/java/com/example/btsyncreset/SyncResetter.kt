package com.example.btsyncreset

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.KeyEvent

/**
 * Tier 1 "soft reset": pause whatever is playing, wait, then resume.
 * Pausing lets the Bluetooth audio pipeline drain/flush its buffer, which is
 * what removes the built-up delay — the same effect as reconnecting,
 * without touching the Bluetooth connection itself.
 */
object SyncResetter {

    enum class Result { STARTED, NOTHING_PLAYING, NO_BLUETOOTH_AUDIO, BUSY }

    private val handler = Handler(Looper.getMainLooper())

    @Volatile
    private var running = false

    fun reset(context: Context, onFinished: (() -> Unit)? = null): Result {
        val appContext = context.applicationContext
        val audio = appContext.getSystemService(AudioManager::class.java)

        if (running) return Result.BUSY
        if (!hasBluetoothOutput(audio)) return Result.NO_BLUETOOTH_AUDIO
        // Guard: never send "play" if nothing was playing, or we'd start random media.
        if (!audio.isMusicActive) return Result.NOTHING_PLAYING

        running = true
        val pauseMs = Prefs.pauseMs(appContext).toLong()

        sendMediaKey(audio, KeyEvent.KEYCODE_MEDIA_PAUSE)
        handler.postDelayed({
            sendMediaKey(audio, KeyEvent.KEYCODE_MEDIA_PLAY)
            running = false
            onFinished?.invoke()
        }, pauseMs)

        return Result.STARTED
    }

    private fun sendMediaKey(audio: AudioManager, keyCode: Int) {
        val now = SystemClock.uptimeMillis()
        audio.dispatchMediaKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0))
        audio.dispatchMediaKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0))
    }

    fun hasBluetoothOutput(audio: AudioManager): Boolean {
        val btTypes = mutableSetOf(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            btTypes += AudioDeviceInfo.TYPE_BLE_HEADSET
            btTypes += AudioDeviceInfo.TYPE_BLE_SPEAKER
        }
        return audio.getDevices(AudioManager.GET_DEVICES_OUTPUTS).any { it.type in btTypes }
    }
}
