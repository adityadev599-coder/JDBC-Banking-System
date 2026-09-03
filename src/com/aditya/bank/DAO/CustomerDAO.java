package com.aditya.bank.DAO;

import com.aditya.bank.model.Customer;
import com.aditya.bank.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    // Method 1: Add customer
    public boolean addCustomer(Customer customer) {

        String sql = "INSERT INTO customers(name, email, phone, password) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Method 2: Login customer
    public Customer login(String email, String password) {

        String sql = "SELECT * FROM customers WHERE email = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Customer customer = new Customer(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password")
                );

                customer.setCustomerId(
                        resultSet.getInt("customer_id")
                );

                return customer;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}