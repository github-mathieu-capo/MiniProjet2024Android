package helloandroid.ut3.miniprojet2024android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.utilities.FireBaseDatabaseLoader;
import helloandroid.ut3.miniprojet2024android.utilities.FireBaseDatabaseUploader;
import helloandroid.ut3.miniprojet2024android.utilities.FireBaseImageLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        generateSampleData().thenAccept(restaurantList -> {
            RecyclerView recyclerView = findViewById(R.id.recyclerViewRestaurants);
            RestaurantAdapter adapter = new RestaurantAdapter(restaurantList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }).exceptionally(throwable -> {
            Log.e("MainActivity", "Error loading restaurants Data", throwable);
            return null;
        });

        findViewById(R.id.buttonOpenCamera).setOnClickListener(v -> openCameraActivity());
    }

    private CompletableFuture<List<Restaurant>> generateSampleData() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("restaurants");

        return FireBaseImageLoader.listAllImagesFromFolder(storageRef)
                .thenApply(imageUrls -> {
                    List<Restaurant> restaurants = new ArrayList<>();
                    for(int i = 0; i < imageUrls.size(); i++) {
                        restaurants.add(new Restaurant("Restaurant " + i, imageUrls.get(i), "Description of Restaurant " + i));
                    }
                    return restaurants;
                })
                .exceptionally(throwable -> {
                    Log.e("generateSampleData", "Error generating sample data", throwable);
                    return Collections.emptyList();
                });
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

    private void getRestaurantsFromBdd(){
        String path = "restaurants";
        FireBaseDatabaseLoader.loadData(path)
                .thenAccept(data -> {
                    data.getChildren().forEach(dataSnapshot -> Log.i("Data retrieved: ", String.valueOf(dataSnapshot)));
                })
                .exceptionally(e -> {
                    Log.e("Error occurred while retrieving data: ", e.getMessage());
                    return null;
                });
    }

}