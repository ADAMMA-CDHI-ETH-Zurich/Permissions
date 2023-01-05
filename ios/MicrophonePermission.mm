#include "MicrophonePermission.hpp"

@implementation MicrophonePermission

- (instancetype)init
{
    self = [super init];
    if (self) {
        _userDialogTitle = @"This app needs microphone access permission to work";
        _userDialogBody = @"You need to allow microphone access permission. If you don't see the alert, you need to allow in: \nSettings->APP_NAME->Microphone->Always";
        [self blockingRequest];
    }
    return self;
}


- (bool) isGranted {
    AVAudioSessionRecordPermission permission = [[AVAudioSession sharedInstance] recordPermission];

    if (permission == AVAudioSessionRecordPermissionGranted)
    {
        return true;
    }
    
    return false;
}

- (void) blockingRequest {
        
    if (![self isGranted])
    {
        AVAudioSession *session = [AVAudioSession sharedInstance];
        [session requestRecordPermission:^(BOOL granted) {
            if (!granted)
            {
                Permission *permission = [super init:_userDialogTitle permissionBody:_userDialogBody];
                [permission displayBlockingAlertDialog];
            }
        }];
    }
}


@end
