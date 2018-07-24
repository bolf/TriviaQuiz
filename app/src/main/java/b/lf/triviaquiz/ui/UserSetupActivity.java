package b.lf.triviaquiz.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import b.lf.triviaquiz.R;

public class UserSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        setSupportActionBar((Toolbar) findViewById(R.id.app_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void goToCategoryChoosing(View view) {
        Intent intent = new Intent(this, ChoosingQuestionsCategoriesActivity.class);
        startActivity(intent);
    }
}
