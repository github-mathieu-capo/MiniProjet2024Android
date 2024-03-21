package helloandroid.ut3.miniprojet2024android.model;

import android.widget.ImageView;

public class AvisPhoto {

    private ImageView picture;

    public AvisPhoto(ImageView picture) {
        this.picture = picture;
        this.isSet = false;
        this.imagePath = "";
    }

    private Boolean isSet;
    private String imagePath;

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }

    public Boolean isSet() {
        return isSet;
    }

    public void setSet(Boolean set) {
        isSet = set;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }



}
