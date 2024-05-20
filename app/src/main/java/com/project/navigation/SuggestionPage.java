package com.project.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuggestionPage extends AppCompatActivity {
    private EditText nameEditText, plateNumberEditText, suggestionEditText;
    private Button submitButton, returnButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_page);

        // Initialize Firebase
        FirebaseDatabase.getInstance(); // Enable local persistence (optional)
        databaseReference = FirebaseDatabase.getInstance().getReference("suggestions");

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        plateNumberEditText = findViewById(R.id.plateNumberEditText);
        suggestionEditText = findViewById(R.id.suggestionEditText);
        submitButton = findViewById(R.id.submitButton);
        returnButton = findViewById(R.id.returnButton);

        // Set click listener for submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String name = nameEditText.getText().toString().trim();
                String plateNumber = plateNumberEditText.getText().toString().trim();
                String suggestion = suggestionEditText.getText().toString().trim();

                // Check if any field is empty
                if (name.isEmpty() || plateNumber.isEmpty() || suggestion.isEmpty()) {
                    Toast.makeText(SuggestionPage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new Suggestion object
                Suggestion newSuggestion = new Suggestion(name, plateNumber, suggestion);

                // Push the suggestion to Firebase database
                databaseReference.push().setValue(newSuggestion);

                // Show a toast message
                Toast.makeText(SuggestionPage.this, "Suggestion submitted successfully", Toast.LENGTH_SHORT).show();

                // Clear input fields
                nameEditText.setText("");
                plateNumberEditText.setText("");
                suggestionEditText.setText("");
            }
        });

        // Set click listener for return button


        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SuggestionPage.this, Dashboard.class);
                startActivity(intent);
                finish();
                return;
            }

        });

    }
}