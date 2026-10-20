package com.example.btsyncreset

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class SyncTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        showIdle()
    }

    override fun onClick() {
        super.onClick()
        val result = SyncResetter.reset(this) { showIdle() }
        val tile = qsTile ?: return
        when (result) {
            SyncResetter.Result.STARTED -> {
                tile.state = Tile.STATE_ACTIVE
                tile.subtitle = getString(R.string.tile_resetting)
            }
            SyncResetter.Result.NOTHING_PLAYING -> {
                tile.state = Tile.STATE_INACTIVE
                tile.subtitle = getString(R.string.tile_nothing_playing)
            }
            SyncResetter.Result.NO_BLUETOOTH_AUDIO -> {
                tile.state = Tile.STATE_INACTIVE
                tile.subtitle = getString(R.string.tile_no_bt)
            }
            SyncResetter.Result.BUSY -> return
        }
        safeUpdate(tile)
    }

    private fun showIdle() {
        val tile = qsTile ?: return
        tile.state = Tile.STATE_INACTIVE
        tile.subtitle = null
        safeUpdate(tile)
    }

    private fun safeUpdate(tile: Tile) {
        try { tile.updateTile() } catch (_: Exception) { /* tile no longer bound */ }
    }
}
