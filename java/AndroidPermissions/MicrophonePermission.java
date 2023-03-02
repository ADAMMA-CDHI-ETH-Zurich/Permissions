package AndroidPermissions;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;


public class MicrophonePermission extends Permission {
    private static final int MICROPHONE_REQUEST_CODE = 500;
    private static final String userDialogTitle = "You need to allow microphone permission";
    private static final String userDialogBody = "In the following screen you need to allow " +
            "microphone permissions to use this app. If you can't see the option you need " +
            "to open\n" + "Settings->Apps->CLAIDDemo->Permissions.";
    private static final String[] RECORDING_PERMISSION = {
            Manifest.permission.RECORD_AUDIO
    };


    @Override
    public boolean isGranted() {
        return (ContextCompat.checkSelfPermission(super.getContext(), RECORDING_PERMISSION[0])
                == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void blockingRequest() {
        if (!isGranted()) {
            super.startIntentWithExtras(RECORDING_PERMISSION, MICROPHONE_REQUEST_CODE, userDialogTitle, userDialogBody);
        }
        while (!isGranted()) {}
    }
}
