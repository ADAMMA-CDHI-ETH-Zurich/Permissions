package AndroidPermissions;


import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


// E.g. usage:
// GenericPermission genericPermission = new GenericPermission(this, "android.permission.CAMERA");
// genericPermission.blockingRequest();
public class GenericPermission extends Permission {
    private final AppCompatActivity activity;
    private static final int GENERIC_REQUEST_CODE = 100;
    private static final int delayTime = 7000;   // (ms) Period we wait before checking user's choice (permission granted or not)
    private String permissionName = null;
    private String[] permissionNames;
    private static final String userDialogTitle = "You need to allow permissions";
    private String userDialogBody = "In the following screen you will need to allow %s\n" +
            "to use this app. If you can't see the option you will need to open Settings->" +
            "Apps->CLAIDDemo->Permissions.";

    public GenericPermission(AppCompatActivity activity, String manifestPermissionName) {
        super(activity);
        this.activity = super.getActivity();
        this.permissionName = manifestPermissionName;
        this.userDialogBody = String.format(userDialogBody, manifestPermissionName);
    }

    public GenericPermission(AppCompatActivity activity, String[] manifestPermissionNames) {
        super(activity);
        this.activity = super.getActivity();
        this.permissionNames = manifestPermissionNames;
        userDialogBody = String.format(userDialogBody,
                String.join(" and ", manifestPermissionNames));
    }

    @Override
    public boolean isGranted() {
        boolean isGranted = true;
        if (permissionName == null) // So we have multiple permission request, not only one
        {
            for (String singlePermission: permissionNames) {
                isGranted = (ContextCompat.checkSelfPermission(activity, singlePermission)
                        == PackageManager.PERMISSION_GRANTED) && isGranted;
            }
            return isGranted;
        }
        isGranted = ContextCompat.checkSelfPermission(activity, permissionName)
                == PackageManager.PERMISSION_GRANTED;
        return isGranted;
    }

    @Override
    public void blockingRequest() {
        if (!isGranted())
        {
            if (permissionName != null) {
                ActivityCompat.requestPermissions(activity, new String[]{permissionName}, GENERIC_REQUEST_CODE);
                checkPermissionAfterDelay();
            }
            else if (permissionNames.length != 0) {
                ActivityCompat.requestPermissions(activity, permissionNames, GENERIC_REQUEST_CODE);
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
