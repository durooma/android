package com.durooma.android.model;

import java.math.BigDecimal;

public class Transaction extends TransactionBody {

    private long id;

    public Transaction(long id, Long source, Long target, BigDecimal amount, BigDecimal huquqAmount) {
        super(source, target, amount, huquqAmount);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
