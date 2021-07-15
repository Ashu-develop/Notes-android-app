package com.example.powernotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mloginemail,mloginpassword;
    private RelativeLayout mlogin,mgotosignin;
    private TextView mforgetpassword;

    private FirebaseAuth firebaseAuth;

    private ProgressBar progressBarofmainactivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        progressBarofmainactivity = findViewById(R.id.progressbarofmailactivity);

        if(firebaseUser!=null){//if user is already logged in
            finish();
            startActivity(new Intent(MainActivity.this,notesActivity.class));

        }


        getSupportActionBar().hide();


        mloginemail = findViewById(R.id.loginemail);
        mloginpassword = findViewById(R.id.loginpassword);
        mlogin = findViewById(R.id.login);
        mgotosignin = findViewById(R.id.gotosignin);
        mforgetpassword = findViewById(R.id.forgetpassword);


        mgotosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Signup.class));

            }
        });
        mforgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Forgetpassword.class));

            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mloginemail.getText().toString().trim();
                String password = mloginpassword.getText().toString().trim();
                if (mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();

                } else {

                    progressBarofmainactivity.setVisibility(View.VISIBLE);//the progress bar will be shown after login is clicked


                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull  Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkEmailVerification();

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"email is not registered",Toast.LENGTH_SHORT).show();

                                progressBarofmainactivity.setVisibility(View.INVISIBLE);


                            }

                        }
                    });
                }
            }
        });
    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(getApplicationContext()," Sucessfully Logged in",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,notesActivity.class));

        }else{

            progressBarofmainactivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"First verify your mail",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();

        }
    }
}