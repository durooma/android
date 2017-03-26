package com.durooma.android.model;

public class Account {

    private long id;
    private String name;
    private double initialBalance;

    public Account(long id, String name, double initialBalance) {
        this.id = id;
        this.name = name;
        this.initialBalance = initialBalance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }

}
