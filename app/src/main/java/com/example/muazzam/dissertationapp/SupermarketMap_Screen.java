package com.example.muazzam.dissertationapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class SupermarketMap_Screen extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;

    private static final int MY_PERMISSION_REQUEST_CODE = 11;
    private static final int PLAY_SERVICES_RESOLUTION__REQUEST = 10;
    private Location mLastLocation;

    double latitude, longitude, x, y;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;


    private static final LatLng IntermarBagatelle = new LatLng(-20.2253832, 57.4957097);
    private static final LatLng IntermarEbene = new LatLng(-20.243435, 57.4862355);
    private static final LatLng IntermarGrandBaie = new LatLng(-20.0210888, 57.5763044);
    private static final LatLng IntermarQuatreBornes = new LatLng(-20.2670612, 57.4758409);
    private static final LatLng IntermarCurepipe = new LatLng(-20.323084, 57.5161963);
    private static final LatLng IntermarBeauBassin = new LatLng(-20.2130891, 57.4678062);
    private static final LatLng WinnerTrianon = new LatLng(-20.2627615, 57.4807817);
    private static final LatLng WinnerStPierre = new LatLng(-20.2260515, 57.5363423);
    private static final LatLng WinnerCurepipe = new LatLng(-20.3272448, 57.5264073);
    private static final LatLng WinnerQuatreBornes = new LatLng(-20.2674668, 57.4775094);
    private static final LatLng WinnerCoromandel = new LatLng(-20.2060469, 57.467475);
    private static final LatLng DreamPricesStPierre = new LatLng(-20.222171, 57.5390363);
    private static final LatLng DreamPricesRoseHill = new LatLng(-20.2582237, 57.4649763);
    private static final LatLng DreamPricesBeauBassin = new LatLng(-20.2195461, 57.4665523);
    private static final LatLng DreamPricesCurepipe = new LatLng(-20.313955, 57.5264343);
    private static final LatLng SuperVGrandBaie = new LatLng(-20.0125224, 57.5848714);
    private static final LatLng SuperVQuatreBornes = new LatLng(-20.2543201, 57.4819507);
    private static final LatLng JumbooPhoenix = new LatLng(-20.2798242, 57.4932505);


    Marker myCurrent;
    Marker InterBaga;
    Marker InterEbene;
    Marker InterQuatreBornes;
    Marker InterGrandBaie;
    Marker InterBeauBassin;
    Marker InterCurepipe;
    Marker WinTrianon;
    Marker WinStPierre;
    Marker WinQuatreBornes;
    Marker WinCoromandel;
    Marker WinCurepipe;
    Marker DPStPierre;
    Marker DPRoseHill;
    Marker DPBeauBassin;
    Marker DPCurepipe;
    Marker SupGrandBaie;
    Marker SupVQuatreBornes;
    Marker JumPhoenix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket_map__screen);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
                break;
        }
    }

    private void setUpLocation() {
        if (android.support.v4.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && android.support.v4.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestRuntimePermisson();
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    private void displayLocation() {
        if (android.support.v4.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && android.support.v4.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if(mLastLocation!=null)
        {
            latitude=mLastLocation.getLatitude();
            longitude=mLastLocation.getLongitude();

            //add marker
            if(myCurrent!=null)
            {
                myCurrent.remove();
            }
            myCurrent = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("YOU")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            //move camera to this postion
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15f));
        }

    }

    private void createLocationRequest(){
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

    }

    private boolean checkPlayServices(){
        int resultCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode!=ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RESOLUTION__REQUEST).show();
            }
            else
            {
                Toast.makeText(this, "This device cant't support",Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void requestRuntimePermisson() {
        android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]
                {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },MY_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i){
        mGoogleApiClient.connect();
    }

    private void startLocationUpdates(){
        if(android.support.v4.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && android.support.v4.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


     @Override
     public void onMapReady(GoogleMap googleMap) {
     mMap = googleMap;

     List<Marker> markerList = new ArrayList<>();

     //adding markers for winner

     InterBaga = mMap.addMarker(new MarkerOptions()
     .position(IntermarBagatelle)
     .title("Intermar Bagatelle")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
     markerList.add(InterBaga);

    InterEbene = mMap.addMarker(new MarkerOptions()
     .position(IntermarEbene)
     .title("Intermar Ebene")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
     markerList.add(InterEbene);

     InterQuatreBornes = mMap.addMarker(new MarkerOptions()
     .position(IntermarQuatreBornes)
     .title("Intermar Quatre Bornes")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
     markerList.add(InterQuatreBornes);

     InterCurepipe = mMap.addMarker(new MarkerOptions()
     .position(IntermarCurepipe)
     .title("Intermar Curepipe")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
     markerList.add(InterCurepipe);

     InterBeauBassin = mMap.addMarker(new MarkerOptions()
     .position(IntermarBeauBassin)
     .title("Intermar Beau Bassin")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
     markerList.add(InterBeauBassin);

     InterGrandBaie = mMap.addMarker(new MarkerOptions()
     .position(IntermarGrandBaie)
     .title("Intermar Grand Baie")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
     markerList.add(InterGrandBaie);

     //adding markers for winner
     WinTrianon = mMap.addMarker(new MarkerOptions()
     .position(WinnerTrianon)
     .title("Winner Trianon")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
     markerList.add(WinTrianon);

     WinStPierre = mMap.addMarker(new MarkerOptions()
     .position(WinnerStPierre)
     .title("Winner St Pierre")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
     markerList.add(WinStPierre);

     WinQuatreBornes = mMap.addMarker(new MarkerOptions()
     .position(WinnerQuatreBornes)
     .title("Winner Quatre Bornes")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
     markerList.add(WinQuatreBornes);

     WinCoromandel = mMap.addMarker(new MarkerOptions()
     .position(WinnerCoromandel)
     .title("Winner Coromandel")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
     markerList.add(WinCoromandel);

     WinCurepipe = mMap.addMarker(new MarkerOptions()
     .position(WinnerCurepipe)
     .title("Winner Curepipe")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
     markerList.add(WinCurepipe);

     //adding marker for jumboo
     JumPhoenix = mMap.addMarker(new MarkerOptions()
     .position(JumbooPhoenix)
     .title("Jumboo Phoenix")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
     markerList.add(JumPhoenix);

     //adding markers for superV
     SupVQuatreBornes = mMap.addMarker(new MarkerOptions()
     .position(SuperVQuatreBornes)
     .title("SuperV QuatreBornes")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
     markerList.add(SupVQuatreBornes);

     SupGrandBaie = mMap.addMarker(new MarkerOptions()
     .position(SuperVGrandBaie)
     .title("SuperV GrandBaie")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
     markerList.add(SupGrandBaie);

     //adding markers for dreamPrices
     DPStPierre = mMap.addMarker(new MarkerOptions()
     .position(DreamPricesStPierre)
     .title("DreamPrices StPierre")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
     markerList.add(DPStPierre);

     DPRoseHill = mMap.addMarker(new MarkerOptions()
     .position(DreamPricesRoseHill)
     .title("DreamPrices RoseHill")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
     markerList.add(DPRoseHill);

     DPBeauBassin = mMap.addMarker(new MarkerOptions()
     .position(DreamPricesBeauBassin)
     .title("DreamPrices BeauBassin")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
     markerList.add(DPBeauBassin);

     DPCurepipe = mMap.addMarker(new MarkerOptions()
     .position(DreamPricesCurepipe)
     .title("DreamPrices Curepipe")
     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
     markerList.add(DPCurepipe);


        mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude,longitude))
            .radius(3000.0)
                .strokeWidth(3f)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(70,50,50,150))
            );
        
}

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
