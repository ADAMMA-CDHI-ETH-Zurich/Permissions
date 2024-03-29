#import "LocationPermission.hpp"

// Class should be rewritten with the same structure of MicrophonePermission, using methods of superclass Permission
@implementation LocationPermission


- (instancetype)init
{
    self = [super init];
    if (self) {
        _locationManager = [CLLocationManager new];
        _locationManager.delegate = self;
        _locationManager.allowsBackgroundLocationUpdates = YES;
        _userDialogTitle = @"This app needs location permissions to work";
        _userDialogBody = @"You need to allow location permissions. If you don't see the alert, you need to allow them in: \nSettings->APP_NAME->Location->Always";
        _userDialog = [[UIAlertView alloc] initWithTitle:_userDialogTitle
                                           message:_userDialogBody
                                          delegate:self
                                 cancelButtonTitle:@"OK"
                                 otherButtonTitles:nil,nil];
        [self blockingRequest];
    }
    return self;
}


- (bool) isGranted {
    CLAuthorizationStatus authStatus = [CLLocationManager authorizationStatus];
    if (authStatus == kCLAuthorizationStatusAuthorizedAlways)
    {
        return true;
    }
    return false;
}


- (void) blockingRequest {
    if (@available(iOS 14.0, *)) {
        [self locationManagerDidChangeAuthorization:_locationManager];
    }
    else {
        [self locationManager:_locationManager didChangeAuthorizationStatus:kCLAuthorizationStatusAuthorizedAlways];
    }
}

// iOS 14+ - From iOS 13.4 we need to perform incremental request, first we ask "whenInUse" and if it's granted we can ask for "always"
// We need to separate for iOS 13.4, because it still needs incremental but can't use manager.authorizationStatus.
- (void)locationManagerDidChangeAuthorization:(CLLocationManager *)manager {
    if (@available(iOS 14.0, *)) {
        switch (manager.authorizationStatus) {
            case kCLAuthorizationStatusNotDetermined:
                [manager requestWhenInUseAuthorization];
                [manager requestAlwaysAuthorization];
                break;
            case kCLAuthorizationStatusAuthorizedAlways:
                break;
            default:
                [manager requestAlwaysAuthorization];
                [_userDialog show];
        }
    }
}


// iOS 14-
- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status
{
    switch (status)
    {
        case kCLAuthorizationStatusNotDetermined:
            [manager requestAlwaysAuthorization];
            break;
        case kCLAuthorizationStatusAuthorizedAlways:
            break;
        default:
            // Check for status on earlier version then iOS 14
            CLAuthorizationStatus authStatus = [CLLocationManager authorizationStatus];
            if (authStatus != kCLAuthorizationStatusAuthorizedAlways)
            {
                [manager requestAlwaysAuthorization];
                [_userDialog show];
            }
    }
}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 0) {
        // We reload to check if we have permissions
        if (![self isGranted])
        {
            [self init];
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
        }
    }
}

@end
