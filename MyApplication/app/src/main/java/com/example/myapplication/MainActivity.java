package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN ACTIVITY";
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MAIN ACTIVITY", "hehehe");
        if (savedInstanceState != null) {
            text = savedInstanceState.getString("text");
            final TextView helloTextView = (TextView) findViewById(R.id.textView);
            helloTextView.setText(text);
        }
        Log.i(TAG, "onCreate");
        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Masti");
        arrayList.add("Sapun");
        arrayList.add("Faina");
        arrayList.add("Ulei");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) list.getItemAtPosition(position);
                text = clickedItem;
                final TextView helloTextView = (TextView) findViewById(R.id.textView);
                helloTextView.setText(clickedItem);
            }
        });
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String color = pref.getString("color", "0");
        Log.i(TAG, color);
        View mview;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Do some stuff
            mview = findViewById(R.id.land);
        } else {
            mview = findViewById(R.id.main_view);
        }
        switch (color) {
            case "Blue":
                mview.setBackgroundColor(Color.BLUE);
                break;
            case "Red":
                mview.setBackgroundColor(Color.RED);
                break;
            case "White":
                mview.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String color = pref.getString("color", "White");
        View mview;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Do some stuff
            mview = findViewById(R.id.land);
        } else {
            mview = findViewById(R.id.main_view);
        }
        switch (color) {
            case "Blue":
                mview.setBackgroundColor(Color.BLUE);
                break;
            case "Red":
                mview.setBackgroundColor(Color.RED);
                break;
            case "White":
                mview.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text", text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item3) {
            Log.i(TAG, "item1");
            openActivity2();
        } else if (item.getItemId() == R.id.item1) {
            openDialog();
        } else if (item.getItemId() == R.id.item2) {
            openSettings();
        }
        else if (item.getItemId()==R.id.item4){
            openSensors();
        }
        return true;
    }
    public void openSensors(){
        Intent intent=new Intent(this, Main4Activity.class);
        startActivity(intent);
    }
    public void openSettings() {
        Intent intent = new Intent(this, Activity3.class);
        startActivity(intent);
    }

    public void openActivity2() {
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(dialogView);
        final EditText email = (EditText) dialogView.findViewById(R.id.editText2);
        final EditText pass = (EditText) dialogView.findViewById(R.id.editText3);
        Button login = (Button) dialogView.findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.i(TAG, email.getText().toString());
                Log.i(TAG, pass.getText().toString());

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
