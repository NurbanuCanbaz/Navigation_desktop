package com.project.navigation;


import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.navigation.activities.ChatBoxMain;
import com.project.navigation.activities.SignInActivity;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;

public class Dashboard extends AppCompatActivity {

    private ImageView  Notification, LifeTime, OptimalRoute, ChatBot, CurrentLocation, Profile, Suggestion, HowTo;
    private Button Return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Return = (Button) findViewById(R.id.Return);
        Notification = (ImageView) findViewById(R.id.Notification);
        LifeTime = (ImageView) findViewById(R.id.LifeTime);
        OptimalRoute = (ImageView) findViewById(R.id.OptimalRoute);
        ChatBot = (ImageView) findViewById(R.id.Chat);
        CurrentLocation = (ImageView) findViewById(R.id.CurrentLocation);
        Profile = (ImageView) findViewById(R.id.Profile);
        Suggestion = (ImageView) findViewById(R.id.Suggestion);
        HowTo = (ImageView) findViewById(R.id.HowTo);


        OptimalRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentOptimalRoute = new Intent(Dashboard.this, OptimalRoute.class);
                startActivity(intentOptimalRoute);
                finish();
                return;


            }
        });




        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentProfile = new Intent(Dashboard.this, ProfilePage.class);
                startActivity(intentProfile);
                finish();
                return;


            }
        });




        Suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentSuggestion = new Intent(Dashboard.this, SuggestionPage.class);
                startActivity(intentSuggestion);
                finish();
                return;


            }
        });



        HowTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentHowTo = new Intent(Dashboard.this, HowToSystemPage.class);
                startActivity(intentHowTo);
                finish();
                return;


            }
        });



        CurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentCurrLoc = new Intent(Dashboard.this, CurrentLocationSystemPage.class);
                startActivity(intentCurrLoc);
                finish();
                return;


            }
        });


        ChatBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentChat = new Intent(Dashboard.this, ChatBoxMain.class);
                startActivity(intentChat);
                finish();
                return;


            }
        });


        LifeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentLifeTime = new Intent(Dashboard.this, LifetimeOriginal.class);
                startActivity(intentLifeTime);
                finish();
                return;


            }
        });



        Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentNotify = new Intent(Dashboard.this, NotificationSystemPage.class);
                startActivity(intentNotify);
                finish();
                return;


            }
        });

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentReturn = new Intent(Dashboard.this, SignInActivity.class);
                startActivity(intentReturn);
                finish();
                return;


            }
        });



    }




}