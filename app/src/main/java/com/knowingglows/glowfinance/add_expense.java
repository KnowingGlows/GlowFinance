package com.knowingglows.glowfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class add_expense extends AppCompatActivity
{

    AppCompatButton

            save_expense,
            addincome_toolbar_btn,addexpense_toolbar_btn,
            bottom_navigation_home,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense);

        Instantiate();
        BottomNavigationBarFunctionality();
        Toolbar();
        SaveRecord();
    }

    public void SaveRecord()
    {
        save_expense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(add_expense.this, "Record Added Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Instantiate()
    {
        save_expense = findViewById(R.id.add_expense_record);
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
                startActivity(new Intent(add_expense.this, add_income.class));
            }
        });

        addexpense_toolbar_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(add_expense.this, add_expense.class));
            }
        });
    }

    public void BottomNavigationBarFunctionality() {

        bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(add_expense.this, R.color.colourpalette_moderngreen));
        bottom_navigation_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                bottom_navigation_home.setBackgroundTintList(ContextCompat.getColorStateList(add_expense.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(add_expense.this, home.class));
            }
        });

        bottom_navigation_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_transactions.setBackgroundTintList(ContextCompat.getColorStateList(add_expense.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(add_expense.this, transactions.class));
            }
        });

        bottom_navigation_addrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(add_expense.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(add_expense.this, income_description.class));
            }
        });

        bottom_navigation_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_profile.setBackgroundTintList(ContextCompat.getColorStateList(add_expense.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(add_expense.this, profile.class));
            }
        });

        bottom_navigation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_report.setBackgroundTintList(ContextCompat.getColorStateList(add_expense.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(add_expense.this, report.class));
            }
        });
    }

}