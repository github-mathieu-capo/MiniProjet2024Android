package helloandroid.ut3.miniprojet2024android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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

import java.util.ArrayList;
import java.util.List;

import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.utilities.FireBaseImageLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        List<Restaurant> restaurantList = generateSampleData();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewRestaurants);
        RestaurantAdapter adapter = new RestaurantAdapter(restaurantList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // camera
        findViewById(R.id.buttonOpenCamera).setOnClickListener(v -> openCameraActivity());
    }

    private List<Restaurant> generateSampleData() {
        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(new Restaurant("Restaurant 1", R.drawable.resto1, "Description of Restaurant 1."));
        restaurants.add(new Restaurant("Restaurant 2", R.drawable.resto2, "Description of Restaurant 2."));
        restaurants.add(new Restaurant("Restaurant 3", R.drawable.resto3, "Description of Restaurant 3."));

        return restaurants;
    }

    private void testDownload() {
        // Firebase Storage references
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("bananabetter.jpg");
        //ImageView imageView = findViewById(R.id.imageView);
        //FireBaseImageLoader.loadImageFromStorageReference(getApplicationContext(),imageRef,imageView);
    }


    private void openCameraActivity() {
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
    }

}