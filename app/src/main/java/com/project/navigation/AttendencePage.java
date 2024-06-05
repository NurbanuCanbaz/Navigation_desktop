package com.project.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AttendencePage extends AppCompatActivity {

    private CalendarView calendarView;
    private CheckBox checkBoxAbsent;
    private Button btnConfirmChanges ,Return;
    private DatabaseReference attendanceRef;
    private Calendar selectedDateCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_page);

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        checkBoxAbsent = findViewById(R.id.checkBoxAbsent);
        btnConfirmChanges = findViewById(R.id.btnConfirmChanges);



        // Initialize Firebase reference
        attendanceRef = FirebaseDatabase.getInstance().getReference().child("attendance");

        // Set listener for date selection in CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Update selectedDate when the user selects a different date
                selectedDateCalendar.set(year, month, dayOfMonth);
            }
        });

        // Set onClickListener for Confirm Changes button
        btnConfirmChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmChanges();
            }
        });
    }

    private void confirmChanges() {
        // Format selected date as "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String selectedDate = dateFormat.format(selectedDateCalendar.getTime());

        // Check if user marked themselves as absent
        boolean isAbsent = checkBoxAbsent.isChecked();

        // Create attendance data
        Map<String, Object> attendanceData = new HashMap<>();
        attendanceData.put("date", selectedDate);
        attendanceData.put("isAbsent", isAbsent);

        // Push attendance data to Firebase database
        Task<Void> attendanceDataSavedSuccessfully = attendanceRef.push().setValue(attendanceData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data successfully saved
                        Toast.makeText(AttendencePage.this, "Attendance data saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error saving data
                        Toast.makeText(AttendencePage.this, "Failed to save attendance data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
           });
    }
    @Override
    public void onBackPressed() {
        // do something on back.
        super.onBackPressed();
        startActivity(new Intent(AttendencePage.this, ProfilePage.class));
        return;
    }
}