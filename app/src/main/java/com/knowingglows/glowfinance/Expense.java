package com.knowingglows.glowfinance;
import com.google.firebase.firestore.PropertyName;
import java.util.Date;

public class Expense {
    @PropertyName("name")
    private String name;

    @PropertyName("date")
    private Date date;

    @PropertyName("amount")
    private double amount;

    @PropertyName("description")
    private String description;

    // Constructors, getters, and setters

    public Expense() {
    }

    // Constructor with parameters
    public Expense(String name, Date date, double amount, String description) {
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }
}


