package helloandroid.ut3.miniprojet2024android;

import static helloandroid.ut3.miniprojet2024android.views.Camera.REQUEST_IMAGE_CAPTURE;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;

import javax.annotation.Nullable;
import java.util.ArrayList;


import helloandroid.ut3.miniprojet2024android.views.Camera;
import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.repositories.RestaurantRepository;
import helloandroid.ut3.miniprojet2024android.viewmodels.RestaurantDetailViewModel;

public class AddAvisActivity extends AppCompatActivity {

    private RestaurantDetailViewModel viewModel;
    private ImageView[] stars = new ImageView[5];
    private int rating = 0;

    private EditText authorEditText;
    private EditText descriptionEditText;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_avis);

        initializeViews();

        Intent intent = getIntent();
        if (intent != null) {
            String restaurantId = getIntent().getStringExtra("RestaurantId");
            viewModel = new ViewModelProvider(this, new RestaurantDetailViewModel(restaurantId))
                    .get(RestaurantDetailViewModel.class);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeViews() {
        authorEditText = findViewById(R.id.author);
        descriptionEditText = findViewById(R.id.description);

        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);

        for (int i = 0; i < 5; i++) {
            final int ratingValue = i + 1;
            stars[i].setOnClickListener(v -> onStarClick(ratingValue));
        }

        picture = findViewById(R.id.addPicture);
        picture.setOnClickListener(v -> dispatchTakePictureIntent());

        findViewById(R.id.buttonAjouter).setOnClickListener(v -> ajouterAvis());
        ConstraintLayout parentLayout = findViewById(R.id.addAvisLayout);
        parentLayout.setOnTouchListener((v, event) -> {
            hideKeyboard(AddAvisActivity.this);
            return false;
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
            String imagePath = data.getStringExtra("imagePath");
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                picture.setImageBitmap(bitmap);
            }
        }
    }

    private void ajouterAvis() {
        String author = authorEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        Avis avis = new Avis(author, rating, new ArrayList<>(), description);
        viewModel.updateRestaurantWithReview(avis, new RestaurantRepository.OnRestaurantUpdatedListener() {
            @Override
            public void onRestaurantUpdatedSuccessfully() {
                Toast.makeText(getApplicationContext(), "Review added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onRestaurantUpdateFailed() {
                Toast.makeText(getApplicationContext(), "Failed to add review. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onStarClick(int selectedRating) {
        for (int i = 0; i < 5; i++) {
            if (i < selectedRating) {
                stars[i].setImageResource(R.drawable.ic_yellow_star_filled);
            } else {
                stars[i].setImageResource(R.drawable.ic_yellow_star_outline);
            }
        }
        rating = selectedRating;
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
