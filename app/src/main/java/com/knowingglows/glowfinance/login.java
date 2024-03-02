package com.knowingglows.glowfinance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class login extends AppCompatActivity {

    private FirebaseAuth login;
    private int RC_SIGN_IN = 100;
    private EditText username, userpassword, useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInClient googleSignInClient;
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        login = FirebaseAuth.getInstance(); // Initialize Firebase instance

        username = findViewById(R.id.user_username);
        userpassword = findViewById(R.id.user_password);
        useremail = findViewById(R.id.user_email);
        AppCompatButton redirect_signup_page = findViewById(R.id.redirect_signup_page);
        AppCompatButton loginbtn = findViewById(R.id.user_loginbtn);
        AppCompatButton login_google = findViewById(R.id.login_google);


        redirect_signup_page.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(login.this, signup.class));
            }
        });
        login_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleSignInClientSignInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(googleSignInClientSignInIntent, RC_SIGN_IN);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values when login button is clicked
                String user_name = username.getText().toString();
                String user_email = useremail.getText().toString();
                String user_password = userpassword.getText().toString();

                if (!user_name.isEmpty()) {
                    login.signInWithEmailAndPassword(user_email, user_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogle(signInAccount);
            } catch (Exception e) {
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        login.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = login.getCurrentUser();
                        if (Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser()) {
                            Toast.makeText(login.this, "Account Created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(login.this, home.class));
                        } else {
                            Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(login.this, home.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = login.getCurrentUser();
        if(currentUser != null)
        {
            startActivity(new Intent(login.this, home.class));
        }
    }
}
