package com.example.powernotes;

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

public class Signup extends AppCompatActivity {

    private EditText msignupemail,msignuppassword;
    private RelativeLayout msignup;
    private TextView mgoforlogin;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();//storing the instance in firebaseauth

        getSupportActionBar().hide();

        msignupemail = findViewById(R.id.signupemail);
        msignuppassword = findViewById(R.id.signuppassword);
        msignup = findViewById(R.id.signup);
        mgoforlogin = findViewById(R.id.goforlogin);

        mgoforlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this,MainActivity.class);
                startActivity(intent);
            }
        });
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = msignupemail.getText().toString().trim();
                String password = msignuppassword.getText().toString().trim();
                if (mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();

                } else if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "password should contain minnimum 8 characters", Toast.LENGTH_SHORT).show();

                } else {

                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull  Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Sucessfully registered", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            } else {
                                Toast.makeText(getApplicationContext(),"erroe while connecting",Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }
            }
        });

    }

    public void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!= null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"verify the email sent to your account",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Signup.this,MainActivity.class));


                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"error while sending verification email",Toast.LENGTH_SHORT).show();

        }
    }

    }
