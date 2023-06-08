package com.example.finalprojectamit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.finalprojectamit.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    Animation animation;
    Animation animation2;
    Timer timer;
    Timer timer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // sets the application screen as a full screen
        setContentView(R.layout.activity_splash);

        timer = new Timer();
        timer2 = new Timer();

        logo = findViewById(R.id.logo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in); // loading the first animation (fade in)
        logo.setAnimation(animation); // starting the animation on the logo

        timer.schedule(new TimerTask() { // starting a timer for 2.5seconds
            @Override
            public void run() {
                animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out); // loading the second animation (fade out)
                logo.setAnimation(animation2); // starting the second animation on the logo
            }
        }, 2500);
        timer2.schedule(new TimerTask() { //starting a new timer for 5 seconds
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class); // creating an intent to the log in screen
                startActivity(intent); // starting the intent
                finish();
            }
        }, 5000);

    }
}