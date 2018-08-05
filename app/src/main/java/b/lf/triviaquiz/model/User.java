package b.lf.triviaquiz.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.database.CategoriesListToJsonStringConverter;

@Entity(tableName = "user")
public class User{
    @PrimaryKey
    private long id;
    private String nick;
    private int avatarId;
    @TypeConverters(CategoriesListToJsonStringConverter.class)
    @ColumnInfo(name = "chosen_categories")
    private List<QuestionCategory> chosenQuestionsCategories;
    private int questionsQuantity;
    private String difficulty;

    @Override
    public String toString() {
        return getNick();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<QuestionCategory> getChosenQuestionsCategories() {
        return chosenQuestionsCategories;
    }

    public void setChosenQuestionsCategories(List<QuestionCategory> chosenQuestionsCategories) {
        this.chosenQuestionsCategories = chosenQuestionsCategories;
    }

    public int getQuestionsQuantity() {
        return questionsQuantity;
    }

    public void setQuestionsQuantity(int questionsQuantity) {
        this.questionsQuantity = questionsQuantity;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public static User getDefaultUser(){
        return new User("Mr.Fox", 10, null, 32);
    }

    public User(){}

    @Ignore
    public User(String nick, int avatarId, String difficulty, int questionsQuantity) {
        this.nick = nick;
        this.avatarId = avatarId;
        this.id = System.currentTimeMillis();
        this.difficulty = difficulty;
        this.questionsQuantity = questionsQuantity;
        this.chosenQuestionsCategories = new ArrayList<>();
    }

    public int getDrawableID(){
        int id = -1;
        if(avatarId == 0){
            id = R.drawable.ic_girl0;
        }else if(avatarId == 1){
            id = R.drawable.ic_girl1;
        }else if(avatarId == 10){
            id = R.drawable.ic_man0;
        }else if(avatarId == 11){
            id = R.drawable.ic_man1;
        }
        return id;
    }
}
