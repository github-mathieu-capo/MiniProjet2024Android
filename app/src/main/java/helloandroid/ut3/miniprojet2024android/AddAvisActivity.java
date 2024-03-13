package helloandroid.ut3.miniprojet2024android;

import static helloandroid.ut3.miniprojet2024android.Camera.REQUEST_IMAGE_CAPTURE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.model.Restaurant;

public class AddAvisActivity extends AppCompatActivity {

    private ImageView star1, star2, star3, star4, star5;
    private int rating = 0;

    private EditText authorEditText;
    private TextView descriptionEditText;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_avis);
        ConstraintLayout parentLayout = findViewById(R.id.addAvisLayout);

        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(AddAvisActivity.this);
                return false;
            }
        });

        authorEditText = findViewById(R.id.author);
        descriptionEditText = findViewById(R.id.description);
        findViewById(R.id.buttonAjouter).setOnClickListener(v -> ajouterAvis());


        FirebaseApp.initializeApp(this);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        for (int i = 1; i <= 5; i++) {
            int starId = getResources().getIdentifier("star" + i, "id", getPackageName());
            ImageView star = findViewById(starId);
            final int finalI = i;
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStarClick(finalI);
                }
            });
        }
        picture = findViewById(R.id.addPicture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(this, Camera.class);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            // L'image a été capturée avec succès, obtenir le chemin de l'image
            String imagePath = data.getStringExtra("imagePath");
            // Charger l'image à partir du chemin du fichier et l'afficher dans l'ImageView
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                picture.setImageBitmap(bitmap);
            }
        }
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
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
                            resultIntent.putExtra("newReview", avis);
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
        ImageView[] stars = {star1, star2, star3, star4, star5};

        for (ImageView star : stars) {
            star.setImageResource(R.drawable.ic_yellow_star_outline);
        }

        for (int i = 0; i < selectedRating; i++) {
            stars[i].setImageResource(R.drawable.ic_yellow_star_filled);
        }

        rating = selectedRating;
    }

}