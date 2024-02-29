package helloandroid.ut3.miniprojet2024android.model;

public class Avis {

    private String author;
    private int note;
    private String description;

    public Avis(String author, int note, String description) {
        this.author = author;
        this.note = note;
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
