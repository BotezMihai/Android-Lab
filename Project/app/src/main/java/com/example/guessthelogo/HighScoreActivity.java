package com.example.guessthelogo;

import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        loadHighScores();
    }
    void loadHighScores(){
        ArrayList<Integer> scores;
        SharedPreferences sharedPreferences = getSharedPreferences("highScores", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("scores", null);
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        scores = gson.fromJson(json, type);
        if(scores!=null){
            final ListView list = findViewById(R.id.listView);
            ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, R.layout.custom_layout, scores);
            list.setAdapter(arrayAdapter);
        }

    }
}
