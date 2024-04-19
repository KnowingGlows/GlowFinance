package com.knowingglows.glowfinance;


import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.billingclient.api.QueryProductDetailsParams;
import com.google.android.gms.ads.AdRequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.ImmutableList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.knowingglows.glowfinance.databinding.ActivityGlowcoinspageBinding;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class glowcoinspage extends AppCompatActivity {


    AppCompatTextView
            User_GlowCoins,
            user_profilename;

    FirebaseFirestore db;

    private RewardedAd rewardedAd;
    public int EarnedClowCoins;
    private final String TAG = "glowcoinspage";

    AppCompatTextView
            AvailableGlowCoins;

    public BillingClient billingClient;
    Boolean success = false;

    ActivityGlowcoinspageBinding binding;
    AppCompatButton
            Purchase_50gc, Purchase_250gc, Purchase_500gc;
    AppCompatButton

            glowcoins_ad,
            bottom_navigation_home,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glowcoinspage);
        binding = ActivityGlowcoinspageBinding.inflate(getLayoutInflater());
        Instantiate();
        BottomNavigationBarFunctionality();
        UserSetup();
        loadRewardedAd();

        glowcoins_ad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_animation);
                glowcoins_ad.startAnimation(anim);
                glowcoins_ad.setBackgroundResource(R.drawable.background_hover_effect);
                showRewardedVideoAd();
            }
        });
    }


    private void showRewardedVideoAd()
    {
        if (rewardedAd != null) {
            Activity activityContext = glowcoinspage.this;
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener()
            {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Reload the rewarded ad
                    loadRewardedAd();

                    // Handle the reward earned by the user
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    EarnedClowCoins = rewardAmount;
                    GlowCoins glowCoins = new GlowCoins();
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    glowCoins.addGlowCoins(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), rewardAmount, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(glowcoinspage.this, "Earned!", Toast.LENGTH_SHORT).show();
                            GlowCoins.getGlowCoins(firebaseAuth.getCurrentUser().getUid(), new GlowCoins.OnGlowCoinsLoadedListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onGlowCoinsLoaded(long glowCoins) {
                                    User_GlowCoins.setText(String.valueOf(glowCoins));
                                    AvailableGlowCoins.setText("GlowCoins = " + glowCoins);
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    // Handle error
                                }
                            });
                        }
                    });
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }


    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-9082390422569348/5026639934", adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                rewardedAd = ad;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError error) {
                // Handle ad load failure
            }
        });

}

    public void Instantiate()
    {
        User_GlowCoins = findViewById(R.id.user_glowcoins_num);
        AvailableGlowCoins = findViewById(R.id.AvailableGlowCoins);
        glowcoins_ad = findViewById(R.id.glowcoins_ad);
        user_profilename = findViewById(R.id.user_username);
        bottom_navigation_home = findViewById(R.id.bottom_navigation_home);
        bottom_navigation_transactions = findViewById(R.id.bottom_navigation_transactions);
        bottom_navigation_addrecords = findViewById(R.id.bottom_navigation_addrecords);
        bottom_navigation_profile = findViewById(R.id.bottom_navigation_profile);
        bottom_navigation_report = findViewById(R.id.bottom_navigation_report);
    }

    public void BottomNavigationBarFunctionality() {
        bottom_navigation_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(glowcoinspage.this, home.class));
            }
        });

        bottom_navigation_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(glowcoinspage.this, transactions.class));
            }
        });

        bottom_navigation_addrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(glowcoinspage.this, income_description.class));
            }
        });

        bottom_navigation_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(glowcoinspage.this, profile.class));
            }
        });

        bottom_navigation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(glowcoinspage.this, report.class));
            }
        });
    }

    public void UserSetup() {
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
                AvailableGlowCoins.setText("GlowCoins = " + GlowCoins);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }



}