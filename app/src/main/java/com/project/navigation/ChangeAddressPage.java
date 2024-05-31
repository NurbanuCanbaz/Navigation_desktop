package com.project.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.project.navigation.utilities.Constants;
import com.project.navigation.utilities.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeAddressPage extends AppCompatActivity {

    private EditText newAddressEditText;
    private Button saveChangesButton, Return;
    private DatabaseReference addressRef;
    public String newAddresses;
    private String userId; // User ID obtained from PreferenceManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address_page);

        // Initialize views
        newAddressEditText = findViewById(R.id.newAddressEditText);
        saveChangesButton = findViewById(R.id.saveChangesButton);
        Return = findViewById(R.id.Return);

        // Initialize Firebase reference
        addressRef = FirebaseDatabase.getInstance().getReference().child("userAddresses");

        // Get the user ID from SharedPreferences
        userId = new PreferenceManager(getApplicationContext()).getString(Constants.KEY_USER_ID);

        // Set onClickListener for Save Changes button
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeAddressPage.this, ProfilePage.class);
                startActivity(intent);
                finish();
            }
        });


    }
    private void saveChanges() {
        String newAddress = newAddressEditText.getText().toString().trim();

        if (!newAddress.isEmpty()) {

            SharedPreferences preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("address", newAddress);
            editor.commit(); // Use apply() for asynchronous save, commit() for synchronous save
            geocodeAddress(newAddress);

        } else {
            Toast.makeText(ChangeAddressPage.this, "Please enter a new address", Toast.LENGTH_SHORT).show();
        }


    }

    private void geocodeAddress(String address) {
        // Set your Mapbox access token here
        String accessToken = "sk.eyJ1IjoiemV5Z25jeSIsImEiOiJjbHdvbzNrdzYxMGJyMmpyejJ5cjc1YWlhIn0.oj2CyXZbYL6BKDEkctFXAg";

        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(accessToken)
                .query(address)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CarmenFeature feature = response.body().features().get(0);
                    Point point = feature.center();
                    double latitude = point.latitude();
                    double longitude = point.longitude();

                    // Save the address and coordinates to the database
                    saveCoordinatesToDatabase(address, latitude, longitude);

                } else {
                    Toast.makeText(ChangeAddressPage.this, "No location found for the given address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable t) {
                Toast.makeText(ChangeAddressPage.this, "Geocoding request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveCoordinatesToDatabase(String address, double latitude, double longitude) {
        // Save the address and coordinates to the Firebase Realtime Database
        Map<String, Object> addressData = new HashMap<>();
        addressData.put("address", address);
        addressData.put("latitude", latitude);
        addressData.put("longitude", longitude);

        // Save to the database under the user's ID
        DatabaseReference userAddressRef = addressRef.child(userId);
        userAddressRef.setValue(addressData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChangeAddressPage.this, "Address saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangeAddressPage.this, "Failed to save address: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
           });
    }
}