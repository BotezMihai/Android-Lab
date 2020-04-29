package com.example.guessthelogo;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import static android.widget.ImageView.ScaleType.CENTER;

public class NewGameActivity extends AppCompatActivity {
    private static final String TAG = "NewGameActivity";
    private String[] questions;
    private String[] answers;
    private ArrayList<Quiz> quiz;
    private int currentIndex = 0;
    private int points = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }
    void startNewGame(){
        Intent newGame=new Intent(NewGameActivity.this, NewGameActivity.class);
        startActivity(newGame);
        this.finish();
    }
    void shareHighScore(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I have " + String.valueOf(points) + " " + " points in GuessTheLogo!");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
    void getHighScore(){
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("HighScore");
        builder.setMessage("The highest score is: "+message);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
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
        setContentView(R.layout.activity_new_game);
        Button share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePoints();
            }
        });
        share.setVisibility(ViewGroup.GONE);
        TextView message = findViewById(R.id.info3);
        message.setVisibility(ViewGroup.GONE);
        final EditText editText = findViewById(R.id.editText);
        if (savedInstanceState != null) {
            points = savedInstanceState.getInt("points");
            currentIndex = savedInstanceState.getInt("currentIndex");
            quiz = savedInstanceState.<Quiz>getParcelableArrayList("quiz");
        } else {
            quiz = new ArrayList<>();
            questions = getResources().getStringArray(R.array.logos);
            answers = getResources().getStringArray(R.array.answers);

            for (int index = 0; index < questions.length; index++) {
                quiz.add(new Quiz(questions[index], answers[index]));
            }
            Collections.shuffle(quiz);
        }
        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Integer imgResId = getResources().getIdentifier(quiz.get(currentIndex).getQuestion(), "drawable", NewGameActivity.this.getPackageName());
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(5, 0xFF000000);
        imageView.setBackground(border);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setPadding(5,5,5,5);
        imageView.setImageResource(imgResId);
        LinearLayout linearLayout = findViewById(R.id.newGame);
        linearLayout.addView(imageView);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editText.clearFocus();
                    View view = getCurrentFocus();
                    int orientation = getResources().getConfiguration().orientation;
                    if (view != null && orientation== Configuration.ORIENTATION_LANDSCAPE) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    }
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
                            editText.setVisibility(ViewGroup.GONE);
                            linearLayout.setVisibility(ViewGroup.GONE);
                            share.setVisibility(View.VISIBLE);
                            message.setVisibility(View.VISIBLE);
                            message.setText("You answered correctly at " + String.valueOf(points) + " questions\n Share your progress!");
                            editText.setOnEditorActionListener(null);

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

    private void checkAnswer(String text) {
        if (text.equals(quiz.get(currentIndex).getAnswer().toLowerCase())) {
            points += 1;
            Toast.makeText(NewGameActivity.this, Quiz.messageCorrectAnswer(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(NewGameActivity.this, Quiz.messageWrongAnswer(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sharePoints() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I got " + String.valueOf(points) + " " + " points in GuessTheLogo!");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void exitNewGameActivity() {
        Intent exit = new Intent(NewGameActivity.this, MainActivity.class);
        startActivity(exit);
    }

    private void highScore() {
        ArrayList<Integer> scores;
        SharedPreferences sharedPreferences = getSharedPreferences("highScores", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("scores", null);
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        scores = gson.fromJson(json, type);
        if (scores == null) {
            scores = new ArrayList<Integer>();
            savePoints(-1, scores);
        } else {
            Integer mini = scores.stream().min((a, b) -> (a - b)).get();
            if (mini < points) {
                savePoints(mini, scores);
            } else if (mini > points && scores.size() < 3) {
                savePoints(-1, scores);
            }
        }
    }

    private void savePoints(Integer mini, ArrayList<Integer> scores) {
        boolean delete = false;
        if (mini == -1)
            delete = true;
        else if (scores.size() == 3) {
            delete = true;
        }
        if (delete == true) {
            scores.remove(mini);
        }
        scores.add(points);
        scores = (ArrayList<Integer>) scores.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        SharedPreferences sharedPreferences = getSharedPreferences("highScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String dataToSave = gson.toJson(scores);
        editor.putString("scores", dataToSave);
        editor.apply();
        Toast.makeText(NewGameActivity.this, "High score saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("points", points);
        outState.putInt("currentIndex", currentIndex);
        outState.putParcelableArrayList("quiz", quiz);
    }
}
