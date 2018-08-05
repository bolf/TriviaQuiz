package b.lf.triviaquiz.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import b.lf.triviaquiz.model.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user ORDER BY id")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user WHERE id = :id")
    LiveData<User> getUserById(long id);

    @Insert
    void insertUser(User user);
}
