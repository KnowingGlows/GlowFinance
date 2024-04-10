package com.knowingglows.glowfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.Duration;

public class add_income extends AppCompatActivity
{

    AppCompatButton

            save_income,
            addincome_toolbar_btn,addexpense_toolbar_btn,
            bottom_navigation_home,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_income);

        Instantiate();
        Toolbar();
        BottomNavigationBarFunctionality();
        SaveRecord();
    }

    public void SaveRecord()
    {
        save_income.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(add_income.this, "Record Added Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Instantiate()
    {
        save_income = findViewById(R.id.add_income_record);
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
                startActivity(new Intent(add_income.this, add_income.class));
            }
        });

        addexpense_toolbar_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(add_income.this, add_expense.class));
            }
        });
    }

    public void BottomNavigationBarFunctionality() {
        bottom_navigation_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_income.this, home.class));
            }
        });

        bottom_navigation_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_income.this, transactions.class));
            }
        });

        bottom_navigation_addrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_income.this, income_description.class));
            }
        });

        bottom_navigation_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_income.this, profile.class));
            }
        });

        bottom_navigation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_income.this, report.class));
            }
        });
    }

    }
