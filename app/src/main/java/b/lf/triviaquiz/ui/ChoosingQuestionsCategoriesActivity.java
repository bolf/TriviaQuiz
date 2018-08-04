package b.lf.triviaquiz.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.QuestionCategory;
import b.lf.triviaquiz.model.Session;
import b.lf.triviaquiz.ui.recyclerView.CategoryRecyclerViewAdapter;
import b.lf.triviaquiz.utils.NetworkUtils;
import b.lf.triviaquiz.viewModels.CategoriesViewModel;

public class ChoosingQuestionsCategoriesActivity extends AppCompatActivity {
    private Session mSession;
    private List<QuestionCategory> chosenCategoriesList;
    private GridLayoutManager mLayoutManager;
    private CategoryRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_questions_categories);

        //recycler initialization
        mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView mCategoryRecyclerView = findViewById(R.id.recyclerView_categories);
        mCategoryRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CategoryRecyclerViewAdapter(new ArrayList<QuestionCategory>(),LayoutInflater.from(this).inflate( //header within recycler view
                R.layout.category_rv_header, mCategoryRecyclerView, false));;

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
        CategoriesViewModel categoriesViewModel = ViewModelProviders.of(this).get(CategoriesViewModel.class);
        categoriesViewModel.getAllCategories().observe(this, new Observer<List<QuestionCategory>>() {
            @Override
            public void onChanged(@Nullable List<QuestionCategory> questionCategories) {
                //read from db categories
                mAdapter.setCategoriesList(questionCategories);
                if(NetworkUtils.networkIsAvailable(ChoosingQuestionsCategoriesActivity.this)){
                    Bundle extras = getIntent().getExtras();
                    if(extras == null || (extras != null && !extras.getBoolean("categoriesRequestIsAlreadyDone",true))){


                        getIntent().putExtra("categoriesRequestIsAlreadyDone", true);
                    }
                }else{
                    String msg = "network is down now. can not get updates for categories";
                    Snackbar.make(findViewById(R.id.recyclerView_categories), msg, Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }



//    private void showSnackBar(String msg){
//        Snackbar.make(findViewById(R.id.recyclerView_categories), msg, Snackbar.LENGTH_SHORT).show();
//    }

//    public static ArrayList<QuestionCategory> getFakeCategoryList() {
//        ArrayList<QuestionCategory> aL = new ArrayList<>();
//        aL.add(new QuestionCategory(9, "General Knowledge"));
//        aL.add(new QuestionCategory(10, "Entertainment: Books"));
//        aL.add(new QuestionCategory(11, "Entertainment: Film"));
//        aL.add(new QuestionCategory(12, "Entertainment: Music"));
//        aL.add(new QuestionCategory(13, "Entertainment: Musicals & Theatres"));
//        aL.add(new QuestionCategory(14, "Entertainment: Television"));
//        aL.add(new QuestionCategory(15, "Entertainment: Video Games"));
//        aL.add(new QuestionCategory(16, "Entertainment: Board Games"));
//        aL.add(new QuestionCategory(17, "Science & Nature"));
//        aL.add(new QuestionCategory(18, "Science: Computers"));
//        aL.add(new QuestionCategory(19, "Science: Mathematics"));
//        aL.add(new QuestionCategory(20, "Unknown category"));
//        return aL;
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void goToStarterActivity(View view) {
        Intent intent = new Intent(this,StarterActivity.class);
        startActivity(intent);
    }

    public void goToQuizSetupActivity(View view) {
        Intent intent = new Intent(this,QuizSetupActivity.class);
        startActivity(intent);
    }
}
