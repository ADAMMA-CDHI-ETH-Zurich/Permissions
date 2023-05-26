package AndroidPermissions;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;


public class StoragePermission extends Permission {
    private final static int APP_STORAGE_REQUEST_CODE = 400;
    private static final String userDialogTitle = "You need to allow storage permission";
    private static final String userDialogBody = "In the following screen you need to allow" +
            " storage permission to use this app";
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    public boolean isGranted() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
        {
            return ActivityCompat.checkSelfPermission(super.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(super.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        else
        {
            return Environment.isExternalStorageManager();
        }
    }

    @Override
    public void blockingRequest() {
        if (isGranted())
            System.out.println("We have storage permissions");
        else {
            this.startIntentWithExtras(PERMISSIONS_STORAGE, APP_STORAGE_REQUEST_CODE, userDialogTitle, userDialogBody);
        }
        while (!isGranted()){}
    }

    @Override
    public void startIntentWithExtras(String[] permissions, int requestCode, String dialogTitle, String dialogBody)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
        {
            super.startIntentWithExtras(permissions, requestCode, dialogTitle, dialogBody);
        }
        else {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", super.getContext().getPackageName())));
                super.getContext().startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                super.getContext().startActivity(intent);
            }

            // This is used to prevent line 76 (super.startIntentWithExtra) to start
            // ... before settings are prompted using Intent, in the above lines
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (!super.isAppOnForeground()) {}

            if (!isGranted())
                super.startIntentWithExtras(PERMISSIONS_STORAGE,
                        APP_STORAGE_REQUEST_CODE, userDialogTitle, userDialogBody);
        }
    }

}
