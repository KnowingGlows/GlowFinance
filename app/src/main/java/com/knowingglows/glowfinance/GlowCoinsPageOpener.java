package com.knowingglows.glowfinance;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatButton;

public class GlowCoinsPageOpener {

    public static void setButtonClickListener(Activity currentActivity, AppCompatButton button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currentActivity, glowcoinspage.class);
                currentActivity.startActivity(intent);
            }
        });
    }
}
