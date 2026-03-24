# Fusion Core

Unity modding framework for Android IL2CPP games.

## Compatibility

Current minimum supported Android version is 7.0 (API level 24).
No guarantees it will work on that version though, 
as it has only been tested on Android 11 (API level 30) and above.

As of writing this, PC versions of BepInEx and MelonLoader still use .NET 6.0 and legacy MonoMod.
However, FusionCore uses .NET 10.0 and MonoMod reorganized, so if you directly use MonoMod in your mods,
you will need to make it compatible with the newer MonoMod version.

## Configuration

1. Replace `unity-classes.jar` in `unityLibrary/libs` with the `classes.jar` from the exact Unity version used by the target game.
   - Unity path example: `{Unity Install Path}\Editor\Data\PlaybackEngines\AndroidPlayer\Variations\il2cpp\Release\Classes\classes.jar`

2. Set the target package in `unityLibrary/src/main/java/com/unity3d/player/UnityPlayerActivity.java`:
   - `TARGET_GAME`

3. Add/update `<queries><package ... /></queries>` in `unityLibrary/src/main/AndroidManifest.xml` for your target package.
   - Android package visibility rules require this for package context lookup.

4. Confirm ABI and native dependencies:
   - Current Gradle config is ARM64 only (`arm64-v8a`) in both `launcher/build.gradle.kts` and `unityLibrary/build.gradle.kts`.
   - `unityLibrary/src/main/jni/fusion/CMakeLists.txt` links against `libdobby.so` and `libcoreclr.so` from `unityLibrary/src/main/jniLibs/${CMAKE_ANDROID_ARCH_ABI}`.

5. Fix Unity-version-specific compile/runtime differences as needed.

## Build

Use the Gradle wrapper from the repository root:

```powershell
.\gradlew.bat :launcher:assembleDebug
```

Optional clean build:

```powershell
.\gradlew.bat clean :launcher:assembleDebug
```
