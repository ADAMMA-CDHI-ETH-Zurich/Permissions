#include "MicrophonePermission.hpp"

@implementation MicrophonePermission

- (instancetype)init
{
    self = [super init];
    if (self) {
        _permissionTitle = @"This app needs microphone access permission to work";
        _permissionBody = @"You need to allow microphone access permission. If you don't see the alert, you need to allow in: \nSettings->APP_NAME->Microphone->Always";
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
                [super displayBlockingAlertDialog:_permissionTitle permissionBody:_permissionBody];
            }
        }];
    }
    
}


@end
