package AndroidPermissions;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;


public class LocationPermission extends Permission {
    private static final int LOCATION_REQUEST_CODE = 200;
    private static final String userDialogTitleLocation = "You need to allow location permission";
    private static final String userDialogBodyLocation = "You need to allow location permission to use this app." +
    " If you can't see the option you need to open\n" + "Settings->Apps->CLAIDDemo->Permissions";
    private static final String userDialogTitleBackgroundOver29 = "You need to allow location permission";
    private static final String userDialogBodyBackgroundOver29 = "In the following screen you " +
    "need to select 'Allow all the time'.\nIf you can't see the option you need to open " +
    "Settings->Apps->CLAIDDemo->Permissions->Location->'Allow all the time'";
    private static final String userDialogTitleFineLocationOver29 = "You need to allow precise location permission";
    private static final String userDialogBodyFineLocationOver29 = "In the following screen you need to " +
    "select 'Use precise location'.\nIf you can't see the option you need to open " +
    "Settings->Apps->CLAIDDemo->Permissions->Location->'Use precise location'";
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
                super.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public boolean isFineGranted() {
        return ContextCompat.checkSelfPermission(
                super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public boolean isBackgroundGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ContextCompat.checkSelfPermission(
                    super.getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        return (isCoarseGranted() && isFineGranted());
    }

    @Override
    public void blockingRequest() {
            if (isGranted()) {
                System.out.println("We have location permissions");
            }
            // On API 30+ we need to perform incremental permissions request
            else if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)) {
                if (!isFineGranted()) {
                    super.startIntentWithExtras(stringPermissionsFineCoarse, LOCATION_REQUEST_CODE, userDialogTitleFineLocationOver29, userDialogBodyFineLocationOver29);
                }

                while (!isFineGranted()) {
                }

                if (!isBackgroundGranted()) {
                    super.startIntentWithExtras(stringPermissionsBackground, LOCATION_REQUEST_CODE, userDialogTitleBackgroundOver29, userDialogBodyBackgroundOver29);
                }
            }
            // On API 29 we need to ask all permissions together
            else if ((Build.VERSION.SDK_INT == Build.VERSION_CODES.Q)) {
                if (!isBackgroundGranted() || !isFineGranted()) {
                    super.startIntentWithExtras(stringPermissionAPI29, LOCATION_REQUEST_CODE, userDialogTitleBackgroundOver29, userDialogBodyBackgroundOver29);
                }
            }
            // On API < 29 we don't need background location permission
            else {
                if (!isFineGranted()) {
                    super.startIntentWithExtras(stringPermissionsFineCoarse, LOCATION_REQUEST_CODE, userDialogTitleLocation, userDialogBodyLocation);
                }
            }
            while (!isGranted()){}
    }

}

