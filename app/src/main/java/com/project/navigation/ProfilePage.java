package com.project.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.navigation.activities.SignInActivity;
import com.project.navigation.databinding.ActivityProfilePageBinding;
import com.project.navigation.utilities.Constants;
import com.project.navigation.utilities.PreferenceManager;
import java.util.HashMap;


public class ProfilePage extends AppCompatActivity {
    private Button ProfileEditProfile, ProfileReturn,ProfileAttendence,ProfileChangeAddress,ProfilePayment;
    private EditText  ProfileName,ProfileSurname;
    private RoundedImageView ProfileImage;
    private PreferenceManager preferenceManager;
    private AppCompatImageView SignOut;
    private Button Return;
    private FirebaseFirestore database;
    private ActivityProfilePageBinding bindingProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile_page);
        bindingProfile = ActivityProfilePageBinding.inflate(getLayoutInflater());
        setContentView(bindingProfile.getRoot());

        SignOut = (AppCompatImageView) findViewById(R.id.imageSignOut);
        ProfileImage = (RoundedImageView) findViewById(R.id.imageProfile);
        preferenceManager = new PreferenceManager(getApplicationContext());
        ProfileEditProfile = (Button) findViewById(R.id.EditProfile);
        ProfileReturn = (Button) findViewById(R.id.Return);
        ProfileAttendence = (Button) findViewById(R.id.Attendence);
        ProfileChangeAddress = (Button) findViewById(R.id.ChangeAddress);
        ProfilePayment=(Button) findViewById(R.id.Payment);



        preferenceManager = new PreferenceManager(getApplicationContext());




        ProfileEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfilePage.this, PaymentPage.class);
                startActivity(intent);
                finish();
                return;


            }
        });





        ProfilePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfilePage.this, PaymentPage.class);
                startActivity(intent);
                finish();
                return;


            }
        });

        ProfileChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfilePage.this, ChangeAddressPage.class);
                startActivity(intent);
                finish();
                return;


            }
        });



        ProfileAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfilePage.this, AttendencePage.class);
                startActivity(intent);
                finish();
                return;


            }
        });




        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();


            }

        });


        ProfileEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfilePage.this, PaymentPage.class);
                startActivity(intent);
                finish();
                return;


            }
        });
        //setContentView(bindingProfile.getRoot());
        init();
        loadUserDetails();
        setListeners();


    }
    private void init(){

        database = FirebaseFirestore.getInstance();
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setListeners(){

        bindingProfile.imageSignOut.setOnClickListener(v -> signOut());

    }

    private void loadUserDetails(){

        bindingProfile.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bindingProfile.imageProfile.setImageBitmap(bitmap);

    }

    private void signOut(){
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    public void onBackPressed() {
        // do something on back.
        super.onBackPressed();
        startActivity(new Intent(ProfilePage.this, Dashboard.class));
        return;
    }

}