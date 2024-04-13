package com.knowingglows.glowfinance;
import com.google.firebase.firestore.PropertyName;
import java.util.Date;

public class Income {
    @PropertyName("name")
    private String name;

    @PropertyName("date")
    private Date date;

    @PropertyName("amount")
    private double amount;

    @PropertyName("description")
    private String description;

    // Default constructor required for Firestore serialization
    public Income() {
    }

    // Constructor with parameters
    public Income(String name, Date date, double amount, String description) {
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }
    // Constructors, getters, and setters
}
