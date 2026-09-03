package com.aditya.bank.model;

import java.math.BigDecimal;

public class Account {

    private int accountId;
    private int customerId;
    private String accountNumber;
    private BigDecimal balance;

    public Account(int customerId, String accountNumber, BigDecimal balance) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}