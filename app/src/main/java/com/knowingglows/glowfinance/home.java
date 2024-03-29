package com.knowingglows.glowfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class home extends AppCompatActivity
{
    //creating variables


    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    AppCompatButton profile_btn,glowcoins_btn,home_btn,transactions_btn,addrecords_btn,profilepage_btn,report_btn;
    AppCompatTextView user_profilename, user_glowcoins_num;

    PieChart userspendchart;


    HashMap<String, Object> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //firebase api connection
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //firestore linking
        user.put("user_name", user_profilename);
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
        {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });

        //instantiating activities

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
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();


        BasicUserImplementation();


        home_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(home.this, home.class));
            }
        });


        transactions_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(home.this, transactions.class));
            }
        });

        addrecords_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(home.this, addrecord_income.class));
            }
        });

        profilepage_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(home.this, profile.class));
            }
        });

        report_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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

        DynamicPieChart();

    }

    public void BasicUserImplementation()
    {
        user_profilename.setText(firebaseUser.getDisplayName());
    }

    public void DynamicPieChart()
    {
        ArrayList<PieEntry> test_data = new ArrayList<>();
        test_data.add(new PieEntry(10f, "Income"));
        test_data.add(new PieEntry(20f, "Expense"));

        PieDataSet dataSet = new PieDataSet(test_data, "PieEntry");
        List<Integer> customColors = new ArrayList<>();
        customColors.add(Color.parseColor("#61DE70"));
        customColors.add(Color.parseColor("#FF0000"));
        PieData data = new PieData(dataSet);
        dataSet.setColors(customColors);
        userspendchart.setData(data);

        userspendchart.getDescription().setEnabled(false);
        userspendchart.setDrawHoleEnabled(true);
        userspendchart.setTransparentCircleRadius(61f);
        userspendchart.setHoleRadius(70f);
        userspendchart.animateY(1000, Easing.EaseInOutCubic);

        userspendchart.invalidate();


    }
}