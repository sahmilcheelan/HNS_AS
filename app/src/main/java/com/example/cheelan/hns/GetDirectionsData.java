package com.example.cheelan.hns;

import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
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
    String [] directionsList;
    Collection<JSONObject> LatLngOb = new ArrayList<JSONObject>();
    //public Collection<JSONObject> LatLngObj = new ArrayList<JSONObject>();
    //public  List<LatLng> cLatLong = new ArrayList<LatLng>();
    MapsActivity mapsActivity=new MapsActivity();


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



        DataParser parser=new DataParser();
        directionsList=parser.parseDirections(s);
        try {
            displayDirection(directionsList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void displayDirection(String[] directionsList) throws JSONException {
        JSONArray checkLat = new JSONArray();
        int count = directionsList.length;

        for(int i=0;i<count;i++)
        {
            PolylineOptions options=new PolylineOptions();
            options.color(Color.RED);
            options.width(20);
            options.addAll(PolyUtil.decode(directionsList[i]));
            for (int j = 0; j< options.getPoints().size(); j++) {
                JSONObject coordinates = new JSONObject();
                mapsActivity.cLatLong.add(new LatLng(options.getPoints().get(j).latitude, options.getPoints().get(j).longitude));
                coordinates.put("Latitude", options.getPoints().get(j).latitude);
                coordinates.put("Longitude", options.getPoints().get(j).longitude);
                mapsActivity.LatLngObj.add(coordinates);
            }

            mMap.addPolyline(options);
        }
    }

    public double getCoordinates(){
        double a=mapsActivity.cLatLong.get(0).latitude;
        return a;
    }

}

