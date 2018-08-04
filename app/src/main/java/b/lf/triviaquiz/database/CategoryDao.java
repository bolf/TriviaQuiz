package b.lf.triviaquiz.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import b.lf.triviaquiz.model.QuestionCategory;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY id")
    LiveData<List<QuestionCategory>> getAllCategories();

    @Insert
    void insertCategory(QuestionCategory questionCategory);
}
