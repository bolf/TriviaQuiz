package b.lf.triviaquiz.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "user")
public class User implements Parcelable {
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

    @Ignore
    private User(Parcel in) {
        nick = in.readString();
        avatarId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nick);
        dest.writeInt(avatarId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
