package b.lf.triviaquiz.viewModels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.Question;
import b.lf.triviaquiz.model.QuestionCategory;

public class QuestionViewModel extends TriviaQuizBaseViewModel {

    private LiveData<List<Question>> questionsFromDb;
    private MutableLiveData<List<String>> questionsLiveDataFilter = new MutableLiveData<>();

    private Map<QuestionCategory, Integer> questionsLoadingTask; //holds map - how many questions per each category

    private List<Question> playingQuestionList;

    private int currentQuestionIndex;

    private Integer limit;
    private String[] difficulties;

    public QuestionViewModel(@NonNull Application application) {
        super(application);

        playingQuestionList = new ArrayList<>();

        TQ_DataBase db = TQ_DataBase.getInstance(this.getApplication());

        questionsFromDb = Transformations.switchMap(questionsLiveDataFilter, value -> db.questionDao().getQuestionsByCategories(value, limit, difficulties));
    }

    public void setDifficulties(String[] difficulties){
        this.difficulties = difficulties;
    }

    public void setLimit(int q){
        this.limit = q;
    }

    public void setQuestionsLiveDataFilter(List<String> categories){
        questionsLiveDataFilter.setValue(categories);
    }

    public LiveData<List<Question>> getQuestionsFromDb(){
        return questionsFromDb;
    }

    public Map<QuestionCategory, Integer> getQuestionsLoadingTask() {
        return questionsLoadingTask;
    }

    public void setQuestionsLoadingTask(Map<QuestionCategory, Integer> questionsLoadingTask) {
        this.questionsLoadingTask = questionsLoadingTask;
    }

    public List<Question> getPlayingQuestionList() {
        return playingQuestionList;
    }

    public void setPlayingQuestionList(List<Question> questionList) {
        this.playingQuestionList = questionList;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

}
