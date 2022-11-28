package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class createnote extends AppCompatActivity {

        public EditText mcreatetitleofnote,mcreatecontentofnote;
        FloatingActionButton msavenote;
        FirebaseAuth firebaseAuth;
        FirebaseUser firebaseUser;
        FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnote);


        msavenote=(FloatingActionButton) findViewById(R.id.savenote);
        mcreatecontentofnote=(EditText) findViewById(R.id.createcontentofnote);
        mcreatetitleofnote=(EditText)findViewById(R.id.createtitlefornote);
        ProgressBar mprogressbarforcreatenoteactivity;

        mprogressbarforcreatenoteactivity=findViewById(R.id.progressbarofcreatenote);

        Toolbar toolbar=findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);
         Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//if someone come by mistake on note so this will send user at home pag

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        msavenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title=mcreatetitleofnote.getText().toString().trim();
                String content=mcreatecontentofnote.getText().toString().trim();

                if(title.isEmpty()|| content.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Both fields are required",Toast.LENGTH_SHORT).show();
                }

                else{
                        mprogressbarforcreatenoteactivity.setVisibility(View.VISIBLE);

                    //here cloud firestore woks like a herar tree like structure where notes like book,getuid like page,mynotes work as what you want to write in the page
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();

                   //here hashmap problem occurs and we will later solved it

                    Map<String,Object> note=new HashMap<>();

                    note.put("title",title);
                    note.put("content",content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Notes created Successfully",Toast.LENGTH_SHORT).show();
                            //mprogressbarforcreatenoteactivity.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(createnote.this,notesactivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Fail To Create Note",Toast.LENGTH_SHORT).show();

                            mprogressbarforcreatenoteactivity.setVisibility(View.INVISIBLE);
                        }
                    });
                }


            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}