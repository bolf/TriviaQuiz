package b.lf.triviaquiz.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.AnsweredQuestion;
import b.lf.triviaquiz.model.Question;
import b.lf.triviaquiz.model.QuestionCategory;
import b.lf.triviaquiz.model.QuestionWrapper;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.utils.CurrentQuizPersistService;
import b.lf.triviaquiz.utils.DiskIOExecutor;
import b.lf.triviaquiz.utils.NetworkUtils;
import b.lf.triviaquiz.utils.NoticeDialogFragment;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.QuestionViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionActivity extends AppCompatActivity implements NoticeDialogFragment.NoticeDialogListener{
    private QuestionViewModel mQuestionViewModel;
    private boolean clearCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            if (dpWidth < 700) {
                int padding = (int) dpWidth / 4;
                findViewById(R.id.question_layout_root).setPadding(padding, 0, padding, 0);
            }
        }
        RadioGroup rG = findViewById(R.id.radioGroup);
        rG.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 && !clearCheck) {
                mQuestionViewModel.getPlayingQuestionList().get(mQuestionViewModel.getCurrentQuestionIndex()).setCurrentAnswer(((RadioButton) findViewById(checkedId)).getText().toString());
            }else clearCheck = false;
        });
        setUpViewModel();
    }

    private void setUpViewModel() {
        mQuestionViewModel = ViewModelProviders.of(this).get(QuestionViewModel.class);
        mQuestionViewModel.setUserLiveDataFilter(SharedPreferencesUtils.retrieveCurrentUserId(this));

        mQuestionViewModel.setRequestsMade(0);
        if (mQuestionViewModel.getQuestionsFromDb().getValue() != null)
            mQuestionViewModel.getQuestionsFromDb().getValue().clear();
        if (mQuestionViewModel.getQuestionsLoadingTask() != null)
            mQuestionViewModel.getQuestionsLoadingTask().clear();
        if (mQuestionViewModel.getPlayingQuestionList() != null)
            mQuestionViewModel.getPlayingQuestionList().clear();
        mQuestionViewModel.setCurrentQuestionIndex(0);

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
        findViewById(R.id.question_progressBar).setVisibility(View.VISIBLE);
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
            mQuestionViewModel.setLimit(currUser.getQuestionsQuantity());
            String[] difficulties = new String[3];
            if(currUser.getDifficulty() != null  && !currUser.getDifficulty().isEmpty()){
                difficulties[0] = currUser.getDifficulty().toLowerCase();
            }else{
                difficulties[0] = getString(R.string.easy);
                difficulties[1] = getString(R.string.medium);
                difficulties[2] = getString(R.string.hard);
            }
            mQuestionViewModel.setDifficulties(difficulties);
            mQuestionViewModel.getQuestionsFromDb().observe(this, this::processGettingQuestionsFromDb);
        }
    }

    private void loadQuestionsFromNet() {
        for (Map.Entry<QuestionCategory, Integer> entry : mQuestionViewModel.getQuestionsLoadingTask().entrySet()) {
            Map<String, String> queryParams = new HashMap<>();
            //category param
            queryParams.put(getString(R.string.category), String.valueOf(entry.getKey().getId()));
            //number of questions
            queryParams.put(getString(R.string.amount), entry.getValue().toString());
            //difficulty
            String difficulty = mQuestionViewModel.getUser().getValue().getDifficulty();
            if (difficulty != null && !difficulty.isEmpty()) {
                queryParams.put(getString(R.string.difficulty), difficulty.toLowerCase());
            }

            Call<QuestionWrapper> wrapperCall = NetworkUtils.getNetworkService().getQuestions(queryParams);
            wrapperCall.enqueue(new Callback<QuestionWrapper>() {
                @Override
                public void onResponse(@NonNull Call<QuestionWrapper> call, @NonNull Response<QuestionWrapper> response) {
                    try {
                        List<Question> lst = response.body().getResults();
                        mQuestionViewModel.getPlayingQuestionList().addAll(lst);
                        //persist questions
                        final Question[] tmpQArray = new Question[lst.size()];
                        for(int i = 0; i < tmpQArray.length; i++){
                            tmpQArray[i] = lst.get(i);
                        }
                        DiskIOExecutor.getInstance().diskIO().execute(() -> TQ_DataBase.getInstance(QuestionActivity.this).questionDao().bulkInsert(tmpQArray));
                        mQuestionViewModel.setRequestsMade(mQuestionViewModel.getRequestsMade() + 1);
                        if(mQuestionViewModel.getPlayingQuestionList().size() > 0 && ((TextView)findViewById(R.id.question_category_name_tv)).getText().equals("") &&
                                mQuestionViewModel.getRequestsMade() == mQuestionViewModel.getQuestionsLoadingTask().size()) {
                            clearCheck = false;
                            setQuestionOnUi();
                        }
                    } catch (Exception e) {
                        Log.e(e.getClass().getName(), e.getMessage());
                        mQuestionViewModel.setRequestsMade(mQuestionViewModel.getRequestsMade() + 1);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<QuestionWrapper> call, @NonNull Throwable t) {
                    Log.e(t.getClass().getName(), t.getMessage());
                }
            });
        }
    }

    private void setQuestionOnUi(){
        User currUser = mQuestionViewModel.getUser().getValue();

        Question currQuestion = mQuestionViewModel.getPlayingQuestionList().get(mQuestionViewModel.getCurrentQuestionIndex());
        QuestionCategory currCategory = null;
        for(QuestionCategory c : currUser.getChosenQuestionsCategories()){
            if(c.getName().equals(currQuestion.getCategory())){
                currCategory = c;
            }
        }

        if(mQuestionViewModel.getPlayingQuestionList().indexOf(currQuestion) == mQuestionViewModel.getPlayingQuestionList().size() - 1){
            findViewById(R.id.question_go_to_next_btn).setVisibility(View.GONE);
        }else{
            findViewById(R.id.question_go_to_next_btn).setVisibility(View.VISIBLE);
        }

        if(mQuestionViewModel.getPlayingQuestionList().indexOf(currQuestion) == 0){
            findViewById(R.id.question_go_to_prev_btn).setVisibility(View.GONE);
        }else{
            findViewById(R.id.question_go_to_prev_btn).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.radioButton0).setVisibility(View.GONE);
        findViewById(R.id.radioButton1).setVisibility(View.GONE);
        findViewById(R.id.radioButton2).setVisibility(View.GONE);
        findViewById(R.id.radioButton3).setVisibility(View.GONE);

        ((RadioGroup)findViewById(R.id.radioGroup)).clearCheck();

        ((ImageView)findViewById(R.id.question_category_iv)).setImageResource(currCategory.getIconId());
        ((TextView)findViewById(R.id.question_category_name_tv)).setText(currCategory.getName());
        ((TextView)findViewById(R.id.question_difficulty_tv)).setText(getString(R.string.difficulty_title).concat(currQuestion.getDifficulty()));
        ((TextView)findViewById(R.id.question_question_number)).setText(getString(R.string.question_title).concat(" " + String.valueOf(mQuestionViewModel.getCurrentQuestionIndex() + 1)).concat(" of ").concat(String.valueOf(mQuestionViewModel.getPlayingQuestionList().size()))); //Question 16 of 32
        ((TextView)findViewById(R.id.question_question_text)).setText(cleanString(currQuestion.getQuestion()));

        Random rand = new Random();
        int currRandNumOfRightAnswer = rand.nextInt(currQuestion.getIncorrect_answers().length + 1);
        ArrayList<String> tmpAnswersList = new ArrayList<>(Arrays.asList(currQuestion.getIncorrect_answers()));
        tmpAnswersList.add(currRandNumOfRightAnswer, currQuestion.getCorrect_answer());


        for(int i = 0; i < tmpAnswersList.size(); i++){
            RadioButton cRb = (findViewById(getResources().getIdentifier(getString(R.string.radioButton).concat(String.valueOf(i)), getString(R.string.id), getPackageName())));
            if (i == currRandNumOfRightAnswer) {
                cRb.setTextColor(getResources().getColor(R.color.dark_green));
            } else {
                cRb.setTextColor(getResources().getColor(R.color.black));
            }
            cRb.setText(cleanString(tmpAnswersList.get(i)));
            cRb.setVisibility(View.VISIBLE);
            if(tmpAnswersList.get(i).equals(currQuestion.getCurrentAnswer())){
                clearCheck = true;
                cRb.setChecked(true);
            }
        }
        findViewById(R.id.question_progressBar).setVisibility(View.GONE);
    }

    String cleanString(String s){
        return s.replace("&quot;", "\"").replace("&#039;","\'" ).
                replace("&pi;", "PI").replace("&Eacute;","e").
                replace("&eacute;", "e").replace("&ldquo;", "\"").
                replace("&rdquo;", "\"").replace("&ecirc;", "e").
                replace("&ouml;", "o").replace("&amp;", "&").
                replace("&ntilde;", "~").replace("&aacute;","a");
    }

    private void processGettingQuestionsFromDb(List<Question> questionsFromDB) {
        mQuestionViewModel.getQuestionsFromDb().removeObservers(this);
        mQuestionViewModel.getPlayingQuestionList().addAll(questionsFromDB);

        if(mQuestionViewModel.getPlayingQuestionList().size() > 0) {
            clearCheck = false;
            setQuestionOnUi();
        }
        findViewById(R.id.question_progressBar).setVisibility(View.GONE);
    }

    public void showNextQuestion(View view) {
        mQuestionViewModel.setCurrentQuestionIndex(mQuestionViewModel.getCurrentQuestionIndex()+1);
        clearCheck = true;
        setQuestionOnUi();
    }

    public void doneWithCurrentQuestionSet(View view) {
        for (Question q : mQuestionViewModel.getPlayingQuestionList()) {
            if (q.getCurrentAnswer() == null || q.getCurrentAnswer().isEmpty()) {
                DialogFragment dialog = new NoticeDialogFragment();
                dialog.show(getSupportFragmentManager(), getString(R.string.NoticeDialogFragment_tag));
                return;
            }
        }
        findViewById(R.id.question_progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.question_done_btn).setVisibility(View.GONE);
        //persist current quiz to db
        Intent intent = new Intent(this,CurrentQuizPersistService.class);
        intent.putParcelableArrayListExtra(getString(R.string.currentQuizQuestions), getCurrentAnsweredQuestionsList());
        startService(intent);
        startActivity(new Intent(this, AchievementsActivity.class));
        finish();
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        findViewById(R.id.question_progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.question_done_btn).setVisibility(View.GONE);
        //persist current quiz to db
        if (getCurrentAnsweredQuestionsList().size() > 0) {
            Intent intent = new Intent(this, CurrentQuizPersistService.class);
            intent.putParcelableArrayListExtra(getString(R.string.currentQuizQuestions), getCurrentAnsweredQuestionsList());
            startService(intent);
        }
        startActivity(new Intent(this, AchievementsActivity.class));
        finish();
    }


    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        Snackbar.make(findViewById(R.id.question_done_btn), R.string.lets,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public String getDialogMessage() {
        return getString(R.string.unanswered);
    }

    @Override
    public String getDialogTitle() {
        return getString(R.string.live_curr_quiz);
    }

    public void showPrevQuestion(View view) {
        mQuestionViewModel.setCurrentQuestionIndex(mQuestionViewModel.getCurrentQuestionIndex()-1);
        clearCheck = true;
        setQuestionOnUi();
    }

    ArrayList<AnsweredQuestion> getCurrentAnsweredQuestionsList(){
        ArrayList<AnsweredQuestion> tmpLst = new ArrayList<>();
        for (int ind = 0; ind < mQuestionViewModel.getPlayingQuestionList().size(); ind++) {
            Question question = mQuestionViewModel.getPlayingQuestionList().get(ind);
            if(question.getCurrentAnswer() == null || question.getCurrentAnswer().isEmpty()) continue;
            AnsweredQuestion aQTmp = new AnsweredQuestion(
                    question.getCurrentAnswer(),
                    question.getCurrentAnswer().equals(question.getCorrect_answer()),
                    true,
                    mQuestionViewModel.getUser().getValue().getId());
            aQTmp.setCategory(question.getCategory());
            aQTmp.setCorrect_answer(question.getCorrect_answer());
            aQTmp.setDifficulty(question.getDifficulty());
            aQTmp.setQuestion(question.getQuestion());
            tmpLst.add(aQTmp);
        }
        return tmpLst;
    }
}
