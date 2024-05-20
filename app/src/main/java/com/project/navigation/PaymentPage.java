package com.project.navigation;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentPage extends AppCompatActivity {

    private Button Return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        Return = (Button) findViewById(R.id.Return);

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaymentPage.this, ProfilePage.class);
                startActivity(intent);
                finish();
                return;


            }
        });
    }
}