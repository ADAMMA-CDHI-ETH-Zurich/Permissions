package AndroidPermissions;

import android.content.pm.PackageManager;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import com.example.claiddemo.MainActivity;
import com.example.claiddemo.R;


//On API 30+ after two denial, the permission request won't be prompted again
public class GenericPermission extends Permission {
    private final MainActivity activity;
    private String permissionName = null;
    private String[] permissionNames;
    private final String userDialogTitle;
    private final String userDialogBody;

    public GenericPermission(MainActivity activity, String manifestPermissionName) {
        super(activity);
        this.activity = super.getActivity();
        this.permissionName = manifestPermissionName;
        userDialogTitle = activity.getString(R.string.title_generic_permission);
        userDialogBody = activity.getString(R.string.body_generic_permission,
                manifestPermissionName);
    }

    public GenericPermission(MainActivity activity, String[] manifestPermissionNames) {
        super(activity);
        this.activity = super.getActivity();
        this.permissionNames = manifestPermissionNames;
        userDialogTitle = activity.getString(R.string.title_generic_permission);
        userDialogBody = activity.getString(R.string.body_generic_permission,
                String.join(" and ", manifestPermissionNames));
    }

    @Override
    public void blockingRequest() {
        if (permissionName != null) {
            ActivityResultLauncher<String> genericPermissionRequest =
                    activity.registerForActivityResult(new ActivityResultContracts
                            .RequestPermission(), result -> {
                        if (!isGranted()) {
                            this.displayBlockingAlertDialog(userDialogTitle, userDialogBody);
                        }
                    });

            if (isGranted()) {
                System.out.println("We have " + permissionName + " permission");
            } else {
                genericPermissionRequest.launch(permissionName);
            }
        }

        else if (permissionNames.length != 0){
            ActivityResultLauncher<String[]> genericPermissionsRequest =
                    activity.registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        if (!isGranted()) {
                            this.displayBlockingAlertDialog(userDialogTitle, userDialogBody);
                        }
                    });

            if (isGranted()) {
                System.out.println("We have " + permissionName + " permission");
            } else {
                genericPermissionsRequest.launch(permissionNames);
            }
        }
    }

    @Override
    public boolean isGranted() {
        boolean isGranted = true;
        if (permissionName == null)
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

}
