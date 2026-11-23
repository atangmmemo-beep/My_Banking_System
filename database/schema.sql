CREATE DATABASE IF NOT EXISTS bank_db;
USE bank_db;

CREATE TABLE IF NOT EXISTS customers (
                                         customer_id VARCHAR(10)  PRIMARY KEY,
    first_name  VARCHAR(50)  NOT NULL,
    surname     VARCHAR(50)  NOT NULL,
    address     VARCHAR(100),
    phone       VARCHAR(20),
    email       VARCHAR(100),
    username    VARCHAR(30)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS accounts (
                                        account_number   VARCHAR(15)  PRIMARY KEY,
    account_type     VARCHAR(12)  NOT NULL,   -- SAVINGS, INVESTMENT or CHEQUE
    balance          DOUBLE       NOT NULL,
    branch           VARCHAR(50),
    date_opened      VARCHAR(30)  NOT NULL,
    employer_name    VARCHAR(80),             -- only used by CHEQUE accounts
    employer_address VARCHAR(100),            -- only used by CHEQUE accounts
    customer_id      VARCHAR(10)  NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
    );

CREATE TABLE IF NOT EXISTS transactions (
                                            transaction_id INT AUTO_INCREMENT PRIMARY KEY,
                                            account_number VARCHAR(15)  NOT NULL,
    type           VARCHAR(10)  NOT NULL,     -- DEPOSIT, WITHDRAW or INTEREST
    amount         DOUBLE       NOT NULL,
    date_time      VARCHAR(40)  NOT NULL,
    balance_after  DOUBLE       NOT NULL,
    note           VARCHAR(100),
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
    );