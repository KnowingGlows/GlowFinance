package com.knowingglows.glowfinance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.Objects;

public class home extends AppCompatActivity
{
    //creating variables
    String income_des, expense_des, income_src, expense_src, income_name, expense_name;
    Integer income_amount, expense_amount;
    Date income_src_date, expense_src_date;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    AppCompatTextView testtxtemail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        testtxtemail = findViewById(R.id.testtxtemail);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String email = firebaseUser.getEmail().toString();
        testtxtemail.setText(email);


    }
}