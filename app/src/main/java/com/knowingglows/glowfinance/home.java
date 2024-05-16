package com.knowingglows.glowfinance;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class home extends AppCompatActivity {

    String CentreText;
    String UserId;
    double AvailableBalance;
    double finalTotalIncome, finalTotalExpense;

    ArrayList<transaction_records> transactionRecords;
    RecyclerView recyclerView;
    MyAdapter myAdapter;



    int count = 1;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    CircularProfilePic circularProfilePic;
    FirebaseFirestore db;
    AppCompatButton  glowcoins_btn, home_btn, transactions_btn, addrecords_btn, profilepage_btn, report_btn;
    AppCompatTextView user_profilename, User_GlowCoins;

    PieChart userspendchart;
    ProgressDialog progressDialog;

    AppCompatImageView userdp;

    public home() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Initialize();
        homePage();
        checkAndUpdateUserFields(firebaseAuth.getUid());
        DynamicPieChart();

        userdp = findViewById(R.id.user_profile);
        if (firebaseAuth.getCurrentUser() != null) {
            Uri photoUrl = firebaseAuth.getCurrentUser().getPhotoUrl();

            if (photoUrl != null) {
                // Load the profile picture into the ImageView using Glide
                Glide.with(this)
                        .load(photoUrl)
                        .into(userdp);
            }
        }

        UserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionRecords = new ArrayList<transaction_records>();
        myAdapter = new MyAdapter(home.this, transactionRecords);
        recyclerView.setAdapter(myAdapter);
        FetchTransactions();

        CircularProfilePic.loadCircularImage(this, userdp);


   

    }


    private void checkAndUpdateUserFields(String userId) {
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


    public void BasicUserImplementation() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
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


    public void Initialize() {
        glowcoins_btn = findViewById(R.id.glowcoin_btn);
        user_profilename = findViewById(R.id.user_username);
        User_GlowCoins = findViewById(R.id.user_glowcoins_num);
        profilepage_btn = findViewById(R.id.profilepage_btn);
        home_btn = findViewById(R.id.home_btn);
        transactions_btn = findViewById(R.id.transactionpage_btn);
        report_btn = findViewById(R.id.reportpage_btn);
        addrecords_btn = findViewById(R.id.addrecordspage_btn);
        userspendchart = findViewById(R.id.user_spend_chart);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user_profilename.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName());
        GlowCoins.getGlowCoins(firebaseAuth.getUid(), new GlowCoins.OnGlowCoinsLoadedListener() {
            @Override
            public void onGlowCoinsLoaded(long glowCoins) {
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(new Intent(home.this, transactions.class));
            }
        });

        addrecords_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrecords_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(new Intent(home.this, income_description.class));
            }
        });

        profilepage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profilepage_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(new Intent(home.this, profile.class));
            }
        });

        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                report_btn.setBackgroundTintList(ContextCompat.getColorStateList(home.this, R.color.colourpalette_moderngreen));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(new Intent(home.this, report.class));
            }
        });

        glowcoins_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(new Intent(home.this, glowcoinspage.class));
            }
        });
    }

    @SuppressLint({"RestrictedApi", "NotifyDataSetChanged"})
    private void FetchTransactions()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        tasks.add(db.collection("users").document(userId).collection("Income").orderBy("date", Query.Direction.ASCENDING).get());
        tasks.add(db.collection("users").document(userId).collection("Expense").orderBy("date", Query.Direction.DESCENDING).get());

        Tasks.whenAllComplete(tasks)
                .addOnSuccessListener(tasksResults -> {
                    List<transaction_records> allTransactions = new ArrayList<>();

                    // Use a regular for loop to iterate through tasksResults
                    for (int i = 0; i < tasksResults.size(); i++) {
                        Task<QuerySnapshot> task = (Task<QuerySnapshot>) tasksResults.get(i);

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                transaction_records transaction = document.toObject(transaction_records.class);

                                if (document.getReference().getParent().getId().equals("Income")) {
                                    transaction.setType("Income");
                                } else {
                                    transaction.setType("Expense");
                                }

                                allTransactions.add(transaction);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                    Collections.sort(allTransactions, (t1, t2) -> t1.getDate().compareTo(t2.getDate()));

                    myAdapter.transactionRecords.clear();
                    myAdapter.transactionRecords.addAll(allTransactions);
                    myAdapter.notifyDataSetChanged();

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                });
    }
}

