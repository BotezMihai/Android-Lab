package com.example.guessthelogo;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.guessthelogo.Quiz;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class NewGameActivity extends AppCompatActivity {
    private static final String TAG = "NewGameActivity";
    private String[] questions;
    private String[] answers;
    private ArrayList<Quiz> quiz;
    private int currentIndex = 0;
    private int points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        final EditText editText = findViewById(R.id.editText);

        quiz = new ArrayList<>();
        questions = getResources().getStringArray(R.array.logos);
        answers = getResources().getStringArray(R.array.answers);

        for (int index = 0; index < questions.length; index++) {
            quiz.add(new Quiz(questions[index], answers[index]));

        }

        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Integer imgResId = getResources().getIdentifier(quiz.get(0).getQuestion(), "drawable", NewGameActivity.this.getPackageName());
        imageView.setImageResource(imgResId);
        LinearLayout linearLayout = findViewById(R.id.newGame);
        linearLayout.addView(imageView);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = editText.getText().toString().trim().toLowerCase();
                    if (currentIndex == 0) {
                        checkAnswer(text);
                        editText.setText("");
                        currentIndex += 1;
                        int id = getResources().getIdentifier(quiz.get(currentIndex).getQuestion(), "drawable", NewGameActivity.this.getPackageName());
                        imageView.setImageResource(id);
                    } else {
                        checkAnswer(text);
                        editText.setText("");
                        currentIndex += 1;
                        if (currentIndex == quiz.size()) {
                            highScore();
                            imageView.setVisibility(ViewGroup.GONE);
                            Toast.makeText(NewGameActivity.this, String.valueOf(points)+" "+"points!", Toast.LENGTH_SHORT).show();
                            editText.setOnEditorActionListener(null);
                            sharePoints();
                            return true;
                        }
                        int id = getResources().getIdentifier(quiz.get(currentIndex).getQuestion(), "drawable", NewGameActivity.this.getPackageName());
                        imageView.setImageResource(id);
                    }
                }
                return true;
            }
        });
    }
    private void checkAnswer(String text){
        if (text.equals(quiz.get(currentIndex).getAnswer().toLowerCase())) {
            points += 1;
            Toast.makeText(NewGameActivity.this, Quiz.messageCorrectAnswer(), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(NewGameActivity.this, Quiz.messageWrongAnswer(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sharePoints(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Share");
        builder.setMessage("Share your points");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent sendIntent=new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I got "+String.valueOf(points)+" "+" points in GuessTheLogo!");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitNewGameActivity();
            }
        });
        builder.setCancelable(false);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    private void exitNewGameActivity(){
        Intent exit=new Intent(NewGameActivity.this, MainActivity.class);
        startActivity(exit);
    }

    private void highScore(){
        ArrayList<Pair<String, Integer>> scores;
        SharedPreferences sharedPreferences = getSharedPreferences("highScores",MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString("scores", null);
        Type type=new TypeToken<ArrayList<Pair<String, Integer>>>(){}.getType();
        scores=gson.fromJson(json, type);
        if(scores==null){
            Log.d(TAG,"nulll");
            scores=new ArrayList<Pair<String, Integer>>();
            scores.add(new Pair<String, Integer>("test", points));
        } else {
           Pair<String, Integer> mini= scores.stream().min((a, b)->(a.second-b.second)).get();
           Log.d(TAG, String.valueOf(mini.second)+"  "+"minul");
            scores.add(new Pair<String, Integer>("test", points));
            Log.d(TAG,"Altfel");
        }
        SharedPreferences.Editor editor= sharedPreferences.edit();
        String dataToSave=gson.toJson(scores);
        editor.putString("scores", dataToSave);
        editor.apply();



    }
}
