package b.lf.triviaquiz.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.QuestionCategory;
import b.lf.triviaquiz.model.Session;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;

public class UserSetupActivity extends AppCompatActivity {
    private Session mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle intentExtras = getIntent().getExtras();
        if(intentExtras != null && intentExtras.getBoolean("newSession",false)){
            mSession = new Session(User.getDefaultUser(),new ArrayList<QuestionCategory>(),0,null);
            getIntent().removeExtra("newSession");
        }else{
            mSession = SharedPreferencesUtils.retrieveSession(this);
        }

        setUIAccordingToSessionState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mSession.getUser().setNick(((TextInputEditText)findViewById(R.id.user_setup_ti_nick)).getText().toString());
        SharedPreferencesUtils.persistSession(this, mSession);
        super.onSaveInstanceState(outState);
    }

    private void setUIAccordingToSessionState() {
        ((TextInputEditText)findViewById(R.id.user_setup_ti_nick)).setText(mSession.getUser().getNick());
        if (mSession.getUser().getAvatarId() == 0) {
            findViewById(R.id.user_setup_iv_first_girl).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mSession.getUser().getAvatarId() == 1) {
            findViewById(R.id.user_setup_iv_second_girl).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mSession.getUser().getAvatarId() == 10) {
            findViewById(R.id.user_setup_iv_first_man).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mSession.getUser().getAvatarId() == 11) {
            findViewById(R.id.user_setup_iv_second_man).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        }
    }

    public void goToCategoryChoosing(View view) {
        mSession.getUser().setNick(((TextInputEditText)findViewById(R.id.user_setup_ti_nick)).getText().toString());
        SharedPreferencesUtils.persistSession(this, mSession);

        //here AsyncTask usage goes!
        new AsyncTask<User,Void,Void>(){
            @Override
            protected Void doInBackground(User... users) {
                TQ_DataBase.getInstance(UserSetupActivity.this).userDao().insertUser(users[0]);
                return null;
            }
        }.execute(mSession.getUser());

        Intent intent = new Intent(this, ChoosingQuestionsCategoriesActivity.class);
        startActivity(intent);
        finish();
    }

    public void onChoosingAvatar(View view){
        findViewById(R.id.user_setup_iv_first_girl).setBackground(null);
        findViewById(R.id.user_setup_iv_first_man).setBackground(null);
        findViewById(R.id.user_setup_iv_second_girl).setBackground(null);
        findViewById(R.id.user_setup_iv_second_man).setBackground(null);

        view.setBackground(getDrawable(R.drawable.two_dp_bottom_line));

        if(view.getId() == R.id.user_setup_iv_first_man){
            mSession.getUser().setAvatarId(10);
        }else if(view.getId() == R.id.user_setup_iv_second_man){
            mSession.getUser().setAvatarId(11);
        }else if(view.getId() == R.id.user_setup_iv_first_girl){
            mSession.getUser().setAvatarId(0);
        }else if(view.getId() == R.id.user_setup_iv_second_girl){
            mSession.getUser().setAvatarId(1);
        }
    }
}
