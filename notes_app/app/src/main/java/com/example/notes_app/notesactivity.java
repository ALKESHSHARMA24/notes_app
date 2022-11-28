package com.example.notes_app;

import static java.time.LocalDateTime.now;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class notesactivity extends AppCompatActivity {

    FloatingActionButton mcratesnotesfab;
    FirebaseAuth firebaseAuth;

    RecyclerView mrecylcerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel,Noteviewholder> noteAdapter;//this will fatch the notes from the firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesactivity);

        mcratesnotesfab = (FloatingActionButton) findViewById(R.id.createnotefab);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();//this fetch the get current user and according that user fetch the all information
        firebaseFirestore = FirebaseFirestore.getInstance();

        //work for set any title for activity at top here we set set title = "All notes"
        Objects.requireNonNull(getSupportActionBar()).setTitle("All Notes ");

        mcratesnotesfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(notesactivity.this, createnote.class));


            }
        });

        //Ascending the notes in title wise formate.

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, Noteviewholder>(allusernotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull Noteviewholder holder, int position, @NonNull firebasemodel firebasemodel) {

                ImageView popupbutton=holder.itemView.findViewById(R.id.notesmenupopbutton);

                // it will fecth any random color to the notes
                int colourcode=getrandomcolor();
                holder.mnote.setBackgroundColor(holder.itemView.getResources().getColor(colourcode,null));


                holder.notetitle.setText(firebasemodel.getTitle());
                holder.notecontent.setText(firebasemodel.getContent());

                String docId=noteAdapter.getSnapshots().getSnapshot(position).getId();


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //we have to open note detail activity


                        Intent intent=new Intent(v.getContext(),notedetailactivity.class);
                        intent.putExtra("title",firebasemodel.getTitle());
                        intent.putExtra("content",firebasemodel.getContent());
                        intent.putExtra("noteId",docId);
                        v.getContext().startActivity(intent);


                       // Toast.makeText(getApplicationContext(),"this is Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //notes edit menu option creating

                        PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent=new Intent(v.getContext(),editnoteactivity.class);
                                intent.putExtra("title",firebasemodel.getTitle());
                                intent.putExtra("content",firebasemodel.getContent());
                                intent.putExtra("noteId",docId);
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {



                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                // crating the  delete alert dilalouge box for delete notes
                                AlertDialog.Builder builder = new AlertDialog.Builder(notesactivity.this);
                                builder.setTitle("Delete");
                                builder.setMessage("Are you sure you want to delete?");


                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    //deleting the note
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(v.getContext(),"Failed To Delete",Toast.LENGTH_SHORT).show();
                                            }

                                        });
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.create().show();
                                return false;
                            }
                        });


                        //how to export notes into pdf file


                        popupMenu.getMenu().add("Export PDF").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                LocalDateTime datetime1 = now();
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
                                String formatDateTime = datetime1.format(format);

                                String stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/Note" + formatDateTime +".pdf";
                                File file = new File(stringFilePath);

                                PdfDocument pdfDocument = new PdfDocument();
                                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
                                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                                Paint paint = new Paint();
                                String stringPDF = firebasemodel.getTitle() + "\n\n" + firebasemodel.getContent();

                                int x = 10, y = 25;

                                for (String line:stringPDF.split("\n")){
                                    page.getCanvas().drawText(line,x,y, paint);

                                    y+=paint.descent()-paint.ascent();
                                }
                                pdfDocument.finishPage(page);
                                try {
                                    pdfDocument.writeTo(new FileOutputStream(file));
                                    Toast.makeText(v.getContext(),"file pdf created",Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e){
                                    Toast.makeText(v.getContext(),"file pdf didn't create",Toast.LENGTH_SHORT).show();
                                }
                                pdfDocument.close();

                                return false;
                            }
                        });



                        /*
                        //how to export notes into docx file

                        popupMenu.getMenu().add("Export docx").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                LocalDateTime datetime1 = now();
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
                                String formatDateTime = datetime1.format(format);

                                String stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/Note" + formatDateTime +".docx";
                                File file = new File(stringFilePath);

                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(v.getContext(),"file docx didn't create",Toast.LENGTH_SHORT).show();
                                }

                                try {
                                    XWPFDocument xwpfDocument = new XWPFDocument();
                                    XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
                                    XWPFRun xwpfRuntt = xwpfParagraph.createRun();
                                    XWPFRun xwpfRunct = xwpfParagraph.createRun();

                                    xwpfRuntt.setText(firebasemodel.getTitle() + "\n");
                                    xwpfRuntt.addBreak();
                                    xwpfRuntt.setFontSize(24);
                                    xwpfRuntt.setBold(true);

                                    //xwpfRun1.setText(firebasemodel.getContent());
                                    String data = firebasemodel.getContent();
                                    if(data.contains("\n")){
                                        String[] lines = data.split("\n");
                                        xwpfRunct.setText(lines[0], 0);
                                        for(int i=1;i<lines.length;i++){
                                            xwpfRunct.addBreak();
                                            xwpfRunct.setText(lines[i]);
                                        }
                                    } else {
                                        xwpfRunct.setText(data, 0);
                                    }
                                    xwpfRunct.setFontSize(16);


                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                                    xwpfDocument.write(fileOutputStream);
                                    if (fileOutputStream!=null){
                                        fileOutputStream.flush();
                                        fileOutputStream.close();
                                    }

                                    xwpfDocument.close();
                                    Toast.makeText(v.getContext(),"file docx created",Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(v.getContext(),"file docx didn't create",Toast.LENGTH_SHORT).show();
                                }
                                return false;
                            }
                        });

                         */

                        popupMenu.show();
                    }
                });
            }

            @NonNull
            @Override
            public Noteviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new Noteviewholder(view);
            }
        };

        mrecylcerview = findViewById(R.id.recylerview);
        mrecylcerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mrecylcerview.setLayoutManager(staggeredGridLayoutManager);
        mrecylcerview.setAdapter(noteAdapter);
    }

    public class Noteviewholder extends RecyclerView.ViewHolder {


        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;


        public Noteviewholder(@NonNull View itemView) {
            super(itemView);

            notetitle = (TextView) itemView.findViewById(R.id.notetitle);
            notecontent = (TextView) itemView.findViewById(R.id.notecontent);
            mnote = (LinearLayout) itemView.findViewById(R.id.note);



            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.note_animation);
            mnote.setAnimation(translate_anim);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(notesactivity.this, Login.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();//when user come from any activity to our notes activity so load the recycler view again
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {

            noteAdapter.stopListening();
        }
    }

    private int getrandomcolor(){

        List<Integer> colorcode =new ArrayList<>();
        colorcode.add(R.color.grey);
        colorcode.add(R.color.green);
        colorcode.add(R.color.pink);
        colorcode.add(R.color.skyblue);
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.color3);
        colorcode.add(R.color.lightgreen);

        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);
    }
}