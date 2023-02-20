package AndroidPermissions;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class StoragePermission extends Permission {
    private final AppCompatActivity activity;
    private final static int APP_STORAGE_REQUEST_CODE = 400;
    private static final int delayTime = 7000;   // (ms) Period we wait before checking user's choice (permission granted or not)
    private static final String userDialogTitle = "You need to allow storage permission";
    private static final String userDialogBody = "In the following screen you will need to allow" +
            " storage permission to use this app";
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public StoragePermission(AppCompatActivity activity) {
        super(activity);
        this.activity = super.getActivity();
    }

    @Override
    public boolean isGranted() {
        int permissionStatus = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void blockingRequest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (isGranted())
                System.out.println("We have storage permissions");
            else {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, APP_STORAGE_REQUEST_CODE);
                checkPermissionAfterDelay();
            }
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
