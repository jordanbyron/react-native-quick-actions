var ReactAppShortcuts = require('react-native').NativeModules.ReactAppShortcuts;

module.exports = {
  /**
   * An initial action will be available if the app was cold-launched
   * from an action.
   *
   * The first caller of `popInitialAction` will get the initial
   * action object, or `null`. Subsequent invocations will return null.
   */
  popInitialAction: function() {
    return ReactAppShortcuts.popInitialAction();
  },

  /**
   * Adds shortcut items to application
   */
  setShortcutItems: function(items) {
    ReactAppShortcuts.setShortcutItems(items);
  },

  /**
   * Clears all previously set dynamic icons
   */
  clearShortcutItems: function() {
    ReactAppShortcuts.clearShortcutItems();
  },

  /**
   * Check if quick actions are supported
   */
   isSupported: function(callback) {
     ReactAppShortcuts.isSupported(callback);
   }
};
