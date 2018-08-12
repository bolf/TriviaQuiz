package b.lf.triviaquiz.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.StarterActivityViewModel;

public class StarterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,NavigationView.OnNavigationItemSelectedListener {
    private StarterActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mViewModel = ViewModelProviders.of(this).get(StarterActivityViewModel.class);
        mViewModel.getAllUsers().observe(this, users -> {
            processUsersListFromDb(users);
        });
    }

    private void processUsersListFromDb(List<User> users) {
        if(users.size() > 0){ //we have previously set user
            long currentUserId = SharedPreferencesUtils.retrieveCurrentUserId(StarterActivity.this);
            for (User u : users) {
                if (u.getId() == currentUserId) {
                    mViewModel.setUser(u);
                    break;
                }
            }
            processCurrentUserChange();
            processExistingUsersList(users);

        }else{ //app is used for the 1-t time
            Intent intent = new Intent(StarterActivity.this, UserSetupActivity.class);
            intent.putExtra(getString(R.string.newUserBooleanIntentExtra), true);
            startActivity(intent);
            finish();
        }
    }

    private void processExistingUsersList(List<User> users) {
        if(users.size() > 1){
            Spinner spinner = findViewById(R.id.starter_spinner_users);
            ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, users);
            adapter.setDropDownViewResource(R.layout.spiner_item_textview);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            spinner.setSelection(users.indexOf(mViewModel.getUser()));
        }else{
            findViewById(R.id.starter_spinner_header).setVisibility(View.GONE);
            findViewById(R.id.starter_spinner_users).setVisibility(View.GONE);
        }
    }

    private void processCurrentUserChange(){
        ImageView curUserIV = findViewById(R.id.starter_iv_current_user);
        curUserIV.setImageResource(mViewModel.getUser().getDrawableID());
        ((TextView)findViewById(R.id.starter_current_user_nick)).setText(mViewModel.getUser().getNick());



        NavigationView navigationView = findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        ((TextView)header.findViewById(R.id.user_info_bar_user_name_tv)).setText("жопа!");
//        email = (TextView)header.findViewById(R.id.email);
//        name.setText(personName);
//        email.setText(personEmail);


//        ImageView curUserNav_IV = findViewById(R.id.user_info_bar_user_iv);
//        if (curUserNav_IV == null) return;
//        curUserNav_IV.setImageResource(mViewModel.getUser().getDrawableID());
//        ((TextView)findViewById(R.id.user_info_bar_user_name_tv)).setText(mViewModel.getUser().getNick());
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mViewModel.setUser(mViewModel.getAllUsers().getValue().get(position));
        processCurrentUserChange();
        SharedPreferencesUtils.persistCurrentUserId(this, mViewModel.getAllUsers().getValue().get(position).getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //processCurrentUserChange();
    }

    public void goToCategoryChoosing(View view) {
        Intent intent = new Intent(this, ChoosingQuestionsCategoriesActivity.class);
        intent.putExtra(getString(R.string.ButtonPreviousVisibility), View.VISIBLE);
        startActivity(intent);
    }

    public void addUser(View view) {
        Intent intent = new Intent(StarterActivity.this, UserSetupActivity.class);
        intent.putExtra(getString(R.string.newUserBooleanIntentExtra), true);
        startActivity(intent);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_achievements) {
            startActivity(new Intent(StarterActivity.this,NavActivity.class));
        } else if (id == R.id.nav_restart_current) {

        } else if (id == R.id.nav_reset_total) {
            Snackbar.make(findViewById(R.id.coordinator),"Total scores are reset" , Snackbar.LENGTH_LONG);
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(StarterActivity.this, AboutActivity.class));
        } else if (id == R.id.nav_set_questions_categories) {
            startActivity(new Intent(StarterActivity.this,ChoosingQuestionsCategoriesActivity.class));
        } else if (id == R.id.nav_set_questions_quantity_difficulty) {
            startActivity(new Intent(StarterActivity.this,QuizSetupActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}