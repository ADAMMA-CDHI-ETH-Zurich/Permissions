#include "Permission.hpp"

@implementation Permission


- (instancetype)init:(NSString*) permissionTitle permissionBody:(NSString*)permissionBody
{
    self = [super init];
    if (self) {
        _permissionTitle = permissionTitle;
        _permissionBody = permissionBody;
    }
    return self;
}

    
- (void)displayBlockingAlertDialog
{
    dispatch_async(dispatch_get_main_queue(), ^(void){
        _alert = [[UIAlertView alloc] initWithTitle:_permissionTitle
                                            message:_permissionBody
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
        [self displayBlockingAlertDialog];

    }
}


@end

