package AndroidPermissions;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.claiddemo.MainActivity;

//TODO: test API 24
 public class NotificationPermission extends Permission {
     private final MainActivity activity;
     private static final String userDialogTitle = "You need to allow notification permission";
     private static final String userDialogBody = "In the following screen you will need to allow " +
             "notification permissions to use this app. If you can't see the option you will need " +
             "to open\nSettings->Apps->CLAIDDemo->Permissions.";


     public NotificationPermission(MainActivity activity) {
         super(activity);
         this.activity = super.getActivity();
     }

     @Override
     public void blockingRequest() {
         // Here user still sees the alert also if he allow permission the first time, check if possible to fix
         if (Build.VERSION.SDK_INT >= 33) {
             ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
             if (!isGranted())
             {
                 displayBlockingAlertDialog(userDialogTitle, userDialogBody);
             }
         }
     }


     @Override
     public boolean isGranted() {
         if (Build.VERSION.SDK_INT >= 33) {
             return ContextCompat.checkSelfPermission(this.activity,
                     Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
         }
         return true;
     }

 }
