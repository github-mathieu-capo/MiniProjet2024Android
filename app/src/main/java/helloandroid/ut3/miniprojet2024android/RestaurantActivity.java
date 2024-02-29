package helloandroid.ut3.miniprojet2024android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.utilities.FireBaseImageLoader;

public class RestaurantActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants_detail);

        FirebaseApp.initializeApp(this);
        Intent intent = getIntent();
        if (intent != null) {
            Restaurant selectedRestaurant = intent.getParcelableExtra("restaurantInfo");
            ImageView imageView = findViewById(R.id.restaurantImage);
            TextView textViewName = findViewById(R.id.restaurantName);
            TextView textViewDescription = findViewById(R.id.restaurantDescription);

            String pathToImage = "restaurants/"+selectedRestaurant.getImageUrl();
            FireBaseImageLoader.loadImageFromStorageReference(getApplicationContext(),pathToImage,imageView);
            textViewName.setText(selectedRestaurant.getName());
            textViewDescription.setText(selectedRestaurant.getDescription());
        }
    }
}
