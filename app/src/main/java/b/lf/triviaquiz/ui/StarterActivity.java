package b.lf.triviaquiz.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.User;

public class StarterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<User> mUserLst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);


        mUserLst = getAllUsers();
//        String[] users = new String[userLst.size()];
//        for(int i = 0; i < userLst.size(); i++){
//            users[i] = userLst.get(i).getNick();
//        }

        Spinner spinner = findViewById(R.id.starter_spinner_users);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,mUserLst);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spiner_item_textview);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    ArrayList<User> getAllUsers(){
        //implement getting user from DB here
        ArrayList<User> usersL = new ArrayList<>();
        usersL.add(new User("foxy",01));
        usersL.add(new User("foxing",01));
        usersL.add(new User("foxonius",10));
        usersL.add(new User("foxinen",11));
        usersL.add(new User("foxosof",01));

        return usersL;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Snackbar.make(view, mUserLst.get(position).getNick(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
