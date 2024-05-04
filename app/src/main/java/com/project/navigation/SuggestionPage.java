package com.project.navigation;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SuggestionPage extends AppCompatActivity {
    private Button SuggestionReturn;
    private EditText  vehiclePlateNumber, feedback,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_page);

        name = (EditText) findViewById(R.id.editTextText2);
        vehiclePlateNumber =(EditText) findViewById(R.id.platetext);
        feedback = (EditText) findViewById(R.id.typesuggestion);
        SuggestionReturn=(Button) findViewById(R.id.Return);

        SuggestionReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name_sug=name.getText().toString();
                final String vehicle_num_sug=vehiclePlateNumber.getText().toString();
                final String feedback_sug=feedback.getText().toString();

                Intent intent = new Intent(SuggestionPage.this, Dashboard.class);
                startActivity(intent);
                finish();
                return;


            }
        });

    }




}