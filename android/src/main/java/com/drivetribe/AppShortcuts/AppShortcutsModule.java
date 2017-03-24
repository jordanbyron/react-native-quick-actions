package com.drivetribe.AppShortcuts;

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
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.ArrayList;
import java.util.List;

public class AppShortcutsModule extends ReactContextBaseJavaModule {

    private static final String SHORTCUT_TYPE = "SHORTCUT_TYPE";

    private List<ShortcutItem> mShortcutItems;

    public AppShortcutsModule(ReactApplicationContext reactContext) {
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
        return "ReactAppShortcuts";
    }

    @ReactMethod
    @TargetApi(25)
    public void popInitialAction(Promise promise) {
        try {
            Activity currentActivity = getCurrentActivity();
            WritableMap map = null;

            if (currentActivity != null) {
                Intent intent = currentActivity.getIntent();
                String type = intent.getStringExtra(SHORTCUT_TYPE);
                if (type != null) {
                    map = Arguments.createMap();
                    map.putString("type", type);
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
        if (items.size() == 0)
            return;

        Context context = getReactApplicationContext();
        mShortcutItems = new ArrayList<>(items.size());
        List<ShortcutInfo> shortcuts = new ArrayList<>(items.size());

        for (int i = 0; i < items.size(); i++) {
            ShortcutItem item = ShortcutItem.fromReadableMap(items.getMap(i));
            mShortcutItems.add(item);
            shortcuts.add(createShortcutInfo(context, item, "id" + i));
        }

        getShortcutManager().setDynamicShortcuts(shortcuts);
    }

    @ReactMethod
    @TargetApi(25)
    public void clearShortcutItems() {
        getShortcutManager().removeAllDynamicShortcuts();
        mShortcutItems = null;
    }

    @ReactMethod
    public void isSupported(com.facebook.react.bridge.Callback callback) {
        if (callback != null) {
            callback.invoke(null, Build.VERSION.SDK_INT >= 25);
        }
    }

    @TargetApi(25)
    private ShortcutManager getShortcutManager() {
        return getReactApplicationContext().getSystemService(ShortcutManager.class);
    }

    @TargetApi(25)
    private ShortcutInfo createShortcutInfo(Context context, ShortcutItem item, String id) {
        int iconResId = context.getResources()
                .getIdentifier(item.icon, "drawable", context.getPackageName());
        Intent intent = new Intent(context, getCurrentActivity().getClass());
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(SHORTCUT_TYPE, item.type);

        return new ShortcutInfo.Builder(context, id)
                .setShortLabel(item.title)
                .setLongLabel(item.title)
                .setIcon(Icon.createWithResource(context, iconResId))
                .setIntent(intent)
                .build();
    }

    private void sendJSEvent(Intent intent) {
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

    static class UserInfo {
        public String url;

        static public UserInfo fromReadableMap(ReadableMap map) {
            final UserInfo info = new UserInfo();
            info.url = map.getString("url");
            return info;
        }

        public WritableMap toWritableMap() {
            WritableMap map = Arguments.createMap();
            map.putString("url", url);
            return map;
        }
    }

    static class ShortcutItem {
        public String type;
        public String title;
        public String icon;
        public UserInfo userInfo;

        static public ShortcutItem fromReadableMap(ReadableMap map) {
            final ShortcutItem item = new ShortcutItem();
            item.type = map.getString("type");
            item.title = map.getString("title");
            item.icon = map.getString("icon");
            item.userInfo = UserInfo.fromReadableMap(map.getMap("userInfo"));
            return item;
        }

        public WritableMap toWritableMap() {
            WritableMap map = Arguments.createMap();
            map.putString("type", type);
            map.putString("title", title);
            map.putString("icon", icon);
            map.putMap("userInfo", userInfo.toWritableMap());
            return map;
        }
    }

}
