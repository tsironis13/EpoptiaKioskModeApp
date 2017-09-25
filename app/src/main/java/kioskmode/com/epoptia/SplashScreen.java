package kioskmode.com.epoptia;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import kioskmode.com.epoptia.admin.LoginAdminActivity;
import kioskmode.com.epoptia.admin.WorkStationsActivity;
import kioskmode.com.epoptia.databinding.SplashScreenBinding;
import kioskmode.com.epoptia.kioskmode.KioskModeActivity;
import kioskmode.com.epoptia.utls.SharedPrefsUtl;

public class SplashScreen extends BaseActivity {

    private static final String debugTag = SplashScreen.class.getSimpleName();
    private SplashScreenBinding mBinding;
//    private OverlayDialog mOverlayDialog;
    private Intent intent;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName mDPM;
    private int actionType;
//    private static final int CALL_PHONE = 1030;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.splash_screen);
        initializeApp();
        intent = getIntent();
//        Log.e(debugTag, "onCreate " + intent);

        actionType = intent.getIntExtra(getResources().getString(R.string.action_type), 0);
        if (actionType == 1020) { //UNLOCK SCREEN
            Log.e(debugTag, "unlock screen FROM ON CREATE");
            PackageManager p = getPackageManager();
            ComponentName cN = new ComponentName(getApplicationContext(), KioskModeActivity.class);
            p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }

//        mBinding.lockBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                lockDevice();
//            }
//        });
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void initializeApp() {
        final boolean authenticated = SharedPrefsUtl.getBooleanFlag(this, getResources().getString(R.string.domain_authenticated));
        final boolean locked = SharedPrefsUtl.getBooleanFlag(this, getResources().getString(R.string.device_locked));
//        startActivity(new Intent(SplashScreen.this, LoginAdminActivity.class));
        if (!authenticated) {
            startActivity(new Intent(SplashScreen.this, LoginAdminActivity.class));
        } else if (!locked) {
            startActivity(new Intent(SplashScreen.this, WorkStationsActivity.class));
        } else {
            startActivity(new Intent(this, KioskModeActivity.class));
        }
    }

}
