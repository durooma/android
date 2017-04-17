package com.durooma.android.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Transaction extends TransactionBody {

    private long id;

    public Transaction(long id, Date date, Long source, Long target, BigDecimal amount, BigDecimal exempt, String description) {
        super(date, source, target, amount, exempt, description);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
