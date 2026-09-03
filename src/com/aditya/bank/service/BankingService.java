package com.aditya.bank.service;

import com.aditya.bank.DAO.AccountDAO;
import com.aditya.bank.DAO.CustomerDAO;
import com.aditya.bank.DAO.TransactionDAO;
import com.aditya.bank.model.Account;
import com.aditya.bank.model.Customer;
import com.aditya.bank.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class BankingService {

    private final CustomerDAO customerDAO;
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public BankingService() {
        customerDAO = new CustomerDAO();
        accountDAO = new AccountDAO();
        transactionDAO = new TransactionDAO();
    }

    public boolean registerCustomer(
            String name,
            String email,
            String phone,
            String password) {

        Customer customer =
                new Customer(name, email, phone, password);

        return customerDAO.addCustomer(customer);
    }

    public Customer login(String email, String password) {

        return customerDAO.login(email, password);
    }

    public boolean createAccount(
            int customerId,
            String accountNumber,
            BigDecimal initialBalance) {

        Account account =
                new Account(
                        customerId,
                        accountNumber,
                        initialBalance
                );

        return accountDAO.createAccount(account);
    }

    public boolean deposit(
            String accountNumber,
            BigDecimal amount) {

        return accountDAO.deposit(accountNumber, amount);
    }

    public boolean withdraw(
            String accountNumber,
            BigDecimal amount) {

        return accountDAO.withdraw(accountNumber, amount);
    }

    public boolean transfer(
            String fromAccount,
            String toAccount,
            BigDecimal amount) {

        return accountDAO.transfer(
                fromAccount,
                toAccount,
                amount
        );
    }

    public List<Transaction> getTransactionHistory(
            int accountId) {

        return transactionDAO.getTransactionHistory(accountId);
    }

    public Account getAccount(String accountNumber) {

        return accountDAO.getAccount(accountNumber);
    }

    public Account getAccountByCustomerId(int customerId) {
        return accountDAO.getAccountByCustomerId(customerId);
    }
}