package com.project.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }

                                    }
        );

        Return.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, LoginPage.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }

                                    }
        );
    }
}