package helloandroid.ut3.miniprojet2024android;

import static helloandroid.ut3.miniprojet2024android.Camera.REQUEST_IMAGE_CAPTURE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.FirebaseApp;
import java.io.File;

import java.io.File;

import javax.annotation.Nullable;
import java.util.ArrayList;


import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.repositories.RestaurantRepository;
import helloandroid.ut3.miniprojet2024android.viewmodels.RestaurantDetailViewModel;

public class AddAvisActivity extends AppCompatActivity {

    private RestaurantDetailViewModel viewModel;
    private ImageView star1, star2, star3, star4, star5;
    private int rating = 0;

    private EditText authorEditText;
    private TextView descriptionEditText;
    private ImageView picture;
    private boolean isImageSet = false;
    private static final int REQUEST_EDIT_IMAGE = 1;
    private String imagePath;

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

        Intent intent = getIntent();
        if (intent != null) {
            String restaurantId = getIntent().getStringExtra("RestaurantId");
            viewModel = new ViewModelProvider(this, new RestaurantDetailViewModel(restaurantId)).get(RestaurantDetailViewModel.class);
        }

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
                if (isImageSet) {
                    showOptionsDialog();
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });

        picture2 = findViewById(R.id.picture);

    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                .setItems(new CharSequence[]{"Modifier", "Supprimer", "Annuler"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Modifier
                                openEditImageActivity();
                                break;
                            case 1:
                                // Supprimer
                                clearImage();
                                break;
                            case 2:
                                // Annuler
                                dialog.dismiss();
                                break;
                        }
                    }
                });
        builder.show();
    }

    private void clearImage() {
        picture.setImageResource(R.drawable.ic_add_image);
        isImageSet = false;
    }

    private void openEditImageActivity() {
        Intent intent = new Intent(this, EditImageActivity.class);
        intent.putExtra("imagePath", imagePath);
        startActivityForResult(intent, REQUEST_EDIT_IMAGE);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(this, Camera.class);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( (requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_EDIT_IMAGE) && resultCode == RESULT_OK && data != null) {
            // L'image a été capturée avec succès, obtenir le chemin de l'image
            imagePath = data.getStringExtra("imagePath");
            // Charger l'image à partir du chemin du fichier et l'afficher dans l'ImageView
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                picture.setImageBitmap(bitmap);
                isImageSet = true;

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
            String author = authorEditText.getText().toString();
            int grade = rating;
            ArrayList<String> photos = new ArrayList<>();
            String description = descriptionEditText.getText().toString();

            Avis avis = new Avis(author, grade, photos, description);
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