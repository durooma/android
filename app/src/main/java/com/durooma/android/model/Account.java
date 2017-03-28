package com.durooma.android.model;

public class Account extends AccountBody {

    private long id;

    public Account(long id, String name, double initialBalance) {
        super(name, initialBalance);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
