package b.lf.triviaquiz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import b.lf.triviaquiz.R;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }

    public void goToNextQuestion(View view) {
        startActivity(new Intent(this,NavActivity.class));

    }
}
