package helloandroid.ut3.miniprojet2024android.utilities;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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

    public static ArrayList<String> listAllImagesFromFolder(StorageReference storageRef) {
        ArrayList<String> imageUrls = new ArrayList<>(); // TODO AWAIT LA FIN DES OPERATIONS

        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                                @Override
                                public void onSuccess(android.net.Uri uri) {
                                    String imageUrl = uri.toString();
                                    imageUrls.add(imageUrl);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Failed to get download URL for image", e);
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to list items in folder", e);
                    }
                });
        return imageUrls;
    }
}
