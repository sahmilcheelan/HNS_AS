package com.example.cheelan.hns;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener
{

    public GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private  Location lastLocation;
    private Marker currentLocationMarker;
    public double latitude,longitude;
    double end_latitude,end_longitude;
    double check1,check2;
    int PROXIMITY_RADIUS=5000;
    public static final int REQUEST_LOCATION_CODE=99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_LOCATION_CODE:
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //permission is granted
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                {
                    if (client==null)
                    {
                        buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);
                }
            }
            else
            {
                //permission is denied
                Toast.makeText(this,"perrmission denied",Toast.LENGTH_LONG).show();
            }
            return;
        }
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
           buildGoogleApiClient();
           mMap.setMyLocationEnabled(true);
       }
        mMap.setMyLocationEnabled(true);

       mMap.setOnMarkerDragListener(this);
       mMap.setOnMarkerClickListener(this);


    }

    public void onClick(View v)
    {
        Object dataTransfer[]=new Object[2];
        GetNearbyPlacesData getNearbyPlacesData=new GetNearbyPlacesData();
        switch (v.getId()) {
            case R.id.B_search:

            {
                EditText tf_location = findViewById(R.id.TF_location);
                String location = tf_location.getText().toString();
                List<Address> addressList = null;
                MarkerOptions markerOptions = new MarkerOptions();
                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < addressList.size(); i++) {
                        Address myAddress = addressList.get(i);
                        LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                        check1=myAddress.getLatitude();
                        check2=myAddress.getLongitude();
                        markerOptions.position(latLng);
                        markerOptions.title("your search result");
                        mMap.addMarker(markerOptions);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    }
                }
            }




            break;

            case  R.id.B_hospital:
                mMap.clear();
                String hospital="hospital";
                String url=getUrl(latitude,longitude,hospital);

                dataTransfer[0]=mMap;
                dataTransfer[1]=url;


                getNearbyPlacesData.execute(dataTransfer);

                Toast.makeText(MapsActivity.this,"showing nearby hospitals",Toast.LENGTH_LONG).show();
                break;

            
            case R.id.B_to:

                Toast.makeText(MapsActivity.this,"showing",Toast.LENGTH_LONG).show();
              dataTransfer =  new Object[3];
              url = getDirectionsUrl();
              GetDirectionsData getDirectionsData=new GetDirectionsData();
              dataTransfer[0]=mMap;
              dataTransfer[1]=url;
              //dataTransfer[2]=new LatLng(end_latitude,end_longitude);
                dataTransfer[2]=new LatLng(check1,check2);

              getDirectionsData.execute(dataTransfer);



              break;

            case R.id.ar_button:
                Intent i = new Intent(getApplicationContext(),WikiActivity.class);
                startActivity(i);
                setContentView(R.layout.activity_wiki);


        }
    }

    private String getDirectionsUrl()
    {
        StringBuilder googleDirectionsUrl =new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latitude+","+longitude);
       // googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&destination="+check1+","+check2);
        //googleDirectionsUrl.append("&waypoint=")
        googleDirectionsUrl.append("&key="+"AIzaSyAbyB6GLmP2ROBmtq_iWBe5KBAnVsxAIwk");

        return googleDirectionsUrl.toString();



    }

    private  String getUrl(double latitude,double longitude,String nearbyPlace)
    {
        StringBuilder googlePlaceUrl=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyDXCDfvYp3ZzNrGIuESfzsl-6y5n0UFuZE");
        return googlePlaceUrl.toString();
    }
    protected  synchronized void buildGoogleApiClient()
    {

        client=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }
    @Override
    public void onLocationChanged(Location location) {

        lastLocation=location;
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        if(currentLocationMarker!=null)
        {
            currentLocationMarker.remove();
        }

        LatLng LatLng =new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions mo = new MarkerOptions();
        mo.position(LatLng);
        mo.title("current location");
        mo.draggable(true);
        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker = mMap.addMarker(mo);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
        if(client!=null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }

    }

    /*@Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest =new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    public  boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED )
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            return  false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.setDraggable(true);
      //  Toast.makeText(MapsActivity.this,"clicked",Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //Toast.makeText(MapsActivity.this,"dragging",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //Toast.makeText(MapsActivity.this,"drag",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        end_latitude=marker.getPosition().latitude;
        end_longitude=marker.getPosition().longitude;
    }
}
