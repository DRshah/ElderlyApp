package com.example.android.elderlyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Darsh_Shah on 03-07-2017.
 */

public class SOSFragment extends Fragment implements LocationListener{
    private TextView latituteField;
    private TextView longitudeField,AddressField;
    private LocationManager locationManager;
    private String provider,ad;
    private boolean flag=true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sos, container, false);

        latituteField = (TextView) view.findViewById(R.id.TextView02);
        longitudeField = (TextView) view.findViewById(R.id.TextView04);
        AddressField = (TextView) view.findViewById(R.id.TextView44);

        LocationManager lm= (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, (android.location.LocationListener) this);

        return view;

    }
    @Override
    public void onLocationChanged(Location location) {
        Geocoder geocoder=new Geocoder(this.getActivity(),Locale.getDefault());
        List<Address> addresses;
        if(location==null){
            latituteField.setText("not found");
            longitudeField.setText("not found");
        }
        else {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                ad = address + "," + city + "," + state + "\n" + postalCode + ".\nKnown Landmark Nearby :" + knownName;
            }
            catch (Exception e){}



            String latitude = String.valueOf(lat);
            String longitude = String.valueOf(lng);
            latituteField.setText(lat+"");
            longitudeField.setText(lng+"");
            AddressField.setText(ad);
            if(flag) {
                flag=false;
                EmergencyMessage(lat, lng,ad);
            }

        }
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

    public void EmergencyMessage(double lat,double lng,String ad) {
        SmsManager smsManager = SmsManager.getDefault();
        String sms ="http://maps.google.com/?q="+lat+","+lng;
        String phoneNumber = "9930945890";
        Log.d("COORDINATES", sms+""+ad);
        smsManager.sendTextMessage(phoneNumber, null, sms+"="+ad, null, null);

    }
}
