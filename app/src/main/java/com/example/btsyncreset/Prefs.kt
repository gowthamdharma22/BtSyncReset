package com.example.btsyncreset

import android.content.Context

object Prefs {
    const val DEFAULT_PAUSE_MS = 1000
    const val MIN_PAUSE_MS = 300
    const val MAX_PAUSE_MS = 5000

    private const val FILE = "settings"
    private const val KEY_PAUSE_MS = "pause_ms"
    private const val KEY_AUTO_ON_CONNECT = "auto_on_connect"

    private fun prefs(c: Context) =
        c.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun pauseMs(c: Context): Int =
        prefs(c).getInt(KEY_PAUSE_MS, DEFAULT_PAUSE_MS).coerceIn(MIN_PAUSE_MS, MAX_PAUSE_MS)

    fun setPauseMs(c: Context, value: Int) =
        prefs(c).edit().putInt(KEY_PAUSE_MS, value).apply()

    fun autoOnConnect(c: Context): Boolean =
        prefs(c).getBoolean(KEY_AUTO_ON_CONNECT, false)

    fun setAutoOnConnect(c: Context, value: Boolean) =
        prefs(c).edit().putBoolean(KEY_AUTO_ON_CONNECT, value).apply()
}
