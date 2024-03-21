package helloandroid.ut3.miniprojet2024android.utilities.firebase.images;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class FireBaseImageUploader {
    public static void uploadImage(Bitmap bitmap, String name) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imagesRef = storageRef.child("images/" + name + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Log.d("Camera", "Image uploaded to Firebase Storage");
        }).addOnFailureListener(exception -> {
            Log.e("Camera", "Failed to upload image to Firebase Storage: " + exception.getMessage());
        });
    }
}
