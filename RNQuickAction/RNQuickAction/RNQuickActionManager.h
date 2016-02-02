//
//  RNQuickAction.h
//  RNQuickAction
//
//  Created by Jordan Byron on 9/26/15.
//  Copyright © 2015 react-native. All rights reserved.
//

#import "RCTBridgeModule.h"

@interface RCTApplicationShortcutIcon : NSObject

// Create an icon using a system-defined image.
+ (instancetype)iconWithType:(NSNumber *)type;

// Create an icon from a custom image.
// The provided image named will be loaded from the app's bundle
// and will be masked to conform to the system-defined icon style.
+ (instancetype)iconWithTemplateImageName:(NSString *)templateImageName;

@end

@interface RCTApplicationShortcutItem : NSObject

- (instancetype)init NS_UNAVAILABLE;
- (instancetype)initWithType:(NSString *)type localizedTitle:(NSString *)localizedTitle localizedSubtitle:(nullable NSString *)localizedSubtitle icon:(nullable RCTApplicationShortcutIcon *)icon userInfo:(nullable NSDictionary *)userInfo NS_DESIGNATED_INITIALIZER;
- (instancetype)initWithType:(NSString *)type localizedTitle:(NSString *)localizedTitle;

// An application-specific string that identifies the type of action to perform.
@property (nonatomic, copy, readonly) NSString *type;

// Properties controlling how the item should be displayed on the home screen.
@property (nonatomic, copy, readonly) NSString *localizedTitle;
@property (nullable, nonatomic, copy, readonly) NSString *localizedSubtitle;
@property (nullable, nonatomic, copy, readonly) RCTApplicationShortcutIcon *icon;

// Application-specific information needed to perform the action.
// Will throw an exception if the NSDictionary is not plist-encodable.
@property (nullable, nonatomic, copy, readonly) NSDictionary *userInfo;

@end

@interface RCTMutableApplicationShortcutItem : RCTApplicationShortcutItem

// An application-specific string that identifies the type of action to perform.
@property (nonnull, nonatomic, copy) NSString *type;

// Properties controlling how the item should be displayed on the home screen.
@property (nonnull, nonatomic, copy) NSString *localizedTitle;
@property (nullable, nonatomic, copy) NSString *localizedSubtitle;
@property (nullable, nonatomic, copy) RCTApplicationShortcutIcon *icon;

// Application-specific information needed to perform the action.
// Will throw an exception if the NSDictionary is not plist-encodable.
@property (nullable, nonatomic, copy) NSDictionary *userInfo;

@end

//RCTShortcutItemClicked

@interface RNQuickActionManager : NSObject <RCTBridgeModule>
+(void) onQuickActionPress:(NSObject *) shortcutItem completionHandler:(void (^)(BOOL succeeded)) completionHandler;
@end
