package helloandroid.ut3.miniprojet2024android;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Camera extends AppCompatActivity {

    private static final String TAG = "Camera2Example";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private TextureView textureView;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;
    private byte[] capturedImageData;
    private ImageReader imageReader;

    private File imageFile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.texture_view);

        textureView = findViewById(R.id.textureView);

        // Ajouter un écouteur de clic pour le bouton de capture
        Button btnCapture = findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(v -> captureImage());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }

    }

    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            Size previewSize = new Size(640, 480); // Adjust the preview size as needed
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640; // Ajustez la largeur de l'image selon vos besoins
            int height = 480; // Ajustez la hauteur de l'image selon vos besoins
            if (jpegSizes != null && jpegSizes.length > 0) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            imageFile = new File(getExternalFilesDir(null), "image.jpg");
            imageReader.setOnImageAvailableListener(reader -> {
                Image image = null;
                try {
                    image = reader.acquireLatestImage();
//                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//                    byte[] bytes = new byte[buffer.capacity()];
//                    buffer.get(bytes);
                    //save(bytes);
                } /*catch (IOException e) {
                    e.printStackTrace();
                } */finally {
                    if (image != null) {
                        image.close();
                    }
                }
            }, backgroundHandler);
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    createCameraPreviewSession();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    cameraDevice.close();
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    cameraDevice.close();
                    cameraDevice = null;
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(640, 480); // Ajustez la taille du tampon selon vos besoins

            Surface surface = new Surface(texture);
            Surface imageSurface = imageReader.getSurface(); // Ajoutez cette ligne pour obtenir la surface de l'ImageReader

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            captureRequestBuilder.addTarget(imageSurface); // Ajoutez cette ligne pour ajouter la surface de l'ImageReader

            cameraDevice.createCaptureSession(Arrays.asList(surface, imageSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (cameraDevice == null) {
                        return;
                    }
                    cameraCaptureSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(Camera.this, "Failed to create camera session", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }



    private void updatePreview() {
        if (cameraDevice == null) {
            return;
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);

        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();

        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    private void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Appel à la méthode de la classe parente

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void save(byte[] bytes) throws IOException {
        // Stockez les données de l'image dans la variable
        capturedImageData = bytes;
        Log.e(TAG, "TEST2");
        // Ajoutez ici le code pour traiter l'image capturée, par exemple, affichage ou traitement ultérieur.
        uploadImageToFirebaseStorage();
    }

    private void captureImage() {
        if (cameraDevice == null) {
            Log.e(TAG, "Camera not initialized");
            return;
        }

        try {
            // Créer une CaptureRequest.Builder pour une capture d'image
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());

            // Configurer l'orientation de la capture (ajustez selon vos besoins)
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation());

            // Commencer la capture
            cameraCaptureSession.capture(captureBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    // La capture d'image est terminée
                    Log.d(TAG, "Image captured successfully");

                    imageReader.setOnImageAvailableListener(reader -> {
                        Image image = null;
                        try {
                            image = reader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes);
                            capturedImageData = bytes;
                        }/* catch (IOException e) {
                    e.printStackTrace();
                } */finally {
                            if (image != null) {
                                image.close();
                            }
                        }
                    }, backgroundHandler);


                    uploadImageToFirebaseStorage();
                }
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir l'orientation correcte
    private int getOrientation() {
        // Ajoutez ici la logique pour obtenir l'orientation correcte
        // Vous pouvez utiliser les capteurs ou la configuration de l'appareil pour déterminer l'orientation
        // Par exemple, vous pouvez utiliser la méthode getWindowManager().getDefaultDisplay().getRotation()
        // pour obtenir la rotation actuelle de l'écran et ajuster l'orientation de la capture en conséquence.
        return 0; // À remplacer par la valeur correcte selon votre logique
    }


    private void uploadImageToFirebaseStorage() {
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

