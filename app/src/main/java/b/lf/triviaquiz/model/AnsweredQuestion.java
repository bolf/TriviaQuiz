package b.lf.triviaquiz.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

//class for storing results of quiz passing

@Entity(tableName = "answered_question")
public class AnsweredQuestion extends Question implements Parcelable {
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

    protected AnsweredQuestion(Parcel in) {
        chosenAnswer = in.readString();
        correct = in.readByte() != 0x00;
        current = in.readByte() != 0x00;
        userId = in.readLong();
        question = in.readString();
        difficulty = in.readString();
        category = in.readString();
        correct_answer = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chosenAnswer);
        dest.writeByte((byte) (correct ? 0x01 : 0x00));
        dest.writeByte((byte) (current ? 0x01 : 0x00));
        dest.writeLong(userId);
        dest.writeString(question);
        dest.writeString(difficulty);
        dest.writeString(category);
        dest.writeString(correct_answer);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AnsweredQuestion> CREATOR = new Parcelable.Creator<AnsweredQuestion>() {
        @Override
        public AnsweredQuestion createFromParcel(Parcel in) {
            return new AnsweredQuestion(in);
        }

        @Override
        public AnsweredQuestion[] newArray(int size) {
            return new AnsweredQuestion[size];
        }
    };
}
