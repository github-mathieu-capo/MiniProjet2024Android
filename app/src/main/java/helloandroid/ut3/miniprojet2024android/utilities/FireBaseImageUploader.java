package helloandroid.ut3.miniprojet2024android.utilities;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FireBaseImageUploader extends Activity {

    private void uploadImageToFirebaseStorage(byte[] capturedImageData) {
        if (capturedImageData != null) {
            // Créer une référence au dossier "images" dans votre Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images");

            // Créer une référence unique pour l'image à télécharger
            String imageName = "image_" + System.currentTimeMillis() + ".jpg";
            StorageReference imageRef = storageRef.child(imageName);

            // Télécharger les données de l'image
            UploadTask uploadTask = imageRef.putBytes(capturedImageData);

            // Écouter les événements de téléchargement
            uploadTask.addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // L'image a été téléchargée avec succès
                    // Obtenez l'URL de téléchargement si nécessaire
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        Log.d(TAG, "Image uploaded successfully. Download URL: " + downloadUrl);
                        // Ajoutez ici le code pour traiter l'URL de téléchargement si nécessaire
                    });
                } else {
                    // Erreur lors du téléchargement de l'image
                    Log.e(TAG, "Error uploading image to Firebase Storage", task.getException());
                }
            });
        } else {
            // Aucune image à télécharger
            Log.e(TAG, "No image data to upload");
        }
    }

}
