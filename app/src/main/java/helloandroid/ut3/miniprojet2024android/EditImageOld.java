package helloandroid.ut3.miniprojet2024android;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;

public class EditImageOld {

//
//    // Créer un bitmap mutable pour éditer
//    editedBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), originalBitmap.getConfig());
//
//    // Créer un canvas à partir du bitmap édité
//    canvas = new Canvas(editedBitmap);
//        canvas.drawBitmap(originalBitmap, 0, 0, null);
//
//    // Afficher l'image éditée dans l'ImageView
//        imageView.setImageBitmap(editedBitmap);
//
//    // Ajouter un listener de toucher pour l'ImageView
//        imageView.setOnTouchListener(new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    // Lorsque l'utilisateur appuie sur l'image, récupérez les coordonnées du toucher
//                    float x = event.getX();
//                    float y = event.getY();
//
//                    // Assurez-vous que le sticker est chargé
//                    if (stickerBitmap != null) {
//                        // Définir l'offset pour ajuster la position du sticker
//                        offsetX = x - stickerBitmap.getWidth() / 2;
//                        offsetY = y - stickerBitmap.getHeight() / 2;
//
//                        // Dessiner le sticker sur le canvas
//                        drawSticker(offsetX, offsetY);
//                    }
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    // Lorsque l'utilisateur déplace le doigt, mettez à jour les coordonnées du sticker
//                    x = event.getX();
//                    y = event.getY();
//
//                    // Dessiner le sticker sur le canvas avec les nouvelles coordonnées
//                    drawSticker(x - stickerBitmap.getWidth() / 2, y - stickerBitmap.getHeight() / 2);
//                    break;
//                case MotionEvent.ACTION_UP:
//                    // Lorsque l'utilisateur relâche le doigt, définissez le sticker sur null
//                    stickerBitmap = null;
//                    break;
//            }
//            return true;
//        }
//    });
}
