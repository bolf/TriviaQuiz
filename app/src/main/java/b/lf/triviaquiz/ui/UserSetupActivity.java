package b.lf.triviaquiz.ui;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;

public class UserSetupActivity extends AppCompatActivity {
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mUser == null) {
            String userInJson = SharedPreferencesUtils.readStringFromSharedPreferences(this, SharedPreferencesUtils.SHARED_PREF_USER);
            if (userInJson != null) {
                mUser = new Gson().fromJson(userInJson, User.class);
            } else {
                mUser = User.getDefaultUser();
            }
        }

        setUIAccordingToUserObject();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mUser.setNick(((TextInputEditText)findViewById(R.id.user_setup_ti_nick)).getText().toString());
        SharedPreferencesUtils.writeStringValue(this, SharedPreferencesUtils.SHARED_PREF_USER,new Gson().toJson(mUser));
        super.onSaveInstanceState(outState);
    }

    private void setUIAccordingToUserObject() {
        ((TextInputEditText)findViewById(R.id.user_setup_ti_nick)).setText(mUser.getNick());
        if (mUser.getAvatarId() == 0) {
            findViewById(R.id.user_setup_iv_first_girl).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mUser.getAvatarId() == 1) {
            findViewById(R.id.user_setup_iv_second_girl).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mUser.getAvatarId() == 10) {
            findViewById(R.id.user_setup_iv_first_man).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mUser.getAvatarId() == 11) {
            findViewById(R.id.user_setup_iv_second_man).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        }
    }

    public void goToCategoryChoosing(View view) {
        Intent intent = new Intent(this, ChoosingQuestionsCategoriesActivity.class);
        startActivity(intent);
    }

    public void onChoosingAvatar(View view){
        findViewById(R.id.user_setup_iv_first_girl).setBackground(null);
        findViewById(R.id.user_setup_iv_first_man).setBackground(null);
        findViewById(R.id.user_setup_iv_second_girl).setBackground(null);
        findViewById(R.id.user_setup_iv_second_man).setBackground(null);

        view.setBackground(getDrawable(R.drawable.two_dp_bottom_line));

        if(view.getId() == R.id.user_setup_iv_first_man){
            mUser.setAvatarId(10);
        }else if(view.getId() == R.id.user_setup_iv_second_man){
            mUser.setAvatarId(11);
        }else if(view.getId() == R.id.user_setup_iv_first_girl){
            mUser.setAvatarId(0);
        }else if(view.getId() == R.id.user_setup_iv_second_girl){
            mUser.setAvatarId(1);
        }
    }
}
