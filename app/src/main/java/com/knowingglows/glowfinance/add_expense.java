package com.knowingglows.glowfinance;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;
import java.util.Objects;

public class add_expense extends AppCompatActivity
{

public Double Amount;

AppCompatImageView
        userdp;
    AppCompatEditText
            Expense_amount, Expense_date, Expense_src, Expense_desc;
    AppCompatTextView
        user_profilename,User_GlowCoins;
FirebaseFirestore db;
FirebaseAuth User;
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
        UserSetup();
        ExpenseRecord();
        UserSetupGlowCoins();
        Intent intent = getIntent();
        Expense_amount.setText(intent.getStringExtra("AMOUNT_STRING"));
        save_expense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveRecord();
            }
        });

        User = FirebaseAuth.getInstance();
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

    public void Instantiate()
    {
        Expense_amount = findViewById(R.id.Expense_amount);
        Expense_date = findViewById(R.id.Expense_date);
        Expense_src = findViewById(R.id.Expense_src);
        Expense_desc = findViewById(R.id.Expense_desc);
        user_profilename = findViewById(R.id.user_username);
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
    public void UserSetup()
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();
        user_profilename.setText(firebaseUser);
    }

    public void ExpenseRecord()
    {

        Expense_date.setFocusable(false);
        Expense_date.setClickable(true);
        Expense_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker();
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
                        Expense_date.setText(selectedDate);
                    }
                },
                year, month, dayOfMonth
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    public void SaveRecord()
    {
        db = FirebaseFirestore.getInstance();
        String amountStr = Objects.requireNonNull(Expense_amount.getText()).toString();
        if (!amountStr.isEmpty()) {
            Amount = Double.parseDouble(amountStr);
        } else {

        }
        Double ExpenseAmount = Amount;
        String Expense_Date = Objects.requireNonNull(Expense_date.getText()).toString();
        String Expense_Src = Objects.requireNonNull(Expense_src.getText()).toString();
        String Expense_Desc = Objects.requireNonNull(Expense_desc.getText()).toString();

        String UserId = FirebaseAuth.getInstance().getUid();
        Expense ExpenseRecord = new Expense(ExpenseAmount, Expense_Date, Expense_Src, Expense_Desc);
        assert UserId!=null;
        db.collection("users").document(UserId).collection("Expense")
                .add(ExpenseRecord)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        Toast.makeText(add_expense.this, "Added!", Toast.LENGTH_SHORT).show();
                        Expense_amount.setText("");
                        Expense_date.setText("");
                        Expense_desc.setText("");
                        Expense_src.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

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