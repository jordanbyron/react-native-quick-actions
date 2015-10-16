var RNQuickActionManager = require('NativeModules').RNQuickAction;
var _initialGesture = RNQuickActionManager.initialGesture;

class RNQuickActionIOS {
  
  /**
   * An initial gesture will be available if the app was cold-launched
   * from a gesture.
   *
   * The first caller of `popInitialGesture` will get the initial
   * gesture object, or `null`. Subsequent invocations will return null.
   */
  static popInitialGesture() {
    var initialGesture = _initialGesture;
    _initialGesture = null;
    return initialGesture;
  }
  
}
