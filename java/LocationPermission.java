package AndroidPermissions;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import com.example.claiddemo.MainActivity;
import com.example.claiddemo.R;

public class LocationPermission extends Permission {
    private static final String userDialogTitleLocation = "You need to allow location permission";
    private static final String userDialogBodyLocation = "You need to allow location permission to use this app";
    private static final String userDialogTitleBackgroundOver29 = "You need to allow location permission";
    private static final String userDialogBodyBackgroundOver29 = "In the following screen you will " +
    "need to select 'Allow all the time'.\nIf you can't see the option you will need to open " +
    "Settings->Apps->CLAIDDemo->Permissions->Location->'Allow all the time'";
    private static final String userDialogTitleFineLocationOver29 = "You need to allow precise location permission";
    private static final String userDialogBodyFineLocationOver29 = "In the following screen you will need to " +
    "select 'Use precise location'.\nIf you can't see the option you will need to open " +
    "Settings->Apps->CLAIDDemo->Permissions->Location->'Use precise location'";

    private final MainActivity activity;
    private final String[] stringPermissionsFineCoarse = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    @SuppressLint("InlinedApi")
    private final String[] stringPermissionsBackground = new String[]{
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };
    @SuppressLint("InlinedApi")
    private final String[] stringPermissionAPI29 = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };

    public LocationPermission(MainActivity activity) {
        super(activity);
        this.activity = super.getActivity();
    }

    @Override
    public boolean isGranted() {
        // If API 29+ we need background permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return (isBackgroundGranted() && isFineGranted());
        // Else we already have background permission if we have coarse & fine location
        return (isCoarseGranted() && isFineGranted());
    }

    public boolean isCoarseGranted() {
        return ContextCompat.checkSelfPermission(
                this.activity, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public boolean isFineGranted() {
        return ContextCompat.checkSelfPermission(
                this.activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public boolean isBackgroundGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ContextCompat.checkSelfPermission(
                    this.activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        return (isCoarseGranted() && isFineGranted());
    }

    @Override
    public void blockingRequest() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                this.activity.registerForActivityResult(new ActivityResultContracts
                        .RequestMultiplePermissions(), result -> {
                    // On API 29+ we can keep asking for "allow all the time"
                    // On API 30+ after two denial the request won't be prompted again
                    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ){
                        if (!isBackgroundGranted()) {
                            displayBlockingAlertDialog(userDialogTitleBackgroundOver29,
                                    userDialogBodyBackgroundOver29);
                        }
                        else if (!isFineGranted()) {
                            displayBlockingAlertDialog(userDialogTitleFineLocationOver29,
                                    userDialogBodyFineLocationOver29);
                        }
                    }
                    else if (!isGranted()) {
                        displayBlockingAlertDialog(userDialogTitleLocation,
                                userDialogBodyLocation);
                    }
                });


        if (isGranted()) {
            System.out.println("We have location permissions");
        }
        // On API 30+ we need to perform incremental permissions request
        else if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.R))
        {
            if (!isFineGranted()) {
                locationPermissionRequest.launch(stringPermissionsFineCoarse);
            }
            if (!isBackgroundGranted()){
                locationPermissionRequest.launch(stringPermissionsBackground);
            }
        }
        // On API 29 we need to ask all permissions together
        else if ((Build.VERSION.SDK_INT == Build.VERSION_CODES.Q)) {
            locationPermissionRequest.launch(stringPermissionAPI29);
        }
        // On API < 29 we don't need background location permission
        else {
            locationPermissionRequest.launch(stringPermissionsFineCoarse);
        }

    }
}
