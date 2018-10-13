react-native home screen quick actions
======================================

Support for the new 3D Touch home screen quick actions for your React Native apps!

Please note that on Android if `android:launchMode` is set to default value `standard` in AndroidManifest.xml, the app will be re-created each time when app is being brought back from background and it won't receive `quickActionShortcut` event from DeviceEventEmitter, instead `popInitialAction` will be receiving the app shortcut event.

__This project currently supports iOS 9+ and Android 7__

![](http://i.imgur.com/holmBPD.png)

## Supported React Native Versions

| React Native version(s) | Supporting react-native-quick-actions version(s) |
|-------------------------|--------------------------------------------------|
| < v0.40                   | v0.1.5                                         |
| v0.40+                    | v0.2.0+                                        |

## Installing

First cd into your project's directory and grab the latest version of this code:

```bash
$ yarn add --dev react-native-quick-actions
```

Then link quick actions to your project:

```bash
$ react-native link react-native-quick-actions
```

## Usage

Now let's see how you can use this library to trigger shortcut actions in your react native app.

### Adding static quick actions - iOS only

This part is pretty easy. There are a [bunch of
tutorials](https://littlebitesofcocoa.com/79) and
[docs](https://developer.apple.com/library/prerelease/ios/samplecode/ApplicationShortcuts/Introduction/Intro.html#//apple_ref/doc/uid/TP40016545) out there that
go over all your options, but here is the quick and dirty version:

Add these entries into to your `Info.plist` file and replace the generic stuff
(Action Title, .action, etc):

```xml
<key>UIApplicationShortcutItems</key>
<array>
  <dict>
    <key>UIApplicationShortcutItemIconType</key>
    <string>UIApplicationShortcutIconTypeLocation</string>
    <key>UIApplicationShortcutItemTitle</key>
    <string>Action Title</string>
    <key>UIApplicationShortcutItemType</key>
    <string>$(PRODUCT_BUNDLE_IDENTIFIER).action</string>
  </dict>
</array>
```

A full list of available icons can be found here:

<https://developer.apple.com/library/prerelease/ios/documentation/UIKit/Reference/UIApplicationShortcutIcon_Class/index.html#//apple_ref/c/tdef/UIApplicationShortcutIconType>

### Adding dynamic quick actions

In order to add / remove dynamic actions during application lifecycle, you need to require `react-native-quick-actions` and call either `setShortcutItems` or `clearShortcutItems` (useful when user is logging out).

```js
var QuickActions = require('react-native-quick-actions');

// Add few actions
QuickActions.setShortcutItems([
  {
    type: "Orders", // Required
    title: "See your orders", // Optional, if empty, `type` will be used instead
    subtitle: "See orders you've made",
    icon: "Compose", // Pass any of UIApplicationShortcutIconType<name>
    userInfo: {
      url: "app://orders" // provide custom data, like in-app url you want to open
    }
  }
]);

// Clear them all
QuickActions.clearShortcutItems();
```

In order to specify icon for your shortcut item, either include `UIApplicationShortcutIconType<name>`, e.g. for `UIApplicationShortcutIconTypeCompose` go with `Compose` or define your asset name if you want to use image from a template (e.g. `my-custom-icon` if that's the name of image in `Images.xcassets`. Remember not to name your custom icons the same as any system icons otherwise system ones will be loaded instead).

Full list of available icons has been already listed in the previous section.

For Android icons, names must contain only lowercase a-z, 0-9, or underscore. So `guide_icon.png` would be acceptable but `guide-icon.png` would not be. Place your icons in res/drawable folder and set them when you create your shortcuts in setShortcutItems in the following way 
```js
{
  ...
  icon: "guide_icon",
  ...
}
```
### Listening for quick actions in your javascript code

First, you'll need to make sure `DeviceEventEmitter` is added to the list of
requires for React.

```js
var React = require('react-native');
var {
  //....things you need plus....
  DeviceEventEmitter
} = React;

```

Use `DeviceEventEmitter` to listen for `quickActionShortcut` messages.

```js
var subscription = DeviceEventEmitter.addListener(
  'quickActionShortcut', function(data) {
    console.log(data.title);
    console.log(data.type);
    console.log(data.userInfo);
  });
```

To get any actions sent when the app is cold-launched using the following code:

```js
var QuickActions = require('react-native-quick-actions');

QuickActions.popInitialAction().then(doSomethingWithTheAction).catch(console.error)
```

### Check if 3D Touch is supported

The following function will alert you if the user's device supports 3D Touch. Please
note this project currently only supports iOS 9+ which means this code will not
work on iOS devices running versions < 9.0.

```js
var QuickActions = require('react-native-quick-actions');

QuickActions.isSupported(function(error, supported) {
  if (!supported) {
    console.log('Device does not support 3D Touch or 3D Touch is disabled.');
  }
});

```

## License

Copyright (c) 2015 Jordan Byron (http://github.com/jordanbyron/)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
