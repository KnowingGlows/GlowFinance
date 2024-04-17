package com.knowingglows.glowfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.model.GradientColor;
import com.google.android.gms.tasks.Task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class home extends AppCompatActivity

{
    //creating variables

        String CentreText;
        double AvailableBalance;
     double finalTotalIncome, finalTotalExpense;

     AppCompatTextView
             Income_src_1, Income_date_1,
             Income_amount_1, Expense_src_1,
             Expense_src_2, Expense_date_1,
             Expense_date_2,Expense_amount_1,
             Expense_amount_2;

    public double income, expense;
    AppCompatImageView transaction1, transaction2, transaction3;
    int count = 1;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    AppCompatButton profile_btn, glowcoins_btn, home_btn, transactions_btn, addrecords_btn, profilepage_btn, report_btn;
    AppCompatTextView user_profilename, user_glowcoins_num;

    PieChart userspendchart;

    public home() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Initialize();
        homePage();
        BasicUserImplementation();
        DynamicPieChart();
        count+=1;
        UIstuff();
    }

    public void BasicUserImplementation()
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            String username = firebaseUser.getDisplayName(); // Get the real-time username

            db = FirebaseFirestore.getInstance(); // Initialize FirebaseFirestore instance

            // Create a HashMap to represent the user's data
            HashMap<String, Object> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("balance", 0.0);

            // Add the user document with username and balance fields
            db.collection("users").document(userId)
                    .set(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(home.this, "User document created successfully!", Toast.LENGTH_SHORT).show();
                            UserIncome(userId);
                            UserExpense(userId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(home.this, "Failed to create user document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(home.this, "User not authenticated!", Toast.LENGTH_SHORT).show();
        }
    }

    public void UserIncome(String userId)
    {
        Map<String, Object> initialIncome = new HashMap<>();
        initialIncome.put("name", "Initial Income");
        initialIncome.put("date", new Date());
        initialIncome.put("amount", 0.0);
        initialIncome.put("description", "");

        if (count==1)
        {
            db.collection("users").document(userId).collection("Income")
                    .add(initialIncome)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(home.this, "Successful!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(home.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void UserExpense(String userId)
    {
        if (count==1)
        {
            Map<String, Object> initialExpense = new HashMap<>();
            initialExpense.put("name", "Initial Expense");
            initialExpense.put("date", new Date());
            initialExpense.put("amount", 0.0);
            initialExpense.put("description", "");

            db.collection("users").document(userId).collection("Expense")
                    .add(initialExpense)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    public void DynamicPieChart()
    {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db.collection("users").document(userId).collection("Income").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> incomeTask) {
                if (incomeTask.isSuccessful())
                {
                    double totalIncome = 0;
                    for (QueryDocumentSnapshot document : incomeTask.getResult())
                    {
                        Double amountNumber = document.getDouble("amount");
                        if (amountNumber != null) {
                            totalIncome += amountNumber;
                        }

                        finalTotalIncome = totalIncome;
                    }

                    db.collection("users").document(userId).collection("Expense").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> expenseTask) {
                            if (expenseTask.isSuccessful()) {
                                double totalExpense = 0;
                                for (QueryDocumentSnapshot document : expenseTask.getResult()) {
                                    Double amountNumber = document.getDouble("amount");
                                    if (amountNumber != null) {
                                        totalExpense += amountNumber;
                                    }

                                    finalTotalExpense = totalExpense;
                                }

                                AvailableBalance = finalTotalExpense-finalTotalIncome;
                                // Display pie chart only after both income and expense data are fetched
                                displayPieChart(userspendchart, finalTotalIncome, finalTotalExpense);
                            } else {
                                Log.e("Expense Fetch", "Failed to fetch expenses", expenseTask.getException());
                            }
                        }
                    });
                } else {
                    Log.e("Income Fetch", "Failed to fetch income", incomeTask.getException());
                }
            }
        });
    }
    private void displayPieChart(@NonNull PieChart pieChart, double totalIncome, double totalExpense) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) totalIncome, ""));
        entries.add(new PieEntry((float) totalExpense, ""));

        int incomeColor = ContextCompat.getColor(home.this, R.color.colourpalette_moderngreen);
        int expenseColor = ContextCompat.getColor(home.this, R.color.expense_piechart_colour2);

        AvailableBalance = totalIncome-totalExpense;
        // Create PieDataSet
        PieDataSet dataSet = new PieDataSet(entries,"");
        // Create PieData and set it to the PieChart
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setDrawCenterText(true);
        dataSet.setValueTextSize(16f);
        pieChart.setCenterTextColor(ContextCompat.getColor(home.this, R.color.unselected_txt_btn));
        pieChart.setCenterTextTypeface(ResourcesCompat.getFont(home.this, R.font.poppins_semibold));
        CentreText = "Available Balance" + "\n" + String.valueOf(AvailableBalance);
        pieChart.setCenterText(CentreText);
        Typeface LabelFont = ResourcesCompat.getFont(home.this, R.font.poppins_semibold);
        dataSet.setValueTypeface(LabelFont);
        int LabelTextColour = ContextCompat.getColor(home.this, R.color.colourpalette_white);
        dataSet.setValueTextColor(LabelTextColour);

        // pieChart.setCenterText(generateCenterText(AvailableBalance)); // Set the text in the center of the pie chart
        pieChart.setCenterTextSize(16f);

        dataSet.setColors(incomeColor, expenseColor);

        // Refresh the PieChart
        pieChart.invalidate();
    }



    public void Initialize()
    {
        transaction1 = findViewById(R.id.transaction1);
        transaction2 = findViewById(R.id.transaction2);
        transaction3 = findViewById(R.id.transaction3);
        profile_btn = findViewById(R.id.user_profile);
        glowcoins_btn = findViewById(R.id.glowcoin_btn);
        user_profilename = findViewById(R.id.user_username);
        user_glowcoins_num = findViewById(R.id.user_glowcoins_num);
        profilepage_btn = findViewById(R.id.profilepage_btn);
        home_btn = findViewById(R.id.home_btn);
        transactions_btn = findViewById(R.id.transactionpage_btn);
        report_btn = findViewById(R.id.reportpage_btn);
        addrecords_btn = findViewById(R.id.addrecordspage_btn);
        userspendchart = findViewById(R.id.user_spend_chart);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user_profilename.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName());
    }
    public void homePage()
    {
        home_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                home_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(home.this, home.class));
            }
        });


        transactions_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                transactions_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(home.this, transactions.class));
            }
        });

        addrecords_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addrecords_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(home.this, income_description.class));
            }
        });

        profilepage_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                profilepage_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(home.this, profile.class));
            }
        });

        report_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                report_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(home.this, report.class));
            }
        });

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
    }

    public void UIstuff()
    {
        transaction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });

        transaction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        transaction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}