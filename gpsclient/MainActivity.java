package com.example.itb.gpsclient;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.renderscript.Double2;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.content.BroadcastReceiver;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //  private Button b;
    // private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;


    private BroadcastReceiver broadcastReceiver;

    private String coord = "hello";
    //  private static float lo;
    //  private static float lat;
    public String lo;
    public String lat;

    public String getInterval;
    public Integer intervalnum;


    private BroadcastReceiver myReceiver;

    private static final String URL = "http://iwillinmy.com/coordUpdate.php";

    private static final String URL2 = "http://iwillinmy.com/testCoord.php";


    final String latAsString = String.valueOf(lat);
    final String loAsString = String.valueOf(lo);

    public static final String lati = "la1";
    public static final String longi = "lo1";

    final String lo1 = "-6.2422709";
    final String la1 = "53.3243201";

    Button buttoncall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttoncall = (Button) findViewById(R.id.button2);
        // sendSMSMessage();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        buttonCall();

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // t.append("\n " + location.getLongitude() + " " + location.getLatitude());
                double loD = location.getLongitude();
                double latD = location.getLatitude();

                double newlod = ((int) Math.round(loD * 1E6)) / 1E6;
                double newlatD = ((int) Math.round(latD * 1E6)) / 1E6;


                lo = String.valueOf(newlod);
                lat = String.valueOf(newlatD);


                sendCoord();
                // sendtorecords();

                Toast.makeText(getApplicationContext(), "Longitude :" + lat + "Latitude: " + lo, Toast.LENGTH_LONG).show();

                getLo(loD);
                getLat(latD);

                //Toast.makeText(getApplicationContext(), "Current speed:" + location.getSpeed(), Toast.LENGTH_SHORT).show();

                if (location.getSpeed() <= 0) {
                    //  Toast.makeText(getApplicationContext(), "Current speed alert!:", Toast.LENGTH_SHORT).show();

                }

                //Toast.makeText(getApplicationContext(),coord, Toast.LENGTH_LONG).show();

                // Toast.makeText(getApplicationContext(), "Now Tracking!!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }

        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, listener);

        configure_button();
        // Toast.makeText(this,coord, Toast.LENGTH_LONG).show();
    }


    public void getLo(Double L) {
        String loAstring = String.valueOf(L);

    }

    public void buttonCall() {


        buttoncall.setOnLongClickListener(new View.OnLongClickListener(){


            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this,"Making the call",Toast.LENGTH_LONG).show();
                makecall();
                return false;
            }




        });

    }
    public void makecall(){

        String number = "080032345";
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }


    public void getLat(Double L) {
        //L = lat;
        String latAsString = String.valueOf(L);

    }

    public void onResume() {
        super.onResume();
        startService();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SHUTDOWN);
        myReceiver = new Shutdownreciever();
        registerReceiver(myReceiver, filter);
    }

    public void onPause() {
        super.onPause();

        this.unregisterReceiver(myReceiver);
    }

    public void onDestroy() {

        super.onDestroy();

    }

    public void startService() {

        Intent intent = new Intent(this, backgroundService.class);
        startService(intent);
    }

    public void sendCoord() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        // Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Internet is turned off! Please enable mobile data or wifi", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //List<String,String> params = new HashMap<String, String>();
                Map<String, String> params = new HashMap<String, String>();

                params.put("la1", lat);
                params.put("lo1", lo);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void sendtorecords() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        // Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Internet is turned off! Please enable mobile data or wifi", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //List<String,String> params = new HashMap<String, String>();
                Map<String, String> params = new HashMap<String, String>();

                params.put("lattest", lat);
                params.put("longtest", lo);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    String url = fetchdata.DATA_URL;

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

//    RequestQueue requestQueue = Volley.newRequestQueue(this);
    //requestQueue.add(stringRequest);



    private void showJSON(String response) {
        String trackstat = "";
        String interval = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(fetchdata.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            interval = collegeData.getString(fetchdata.INTERVAL);
            //longi = collegeData.getString(fetchcoord.LONGITUDE);

            getInterval = interval;

            intervalnum = Integer.valueOf(getInterval);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    // @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // checking for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
    }



}

