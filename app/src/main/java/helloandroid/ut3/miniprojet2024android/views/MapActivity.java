package helloandroid.ut3.miniprojet2024android.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import helloandroid.ut3.miniprojet2024android.R;
import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.utilities.map.MapManager;
import helloandroid.ut3.miniprojet2024android.viewmodels.RestaurantViewModel;

public class MapActivity extends AppCompatActivity {

    private MapManager mapManager;
    private RestaurantViewModel restaurantManager;
    private String selectedRestaurantId;
    private List<Restaurant> upToDateRestaurants;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        upToDateRestaurants = new ArrayList<>();

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
                upToDateRestaurants = restaurants;
                displayRestaurantsOnMap();
            }
        });
        mapManager.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String restaurantName = marker.getTitle();
                Restaurant currentRestaurant = getRestaurantFromName(restaurantName);
                if (currentRestaurant != null) {
                    displayReviewImagesForRestaurant(currentRestaurant);
                }
                return false;
            }
        });
    }

    private Restaurant getRestaurantFromName(String restaurantName) {
        Restaurant toReturn = null;
        for(Restaurant restaurant : upToDateRestaurants) {
            if(restaurant.getName().equals(restaurantName)) toReturn = restaurant;
        }
        return toReturn;
    }

    private void displayRestaurantsOnMap() {
        if (upToDateRestaurants != null && !upToDateRestaurants.isEmpty()) {
            for (Restaurant restaurant : upToDateRestaurants) {
                mapManager.addMarker(restaurant.getAddress(), restaurant.getName());
                if (restaurant.getId().equals(selectedRestaurantId)) {
                    mapManager.centerMap(restaurant.getAddress(), 16f);
                }
            }
        }
    }

    private void displayReviewImagesForRestaurant(Restaurant restaurant) {
        List<String> imageNames = new ArrayList<>();

        List<Avis> avisList = new ArrayList<>();
        avisList.addAll(restaurant.getReviews());


        if (avisList != null) {
            for (Avis avis : avisList) {
                imageNames.addAll(avis.getPhotos());
            }
        }

        ViewPager viewPager = findViewById(R.id.viewPager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(imageNames);
        viewPager.setAdapter(adapter);
    }

}