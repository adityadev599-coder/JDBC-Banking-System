package com.aditya.bank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {

    private int transactionId;
    private int accountId;
    private String transactionType;
    private BigDecimal amount;
    private Timestamp transactionDate;

    public Transaction(int accountId, String transactionType, BigDecimal amount) {
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }
}