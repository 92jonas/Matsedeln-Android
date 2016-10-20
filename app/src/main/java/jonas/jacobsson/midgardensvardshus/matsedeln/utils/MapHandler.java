package jonas.jacobsson.midgardensvardshus.matsedeln.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import jonas.jacobsson.midgardensvardshus.matsedeln.R;

/**
 * Created by jonas on 2016-09-29.
 */

public class MapHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = MapHandler.class.getSimpleName();

    private Context context;
    private GoogleApiClient locationClient;
    private LocationRequest locationRequest, locationRequestAlarm, locationRequestStandby;
    private MapFragment mapFragment;
    private GoogleMap map;
    private Location oldLocation;
    private boolean firstLocation = true;
    private boolean isMarkerInFocus = false;
    private boolean myLocInFrame;
    private boolean rapidLocationUpdate;
    private ImageView rippleImg;
    private Handler handler;
    private float slowZoomOutValue;
    private boolean stopSlowZoomOut;

    public MapHandler(final MapFragment mapFragment, Context context) {
        this.context = context;
        this.mapFragment = mapFragment;
        this.handler = new Handler();
        initMap();
    }

    private void initLocation() {

        LatLng midgardenLatLng = new LatLng(56.252907, 12.892882);
        map.addMarker(new MarkerOptions().position(midgardenLatLng).title(context.getResources().getString(R.string.map_find_us)));
        moveToLocation(midgardenLatLng, 16f, true);

//        map.setMyLocationEnabled(true);
        // Handles location updating using GPS, network and passive automatically
        locationClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Frequency of sending location requests
        locationRequestAlarm = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        locationRequestStandby = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(20000)        // 20 seconds, in milliseconds
                .setFastestInterval(5000); // 5 second, in milliseconds

        locationRequest = locationRequestStandby;
    }

    private void initMap() {
        // Setting up map
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                initLocation();
            }
        });
    }

    public void startRequestingLocation() {
        locationClient.connect();
    }

    public void stopRequestingLocation() {
        if (locationClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(locationClient, this);
            locationClient.disconnect();
        }
    }

    public void cameraOnMe(boolean animate, float zoom) {
        moveToLocation(new LatLng(oldLocation.getLatitude(), oldLocation.getLongitude()), zoom, animate);
    }

    private void moveToLocation(LatLng pos, float zoom, boolean animate) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(pos)
                .zoom(zoom)
                .build();
        CameraUpdate cameraPos = CameraUpdateFactory.newCameraPosition(cameraPosition);
        if (animate) {
            map.animateCamera(cameraPos);
        } else {
            map.moveCamera(cameraPos);
        }
    }


    public void clearContactsFromMap() {
        map.clear();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "GPS - Location services connected.");

        Location location = LocationServices.FusedLocationApi.getLastLocation(locationClient);
        handleNewLocation(location);
        LocationServices.FusedLocationApi.requestLocationUpdates(locationClient, locationRequest, this);
    }

    private void handleNewLocation(Location location) {
        if (location != null) {
            Log.i(TAG, "GPS - onLocationChanged, location= " + location.toString());
            oldLocation = location;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

}
