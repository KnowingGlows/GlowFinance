package com.knowingglows.glowfinance;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class income_description extends AppCompatActivity
{

    AppCompatButton


            add_income_btn,
            addincome_toolbar_btn,addexpense_toolbar_btn,
            bottom_navigation_home,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_description);

        Instantiate();
        Toolbar();
        AddRecord();
        BottomNavigationBarFunctionality();

    }


    public void AddRecord()
    {
        add_income_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(income_description.this, add_income.class));
            }
        });
    }
    public void Instantiate()
    {

        add_income_btn=findViewById(R.id.add_income_record);
        addexpense_toolbar_btn=findViewById(R.id.addexpense_toolbar_btn);
        addincome_toolbar_btn = findViewById(R.id.addincome_toolbar_btn);
        bottom_navigation_home = findViewById(R.id.bottom_navigation_home);
        bottom_navigation_transactions = findViewById(R.id.bottom_navigation_transactions);
        bottom_navigation_addrecords = findViewById(R.id.bottom_navigation_addrecords);
        bottom_navigation_profile = findViewById(R.id.bottom_navigation_profile);
        bottom_navigation_report = findViewById(R.id.bottom_navigation_report);
    }

    public void Toolbar()
    {
        addincome_toolbar_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(income_description.this, income_description.class));
            }
        });

        addexpense_toolbar_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(income_description.this, expense_description.class));
            }
        });
    }

    public void BottomNavigationBarFunctionality() {

        bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(income_description.this, R.color.colourpalette_moderngreen));
        bottom_navigation_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_home.setBackgroundTintList(ContextCompat.getColorStateList(income_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(income_description.this, home.class));
            }
        });

        bottom_navigation_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_transactions.setBackgroundTintList(ContextCompat.getColorStateList(income_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(income_description.this, transactions.class));
            }
        });

        bottom_navigation_addrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(income_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(income_description.this, income_description.class));
            }
        });

        bottom_navigation_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_profile.setBackgroundTintList(ContextCompat.getColorStateList(income_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(income_description.this, profile.class));
            }
        });

        bottom_navigation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_report.setBackgroundTintList(ContextCompat.getColorStateList(income_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(income_description.this, report.class));
            }
        });
    }
}