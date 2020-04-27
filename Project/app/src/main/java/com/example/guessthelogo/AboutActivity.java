package com.example.guessthelogo;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {
    private String[] logos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textView =findViewById(R.id.info2);
        logos = getResources().getStringArray(R.array.logos);
        textView.setText(String.valueOf(logos.length));

    }
}
