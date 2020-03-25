package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;

public class Activity2 extends AppCompatActivity {
    private EditText EditText;
    private Button send;
    private EditText EditTextSave;
    private Button save;
    private Button load;
    private static final String FILE = "msj.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        EditText = findViewById(R.id.editText);
        send = findViewById(R.id.button2);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendAction();
            }
        });

        save = findViewById(R.id.button3);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveAction();
            }
        });

        load = findViewById(R.id.button5);
        load.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadAction();
            }
        });
    }

    private void sendAction() {
        EditText = findViewById(R.id.editText);
        String msg = EditText.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void saveAction() {
        EditTextSave = findViewById(R.id.editText4);
        String msg = EditTextSave.getText().toString();
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE, MODE_PRIVATE);
            fos.write(msg.getBytes());
            EditTextSave.getText().clear();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadAction() {
        FileInputStream fis=null;

        try {
            fis=openFileInput(FILE);
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String text;
            while((text=br.readLine())!=null){
                sb.append(text).append("\n");
            }
            EditText.setText(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String color = pref.getString("color", "White");
        View mview;
        mview = findViewById(R.id.activity2_view);

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

}
