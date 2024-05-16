package com.knowingglows.glowfinance;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.NullValue;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class report extends AppCompatActivity
{

AppCompatEditText
        report_username, report_deliverydate, report_specificinfo;

FirebaseAuth user;
AppCompatImageView userdp;
    AppCompatButton

            glowcoins_btn,

            create_report_btn,
            report_7_days, report_14_days, report_30_days,
            bottom_navigation_home,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;

    AppCompatTextView
            user_profilename,
            report_cost,User_GlowCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Instantiate();
        BottomNavigationBarFunctionality();
        FinancialReport();
        UserSetup();
        CreateReport();
        UserSetupGlowCoins();
        user = FirebaseAuth.getInstance();
        userdp = findViewById(R.id.user_profile);
        report_deliverydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker();
            }
        });
        CircularProfilePic.loadCircularImage(this, userdp);
        GlowCoinsPageOpener.setButtonClickListener(report.this, glowcoins_btn);
    }

    public void Instantiate()
    {
        glowcoins_btn = findViewById(R.id.glowcoin_btn);
        report_username = findViewById(R.id.report_username);
        report_deliverydate = findViewById(R.id.report_deliverydate);
        report_specificinfo = findViewById(R.id.report_specificinfo);
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v)
            {
                report_cost.setText("250");
                report_7_days.setBackgroundTintList(null);
                report_14_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
                report_30_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
            }
        });

        report_14_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                report_cost.setText("500");
                report_14_days.setBackgroundTintList(null);
                report_7_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
                report_30_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
            }
        });

        report_30_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                report_cost.setText("700");
                report_30_days.setBackgroundTintList(null);
                report_7_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
                report_14_days.setBackgroundTintList(ContextCompat.getColorStateList(report.this, R.color.dark_bg));
            }
        });
    }

    public void UserSetup()
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        String firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();
        user_profilename.setText(firebaseUser);
    }

    public void CreateReport()
    {
        create_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (report_cost.getText().toString().equals(String.valueOf(250))) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    GlowCoins.getGlowCoins(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), new GlowCoins.OnGlowCoinsLoadedListener() {
                        @Override
                        public void onGlowCoinsLoaded(long glowCoins) {
                            if (glowCoins >= Long.parseLong(report_cost.getText().toString())) {
                                financialreport.composeEmail(report.this, Objects.requireNonNull(report_username.getText()).toString(), Objects.requireNonNull(report_deliverydate.getText()).toString(), Objects.requireNonNull(report_specificinfo.getText()).toString(), firebaseAuth.getCurrentUser().getEmail());
                                GlowCoins glowCoins_new = new GlowCoins();
                                glowCoins_new.updateGlowCoins(firebaseAuth.getCurrentUser().getUid(), glowCoins - 250, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                            } else {
                                Toast.makeText(report.this, "Insufficient Glow Coins", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
                }
                else if (report_cost.getText().toString().equals("500")) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    GlowCoins.getGlowCoins(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), new GlowCoins.OnGlowCoinsLoadedListener() {
                        @Override
                        public void onGlowCoinsLoaded(long glowCoins) {
                            if (glowCoins >= Long.parseLong(report_cost.getText().toString())) {
                                financialreport.composeEmail(report.this, Objects.requireNonNull(report_username.getText()).toString(), Objects.requireNonNull(report_deliverydate.getText()).toString(), Objects.requireNonNull(report_specificinfo.getText()).toString(), firebaseAuth.getCurrentUser().getEmail());
                                GlowCoins glowCoins_new = new GlowCoins();
                                glowCoins_new.updateGlowCoins(firebaseAuth.getCurrentUser().getUid(), glowCoins - 500, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                            } else {
                                Toast.makeText(report.this, "Insufficient Glow Coins", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
                }
                    else if (report_cost.getText().toString().equals("700"))
                    {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        GlowCoins.getGlowCoins(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), new GlowCoins.OnGlowCoinsLoadedListener() {
                            @Override
                            public void onGlowCoinsLoaded(long glowCoins) {
                                if (glowCoins >= Long.parseLong(report_cost.getText().toString())) {
                                    financialreport.composeEmail(report.this, Objects.requireNonNull(report_username.getText()).toString(), Objects.requireNonNull(report_deliverydate.getText()).toString(), Objects.requireNonNull(report_specificinfo.getText()).toString(), firebaseAuth.getCurrentUser().getEmail());
                                    GlowCoins glowCoins_new = new GlowCoins();
                                    glowCoins_new.updateGlowCoins(firebaseAuth.getCurrentUser().getUid(), glowCoins - 750, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });
                                } else {
                                    Toast.makeText(report.this, "Insufficient Glow Coins", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });
                    }
                }
            });
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

    public void DatePicker()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set initial date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        // Update the EditText with the selected date
                        @SuppressLint("DefaultLocale") String selectedDate = String.format("%d-%02d-%02d", selectedYear, monthOfYear + 1, dayOfMonth);
                        report_deliverydate.setText(selectedDate);
                    }
                },
                year, month, dayOfMonth
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
}
