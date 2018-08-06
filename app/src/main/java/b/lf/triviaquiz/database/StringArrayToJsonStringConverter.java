package b.lf.triviaquiz.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class StringArrayToJsonStringConverter {
    @TypeConverter
    public static String[] stringToStringArray(String str){
        if(str == null || str.isEmpty()){
            return new String[0];
        }

        Type arrayType = new TypeToken<String[]>(){}.getType();

        return new Gson().fromJson(str, arrayType);
    }

    @TypeConverter
    public static String stringArrayToString(String[] strings){
        return new Gson().toJson(strings);
    }
}
