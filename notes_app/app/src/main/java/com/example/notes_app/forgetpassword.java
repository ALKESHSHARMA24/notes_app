package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpassword extends AppCompatActivity {

    private EditText mfotgetpassword;
    private Button mpasswordrecoverbutton;
    private TextView mgobacktologin;
   private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        getSupportActionBar().hide();
        mfotgetpassword=findViewById(R.id.forgetpassword);
        mpasswordrecoverbutton=findViewById(R.id.paswordrecoverbutton);
        mgobacktologin=findViewById(R.id.gobacktologin);
        firebaseAuth= FirebaseAuth.getInstance();

        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(forgetpassword.this,Login.class);
                startActivity(intent);
            }
        });

        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mfotgetpassword.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter your Email", Toast.LENGTH_SHORT).show();
                }
                else{
                    // we redirect user to email verfication

                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(getApplicationContext(),"the password reset link is succesfully sent to your eamil",Toast.LENGTH_SHORT).show();
                                finish();

                                startActivity(new Intent(forgetpassword.this,Login.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Sorry the email id is wrong or Account does not exist",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}