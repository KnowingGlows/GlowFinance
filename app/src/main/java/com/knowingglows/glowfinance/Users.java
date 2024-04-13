package com.knowingglows.glowfinance;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {
    private String username;
    private double balance;

    public Users() {
        // Default constructor required for Firestore
    }

    // Parameterized constructor with username
    public Users(String username) {
        this.username = username;
        this.balance = 0.0; // Set balance to 0 by default
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for balance
    public double getBalance() {
        return balance;
    }

    // Setter for balance
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
