package b.lf.triviaquiz.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.Question;
import b.lf.triviaquiz.model.QuestionCategory;
import b.lf.triviaquiz.model.QuestionWrapper;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.utils.DiskIOExecutor;
import b.lf.triviaquiz.utils.NetworkUtils;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.QuestionViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionActivity extends AppCompatActivity {
    private QuestionViewModel mQuestionViewModel;

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

        //1. calculate - the question quantity for each chosen category
        int quantityQuestionsPerCategory = 1;
        Map<QuestionCategory, Integer> questionsLoadingTask = new HashMap<>();
        User currUser = mQuestionViewModel.getUser().getValue();
        if (currUser.getQuestionsQuantity() >= currUser.getChosenQuestionsCategories().size()) {
            quantityQuestionsPerCategory = currUser.getQuestionsQuantity() / currUser.getChosenQuestionsCategories().size();
            for (QuestionCategory category : currUser.getChosenQuestionsCategories()) {
                questionsLoadingTask.put(category, quantityQuestionsPerCategory);
            }
            int remainder = currUser.getQuestionsQuantity() - quantityQuestionsPerCategory * questionsLoadingTask.size();
            if (remainder > 0) {
                QuestionCategory lastCategory = currUser.getChosenQuestionsCategories().get(currUser.getChosenQuestionsCategories().size() - 1);
                questionsLoadingTask.put(lastCategory, questionsLoadingTask.get(lastCategory) + remainder);
            }
        } else
            for (QuestionCategory category : currUser.getChosenQuestionsCategories()) {
                questionsLoadingTask.put(category, quantityQuestionsPerCategory);
            }

        mQuestionViewModel.setQuestionsLoadingTask(questionsLoadingTask);

        //2. if network is down then get questions from db otherwise get questions from remote server
        if (NetworkUtils.networkIsAvailable(this)) {
            loadQuestionsFromNet();
        } else {
            Set<QuestionCategory> categorySet = questionsLoadingTask.keySet();
            ArrayList<String> chosenCategories = new ArrayList<>();
            for (QuestionCategory category : categorySet) {
                chosenCategories.add(category.getName());
            }
            mQuestionViewModel.setQuestionsLiveDataFilter(chosenCategories);
            mQuestionViewModel.getQuestions().observe(this, questions -> processGettingQuestionsFromDb(questions));
        }
    }

    private void loadQuestionsFromNet() {
        for (Map.Entry<QuestionCategory, Integer> entry : mQuestionViewModel.getQuestionsLoadingTask().entrySet()) {
            Map<String, String> queryParams = new HashMap<>();
            //category param
            queryParams.put("category", String.valueOf(entry.getKey().getId()));
            //number of questions
            queryParams.put("amount", entry.getValue().toString());
            //difficulty
            String difficulty = mQuestionViewModel.getUser().getValue().getDifficulty();
            if (difficulty != null && !difficulty.isEmpty()) {
                queryParams.put("difficulty", difficulty.toLowerCase());
            }

            Call<QuestionWrapper> wrapperCall = NetworkUtils.getNetworkService().getQuestions(queryParams);
            wrapperCall.enqueue(new Callback<QuestionWrapper>() {
                @Override
                public void onResponse(Call<QuestionWrapper> call, Response<QuestionWrapper> response) {
                    try {
                        final List<Question> lst = response.body().getResults();
                        mQuestionViewModel.getQuestionList().addAll(lst);
                        //persist questions
                        DiskIOExecutor.getInstance().diskIO().execute(() -> {
                            TQ_DataBase.getInstance(QuestionActivity.this).questionDao().bulkInsert((Question[])lst.toArray());
                        });
                    } catch (Exception e) {
                        Log.e(e.getClass().getName(), e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<QuestionWrapper> call, Throwable t) {
                    Log.e(t.getClass().getName(), t.getMessage());
                }
            });
        }
    }


    private void processGettingQuestionsFromDb(List<Question> questionsFromDB) {
        mQuestionViewModel.getQuestions().removeObservers(this);
        mQuestionViewModel.getQuestionList().addAll(questionsFromDB);
    }

    public void goToNextQuestion(View view) {
        startActivity(new Intent(this,NavActivity.class));

    }
}
