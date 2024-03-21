package helloandroid.ut3.miniprojet2024android.utilities.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import helloandroid.ut3.miniprojet2024android.R;

public class MapManager implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Context context;

    private GoogleMap.OnMarkerClickListener onMarkerClickListener;
    private GoogleMap.OnMapClickListener onMapClickListener;

    public MapManager(@NonNull Context context) {
        this.context = context;
        geocoder = new Geocoder(context);
    }

    public void initializeMap(FragmentActivity activity) {
        SupportMapFragment mapFragment = (SupportMapFragment) activity
                .getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void addMarker(String address, String title) {
        LatLng location = getLocationFromAddress(address);
        if (location != null && mMap != null) {
            mMap.addMarker(new MarkerOptions().position(location).title(title));
        } else {
            Toast.makeText(context, "Unable to add marker. Invalid address.", Toast.LENGTH_SHORT).show();
        }
    }

    public void centerMap(String address, float zoomLevel) {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocationFromAddress(address), zoomLevel));
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (onMarkerClickListener != null) {
                    return onMarkerClickListener.onMarkerClick(marker);
                }
                return false;
            }
        });
        if (onMapClickListener != null) {
            mMap.setOnMapClickListener(onMapClickListener);
        }
    }

    public void setOnMarkerClickListener(GoogleMap.OnMarkerClickListener onMarkerClickListener) {
        this.onMarkerClickListener = onMarkerClickListener;
    }

    public void setOnMapClickListener(GoogleMap.OnMapClickListener onMapClickListener) {
        this.onMapClickListener = onMapClickListener;
        // If map is ready, set the click listener immediately
        if (mMap != null) {
            mMap.setOnMapClickListener(onMapClickListener);
        }
    }

}
