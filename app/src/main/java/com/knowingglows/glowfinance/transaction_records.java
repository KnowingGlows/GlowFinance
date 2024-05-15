package com.knowingglows.glowfinance;

public class transaction_records {
    private String source;
    private Double amount;
    private String date;
    private String type;
    private String description;

    // Constructors
    public transaction_records() { } // Empty constructor

    public transaction_records(String source, Double amount, String date, String description,String type) {
        this.source = source;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    // Getters and Setters (non-static)
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}