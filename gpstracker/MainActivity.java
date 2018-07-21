package com.example.itb.gps;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private Button track, stoptrack;
    private TextView textView;
    private BroadcastReceiver broadcastReceiver;
    private TextView textView1 ;
    private TextView textView3;

    public String getLocName;

    public String getLat;
    public String getLong;

    public double DGetlat;
    public double DGetlong;

    public double latD;
    public double longD;

    public double mylat;
    public double mylong;
    public String lats;
    public String longs;

    GoogleMap myGoogleMap;
    GoogleApiClient myGoogleApiClient;

    private static final String URL = "http://iwillinmy.com/trackercoordUpdate.php";

    Button addDB;

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    textView.append("\n" + intent.getExtras().get("coordinates"));
                    String getCoord = textView.getText().toString();

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    public void onConnected() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        textView1= (TextView) findViewById(R.id.textView2);

        track = (Button) findViewById(R.id.button2);

        getData();

        enable_buttons();

        if (!runtime_permissions())
            enable_buttons();


    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    private void enable_buttons() {

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    locate(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getData(){
        String id = textView1.getText().toString().trim();
        if (id.equals("")) {
           // Toast.makeText(this, "Please enter an id", Toast.LENGTH_LONG).show();
            return;
        }


        String url = fetchcoord.DATA_URL;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(MainActivity.this,"",Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
    private void showJSON(String response){
        String lat="";
        String longi="";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(fetchcoord.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            lat = collegeData.getString(fetchcoord.LATITUDE);
            longi = collegeData.getString(fetchcoord.LONGITUDE);

            getLat = lat;
            getLong = longi;


            latD = Double.valueOf(getLat);
            longD = Double.valueOf(getLong);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textView1.setText("Child's Location: "+""+getLocName);

    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            } else {
                runtime_permissions();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        myGoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        myGoogleMap.setMyLocationEnabled(true);


        myGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        myGoogleApiClient.connect();
    }



    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        myGoogleMap.moveCamera(update);
    }


    private void goTomyLoc(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        myGoogleMap.moveCamera(update);
        //Toast.makeText(this, "?????", Toast.LENGTH_LONG).show();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeNormal:
                myGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                myGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                myGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                myGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    LocationRequest myLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        myLocationRequest = LocationRequest.create();
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        myLocationRequest.setInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(myGoogleApiClient, myLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng myloc1 = new LatLng(location.getLatitude(), location.getLongitude());
//            latD = Double.valueOf(getLat);
            //     longD = Double.valueOf(getLong);

            mylat = location.getLatitude();
            mylong = location.getLongitude();
            double myLongD = ((int) Math.round(mylong * 1E6)) / 1E6;
            double myLatD = ((int) Math.round(mylat * 1E6)) / 1E6;
            longs = String.valueOf(myLongD);
            lats = String.valueOf(myLatD);


            getData();
            LatLng newlalo = new LatLng(latD,longD);

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(newlalo, 15);


            MarkerOptions options1 = new MarkerOptions()
                    .title("You are here")
                    .position(myloc1);

            MarkerOptions options2 = new MarkerOptions()
                    .title("Child here")
                    .position(new LatLng(latD,longD));

            Toast.makeText(MainActivity.this,"Latitude: "+lats+" Longitude: "+longs,Toast.LENGTH_LONG).show();


            myGoogleMap.addMarker(options1);
            myGoogleMap.addMarker(options2);

            myGoogleMap.animateCamera(update);


            enable_buttons();

            sendCoord();

        }

    }
    public void locate(View view)throws IOException{
        Geocoder gc = new Geocoder(this);
        String location = "Paris";

        List<android.location.Address> list = gc.getFromLocation(latD ,longD,1);
        try {
            android.location.Address address = list.get(0);
            if (list != null && list.size() > 0) {

                StringBuilder strAddress = new StringBuilder();

                // getLocName = address.getLocality();

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    strAddress.append(address.getAddressLine(i)).append("\n");
                }
                getLocName = " " + strAddress.toString();

            } else {


            }
        }
        catch(Exception e){


        }

    }

    public void sendCoord(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Internet is disabled, please enable wifi or mobile data!",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                //List<String,String> params = new HashMap<String, String>();
                Map<String,String> params = new HashMap<>();

                params.put("mylat",lats);
                params.put("mylo", longs);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {

    }
}
