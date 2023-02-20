package AndroidPermissions;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


 public class NotificationPermission extends Permission {
     private final AppCompatActivity activity;
     private static final int NOTIFICATION_REQUEST_CODE = 300;
     private static final int delayTime = 7000;   // (ms) Period we wait before checking user's choice (permission granted or not)
     private static final String userDialogTitle = "You need to allow notification permission";
     private static final String userDialogBody = "In the following screen you will need to allow " +
             "notification permissions to use this app. If you can't see the option you will need " +
             "to open\nSettings->Apps->CLAIDDemo->Permissions.";
     private static final String[] PERMISSION_NOTIFICATION = {
             Manifest.permission.POST_NOTIFICATIONS
     };

     public NotificationPermission(AppCompatActivity activity) {
         super(activity);
         this.activity = super.getActivity();
     }

    @Override
    public boolean isGranted() {
        if (Build.VERSION.SDK_INT >= 33) {
            return ContextCompat.checkSelfPermission(this.activity,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
     public void blockingRequest() {
         if (!isGranted() && Build.VERSION.SDK_INT >= 33)
         {
             ActivityCompat.requestPermissions(activity, PERMISSION_NOTIFICATION, NOTIFICATION_REQUEST_CODE);
             checkPermissionAfterDelay();
         }
     }

    private void checkPermissionAfterDelay()
    {
        new Handler().postDelayed(() -> {
            if (!isGranted()) {
                displayBlockingAlertDialog(userDialogTitle, userDialogBody);
            }
        }, delayTime);
    }

    public void displayBlockingAlertDialog(String alertTitle, String alertBody){
        super.displayBlockingAlertDialog(alertTitle, alertBody);
    }
 }
