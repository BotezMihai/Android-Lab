package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.*;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.*;

public class Main4Activity extends AppCompatActivity implements SensorEventListener, LocationListener {
    private SensorManager sensorManager;
    List<Sensor> sensorList;
    private LocationManager locationManager;
    private int GPS = 1;
    private static final String TAG = "Main4Activity";
    private String longitudine;
    private String latitudine;
    private String altitudine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<String> sensors = new ArrayList<String>();

        for (Sensor s : sensorList) {
            String name = s.getName();
            sensors.add(name);
        }
        sensors.add("GET GPS COORDINATES");

        final ListView list = findViewById(R.id.list_activity4);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sensors);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                sensorManager.unregisterListener(Main4Activity.this);

                String clickedItem = (String) list.getItemAtPosition(position);
                String text = clickedItem;

                for (Sensor s : sensorList) {
                    if (s.getName().equals(text)) {
                        sensorManager.registerListener(Main4Activity.this, s, SensorManager.SENSOR_DELAY_NORMAL);
                    } else {
                        sensorManager.unregisterListener(Main4Activity.this, s);
                    }
                }

                if (text.equals("GET GPS COORDINATES")) {

                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(Main4Activity.this, "Permision denied", Toast.LENGTH_LONG).show();
                        requestPermissionsGps();
                    } else {
                        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                            final TextView helloTextView = (TextView) findViewById(R.id.textView3);
                            String textLocation = "GPS\n" + "Longitudine: " + loc.getLongitude() + "\n Latitudine: " + loc.getLatitude() + "\n Altitudine: " + loc.getAltitude();
                            helloTextView.setText(textLocation);
                        } else {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, Main4Activity.this);
                        }
                    }

                }


            }
        });
    }

    public void requestPermissionsGps() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.d(TAG, "aiivi rationale");
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GPS);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, GPS);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GPS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Main4Activity.this, "Permission granted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(Main4Activity.this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        String text = sensor.getName() + Arrays.toString(event.values);
        final TextView helloTextView = (TextView) findViewById(R.id.textView3);
        helloTextView.setText(text);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {

        locationManager.removeUpdates(this);
        //open the map:
        latitudine = String.valueOf(location.getLatitude());
        longitudine = String.valueOf(location.getLongitude());
        altitudine = String.valueOf(location.getAltitude());
        Toast.makeText(Main4Activity.this, "latitude:" + location.getLatitude() + " longitude:" +location.getLongitude() , Toast.LENGTH_SHORT).show();

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
    protected void onStart() {
        super.onStart();

    }
}
