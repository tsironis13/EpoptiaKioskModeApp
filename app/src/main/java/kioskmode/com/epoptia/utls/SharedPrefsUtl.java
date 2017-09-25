package kioskmode.com.epoptia.utls;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import kioskmode.com.epoptia.R;

/**
 * Created by giannis on 5/9/2017.
 */

public class SharedPrefsUtl {

//    private Context mContext;
//
//    public SharedPrefsUtl(Context context) {
//        this.mContext = context;
//    }

    private static SharedPreferences getSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setBooleanPref(Context context, String str, boolean flag) {
        getSharedPrefs(context).edit().putBoolean(str, flag).apply();
    }

    public static boolean getBooleanFlag(Context context, String str) {
        return getSharedPrefs(context).getBoolean(str, false);
    }

    public static void setStringPref(Context context, String str, String flag) {
        getSharedPrefs(context).edit().putString(str, flag).apply();
    }

    public static String getStringFlag(Context context, String str) {
        return getSharedPrefs(context).getString(str, str);
    }

    public static void setIntPref(Context context, int value, String flag) {
        getSharedPrefs(context).edit().putInt(flag, value).apply();
    }

    public static int getIntFlag(Context context, String str) {
        return getSharedPrefs(context).getInt(str, 0);
    }

    public static void removeStringkey(Context context, String key) {
        getSharedPrefs(context).edit().remove(key).apply();
    }
}
