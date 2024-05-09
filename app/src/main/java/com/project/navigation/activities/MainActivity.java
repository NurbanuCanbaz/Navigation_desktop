package com.project.navigation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.navigation.LoginPage;
import com.project.navigation.R;

public class MainActivity extends AppCompatActivity {
    private Button Dashboard ,Return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dashboard=(Button) findViewById(R.id.Dashboard);
        Return=(Button) findViewById(R.id.Return);


        Dashboard.setOnClickListener(new View.OnClickListener() {
                                        @Override

                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, com.project.navigation.Dashboard.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }

                                    }
        );

        Return.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }

                                    }
        );
    }
}