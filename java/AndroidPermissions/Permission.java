package AndroidPermissions;
import static com.example.claiddemo.PermissionActivity.EXTRA_DIALOG_BODY;
import static com.example.claiddemo.PermissionActivity.EXTRA_DIALOG_TITLE;
import static com.example.claiddemo.PermissionActivity.EXTRA_PERMISSIONS;
import static com.example.claiddemo.PermissionActivity.EXTRA_REQUEST_CODE;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import com.example.claiddemo.PermissionActivity;
import java.util.List;
import JavaCLAID.CLAID;

public abstract class Permission
{
    public abstract void blockingRequest();

    public abstract boolean isGranted();

    public void startIntentWithExtras(String[] permissions, int requestCode, String dialogTitle, String dialogBody)
    {
        while (!isAppOnForeground()){}
        Intent intent = new Intent(getContext(), PermissionActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        intent.putExtra(EXTRA_REQUEST_CODE, requestCode);
        intent.putExtra(EXTRA_DIALOG_TITLE, dialogTitle);
        intent.putExtra(EXTRA_DIALOG_BODY, dialogBody);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) this.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = this.getContext().getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public Context getContext() {return (Context) CLAID.getContext();}

}
