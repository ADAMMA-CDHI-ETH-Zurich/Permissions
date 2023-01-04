#pragma once
#include "CLAID.hpp"
#include "ViewController.hpp"
#include "AppDelegate.hpp"

@interface Permission : NSObject <UIAlertViewDelegate>

@property (strong) UIAlertView *alert;

@property (strong) NSString *permissionTitle;

@property (strong) NSString *permissionBody;

- (instancetype)init:(NSString*) permissionTitle permissionBody:(NSString*)permissionBody;

- (void)blockingRequest;

- (bool)isGranted;

- (void)displayBlockingAlertDialog;

@end
