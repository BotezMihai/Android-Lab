package com.example.guessthelogo;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }
    void startNewGame(){
        Intent newGame=new Intent(MainActivity.this, NewGameActivity.class);
        startActivity(newGame);

    }
    void shareHighScore(){
        String points=getScore();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I got " + String.valueOf(points) + " " + " points in GuessTheLogo!");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
    void getHighScore(){

        String message=getScore();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("HighScore");
        builder.setMessage("The highest score is: "+message);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private String getScore(){
        ArrayList<Integer> scores;
        SharedPreferences sharedPreferences = getSharedPreferences("highScores", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("scores", null);
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        scores = gson.fromJson(json, type);
        String message;
        if(scores==null){
            message="There's no score!";
        } else {
            message=String.valueOf(scores.get(0));
        }
        return message;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                startNewGame();
                return true;
            case R.id.item2:
                getHighScore();
                return true;
            case R.id.item3:
                shareHighScore();
                return true;
        }
        return true;
    }
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
