package com.project.navigation;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CurrentLocationSystemPage extends AppCompatActivity {
    private Button Return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location_system_page);
        Return=(Button)findViewById(R.id.Return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CurrentLocationSystemPage.this, Dashboard.class);
                startActivity(intent);
                finish();
                return;


            }
        });
    }
}