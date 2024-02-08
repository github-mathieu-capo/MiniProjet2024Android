package helloandroid.ut3.miniprojet2024android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("restaurants");
        ArrayList<String> imageUrls = FireBaseImageLoader.listAllImagesFromFolder(storageRef);

        List<Restaurant> restaurants = new ArrayList<>();
        Log.i("TESTTTTT", imageUrls.size()+"zeub");
        for(int i = 0; i < imageUrls.size(); i++){
            restaurants.add(new Restaurant("Restaurant"+i, imageUrls.get(i),"Description of Restaurant "+i));
            Log.i("add resto", i+"");
        }
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