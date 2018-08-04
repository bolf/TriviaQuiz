package b.lf.triviaquiz.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.Session;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.UserViewModel;

public class StarterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Session mSession;
    private List<User> mUserLst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        mSession = SharedPreferencesUtils.retrieveSession(this);

        if (mSession == null){ //there is no previously saved session, let us choose user & open new session
            Intent intent = new Intent(StarterActivity.this, UserSetupActivity.class);
            intent.putExtra("newSession", true);
            startActivity(intent);
            finish();
        }

        setupViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSession = SharedPreferencesUtils.retrieveSession(this);

        processCurrentUserChange();
    }

    private void processCurrentUserChange(){
        ImageView curUserIV = findViewById(R.id.starter_iv_current_user);
        curUserIV.setImageResource(mSession.getUser().getDrawableID());
        ((TextView)findViewById(R.id.starter_current_user_nick)).setText(mSession.getUser().getNick());
    }

    private void setupViewModel() {
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if(users.size() == 0){ //there are no users previously saved
                    Intent intent = new Intent(StarterActivity.this, UserSetupActivity.class);
                    startActivity(intent);
                }else{
                    processExistingUsersList(users);
                }
            }
        });
    }

    private void processExistingUsersList(List<User> users) {
        mUserLst = users;
        if(users.size() > 1){
            Spinner spinner = findViewById(R.id.starter_spinner_users);
            ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mUserLst);
            adapter.setDropDownViewResource(R.layout.spiner_item_textview);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            spinner.setSelection(users.indexOf(mSession.getUser()));
        }else{
            findViewById(R.id.starter_spinner_header).setVisibility(View.GONE);
            findViewById(R.id.starter_spinner_users).setVisibility(View.GONE);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSession.setUser(mUserLst.get(position));
        processCurrentUserChange();
        SharedPreferencesUtils.persistSession(this, mSession);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        processCurrentUserChange();
    }

    public void goToCategoryChoosing(View view) {
        Intent intent = new Intent(this, ChoosingQuestionsCategoriesActivity.class);
        intent.putExtra("ButtonPreviousVisibility", View.VISIBLE);
        startActivity(intent);
    }

    public void addUser(View view) {
        Intent intent = new Intent(StarterActivity.this, UserSetupActivity.class);
        intent.putExtra("newSession", true);
        startActivity(intent);
        finish();
    }
}
