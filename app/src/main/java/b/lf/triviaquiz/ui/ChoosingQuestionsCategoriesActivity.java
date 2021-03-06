package b.lf.triviaquiz.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.database.CategoryDao;
import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.database.UserDao;
import b.lf.triviaquiz.model.QuestionCategory;
import b.lf.triviaquiz.model.QuestionCategoryWrapper;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.model.UserAchievements;
import b.lf.triviaquiz.ui.recyclerView.CategoryRecyclerViewAdapter;
import b.lf.triviaquiz.utils.DiskIOExecutor;
import b.lf.triviaquiz.utils.NetworkUtils;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.CategoriesViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosingQuestionsCategoriesActivity extends TriviaQuizBaseActivity{
    private CategoriesViewModel mCategoriesViewModel;
    private GridLayoutManager mLayoutManager;
    private CategoryRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_questions_categories);

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

        //recycler initialization
        mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView mCategoryRecyclerView = findViewById(R.id.recyclerView_categories);
        mCategoryRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CategoryRecyclerViewAdapter(new ArrayList<>(),LayoutInflater.from(this).inflate( //header within recycler view
                R.layout.category_rv_header, mCategoryRecyclerView, false));

        mCategoryRecyclerView.setHasFixedSize(true);
        mCategoryRecyclerView.setAdapter(mAdapter);

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.isHeader(position) ? mLayoutManager.getSpanCount() : 1;
            }
        });
        setupViewModel();
    }

    private void setupViewModel() {
        mCategoriesViewModel = ViewModelProviders.of(this).get(CategoriesViewModel.class);
        mCategoriesViewModel.setUserLiveDataFilter(SharedPreferencesUtils.retrieveCurrentUserId(this));
        mCategoriesViewModel.getUser().observe(this, user -> processGettingCurrentUserFromDb());
    }

    private void processGettingCurrentUserFromDb() {
        mCategoriesViewModel.getUserWithAchievements().observe(this, this::processUserAchievementsGetting);

        mCategoriesViewModel.getAllCategories().observe(this, questionCategories -> {
            //for correct setting appearance let us set the chosen field
            List<QuestionCategory> tmpLst = mCategoriesViewModel.getUser().getValue().getChosenQuestionsCategories();
            for (QuestionCategory chosenCategory : tmpLst) {
                for (QuestionCategory categoryForAdapter : questionCategories) {
                    if (categoryForAdapter.equals(chosenCategory)) {
                        categoryForAdapter.setChosen(true);
                    }
                }
            }
            mAdapter.setCategoriesList(questionCategories);
            mAdapter.notifyDataSetChanged();
            //if today we already have asked the remote server for categories then won't do this again
            if (questionCategories.size() == 0 || (System.currentTimeMillis() - SharedPreferencesUtils.retrieveCategoriesGettingTime(ChoosingQuestionsCategoriesActivity.this)) > 86400000)
                if (NetworkUtils.networkIsAvailable(ChoosingQuestionsCategoriesActivity.this)) {
                    getCategoriesFromNet();
                    SharedPreferencesUtils.writeLastCategoriesGettingTime(ChoosingQuestionsCategoriesActivity.this);
                } else {
                    String msg = getString(R.string.network_s_dowm_warning);
                    Snackbar.make(findViewById(R.id.recyclerView_categories), msg, Snackbar.LENGTH_LONG).show();
                }
        });

    }

    private void processUserAchievementsGetting(UserAchievements userAchievements) {
        //setting curr.user data in the navigation view
        setNavigationViewUserInfo(((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0),mCategoriesViewModel);
    }

    private void getCategoriesFromNet() {
        ((ProgressBar)findViewById(R.id.progressBar_loadingCategories)).setVisibility(View.VISIBLE);
        Call<QuestionCategoryWrapper> wrapperCall = NetworkUtils.getNetworkService().getCategories();
        wrapperCall.enqueue(new Callback<QuestionCategoryWrapper>() {
            @Override
            public void onResponse(Call<QuestionCategoryWrapper> call, Response<QuestionCategoryWrapper> response) {
                try{
                    List<QuestionCategory> lst = response.body().getTrivia_categories();
                    List<QuestionCategory> adapterCurrentList = mAdapter.getmCategoriesList();
                    ArrayList<QuestionCategory> categoriesForDbPersistence = new ArrayList<>();
                    for(QuestionCategory category : lst){
                        if(! adapterCurrentList.contains(category)){
                            categoriesForDbPersistence.add(category);
                        }
                    }
                    if (categoriesForDbPersistence.size() > 0) {
                        final CategoryDao categoryDao = TQ_DataBase.getInstance(ChoosingQuestionsCategoriesActivity.this).categoryDao();
                        for (final QuestionCategory category : categoriesForDbPersistence) {
                            DiskIOExecutor.getInstance().diskIO().execute(() -> {
                                category.setIconId(QuestionCategory.getIconById(category.getId()));
                                categoryDao.insertCategory(category);
                            });
                        }
                    }

                }catch (Exception e){
                    Log.e(e.getClass().getName(),e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<QuestionCategoryWrapper> call, Throwable t) {
                Log.e(t.getClass().getName(),t.getMessage());
            }
        });
        ((ProgressBar)findViewById(R.id.progressBar_loadingCategories)).setVisibility(View.GONE);
    }

    public void goToStarterActivity(View view) {
        commitChosenCategoriesToUserObject();
        Intent intent = new Intent(this,StarterActivity.class);
        startActivity(intent);
    }


    private void commitChosenCategoriesToUserObject(){
        User usr = mCategoriesViewModel.getUser().getValue();
        usr.getChosenQuestionsCategories().clear();
        List<QuestionCategory> tmpLst = mAdapter.getmCategoriesList();
        for(QuestionCategory category : tmpLst){
            if(category.isChosen()){
                usr.getChosenQuestionsCategories().add(category);
            }
        }
        final UserDao userDao = TQ_DataBase.getInstance(ChoosingQuestionsCategoriesActivity.this).userDao();
        DiskIOExecutor.getInstance().diskIO().execute(() -> {
            userDao.insertUser(usr);
        });
    }

    public void goToQuizSetupActivity(View view) {
        commitChosenCategoriesToUserObject();
        Intent intent = new Intent(this,QuizSetupActivity.class);
        //startActivity(intent);

        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent,bundle);
    }
}
