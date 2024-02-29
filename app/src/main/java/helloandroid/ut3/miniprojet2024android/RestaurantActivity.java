package helloandroid.ut3.miniprojet2024android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

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
                    // TODO print confirmation message
                    makeReservation(reservationDate, numberOfPeople);
                }
            });
        }
    }

    private void makeReservation(String date, int numberOfPeople) {
        // TODO verify data and send it to firebase.
    }
}
