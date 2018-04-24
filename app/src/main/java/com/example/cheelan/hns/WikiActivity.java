package com.example.cheelan.hns;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;
import com.example.cheelan.hns.MapsActivity;

import java.io.IOException;

public class WikiActivity extends AppCompatActivity implements LocationListener {

    private ArchitectView architectView;
    private LocationProvider locationProvider;
    public MapsActivity mapsActivity=new MapsActivity();
    private double clat,clon;
    private LocationManager lm;

    private double latitude;
    private double longitude;
    private double altitude = 0;
    private float accuracy;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey( "ORJxysP8GFfeXrAu4GBqJLkJSst8vbItMVMI/6GHqtMo2lMGdVY69Tfk9fXVL2BTMpfoSCw2cuCNIK8gR/SRaIR6GTMx9DgoxQYHi/r6ZEm7diUW9tFtuJPQ+Pl7H9opkJ7Z5vqKYiBL+cObdI04GFzIHkgP1viT+/orYpXPllpTYWx0ZWRfX2AXHnE+dUERuxJXU+uHlzlQ1swKXOYl+RAbk+f3STKBN5ELYKUTtNBwHWBHD1fDZ0jML4buq09d0x8Ps9BD7gO9p7xMlhoXzCx2y2Mm4YTh9jlZprKK0G+nRFofqPfhw4XZA+vP0rPMNA/zhw58HT1B09LbB0g/9/cXj6C2MbTdwG8X05O6+h9BRIv4OZXkQUvBWb0nGwZ1V9hvWIQZgUQ9wCc2ktJm8cpBNysA80eygrcjyRiWpsstdZpSu3pBvt60wsnkVHyacnBDl9BdC7lYzbpGIewvnhWzD+de7j4jrLEbEOBYhHlr7hQRItWatqtHBNbRQ8nqM4RcThObptVxIMI4FvCuCAl4xEXsyqeKfA6oEDiY1o0plNXLnQP7BpDek+rqcBYT2Brt5Qcwn7O5v/Mn2Hv5zcwl1wEslwT2eBM1D6TxIQWhi55fZOPbKPeOpCjIYDNzNX/qblUAypr7MRmS0UDbfqvM+ZI6nSk3soU4t7uIzac=" );
        this.architectView.onCreate( config );

        //locationProvider = new LocationProvider(this,this);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //clat=mapsActivity.check1;
        //clon=mapsActivity.check2;
        Toast.makeText(this,"check",Toast.LENGTH_LONG).show();
        architectView.onPostCreate();
        //architectView.setLocation(clat,clon, 100);

      //architectView.setLocation(11.0516533,72.9714383,13,10);


        try {
            this.architectView.load( "file:///android_asset/demo3/index.html" );
            architectView.setLocation( 13.128628403018228,74.89088584566348,16,1);
           // architectView.setLocation(latitude, longitude, altitude, accuracy);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
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
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);

        architectView.onResume();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        architectView.onDestroy();
    }

    @Override
    protected void onPause() {

        super.onPause();

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
        lm.removeUpdates(this);


        architectView.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {

        Toast.makeText(this,"location changed",Toast.LENGTH_LONG).show();
        float accuracy = location.hasAccuracy() ? location.getAccuracy() : 1;
        if (location.hasAltitude()) {
            architectView.setLocation(location.getLatitude(), location.getLongitude(), location.getAltitude(), accuracy);
        } else {
            architectView.setLocation(location.getLatitude(), location.getLongitude(), accuracy);
       }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        String newStatus = "";
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                newStatus = "out of service service";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                newStatus = "Temporarily available";
                break;
            case LocationProvider.AVAILABLE:
                newStatus = "available";
                break;
        }

        Toast.makeText(this,newStatus,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        architectView.setLocation(latitude, longitude, altitude, accuracy);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
