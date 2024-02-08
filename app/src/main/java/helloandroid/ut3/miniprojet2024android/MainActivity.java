package helloandroid.ut3.miniprojet2024android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

import helloandroid.ut3.miniprojet2024android.model.Restaurant;

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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child("userId").setValue("user2");
    }

}