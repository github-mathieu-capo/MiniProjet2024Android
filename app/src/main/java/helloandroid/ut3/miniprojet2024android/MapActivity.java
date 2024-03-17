package helloandroid.ut3.miniprojet2024android;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.utilities.map.MapManager;
import helloandroid.ut3.miniprojet2024android.viewmodels.RestaurantViewModel;

public class MapActivity extends AppCompatActivity {

    private MapManager mapManager;
    private RestaurantViewModel restaurantManager;
    private String selectedRestaurantId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        if (intent != null) {
            selectedRestaurantId = intent.getStringExtra("RestaurantId");
        }

        mapManager = new MapManager(this);
        mapManager.initializeMap(this);

        restaurantManager = new ViewModelProvider(this).get(RestaurantViewModel.class);
        restaurantManager.getRestaurants().observe(this, new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                displayRestaurantsOnMap(restaurants);
            }
        });
    }

    private void displayRestaurantsOnMap(List<Restaurant> restaurants) {
        if (restaurants != null && !restaurants.isEmpty()) {
            for (Restaurant restaurant : restaurants) {
                mapManager.addMarker(restaurant.getAddress(), restaurant.getName());
                if (restaurant.getId().equals(selectedRestaurantId)) {
                    mapManager.centerMap(restaurant.getAddress(), 16f);
                }
            }
        }
    }
}