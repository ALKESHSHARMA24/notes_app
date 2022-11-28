package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {
    private EditText msignupemail;
    private EditText msignuppassword;
    RelativeLayout msignup;
    private TextView mgotologin;

    private FirebaseAuth firebaseauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();


        firebaseauth=FirebaseAuth.getInstance();

        getSupportActionBar().hide();
        msignupemail=findViewById(R.id.signupemail);
        msignuppassword=findViewById(R.id.signuppassword);
        msignup=findViewById(R.id.signup);
        mgotologin=findViewById(R.id.gotologin);


        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signup.this,Login.class);
                startActivity(intent);
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=msignupemail.getText().toString().trim();
                String password = msignuppassword.getText().toString().trim();


                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"All field are required",Toast.LENGTH_SHORT).show();
                }

                else if (password.length()<7){
                    Toast.makeText(getApplicationContext(),"the password should be greater than 7 charcters",Toast.LENGTH_SHORT).show();

                }

                else{
                    firebaseauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                                sendEmailVerfication();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Failed to Register",Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });




    }

    private void sendEmailVerfication(){

        FirebaseUser firebaseuser=firebaseauth.getCurrentUser();
        if(firebaseuser!=null){

            firebaseuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(getApplicationContext(),"Verification Link Send Successfully,Please Verified and Login Again",Toast.LENGTH_SHORT).show();
                    firebaseauth.signOut();
                    finish();
                    startActivity(new Intent(signup.this,Login.class));
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Failed Send Verification Email",Toast.LENGTH_SHORT).show();

        }
    }
}