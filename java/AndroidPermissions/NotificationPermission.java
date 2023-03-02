package AndroidPermissions;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;


public class NotificationPermission extends Permission {
     private static final int NOTIFICATION_REQUEST_CODE = 300;
     private static final String userDialogTitle = "You need to allow notification permission";
     private static final String userDialogBody = "In the following screen you need to allow " +
             "notification permissions to use this app. If you can't see the option you need " +
             "to open\nSettings->Apps->CLAIDDemo->Permissions.";
     private static final String[] NOTIFICATION_PERMISSION = {
             Manifest.permission.POST_NOTIFICATIONS
     };


    @Override
    public boolean isGranted() {
        if (Build.VERSION.SDK_INT >= 33) {
            return ContextCompat.checkSelfPermission(super.getContext(),
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
     public void blockingRequest() {
        if (!isGranted() && Build.VERSION.SDK_INT >= 33) {
            startIntentWithExtras(NOTIFICATION_PERMISSION, NOTIFICATION_REQUEST_CODE, userDialogTitle, userDialogBody);
            while (!isGranted()) {}
        }
    }

 }
