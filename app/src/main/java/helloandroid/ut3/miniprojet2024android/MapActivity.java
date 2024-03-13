package helloandroid.ut3.miniprojet2024android;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialiser le géocodeur
        geocoder = new Geocoder(this);

        // Récupérer la carte depuis le fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Convertir l'adresse en coordonnées GPS
        LatLng restaurantLocation = getLocationFromAddress("103 rue bonnat, Toulouse, France");

        if (restaurantLocation != null) {
            // Placer un marqueur sur la carte pour le restaurant
            mMap.addMarker(new MarkerOptions().position(restaurantLocation).title("Nom du Restaurant"));

            // Centrer la caméra sur la position du restaurant
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocation, 16f));
        } else {
            Toast.makeText(this, "Adresse invalide", Toast.LENGTH_SHORT).show();
        }
    }

    private LatLng getLocationFromAddress(String strAddress) {
        try {
            List<Address> addressList = geocoder.getFromLocationName(strAddress, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}