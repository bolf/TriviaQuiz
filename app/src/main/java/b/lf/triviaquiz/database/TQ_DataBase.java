package b.lf.triviaquiz.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import b.lf.triviaquiz.model.AnsweredQuestion;
import b.lf.triviaquiz.model.Question;
import b.lf.triviaquiz.model.QuestionCategory;
import b.lf.triviaquiz.model.User;

@Database(entities = {User.class, QuestionCategory.class, Question.class, AnsweredQuestion.class},version = 1, exportSchema = false)
public abstract class TQ_DataBase extends RoomDatabase{

    private static final String DB_NAME = "trivia_quiz";
    private static final Object LOCK = new Object();
    private static TQ_DataBase sInstance;

    public static TQ_DataBase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance= Room.databaseBuilder(context, TQ_DataBase.class, TQ_DataBase.DB_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract UserDao userDao();

    public abstract CategoryDao categoryDao();

    public abstract QuestionDao questionDao();

    public abstract AnsweredQuestionDao answeredQuestionDao();
}
