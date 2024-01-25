package com.knowingglows.glowfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private FirebaseAuth login;
    private EditText username, userpassword, useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = FirebaseAuth.getInstance(); // Initialize Firebase instance

        username = findViewById(R.id.user_username);
        userpassword = findViewById(R.id.user_password);
        useremail = findViewById(R.id.user_email);

        Button loginbtn = findViewById(R.id.user_loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values when login button is clicked
                String user_name = username.getText().toString();
                String user_email = useremail.getText().toString();
                String user_password = userpassword.getText().toString();

                if (!user_name.isEmpty())
                {
                    login.signInWithEmailAndPassword(user_email, user_password).addOnSuccessListener(new OnSuccessListener<AuthResult>()
                    {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(login.this, home.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
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
        FirebaseUser currentUser = login.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(login.this, home.class));
            finish();
        }
    }
}

