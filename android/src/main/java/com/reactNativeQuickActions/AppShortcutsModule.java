package com.reactNativeQuickActions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.ArrayList;
import java.util.List;

@ReactModule(name = AppShortcutsModule.REACT_NAME)
class AppShortcutsModule extends ReactContextBaseJavaModule {

    static final String REACT_NAME = "ReactAppShortcuts";

    private static final String ACTION_SHORTCUT = "ACTION_SHORTCUT";
    private static final String SHORTCUT_TYPE = "SHORTCUT_TYPE";

    private List<ShortcutItem> mShortcutItems;

    AppShortcutsModule(ReactApplicationContext reactContext) {
        super(reactContext);

        reactContext.addActivityEventListener(new ActivityEventListener() {
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                // Do nothing
            }

            @Override
            public void onNewIntent(Intent intent) {
                sendJSEvent(intent);
            }
        });
    }

    @Override
    public String getName() {
        return REACT_NAME;
    }

    @ReactMethod
    @TargetApi(25)
    public void popInitialAction(Promise promise) {
        try {
            Activity currentActivity = getCurrentActivity();
            WritableMap map = null;

            if (currentActivity != null) {
                Intent intent = currentActivity.getIntent();

                if (ACTION_SHORTCUT.equals(intent.getAction())) {
                    String type = intent.getStringExtra(SHORTCUT_TYPE);
                    if (type != null) {
                        map = Arguments.createMap();
                        map.putString("type", type);
                    }
                }
            }

            promise.resolve(map);
        } catch (Exception e) {
            promise.reject(new JSApplicationIllegalArgumentException(
                    "AppShortcuts.popInitialAction error. " + e.getMessage()));
        }
    }

    @ReactMethod
    @TargetApi(25)
    public void setShortcutItems(ReadableArray items) {
        if (!isShortcutSupported() || items.size() == 0) {
            return;
        }

        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }

        Context context = getReactApplicationContext();
        mShortcutItems = new ArrayList<>(items.size());
        List<ShortcutInfo> shortcuts = new ArrayList<>(items.size());

        for (int i = 0; i < items.size(); i++) {
            ShortcutItem item = ShortcutItem.fromReadableMap(items.getMap(i));
            mShortcutItems.add(item);

            int iconResId = context.getResources()
                    .getIdentifier(item.icon, "drawable", context.getPackageName());
            Intent intent = new Intent(context, currentActivity.getClass());
            intent.setAction(ACTION_SHORTCUT);
            intent.putExtra(SHORTCUT_TYPE, item.type);

            shortcuts.add(new ShortcutInfo.Builder(context, "id" + i)
                    .setShortLabel(item.title)
                    .setLongLabel(item.title)
                    .setIcon(Icon.createWithResource(context, iconResId))
                    .setIntent(intent)
                    .build());
        }

        getReactApplicationContext().getSystemService(ShortcutManager.class).setDynamicShortcuts(shortcuts);
    }

    @ReactMethod
    @TargetApi(25)
    public void clearShortcutItems() {
        if (!isShortcutSupported()) {
            return;
        }

        getReactApplicationContext().getSystemService(ShortcutManager.class).removeAllDynamicShortcuts();
        mShortcutItems = null;
    }

    @ReactMethod
    public void isSupported(Callback callback) {
        if (callback != null) {
            callback.invoke(null, isShortcutSupported());
        }
    }

    private boolean isShortcutSupported() {
        return Build.VERSION.SDK_INT >= 25;
    }

    private void sendJSEvent(Intent intent) {
        if (!ACTION_SHORTCUT.equals(intent.getAction()) || !isShortcutSupported()) {
            return;
        }

        String type = intent.getStringExtra(SHORTCUT_TYPE);
        ShortcutItem item = getShortcutItem(type);
        if (item != null) {
            getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("quickActionShortcut", item.toWritableMap());
        }
    }

    private ShortcutItem getShortcutItem(String type) {
        if (mShortcutItems != null && type != null) {
            for (ShortcutItem item : mShortcutItems) {
                if (item.type.equals(type)) {
                    return item;
                }
            }
        }
        return null;
    }

}
