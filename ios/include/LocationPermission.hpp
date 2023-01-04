#pragma once
#include "Permission.hpp"
#import <CoreLocation/CoreLocation.h>

@interface LocationPermission : Permission <CLLocationManagerDelegate>

- (bool)isGranted;

- (void)blockingRequest;

@property (strong) CLLocationManager *locationManager;

@property (strong) NSString *userDialogTitle;

@property (strong) NSString *userDialogBody;

@property (strong) UIAlertView *userDialog;


@end
