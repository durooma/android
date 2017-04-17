package com.durooma.android.model;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionBody {

    private Date date;
    private Long source;
    private Long target;
    private BigDecimal amount;
    private BigDecimal exempt;
    private String description;

    public TransactionBody(Date date, Long source, Long target, BigDecimal amount, BigDecimal exempt, String description) {
        this.date = date;
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.exempt = exempt;
        this.description = description;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getExempt() {
        return exempt;
    }

    public void setExempt(BigDecimal exempt) {
        this.exempt = exempt;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
