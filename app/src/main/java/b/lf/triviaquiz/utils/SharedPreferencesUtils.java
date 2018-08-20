package b.lf.triviaquiz.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private static final String PREF_FILE_NAME = "b.lf.triviaquiz.shp";
    private static final String SHARED_PREF_CURR_USER_ID = "currUserId";
    private static final String SHARED_PREF_CURR_USER_NICK = "currUserNick";
    private static final String SHARED_PREF_CURR_USER_ACCURACY = "currUserAccuracy";
    private static final String SHARED_PREF_CATEGORIES_GETTING_TIME = "lastCategoriesGettingTime";

    public static void persistCurrentUserId(Context context, long id) {
        writeLongValue(context, SHARED_PREF_CURR_USER_ID, id);
    }

    public static long retrieveCurrentUserId(Context context) {
        return readLongFromSharedPreferences(context, SHARED_PREF_CURR_USER_ID);
    }

    public static void persistCurrentUserNick(Context context, String nick) {
        writeStringValue(context, SHARED_PREF_CURR_USER_NICK, nick);
    }

    public static String retrieveCurrentUserNick(Context context) {
        return readStringFromSharedPreferences(context, SHARED_PREF_CURR_USER_NICK);
    }

    public static void persistCurrentUserAccuracy(Context context, long accuracy){
        writeLongValue(context, SHARED_PREF_CURR_USER_ACCURACY, accuracy);
    }

    public static long retrieveCurrentUserAccuracy(Context context){
        return readLongFromSharedPreferences(context, SHARED_PREF_CURR_USER_ACCURACY);
    }

    public static void writeLastCategoriesGettingTime(Context context){
       writeLongValue(context, SHARED_PREF_CATEGORIES_GETTING_TIME, System.currentTimeMillis());
    }

    public static long retrieveCategoriesGettingTime(Context context){
        return SharedPreferencesUtils.readLongFromSharedPreferences(context, SHARED_PREF_CATEGORIES_GETTING_TIME);
    }

    //workers
    private static void writeLongValue(Context context, String prefName, long prefVal){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(prefName, prefVal);
        editor.apply();
    }

    private static long readLongFromSharedPreferences(Context context, String prefName){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getLong(prefName,-1);
    }

    private static void writeStringValue(Context context, String prefName, String prefVal){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(prefName, prefVal);
        editor.apply();
    }

    private static String readStringFromSharedPreferences(Context context, String prefName) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPref.getString(prefName,null);
    }

}
