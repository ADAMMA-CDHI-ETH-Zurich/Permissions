package AndroidPermissions;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MicrophonePermission extends Permission {
    private final AppCompatActivity activity;
    private static final int MICROPHONE_REQUEST_CODE = 500;
    private static final int delayTime = 7000;   // (ms) Period we wait before checking user's choice (permission granted or not)
    private static final String userDialogTitle = "You need to allow microphone permission";
    private static final String userDialogBody = "In the following screen you will need to allow " +
            "microphone permissions to use this app. If you can't see the option you will need " +
            "to open\nSettings->Apps->CLAIDDemo->Permissions.";
    private static final String[] PERMISSION_NOTIFICATION = {
            Manifest.permission.RECORD_AUDIO
    };

    public MicrophonePermission(AppCompatActivity activity) {
        super(activity);
        this.activity = super.getActivity();
    }

    @Override
    public boolean isGranted() {
        return ContextCompat.checkSelfPermission(this.activity,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void blockingRequest() {
        if (!isGranted())
        {
            ActivityCompat.requestPermissions(activity, PERMISSION_NOTIFICATION, MICROPHONE_REQUEST_CODE);
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
