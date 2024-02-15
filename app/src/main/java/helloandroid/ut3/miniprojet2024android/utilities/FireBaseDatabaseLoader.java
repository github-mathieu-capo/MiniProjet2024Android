package helloandroid.ut3.miniprojet2024android.utilities;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

public class FireBaseDatabaseLoader {
    public static CompletableFuture<String> loadData(String path) {
        CompletableFuture<String> future = new CompletableFuture<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase database", "Error getting data", task.getException());
                    future.completeExceptionally(task.getException());
                } else {
                    String data = String.valueOf(task.getResult().getValue());
                    Log.d("firebase database", "Data retrieved with success: " + data);
                    future.complete(data);
                }
            }
        });
        return future;
    }
}
