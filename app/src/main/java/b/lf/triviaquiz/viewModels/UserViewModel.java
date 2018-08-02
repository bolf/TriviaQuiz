package b.lf.triviaquiz.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.User;

public class UserViewModel extends AndroidViewModel {
    private LiveData<List<User>> usersList;

    public UserViewModel(@NonNull Application application) {
        super(application);
        TQ_DataBase db = TQ_DataBase.getInstance(this.getApplication());
        usersList = db.userDao().getAllUsers();
    }

    public LiveData<List<User>> getAllUsers(){
        return usersList;
    }
}
