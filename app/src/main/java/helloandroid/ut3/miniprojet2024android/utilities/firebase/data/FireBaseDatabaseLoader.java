package helloandroid.ut3.miniprojet2024android.utilities.firebase.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.model.Restaurant;

public class FireBaseDatabaseLoader {
    public static CompletableFuture<List<Restaurant>> loadData(String path) {
        CompletableFuture<List<Restaurant>> future = new CompletableFuture<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child(path);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Restaurant> restaurants = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String restaurantId = snapshot.getKey();
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    if (restaurant != null) {
                        restaurant.setId(restaurantId);

                        List<Avis> reviews = new ArrayList<>();
                        for (DataSnapshot reviewSnapshot : snapshot.child("reviews").getChildren()) {
                            Avis review = reviewSnapshot.getValue(Avis.class);
                            if (review != null) {
                                reviews.add(review);
                            }
                        }
                        restaurant.setReviews(reviews);
                        restaurants.add(restaurant);
                    }
                }
                future.complete(restaurants);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    public static CompletableFuture<LatLng> loadGPSForRestaurant(String path) {
        return null;
    }
}