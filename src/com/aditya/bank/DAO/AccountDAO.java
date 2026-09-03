package com.aditya.bank.DAO;

import com.aditya.bank.model.Account;
import com.aditya.bank.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class AccountDAO {

    public boolean createAccount(Account account) {

        String sql = "INSERT INTO accounts(customer_id, account_number, balance) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, account.getCustomerId());
            preparedStatement.setString(2, account.getAccountNumber());
            preparedStatement.setBigDecimal(3, account.getBalance());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deposit(String accountNumber, BigDecimal amount) {

        String updateSql = "UPDATE accounts " +
                "SET balance = balance + ? " +
                "WHERE account_number = ?";

        String transactionSql = "INSERT INTO transactions " +
                "(account_id, transaction_type, amount) " +
                "SELECT account_id, 'DEPOSIT', ? " +
                "FROM accounts WHERE account_number = ?";

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();

            connection.setAutoCommit(false);

            try (PreparedStatement updateStatement =
                         connection.prepareStatement(updateSql);
                 PreparedStatement transactionStatement =
                         connection.prepareStatement(transactionSql)) {

                // Update balance
                updateStatement.setBigDecimal(1, amount);
                updateStatement.setString(2, accountNumber);

                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected == 0) {
                    connection.rollback();
                    return false;
                }

                // Record transaction
                transactionStatement.setBigDecimal(1, amount);
                transactionStatement.setString(2, accountNumber);

                transactionStatement.executeUpdate();

                // Commit both operations
                connection.commit();

                return true;
            }

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

            e.printStackTrace();
            return false;

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean withdraw(String accountNumber, BigDecimal amount) {

        String updateSql = "UPDATE accounts " +
                "SET balance = balance - ? " +
                "WHERE account_number = ? AND balance >= ?";

        String transactionSql = "INSERT INTO transactions " +
                "(account_id, transaction_type, amount) " +
                "SELECT account_id, 'WITHDRAW', ? " +
                "FROM accounts WHERE account_number = ?";

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();

            // Start transaction
            connection.setAutoCommit(false);

            try (PreparedStatement updateStatement =
                         connection.prepareStatement(updateSql);
                 PreparedStatement transactionStatement =
                         connection.prepareStatement(transactionSql)) {

                // Deduct money
                updateStatement.setBigDecimal(1, amount);
                updateStatement.setString(2, accountNumber);
                updateStatement.setBigDecimal(3, amount);

                int rowsAffected = updateStatement.executeUpdate();

                // Account doesn't exist OR insufficient balance
                if (rowsAffected == 0) {
                    connection.rollback();
                    return false;
                }

                // Record transaction
                transactionStatement.setBigDecimal(1, amount);
                transactionStatement.setString(2, accountNumber);

                transactionStatement.executeUpdate();

                // Everything successful
                connection.commit();

                return true;
            }

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

            e.printStackTrace();
            return false;

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean transfer(
            String fromAccount,
            String toAccount,
            BigDecimal amount) {

        String withdrawSql = "UPDATE accounts " +
                "SET balance = balance - ? " +
                "WHERE account_number = ? AND balance >= ?";

        String depositSql = "UPDATE accounts " +
                "SET balance = balance + ? " +
                "WHERE account_number = ?";

        String transactionSql = "INSERT INTO transactions " +
                "(account_id, transaction_type, amount) " +
                "SELECT account_id, ?, ? " +
                "FROM accounts WHERE account_number = ?";

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();

            // Start transaction
            connection.setAutoCommit(false);

            // 1. Withdraw from sender
            try (PreparedStatement withdrawStatement =
                         connection.prepareStatement(withdrawSql)) {

                withdrawStatement.setBigDecimal(1, amount);
                withdrawStatement.setString(2, fromAccount);
                withdrawStatement.setBigDecimal(3, amount);

                int rowsAffected = withdrawStatement.executeUpdate();

                if (rowsAffected == 0) {
                    connection.rollback();
                    return false;
                }
            }

            // 2. Deposit into receiver
            try (PreparedStatement depositStatement =
                         connection.prepareStatement(depositSql)) {

                depositStatement.setBigDecimal(1, amount);
                depositStatement.setString(2, toAccount);

                int rowsAffected = depositStatement.executeUpdate();

                if (rowsAffected == 0) {
                    connection.rollback();
                    return false;
                }
            }

            // 3. Record TRANSFER_OUT
            try (PreparedStatement transactionStatement =
                         connection.prepareStatement(transactionSql)) {

                transactionStatement.setString(1, "TRANSFER_OUT");
                transactionStatement.setBigDecimal(2, amount);
                transactionStatement.setString(3, fromAccount);

                transactionStatement.executeUpdate();
            }

            // 4. Record TRANSFER_IN
            try (PreparedStatement transactionStatement =
                         connection.prepareStatement(transactionSql)) {

                transactionStatement.setString(1, "TRANSFER_IN");
                transactionStatement.setBigDecimal(2, amount);
                transactionStatement.setString(3, toAccount);

                transactionStatement.executeUpdate();
            }

            // Everything successful
            connection.commit();

            return true;

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

            e.printStackTrace();
            return false;

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Account getAccount(String accountNumber) {

        String sql = "SELECT * FROM accounts WHERE account_number = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            preparedStatement.setString(1, accountNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Account account = new Account(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("account_number"),
                        resultSet.getBigDecimal("balance")
                );

                account.setAccountId(
                        resultSet.getInt("account_id")
                );

                return account;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Account getAccountByCustomerId(int customerId) {

        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Account account = new Account(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("account_number"),
                        resultSet.getBigDecimal("balance")
                );

                account.setAccountId(
                        resultSet.getInt("account_id")
                );

                return account;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}