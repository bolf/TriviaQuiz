package b.lf.triviaquiz.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.Question;
import b.lf.triviaquiz.model.QuestionCategory;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.QuestionViewModel;

public class QuestionActivity extends AppCompatActivity {
    private QuestionViewModel mQuestionViewModel;

    private ArrayList<Question> questionList;
    private int currentQuestionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        setUpViewModel();
    }

    private void setUpViewModel() {
        mQuestionViewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);
        mQuestionViewModel.setUserLiveDataFilter(SharedPreferencesUtils.retrieveCurrentUserId(this));
        mQuestionViewModel.getUser().observe(this, usr -> processGettingCurrentUserFromDb());
    }

    private void processGettingCurrentUserFromDb() {
        mQuestionViewModel.getUser().removeObservers(this);

        //1. calculate - the question quantity for chosen categories
        int numQuestionsPerCategory = 2;
        Map<QuestionCategory, Integer> questionsLoadingTask = new HashMap<>();
        User currUser = mQuestionViewModel.getUser().getValue();
        if (currUser.getQuestionsQuantity() >= currUser.getChosenQuestionsCategories().size()) {
            numQuestionsPerCategory = currUser.getChosenQuestionsCategories().size() / currUser.getQuestionsQuantity();
            for (QuestionCategory category : currUser.getChosenQuestionsCategories()) {
                questionsLoadingTask.put(category, numQuestionsPerCategory);
            }
            int remainder = currUser.getQuestionsQuantity() - numQuestionsPerCategory * questionsLoadingTask.size();
            if (remainder > 0) {
                QuestionCategory lastCategory = currUser.getChosenQuestionsCategories().get(currUser.getChosenQuestionsCategories().size() - 1);
                questionsLoadingTask.put(lastCategory, questionsLoadingTask.get(lastCategory) + remainder);
            }
        } else
            for (QuestionCategory category : currUser.getChosenQuestionsCategories()) {
                questionsLoadingTask.put(category, 1);
            }

        //2.
        // get questions from db
        Set<QuestionCategory> categorySet = questionsLoadingTask.keySet();
        ArrayList<String> chosenCategories = new ArrayList<>();
        for(QuestionCategory category : categorySet){
            chosenCategories.add(category.getName());
        }

        mQuestionViewModel.setQuestionsLiveDataFilter(chosenCategories);
        mQuestionViewModel.getQuestions().observe(this, questions -> processGettingQuestionsFromDb(questions));
    }

    private void processGettingQuestionsFromDb(List<Question> questions) {
        mQuestionViewModel.getQuestions().removeObservers(this);
        //3. get questions from remote server

    }

    public void goToNextQuestion(View view) {
        startActivity(new Intent(this,NavActivity.class));

    }
}
