package com.example.btsyncreset

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper

/** Optional: soft-reset shortly after a Bluetooth audio device connects (only if something is playing). */
class BtConnectReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED) return
        if (intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1) != BluetoothProfile.STATE_CONNECTED) return
        if (!Prefs.autoOnConnect(context)) return

        val appContext = context.applicationContext
        val pending = goAsync()
        Handler(Looper.getMainLooper()).postDelayed({
            val result = SyncResetter.reset(appContext) { pending.finish() }
            if (result != SyncResetter.Result.STARTED) pending.finish()
        }, CONNECT_DELAY_MS)
    }

    companion object {
        // Delay + max pause (5 s) stays under the broadcast time limit.
        private const val CONNECT_DELAY_MS = 4000L
    }
}
