package com.durooma.android.model;

import java.math.BigDecimal;

public class TransactionBody {

    private Long source;
    private Long target;
    private BigDecimal amount;
    private BigDecimal huquqAmount;

    public TransactionBody(Long source, Long target, BigDecimal amount, BigDecimal huquqAmount) {
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.huquqAmount = huquqAmount;
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

    public BigDecimal getHuquqAmount() {
        return huquqAmount;
    }

    public void setHuquqAmount(BigDecimal huquqAmount) {
        this.huquqAmount = huquqAmount;
    }
}
