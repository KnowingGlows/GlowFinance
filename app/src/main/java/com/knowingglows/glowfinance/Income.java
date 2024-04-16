package com.knowingglows.glowfinance;
import com.google.firebase.firestore.PropertyName;
import java.util.Date;

public class Income {
    private Double amount;
    private String date;
    private String source;
    private String description;

    public Income(Double amount, String date, String source, String description) {
        this.amount = amount;
        this.date = date;
        this.source = source;
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }
}