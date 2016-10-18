module.exports = {
  /**
   * An initial action will be available if the app was cold-launched
   * from an action.
   *
   * The first caller of `popInitialAction` will get the initial
   * action object, or `null`. Subsequent invocations will return null.
   */
  popInitialAction: function () {
    console.warn('react-native-quick-actions popInitialAction not supported on iOS');
    return null;
  },

  /**
   * Adds shortcut items to application
   */
  setShortcutItems: function(icons) {
    console.warn('react-native-quick-actions setShortcutItems not supported on iOS');
  },

  /**
   * Clears all previously set dynamic icons
   */
  clearShortcutItems: function() {
    console.warn('react-native-quick-actions clearShortcutItems not supported on iOS');
  },

  /**
   * Check if quick actions are supported
   */
   isSupported: function(callback) {
     callback(null, false);
   }
};
