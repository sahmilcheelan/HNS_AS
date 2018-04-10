package com.example.cheelan.hns;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;

import java.io.IOException;

public class WikiActivity extends AppCompatActivity implements LocationListener {

    private ArchitectView architectView;
    private LocationProvider locationProvider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);
        this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setLicenseKey( "ORJxysP8GFfeXrAu4GBqJLkJSst8vbItMVMI/6GHqtMo2lMGdVY69Tfk9fXVL2BTMpfoSCw2cuCNIK8gR/SRaIR6GTMx9DgoxQYHi/r6ZEm7diUW9tFtuJPQ+Pl7H9opkJ7Z5vqKYiBL+cObdI04GFzIHkgP1viT+/orYpXPllpTYWx0ZWRfX2AXHnE+dUERuxJXU+uHlzlQ1swKXOYl+RAbk+f3STKBN5ELYKUTtNBwHWBHD1fDZ0jML4buq09d0x8Ps9BD7gO9p7xMlhoXzCx2y2Mm4YTh9jlZprKK0G+nRFofqPfhw4XZA+vP0rPMNA/zhw58HT1B09LbB0g/9/cXj6C2MbTdwG8X05O6+h9BRIv4OZXkQUvBWb0nGwZ1V9hvWIQZgUQ9wCc2ktJm8cpBNysA80eygrcjyRiWpsstdZpSu3pBvt60wsnkVHyacnBDl9BdC7lYzbpGIewvnhWzD+de7j4jrLEbEOBYhHlr7hQRItWatqtHBNbRQ8nqM4RcThObptVxIMI4FvCuCAl4xEXsyqeKfA6oEDiY1o0plNXLnQP7BpDek+rqcBYT2Brt5Qcwn7O5v/Mn2Hv5zcwl1wEslwT2eBM1D6TxIQWhi55fZOPbKPeOpCjIYDNzNX/qblUAypr7MRmS0UDbfqvM+ZI6nSk3soU4t7uIzac=" );
        this.architectView.onCreate( config );

       // locationProvider = new LocationProvider(this,this,null);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);



        architectView.onPostCreate();
      //architectView.setLocation(11.0516533,72.9714383,10);
        architectView.setLocation( 13.0508849,74.9661632,100);

        try {
            this.architectView.load( "file:///android_asset/demo2/index.html" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

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

        architectView.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
       // float accuracy = location.hasAccuracy() ? location.getAccuracy() : 1000;
        //if (location.hasAltitude()) {
          //  architectView.setLocation(location.getLatitude(), location.getLongitude(), location.getAltitude(), accuracy);
        //} else {
          //  architectView.setLocation(location.getLatitude(), location.getLongitude(), accuracy);
        //}

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
