package b.lf.triviaquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String mNick;
    private int mAvatarId;

    private User(Parcel in) {
        mNick = in.readString();
        mAvatarId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNick);
        dest.writeInt(mAvatarId);
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
