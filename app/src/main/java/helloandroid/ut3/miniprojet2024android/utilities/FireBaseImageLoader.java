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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

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

    public static CompletableFuture<ArrayList<String>> listAllImagesFromFolder(StorageReference storageRef) {
        CompletableFuture<ArrayList<String>> future = new CompletableFuture<>();

        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    ArrayList<Task<Uri>> downloadTasks = new ArrayList<>();
                    for (StorageReference item : listResult.getItems()) {
                        downloadTasks.add(item.getDownloadUrl());
                    }
                    Tasks.whenAllComplete(downloadTasks)
                            .addOnCompleteListener(task -> {
                                ArrayList<String> imageUrls = new ArrayList<>();
                                for (Task<android.net.Uri> downloadTask : downloadTasks) {
                                    if (downloadTask.isSuccessful()) {
                                        android.net.Uri uri = downloadTask.getResult();
                                        String imageUrl = uri.toString();
                                        imageUrls.add(imageUrl);
                                    } else {
                                        Log.e(TAG, "Failed to get download URL for image", downloadTask.getException());
                                    }
                                }
                                future.complete(imageUrls);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to retrieve images for restaurants", e);
                    future.completeExceptionally(e);
                });

        return future;
    }
}
