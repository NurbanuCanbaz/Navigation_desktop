package com.project.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeAddressPage extends AppCompatActivity {

    private EditText newAddressEditText;
    private Button saveChangesButton, Return;
    private DatabaseReference addressRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address_page);

        // Initialize views
        newAddressEditText = findViewById(R.id.newAddressEditText);
        saveChangesButton = findViewById(R.id.saveChangesButton);
        Return = (Button) findViewById(R.id.Return);

        // Initialize Firebase reference
        addressRef = FirebaseDatabase.getInstance().getReference().child("userAddresses");

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
                return;
            }

        });
    }

    private void saveChanges() {
        String newAddress = newAddressEditText.getText().toString().trim();

        if (!newAddress.isEmpty()) {
            geocodeAddress(newAddress);
        } else {
            Toast.makeText(ChangeAddressPage.this, "Please enter a new address", Toast.LENGTH_SHORT).show();
        }
    }

    private void geocodeAddress(String address) {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken("sk.eyJ1IjoibnVyYmFudWNhbmJheiIsImEiOiJjbHZpMzBwenoxN3ZjMmtwNXpoY2U4a29hIn0.aJOOvQlksuYh33YkSkIQIQ")
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

                    saveCoordinatesToFirebase(address, latitude, longitude);
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

    private void saveCoordinatesToFirebase(String address, double latitude, double longitude) {
        // Assuming you have a user ID to identify the user
        String userId = "unique_user_id"; // Replace with actual user ID

        Map<String, Object> addressData = new HashMap<>();
        addressData.put("address", address);
        addressData.put("latitude", latitude);
        addressData.put("longitude", longitude);

        addressRef.child(userId).setValue(addressData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ChangeAddressPage.this, "Address saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ChangeAddressPage.this, "Failed to save address: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}