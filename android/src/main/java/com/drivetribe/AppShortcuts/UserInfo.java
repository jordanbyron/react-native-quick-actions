package com.drivetribe.AppShortcuts;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

class UserInfo {

    private String url;

    static UserInfo fromReadableMap(ReadableMap map) {
        final UserInfo info = new UserInfo();
        info.url = map.getString("url");
        return info;
    }

    WritableMap toWritableMap() {
        WritableMap map = Arguments.createMap();
        map.putString("url", url);
        return map;
    }
}
