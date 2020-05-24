package com.example.lab7;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapLongClickListener {

    List<MarkerActivity> markerList;
    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 101;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    private SensorManager sensorManager;
    private Sensor mSensor;

    Marker gpsMarker = null;

    private Button button2;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton floatingActionButton2;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        markerList = new ArrayList<>();

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
            }
        });

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        floatingActionButton2 = findViewById(R.id.floatingActionButton2);
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButton.animate().alpha(0.0f).setDuration(1000);
                floatingActionButton2.animate().alpha(0.0f).setDuration(1000);
                textView.animate().alpha(0.0f).setDuration(1000);
            }
        });

        textView = findViewById(R.id.textView);


    }



    private void startSensor() {

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    private void stopLocationUpdates() {
        if(locationCallback != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }
    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    public void zoomInClick(View view) {
        mMap.moveCamera(CameraUpdateFactory.zoomIn());
    }

    public void zoomOutClick(View view) {
        mMap.moveCamera(CameraUpdateFactory.zoomOut());
    }


    public void onSensorChanged(SensorEvent event){
        textView.setText("Acceleration\n x:"+event.values[0]+" y: "+event.values[1]);
    }
/*
    private void saveToJson(){
        Gson gson = new Gson();
        String listJson = gson.toJson(markerList);
        FileOutputStream outputStream;
        try{
            outputStream = openFileOutput("tasks.json");
            FileWriter writer = new FileWriter(outputStream.getFD());
            writer.write(listJson);
            writer.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void restoreFromJson(){
        FileInputStream inputStream;
        int DEFAULT_BUFFER_SIZE = 10000;
        Gson gson = new Gson();
        String readJson;

        try{
            inputStream = openFileInput("tasks.json");
            FileReader reader = new FileReader(inputStream.getFD());
            char[] buf = new char[DEFAULT_BUFFER_SIZE];
            int n;
            StringBuilder builder = new StringBuilder();
            while ((n=reader.read(buf))>=0){
                String tmp = String.valueOf(buf);
                String substring = (n<DEFAULT_BUFFER_SIZE) ? tmp.substring(0, n): tmp;
                builder.append(substring);
            }
            reader.close();
            reader = builder.toString();
            Type collectionType = new TypeToken<List<MarkerActivity>>(){

            }.getType();
            List<MarkerActivity> o = gson.fromJson(readJson, collectionType);

            if(o != null){
                markerList.add();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng())
                        .alpha(0.8f)
                        .title(String.format("Position: (%.2f, %.2f)",mLat, mLon)));
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

*/
    @Override
    public void onMapLoaded() {
        Log.i(MapsActivity.class.getSimpleName(), "MapLoaded");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        Task<Location> lastLocation = fusedLocationClient.getLastLocation();

        lastLocation.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null && mMap != null) {

                }
            }
        });

        createLocationRequest();
        createLocationCallback();
        startLocationUpdates();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        CameraPosition cameraPos = mMap.getCameraPosition();
        floatingActionButton.animate().translationY(0f).alpha(1f).setDuration(1000);
        floatingActionButton2.animate().translationY(0f).alpha(1f).setDuration(1000);
        textView.animate().translationY(0f).alpha(1f).setDuration(1000);
        return false;
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
    }

    private void createLocationCallback(){
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                if(locationResult != null){
                    if(gpsMarker != null)
                        gpsMarker.remove();
                }
            }
        };
    }



    @Override
    public void onMapLongClick(LatLng latLng) {

         Marker marker = mMap.addMarker(new MarkerOptions()
        .position(new LatLng(latLng.latitude,latLng.longitude))
                 .position(new LatLng(latLng.latitude, latLng.longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .alpha(0.8f)
                .title(String.format("Position:(%.2f, %.2f)", latLng.latitude,latLng.longitude)));
        markerList.add(new MarkerActivity());
    }




}
