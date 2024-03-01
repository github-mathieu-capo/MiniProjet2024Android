package helloandroid.ut3.miniprojet2024android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class AddAvisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_avis);

        findViewById(R.id.buttonAjouter).setOnClickListener(v -> ajouterAvis());


        FirebaseApp.initializeApp(this);
        findViewById(R.id.buttonOpenCamera).setOnClickListener(v -> openCameraActivity());
    }

    private void ajouterAvis() {
        Intent intent = getIntent();
        if (intent != null) {
            TextView textViewAuthor = findViewById(R.id.author);
            TextView textViewDescription = findViewById(R.id.description);

            RadioGroup noteRadioGroup = findViewById(R.id.note);

            // Récupérez l'ID du RadioButton sélectionné dans le RadioGroup
            int selectedRadioButtonId = noteRadioGroup.getCheckedRadioButtonId();

            // Trouvez le RadioButton sélectionné en utilisant son ID
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

            if (selectedRadioButton != null) {
                // Récupérez la valeur du RadioButton sélectionné
                String selectedNote = selectedRadioButton.getText().toString();

                // Utilisez la valeur sélectionnée comme vous le souhaitez
                System.out.println("LA NOTE EST : " + selectedNote);
            } else {
                // Aucun RadioButton sélectionné
                System.out.println("Aucune note sélectionnée");
            }

            System.out.println("TODO : AFFICHER NOTE");
        }
    }

    private void openCameraActivity() {
        Intent intent = new Intent(this, Camera.class);

        startActivity(intent);
    }
}