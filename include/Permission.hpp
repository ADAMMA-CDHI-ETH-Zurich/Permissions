#pragma once
#include "CLAID.hpp"

@interface Permission : NSObject <UIAlertViewDelegate>

@property (strong) UIAlertView *alert;

- (void)blockingRequest;

- (bool)isGranted;

- (void)displayBlockingAlertDialog:(NSString*) permissionTitle permissionBody:(NSString*)permissionBody;

@end
