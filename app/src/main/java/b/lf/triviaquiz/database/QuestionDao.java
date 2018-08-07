package b.lf.triviaquiz.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import b.lf.triviaquiz.model.Question;

@Dao
public interface QuestionDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(Question question);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(Question... questions);

    @Query("SELECT * FROM question WHERE category IN (:categories)")
    LiveData<List<Question>> getQuestionsByCategories(List<String> categories);
}
