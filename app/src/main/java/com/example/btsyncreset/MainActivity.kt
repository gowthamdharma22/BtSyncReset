package com.example.btsyncreset

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.StatusBarManager
import android.content.ComponentName
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var status: TextView
    private lateinit var pauseLabel: TextView
    private lateinit var autoSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.status)
        pauseLabel = findViewById(R.id.pause_label)
        autoSwitch = findViewById(R.id.auto_switch)
        val resetButton = findViewById<Button>(R.id.reset_button)
        val pauseSeek = findViewById<SeekBar>(R.id.pause_seek)
        val addTileButton = findViewById<Button>(R.id.add_tile_button)

        // --- Reset button ---
        resetButton.setOnClickListener {
            val result = SyncResetter.reset(this) { status.setText(R.string.status_done) }
            status.text = when (result) {
                SyncResetter.Result.STARTED -> getString(R.string.status_resetting, Prefs.pauseMs(this))
                SyncResetter.Result.NOTHING_PLAYING -> getString(R.string.status_nothing_playing)
                SyncResetter.Result.NO_BLUETOOTH_AUDIO -> getString(R.string.status_no_bt)
                SyncResetter.Result.BUSY -> getString(R.string.status_busy)
            }
        }

        // --- Pause length slider ---
        pauseSeek.min = Prefs.MIN_PAUSE_MS
        pauseSeek.max = Prefs.MAX_PAUSE_MS
        pauseSeek.progress = Prefs.pauseMs(this)
        updatePauseLabel(pauseSeek.progress)
        pauseSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, value: Int, fromUser: Boolean) {
                val rounded = (value / 100) * 100
                updatePauseLabel(rounded)
                if (fromUser) Prefs.setPauseMs(this@MainActivity, rounded)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // --- Auto-reset on connect ---
        autoSwitch.isChecked = Prefs.autoOnConnect(this) && hasBtPermission()
        autoSwitch.setOnCheckedChangeListener { _, checked ->
            if (checked && !hasBtPermission()) {
                requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQ_BT)
            } else {
                Prefs.setAutoOnConnect(this, checked)
            }
        }

        // --- Add Quick Settings tile (Android 13+) ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            addTileButton.setOnClickListener { requestTile() }
        } else {
            addTileButton.visibility = View.GONE
        }
    }

    private fun updatePauseLabel(ms: Int) {
        pauseLabel.text = getString(R.string.pause_label, ms)
    }

    private fun hasBtPermission(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQ_BT) return
        val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        Prefs.setAutoOnConnect(this, granted)
        if (!granted) {
            autoSwitch.isChecked = false
            status.setText(R.string.status_permission_denied)
        }
    }

    @TargetApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestTile() {
        val sbm = getSystemService(StatusBarManager::class.java) ?: return
        sbm.requestAddTileService(
            ComponentName(this, SyncTileService::class.java),
            getString(R.string.tile_label),
            Icon.createWithResource(this, R.drawable.ic_sync),
            mainExecutor
        ) { result: Int ->
            status.text = when (result) {
                StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED,
                StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED -> getString(R.string.status_tile_added)
                else -> getString(R.string.status_tile_not_added)
            }
        }
    }

    companion object {
        private const val REQ_BT = 1
    }
}
