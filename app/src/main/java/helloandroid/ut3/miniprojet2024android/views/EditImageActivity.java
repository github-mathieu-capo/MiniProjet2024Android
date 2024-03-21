package helloandroid.ut3.miniprojet2024android.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.Manifest;
import android.widget.RadioGroup;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import helloandroid.ut3.miniprojet2024android.R;

public class EditImageActivity extends AppCompatActivity implements SensorEventListener {

    private ImageView imageView;
    private Bitmap originalBitmap;

    // Paramètres pour l'AudioRecord
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
    private AudioRecord audioRecord;
    private Thread recordingThread;
    private boolean isRecording =false;

    private boolean isCheckingBrightness =false;

    private static final int RECORD_AUDIO_PERMISSION_CODE = 101;

    private RadioGroup couleursGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_image_activity);

        imageView = findViewById(R.id.imageToEdit);

        String imagePath = getIntent().getStringExtra("imagePath");

        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            originalBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        imageView.setImageBitmap(originalBitmap);
        // Vérification des autorisations pour l'enregistrement audio
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_CODE);
        }

        Button colorButton = findViewById(R.id.colorButton);
        couleursGroup = findViewById(R.id.couleursGroup);
        Button brightnessButton = findViewById(R.id.brightnessButton);
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    colorButton.setText("Valider");
                    brightnessButton.setVisibility(View.GONE);
                    couleursGroup.setVisibility(View.VISIBLE);
                    startAudioRecording();
                } else {
                    colorButton.setText("Filtre de couleur");
                    brightnessButton.setVisibility(View.VISIBLE);
                    couleursGroup.setVisibility(View.GONE);
                    stopAudioRecording();
                }
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        brightnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheckingBrightness) {
                    brightnessButton.setText("Valider");
                    colorButton.setVisibility(View.GONE);
                    onResume();
                    isCheckingBrightness = true;

                } else {
                    brightnessButton.setText("Filtre de luminosité");
                    colorButton.setVisibility(View.VISIBLE);
                    updateImage();
                    onPause();
                }
            }
        });

        Button validateButton = findViewById(R.id.acceptChanges);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editEnd(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Button cancelButton = findViewById(R.id.cancelChanges);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editEnd(false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    // Méthode pour démarrer l'enregistrement audio
    private void startAudioRecording() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
        audioRecord.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRecording) {
                    calculateDecibelLevel();
                }
            }
        });
        recordingThread.start();
    }

    // Méthode pour arrêter l'enregistrement audio
    private void stopAudioRecording() {
        isRecording = false;
        updateImage();
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        if (recordingThread != null) {
            recordingThread.interrupt();
            recordingThread = null;
        }
    }

    // Méthode pour calculer le niveau de décibel en temps réel
    private void calculateDecibelLevel() {
        short[] audioBuffer = new short[BUFFER_SIZE / 2];
        int bytesRead = audioRecord.read(audioBuffer, 0, audioBuffer.length);
        if (bytesRead > 0) {
            double sum = 0;
            for (short audioSample : audioBuffer) {
                sum += audioSample * audioSample;
            }
            double rms = Math.sqrt(sum / bytesRead);
            if (rms > 0) {
                double db = 20 * Math.log10(rms);
                Log.d("Decibel", "Decibel Level: " + db);
                applyColorFilter(db);
            } else {
                Log.d("Decibel", "Invalid RMS value: " + rms);
            }
        }
    }

    private void applyColorFilter(double db) {
        ColorMatrix colorMatrix = new ColorMatrix();
        if (db < 45) {
            colorMatrix.setSaturation(0); // 0 pour le noir et blanc
        } else if (db<=75) {
            int axis = -1;
            if (couleursGroup.getCheckedRadioButtonId() == R.id.redRadioButton) {
                axis = 0;
            } else if (couleursGroup.getCheckedRadioButtonId() == R.id.greenRadioButton) {
                axis = 1;
            } else if (couleursGroup.getCheckedRadioButtonId() == R.id.blueRadioButton){
                axis = 2;
            }
            float rotation = (float) (db - 45) * 360 / 30;
            colorMatrix.setRotate(axis, rotation );
        }

            ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

            imageView.setColorFilter(colorFilter);

    }


    // Gestion des autorisations
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudioRecording();
    }


    // LIGHT SENSOR
    private SensorManager sensorManager;
    private Sensor lightSensor;

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        isCheckingBrightness = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isCheckingBrightness && event.sensor.getType() == Sensor.TYPE_LIGHT) {

            float lux = event.values[0];
            Log.d("LUMI1", "LUMI111111111111 : "+ lux);
            lux = (lux - 35) * 255 / 35;
            // Faire quelque chose avec la valeur de luminosité (lux).
            Log.d("LUMI", "LUMI : "+ lux);

            ColorMatrix colorMatrix = new ColorMatrix();

                // Si la valeur aléatoire est supérieure ou égale à 1, ajuster la luminosité
                colorMatrix.set(new float[]{
                        1, 0, 0, 0, lux,
                        0, 1, 0, 0, lux,
                        0, 0, 1, 0, lux,
                        0, 0, 0, 1, 0
                });

                imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Gérer les changements de précision du capteur si nécessaire.
    }

    private void updateImage(){
        ColorFilter existingFilter = imageView.getColorFilter();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        imageView.setColorFilter(null);
        imageView.setImageBitmap(bitmap);
    }

    private void editEnd(boolean acceptChanges) throws IOException {
        if(acceptChanges){
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            // Enregistrer le bitmap dans le fichier
            FileOutputStream outputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();

            Intent intent = new Intent();
            intent.putExtra("imagePath", image.getAbsolutePath());
            setResult(RESULT_OK, intent);
        } else{
            setResult(RESULT_CANCELED);
        }
        finish();
    }

}




