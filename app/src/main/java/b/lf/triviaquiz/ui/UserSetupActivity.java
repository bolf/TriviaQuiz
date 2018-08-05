package b.lf.triviaquiz.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.UserSetupActivityViewModel;

public class UserSetupActivity extends AppCompatActivity {
    private UserSetupActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        mViewModel = ViewModelProviders.of(this).get(UserSetupActivityViewModel.class);

        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null && intentExtras.getBoolean(getString(R.string.newUserBooleanIntentExtra), false)) {
            mViewModel.setUser(User.getDefaultUser());
            getIntent().removeExtra(getString(R.string.newUserBooleanIntentExtra));
        }
    }

    private void setUIAccordingToUserState() {
        ((TextInputEditText)findViewById(R.id.user_setup_ti_nick)).setText(mViewModel.getUser().getNick());
        if (mViewModel.getUser().getAvatarId() == 0) {
            findViewById(R.id.user_setup_iv_first_girl).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mViewModel.getUser().getAvatarId() == 1) {
            findViewById(R.id.user_setup_iv_second_girl).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mViewModel.getUser().getAvatarId() == 10) {
            findViewById(R.id.user_setup_iv_first_man).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mViewModel.getUser().getAvatarId() == 11) {
            findViewById(R.id.user_setup_iv_second_man).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUIAccordingToUserState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mViewModel.getUser().setNick(((TextInputEditText)findViewById(R.id.user_setup_ti_nick)).getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void goToCategoryChoosing(View view) {
        mViewModel.getUser().setNick(((TextInputEditText)findViewById(R.id.user_setup_ti_nick)).getText().toString());
        SharedPreferencesUtils.persistCurrentUserId(this, mViewModel.getUser().getId());

        //here AsyncTask utilization goes!
        new AsyncTask<User,Void,Void>(){
            @Override
            protected Void doInBackground(User... users) {
                TQ_DataBase.getInstance(UserSetupActivity.this).userDao().insertUser(users[0]);
                return null;
            }
        }.execute(mViewModel.getUser());

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
            mViewModel.getUser().setAvatarId(10);
        }else if(view.getId() == R.id.user_setup_iv_second_man){
            mViewModel.getUser().setAvatarId(11);
        }else if(view.getId() == R.id.user_setup_iv_first_girl){
            mViewModel.getUser().setAvatarId(0);
        }else if(view.getId() == R.id.user_setup_iv_second_girl){
            mViewModel.getUser().setAvatarId(1);
        }
    }
}
