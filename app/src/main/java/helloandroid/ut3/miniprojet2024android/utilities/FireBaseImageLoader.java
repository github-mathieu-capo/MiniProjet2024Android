package helloandroid.ut3.miniprojet2024android.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

public class FireBaseImageLoader {

    public static void loadImageFromStorageReference(Context applicationContext, StorageReference imageReference, ImageView imageView) {
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadUrl = uri.toString();

                Glide.with(applicationContext)
                        .load(downloadUrl)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("ImageLoad", "Cannot retrieve Image "+imageReference, exception);
            }
        });
    }
}
