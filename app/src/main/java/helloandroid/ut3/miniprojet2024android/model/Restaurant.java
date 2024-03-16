package helloandroid.ut3.miniprojet2024android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Parcelable {

    private String id;
    private String name;
    private String address;
    private String imageUrl;
    private String description;
    private List<Avis> reviews;

    protected Restaurant(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        reviews = in.createTypedArrayList(Avis.CREATOR);
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public Restaurant(String id, String name,String adress, String imageUrl, String description, List<Avis> reviews) {
        this.id = id;
        this.name = name;
        this.address = adress;
        this.imageUrl = imageUrl;
        this.description = description;
        this.reviews = reviews;
    }

    protected Restaurant() {
        id = "";
        name = "";
        address = "";
        imageUrl = "";
        description ="";
        reviews = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public List<Avis> getReviews() {
        return reviews;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeTypedList(reviews);
    }

    public void setReviews(List<Avis> reviews) {
        this.reviews = reviews;
    }
}


