package com.knowingglows.glowfinance;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.initialization.qual.Initialized;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class transactions extends AppCompatActivity
{


    FirebaseFirestore db;
    LineChart lineChart;

    FirebaseAuth user;
    AppCompatImageView userdp;
    AppCompatTextView user_profilename,AvailableBalance,User_GlowCoins;

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
        UserSetup();
        UserSetupGlowCoins();
        fetchDataForLineChart(7, "Income");


        user = FirebaseAuth.getInstance();
        userdp = findViewById(R.id.user_profile);
        if (user.getCurrentUser() != null) {
            Uri photoUrl = user.getCurrentUser().getPhotoUrl();

            if (photoUrl != null) {
                // Load the profile picture into the ImageView using Glide
                Glide.with(this)
                        .load(photoUrl)
                        .into(userdp);
            }
        }
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

    public void Initialize()
    {
        AvailableBalance = findViewById(R.id.AvailableBalance);
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
                fetchDataForLineChart(7,"Income");
                seven_days_data.setBackgroundTintList(null);
                fourteen_days_data.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.dark_bg));
                thirty_days_data.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.dark_bg));
            }
        });

        fourteen_days_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                fetchDataForLineChart(14,"Income");
                seven_days_data.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.dark_bg));
                fourteen_days_data.setBackgroundTintList(null);
                thirty_days_data.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.dark_bg));
            }
        });

        thirty_days_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                fetchDataForLineChart(30,"Income");
                seven_days_data.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.dark_bg));
                fourteen_days_data.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.dark_bg));
                thirty_days_data.setBackgroundTintList(null);
            }
        });
        income_chart_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fetchDataForLineChart(7,"Income");
                income_chart_btn.setBackgroundTintList(null);
                expense_chart_btn.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.dark_bg));
            }
        });

        expense_chart_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fetchDataForLineChart(7,"Expense");
                income_chart_btn.setBackgroundTintList(ContextCompat.getColorStateList(transactions.this, R.color.dark_bg));
                expense_chart_btn.setBackgroundTintList(null);
            }
        }
        );
    }

    public void UserSetup()
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();
        user_profilename.setText(firebaseUser);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserId = firebaseAuth.getCurrentUser().getUid(); // Retrieve the UID of the current user
        DocumentReference documentReference = db.collection("users").document(currentUserId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve the Balance field
                    Double availableBalance = documentSnapshot.getDouble("Balance");
                    if (availableBalance != null) {
                        AvailableBalance.setText(String.valueOf(availableBalance));
                    } else {
                        Log.d(TAG, "Balance field is null");
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error retrieving balance", e);
            }
        });

    }

    private void fetchDataForLineChart(int timeframeDays, String dataType)
    {
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setDrawGridBackground(false); // Disable drawing the grid background
        lineChart.getXAxis().setDrawGridLines(false); // Disable vertical grid lines
        lineChart.getAxisLeft().setDrawGridLines(false); // Disable horizontal grid lines
        Typeface typeface = ResourcesCompat.getFont(this, R.font.poppins_bold);
        lineChart.getXAxis().setTypeface(typeface);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Custom formatting for x-axis labels
                // You can format the value as needed, e.g., convert float value to date
                return String.valueOf((int)value); // Example: Convert float value to integer
            }
        });

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTypeface(typeface);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTypeface(typeface);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return String.valueOf((int)value);
            }
        });
        lineChart.getDescription().setTypeface(typeface);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setTextColor(Color.WHITE);

        // Set animation
        lineChart.animateX(1500, Easing.EaseInOutExpo); // X-axis animation
        lineChart.animateY(1500, Easing.EaseInOutExpo); // Y-axis animation
        Date currentDate = new Date();

        // Calculate the start date based on the timeframe
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, -timeframeDays);
        Date startDate = calendar.getTime();

        // Construct the Firestore query
        CollectionReference dataCollection;
        if (dataType.equals("Income"))
        {
            dataCollection = FirebaseFirestore.getInstance().collection("users")
                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .collection("Income");
        } else {
            dataCollection = FirebaseFirestore.getInstance().collection("users")
                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .collection("Expense");
        }

        // Fetch data based on the query
        dataCollection
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful()) {
                            // Process the retrieved data
                            List<Entry> entries = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                // Parse string date to Date object
                                String dateString = document.getString("date");
                                Log.d(TAG, "Fetched date string: " + dateString); // Log the fetched date string
                                Date dataDate = parseDate(dateString);
                                if (dataDate != null) {
                                    // Calculate x-value (time difference from start date)
                                    long timeDifference = dataDate.getTime() - startDate.getTime();
                                    float xValue = TimeUnit.MILLISECONDS.toDays(timeDifference);
                                    // Extract amount value
                                    float dataValue = document.getDouble("amount").floatValue();
                                    // Add entry to the list
                                    entries.add(new Entry(xValue, dataValue));

                                }
                            }
                            int lineColor;
                            int fillColor;
                            if (dataType.equals("Income")) {
                                lineColor = ContextCompat.getColor(transactions.this, R.color.income_piechart_colour1);
                                fillColor =  ContextCompat.getColor(transactions.this, R.color.income_piechart_colour1);
                            } else {
                                lineColor = Color.parseColor("#FF5733"); // Orange-red color for expense
                                fillColor = Color.parseColor("#FF4C00"); // Light orange-red fill color for expense
                            }


                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setValueFormatter(new ValueFormatter() {
                                @Override
                                public String getFormattedValue(float value) {
                                    // Convert value (days since start date) back to a Date object
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(startDate);
                                    calendar.add(Calendar.DAY_OF_YEAR, (int) value);
                                    // Extract day part from the date
                                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                    // Return day part as string
                                    return String.valueOf(dayOfMonth);
                                }
                            });
                            // Populate the line chart with the processed data
                            LineDataSet dataSet = new LineDataSet(entries, dataType);
                            lineChart.getXAxis().setTextColor(Color.WHITE); // Set X-axis text color to white
                            lineChart.getAxisLeft().setTextColor(Color.WHITE); // Set left Y-axis text color to white
                            lineChart.getAxisRight().setTextColor(Color.WHITE); // Set right Y-axis text color to white

                            // Set animation
                            lineChart.animateX(1500, Easing.EaseInOutExpo); // X-axis animation
                            lineChart.animateY(1500, Easing.EaseInOutExpo); // Y-axis animation

                            // Set curved lines
                            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                            dataSet.setCubicIntensity(0.2f);
                            LineData lineData = new LineData(dataSet);
                            Description desc = new Description();
                            desc.setText("");
                             // Set curvature intensity (between 0 and 1)

                            // Set fill color
                            dataSet.setDrawFilled(true);
                            dataSet.setFillColor(fillColor);
                            lineChart.setData(lineData);
                            dataSet.setColor(lineColor);
                            lineChart.invalidate(); // Refresh chart
                        } else {
                            Log.e(TAG, "Error getting data for line chart", task.getException());
                        }
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + dateString, e);
            return null;
        }
    }

    public void UserSetupGlowCoins()
    {
        User_GlowCoins = findViewById(R.id.user_glowcoins_num);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();
        user_profilename.setText(firebaseUser);
        GlowCoins glowCoins = new GlowCoins();
        GlowCoins.getGlowCoins(firebaseAuth.getCurrentUser().getUid(), new GlowCoins.OnGlowCoinsLoadedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onGlowCoinsLoaded(long glowCoins) {
                String GlowCoins = String.valueOf(glowCoins);
                User_GlowCoins.setText(GlowCoins);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

}