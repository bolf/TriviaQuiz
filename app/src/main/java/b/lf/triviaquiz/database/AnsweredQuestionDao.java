package b.lf.triviaquiz.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import b.lf.triviaquiz.model.AnsweredQuestion;

@Dao
public interface AnsweredQuestionDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAnsweredQuestion(AnsweredQuestion aQuestion);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(AnsweredQuestion... aQuestions);

    @Query("SELECT * FROM answered_question WHERE userId = :userId")
    LiveData<List<AnsweredQuestion>> getAnsweredQuestionsByUserId(long userId);

    @Query("SELECT * FROM answered_question WHERE current = 1 AND userId = :userId")
    List<AnsweredQuestion> getAnsweredQuestionsCurrent(long userId);

    @Query("DELETE FROM answered_question WHERE userId = :userId")
    void deleteByUserId(long userId);
}
