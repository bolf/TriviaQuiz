package b.lf.triviaquiz.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import b.lf.triviaquiz.model.User;

public class UserSetupActivityViewModel extends AndroidViewModel {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserSetupActivityViewModel(@NonNull Application application) {
        super(application);
    }
}
