package com.durooma.android.model;

import java.math.BigDecimal;

public class TransactionBody {

    private Long source;
    private Long target;
    private BigDecimal amount;
    private BigDecimal exempt;

    public TransactionBody(Long source, Long target, BigDecimal amount, BigDecimal exempt) {
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.exempt = exempt;
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
}
