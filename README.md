# GOVINDA Android

A lightweight native Android app for pasting, editing, copying, and sharing text or links. UI colors use a devotional saffron, gold, and dark-maroon palette.

## Build in GitHub Actions

1. Upload the **contents** of this folder to the root of your GitHub repository (`GovindaAndroid` or `GovindaAndroid9`).
2. Ensure `.github/workflows/android.yml` is at the repository root.
3. Open **Actions** and run **Android CI** (or push to `main`/`master`).
4. Download the `govinda-debug-apk` artifact from a successful run.

## Build locally

Requirements: JDK 17, Android SDK platform 35 and Gradle 8.9.

```bash
gradle --no-daemon :app:assembleDebug --stacktrace
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## Build fixes included

- Android Gradle Plugin is explicitly versioned (`8.7.3`) in the root `build.gradle`.
- Plugin repositories are declared in `settings.gradle` (`google()`, `mavenCentral()`, `gradlePluginPortal()`).
- `:app` is included and the app module has its own Android plugin/configuration.
- GitHub Actions workflow is correctly placed under `.github/workflows/` and uses Gradle 8.9 with JDK 17.
- Uses a dependency-light Java implementation, avoiding Kotlin plugin/version resolution and third-party UI libraries.

Clipboard behavior is intentionally limited to when the app is open, in line with Android privacy restrictions. A build cannot be verified in this environment because Android SDK/Gradle dependencies are not available here; GitHub Actions provides the build check.
