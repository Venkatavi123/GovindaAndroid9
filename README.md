# GOVINDA Android starter project

A native Android starter app inspired by the requested clipboard/link workflow, with a devotional saffron, cream, and maroon design.

## Included
- Manual paste from the Android clipboard
- Clipboard refresh/listener while the app is visible (Android may restrict clipboard reads; this is not background monitoring)
- Copy edited text to clipboard
- Share text or links using Android's share sheet
- Simple devotional GOVINDA-themed interface

## Build
1. Install Android Studio (recent stable version) and Android SDK Platform 35.
2. Open this folder in Android Studio.
3. Allow Gradle sync to complete.
4. Choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. Android Studio will show the generated APK location.

## Notes
- This is a starter implementation, not a verified clone of every behavior in the reference APK.
- Android restricts background clipboard access for privacy. Clipboard changes are handled while the app is in use, and manual paste is available.
- Package ID: `com.govinda.app`; app label: `GOVINDA`.
