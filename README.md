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

1. Go to the [Actions tab](../../actions) and open the latest successful **Build APK** run.
2. Scroll down to **Artifacts** and download **BtSyncReset-apk**.
3. Unzip it to get `app-debug.apk`.

If the artifact has expired, open **Actions > Build APK > Run workflow** to build a fresh one.

## Install

1. Copy `app-debug.apk` to your phone and open it.
2. Allow "Install unknown apps" when asked.
3. For the auto-reset feature on OnePlus/OxygenOS, set the app's battery usage to **Unrestricted**
   and allow **Auto-launch**.

## Usage tips

- Start with a 1000 ms pause. If the audio is still behind, increase it step by step.
- Around 3000 to 4000 ms usually gives the strongest reset.
