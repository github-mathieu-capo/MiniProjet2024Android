package helloandroid.ut3.miniprojet2024android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.utilities.FireBaseImageLoader;

public class RestaurantActivity extends AppCompatActivity {

    private LinearLayout reservationFormLayout;
    private EditText reservationDateEditText;
    private EditText numberOfPeopleEditText;

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

            Button reserveButton = findViewById(R.id.reserveButton);
            reservationFormLayout = findViewById(R.id.reservationFormLayout);
            reservationDateEditText = findViewById(R.id.reservationDate);
            numberOfPeopleEditText = findViewById(R.id.numberOfPeople);
            Button confirmReservationButton = findViewById(R.id.confirmReservationButton);

            reserveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reservationFormLayout.setVisibility(View.VISIBLE);
                }
            });

            confirmReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String reservationDate = reservationDateEditText.getText().toString();
                    int numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());
                    if(validateReservationData(reservationDate, numberOfPeople)) {
                        String confirmationMessage = "Reservation confirmed for " + numberOfPeople + " people on " + reservationDate + ".";
                        Toast.makeText(getApplicationContext(), confirmationMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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
