package com.example.mihai.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity2 extends AppCompatActivity {
    private EditText EditText;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        EditText = findViewById(R.id.editText);
        send = findViewById(R.id.button2);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                send();
            }
        });
    }

    private void send() {
        EditText = findViewById(R.id.editText);
        String msg=EditText.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:" + "43534534534"));
//        startActivity(callIntent);
//        Uri webpage = Uri.parse("tel:"+ "07643463437");
//        Intent webIntent = new Intent(Intent.ACTION_DIAL, webpage);
//        Intent shareIntent=Intent.createChooser(webIntent, "Deschie fraiere link ul cu:");
//        startActivity(webIntent);
    }

}
