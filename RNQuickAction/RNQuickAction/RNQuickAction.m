//
//  RNQuickAction.m
//  RNQuickAction
//
//  Created by Jordan Byron on 9/26/15.
//  Copyright © 2015 react-native. All rights reserved.
//

#import "RCTBridge.h"
#import "RCTConvert.h"
#import "RCTEventDispatcher.h"
#import "RNQuickAction.h"
#import "RCTUtils.h"

NSString *const RCTShortcutItemClicked = @"ShortcutItemClicked";

@implementation RNQuickAction
{
    NSDictionary *_initialGesture;
}

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (instancetype)init
{
    if ((self = [super init])) {
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(handleQuickActionPress:)
                                                     name:RCTShortcutItemClicked
                                                   object:nil];
    }
    return self;
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)setBridge:(RCTBridge *)bridge
{
    _bridge = bridge;
    _initialGesture = [bridge.launchOptions[UIApplicationLaunchOptionsShortcutItemKey] copy];
}

+ (void) onQuickActionPress:(UIApplicationShortcutItem *) shortcutItem completionHandler:(void (^)(BOOL succeeded)) completionHandler
{
    RCTLogInfo(@"[RNQuickAction] Quick action shortcut item pressed: %@", [shortcutItem type]);
    
    NSDictionary *userInfo = @{
        @"type": shortcutItem.type,
        @"title": shortcutItem.localizedTitle,
        @"userInfo": shortcutItem.userInfo ?: @{}
    };
    
    [[NSNotificationCenter defaultCenter] postNotificationName:RCTShortcutItemClicked
                                                        object:self
                                                      userInfo:userInfo];
    
    completionHandler(YES);
}

- (void)handleQuickActionPress:(NSNotification *) notification
{
    [_bridge.eventDispatcher sendDeviceEventWithName:@"quickActionShortcut"
                                                body:notification.userInfo];
}

- (NSDictionary *)constantsToExport
{
    return @{
        @"initialGesture": RCTNullIfNil(_initialGesture),
    };
}

@end
