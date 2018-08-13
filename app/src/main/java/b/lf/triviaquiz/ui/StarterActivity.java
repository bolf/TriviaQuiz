package b.lf.triviaquiz.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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
import b.lf.triviaquiz.viewModels.TriviaQuizBaseViewModel;

public class StarterActivity extends TriviaQuizBaseActivity implements AdapterView.OnItemSelectedListener{
    private TriviaQuizBaseViewModel mViewModel;

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

        mViewModel = ViewModelProviders.of(this).get(TriviaQuizBaseViewModel.class);
        mViewModel.getAllUsers().observe(this, users -> {
            processUsersListFromDb(users);
        });
    }

    private void processUsersListFromDb(List<User> users) {
        if(users.size() > 0){ //we have previously set user
            long currentUserId = SharedPreferencesUtils.retrieveCurrentUserId(StarterActivity.this);
            for (User u : users) {
                if (u.getId() == currentUserId) {
                    MutableLiveData<User> usr = new MutableLiveData<>();
                    usr.setValue(u);
                    mViewModel.setUser(usr);
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
        curUserIV.setImageResource(mViewModel.getUser().getValue().getDrawableID());
        ((TextView)findViewById(R.id.starter_current_user_nick)).setText(mViewModel.getUser().getValue().getNick());

        //setting curr.user data in the navigation view

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        ((TextView)header.findViewById(R.id.user_info_bar_user_name_tv)).setText(mViewModel.getUser().getValue().getNick());
        ((ImageView)header.findViewById(R.id.user_info_bar_user_iv)).setImageResource(mViewModel.getUser().getValue().getDrawableID());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((MutableLiveData<User>)mViewModel.getUser()).setValue(mViewModel.getAllUsers().getValue().get(position));
        processCurrentUserChange();
        SharedPreferencesUtils.persistCurrentUserId(this, mViewModel.getAllUsers().getValue().get(position).getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

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
}