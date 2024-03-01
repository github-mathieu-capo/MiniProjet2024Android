package helloandroid.ut3.miniprojet2024android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class AddAvisActivity extends AppCompatActivity {

    private ImageView star1, star2, star3, star4, star5;
    private int rating = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_avis);

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
            TextView textViewAuthor = findViewById(R.id.author);
            TextView textViewDescription = findViewById(R.id.description);


            // Récupérez l'ID du RadioButton sélectionné dans le RadioGroup
            //TODO Update to retrieve the imageViews int selectedRadioButtonId = noteRadioGroup.getCheckedRadioButtonId();

            // Trouvez le RadioButton sélectionné en utilisant son ID
            /*RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

            if (selectedRadioButton != null) {
                // Récupérez la valeur du RadioButton sélectionné
                String selectedNote = selectedRadioButton.getText().toString();

                // Utilisez la valeur sélectionnée comme vous le souhaitez
                System.out.println("LA NOTE EST : " + selectedNote);
            } else {
                // Aucun RadioButton sélectionné
                System.out.println("Aucune note sélectionnée");
            }*/

            System.out.println("TODO : AFFICHER NOTE");
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