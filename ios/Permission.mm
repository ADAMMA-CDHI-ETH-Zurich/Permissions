#include "Permission.hpp"

@implementation Permission


- (instancetype)init
{
    self = [super init];
    if (self) {
    }
    return self;
}

    
- (void)displayBlockingAlertDialog:(NSString*) permissionTitle permissionBody:(NSString*)permissionBody{
    dispatch_async(dispatch_get_main_queue(), ^(void){
        _alert = [[UIAlertView alloc] initWithTitle:permissionTitle
                                            message:permissionBody
                                            delegate:self
                                  cancelButtonTitle:@"OK"
                                  otherButtonTitles:nil,nil];
        [_alert show];
        
        // Prompt user to settings
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString]];
    });

}

- (bool)isGranted{};

- (void)blockingRequest{};

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 0) {
        // We reload to check if we have permissions
        [self displayBlockingAlertDialog:@"This app needs microphone access permission to work"
                          permissionBody:@"You need to allow microphone access permission. If you don't see the alert, you need to allow in: \nSettings->APP_NAME->Microphone->Always"];
    }
}


@end

