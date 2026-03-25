package dev.allofus.fusioncore;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

import bitter.jnibridge.JNIBridge;
import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;

public class NativeLibraryManager {
    private static final String TAG = "NativeLibraryManager";

    private static final HashMap<String, String> LibraryMap
            = new HashMap<>();

    public static void mapLibrary(String name, String path) {
        LibraryMap.put(name, path);
    }

    public static void setupLibraryHooks(FusionConfig config) {
        Method findLibraryMethod = findLibraryMethodViaReflection();

        if (findLibraryMethod == null) {
            Log.wtf(TAG, "unable to hook findLibrary method");
            return;
        }

        Pine.hook(findLibraryMethod, new MethodHook() {
            @Override
            public void beforeCall(Pine.CallFrame callFrame) {
                var libName = callFrame.args[0].toString();

                Log.i(TAG, "findLibrary called for " + libName);

                String override = LibraryMap.get(libName);
                if (override != null) {
                    Log.i(TAG, "override to " + override);
                    callFrame.setResult(override);
                } else {
                    callFrame.setResult(config.gameLibraryDirectory + "/lib" + callFrame.args[0] + ".so");
                }
            }

            @Override
            public void afterCall(Pine.CallFrame callFrame) {
                if (callFrame.hasThrowable()) {
                    Log.wtf(TAG, "findLibrary threw an exception for " + callFrame.args[0], callFrame.getThrowable());
                }
            }
        });
    }

    private static Method findLibraryMethodViaReflection() {
        Method findLibraryMethod = null;
        Class<?> clazz = Objects.requireNonNull(JNIBridge.class.getClassLoader()).getClass();

        while (findLibraryMethod == null && clazz != null) {
            try {
                try {
                    Class.forName(clazz.getName(), true, JNIBridge.class.getClassLoader());
                } catch (ClassNotFoundException e) {
                    Log.wtf(TAG, "Class not found: " + clazz.getName(), e);
                }

                findLibraryMethod = clazz.getDeclaredMethod("findLibrary", String.class);
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            }
        }

        return findLibraryMethod;
    }
}
