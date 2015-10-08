CREATE DATABASE contact_db;
USE contact_db;

CREATE TABLE contacts (
name VARCHAR(20) NOT NULL,
surname VARCHAR(30) NOT NULL,
login VARCHAR(15) NOT NULL,
email VARCHAR(50) NOT NULL,
phone_number VARCHAR(20) NOT NULL,
PRIMARY KEY (login)
);