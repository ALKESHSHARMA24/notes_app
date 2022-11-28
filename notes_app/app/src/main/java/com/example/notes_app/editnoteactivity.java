package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

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
import java.util.*;

public class editnoteactivity extends AppCompatActivity {

    //Here we will get our notes which want to edit and also which is already we make in our notes app first
    //In short we will fetch our old notes data and edit that notes data


    Intent data;
    EditText medittitleofnote,meditcontentofnote;
    FloatingActionButton msaveeditnote;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);

        meditcontentofnote=(EditText) findViewById(R.id.editcontentofnote);
        medittitleofnote=(EditText) findViewById(R.id.edittitleofnote);
        msaveeditnote=(FloatingActionButton) findViewById(R.id.saveeditnote);


        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar=findViewById(R.id.toolbarforeditnote);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);  //if user come bimistakly come on this page so he can go home page


       msaveeditnote.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //Toast.makeText(getApplicationContext(),"updatebutton",Toast.LENGTH_SHORT).show();

               String newtitle=medittitleofnote.getText().toString();
               String newcontent=meditcontentofnote.getText().toString();

               if(newtitle.isEmpty()||newcontent.isEmpty()){

                   Toast.makeText(getApplicationContext(),"Both fileds are required",Toast.LENGTH_SHORT).show();
               }
               else{

                   //update the notes data

                   DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));

                   //here hashmap problem occurs and we will later solved it
                   Map<String,Object> notes=new HashMap<>();
                   notes.put("title",newtitle);
                   notes.put("content",newcontent);
                   documentReference.set(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {


                           Toast.makeText(getApplicationContext(),"Note is Updated",Toast.LENGTH_SHORT).show();

                           startActivity(new Intent(editnoteactivity.this,notesactivity.class));
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(getApplicationContext(),"Failed to update",Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           }
       });

        //set previous data from notesdetail activity for note title and content to editnote acitivity
        data=getIntent();

       String notetitle=data.getStringExtra("title");
        String notecontent=data.getStringExtra("content");
        medittitleofnote.setText(notetitle);     //here medittitleofnote will send the title data and we store that data in notetitle
        meditcontentofnote.setText(notecontent);  //here meditcontentofnote will send the content data and we store that data in notecontent

    }

    //code for getsupportactionbar to go at homepage

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}