package helloandroid.ut3.miniprojet2024android.views;

import static helloandroid.ut3.miniprojet2024android.views.Camera.REQUEST_IMAGE_CAPTURE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import helloandroid.ut3.miniprojet2024android.R;
import helloandroid.ut3.miniprojet2024android.model.Avis;
import helloandroid.ut3.miniprojet2024android.model.AvisPhoto;
import helloandroid.ut3.miniprojet2024android.repositories.RestaurantRepository;
import helloandroid.ut3.miniprojet2024android.utilities.firebase.images.FireBaseImageUploader;
import helloandroid.ut3.miniprojet2024android.viewmodels.RestaurantDetailViewModel;

public class AddAvisActivity extends AppCompatActivity {

    private RestaurantDetailViewModel viewModel;
    private ImageView[] stars = new ImageView[5];
    private int rating = 0;
    private EditText authorEditText;
    private TextView descriptionEditText;
    private AvisPhoto pictureToEdit;
    private List<AvisPhoto> pictures;
    private static final int REQUEST_EDIT_IMAGE = 1;

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


        // Photos of the review
        pictures = new ArrayList<>();
        pictures.add(new AvisPhoto(findViewById(R.id.picture1)));
        pictures.add(new AvisPhoto(findViewById(R.id.picture2)));
        pictures.add(new AvisPhoto(findViewById(R.id.picture3)));
        for (AvisPhoto picture : pictures) {
            picture.getPicture().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (picture.isSet()) {
                        showOptionsDialog(picture);
                    } else {
                        dispatchTakePictureIntent(picture);
                    }
                }
            });
        }

        findViewById(R.id.buttonAjouter).setOnClickListener(v -> ajouterAvis());
        ConstraintLayout parentLayout = findViewById(R.id.addAvisLayout);
        parentLayout.setOnTouchListener((v, event) -> {
            hideKeyboard(AddAvisActivity.this);
            return false;
        });
    }

    private void showOptionsDialog(AvisPhoto picture) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                .setItems(new CharSequence[]{"Modifier", "Supprimer", "Annuler"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Modifier
                                openEditImageActivity(picture);
                                break;
                            case 1:
                                // Supprimer
                                clearImage(picture);
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

    private void clearImage(AvisPhoto picture) {
        picture.getPicture().setImageResource(R.drawable.ic_add_image);
        picture.setSet(false);
    }

    private void openEditImageActivity(AvisPhoto picture) {
        Intent intent = new Intent(this, EditImageActivity.class);
        intent.putExtra("imagePath", picture.getImagePath());
        pictureToEdit = picture;
        startActivityForResult(intent, REQUEST_EDIT_IMAGE);
    }

    private void dispatchTakePictureIntent(AvisPhoto picture) {
        pictureToEdit = picture;
        Intent takePictureIntent = new Intent(this, Camera.class);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( (requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_EDIT_IMAGE) && resultCode == RESULT_OK && data != null) {
            // L'image a été capturée avec succès, obtenir le chemin de l'image
            pictureToEdit.setImagePath(data.getStringExtra("imagePath"));
            // Charger l'image à partir du chemin du fichier et l'afficher dans l'ImageView
            File imgFile = new File(pictureToEdit.getImagePath());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                pictureToEdit.getPicture().setImageBitmap(bitmap);
                pictureToEdit.setSet(true);

            }
        }
    }

    private String cropPath(String toCrop) {
        String[] parts = toCrop.split("/");
        String lastElement = parts[parts.length - 1];
        return lastElement;
    }

    private void ajouterAvis() {
        String author = authorEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        ArrayList<String> picLink = new ArrayList<>();
        for(AvisPhoto pic : pictures) {
            if(!pic.getImagePath().isEmpty()) {
                Log.i("TESTTTTT",cropPath(pic.getImagePath()));
                String name = cropPath(pic.getImagePath());
                picLink.add(name);
                FireBaseImageUploader.uploadImage(BitmapFactory.decodeFile(pic.getImagePath()), name);
                Log.i("TESTTTTT2",picLink.get(0));
            }
        }
        Avis avis = new Avis(author, rating, picLink, description);
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
