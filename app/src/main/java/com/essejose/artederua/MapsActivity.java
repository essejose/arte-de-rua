package com.essejose.artederua;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.essejose.artederua.dao.EventDAO;
import com.essejose.artederua.model.Event;




public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Event event;
    EventDAO events  = new EventDAO(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    private void populaMapa() {


        for( Event eventM : events.getAll())
        {
            Bitmap v =  BitmapFactory.decodeFile(eventM.getImage());
            Bitmap s = Bitmap.createScaledBitmap(v, 100, 100, false);

            MarkerOptions markerPOI = new MarkerOptions();
            markerPOI .position(new LatLng(eventM.getLatiude(),eventM.getLongitude()))
                    .title(eventM.getTitle())
                    .snippet(eventM.getDescripion())
                    .icon(BitmapDescriptorFactory.fromBitmap(s));

            mMap.addMarker(markerPOI );

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



        if(getIntent().hasExtra("EVENT")){



            event = getIntent().getParcelableExtra("EVENT");
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bmp = Bitmap.createBitmap(80, 80, conf);

            Bitmap myBitmap =  BitmapFactory.decodeFile(event.getImage());
            Bitmap smallMarker = Bitmap.createScaledBitmap(myBitmap, 100, 100, false);
            Canvas canvas1 = new Canvas(smallMarker);

            Paint color = new Paint();
            color.setTextSize(35);
            color.setColor(Color.BLACK);

            LatLng eventLng = new LatLng(event.getLatiude(), event.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(eventLng)
                    .title(event.getTitle())
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .anchor(0.5f, 1));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLng,18));
        }else{

            populaMapa();

        }


    }
}
