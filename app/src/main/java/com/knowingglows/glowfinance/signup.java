package com.knowingglows.glowfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class signup extends AppCompatActivity
{
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        //firebase link
        auth = FirebaseAuth.getInstance();

        //calling activity
        AppCompatButton signupbtn = findViewById(R.id.user_signupbtn);
        AppCompatEditText username = findViewById(R.id.user_username);
        AppCompatEditText useremail = findViewById(R.id.user_email);
        AppCompatEditText userpassword = findViewById(R.id.user_password);
        AppCompatButton loginbtn = findViewById(R.id.signin_btn);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this, login.class));
            }
        });

        //signup process
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = username.getText().toString();
                String user_password = userpassword.getText().toString();
                String user_email = useremail.getText().toString();

                if (user_name.isEmpty()) {
                    username.setError("User Name Cannot Be Empty");
                } else if (user_email.isEmpty()) {
                    useremail.setError("Email Cannot Be Empty");
                } else if (user_password.isEmpty()) {
                    userpassword.setError("Password Cannot Be Empty");
                } else {
                    Task<AuthResult> authResultTask = auth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(signup.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signup.this, home.class));
                            } else {
                                Toast.makeText(signup.this, "Signup Failed. Please Try Again!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null)
        {
            startActivity(new Intent(signup.this, home.class));
        }
    }
}

