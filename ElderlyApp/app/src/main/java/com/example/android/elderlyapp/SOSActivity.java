package com.example.android.elderlyapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SOSActivity extends AppCompatActivity implements android.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView latituteField;
    private TextView longitudeField,AddressField;
    private LocationManager locationManager;
    private String provider,ad;
    Geocoder geocoder;
    FusedLocationProviderApi fusedLocationProviderApi;
    List<Address> addresses;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private FusedLocationProviderApi locationProvider=LocationServices.FusedLocationApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);
        AddressField = (TextView) findViewById(R.id.TextView44);

        googleApiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest=new LocationRequest();
        locationRequest.setInterval(60*1000);
        locationRequest.setFastestInterval(15*1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


//        geocoder = new Geocoder(this, Locale.getDefault());
//        // Get the location manager
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        // Define the criteria how to select the locatioin provider -> use
//        // default
//        Criteria criteria = new Criteria();
//        provider = locationManager.getBestProvider(criteria,true);
//        Log.d("LOCATION",provider+"");
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
//
//        // Initialize the location fields
//        if (location != null) {
//            System.out.println("Provider " + provider + " has been selected.");
//
//            try {
//                addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
//                ad=address+","+city+","+state+"\n"+postalCode+".\nKnown Landmark Nearby :"+knownName;
//                //AddressField.setText(address+","+city+","+state+"\n"+postalCode+".\nKnown Landmark Nearby :"+knownName);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            onLocationChanged(location);
//        } else {
//            latituteField.setText("Location not available");
//            longitudeField.setText("Location not available");
//        }

    }


    @Override
    public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        String latitude=String.valueOf(lat);
        String longitude=String.valueOf(lng);
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
        Toast.makeText(this,"Message sent to contacts",Toast.LENGTH_LONG).show();
        EmergencyMessage(lat,lng);
        Intent intent=new Intent(SOSActivity.this,AccountActivity.class);
        startActivity(intent);

    }

    public void showLocation(){

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public void EmergencyMessage(double lat,double lng) {
        SmsManager smsManager = SmsManager.getDefault();
        String sms ="http://maps.google.com/?q="+lat+","+lng;
        String phoneNumber = "";//add contacts
        Log.d("COORDINATES", sms);
        smsManager.sendTextMessage(phoneNumber, null, sms+ad, null, null);
    }

    //after onStart
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }



    @Override
    public void onResume() {
        super.onResume();
        if(googleApiClient.isConnected())
            requestLocationUpdates();
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//           return;
//        }
//        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, (com.google.android.gms.location.LocationListener) this);
//        locationManager.removeUpdates(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }



    void requestLocationUpdates(){
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest, (com.google.android.gms.location.LocationListener) this);
    }
}
