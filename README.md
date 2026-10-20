# BT Sync Reset

Tier 1 "soft reset" for Bluetooth audio/video drift: pause, wait, play. This flushes the
Bluetooth audio buffer without disconnecting. No root, no special permissions.

## Build option A: Android Studio
1. Open this folder in Android Studio (File > Open) and let Gradle sync finish.
2. Build > Build App Bundle(s) / APK(s) > Build APK(s).
3. The APK appears in app/build/outputs/apk/debug/app-debug.apk.

## Build option B: GitHub (no Android Studio needed)
1. Create a new GitHub repo and upload all files in this folder, including the hidden .github folder.
2. Open the repo's Actions tab. "Build APK" runs automatically, or click "Run workflow".
3. When it finishes, download the BtSyncReset-apk artifact, unzip it, and copy the APK to your phone.

## Install on OnePlus
Open the APK on the phone and allow "Install unknown apps" for your file manager when asked.

For auto-reset-on-connect, also go to Settings > Apps > BT Sync Reset > Battery, set it to
Unrestricted, and allow Auto-launch if OxygenOS shows that option.

## Tuning
- Start with 1000 ms. If the sync isn't fixed, increase the pause.
- Around 3000-4000 ms usually lets Android put the audio output fully on standby (strongest reset).
- If even 5000 ms doesn't fix it on your headset, Tier 1 isn't enough, and you'd need the
  Shizuku-based reconnect (Tier 3).
