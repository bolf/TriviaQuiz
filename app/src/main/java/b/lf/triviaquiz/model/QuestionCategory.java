package b.lf.triviaquiz.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

import b.lf.triviaquiz.R;

@Entity(tableName = "category")
public class QuestionCategory{
    @PrimaryKey
    private int id;
    private String name;
    private int iconId;
    @Ignore
    private transient boolean isChosen;

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionCategory category = (QuestionCategory) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public int getIconId() {
        return iconId;
    }

    public int getId() {
        return id;
    }

    public QuestionCategory(){}

    @Ignore
    public QuestionCategory(int id, String name) {
        this.id = id;
        this.name = name;
        iconId = getIconById(id);
    }

    public static int getIconById(int id){
        if(id == 9) return R.drawable._general_knowledge;
        if(id == 10) return R.drawable._books;
        if(id == 11) return R.drawable._film;
        if(id == 12) return R.drawable._music;
        if(id == 13) return R.drawable._theatre;
        if(id == 14) return R.drawable._television;
        if(id == 15) return R.drawable._video_game;
        if(id == 16) return R.drawable._board_game;
        if(id == 17) return R.drawable._nat_science;
        if(id == 18) return R.drawable._computer;
        if(id == 19) return R.drawable._math;
        if(id == 20) return R.drawable._mythology;
        if(id == 21) return R.drawable._sports;
        if(id == 22) return R.drawable._geography;
        if(id == 23) return R.drawable._history;
        if(id == 24) return R.drawable._politics;
        if(id == 25) return R.drawable._art;
        if(id == 26) return R.drawable._celebrities;
        if(id == 27) return R.drawable._animals;
        if(id == 28) return R.drawable._vehicles;
        if(id == 29) return R.drawable._comics;
        if(id == 30) return R.drawable._gadgets;
        if(id == 31) return R.drawable._anime;
        if(id == 32) return R.drawable._cartoon;
        return R.drawable.ic_four_o_four; //if match has not been found
    }
}
