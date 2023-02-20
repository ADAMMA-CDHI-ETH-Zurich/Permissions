package AndroidPermissions;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public abstract class Permission
{
    private final AppCompatActivity activity;

    public Permission(AppCompatActivity activity){
        this.activity = activity;
    }

    public abstract void blockingRequest();

    public abstract boolean isGranted();

    public void displayBlockingAlertDialog(String permissionTitle, String permissionBody)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(permissionTitle);
        builder.setMessage(permissionBody);
        builder.setPositiveButton("OK", (dialog, id) -> {
            PackageManager packageManager = activity.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(activity
                    .getPackageName());
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            activity.startActivity(mainIntent);
            Runtime.getRuntime().exit(0);
        });
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    public AppCompatActivity getActivity()
    {
        return activity;
    }
}
