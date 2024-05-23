package com.project.navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class HowToSystemPage extends AppCompatActivity {

    private List<Button> buttons;
    private Button Return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_system_page);

        // Initialize buttons
        Button returnButton = findViewById(R.id.returnButton);
        Button markAbsentButton = findViewById(R.id.markAbsentButton);
        Button changeAddressButton = findViewById(R.id.changeAddressButton);
        Button changeLocationButton = findViewById(R.id.changeLocationButton);

        // Add buttons to the list for easy access during search
        buttons = new ArrayList<>();
        buttons.add(markAbsentButton);
        buttons.add(changeAddressButton);
        buttons.add(changeLocationButton);

        // Set click listeners for each button
        Return = (Button) findViewById(R.id.returnButton);

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HowToSystemPage.this, Dashboard.class);
                startActivity(intent);
                finish();
                return;


            }
        });

        markAbsentButton.setOnClickListener(v -> openYouTubeVideo("https://youtu.be/XaF42xeAjXg?si=QvoP4lYo0INTHyOm"));
        changeAddressButton.setOnClickListener(v -> openYouTubeVideo("https://www.youtube.com/watch?v=c3TVR54YxQ8&list=RDc3TVR54YxQ8&start_radio=1"));
        changeLocationButton.setOnClickListener(v -> openYouTubeVideo("https://youtu.be/TaktufqaSgY?si=Pr-vCuawz6SgOLob"));

        // Initialize and set up SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterButtons(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterButtons(newText);
                return false;
            }
        });

        // Set up the title TextView
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText("How To");
    }

    // Method to open a YouTube video based on its URL
    private void openYouTubeVideo(String videoUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        intent.putExtra("force_fullscreen", true); // Optional: force full screen mode
        startActivity(intent);
    }


    private void filterButtons(String query) {
        for (Button button : buttons) {
            if (button.getText().toString().toLowerCase().contains(query.toLowerCase())) {
                button.setVisibility(View.VISIBLE);
                System.out.println("Button '" + button.getText().toString() + "' is visible");
            } else {
                button.setVisibility(View.GONE);
                System.out.println("Button '" + button.getText().toString() + "' is hidden");
            }
        }
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        super.onBackPressed();
        startActivity(new Intent(HowToSystemPage.this, Dashboard.class));
        return;
    }

}