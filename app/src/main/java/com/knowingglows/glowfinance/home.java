package com.knowingglows.glowfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class home extends AppCompatActivity
{
    //creating variables


    FirebaseUser firebaseUer;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    AppCompatButton profile_btn,glowcoins_btn,user_selectdatachart, user_addrecords;
    AppCompatTextView user_profilename, user_balance, user_expense;

    AppCompatImageView user_spendbehaviour;

    HashMap<String, Object> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //firebase api connection
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //firestore linking
        user.put("user_name", user_profilename);
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //instantiating activities
        profile_btn = findViewById(R.id.user_profile);
        glowcoins_btn = findViewById(R.id.user_glowcoins);
        user_selectdatachart = findViewById(R.id.btn_selectcharttype);
        user_addrecords = findViewById(R.id.btn_adddata);
        user_profilename = findViewById(R.id.user_name);
        user_balance = findViewById(R.id.user_balance);
        user_expense= findViewById(R.id.user_expense);
        user_spendbehaviour = findViewById(R.id.user_spendbehaviour);

        profile_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(home.this, profile.class));
            }
        });

        glowcoins_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(home.this, glowcoinspage.class));
            }
        });

        user_addrecords.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
             startActivity(new Intent(home.this, addrecords.class));
            }
        });

        BasicUserImplementation();
    }


    public void BasicUserImplementation()
    {
        user_profilename.setText(firebaseAuth.getCurrentUser().getDisplayName().toString());
    }
}