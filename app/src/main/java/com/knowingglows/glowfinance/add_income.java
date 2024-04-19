package com.knowingglows.glowfinance;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.widget.DatePicker;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class add_income extends AppCompatActivity {

    AppCompatTextView
            user_profilename,User_GlowCoins;

    AppCompatEditText
            Income_amount, Income_date, Income_src, Income_desc;


    AppCompatImageView
            userdp;
    AppCompatButton

            save_income,
            addincome_toolbar_btn, addexpense_toolbar_btn,
            bottom_navigation_home,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;

    FirebaseFirestore db;
    FirebaseAuth User;

    Double Amount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_income);

        Instantiate();
        Toolbar();
        BottomNavigationBarFunctionality();
        UserSetup();
        IncomeRecord();
        UserSetupGlowCoins();
        Intent intent = getIntent();
        Income_amount.setText(intent.getStringExtra("AMOUNT_STRING"));
        save_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveRecord();
            }
        });

        userdp = findViewById(R.id.user_profile);
        if (User.getCurrentUser() != null) {
            Uri photoUrl = User.getCurrentUser().getPhotoUrl();

            if (photoUrl != null) {
                // Load the profile picture into the ImageView using Glide
                Glide.with(this)
                        .load(photoUrl)
                        .into(userdp);
            }
        }
    }

    public void Instantiate() {
        db = FirebaseFirestore.getInstance();
        User = FirebaseAuth.getInstance();
        Income_amount = findViewById(R.id.Income_amount);
        Income_date = findViewById(R.id.Income_date);
        Income_src = findViewById(R.id.Income_src);
        Income_desc = findViewById(R.id.Income_desc);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user_profilename = findViewById(R.id.user_username);
        save_income = findViewById(R.id.add_income_record);
        addexpense_toolbar_btn = findViewById(R.id.addexpense_toolbar_btn);
        addincome_toolbar_btn = findViewById(R.id.addincome_toolbar_btn);
        bottom_navigation_home = findViewById(R.id.bottom_navigation_home);
        bottom_navigation_transactions = findViewById(R.id.bottom_navigation_transactions);
        bottom_navigation_addrecords = findViewById(R.id.bottom_navigation_addrecords);
        bottom_navigation_profile = findViewById(R.id.bottom_navigation_profile);
        bottom_navigation_report = findViewById(R.id.bottom_navigation_report);
        Income_date.setFocusable(false);
        Income_date.setClickable(true);
    }

    public void Toolbar() {
        addincome_toolbar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_income.this, add_income.class));
            }
        });

        addexpense_toolbar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_income.this, add_expense.class));
            }
        });
    }

    public void BottomNavigationBarFunctionality() {

        bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(add_income.this, R.color.colourpalette_moderngreen));
        bottom_navigation_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_home.setBackgroundTintList(ContextCompat.getColorStateList(add_income.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(add_income.this, home.class));
            }
        });

        bottom_navigation_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_transactions.setBackgroundTintList(ContextCompat.getColorStateList(add_income.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(add_income.this, transactions.class));
            }
        });

        bottom_navigation_addrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(add_income.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(add_income.this, income_description.class));
            }
        });

        bottom_navigation_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_profile.setBackgroundTintList(ContextCompat.getColorStateList(add_income.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(add_income.this, profile.class));
            }
        });

        bottom_navigation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_report.setBackgroundTintList(ContextCompat.getColorStateList(add_income.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(add_income.this, report.class));
            }
        });
    }

    public void UserSetup() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();
        user_profilename.setText(firebaseUser);
    }

    public void IncomeRecord() {

        Income_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker();
            }
        });
    }

    public void DatePicker() {
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
                        Income_date.setText(selectedDate);
                    }
                },
                year, month, dayOfMonth
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    public void SaveRecord() {
        String amountStr = Objects.requireNonNull(Income_amount.getText()).toString();
        if (!amountStr.isEmpty()) {
            Amount = Double.parseDouble(amountStr);
        } else {

        }
        Double IncomeAmount = Amount;
        String Income_Date = Objects.requireNonNull(Income_date.getText()).toString();
        String Income_Src = Objects.requireNonNull(Income_src.getText()).toString();
        String Income_Desc = Objects.requireNonNull(Income_desc.getText()).toString();

        String UserId = User.getUid();
        Income IncomeRecord = new Income(IncomeAmount, Income_Date, Income_Src, Income_Desc);
        assert UserId != null;
        db.collection("users")
                .document(UserId)
                .collection("Income")
                .add(IncomeRecord)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(add_income.this, "Added!", Toast.LENGTH_SHORT).show();
                        Income_amount.setText("");
                        Income_date.setText("");
                        Income_desc.setText("");
                        Income_src.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
}
