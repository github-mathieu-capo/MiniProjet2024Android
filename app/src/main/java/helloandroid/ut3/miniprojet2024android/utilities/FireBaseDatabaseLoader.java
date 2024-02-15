package helloandroid.ut3.miniprojet2024android.utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseDatabaseLoader {


    public static void loadData(String path) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase database", "Error getting data", task.getException());
                }
                else {
                    String value =String.valueOf(task.getResult().getValue());
                    Log.d("firebase database", "Data retrieved with success");
                }
            }
        });
    }

}
