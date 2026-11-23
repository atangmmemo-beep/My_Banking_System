# Banking System (CSE202)

Object-oriented banking system in Java with a JavaFX GUI and a MySQL database.

## Packages
- model: Customer, Account, SavingsAccount, InvestmentAccount, ChequeAccount, Transaction, InterestBearing
- view: JavaFX screens
- controller: application logic between the screens and the model
- dao: JDBC classes (CustomerDAO, AccountDAO, TransactionDAO, DBConnection)
- database/schema.sql: MySQL tables
- docs: integration testing report

## How to run
1. Install MySQL and set your password in dao/DBConnection.java
2. Run dao/SampleData.java once (creates bank_db and adds 10 sample customers)
3. Run Launcher.java to open the GUI
4. Log in with thabo / pass1234

## Requirements
JDK 26, JavaFX 26.0.2, MySQL 8, mysql-connector-j

## Author
Atang Lefa Mmemo