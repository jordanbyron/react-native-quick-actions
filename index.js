var Platform = require('react-native').NativeModules;
var entryFile = Platform.OS === 'ios' ? require('./index.ios.js') : require('./index.android.js');
module.exports = entryFile;
