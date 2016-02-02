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
#import "RNQuickActionManager.h"
#import "RCTUtils.h"

NSString *const RCTShortcutItemClicked = @"ShortcutItemClicked";

NSDictionary *RNQuickAction(RCTApplicationShortcutItem *item) {
    if (!item) return nil;
    return @{
        @"type": item.type,
        @"title": item.localizedTitle,
        @"userInfo": item.userInfo ?: @{}
    };
}



@implementation RCTApplicationShortcutItem

-(instancetype)initWithType:(NSString *)type localizedTitle:(NSString *)localizedTitle localizedSubtitle:(nullable NSString *)localizedSubtitle icon:(nullable RCTApplicationShortcutIcon *)icon userInfo:(nullable NSDictionary *)userInfo;
{
    Class UIApplicationShortcutItemClass = NSClassFromString(@"UIApplicationShortcutItem");
    if (NSClassFromString(@"UIApplicationShortcutItem")) {
        return [[UIApplicationShortcutItemClass alloc] initWithType:type localizedTitle:localizedTitle localizedSubtitle:localizedSubtitle icon:icon userInfo:userInfo];
    }
    return self;
}

-(instancetype)initWithType:(NSString *)type localizedTitle:(NSString *)localizedTitle;
{
    Class UIApplicationShortcutItemClass = NSClassFromString(@"UIApplicationShortcutItem");
    if (NSClassFromString(@"UIApplicationShortcutItem")) {
        return [[UIApplicationShortcutItemClass alloc] initWithType:type localizedTitle:localizedTitle];
    }
    return self;
}

@end

@implementation RCTApplicationShortcutIcon

+(instancetype)iconWithType:(NSNumber *)type
{
    Class UIApplicationShortcutIconClass = NSClassFromString(@"UIApplicationShortcutIcon");
    if (NSClassFromString(@"UIApplicationShortcutIcon")) {
        SEL s = NSSelectorFromString(@"iconWithType");
        return objc_msgSend(UIApplicationShortcutIconClass, s, type);
        //return [[UIApplicationShortcutIconClass alloc] iconWithType:[type intValue]];
    }
    return nil;
}

+ (instancetype)iconWithTemplateImageName:(NSString *)templateImageName
{
    Class UIApplicationShortcutIconClass = NSClassFromString(@"UIApplicationShortcutIcon");
    if (NSClassFromString(@"UIApplicationShortcutIcon")) {
        SEL s = NSSelectorFromString(@"iconWithTemplateImageName");
        return objc_msgSend(UIApplicationShortcutIconClass, s, templateImageName);
//        return [[UIApplicationShortcutIconClass alloc] iconWithTemplateImageName:templateImageName];
    }
    return nil;
}

@end

@implementation RNQuickActionManager
{
    RCTApplicationShortcutItem *_initialAction;
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
    NSString *key = [RNQuickActionManager lookupStringConstant:@"UIApplicationLaunchOptionsShortcutItemKey"];
    if (key) {
       _initialAction = [bridge.launchOptions[key] copy];
    }

}

+(NSNumber *)lookupNumberConstant:(NSString *)constantName
{
    void ** dataPtr = CFBundleGetDataPointerForName(CFBundleGetMainBundle(), (__bridge CFStringRef)constantName);
    return (__bridge NSNumber *)(dataPtr ? *dataPtr : nil);
}
+(NSString *)lookupStringConstant:(NSString *)constantName
{
    void ** dataPtr = CFBundleGetDataPointerForName(CFBundleGetMainBundle(), (__bridge CFStringRef)constantName);
    return (__bridge NSString *)(dataPtr ? *dataPtr : nil);
}

// Map user passed array of UIApplicationShortcutItem
- (NSArray*)dynamicShortcutItemsForPassedArray:(NSArray*)passedArray {
    NSMutableArray *shortcutItems = [NSMutableArray new];
    
    [passedArray enumerateObjectsUsingBlock:^(NSDictionary *item, NSUInteger idx, BOOL *stop) {
        NSString *iconName = item[@"icon"];
        
        // If passed iconName is enum, use system icon
        // Otherwise, load from bundle
        RCTApplicationShortcutIcon *shortcutIcon;
        NSString *constantName = [@"NSApplicationShortcutIconType" stringByAppendingString:iconName];
        NSNumber *iconType = [RNQuickActionManager lookupNumberConstant:constantName];
        
        if (iconType) {
            shortcutIcon = [RCTApplicationShortcutIcon iconWithType:iconType];
        } else if (iconName) {
            shortcutIcon = [RCTApplicationShortcutIcon iconWithTemplateImageName:iconName];
        }
        
        [shortcutItems addObject:[[RCTApplicationShortcutItem alloc] initWithType:item[@"type"]
                                                                  localizedTitle:item[@"title"] ?: item[@"type"]
                                                               localizedSubtitle:item[@"subtitle"]
                                                                            icon:shortcutIcon
                                                                        userInfo:item[@"userInfo"]]];
    }];
    
    return shortcutItems;
}

RCT_EXPORT_METHOD(setShortcutItems:(NSArray *) shortcutItems)
{
    NSArray *dynamicShortcuts = [self dynamicShortcutItemsForPassedArray:shortcutItems];
    SEL s = NSSelectorFromString(@"setShortcutItems");
    objc_msgSend([UIApplication sharedApplication], s, dynamicShortcuts);
}

RCT_EXPORT_METHOD(clearShortcutItems)
{
    SEL s = NSSelectorFromString(@"setShortcutItems");
    objc_msgSend([UIApplication sharedApplication], s, nil);
}

+ (void)onQuickActionPress:(RCTApplicationShortcutItem *) shortcutItem completionHandler:(void (^)(BOOL succeeded)) completionHandler
{
    RCTLogInfo(@"[RNQuickAction] Quick action shortcut item pressed: %@", [shortcutItem type]);

    [[NSNotificationCenter defaultCenter] postNotificationName:RCTShortcutItemClicked
                                                        object:self
                                                      userInfo:RNQuickAction(shortcutItem)];

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
      @"initialAction": RCTNullIfNil(RNQuickAction(_initialAction))
    };
}

@end
