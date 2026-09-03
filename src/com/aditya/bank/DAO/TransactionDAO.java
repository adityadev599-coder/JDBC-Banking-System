package com.aditya.bank.DAO;

import com.aditya.bank.model.Transaction;
import com.aditya.bank.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public boolean addTransaction(Transaction transaction) {

        String sql = "INSERT INTO transactions " +
                "(account_id, transaction_type, amount) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, transaction.getAccountId());
            preparedStatement.setString(2, transaction.getTransactionType());
            preparedStatement.setBigDecimal(3, transaction.getAmount());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Transaction> getTransactionHistory(int accountId) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM transactions " +
                "WHERE account_id = ? " +
                "ORDER BY transaction_date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, accountId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Transaction transaction = new Transaction(
                        resultSet.getInt("account_id"),
                        resultSet.getString("transaction_type"),
                        resultSet.getBigDecimal("amount")
                );

                transaction.setTransactionId(
                        resultSet.getInt("transaction_id")
                );

                transaction.setTransactionDate(
                        resultSet.getTimestamp("transaction_date")
                );

                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
}