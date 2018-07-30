package b.lf.triviaquiz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import b.lf.triviaquiz.R;

public class SharedPreferencesUtils {
    public static void writeStringValue(Context context, String shPrefName, String prefName, String prefVal){
        SharedPreferences sharedPref = context.getSharedPreferences(shPrefName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(prefName, prefVal);
        editor.apply();
    }

    public static void writeIntValue(Context context, String shPrefName, String prefName, int prefVal){
        SharedPreferences sharedPref = context.getSharedPreferences(shPrefName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(prefName, prefVal);
        editor.apply();
    }

    public static String readStringFromSharedPreferences(Context context, String shPrefName, String prefName) {
        SharedPreferences sharedPref = context.getSharedPreferences(shPrefName,Context.MODE_PRIVATE);
        return sharedPref.getString(prefName,null);
    }

    public static  int readIntFromSharedPreferences(Context context, String shPrefName, String prefName){
        SharedPreferences sharedPref = context.getSharedPreferences(shPrefName,Context.MODE_PRIVATE);
        return sharedPref.getInt(prefName,0);
    }

}
