package jonas.jacobsson.midgardensvardshus.matsedeln.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;

import static jonas.jacobsson.midgardensvardshus.matsedeln.activities.MainActivity.MAP_LOCATION;
import static jonas.jacobsson.midgardensvardshus.matsedeln.activities.MainActivity.MAP_ZOOM;

/**
 * Created by Jonas on 2016-10-04.
 */
public class MapTabActivity extends FragmentActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapView mapView = (MapView) findViewById(R.id.map_view);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MAP_LOCATION, MAP_ZOOM));
            }
        });
    }
}
