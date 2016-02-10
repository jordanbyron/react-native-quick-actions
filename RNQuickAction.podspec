require 'json'
version = JSON.parse(File.read('package.json'))["version"]

Pod::Spec.new do |s|

  s.name         = "RNQuickAction"
  s.version      = version
  s.homepage     = "https://github.com/jordanbyron/react-native-quick-actions"
  s.platform     = :ios, "9.0"
  s.source       = { :git => "https://github.com/jordanbyron/react-native-quick-actions.git", :tag => "#{s.version}" }
  s.source_files = 'RNQuickAction/RNQuickAction/*.{h,m}'
  s.preserve_paths = "**/*.js"
  s.dependency 'React'

end
