package com.project.navigation;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class ProfilePage extends AppCompatActivity {
    private Button ProfileEditProfile, ProfileReturn,ProfileAttendence,ProfileChangeAddress,ProfilePayment;
    private EditText  ProfileName,ProfileSurname;



    private Button Return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location_system_page);
        Return=(Button)findViewById(R.id.Return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfilePage.this, Dashboard.class);
                startActivity(intent);
                finish();
                return;


            }
        });
    }

/*
    //private FirebaseAuth logAuth;
    //private FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        logAuth=FirebaseAuth.getInstance();
        final Intent[] intent = new Intent[1];

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();   //Checking the user status. Stores the currently logged in user info.
                if(user!=null  ){
                    intent[0] = new Intent(ProfilePage.this, Dashboard.class); //login page den dashboard a yönlendiriyor.

                    return;
                }
            }
        };


        ProfileEditProfile = (Button) findViewById(R.id.EditProfile);
        ProfileReturn = (Button) findViewById(R.id.Return);
        ProfileAttendence = (Button) findViewById(R.id.Attendence);
        ProfileChangeAddress = (Button) findViewById(R.id.ChangeAddress);
        ProfilePayment=(Button) findViewById(R.id.Payment);
        ProfileName=(EditText) findViewById(R.id.Name);
        ProfileSurname=(EditText) findViewById(R.id.SurName);

/*

        ProfileEditProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String name=LogMailAccount.getText().toString();
                final String surname=LogMailPassword.getText().toString();
                logAuth.signInWithEmailAndPassword(name,surname).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(LoginPage.this,"sign in failed",Toast.LENGTH_SHORT).show();
                        }
                        startActivity(intent[0]);
                        finish();
                    }
                });
            }
        });
        */



/*

        ProfileEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfilePage.this, PaymentPage.class);
                startActivity(intent);
                finish();
                return;


            }
        });

*/

/*

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



        ProfileReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfilePage.this, Dashboard.class);
                startActivity(intent);
                finish();
                return;


            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();
        logAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        logAuth.removeAuthStateListener(firebaseAuthListener);
    }

 */
}