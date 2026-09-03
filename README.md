# JDBC Banking System

A console-based banking application built using **Java, JDBC, and MySQL**.  
The project demonstrates how a backend banking system can interact with a relational database using JDBC while following a layered architecture.

## 🚀 Features

- Customer registration
- Customer login
- Bank account creation
- Check account balance
- Deposit money
- Withdraw money
- Transfer money between accounts
- Transaction history
- Logout functionality
- JDBC transaction management with commit and rollback
- PreparedStatements for secure SQL execution

## 🛠️ Tech Stack

- **Java**
- **JDBC**
- **MySQL**
- **IntelliJ IDEA**
- **Git & GitHub**

## 📂 Project Structure

```text
JDBC-Banking-System/
│
├── src/
│   └── com/aditya/bank/
│       ├── DAO/
│       │   ├── AccountDAO.java
│       │   ├── CustomerDAO.java
│       │   └── TransactionDAO.java
│       │
│       ├── model/
│       │   ├── Account.java
│       │   ├── Customer.java
│       │   └── Transaction.java
│       │
│       ├── service/
│       │   └── BankingService.java
│       │
│       ├── util/
│       │   └── DBConnection.java
│       │
│       └── Main.java
│
├── .env.example
├── .gitignore
└── README.md
