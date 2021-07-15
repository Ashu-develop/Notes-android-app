package com.example.powernotes;

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

public class Forgetpassword extends AppCompatActivity {


    private EditText mforgetpassword;
    private Button mpasswordrecoverbutton;
    private TextView mgotologin;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);


        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        mforgetpassword = findViewById(R.id.forgetpassword);
        mpasswordrecoverbutton = findViewById(R.id.passwordrecoverbutton);
        mgotologin = findViewById(R.id.gotologin);

        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgetpassword.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mforgetpassword.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter your email",Toast.LENGTH_SHORT).show();

                }else{

                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"check your email to recover your password", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Forgetpassword.this, MainActivity.class));

                            } else {
                                Toast.makeText(getApplicationContext(),"wrong details entered",Toast.LENGTH_SHORT).show();


                            }

                        }
                    });

                }
            }
        });
    }
}