package b.lf.triviaquiz.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.List;

import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.model.UserAchievements;

public class TriviaQuizBaseViewModel extends AndroidViewModel {
    private LiveData<User> user;
    private MutableLiveData<Long> userLiveDataFilter = new MutableLiveData<>();

    private LiveData<List<User>> usersList;

    private LiveData<UserAchievements> userWithAchievements;

    TriviaQuizBaseViewModel(@NonNull Application application) {
        super(application);
        TQ_DataBase db = TQ_DataBase.getInstance(this.getApplication());
        user = Transformations.switchMap(userLiveDataFilter, value -> db.userDao().getUserById(value));
        usersList = db.userDao().getAllUsers();

        userWithAchievements = Transformations.switchMap(userLiveDataFilter, value -> db.userDao().getUserWithAchievements(value));
    }

    public void setUserLiveDataFilter(Long id){
        userLiveDataFilter.setValue(id);
    }

    public LiveData<User> getUser(){
        return user;
    }

    public LiveData<List<User>> getAllUsers(){
        return usersList;
    }

    public void setUser(LiveData<User> user) {
        this.user = user;
    }

    public LiveData<UserAchievements> getUserWithAchievements() {
        return userWithAchievements;
    }
}
