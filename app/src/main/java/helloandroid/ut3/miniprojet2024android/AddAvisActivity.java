package helloandroid.ut3.miniprojet2024android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.model.Restaurant;

public class AddAvisActivity extends AppCompatActivity {

    private ImageView star1, star2, star3, star4, star5;
    private int rating = 0;

    private EditText authorEditText;
    private TextView descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_avis);

        authorEditText = findViewById(R.id.author);
        descriptionEditText = findViewById(R.id.description);
        findViewById(R.id.buttonAjouter).setOnClickListener(v -> ajouterAvis());


        FirebaseApp.initializeApp(this);
        findViewById(R.id.buttonOpenCamera).setOnClickListener(v -> openCameraActivity());
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        // Set OnClickListener for each star
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStarClick(1);
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStarClick(2);
            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStarClick(3);
            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStarClick(4);
            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStarClick(5);
            }
        });
    }

    private void ajouterAvis() {
        Intent intent = getIntent();
        if (intent != null) {
            String author = authorEditText.getText().toString();
            int grade = rating;
            ArrayList<String> photos = new ArrayList<>();

            String description = descriptionEditText.getText().toString();

            Avis avis = new Avis(author, grade, photos, description);

            Restaurant restaurant = getIntent().getParcelableExtra("restaurantInfo");

            restaurant.getReviews().add(avis);

            DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(restaurant.getId());
            restaurantRef.setValue(restaurant)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Review added successfully!", Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("restaurantWithNewReview", restaurant);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to add review. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void openCameraActivity() {
        Intent intent = new Intent(this, Camera.class);

        startActivity(intent);
    }

    public void onStarClick(int selectedRating) {
        // Reset all stars to outline
        star1.setImageResource(R.drawable.ic_yellow_star_outline);
        star2.setImageResource(R.drawable.ic_yellow_star_outline);
        star3.setImageResource(R.drawable.ic_yellow_star_outline);
        star4.setImageResource(R.drawable.ic_yellow_star_outline);
        star5.setImageResource(R.drawable.ic_yellow_star_outline);

        // Set selected star and stars before it to filled
        switch (selectedRating) {
            case 1:
                star1.setImageResource(R.drawable.ic_yellow_star_filled);
                break;
            case 2:
                star1.setImageResource(R.drawable.ic_yellow_star_filled);
                star2.setImageResource(R.drawable.ic_yellow_star_filled);
                break;
            case 3:
                star1.setImageResource(R.drawable.ic_yellow_star_filled);
                star2.setImageResource(R.drawable.ic_yellow_star_filled);
                star3.setImageResource(R.drawable.ic_yellow_star_filled);
                break;
            case 4:
                star1.setImageResource(R.drawable.ic_yellow_star_filled);
                star2.setImageResource(R.drawable.ic_yellow_star_filled);
                star3.setImageResource(R.drawable.ic_yellow_star_filled);
                star4.setImageResource(R.drawable.ic_yellow_star_filled);
                break;
            case 5:
                star1.setImageResource(R.drawable.ic_yellow_star_filled);
                star2.setImageResource(R.drawable.ic_yellow_star_filled);
                star3.setImageResource(R.drawable.ic_yellow_star_filled);
                star4.setImageResource(R.drawable.ic_yellow_star_filled);
                star5.setImageResource(R.drawable.ic_yellow_star_filled);
                break;
        }

        // Update rating
        rating = selectedRating;
    }
}