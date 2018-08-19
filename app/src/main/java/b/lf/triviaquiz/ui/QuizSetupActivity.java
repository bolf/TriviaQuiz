package b.lf.triviaquiz.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.database.UserDao;
import b.lf.triviaquiz.model.UserAchievements;
import b.lf.triviaquiz.ui.recyclerView.CategoryRecyclerViewAdapter;
import b.lf.triviaquiz.utils.DiskIOExecutor;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.TriviaQuizBaseViewModel;

public class QuizSetupActivity extends TriviaQuizBaseActivity implements AdapterView.OnItemSelectedListener{
    private TriviaQuizBaseViewModel mQuizSetupViewModel;
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

        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ((TextInputEditText)findViewById(R.id.quiz_setup_ti)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String questQuantity = ((TextInputEditText) findViewById(R.id.quiz_setup_ti)).getText().toString();
                if(questQuantity.isEmpty()){
                    mQuizSetupViewModel.getUser().getValue().setQuestionsQuantity(0);
                }else {
                    mQuizSetupViewModel.getUser().getValue().setQuestionsQuantity(Integer.parseInt(questQuantity));
                }
            }
        });

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

        mQuizSetupViewModel.getUserWithAchievements().observe(this,this::processUserAchievementsGetting);
    }

    private void processUserAchievementsGetting(UserAchievements userAchievements) {
        //setting curr.user data in the navigation view
        setNavigationViewUserInfo(((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0),mQuizSetupViewModel);
    }

    private void setupViewModel() {
        mQuizSetupViewModel = ViewModelProviders.of(this).get(TriviaQuizBaseViewModel.class);
        mQuizSetupViewModel.setUserLiveDataFilter(SharedPreferencesUtils.retrieveCurrentUserId(this));
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