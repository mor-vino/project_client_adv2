package com.example.mor.final_project_client_adv2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    // sensors
    SwipeRefreshLayout swipeRL;
    SensorManager mSensorManager;
    float last_x, last_y, last_z;
    private long lastUpdate;
    private float mAccel; // acceleration apart from gravity

    private float mAccelCurrent; // current acceleration including gravity

    private float mAccelLast; // last acceleration including gravity
    private static final int SHAKE_THRESHOLD = 6000;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    private ArrayList<ChannelItem> channelItemsList;

    private ServerInfo si;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        this.channelItemsList = new ArrayList<ChannelItem>();
        lastUpdate = System.currentTimeMillis();
        if(!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        // if the machine's orientation is portrait
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            TextView swiper = (TextView)findViewById(R.id.act_maps_swipe_text_view_id);
            swiper.setOnTouchListener(new OnSwipeTouchListener(MapsActivity.this) {
                @Override
                public void onSwipeLeft() {
                    setFragment();
                }

                @Override
                public void onSwipeRight() {
                    setFragment();
                }
            });
            Button menuBtn = (Button) findViewById(R.id.act_maps_menu_btn_id);
            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment menuFragment = new MenuFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    fm.popBackStack();

                    ft.add(R.id.act_maps_menu_layout_id, menuFragment);
                    ft.addToBackStack("");
                    ft.commit();
                }
            });
            checkRefresh();
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensorManager.registerListener(mSensorListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
            mAccel = 0.00f;
            mAccelCurrent = SensorManager.GRAVITY_EARTH;
            mAccelLast = SensorManager.GRAVITY_EARTH;
        }
        // if the machine's orientation is landscape
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Fragment chanelsListFragment = new ChannelListFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fm.popBackStack();

            Bundle chanArrBundle = new Bundle();
            // add the list to the bundle
            chanArrBundle.putSerializable("channelsList", this.channelItemsList);
            // set the bundle as arguments to the channels fragment
            chanelsListFragment.setArguments(chanArrBundle);

            ft.add(R.id.act_maps_land_channels_list, chanelsListFragment);
            ft.addToBackStack(null);
            ft.commit();
            SharedPreferences sp = getSharedPreferences("MyServer", MODE_PRIVATE);
            new GetMyChannels(this).execute("http://" + sp.getString("serverName", "mpti-2048") + ".appspot.com/getMyChannels");
        }

    }

    private void checkRefresh() {
        // set a "down swiper" to refresh the page
        swipeRL = (SwipeRefreshLayout) findViewById(R.id.act_maps_refresh_layout);
        swipeRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intentRefresh = new Intent(MapsActivity.this, ReloadService.class);
                startService(intentRefresh);
            }
        });
        swipeRL.setColorScheme(android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark);
        IntentFilter intentF= new IntentFilter();
        intentF.addAction(ReloadService.DONE);
        registerReceiver(reloadDone, intentF);
    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.act_maps_mapFragment_id))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            }
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        try {
            mSensorManager.unregisterListener(mSensorListener);
            unregisterReceiver(reloadDone);
        }
        catch (Exception e){}
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(32, 35)).title("Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    private void setFragment() {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            FragmentManager fm1 = getSupportFragmentManager();
            FragmentTransaction ft1 = fm1.beginTransaction();
            fm1.popBackStack();
            ft1.add(R.id.act_maps_channel_list_layout_portrait_id, new ChannelListFragment());
            ft1.addToBackStack("sf");
            ft1.commit();
        }
    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Starting location updates", Toast.LENGTH_SHORT).show();
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(final Location location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(17)
                        .bearing(90)
                        .tilt(30)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });
        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("my location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to connect to location updates", Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        mSensorManager.unregisterListener(this.mSensorListener);
    }

    protected void stopLocationUpdates() {
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        setUpMapIfNeeded();
        /*
        sensorManager.registerListener(
                this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                sensorManager.SENSOR_DELAY_NORMAL);*/
    }

    // "listen" to the DONE broadcast
    private BroadcastReceiver reloadDone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            swipeRL.setRefreshing(false);
            Toast.makeText(MapsActivity.this,"Reload is done",Toast.LENGTH_SHORT).show();
        }
    };
    /**
     * shows the reload by starting a reload service. When the five seconds
     * are complete it looks for the finishReload and unregister.
     */
    public void showReload() {
        Intent service = new Intent(this ,ReloadService.class);
        startService(service);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ReloadService.DONE);

        registerReceiver(reloadDone, filter);
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 16) {
                swipeRL.setRefreshing(true);
                showReload();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }


    };
    // the class responsible on getting the list of MY channels from server
}
