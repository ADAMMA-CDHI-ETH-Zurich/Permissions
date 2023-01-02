#pragma once
#include "Permission.hpp"
#import <AVFoundation/AVFoundation.h>
#import <AVFoundation/AVAudioSession.h>
#import "ViewController.hpp"
@interface MicrophonePermission : Permission

- (bool)isGranted;

- (void)blockingRequest;

@property (nonatomic, strong) NSString *permissionTitle;

@property (nonatomic, strong) NSString *permissionBody;

@end
