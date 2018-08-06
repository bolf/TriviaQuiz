package b.lf.triviaquiz.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.database.UserDao;
import b.lf.triviaquiz.ui.recyclerView.CategoryRecyclerViewAdapter;
import b.lf.triviaquiz.utils.DiskIOExecutor;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.QuizSetupViewModel;

public class QuizSetupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private QuizSetupViewModel mQuizSetupViewModel;
    private CategoryRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_setup);

        RecyclerView categoryRecyclerView = findViewById(R.id.quiz_setup_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new CategoryRecyclerViewAdapter(new ArrayList<>(),null);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setAdapter(mAdapter);

        setupViewModel();
    }

    private void processGettingCurrentUserFromDb(){
        mAdapter.setCategoriesList(mQuizSetupViewModel.getUser().getValue().getChosenQuestionsCategories());
        mAdapter.notifyDataSetChanged();

        //spinner adjusting
        String[] difficultiesArray = getResources().getStringArray(R.array.difficulties_array);

        Spinner spinner = findViewById(R.id.quiz_setup_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficultiesArray);
        adapter.setDropDownViewResource(R.layout.spiner_item_textview);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (mQuizSetupViewModel.getUser().getValue().getDifficulty() != null) {
            int i = 0;
            for (; i < difficultiesArray.length; i++) {
                if (difficultiesArray[i].equals(mQuizSetupViewModel.getUser().getValue().getDifficulty())) {
                    break;
                }
            }
            spinner.setSelection(i);
        }

        ((TextInputEditText) findViewById(R.id.quiz_setup_ti)).setText(String.valueOf(mQuizSetupViewModel.getUser().getValue().getQuestionsQuantity()));

        mQuizSetupViewModel.getUser().removeObservers(this);
    }

    private void setupViewModel() {
        mQuizSetupViewModel = ViewModelProviders.of(this).get(QuizSetupViewModel.class);
        mQuizSetupViewModel.setLiveDataFilter(SharedPreferencesUtils.retrieveCurrentUserId(this));
        mQuizSetupViewModel.getUser().observe(this, usr -> processGettingCurrentUserFromDb());
    }

    public void goToQuestionActivity(View view) {
        final UserDao userDao = TQ_DataBase.getInstance(this).userDao();
        DiskIOExecutor.getInstance().diskIO().execute(() -> userDao.insertUser(mQuizSetupViewModel.getUser().getValue()));
        startActivity(new Intent(this,QuestionActivity.class));
    }

    public void goToCategoryChoosingFromQuizSetup(View view) {
        final UserDao userDao = TQ_DataBase.getInstance(this).userDao();
        DiskIOExecutor.getInstance().diskIO().execute(() -> userDao.insertUser(mQuizSetupViewModel.getUser().getValue()));
        startActivity(new Intent(this,ChoosingQuestionsCategoriesActivity.class));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!((TextInputEditText) findViewById(R.id.quiz_setup_ti)).getText().toString().equals("")) {
            mQuizSetupViewModel.getUser().getValue().setQuestionsQuantity(Integer.parseInt(((TextInputEditText) findViewById(R.id.quiz_setup_ti)).getText().toString()));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mQuizSetupViewModel.getUser().getValue().setDifficulty(((Spinner)findViewById(R.id.quiz_setup_spinner)).getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

}
