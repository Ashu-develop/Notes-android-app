package com.example.powernotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class EditNoteActivity extends AppCompatActivity {
    private Intent data;
    private EditText meditnotrtitle,meditnotecontent;
    private FloatingActionButton msavenoteineditnotefab;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        meditnotrtitle = findViewById(R.id.editnotetitle);
        meditnotecontent = findViewById(R.id.editnotecontent);
        msavenoteineditnotefab = findViewById(R.id.savenoteineditnotefab);
        Toolbar toolbar = findViewById(R.id.toolbarofeditnote);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        data=getIntent();

        String notetitle= data.getStringExtra("title");
        String notecontent= data.getStringExtra("content");
        meditnotrtitle.setText(notetitle);//this will set the data in this activity
        meditnotecontent.setText(notecontent);

        msavenoteineditnotefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newtitle = meditnotrtitle.getText().toString();
                String newcontent = meditnotecontent.getText().toString();
                if(newtitle.isEmpty()||newcontent.isEmpty()){
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    DocumentReference documentReference =firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("mynotes").document(data.getStringExtra("noteid"));
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note is updated sucessfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditNoteActivity.this,notesActivity.class));


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(getApplicationContext(),"Note is not updated",Toast.LENGTH_SHORT).show();


                        }
                    });
                }

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.home){

            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }
}