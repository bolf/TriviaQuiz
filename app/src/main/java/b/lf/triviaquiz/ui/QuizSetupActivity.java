package b.lf.triviaquiz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.ui.recyclerView.CategoryRecyclerViewAdapter;

public class QuizSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_setup);

        RecyclerView categoryRecyclerView = findViewById(R.id.chosen_categories_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        //CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(ChoosingQuestionsCategoriesActivity.getFakeCategoryList(),null);
        //categoryRecyclerView.setHasFixedSize(true);
        //categoryRecyclerView.setAdapter(adapter);



    }

    public void goToQuestionActivity(View view) {
        Intent intrnt = new Intent(this,QuestionActivity.class);
        startActivity(intrnt);
    }
}
