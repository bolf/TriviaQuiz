package b.lf.triviaquiz.model;

import android.arch.persistence.room.Relation;

import java.util.List;

public class UserAchievements {
    private long id;
    private String nick;
    private int avatarId;
    @Relation(parentColumn = "id", entityColumn = "userId")
    private List<AnsweredQuestion> answeredQuestions;

    public UserAchievements(long id, String nick, int avatarId) {
        this.id = id;
        this.nick = nick;
        this.avatarId = avatarId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<AnsweredQuestion> getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(List<AnsweredQuestion> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }
}
