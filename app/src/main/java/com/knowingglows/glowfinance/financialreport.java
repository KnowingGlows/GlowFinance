package com.knowingglows.glowfinance;
import static androidx.core.content.ContextCompat.startActivity;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class financialreport {
    public static void composeEmail(Context context, String name, String date, String specificInfo, String email) {
        String subject = "Create Financial Report";
        String body = "Name: " + name + "\n" +
                "Date for the report: " + date + "\n" +
                "Email : " + email + "\n" +
                "Specific Info: " + specificInfo;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"knowingglowsofficial@gmail.com"});

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, "Send Email"));
        }
    }
}


