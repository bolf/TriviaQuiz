package b.lf.triviaquiz.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import b.lf.triviaquiz.model.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user ORDER BY id")
    List<User> getAllUsers();

    @Insert
    void insertUser(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User user);
}
