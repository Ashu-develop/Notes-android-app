package com.example.powernotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetailsActivity extends AppCompatActivity {

    private TextView mnotedetailstitle,mnotedetailscontent;
    FloatingActionButton meditnotefab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

      mnotedetailstitle=findViewById(R.id.notedetailstitle);
      mnotedetailscontent=findViewById(R.id.notedetailscontent);
      meditnotefab=findViewById(R.id.editnotefab);

        Toolbar toolbar = findViewById(R.id.toolbarofnotedetails);
        setSupportActionBar(toolbar);

      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data= getIntent();

        meditnotefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),CreateNote.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteid",data.getStringExtra("noteid"));
                v.getContext().startActivity(intent);
                
            }
        });

        mnotedetailscontent.setText(data.getStringExtra("content"));
        mnotedetailstitle.setText(data.getStringExtra("title"));




    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.home){

            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }
}