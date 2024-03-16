package helloandroid.ut3.miniprojet2024android.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.model.Restaurant;

public class RestaurantRepository {
    private DatabaseReference databaseReference;

    public RestaurantRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("restaurants");
    }

    public void getRestaurants(final OnDataLoadedListener listener) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Restaurant> restaurants = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    restaurants.add(restaurant);
                }
                listener.onDataLoaded(restaurants);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO Handle error
            }
        });
    }

    public void getRestaurantById(String restaurantId, final OnRestaurantLoadedListener listener) {
        DatabaseReference restaurantRef = databaseReference.child(restaurantId);
        restaurantRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                listener.onRestaurantLoaded(restaurant);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO Handle error
            }
        });
    }

    public interface OnRestaurantUpdatedListener {
        void onRestaurantUpdatedSuccessfully();
        void onRestaurantUpdateFailed();
    }

    public void updateRestaurantWithReview(Restaurant restaurant, Avis review, OnRestaurantUpdatedListener listener) {
        restaurant.getReviews().add(review);
        DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(restaurant.getId());
        restaurantRef.setValue(restaurant)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onRestaurantUpdatedSuccessfully();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onRestaurantUpdateFailed();
                    }
                });
    }

    public void updateRestaurant(Restaurant restaurant, OnRestaurantUpdatedListener listener) {
        DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(restaurant.getId());
        restaurantRef.setValue(restaurant)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onRestaurantUpdatedSuccessfully();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onRestaurantUpdateFailed();
                    }
                });
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Restaurant> restaurants);
    }

    public interface OnRestaurantLoadedListener {
        void onRestaurantLoaded(Restaurant restaurant);
    }
}
