package com.example.cheelan.hns;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.plugin.googlemap.GoogleMapWorldPlugin;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.example.cheelan.hns.MapsActivity;

import java.util.ArrayList;




    public class Ar_Activity extends AppCompatActivity implements OnClickBeyondarObjectListener,View.OnClickListener, GoogleMap.OnMarkerClickListener {

        private BeyondarFragmentSupport mBeyondarFragment;
        private GoogleMapWorldPlugin mGoogleMapPlugin;

        GoogleMap mMap;
        public double lat,lon;

        // ...
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            setContentView(R.layout.simple_camera);
            mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(R.id.beyondarFragment);
            // ...

            // private Context context;
            MapsActivity mapsActivity=new MapsActivity();


            //lat=mapsActivity.lastLocation.getLatitude();
           // lon=mapsActivity.longitude;
            World world = new World(this);


// The user can set the default bitmap. This is useful if you are
// loading images form Internet and the connection get lost
            world.setDefaultBitmap(R.drawable.end_green,5);

// User position (you can change it using the GPS listeners form Android
// API)
world.setGeoPosition(74.89088584566348d,13.128628403018228d);
        //    world.setGeoPosition(41.26533734214473d,1.925848038959814d);

            mGoogleMapPlugin = new GoogleMapWorldPlugin(this);
            world.addPlugin(mGoogleMapPlugin);

            // Create an object with an image in the app resources.
            GeoObject go1 = new GeoObject(1l);

            go1.setGeoPosition(world.getLatitude(), world.getLongitude());
            go1.setImageResource(R.drawable.test_1);
            go1.setName("Creature 1");

            // Is it also possible to load the image asynchronously form internet
            GeoObject go2 = new GeoObject(2l);
            go2.setGeoPosition(74.89088584566348d,13.128628403018228d);
            go2.setImageResource(R.drawable.test_2);
            go2.setName("Online image");
            // Also possible to get images from the SDcard
            GeoObject go3 = new GeoObject(3l);
            go3.setGeoPosition(41.26550959641445d, 1.925873388087619d);
            go3.setImageResource(R.drawable.test_3);
            go3.setName("IronMan from sdcard");

            // And the same goes for the app assets
            GeoObject go4 = new GeoObject(4l);
            go4.setGeoPosition(11.987467d,75.275876d);
            go4.setImageResource(R.drawable.test_4);
            go4.setName("Image from assets");

// We add this GeoObjects to the world
            world.addBeyondarObject(go1);
            world.addBeyondarObject(go2);
            world.addBeyondarObject(go3);
            world.addBeyondarObject(go4);

// Finally we add the Wold data in to the fragment
            mBeyondarFragment.setWorld(world);

            mBeyondarFragment.setOnClickBeyondarObjectListener(this);
        }

        // ...

        // ...
        @Override
        public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
            // The first element in the array belongs to the closest BeyondarObject

            Toast.makeText(this, "Clicked on: " + lat, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }


// ...

    }



