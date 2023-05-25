package AndroidPermissions;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.Arrays;
import java.util.Objects;

// "Invisible" activity used to ask permissions
public class PermissionActivity extends AppCompatActivity {
    public static final String EXTRA_PERMISSIONS = "permissions";
    public static final String EXTRA_REQUEST_CODE = "requestCode";
    public static final String EXTRA_DIALOG_TITLE = "dialogTitle";
    public static final String EXTRA_DIALOG_BODY = "dialogBody";
    static final int DEFAULT_PERMISSION_REQUEST_CODE = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme();
        String[] permissions = getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
        int requestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, DEFAULT_PERMISSION_REQUEST_CODE);
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (!Arrays.stream(grantResults).allMatch(result -> result == PackageManager.PERMISSION_GRANTED))
        {
            displayAlertDialog(getIntent().getStringExtra(EXTRA_DIALOG_TITLE), getIntent().getStringExtra(EXTRA_DIALOG_BODY));
        }
        else
        {
            finish();
        }
    }

    private void displayAlertDialog(String dialogTitle, String dialogBody)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogBody);
        builder.setPositiveButton("OK", (dialog, id) -> {
            PackageManager packageManager = this.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(this
                    .getPackageName());
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            this.startActivity(mainIntent);
            Runtime.getRuntime().exit(0);
        });
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    private void setTheme()
    {
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        }
        getWindow().setWindowAnimations(0);
        getWindow().setDimAmount(0.0f);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


}