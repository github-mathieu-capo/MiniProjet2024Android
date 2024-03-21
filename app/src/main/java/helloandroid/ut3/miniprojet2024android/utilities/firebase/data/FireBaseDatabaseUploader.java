package helloandroid.ut3.miniprojet2024android.utilities.firebase.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseDatabaseUploader {

    public static void uploadData(Object data, String path) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ref.setValue(data);
    }

}
