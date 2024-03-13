package helloandroid.ut3.miniprojet2024android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.utilities.FireBaseDatabaseLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        Intent intent = new Intent(this, MapActivity.class);

        startActivity(intent);
        generateSampleData(new DataLoadedCallback() {
            @Override
            public void onDataLoaded(List<Restaurant> restaurants) {
                RecyclerView recyclerView = findViewById(R.id.recyclerViewRestaurants);
                RestaurantAdapter adapter = new RestaurantAdapter(restaurants, MainActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                Log.e("MainActivity", "Error loading restaurants Data", e);
            }
        });
    }

    public interface DataLoadedCallback {
        void onDataLoaded(List<Restaurant> restaurants);
        void onError(Exception e);
    }

    private void generateSampleData(DataLoadedCallback callback) {

        String path = "restaurants";
        CompletableFuture<List<Restaurant>> future = FireBaseDatabaseLoader.loadData(path);
        future.thenAccept(restaurants -> {
            System.out.println("Retrieved restaurants:");
            for (Restaurant restaurant : restaurants) {
                System.out.println("Id: " + restaurant.getId());
                System.out.println("Name: " + restaurant.getName());
                System.out.println("Address: " + restaurant.getAddress());
                System.out.println("Image URL: " + restaurant.getImageUrl());
                System.out.println("Description: " + restaurant.getDescription());
                for (Avis review : restaurant.getReviews()) {
                    System.out.println("Review name: " + review.getName());
                    System.out.println("Review grade: " + review.getGrade());
                    System.out.println("Review description: " + review.getDescription());
                }
            }
            callback.onDataLoaded(restaurants);
        }).exceptionally(ex -> {
            callback.onError((Exception) ex);
            return null;
        });
    }

}