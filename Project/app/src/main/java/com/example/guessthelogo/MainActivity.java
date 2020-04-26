package com.example.guessthelogo;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button newGame = findViewById(R.id.button);
        Button highScore = findViewById(R.id.button2);
        Button about = findViewById(R.id.button3);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNewGame = new Intent(MainActivity.this, NewGameActivity.class);
                startActivity(startNewGame);
            }
        });

        highScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent highScore = new Intent(MainActivity.this, HighScoreActivity.class);
                startActivity(highScore);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(about);
            }
        });
    }
}
