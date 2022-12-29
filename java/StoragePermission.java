package AndroidPermissions;

import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;


import com.example.claiddemo.BuildConfig;
import com.example.claiddemo.MainActivity;
import com.example.claiddemo.R;

public class StoragePermission extends Permission {
    private final static int APP_STORAGE_ACCESS_REQUEST_CODE = 501;
    private final MainActivity activity;

    public StoragePermission(MainActivity activity) {
        super(activity);
        this.activity = super.getActivity();
    }

    //TODO: when user come back after giving permission, we don't need to reboot app
    @Override
    public void blockingRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (isGranted())
                System.out.println("We have storage permissions");
            else if (!Environment.isExternalStorageManager())
            {
                displayBlockingAlertDialog(activity.getString(R.string.title_storage_permission),
                        activity.getString(R.string.body_storage_permission));
                Intent intent = new Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                activity.startActivityForResult(intent, APP_STORAGE_ACCESS_REQUEST_CODE);
            }
        }
    }


    @Override
    public boolean isGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        return true;
    }

}
