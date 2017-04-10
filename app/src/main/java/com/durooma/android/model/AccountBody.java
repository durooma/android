package com.durooma.android.model;

public class AccountBody {

    private String name;
    private double initialBalance;

    public AccountBody(String name, double initialBalance) {
        this.name = name;
        this.initialBalance = initialBalance;
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

    @Override
    public String toString() {
        return this.name;
    }
}
