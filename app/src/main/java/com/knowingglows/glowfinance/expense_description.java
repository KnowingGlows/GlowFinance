package com.knowingglows.glowfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class expense_description extends AppCompatActivity
{


    AppCompatTextView
            user_profilename;
    AppCompatButton

            addexpense,
            addincome_toolbar_btn,addexpense_toolbar_btn,
            bottom_navigation_home,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_description);

        Instantiate();
        Toolbar();
        BottomNavigationBarFunctionality();
        UserSetup();
    }

    public void AddRecord()
    {
        addexpense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(expense_description.this, add_expense.class));
            }
        });
    }
    public void Instantiate()
    {
        user_profilename = findViewById(R.id.user_username);
        addexpense = findViewById(R.id.add_expense_record);
        addexpense_toolbar_btn = findViewById(R.id.addexpense_toolbar_btn);
        addincome_toolbar_btn = findViewById(R.id.addincome_toolbar_btn);
        bottom_navigation_home = findViewById(R.id.bottom_navigation_home);
        bottom_navigation_transactions = findViewById(R.id.bottom_navigation_transactions);
        bottom_navigation_addrecords = findViewById(R.id.bottom_navigation_addrecords);
        bottom_navigation_profile = findViewById(R.id.bottom_navigation_profile);
        bottom_navigation_report = findViewById(R.id.bottom_navigation_report);
    }

    public void BottomNavigationBarFunctionality()
    {

        bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
        bottom_navigation_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_home.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(expense_description.this, home.class));
            }
        });

        bottom_navigation_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_transactions.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(expense_description.this, transactions.class));
            }
        });

        bottom_navigation_addrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(expense_description.this, income_description.class));
            }
        });

        bottom_navigation_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_profile.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(expense_description.this, profile.class));
            }
        });

        bottom_navigation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_report.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(expense_description.this, report.class));
            }
        });
    }

    public void Toolbar()
    {
        addincome_toolbar_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(expense_description.this, income_description.class));
            }
        });

        addexpense_toolbar_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(expense_description.this, expense_description.class));
            }
        });
    }

    public void UserSetup()
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();
        user_profilename.setText(firebaseUser);
    }
}