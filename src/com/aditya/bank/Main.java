package com.aditya.bank;

import com.aditya.bank.model.Account;
import com.aditya.bank.service.BankingService;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BankingService bankingService =
                new BankingService();

        System.out.println("===== JDBC BANKING SYSTEM =====");

        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {

            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter phone: ");
            String phone = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            boolean result =
                    bankingService.registerCustomer(
                            name,
                            email,
                            phone,
                            password
                    );

            if (result) {
                System.out.println("Registration successful!");
            } else {
                System.out.println("Registration failed!");
            }

        } else if (choice == 2) {

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            var customer = bankingService.login(email, password);

            if (customer != null) {

                System.out.println(
                        "Login successful! Welcome, "
                                + customer.getName()
                );

                Account account =
                        bankingService.getAccountByCustomerId(
                                customer.getCustomerId()
                        );

                if (account == null) {

                    System.out.println(
                            "No bank account found."
                    );

                } else {

                    boolean loggedIn = true;

                    while (loggedIn) {

                        System.out.println(
                                "\n========== WELCOME =========="
                        );
                        System.out.println("1. Check Balance");
                        System.out.println("2. Deposit");
                        System.out.println("3. Withdraw");
                        System.out.println("4. Transfer");
                        System.out.println("5. Transaction History");
                        System.out.println("6. Logout");

                        System.out.print("Enter your choice: ");
                        int option = scanner.nextInt();

                        switch (option) {

                            case 1:

                                System.out.println(
                                        "Current Balance: ₹"
                                                + account.getBalance()
                                );

                                break;

                            case 2:

                                System.out.print(
                                        "Enter deposit amount: "
                                );

                                double depositAmount =
                                        scanner.nextDouble();

                                if (depositAmount <= 0) {

                                    System.out.println(
                                            "Amount must be greater than 0."
                                    );

                                    break;
                                }

                                BigDecimal deposit =
                                        new BigDecimal(
                                                String.valueOf(
                                                        depositAmount
                                                )
                                        );

                                boolean depositResult =
                                        bankingService.deposit(
                                                account.getAccountNumber(),
                                                deposit
                                        );

                                if (depositResult) {

                                    account.setBalance(
                                            account.getBalance()
                                                    .add(deposit)
                                    );

                                    System.out.println(
                                            "Deposit successful!"
                                    );

                                    System.out.println(
                                            "Updated Balance: ₹"
                                                    + account.getBalance()
                                    );

                                } else {

                                    System.out.println(
                                            "Deposit failed!"
                                    );
                                }

                                break;

                            case 3:

                                System.out.println(
                                        "Withdraw feature coming soon..."
                                );

                                break;

                            case 4:

                                System.out.println(
                                        "Transfer feature coming soon..."
                                );

                                break;

                            case 5:

                                System.out.println(
                                        "Transaction History feature coming soon..."
                                );

                                break;

                            case 6:

                                loggedIn = false;

                                System.out.println(
                                        "Logged out successfully."
                                );

                                break;

                            default:

                                System.out.println(
                                        "Invalid choice!"
                                );
                        }
                    }
                }

            } else {

                System.out.println(
                        "Invalid email or password!"
                );
            }

        } else if (choice == 3) {

            System.out.println(
                    "Thank you for using the banking system."
            );

        } else {

            System.out.println(
                    "Invalid choice!"
            );
        }

        scanner.close();
    }
}