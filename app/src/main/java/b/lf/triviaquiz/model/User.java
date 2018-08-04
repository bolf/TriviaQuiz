package b.lf.triviaquiz.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

import b.lf.triviaquiz.R;

@Entity(tableName = "user")
public class User{
    @PrimaryKey
    private long id;
    private String nick;
    private int avatarId;

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
        return new User("Mr.Fox", 10);
    }

    public User(){}

    @Ignore
    public User(String nick, int avatarId) {
        this.nick = nick;
        this.avatarId = avatarId;
        this.id = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
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
