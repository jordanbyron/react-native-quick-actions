package com.reactNativeQuickActions;

import android.os.PersistableBundle;

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

    static UserInfo fromPersistableBundle(PersistableBundle bundle) {
        final UserInfo info = new UserInfo();
        info.url = bundle.getString("url");
        return info;
    }

    PersistableBundle toPersistableBundle() {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("url", url);
        return bundle;
    }

    WritableMap toWritableMap() {
        WritableMap map = Arguments.createMap();
        map.putString("url", url);
        return map;
    }
}
