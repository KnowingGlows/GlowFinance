package com.knowingglows.glowfinance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class profile extends AppCompatActivity

{

    AppCompatTextView
            user_profilename;

    FirebaseAuth user;
    AppCompatImageView userdp;

    AppCompatTextView
            User_GlowCoins;
    AppCompatButton
            CreateSpendingReport, GlowFinanceRoadmap,Glowcoins, ContactUs, AboutUs,
            bottom_navigation_home,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Instantiate();
        BottomNavigationBarFunctionality();
        UserSetup();
        Functionality();
        UserSetupGlowCoins();

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


    public void Instantiate()
    {
        AboutUs = findViewById(R.id.AboutUs_btn);
        GlowFinanceRoadmap = findViewById(R.id.GlowFinanceRoadmap_btn);
        Glowcoins = findViewById(R.id.glowcoins_btn);
        ContactUs = findViewById(R.id.contactus_btn);
        CreateSpendingReport = findViewById(R.id.spending_report_btn);
        user_profilename = findViewById(R.id.user_username);
        bottom_navigation_home = findViewById(R.id.bottom_navigation_home);
        bottom_navigation_transactions = findViewById(R.id.bottom_navigation_transactions);
        bottom_navigation_addrecords = findViewById(R.id.bottom_navigation_addrecords);
        bottom_navigation_profile = findViewById(R.id.bottom_navigation_profile);
        bottom_navigation_report = findViewById(R.id.bottom_navigation_report);
    }

    public void BottomNavigationBarFunctionality()
    {

        bottom_navigation_profile.setBackgroundTintList(ContextCompat.getColorStateList(profile.this, R.color.colourpalette_moderngreen));
        bottom_navigation_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_home.setBackgroundTintList(ContextCompat.getColorStateList(profile.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(profile.this, home.class));
            }
        });

        bottom_navigation_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_transactions.setBackgroundTintList(ContextCompat.getColorStateList(profile.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(profile.this, transactions.class));
            }
        });

        bottom_navigation_addrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(profile.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(profile.this, income_description.class));
            }
        });

        bottom_navigation_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_profile.setBackgroundTintList(ContextCompat.getColorStateList(profile.this, R.color.colourpalette_moderngreen));startActivity(new Intent(profile.this, profile.class));
            }
        });

        bottom_navigation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_report.setBackgroundTintList(ContextCompat.getColorStateList(profile.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(profile.this, report.class));
            }
        });
    }

    public void UserSetup()
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();
        user_profilename.setText(firebaseUser);
    }

    public void Functionality()
    {
        AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_animation);
                AboutUs.startAnimation(anim);
                String url = "https://tarry-weather-df7.notion.site/KnowingGlows-Public-View-c79fc0eb41644724853726ecce280297?pvs=74"; // Replace with your desired URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        ContactUs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_animation);
                ContactUs.startAnimation(anim);
                Toast.makeText(profile.this, "Contact Us At knowingglowsofficial@gmail.com", Toast.LENGTH_LONG).show();
            }
        });

        GlowFinanceRoadmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_animation);
                GlowFinanceRoadmap.startAnimation(anim);
                String url = "https://tarry-weather-df7.notion.site/Roadmap-600affc031f14143bacb3d12f5db495e"; // Replace with your desired URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        Glowcoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_animation);
                Glowcoins.startAnimation(anim);
                startActivity(new Intent(profile.this, glowcoinspage.class));

            }
        });

        CreateSpendingReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_animation);
                CreateSpendingReport.startAnimation(anim);
                startActivity(new Intent(profile.this, report.class));
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
            public void onGlowCoinsLoaded(long glowCoins)
            {
                String GlowCoins = String.valueOf(glowCoins);
                User_GlowCoins.setText(GlowCoins);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}