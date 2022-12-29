package AndroidPermissions;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AlertDialog;
import com.example.claiddemo.MainActivity;
import com.example.claiddemo.R;


public abstract class Permission
{
    private final MainActivity activity;

    public Permission(MainActivity activity){
        this.activity = activity;
    }

    public abstract void blockingRequest();

    public abstract boolean isGranted();

    public void displayBlockingAlertDialog(String permissionTitle, String permissionBody)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(permissionTitle);
        builder.setMessage(permissionBody);
        builder.setPositiveButton(R.string.ok_permission, (DialogInterface.OnClickListener) (dialog, id) -> {
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

    public MainActivity getActivity()
    {
        return activity;
    }
}
