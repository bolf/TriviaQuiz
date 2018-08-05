package b.lf.triviaquiz.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.User;

public class QuizSetupViewModel extends AndroidViewModel {
    private LiveData<User> user;
    private MutableLiveData<Long> liveDataFilter = new MutableLiveData<>();
    public QuizSetupViewModel(@NonNull Application application) {
        super(application);

        TQ_DataBase db = TQ_DataBase.getInstance(this.getApplication());
        user = Transformations.switchMap(liveDataFilter, value -> db.userDao().getUserById(value));
    }

    public void setLiveDataFilter(Long id){
        liveDataFilter.setValue(id);
    }

    public LiveData<User> getUser(){
        return user;
    }
}
