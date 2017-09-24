package kioskmode.com.epoptia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import kioskmode.com.epoptia.app.utils.LifecycleHandler;

/**
 * Created by giannis on 27/8/2017.
 */

public class BaseActivity extends AppCompatActivity implements LifecycleHandler.AppStateListener {

    private static final String debugTag = BaseActivity.class.getSimpleName();
    private boolean removeListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            Log.e(debugTag, "savedInstanceState NOT NULL");
//            removeListener = savedInstanceState.getBoolean("removeListener");
//        } else {
//            LifecycleHandler.get(this).addListener(this);
//            Log.e(debugTag, "savedInstanceState NULL");
//        }
    }

    @Override
    public void onBecameForeground() {

    }

    @Override
    public void onBecameBackground() {

    }

    public void showSnackBrMsg(String msg, View container, int length) {
        Snackbar snackbar = Snackbar.make(container, msg, length);
        snackbar.show();
    }
}
