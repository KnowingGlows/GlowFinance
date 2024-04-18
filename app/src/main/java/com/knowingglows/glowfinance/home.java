package com.knowingglows.glowfinance;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.Task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class home extends AppCompatActivity {
    //creating variables

    String CentreText;
    double AvailableBalance;
    double finalTotalIncome, finalTotalExpense;

    AppCompatTextView
            Income_src_1, Income_date_1,
            Income_amount_1, Expense_src_1,
            Expense_src_2, Expense_date_1,
            Expense_date_2, Expense_amount_1,
            Expense_amount_2;

    public double income, expense;
    AppCompatImageView transaction1, transaction2, transaction3;
    int count = 1;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    AppCompatButton profile_btn, glowcoins_btn, home_btn, transactions_btn, addrecords_btn, profilepage_btn, report_btn;
    AppCompatTextView user_profilename, User_GlowCoins;

    PieChart userspendchart;

    public home() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Initialize();
        homePage();
        checkAndUpdateUserFields(firebaseAuth.getUid());
        DynamicPieChart();
        UIstuff();
        LatestIncomeRecords(Income_src_1, Income_amount_1, Income_date_1);
        LatestExpenseRecords(Expense_src_1, Expense_date_1, Expense_amount_1, Expense_src_2, Expense_date_2, Expense_amount_2);
    }

    private void checkAndUpdateUserFields(String userId)
    {
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // User document exists, no need to initialize fields
                Log.d("HomeActivity", "User document exists");
            } else {
                // User document doesn't exist, initialize fields
                Log.d("HomeActivity", "User document does not exist, initializing fields");
                BasicUserImplementation();
            }
        }).addOnFailureListener(e -> {
            // Handle failure
            Log.e("HomeActivity", "Error checking user document", e);
        });
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
                userData.put("GlowCoins", 100.0);

                // Add the user document with username and balance fields
                db.collection("users").document(userId)
                        .set(userData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(home.this, "", Toast.LENGTH_SHORT).show();
                                db.collection("users").document(userId).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    Long glowCoins = documentSnapshot.getLong("GlowCoins");
                                                    if (glowCoins != null) {
                                                        User_GlowCoins.setText(glowCoins.toString());
                                                        Toast.makeText(home.this, "SignUp Bonus Claimed!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // GlowCoins field is not present or is null
                                                    }
                                                } else {
                                                    // Document does not exist
                                                }
                                            }
                                        });
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


    public void DynamicPieChart() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db.collection("users").document(userId).collection("Income").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> incomeTask) {
                if (incomeTask.isSuccessful()) {
                    double totalIncome = 0;
                    for (QueryDocumentSnapshot document : incomeTask.getResult()) {
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

                                AvailableBalance = finalTotalExpense - finalTotalIncome;
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

        AvailableBalance = totalIncome - totalExpense;
        DocumentReference userRef = db.collection("users").document(Objects.requireNonNull(firebaseAuth.getUid()));
        userRef.update("Balance", AvailableBalance)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle success
                        Log.d(TAG, "AvailableBalance updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Log.e(TAG, "Error updating AvailableBalance", e);
                    }
                });
        // Create PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "");
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
        Income_amount_1 = findViewById(R.id.Income_amount_1);
        Income_src_1 = findViewById(R.id.Income_src_1);
        Income_date_1 = findViewById(R.id.Income_date_1);
        Expense_amount_1 = findViewById(R.id.Expense_amount_1);
        Expense_amount_2 = findViewById(R.id.Expense_amount_2);
        Expense_src_1 = findViewById(R.id.Expense_src_1);
        Expense_src_2 = findViewById(R.id.Expense_src_2);
        Expense_date_1 = findViewById(R.id.Expense_date_1);
        Expense_date_2 = findViewById(R.id.Expense_date_2);
        transaction1 = findViewById(R.id.transaction1);
        transaction2 = findViewById(R.id.transaction2);
        transaction3 = findViewById(R.id.transaction3);
        profile_btn = findViewById(R.id.user_profile);
        glowcoins_btn = findViewById(R.id.glowcoin_btn);
        user_profilename = findViewById(R.id.user_username);
        User_GlowCoins= findViewById(R.id.user_glowcoins_num);
        profilepage_btn = findViewById(R.id.profilepage_btn);
        home_btn = findViewById(R.id.home_btn);
        transactions_btn = findViewById(R.id.transactionpage_btn);
        report_btn = findViewById(R.id.reportpage_btn);
        addrecords_btn = findViewById(R.id.addrecordspage_btn);
        userspendchart = findViewById(R.id.user_spend_chart);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user_profilename.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName());
        GlowCoins.getGlowCoins(firebaseAuth.getUid(), new GlowCoins.OnGlowCoinsLoadedListener()
        {
            @Override
            public void onGlowCoinsLoaded(long glowCoins)
            {
                User_GlowCoins.setText(String.valueOf(glowCoins));
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }

    public void homePage() {
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                home_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(home.this, home.class));
            }
        });


        transactions_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactions_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(home.this, transactions.class));
            }
        });

        addrecords_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrecords_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(home.this, income_description.class));
            }
        });

        profilepage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profilepage_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(home.this, profile.class));
            }
        });

        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                report_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(home.this, report.class));
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, profile.class));
            }
        });

        glowcoins_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, glowcoinspage.class));
            }
        });
    }

    public void UIstuff() {
        transaction1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (Objects.equals(v.getBackground().getConstantState(), getResources().getDrawable(R.drawable.background_box_filled).getConstantState())) {
                    v.setBackgroundResource(R.drawable.background_hover_effect);
                } else {
                    v.setBackgroundResource(R.drawable.background_box_filled);
                }
            }
        });

        transaction2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (Objects.equals(v.getBackground().getConstantState(), getResources().getDrawable(R.drawable.background_box_filled).getConstantState())) {
                    v.setBackgroundResource(R.drawable.background_hover_effect);
                } else {
                    v.setBackgroundResource(R.drawable.background_box_filled);
                }
            }
        });

        transaction3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (Objects.equals(v.getBackground().getConstantState(), getResources().getDrawable(R.drawable.background_box_filled).getConstantState())) {
                    v.setBackgroundResource(R.drawable.background_hover_effect);
                } else {
                    v.setBackgroundResource(R.drawable.background_box_filled);
                }
            }
        });
    }

    private void LatestExpenseRecords(AppCompatTextView srcTextView1, AppCompatTextView dateTextView1, AppCompatTextView amountTextView1,
                                      AppCompatTextView srcTextView2, AppCompatTextView dateTextView2, AppCompatTextView amountTextView2) {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        // Query to get the two latest expense records
        db.collection("users").document(userId).collection("Expense")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            String src1 = "", date1 = "", src2 = "", date2 = "";
                            double amount1 = 0.0, amount2 = 0.0;

                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                // Retrieve data from Firestore document
                                String src = document.getString("source");
                                String date = document.getString("date");
                                Double amountDouble = document.getDouble("amount");

                                // Check if amountDouble is not null
                                if (amountDouble != null) {
                                    double amount = amountDouble;

                                    // Update variables based on count
                                    if (count == 0) {
                                        src1 = src;
                                        date1 = date;
                                        amount1 = amount;
                                    } else if (count == 1) {
                                        src2 = src;
                                        date2 = date;
                                        amount2 = amount;
                                    }

                                    count++;
                                }
                            }

                            // Update the TextViews with the retrieved data
                            updateExpenseData(src1, date1, amount1, src2, date2, amount2,
                                    srcTextView1, dateTextView1, amountTextView1,
                                    srcTextView2, dateTextView2, amountTextView2);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void updateExpenseData(String src1, String date1, double amount1,
                                   String src2, String date2, double amount2,
                                   AppCompatTextView srcTextView1, AppCompatTextView dateTextView1, AppCompatTextView amountTextView1,
                                   AppCompatTextView srcTextView2, AppCompatTextView dateTextView2, AppCompatTextView amountTextView2) {
        // Check if any of the fields are empty or the amount is zero for the first set
        if (TextUtils.isEmpty(src1) || TextUtils.isEmpty(date1) || amount1 == 0.0) {
            srcTextView1.setText("");
            dateTextView1.setText("");
            amountTextView1.setText("");
        } else {
            // Set the data to the first set of TextViews
            srcTextView1.setText(src1);
            dateTextView1.setText(date1);
            amountTextView1.setText(String.valueOf(amount1));
        }

        // Check if any of the fields are empty or the amount is zero for the second set
        if (TextUtils.isEmpty(src2) || TextUtils.isEmpty(date2) || amount2 == 0.0) {
            srcTextView2.setText("");
            dateTextView2.setText("");
            amountTextView2.setText("");
        } else {
            // Set the data to the second set of TextViews
            srcTextView2.setText(src2);
            dateTextView2.setText(date2);
            amountTextView2.setText(String.valueOf(amount2));
        }
    }

    @SuppressLint("RestrictedApi")
    public void LatestIncomeRecords(AppCompatTextView income_src_1, AppCompatTextView income_amount_1, AppCompatTextView income_date_1)
    {
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        // Query to get the two latest expense records
        db.collection("users").document(userId).collection("Income")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            String src1 = "", date1 = "";
                            double amount1 = 0.0;

                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                // Retrieve data from Firestore document
                                String src = document.getString("source");
                                String date = document.getString("date");
                                Double amountDouble = document.getDouble("amount");

                                src1 = src;
                                date1= date;
                                amount1 = amountDouble;
                            }
                            UpdateIncomeData(src1, date1, amount1,
                                    income_src_1, income_date_1, income_amount_1);
                        }
                        }
                    });

        }

        public void UpdateIncomeData(String src1, String date1, Double amount1,
                                      AppCompatTextView income_src_1,
                                      AppCompatTextView income_date_1, AppCompatTextView income_amount_1)
        {
            if (TextUtils.isEmpty(src1) || TextUtils.isEmpty(date1) || amount1 == 0.0) {
                income_src_1.setText("");
                income_date_1.setText("");
                income_amount_1.setText("");
            } else {
                // Set the data to the first set of TextViews
                income_src_1.setText(src1);
                income_date_1.setText(date1);
                income_amount_1.setText(String.valueOf(amount1));
            }
        }
    }

