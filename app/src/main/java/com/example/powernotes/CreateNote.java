package com.example.powernotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    private EditText mcreatenotetitle,mcreatecontentnote;
    private FloatingActionButton msavenotefab;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar progressBarofcreatenote;

    ImageButton micbutton;
    SpeechRecognizer speechRecognizer;
    int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mcreatenotetitle=findViewById(R.id.createnotetitle);
        mcreatecontentnote=findViewById(R.id.createcontentnote);
        msavenotefab = findViewById(R.id.savenotefab);
        progressBarofcreatenote = findViewById(R.id.progressbarofcreatenote);

       // micbutton = findViewById(R.id.mic);


       // if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=PackageManager.PERMISSION_GRANTED) {//if permission is not granted
       //     ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
       // }

       // speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
       // Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //micbutton.setOnClickListener(new View.OnClickListener() {//when clicked on mic off it image view will set drawable to mic on
         //   @Override
          //  public void onClick(View v) {
            //    if (count == 0) {//if mic is off
             //       micbutton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_24));

               //     speechRecognizer.startListening(speechRecognizerIntent);//when mic start listening

               //    count= 1;


             //   } else {
                //    micbutton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_off_24));

                 //   speechRecognizer.stopListening();//when mic stop listening
                  //  count=0;


              //  }
           // }
       // });
        /*

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {//when the voice is recognised the recognised voice will be stored in result variable

                ArrayList<String> data = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);// the bundle cannot directly pass the recorded data to string so the dat is passed in arraylistcontaining string

                mcreatecontentnote.setText(data.get(0));

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

         */


        Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);// to add toolbar to current activity
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to add back button to current activity toolbar

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();




        msavenotefab.setOnClickListener(new View.OnClickListener() {//when clicked on fab button
            @Override
            public void onClick(View v) {
                String title = mcreatenotetitle.getText().toString();//taking title input from user
                String content = mcreatecontentnote.getText().toString();//taking content input from user
                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Both the fields are required", Toast.LENGTH_SHORT).show();

                }else {

                    progressBarofcreatenote.setVisibility(View.VISIBLE);
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("mynotes").document();
                    //in document refrence
                    //1 the document of collection of notes is made
                    //2and inside that collection of notes document document of user is made
                    Map<String,Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Note saved sucessfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateNote.this,notesActivity.class));
                            progressBarofcreatenote.setVisibility(View.INVISIBLE);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(getApplicationContext(), "note is not saved", Toast.LENGTH_SHORT).show();
                            progressBarofcreatenote.setVisibility(View.INVISIBLE);


                        }
                    });
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();

            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.home){

            onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }
}