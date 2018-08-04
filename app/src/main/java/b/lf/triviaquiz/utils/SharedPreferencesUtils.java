package b.lf.triviaquiz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import b.lf.triviaquiz.model.Session;

public class SharedPreferencesUtils {

    private static final String PREF_FILE_NAME = "b.lf.triviaquiz.shp";
    private static final String SHARED_PREF_SESSION = "session";
    private static final String SHARED_PREF_CATEGORIES_GETTING_TIME = "LastCategoriesGettingTime";

    public static void persistSession(Context context,Session session){
        writeStringValue(context, SHARED_PREF_SESSION,new Gson().toJson(session));
    }

    public static Session retrieveSession(Context context) {
        Session session = null;
        String sessionInJson = SharedPreferencesUtils.readStringFromSharedPreferences(context, SharedPreferencesUtils.SHARED_PREF_SESSION);
        if (sessionInJson != null) {
            session = new Gson().fromJson(sessionInJson, Session.class);
        }
        return session;
    }

    public static void writeStringValue(Context context, String prefName, String prefVal){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(prefName, prefVal);
        editor.apply();
    }

    public static void writeLastCategoriesGettingTime(Context context){
       writeLongValue(context, SHARED_PREF_CATEGORIES_GETTING_TIME, System.currentTimeMillis());
    }

    public static long retrieveCategoriesGettingTime(Context context){
        return SharedPreferencesUtils.readLongFromSharedPreferences(context, SHARED_PREF_CATEGORIES_GETTING_TIME);
    }

    public static void writeLongValue(Context context, String prefName, long prefVal){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(prefName, prefVal);
        editor.apply();
    }

    public static String readStringFromSharedPreferences(Context context, String prefName) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getString(prefName,null);
    }

    public static  long readLongFromSharedPreferences(Context context, String prefName){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getLong(prefName,0);
    }

}
