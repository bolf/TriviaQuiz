package b.lf.triviaquiz.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.List;

import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.Question;
import b.lf.triviaquiz.model.User;

public class QuestionViewModel extends AndroidViewModel {
    private LiveData<User> user;
    private MutableLiveData<Long> userLiveDataFilter = new MutableLiveData<>();

    private LiveData<List<Question>> questions;
    private MutableLiveData<List<String>> questionsLiveDataFilter = new MutableLiveData<>();

    public QuestionViewModel(@NonNull Application application) {
        super(application);

        TQ_DataBase db = TQ_DataBase.getInstance(this.getApplication());
        user = Transformations.switchMap(userLiveDataFilter, value -> db.userDao().getUserById(value));

        questions = Transformations.switchMap(questionsLiveDataFilter, value -> db.questionDao().getQuestionsByCategories(value));
    }

    public void setUserLiveDataFilter(Long id){
        userLiveDataFilter.setValue(id);
    }

    public LiveData<User> getUser(){
        return user;
    }

    public void setQuestionsLiveDataFilter(List<String> categories){
        questionsLiveDataFilter.setValue(categories);
    }

    public LiveData<List<Question>> getQuestions(){
        return questions;
    }

}
