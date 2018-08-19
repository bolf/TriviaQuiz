package b.lf.triviaquiz.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.model.UserAchievements;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user ORDER BY id")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user WHERE id =:id")
    LiveData<User> getUserById(long id);

    @Transaction
    @Query("SELECT id, nick, avatarId FROM user WHERE id = :id")
    LiveData<UserAchievements> getUserWithAchievements(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

}
