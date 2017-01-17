package com.gleamsoft.searchdrink;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Line;


public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
                           GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,
                           GoogleMap.OnMarkerDragListener, GoogleMap.InfoWindowAdapter, GoogleApiClient.OnConnectionFailedListener,
                           GoogleApiClient.ConnectionCallbacks {

private GoogleMap mMap;
LocationManager locationManager;

private static final String LOGTAG = "android-localizacion";

private static final int PETICION_PERMISO_LOCALIZACION = 101;

private GoogleApiClient apiClient;

private TextView lblLatitud;
private TextView lblLongitud;

LinearLayout layout;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    layout= (LinearLayout) findViewById(R.id.llayout);
    lblLatitud = (TextView) findViewById(R.id.lblLatitud);
    lblLongitud = (TextView) findViewById(R.id.lblLongitud);
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                                                  .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    apiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this)
                        .addConnectionCallbacks(this)
                        .addApi(LocationServices.API)
                        .build();

    layout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        startActivity(new Intent(getBaseContext(),AboutActivity.class));
        }
    });
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
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            }

        case R.id.maptypeNONE:
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                return true;
            }

        case R.id.maptypeNORMAL:
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            }


        case R.id.maptypeSATELLITE:
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            }

        case R.id.maptypeTERRAIN:
            if (mMap != null) {
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

    LatLng mid =new  LatLng(4.5922479,-74.139592);
    LatLng origin = new LatLng(4.5922479,-74.1395925);
    LatLng target = new LatLng(4.5922479,-74.1395925);



    mMap.addMarker(new MarkerOptions().position(origin).title("Dato 1!"));
    mMap.addMarker(new MarkerOptions().position(target).title("Dato 2!"));
    mMap.addMarker(new MarkerOptions().position(mid).title("Dato 3!"));

    mMap.getUiSettings().setZoomControlsEnabled(true);


    //Calculate the markers to get their position
    LatLngBounds.Builder b = new LatLngBounds.Builder();
    b.include(origin);
    b.include(target);

    LatLngBounds bounds = b.build();
    //Change the padding as per needed

    int width = getResources().getDisplayMetrics().widthPixels;
    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200, 200, 5);
    mMap.animateCamera(cu);
}



private void addMarker() {
    if (mMap != null) {

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

                try {
                    lat = Double.parseDouble(strLat);
                } catch (NumberFormatException ex) {
                    parsable = false;
                    Toast.makeText(MapsActivity.this,
                            "Latitude does not contain a parsable double",
                            Toast.LENGTH_LONG).show();
                }

                try {
                    lon = Double.parseDouble(strLon);
                } catch (NumberFormatException ex) {
                    parsable = false;
                    Toast.makeText(MapsActivity.this,
                            "Longitude does not contain a parsable double",
                            Toast.LENGTH_LONG).show();
                }

                if (parsable) {

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
    } else {
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

private View prepareInfoView(Marker marker) {
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

@Override
public void onConnectionFailed(ConnectionResult result) {
    //Se ha producido un error que no se puede resolver automáticamente
    //y la conexión con los Google Play Services no se ha establecido.

    Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
}

@Override
public void onConnected(@Nullable Bundle bundle) {
    //Conectado correctamente a Google Play Services

    if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PETICION_PERMISO_LOCALIZACION);
    } else {

        Location lastLocation =
                LocationServices.FusedLocationApi.getLastLocation(apiClient);

        updateUI(lastLocation);
        loadMaps(lastLocation);

    }
}

@Override
public void onConnectionSuspended(int i) {
    //Se ha interrumpido la conexión con Google Play Services

    Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
}


private void updateUI(Location loc) {
    if (loc != null) {
        lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
        lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
    } else {
        lblLatitud.setText("Latitud: (desconocida)");
        lblLongitud.setText("Longitud: (desconocida)");
    }
}

@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode == PETICION_PERMISO_LOCALIZACION) {
        if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //Permiso concedido

            @SuppressWarnings("MissingPermission")
            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);

        } else {
            //Permiso denegado:
            //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            Log.e(LOGTAG, "Permiso denegado");
        }
    }
}

private void loadMaps(Location l) {
   LatLng mLocation=new LatLng(l.getLatitude(),l.getLongitude());
    mMap.addMarker(new MarkerOptions().position(mLocation).title("Mi ubicación"));
    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        mMap.setMyLocationEnabled(true);
    } else {
        // Show rationale and request permission.
        Toast.makeText(this, "Sin permisos", Toast.LENGTH_SHORT).show();
    }
    double lat=l.getLatitude();
    double lng=l.getLongitude();
    mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng) ,15) );
}
}


