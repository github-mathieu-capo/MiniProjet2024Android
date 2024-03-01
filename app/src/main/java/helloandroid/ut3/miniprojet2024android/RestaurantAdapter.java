package helloandroid.ut3.miniprojet2024android;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import helloandroid.ut3.miniprojet2024android.model.Restaurant;
import helloandroid.ut3.miniprojet2024android.utilities.FireBaseImageLoader;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<Restaurant> restaurantList;
    private Context context;

    public RestaurantAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurants, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        String pathToFile = "restaurants/"+restaurant.getImageUrl();
        FireBaseImageLoader.loadImageFromStorageReference(context,pathToFile,holder.imageView);
        Glide.with(context)
                .load(restaurant.getImageUrl())
                .into(holder.imageView);
        holder.textViewName.setText(restaurant.getName());
        holder.textViewDescription.setText(restaurant.getDescription());
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.restaurantImage);
            textViewName = itemView.findViewById(R.id.restaurantName);
            textViewDescription = itemView.findViewById(R.id.restaurantDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Restaurant selectedRestaurant = restaurantList.get(position);

                        Intent intent = new Intent(context, RestaurantActivity.class);
                        intent.putExtra("restaurantInfos", selectedRestaurant);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}

