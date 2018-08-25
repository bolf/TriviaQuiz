package b.lf.triviaquiz.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.AnsweredQuestion;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.model.UserAchievements;
import b.lf.triviaquiz.utils.DiskIOExecutor;
import b.lf.triviaquiz.utils.NoticeDialogFragment;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.TriviaQuizBaseViewModel;


//implements OnNavigationItemSelectedListener
public class TriviaQuizBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NoticeDialogFragment.NoticeDialogListener{
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_achievements) {
            startActivity(new Intent(this,AchievementsActivity.class));
        } else if (id == R.id.nav_reset_total) {

            DialogFragment dialog = new NoticeDialogFragment();
            dialog.show(getSupportFragmentManager(), getString(R.string.NoticeDialogFragment_tag));

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_set_questions_categories) {
            startActivity(new Intent(this,ChoosingQuestionsCategoriesActivity.class));
        } else if (id == R.id.nav_set_questions_quantity_difficulty) {
            startActivity(new Intent(this,QuizSetupActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setNavigationViewUserInfo(View header, TriviaQuizBaseViewModel viewModel){
        UserAchievements userAchievements = viewModel.getUserWithAchievements().getValue();
        //set picture & text
        ((TextView)header.findViewById(R.id.user_info_bar_user_name_tv)).setText(userAchievements.getNick());
        ((ImageView)header.findViewById(R.id.user_info_bar_user_iv)).setImageResource(User.convertUserIconIdToDrawableID(userAchievements.getAvatarId()));

        //calculate achievements
        float totalTotalQuestions = userAchievements.getAnsweredQuestions().size();
        float totalCorrectAnswers = 0;

        for(AnsweredQuestion aQ : userAchievements.getAnsweredQuestions()){
            if(aQ.isCorrect()){
                totalCorrectAnswers++;
            }
        }

        if(totalTotalQuestions > 0){
            long accuracy = (long)(totalCorrectAnswers / (totalTotalQuestions/100));
            ((TextView)header.findViewById(R.id.user_info_bar_user_accuracy)).setText(getString(R.string.user_accuracy, accuracy));

            SharedPreferencesUtils.persistCurrentUserAccuracy(getApplicationContext(), accuracy);
            Intent updateIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            sendBroadcast(updateIntent);
        }else {
            ((TextView)header.findViewById(R.id.user_info_bar_user_accuracy)).setText("");
        }
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        DiskIOExecutor.getInstance().diskIO().execute(() -> TQ_DataBase.getInstance(this).answeredQuestionDao().deleteByUserId(SharedPreferencesUtils.retrieveCurrentUserId(this)));

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    @Override
    public String getDialogMessage() {
        return getString(R.string.should_delete_history);
    }

    @Override
    public String getDialogTitle() {
        return getString(R.string.sure);
    }
}
