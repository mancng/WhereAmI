package com.mancng.whereami;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    Location location;
    Double lat;
    Double lng;

    TextView viewLatitude;
    TextView viewLongitude;
    TextView viewAccuracy;
    TextView viewSpeed;
    TextView viewAlt;
    TextView viewBearing;
    TextView viewAddress;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewLatitude = (TextView) findViewById(R.id.viewLatitude);
        viewLongitude = (TextView) findViewById(R.id.viewLongitude);
        viewAccuracy = (TextView) findViewById(R.id.viewAccuracy);
        viewSpeed = (TextView) findViewById(R.id.viewSpeed);
        viewAlt = (TextView) findViewById(R.id.viewAlt);
        viewBearing = (TextView) findViewById(R.id.viewBearing);
        viewAddress = (TextView) findViewById(R.id.viewAddress);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return;
        }

        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {

            onLocationChanged(location);
            lat = location.getLatitude();
            lng = location.getLongitude();

        } else {

            Toast.makeText(getApplicationContext(), "Unable to load location at the moment. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }


    @Override
    protected void onResume() {
        super.onResume();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

//        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Double alt = location.getAltitude();
        Float bearing = location.getBearing();
        Float speed = location.getSpeed();
        Float accuracy = location.getAccuracy();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, lng, 1);

            if(listAddresses != null && listAddresses.size() > 0 ) {

                Log.i("PlaceInfo", listAddresses.get(0).toString());

                String addressHolder = "";

                for (int i = 0; i <= listAddresses.get(0).getMaxAddressLineIndex() ; i++) {

                    addressHolder += listAddresses.get(0).getAddressLine(i) + "\n";

                }

                viewAddress.setText("Address: " + addressHolder);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        viewLatitude.setText("Latitude: " + lat.toString());
        viewLongitude.setText("Longitude: " + lng.toString());
        viewAlt.setText("Altitude: " + alt.toString() + "m");
        viewBearing.setText("Bearing: " + bearing.toString());
        viewSpeed.setText("Speed: " + speed.toString() + " m/s");
        viewAccuracy.setText("Accuracy: " + accuracy.toString() + "m");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
