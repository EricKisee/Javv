package com.craptordevelopers.javv;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.GooglePlayServicesUtil;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
        import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;

        import android.location.Criteria;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import android.*;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Toast;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 */
public class MapsActivity extends AppCompatActivity implements LocationListener ,OnMapReadyCallback,ConnectionCallbacks,
        OnConnectionFailedListener  {
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private double latitude , longitude;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;
    GoogleMap googleMap;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    // UI elements
    private TextView lblLocation;
    private Button btnShowLocation, btnStartLocationUpdates;


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
//        googleMap = supportMapFragment.getMap();
//        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000l , 0f, this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            finish();
            }
        });

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        int permissionCheck= ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionSecond=ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED && permissionSecond==PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }else ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>1 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                && grantResults[1]==PackageManager.PERMISSION_GRANTED){
        }
    }

    private void getMyLocation() {

        int permissionCheck= ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionSecond=ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED && permissionSecond==PackageManager.PERMISSION_GRANTED) {

        }else ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION},1);

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();


        } else {
            latitude = 0;
            longitude = 0;
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        getMyLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    private void setUser(){
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    // Name, email address, and profile photo Url
                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.
                    String uid = user.getUid();
                } else {
                    // User is signed out
                    Toast.makeText(MapsActivity.this,"User Logged out ",Toast.LENGTH_SHORT).show();

                }
                // ...
            }
        };
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        getMyLocation();
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));
    }
}