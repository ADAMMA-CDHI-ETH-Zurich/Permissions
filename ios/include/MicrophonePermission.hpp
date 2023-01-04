#pragma once
#import <AVFoundation/AVFoundation.h>
#import <AVFoundation/AVAudioSession.h>
#include "Permission.hpp"

@interface MicrophonePermission : Permission

- (bool)isGranted;

- (void)blockingRequest;

@property (strong) NSString *userDialogTitle;

@property (strong) NSString *userDialogBody;

@end
