package helloandroid.ut3.miniprojet2024android.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.repositories.RestaurantRepository;

public class RestaurantDetailViewModel extends ViewModel implements ViewModelProvider.Factory{

    private String restaurantId;
    private MutableLiveData<Restaurant> restaurantLiveData;
    private RestaurantRepository restaurantRepository;

    public RestaurantDetailViewModel(String restaurantId) {
        this.restaurantId = restaurantId;
        restaurantLiveData = new MutableLiveData<>();
        restaurantRepository = new RestaurantRepository();
        loadRestaurant(restaurantId);
    }

    public LiveData<Restaurant> getRestaurant() {
        return restaurantLiveData;
    }

    private void loadRestaurant(String restaurantId) {
        restaurantRepository.getRestaurantById(restaurantId, new RestaurantRepository.OnRestaurantLoadedListener() {
            @Override
            public void onRestaurantLoaded(Restaurant restaurant) {
                restaurantLiveData.setValue(restaurant);
            }
        });
    }
    public void updateRestaurantWithReview(Avis review, RestaurantRepository.OnRestaurantUpdatedListener listener) {
        Restaurant restaurant = restaurantLiveData.getValue();
        if (restaurant != null) {
            restaurantRepository.updateRestaurantWithReview(restaurant, review, new RestaurantRepository.OnRestaurantUpdatedListener() {
                @Override
                public void onRestaurantUpdatedSuccessfully() {
                    listener.onRestaurantUpdatedSuccessfully();
                }

                @Override
                public void onRestaurantUpdateFailed() {
                    listener.onRestaurantUpdateFailed();
                }
            });
        }
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantDetailViewModel.class)) {
            return (T) new RestaurantDetailViewModel(restaurantId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

