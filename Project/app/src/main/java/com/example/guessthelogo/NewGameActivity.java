package com.example.guessthelogo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

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
        }

        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Integer imgResId = getResources().getIdentifier(quiz.get(currentIndex).getQuestion(), "drawable", NewGameActivity.this.getPackageName());
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
