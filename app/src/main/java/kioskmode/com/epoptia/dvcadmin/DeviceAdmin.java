package kioskmode.com.epoptia.dvcadmin;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.widget.Toast;

import kioskmode.com.epoptia.R;

/**
 * Created by giannis on 23/8/2017.
 */

public class DeviceAdmin extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "status enabled");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "admin_receiver_status_disable_warning";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "admin_receiver_status_disabled");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent, UserHandle user) {
        showToast(context, "admin_receiver_status_pw_changed");
    }

    private void showToast(Context context, String msg) {
        String status = msg;
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
    }
}
