package b.lf.triviaquiz.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

//class for storing results of quiz passing

@Entity(tableName = "answered_question")
public class AnsweredQuestion extends Question {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String chosenAnswer;
    private boolean correct;
    private boolean current;
    private long userId;

    public AnsweredQuestion() {
    }

    @Ignore
    public AnsweredQuestion(String chosenAnswer, boolean correct, boolean current, long userId) {
        this.chosenAnswer = chosenAnswer;
        this.correct = correct;
        this.current = current;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChosenAnswer() {
        return chosenAnswer;
    }

    public void setChosenAnswer(String chosenAnswer) {
        this.chosenAnswer = chosenAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
