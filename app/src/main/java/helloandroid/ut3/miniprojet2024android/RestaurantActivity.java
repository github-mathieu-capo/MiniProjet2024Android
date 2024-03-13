package helloandroid.ut3.miniprojet2024android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.repositories.RestaurantRepository;
import helloandroid.ut3.miniprojet2024android.utilities.FireBaseImageLoader;

public class RestaurantActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            String restaurantId = intent.getStringExtra("RestaurantId");
                            retrieveRestaurantById(restaurantId);
                        }
                    }
                }
            });
    private LinearLayout reservationFormLayout;
    private EditText reservationDateEditText;
    private EditText numberOfPeopleEditText;

    private boolean showReservationForm = false;
    private RestaurantRepository restaurantRepository;
    private Restaurant selectedRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants_detail);
        Intent intent = getIntent();
        if (intent != null) {
            String selectedRestaurantId = intent.getStringExtra("RestaurantId");
            restaurantRepository = new RestaurantRepository();
            retrieveRestaurantById(selectedRestaurantId);


            Button reserveButton = findViewById(R.id.reserveButton);
            reservationFormLayout = findViewById(R.id.reservationFormLayout);
            reservationDateEditText = findViewById(R.id.reservationDate);
            numberOfPeopleEditText = findViewById(R.id.numberOfPeople);
            Button confirmReservationButton = findViewById(R.id.confirmReservationButton);

            reserveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReservationForm = !showReservationForm;
                    if(showReservationForm) {
                        reservationFormLayout.setVisibility(View.VISIBLE);
                    } else {
                        reservationFormLayout.setVisibility(View.GONE);
                    }
                }
            });
            confirmReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String reservationDate = reservationDateEditText.getText().toString();
                    String numberOfPeopleStr = numberOfPeopleEditText.getText().toString();
                    if (numberOfPeopleStr.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter the number of people.", Toast.LENGTH_SHORT).show();
                    } else {
                        int numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());
                        if (validateReservationData(reservationDate, numberOfPeople)) {
                            String confirmationMessage = "Reservation confirmed for " + numberOfPeople + " people on " + reservationDate + ".";
                            Toast.makeText(getApplicationContext(), confirmationMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            findViewById(R.id.buttonAjouter).setOnClickListener(v -> openAddAvisActivity(selectedRestaurant));
        }

    }

    private void retrieveRestaurantById(String restaurantId) {
        restaurantRepository.getRestaurantById(restaurantId, new RestaurantRepository.OnRestaurantLoadedListener() {
            @Override
            public void onRestaurantLoaded(Restaurant restaurant) {
                if (restaurant != null) {
                    selectedRestaurant = restaurant;
                    setRestaurantLayout(restaurant); // TODO bug doubled reviews
                } else {
                    // TODO bug Restaurant not found
                }
            }
        });
    }

    private void openAddAvisActivity(Restaurant selectedRestaurant) {
        Intent mCustomIntent = new Intent(this, AddAvisActivity.class);
        mCustomIntent.putExtra("restaurantInfo", selectedRestaurant);
        mStartForResult.launch(mCustomIntent);
    }


    private void setRestaurantLayout(Restaurant selectedRestaurant) {
        ImageView imageView = findViewById(R.id.restaurantImage);
        TextView textViewName = findViewById(R.id.restaurantName);
        TextView textViewDescription = findViewById(R.id.restaurantDescription);

        String pathToImage = "restaurants/" + selectedRestaurant.getImageUrl();
        FireBaseImageLoader.loadImageFromStorageReference(getApplicationContext(), pathToImage, imageView);
        textViewName.setText(selectedRestaurant.getName());
        textViewDescription.setText(selectedRestaurant.getDescription());

        setReviews(selectedRestaurant.getReviews());
    }
    private void setReviews(List<Avis> reviews) {
        LinearLayout reviewLayout = findViewById(R.id.Reviews);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        for (Avis review : reviews) {
            View reviewItemView = inflater.inflate(R.layout.review_item_layout, reviewLayout, false);
            setupReview(reviewItemView, review);
            reviewLayout.addView(reviewItemView);
        }
    }

    private void setReviews(Avis review) {
        LinearLayout reviewLayout = findViewById(R.id.Reviews);
        View reviewItemView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.review_item_layout, reviewLayout, false);
        setupReview(reviewItemView, review);
        reviewLayout.addView(reviewItemView);
    }

    private void setupReview(View reviewItemView, Avis review) {
        TextView authorTextView = reviewItemView.findViewById(R.id.authorNameTextView);
        authorTextView.setText(review.getName());

        TextView descriptionTextView = reviewItemView.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(review.getDescription());

        LinearLayout starsLayout = reviewItemView.findViewById(R.id.starsLayout);
        int score = review.getGrade();
        addStars(starsLayout, score);

        ImageView photoImageView = reviewItemView.findViewById(R.id.photoImageView);
        if (!review.getPhotos().isEmpty()) {
            photoImageView.setVisibility(View.VISIBLE);
            // TODO: Retrieve the images from the database using loadImageFromStorageReference
        }
    }

    private void addStars(LinearLayout starsLayout, int score) {
        for (int i = 0; i < score; i++) {
            ImageView starImageView = createStarImageView(R.drawable.ic_yellow_star_filled);
            starsLayout.addView(starImageView);
        }

        for (int i = score; i < 5; i++) {
            ImageView starImageView = createStarImageView(R.drawable.ic_yellow_star_outline);
            starsLayout.addView(starImageView);
        }
    }

    private ImageView createStarImageView(int imageResource) {
        ImageView starImageView = new ImageView(getApplicationContext());
        starImageView.setImageResource(imageResource);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 8, 0); // Add margin between stars
        starImageView.setLayoutParams(layoutParams);
        return starImageView;
    }


    private boolean validateReservationData(String reservationDate, int numberOfPeople) {
        // Check if the number of people is within the limit (not more than 12)
        if (numberOfPeople <= 0 || numberOfPeople > 12) {
            Toast.makeText(getApplicationContext(), "Please enter a valid number of people (1-12).", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if the date format is valid and not in the past and not more than a year from now
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date currentDate = new Date();
        try {
            Date selectedDate = sdf.parse(reservationDate);
            if (selectedDate.before(currentDate)) {
                Toast.makeText(getApplicationContext(), "Please select a date in the future.", Toast.LENGTH_SHORT).show();
                return false;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1); // Add one year from now
            Date maxDate = calendar.getTime();
            if (selectedDate.after(maxDate)) {
                Toast.makeText(getApplicationContext(), "Reservation can't be made more than a year from now.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Please enter a valid date (dd/MM/yyyy).", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
