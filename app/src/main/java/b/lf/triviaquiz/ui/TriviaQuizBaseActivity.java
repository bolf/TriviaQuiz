package b.lf.triviaquiz.ui;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.viewModels.TriviaQuizBaseViewModel;

public class TriviaQuizBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TriviaQuizBaseViewModel mViewModel;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_achievements) {
            startActivity(new Intent(this,AchievementsActivity.class));
        } else if (id == R.id.nav_restart_current) {

        } else if (id == R.id.nav_reset_total) {
            Snackbar.make(findViewById(R.id.coordinator),"Total scores are reset" , Snackbar.LENGTH_LONG);
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
}
