package kioskmode.com.epoptia;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kioskmode.com.epoptia.databinding.ActivityMainBinding;
import kioskmode.com.epoptia.dvcadmin.DeviceAdmin;

public class MainActivity extends BaseActivity {

    private static final String debugTag = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
//    private OverlayDialog mOverlayDialog;
    private Intent intent;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName mDPM;
    private int actionType;
    private static final int PHONE_STATE = 1020;
    public static final int OVERLAY_PERMISSION_REQ_CODE = 4545;
//    private static final int CALL_PHONE = 1030;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        intent = getIntent();
//        Log.e(debugTag, "onCreate " + intent);

        actionType = intent.getIntExtra(getResources().getString(R.string.action_type), 0);
        if (actionType == 1020) { //UNLOCK SCREEN
            Log.e(debugTag, "unlock screen FROM ON CREATE");
            PackageManager p = getPackageManager();
            ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
            p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }

        mBinding.lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockDevice();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
//            actionType = intent.getExtras().getInt(getResources().getString(R.string.action_type));
            actionType = intent.getIntExtra(getResources().getString(R.string.action_type), 0);
            if (actionType == 1020) { //UNLOCK SCREEN
                Log.e(debugTag, "unlock screen FROM ON NEW INTENT");
                PackageManager p = getPackageManager();
                ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
                p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        canDrawOverlays();
                    }
                } else {
                    Toast.makeText(this, "PHONE STATE permission denied! App cannot be locked!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
//            case CALL_PHONE: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "CALL_PHONE Permission granted!", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, "No CALL_PHONE permission granted!", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e(debugTag, "onActivityResult Result code => " + resultCode + " Request code => " +requestCode);
        switch (requestCode) {
            case OVERLAY_PERMISSION_REQ_CODE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        Toast.makeText(this, "User cannot access system settings without this permission!App cannot be locked", Toast.LENGTH_SHORT).show();
                    } else {
                        checkAppIsDefaultLauncher();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private boolean isMyAppLauncherDefault() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);

        List<IntentFilter> filters = new ArrayList<IntentFilter>();
        filters.add(filter);

        final String myPackageName = getPackageName();
        List<ComponentName> activities = new ArrayList<ComponentName>();
        final PackageManager packageManager = (PackageManager) getPackageManager();

        // You can use name of your package here as third argument
        packageManager.getPreferredActivities(filters, activities, null);

        for (ComponentName activity : activities) {
//            Log.e(debugTag, activity.getPackageName() +" ACTIVITIES");
            if (myPackageName.equals(activity.getPackageName())) {
//                Log.e(debugTag, myPackageName +" MY PACKAGE");
                return true;
            }
        }
        return false;
    }

    private void lockDevice() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            checkAppIsDefaultLauncher();
        } else {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE);
                }
            } else {
                canDrawOverlays();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void canDrawOverlays() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            checkAppIsDefaultLauncher();
        }
    }

    private void checkAppIsDefaultLauncher() {
        if (!isMyAppLauncherDefault()) {
            Log.e(debugTag, "my app is not default launcher");
            PackageManager p = getPackageManager();
            ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
            p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            Intent selector = new Intent(Intent.ACTION_MAIN);
            selector.addCategory(Intent.CATEGORY_HOME);

            PackageManager localPackageManager = getPackageManager();
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            String str = localPackageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;

            if (!str.equals("kioskmode.com.epoptia")) {
//                Intent intnt = new Intent(Intent.ACTION_MAIN);
//                intnt.addCategory(Intent.CATEGORY_HOME);
//                intnt.addCategory(Intent.CATEGORY_DEFAULT);
//                intnt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                startActivity(Intent.createChooser(intnt, "Set as default to enable Kiosk Mode"));
            }

            Log.e("Curt launcher Package:", str);


            startActivity(selector);
        } else {
            Log.e(debugTag, "my app is default launcher");
            PackageManager p = getPackageManager();
            ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
            p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            startActivity(new Intent(MainActivity.this, KioskModeActivity.class));
        }
    }
}
