package com.project.navigation;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChangeAddressPage extends AppCompatActivity {
    private Button Return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address_page);



        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChangeAddressPage.this, Dashboard.class);
                startActivity(intent);
                finish();
                return;


            }
        });
    }
}