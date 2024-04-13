package com.knowingglows.glowfinance;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.initialization.qual.Initialized;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class transactions extends AppCompatActivity
{


    FirebaseFirestore db;
    LineChart lineChart;
    AppCompatTextView user_profilename;

    AppCompatButton
            bottom_navigation_home,
            seven_days_data, fourteen_days_data, thirty_days_data,
            income_chart_btn, expense_chart_btn,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        Initialize();
        BottomNavigationBarFunctionality();
        TransactionsFunctionality();
        LineChartTest();
        UserSetup();
        CreateUser(user_profilename.toString(), 0f);
    }

    public void BottomNavigationBarFunctionality()
    {
        bottom_navigation_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottom_navigation_home.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(transactions.this, home.class));
            }
        });

        bottom_navigation_transactions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottom_navigation_transactions.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(transactions.this, transactions.class));
            }
        });

        bottom_navigation_addrecords.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.colourpalette_moderngreen));
                        startActivity(new Intent(transactions.this, income_description.class));
                    }
                });

        bottom_navigation_profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottom_navigation_profile.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(transactions.this, profile.class));
            }
        });

        bottom_navigation_report.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottom_navigation_report.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(transactions.this, report.class));
            }
        });

    }

    public void LineChartTest()
    {
        int colour = Color.parseColor("#0DA6C2");
        // Create data entries
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 4));
        entries.add(new Entry(1, 8));
        entries.add(new Entry(2, 6));
        entries.add(new Entry(3, 2));
        entries.add(new Entry(4, 7));

        // Create a dataset with entries
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // Label for the dataset
        dataSet.setColor(colour);
        dataSet.setValueTextColor(Color.BLUE);

        // Set curve line mode
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setFillColor(colour);

        // Set fill color
        dataSet.setDrawFilled(true);// Custom fill drawable

        // Create a LineData object with the dataset
        LineData lineData = new LineData(dataSet);

        // Set LineData to the chart
        lineChart.setData(lineData);

        // Customize chart appearance
        Description desc = new Description();
        desc.setText("Your Chart Description");
        lineChart.setDescription(desc);
        lineChart.getXAxis().setDrawGridLines(false); // Remove grid lines from X axis
        lineChart.getAxisLeft().setDrawGridLines(false); // Remove grid lines from left Y axis
        lineChart.getAxisRight().setDrawGridLines(false); // Remove grid lines from right Y axis

        // Set animation
        lineChart.animateX(1500, Easing.EaseInOutExpo); // X-axis animation
        lineChart.animateY(1500, Easing.EaseInOutExpo); // Y-axis animation
        lineChart.invalidate(); // Refresh the chart// Refresh the chart
    }


    public void Initialize()
    {
        user_profilename = findViewById(R.id.user_username);
        income_chart_btn = findViewById(R.id.income_chart_btn);
        expense_chart_btn = findViewById(R.id.expense_chart_btn);
        bottom_navigation_home = findViewById(R.id.bottom_navigation_home);
        bottom_navigation_transactions = findViewById(R.id.bottom_navigation_transactions);
        bottom_navigation_addrecords = findViewById(R.id.bottom_navigation_addrecords);
        bottom_navigation_profile = findViewById(R.id.bottom_navigation_profile);
        bottom_navigation_report = findViewById(R.id.bottom_navigation_report);
        seven_days_data = findViewById(R.id.seven_days_btn);
        fourteen_days_data = findViewById(R.id.fourteen_days_btn);
        thirty_days_data = findViewById(R.id.thirty_days_btn);
        lineChart = findViewById(R.id.transactions_chart);
        db = FirebaseFirestore.getInstance();
    }

    public void TransactionsFunctionality()
    {
        seven_days_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        fourteen_days_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        thirty_days_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        income_chart_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        expense_chart_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }

    public void UserSetup()
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();
        user_profilename.setText(firebaseUser);
    }

    public Void CreateUser(String username, double balance)
    {
        Users user = new Users();
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        return null;
    }
}