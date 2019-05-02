package device.com.epoptia.viewgroup;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class StatusBarOverlayCustomViewGroup extends ViewGroup {

    //region Constructor

    public StatusBarOverlayCustomViewGroup(Context context) {
        super(context);
    }

    //endregion

    //region Public Methods

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Touch intercepted!
        return true;
    }

    //endregion

}
