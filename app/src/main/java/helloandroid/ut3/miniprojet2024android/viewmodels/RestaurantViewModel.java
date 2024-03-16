package helloandroid.ut3.miniprojet2024android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.repositories.RestaurantRepository;

public class RestaurantViewModel extends ViewModel {
    private MutableLiveData<List<Restaurant>> restaurantsLiveData;
    private RestaurantRepository restaurantRepository;

    public RestaurantViewModel() {
        restaurantsLiveData = new MutableLiveData<>();
        restaurantRepository = new RestaurantRepository();
        loadRestaurants();
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        return restaurantsLiveData;
    }

    private void loadRestaurants() {
        restaurantRepository.getRestaurants(new RestaurantRepository.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Restaurant> restaurants) {
                restaurantsLiveData.setValue(restaurants);
            }
        });
    }

}
