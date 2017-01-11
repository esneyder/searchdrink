package com.gleamsoft.searchdrink;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
                           GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
                           GoogleMap.OnMarkerDragListener, GoogleMap.InfoWindowAdapter
{
private GoogleMap mMap;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                                                  .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

}
@Override
public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.add:
            addMarker();
           return true;
        case R.id.maptypeHYBRID:
            if(mMap != null){
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            }

        case R.id.maptypeNONE:
            if(mMap != null){
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                return true;
            }

        case R.id.maptypeNORMAL:
            if(mMap != null){
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            }


        case R.id.maptypeSATELLITE:
            if(mMap != null){
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            }

        case R.id.maptypeTERRAIN:
            if(mMap != null){
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            }

    }
    return super.onOptionsItemSelected(item);
}
@Override
public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setOnMapClickListener(this);
    mMap.setOnMapLongClickListener(this);
    mMap.setOnMarkerDragListener(this);
    mMap.setInfoWindowAdapter(this);

}
private void addMarker(){
    if(mMap != null){

//create custom LinearLayout programmatically
        LinearLayout layout = new LinearLayout(MapsActivity.this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleField = new EditText(MapsActivity.this);
        titleField.setHint("Title");

        final EditText latField = new EditText(MapsActivity.this);
        latField.setHint("Latitude");
        latField.setInputType(InputType.TYPE_CLASS_NUMBER
                                      | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                      | InputType.TYPE_NUMBER_FLAG_SIGNED);

        final EditText lonField = new EditText(MapsActivity.this);
        lonField.setHint("Longitude");
        lonField.setInputType(InputType.TYPE_CLASS_NUMBER
                                      | InputType.TYPE_NUMBER_FLAG_DECIMAL
                                      | InputType.TYPE_NUMBER_FLAG_SIGNED);

        layout.addView(titleField);
        layout.addView(latField);
        layout.addView(lonField);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Marker");
        builder.setView(layout);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean parsable = true;
                Double lat = null, lon = null;

                String strLat = latField.getText().toString();
                String strLon = lonField.getText().toString();
                String strTitle = titleField.getText().toString();

                try{
                    lat = Double.parseDouble(strLat);
                }catch (NumberFormatException ex){
                    parsable = false;
                    Toast.makeText(MapsActivity.this,
                            "Latitude does not contain a parsable double",
                            Toast.LENGTH_LONG).show();
                }

                try{
                    lon = Double.parseDouble(strLon);
                }catch (NumberFormatException ex){
                    parsable = false;
                    Toast.makeText(MapsActivity.this,
                            "Longitude does not contain a parsable double",
                            Toast.LENGTH_LONG).show();
                }

                if(parsable){

                    LatLng targetLatLng = new LatLng(lat, lon);
                    MarkerOptions markerOptions =
                            new MarkerOptions().position(targetLatLng).title(strTitle);

                    markerOptions.draggable(true);

                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(targetLatLng));

                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }else{
        Toast.makeText(MapsActivity.this, "Map not ready", Toast.LENGTH_LONG).show();
    }
}

@Override
public void onMapClick(LatLng latLng) {
    Toast.makeText(MapsActivity.this,
            "onMapClick:\n" + latLng.latitude + " : " + latLng.longitude,
            Toast.LENGTH_LONG).show();
}

@Override
public void onMapLongClick(LatLng latLng) {
    Toast.makeText(MapsActivity.this,
            "onMapLongClick:\n" + latLng.latitude + " : " + latLng.longitude,
            Toast.LENGTH_LONG).show();

//Add marker on LongClick position
    MarkerOptions markerOptions =
            new MarkerOptions().position(latLng).title(latLng.toString());
    markerOptions.draggable(true);

    mMap.addMarker(markerOptions);
}


@Override
public void onMarkerDragStart(Marker marker) {
    marker.setTitle(marker.getPosition().toString());
    marker.showInfoWindow();
    marker.setAlpha(0.5f);
}

@Override
public void onMarkerDrag(Marker marker) {
    marker.setTitle(marker.getPosition().toString());
    marker.showInfoWindow();
    marker.setAlpha(0.5f);
}

@Override
public void onMarkerDragEnd(Marker marker) {
    marker.setTitle(marker.getPosition().toString());
    marker.showInfoWindow();
    marker.setAlpha(1.0f);
}

@Override
public View getInfoWindow(Marker marker) {
    return null;
//return prepareInfoView(marker);
}

@Override
public View getInfoContents(Marker marker) {
//return null;
    return prepareInfoView(marker);

}

private View prepareInfoView(Marker marker){
//prepare InfoView programmatically
    LinearLayout infoView = new LinearLayout(MapsActivity.this);
    LinearLayout.LayoutParams infoViewParams = new LinearLayout.LayoutParams(
                                                                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    infoView.setOrientation(LinearLayout.HORIZONTAL);
    infoView.setLayoutParams(infoViewParams);

    ImageView infoImageView = new ImageView(MapsActivity.this);
//Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
    Drawable drawable = getResources().getDrawable(R.drawable.locations);
    infoImageView.setImageDrawable(drawable);
    infoView.addView(infoImageView);

    LinearLayout subInfoView = new LinearLayout(MapsActivity.this);
    LinearLayout.LayoutParams subInfoViewParams = new LinearLayout.LayoutParams(
                                                                                       LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    subInfoView.setOrientation(LinearLayout.VERTICAL);
    subInfoView.setLayoutParams(subInfoViewParams);

    TextView subInfoLat = new TextView(MapsActivity.this);
    subInfoLat.setText("Lat: " + marker.getPosition().latitude);
    TextView subInfoLnt = new TextView(MapsActivity.this);
    subInfoLnt.setText("Lnt: " + marker.getPosition().longitude);
    subInfoView.addView(subInfoLat);
    subInfoView.addView(subInfoLnt);
    infoView.addView(subInfoView);

    return infoView;
}

}
