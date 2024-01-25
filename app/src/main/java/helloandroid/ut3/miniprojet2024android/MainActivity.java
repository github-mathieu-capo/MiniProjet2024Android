package helloandroid.ut3.miniprojet2024android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        // Firebase Storage references
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("uploaded_images/zebanane.jpg");
        ImageView imageView = findViewById(R.id.imageView);

        loadImageFromStorageReference(imageRef, imageView);

        // camera
        findViewById(R.id.buttonOpenCamera).setOnClickListener(v -> openCameraActivity());

    }
    private void loadImageFromStorageReference(StorageReference storageReference, ImageView imageView) {
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadUrl = uri.toString();

                Glide.with(getApplicationContext())
                        .load(downloadUrl)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("TAG", "Cannot retrieve Image", exception);
            }
        });
    }

    private void openCameraActivity() {
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
    }

}