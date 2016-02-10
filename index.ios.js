var RNQuickActionManager = require('react-native').NativeModules.RNQuickActionManager;
var _initialAction = RNQuickActionManager.initialAction;

module.exports = {
  /**
   * An initial action will be available if the app was cold-launched
   * from an action.
   *
   * The first caller of `popInitialAction` will get the initial
   * action object, or `null`. Subsequent invocations will return null.
   */
  popInitialAction: function () {
    var initialAction = _initialAction;
    _initialAction = null;
    return initialAction;
  },
  
  /**
   * Adds shortcut items to application
   */
  setShortcutItems: function(icons) {
    RNQuickActionManager.setShortcutItems(icons);
  },
  
  /**
   * Clears all previously set dynamic icons
   */
  clearShortcutItems: function() {
    RNQuickActionManager.clearShortcutItems();
  }
};
