package helloandroid.ut3.miniprojet2024android.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import helloandroid.ut3.miniprojet2024android.R;
import helloandroid.ut3.miniprojet2024android.utilities.firebase.images.FireBaseImageLoader;

public class ImagePagerAdapter extends PagerAdapter {

    private List<String> imageNames;
    private Context context;
    private FirebaseStorage storage;

    public ImagePagerAdapter(List<String> imageNames) {
        this.imageNames = imageNames;
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public int getCount() {
        return imageNames.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        context = container.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_review_image, container, false);

        final ImageView imageView = view.findViewById(R.id.imageView);
        String pathToImg = "reviews/" + imageNames.get(position);
        FireBaseImageLoader.loadImageFromStorageReference(context.getApplicationContext(),pathToImg, imageView );
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
