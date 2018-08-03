package b.lf.triviaquiz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.QuestionCategory;
import b.lf.triviaquiz.ui.recyclerView.CategoryRecyclerViewAdapter;

public class ChoosingQuestionsCategoriesActivity extends AppCompatActivity {
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_questions_categories);



        RecyclerView categoryRecyclerView = findViewById(R.id.recyclerView_categories);
        mLayoutManager = new GridLayoutManager(this, 2);
        categoryRecyclerView.setLayoutManager(mLayoutManager);
        View header = LayoutInflater.from(this).inflate( //header within recycler view
                R.layout.category_rv_header, categoryRecyclerView, false);
        final CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(getFakeCategoryList(),header);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setAdapter(adapter);

        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? mLayoutManager.getSpanCount() : 1;
            }
        });
    }

    public static ArrayList<QuestionCategory> getFakeCategoryList() {
        ArrayList<QuestionCategory> aL = new ArrayList<>();
        aL.add(new QuestionCategory(9, "General Knowledge"));
        aL.add(new QuestionCategory(10, "Entertainment: Books"));
        aL.add(new QuestionCategory(11, "Entertainment: Film"));
        aL.add(new QuestionCategory(12, "Entertainment: Music"));
        aL.add(new QuestionCategory(13, "Entertainment: Musicals & Theatres"));
        aL.add(new QuestionCategory(14, "Entertainment: Television"));
        aL.add(new QuestionCategory(15, "Entertainment: Video Games"));
        aL.add(new QuestionCategory(16, "Entertainment: Board Games"));
        aL.add(new QuestionCategory(17, "Science & Nature"));
        aL.add(new QuestionCategory(18, "Science: Computers"));
        aL.add(new QuestionCategory(19, "Science: Mathematics"));
        aL.add(new QuestionCategory(20, "Unknown category"));
        return aL;
    }

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
