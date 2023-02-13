#include "NotificationPermission.hpp"

@implementation NotificationPermission

- (instancetype)init
{
    self = [super init];
    if (self) {
        _userDialogTitle = @"This app needs notification access permission to work";
        _userDialogBody = @"You need to allow notification access permission. If you don't see the alert, you need to allow in: \nSettings->APP_NAME->Notification->Always";
    }
    
    return self;
}

- (BOOL)isGranted {
    __block BOOL permissionGranted = NO;
    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);
    UNUserNotificationCenter* center = [UNUserNotificationCenter currentNotificationCenter];
    [center getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings* _Nonnull settings) {
        if (settings.authorizationStatus == UNAuthorizationStatusAuthorized) {
            permissionGranted = YES;
        }
        dispatch_semaphore_signal(semaphore);
    }];
    dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER);
    return permissionGranted;
}


- (void) blockingRequest {
    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);
    UNUserNotificationCenter* center = [UNUserNotificationCenter currentNotificationCenter];
    [center requestAuthorizationWithOptions:(UNAuthorizationOptionBadge | UNAuthorizationOptionSound | UNAuthorizationOptionAlert) completionHandler:^(BOOL requestGranted, NSError* _Nullable error) {
        if (!requestGranted) {
            Permission* permission = [super init:_userDialogTitle permissionBody:_userDialogBody];
            [permission displayBlockingAlertDialog];
        }
        dispatch_semaphore_signal(semaphore);
    }];
    dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER);
}

        


@end
