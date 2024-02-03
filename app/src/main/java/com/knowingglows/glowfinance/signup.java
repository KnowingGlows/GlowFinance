package com.knowingglows.glowfinance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class signup extends AppCompatActivity {
    FirebaseAuth auth;
    public static final int RC_SIGN_IN = 100;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Firebase link
        auth = FirebaseAuth.getInstance();

        // Calling activity
        AppCompatButton signupbtn = findViewById(R.id.user_signupbtn);
        AppCompatEditText username = findViewById(R.id.user_username);
        AppCompatEditText useremail = findViewById(R.id.user_email);
        AppCompatEditText userpassword = findViewById(R.id.user_password);
        AppCompatButton loginbtn = findViewById(R.id.signin_btn);
        AppCompatButton google_signupbtn = findViewById(R.id.google_signupbtn);

        // Google Sign In configuration
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        google_signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleSignInClientSignInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(googleSignInClientSignInIntent, RC_SIGN_IN);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this, login.class));
            }
        });

        // Signup process
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = Objects.requireNonNull(username.getText()).toString();
                String user_password = Objects.requireNonNull(userpassword.getText()).toString();
                String user_email = Objects.requireNonNull(useremail.getText()).toString();

                if (user_name.isEmpty()) {
                    username.setError("User Name Cannot Be Empty");
                } else if (user_email.isEmpty()) {
                    useremail.setError("Email Cannot Be Empty");
                } else if (user_password.isEmpty()) {
                    userpassword.setError("Password Cannot Be Empty");
                } else {
                    auth.createUserWithEmailAndPassword(user_email, user_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(signup.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(signup.this, home.class));
                                    } else {
                                        Toast.makeText(signup.this, "Signup Failed. Please Try Again!" +
                                                Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
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
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (authResult.getAdditionalUserInfo().isNewUser()) {
                            Toast.makeText(signup.this, "Account Created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(signup.this, home.class));
                        } else {
                            Toast.makeText(signup.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(signup.this, login.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(signup.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
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


