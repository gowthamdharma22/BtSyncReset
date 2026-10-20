# BT Sync Reset

An Android app that fixes Bluetooth audio drifting out of sync with video.

## What it does

After a while, Bluetooth audio can lag behind the video. Disconnecting and reconnecting the
headset fixes it, but that's annoying to do by hand. This app does the same reset in one tap:
it briefly pauses whatever is playing and resumes it, which clears the built-up audio delay
without disconnecting your device. No root needed.

- **Reset Sync button** in the app
- **Quick Settings tile** so you can reset from the notification shade without leaving your video
- **Adjustable pause length** (300 to 5000 ms)
- **Optional auto-reset** a few seconds after your Bluetooth device connects

## Download

### [⬇️ Download the latest APK](https://github.com/gowthamdharma22/BtSyncReset/releases/latest/download/BtSyncReset.apk)

No GitHub login needed. Works from your phone's browser, or from the **Releases** section in the
GitHub app. Every change pushed to `main` builds and publishes a new release automatically.

## Install

1. Open `BtSyncReset.apk` on your phone.
2. Allow "Install unknown apps" when asked.
3. For the auto-reset feature on OnePlus/OxygenOS, set the app's battery usage to **Unrestricted**
   and allow **Auto-launch**.

## Usage tips

- Start with a 1000 ms pause. If the audio is still behind, increase it step by step.
- Around 3000 to 4000 ms usually gives the strongest reset.
