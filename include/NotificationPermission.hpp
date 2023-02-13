    #pragma once
    #include "Permission.hpp"
    #import <UserNotifications/UserNotifications.h>

    @interface NotificationPermission : Permission

    - (bool)isGranted;

    - (void)blockingRequest;

    @property (strong) NSString* userDialogTitle;

    @property (strong) NSString* userDialogBody;

    @end
