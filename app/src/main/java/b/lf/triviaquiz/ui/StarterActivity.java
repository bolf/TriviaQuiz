package b.lf.triviaquiz.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class StarterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private StarterActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

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
        intent.putExtra("ButtonPreviousVisibility", View.VISIBLE);
        startActivity(intent);
    }

    public void addUser(View view) {
        Intent intent = new Intent(StarterActivity.this, UserSetupActivity.class);
        intent.putExtra(getString(R.string.newUserBooleanIntentExtra), true);
        startActivity(intent);
        finish();
    }
}