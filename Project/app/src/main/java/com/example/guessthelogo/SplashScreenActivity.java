package com.example.guessthelogo;

import android.graphics.Color;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EasySplashScreen config=new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withSplashTimeOut(2500)
                .withTargetActivity(MainActivity.class)
                .withLogo(R.mipmap.logo)
                .withBeforeLogoText("It's quizz time");
//        config.getBeforeLogoTextView().setTextColor(Color.CYAN);
        config.getBeforeLogoTextView().setTextColor(getColorStateList(R.color.orangeCustom));
        config.getBeforeLogoTextView().setIncludeFontPadding(true);
        config.getBeforeLogoTextView().setTextSize((float) 50);
        View easySplashScreen=config.create();
        setContentView(easySplashScreen);
    }
}
