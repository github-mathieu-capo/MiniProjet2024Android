package helloandroid.ut3.miniprojet2024android.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Avis implements Parcelable {

    private String name;
    private int grade;
    private List<String> photos;
    private String description;

    protected Avis(Parcel in) {
        name = in.readString();
        grade = in.readInt();
        photos = in.createStringArrayList();
        description = in.readString();
    }

    public static final Creator<Avis> CREATOR = new Creator<Avis>() {
        @Override
        public Avis createFromParcel(Parcel in) {
            return new Avis(in);
        }

        @Override
        public Avis[] newArray(int size) {
            return new Avis[size];
        }
    };

    public Avis() {
        this.name = "";
        this.grade = -1;
        this.photos = new ArrayList<>();
        this.description = "";
    }

    public Avis(String author, int note, ArrayList<String> photos, String description) {
        this.name = author;
        this.grade = note;
        this.photos = photos;
        this.description = description;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(grade);
        dest.writeStringList(photos);
        dest.writeString(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String author) {
        this.name = author;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int note) {
        this.grade = note;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
