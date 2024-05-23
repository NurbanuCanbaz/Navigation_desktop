package com.project.navigation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.project.navigation.R;

public class MainActivity extends AppCompatActivity {
    //private Button Dashboard ,Return;
    private TextView appName;
    private LottieAnimationView lottie,lottie2;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dashboard = (Button) findViewById(R.id.Dashboard);
        //Return = (Button) findViewById(R.id.Return);
        appName = (TextView) findViewById(R.id.RouteMApp);
        lottie = findViewById(R.id.lottie);
        lottie2 = findViewById(R.id.lottie2);

        //appName.animate().translationY(-1400).setDuration(2700).setStartDelay(0);
        //lottie.animate().translationX(2000).setDuration(2000).setStartDelay(2900);
        mp =  MediaPlayer.create(MainActivity.this,R.raw.backvoicewel);
        mp.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent (getApplicationContext(), SignInActivity.class);
                startActivity(i);
                mp.stop();

            }
        }, 7000);

    }

    @Override
    protected void onDestroy() {

        //mp.release();

        super.onDestroy();
    }
}