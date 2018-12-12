package com.reactNativeQuickActions;

import android.os.PersistableBundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

class ShortcutItem {
    String type;
    String title;
    String icon;
    UserInfo userInfo;

    static ShortcutItem fromReadableMap(ReadableMap map) {
        final ShortcutItem item = new ShortcutItem();
        item.type = map.getString("type");
        item.title = map.getString("title");
        item.icon = map.getString("icon");
        item.userInfo = UserInfo.fromReadableMap(map.getMap("userInfo"));
        return item;
    }

    static ShortcutItem fromPersistableBundle(PersistableBundle bundle) {
        final ShortcutItem item = new ShortcutItem();
        item.type = bundle.getString("type");
        item.title = bundle.getString("title");
        item.icon = bundle.getString("icon");
        item.userInfo = UserInfo.fromPersistableBundle(bundle.getPersistableBundle("userInfo"));
        return item;
    }

    PersistableBundle toPersistableBundle() {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("type", type);
        bundle.putString("title", title);
        bundle.putString("icon", icon);
        bundle.putPersistableBundle("userInfo", userInfo.toPersistableBundle());
        return bundle;
    }

    WritableMap toWritableMap() {
        WritableMap map = Arguments.createMap();
        map.putString("type", type);
        map.putString("title", title);
        map.putString("icon", icon);
        map.putMap("userInfo", userInfo.toWritableMap());
        return map;
    }
}
