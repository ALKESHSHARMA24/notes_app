 package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;





import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.Objects;

 public class notedetailactivity extends AppCompatActivity {

     //on this page we  can only see our notes preview


    private TextView mtitleofnotedetail,mcontentofnotedeatil;
    FloatingActionButton mgotoeditnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetailactivity);

        mtitleofnotedetail=(TextView) findViewById(R.id.titlefornotedetail);
        mcontentofnotedeatil=(TextView) findViewById(R.id.contentofnotedetail);
        mgotoeditnote=(FloatingActionButton) findViewById(R.id.gotoeditnote);


        Toolbar toolbar=findViewById(R.id.toolbarofnotedetail);
        setSupportActionBar(toolbar);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);  //if user come bimistacely come on this page so he can go home page

        Intent data=getIntent();

        mgotoeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetch already previous notes data

                Intent intent=new Intent(v.getContext(),editnoteactivity.class);
                intent.putExtra("title",data.getStringArrayExtra("title"));
                intent.putExtra("content",data.getStringArrayExtra("content"));
                intent.putExtra("noteId",data.getStringArrayExtra("noteId"));
                v.getContext().startActivity(intent);

            }
        });


        //now we set note data

        mtitleofnotedetail.setText(data.getStringExtra("title"));
        mcontentofnotedeatil.setText(data.getStringExtra("content"));

    }

    //code for getsupportactionbar to go at homepage

     @Override
     public boolean onOptionsItemSelected(@NonNull MenuItem item) {

         if(item.getItemId()==android.R.id.home){

//             finish();
//             return true;
             onBackPressed();
         }
         return super.onOptionsItemSelected(item);
     }
}