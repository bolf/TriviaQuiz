package b.lf.triviaquiz.viewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.QuestionCategory;

public class CategoriesViewModel extends TriviaQuizBaseViewModel {
    private LiveData<List<QuestionCategory>> categoriesList;

    public CategoriesViewModel(@NonNull Application application){
        super(application);
        TQ_DataBase db = TQ_DataBase.getInstance(this.getApplication());
        categoriesList = db.categoryDao().getAllCategories();
    }

    public LiveData<List<QuestionCategory>> getAllCategories(){
        return categoriesList;
    }
}
