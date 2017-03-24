package com.drivetribe.AppShortcuts;

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

    WritableMap toWritableMap() {
        WritableMap map = Arguments.createMap();
        map.putString("type", type);
        map.putString("title", title);
        map.putString("icon", icon);
        map.putMap("userInfo", userInfo.toWritableMap());
        return map;
    }
}
