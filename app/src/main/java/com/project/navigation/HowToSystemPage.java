package com.project.navigation;



import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

public class HowToSystemPage extends AppCompatActivity {
    private Button HowToReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_system_page);

        // Access button elements
        HowToReturn=(Button) findViewById(R.id.Return);
        Button button5 = findViewById(R.id.button5);
        Button button8 = findViewById(R.id.button8);
        SearchView searchView = findViewById(R.id.search);

        // Set video URLs (if applicable)
        String videoUrl1 = "https://www.youtube.com/watch?v=cIfrH0svpZc";

        // Implement click listeners
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl1));
                startActivity(intent);
            }
        });

        // Repeat the same pattern for button6 and button7 (if opening videos)

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement navigation to Home Page
                // (Replace with your Home Page navigation logic)
            }
        });
        HowToReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HowToSystemPage.this, Dashboard.class);
                startActivity(intent);
                finish();
                return;


            }
        });

        // Implement search functionality (replace with your logic)
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search based on query
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search text changes
                return true;
            }
        });
    }
}