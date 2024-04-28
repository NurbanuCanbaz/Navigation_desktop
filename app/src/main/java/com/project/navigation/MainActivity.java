package com.project.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button Lifetime, OptimalRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Lifetime=(Button) findViewById(R.id.LifetimeOriginal);
        OptimalRoute=(Button) findViewById(R.id.OptimalRoute);

        Lifetime.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, LifetimeOriginal.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }

                                    }
        );

        OptimalRoute.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, OptimalRoute.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }

                                    }
        );
    }
}