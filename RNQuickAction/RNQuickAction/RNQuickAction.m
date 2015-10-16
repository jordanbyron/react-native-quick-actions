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

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE();

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

- (void)setBridge:(RCTBridge *)bridge
{
    _bridge = bridge;
    _initialGesture = [bridge.launchOptions[UIApplicationLaunchOptionsShortcutItemKey] copy];
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

+ (void) onQuickActionPress:(UIApplicationShortcutItem *) shortcutItem completionHandler:(void (^)(BOOL succeeded)) completionHandler
{
    NSLog(@"Quick action shortcut item pressed: %@", shortcutItem);
    
    NSDictionary *baseNotificationData = @{@"type": shortcutItem.type,
                                           @"title": shortcutItem.localizedTitle,
                                           @"userInfo": shortcutItem.userInfo?: @""
                                           };
    
    NSMutableDictionary *notificationData = [NSMutableDictionary dictionaryWithDictionary:baseNotificationData];
    
    [[NSNotificationCenter defaultCenter] postNotificationName:RCTShortcutItemClicked
                                                        object:self
                                                      userInfo:notificationData];
    
    completionHandler(YES);
}

- (void)handleQuickActionPress:(UIApplicationShortcutItem *) shortcutItem
{
    [_bridge.eventDispatcher sendDeviceEventWithName:@"quickActionShortcut"
                                                body:[shortcutItem userInfo]];
}

- (NSDictionary *)constantsToExport
{
    return @{
        @"initialGesture": RCTNullIfNil(_initialGesture),
    };
}

@end
