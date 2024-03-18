package helloandroid.ut3.miniprojet2024android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import java.util.List;
import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.views.RestaurantAdapter;
import helloandroid.ut3.miniprojet2024android.viewmodels.RestaurantViewModel;

public class MainActivity extends AppCompatActivity {

    private RestaurantViewModel restaurantViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        restaurantViewModel.getRestaurants().observe(this, new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                updateUI(restaurants);
            }
        });
    }

    private void updateUI(List<Restaurant> restaurants) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRestaurants);
        RestaurantAdapter adapter = new RestaurantAdapter(restaurants, MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);
    }
}
