package com.knowingglows.glowfinance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.protobuf.NullValue;

import java.util.Objects;

public class report extends AppCompatActivity
{

    AppCompatButton

            create_report_btn,
            report_7_days, report_14_days, report_30_days,
            bottom_navigation_home,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;

    AppCompatTextView
            user_profilename,
            report_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Instantiate();
        BottomNavigationBarFunctionality();
        FinancialReport();
        UserSetup();
    }

    public void Instantiate()
    {
        user_profilename = findViewById(R.id.user_username);
        report_cost = findViewById(R.id.report_cost);
        create_report_btn = findViewById(R.id.create_report_btn);
        report_7_days = findViewById(R.id.seven_days_btn);
        report_14_days = findViewById(R.id.fourteen_days_btn);
        report_30_days = findViewById(R.id.thirty_days_btn);
        bottom_navigation_home = findViewById(R.id.bottom_navigation_home);
        bottom_navigation_transactions = findViewById(R.id.bottom_navigation_transactions);
        bottom_navigation_addrecords = findViewById(R.id.bottom_navigation_addrecords);
        bottom_navigation_profile = findViewById(R.id.bottom_navigation_profile);
        bottom_navigation_report = findViewById(R.id.bottom_navigation_report);
    }

    public void BottomNavigationBarFunctionality()
    {


        bottom_navigation_report.setBackgroundTintList(ContextCompat.getColorStateList(report.this,R.color.colourpalette_moderngreen));
        bottom_navigation_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_home.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(report.this, home.class));
            }
        });

        bottom_navigation_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_transactions.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(report.this, transactions.class));
            }
        });

        bottom_navigation_addrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(report.this, income_description.class));
            }
        });

        bottom_navigation_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_profile.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(report.this, profile.class));
            }
        });

        bottom_navigation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_report.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(report.this, report.class));
            }
        });
    }

    public void FinancialReport()
    {
        report_7_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                report_cost.setText("Cost = 250");
                report_7_days.setBackgroundTintList(null);
                report_14_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
                report_30_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
            }
        });

        report_14_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                report_cost.setText("Cost = 500");
                report_14_days.setBackgroundTintList(null);
                report_7_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
                report_30_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
            }
        });

        report_30_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                report_cost.setText("Cost = 700");
                report_30_days.setBackgroundTintList(null);
                report_7_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
                report_14_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
            }
        });

        create_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(report.this, "Report Is Being Created!", Toast.LENGTH_SHORT).show();
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