package com.example.cheelan.hns;

import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.util.Log;

import org.json.JSONObject;
import org.w3c.dom.TypeInfo;

/**
 * Created by cheelan on 28-02-2018.
 */

public class GetDirectionsData extends AsyncTask<Object,String ,String >{


    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    String duration,distance;
    LatLng latLng;

    @Override
    protected String doInBackground(Object... objects) {
        mMap=(GoogleMap)objects[0];
        url=(String)objects[1];
        latLng=(LatLng)objects[2];

        DownloadUrl downloadUrl=new DownloadUrl();
        try {
            googleDirectionsData=downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleDirectionsData;

    }
    @Override
    protected void onPostExecute(String s) {


       String [] directionsList;
        DataParser parser=new DataParser();
        directionsList=parser.parseDirections(s);
        displayDirection(directionsList);



    }

    public void displayDirection(String[] directionsList)
    {
        mMap.addPolyline(null);
        JSONObject LatLngObj = new JSONObject();
        List<LatLng> cLatLong = new ArrayList<LatLng>(); //this value stores all the latitude and longitude values from source to destination
        int count=directionsList.length;
        for(int i=0;i<count;i++)
        {
            PolylineOptions options=new PolylineOptions();
            options.color(Color.RED);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));
            for (int j = 0; j< options.getPoints().size(); j++) {
                cLatLong.add(new LatLng(options.getPoints().get(j).latitude, options.getPoints().get(j).longitude));
                LatLngObj = {
                        "Latitude"  : cLatLong.get(j).latitude,
                        "Longitude" : cLatLong.get(j).longitude
            };
            mMap.addPolyline(options);
        }
    }
}
