package b.lf.triviaquiz.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import b.lf.triviaquiz.model.QuestionCategory;

public class CategoriesListToJsonStringConverter {
    @TypeConverter
    public static List<QuestionCategory> stringToQuestionCategoryList(String str) {

        if (str == null || str.isEmpty()) {
            return new ArrayList<>();
        }

        Type listType = new TypeToken<List<QuestionCategory>>() {}.getType();

        return new Gson().fromJson(str, listType);
    }

    @TypeConverter
    public static String questionCategoryListToString(List<QuestionCategory> categories) {
        return new Gson().toJson(categories);
    }

}
