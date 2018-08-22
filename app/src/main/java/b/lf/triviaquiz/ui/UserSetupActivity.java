package b.lf.triviaquiz.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.utils.InsertUserToDbAsyncTask;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.utils.TQ_Application;
import b.lf.triviaquiz.viewModels.TriviaQuizBaseViewModel;

public class UserSetupActivity extends AppCompatActivity {
    private TriviaQuizBaseViewModel mViewModel;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setup);

        mViewModel = ViewModelProviders.of(this).get(TriviaQuizBaseViewModel.class);

        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null && intentExtras.getBoolean(getString(R.string.newUserBooleanIntentExtra), false)) {
            MutableLiveData<User> usr = new MutableLiveData<>();
            usr.setValue(User.getDefaultUser());
            mViewModel.setUser(usr);
            getIntent().removeExtra(getString(R.string.newUserBooleanIntentExtra));
        }

        // Obtain the shared Tracker instance.
        TQ_Application application = (TQ_Application) getApplication();
        mTracker = application.getDefaultTracker();

    }

    private void setUIAccordingToUserState() {
        ((TextInputEditText)findViewById(R.id.user_setup_ti)).setText(mViewModel.getUser().getValue().getNick());
        if (mViewModel.getUser().getValue().getAvatarId() == 0) {
            findViewById(R.id.user_setup_iv_first_girl).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mViewModel.getUser().getValue().getAvatarId() == 1) {
            findViewById(R.id.user_setup_iv_second_girl).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mViewModel.getUser().getValue().getAvatarId() == 10) {
            findViewById(R.id.user_setup_iv_first_man).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        } else if (mViewModel.getUser().getValue().getAvatarId() == 11) {
            findViewById(R.id.user_setup_iv_second_man).setBackground(getDrawable(R.drawable.two_dp_bottom_line));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUIAccordingToUserState();

        mTracker.setScreenName("About~activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mViewModel.getUser().getValue().setNick(((TextInputEditText)findViewById(R.id.user_setup_ti)).getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void goToCategoryChoosing(View view) {
        mViewModel.getUser().getValue().setNick(((TextInputEditText)findViewById(R.id.user_setup_ti)).getText().toString());
        SharedPreferencesUtils.persistCurrentUserId(this, mViewModel.getUser().getValue().getId());
        SharedPreferencesUtils.persistCurrentUserNick(this, mViewModel.getUser().getValue().getNick());

        //here AsyncTask utilization goes!
        new InsertUserToDbAsyncTask(this).execute(mViewModel.getUser().getValue());
    }

    public void onChoosingAvatar(View view){
        findViewById(R.id.user_setup_iv_first_girl).setBackground(null);
        findViewById(R.id.user_setup_iv_first_man).setBackground(null);
        findViewById(R.id.user_setup_iv_second_girl).setBackground(null);
        findViewById(R.id.user_setup_iv_second_man).setBackground(null);

        view.setBackground(getDrawable(R.drawable.two_dp_bottom_line));

        if(view.getId() == R.id.user_setup_iv_first_man){
            mViewModel.getUser().getValue().setAvatarId(10);
        }else if(view.getId() == R.id.user_setup_iv_second_man){
            mViewModel.getUser().getValue().setAvatarId(11);
        }else if(view.getId() == R.id.user_setup_iv_first_girl){
            mViewModel.getUser().getValue().setAvatarId(0);
        }else if(view.getId() == R.id.user_setup_iv_second_girl){
            mViewModel.getUser().getValue().setAvatarId(1);
        }
    }
}
