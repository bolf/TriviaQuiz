package b.lf.triviaquiz.model;

import b.lf.triviaquiz.R;

public class QuestionCategory {
    int mId;
    String mName;
    int mIcon;
    Boolean mCategoryIsChosen;

    public String getmName() {
        return mName;
    }

    public int getmIcon() {
        return mIcon;
    }

    public Boolean getmCategoryIsChosen() {
        return mCategoryIsChosen;
    }

    public int getmId() {
        return mId;
    }

    public QuestionCategory(int id, String name) {
        this.mId = id;
        this.mName = name;
        mIcon = getIconById(id);
    }

    public static int getIconById(int id){
        if(id == 9) return R.drawable.ic_general_knowledge;
        if(id == 10) return R.drawable.ic_books;
        if(id == 11) return R.drawable.ic_film;
        if(id == 12) return R.drawable.ic_music;
        if(id == 13) return R.drawable.ic_theatre;
        if(id == 14) return R.drawable.ic_television;
        if(id == 15) return R.drawable.ic_video_game;
        if(id == 16) return R.drawable.ic_board_game;
        if(id == 17) return R.drawable.ic_nat_science;
        if(id == 18) return R.drawable.ic_computer;
        if(id == 19) return R.drawable.ic_math;
        //TODO: add icons
        return R.drawable.ic_four_o_four;
    }
}
