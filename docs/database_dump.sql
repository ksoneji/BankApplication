-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.19 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             11.0.0.5956
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for bank_app
DROP DATABASE IF EXISTS `bank_app`;
CREATE DATABASE IF NOT EXISTS `bank_app` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bank_app`;

-- Dumping structure for table bank_app.accounts
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE IF NOT EXISTS `accounts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL DEFAULT '0',
  `account_type` enum('savings','salary','loan','current') NOT NULL DEFAULT 'current',
  `status` tinyint NOT NULL DEFAULT '1',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `interest_rate` double(22,2) DEFAULT NULL,
  `interest_calculated_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`customer_id`),
  CONSTRAINT `FK_accounts_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bank_app.accounts: ~5 rows (approximately)
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` (`id`, `customer_id`, `account_type`, `status`, `created_date`, `interest_rate`, `interest_calculated_date`) VALUES
	(13, 4, 'current', 1, '2019-02-07 00:00:00', 3.50, '2021-02-13 10:12:00'),
	(15, 4, 'savings', 1, '2019-02-09 21:55:18', 3.50, '2021-02-13 10:12:00'),
	(16, 4, 'savings', 0, '2021-02-10 22:25:46', 3.50, NULL),
	(17, 4, 'savings', 1, '2021-02-10 22:28:08', 3.50, NULL);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;

-- Dumping structure for table bank_app.account_balance
DROP TABLE IF EXISTS `account_balance`;
CREATE TABLE IF NOT EXISTS `account_balance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL DEFAULT '0',
  `credit` double(22,2) DEFAULT NULL,
  `debit` double(22,2) DEFAULT NULL,
  `balance` double(22,2) NOT NULL DEFAULT '0.00',
  `comment` varchar(100) DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK1_ACCOUNT_ID` (`account_id`),
  CONSTRAINT `FK1_ACCOUNT_ID` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bank_app.account_balance: ~5 rows (approximately)
/*!40000 ALTER TABLE `account_balance` DISABLE KEYS */;
INSERT INTO `account_balance` (`id`, `account_id`, `credit`, `debit`, `balance`, `comment`, `date`) VALUES
	(53, 13, 2000.00, NULL, 2000.00, 'Initial Payment', '2021-02-06 13:19:23'),
	(54, 13, NULL, 100.00, 1900.00, 'Online Shopping', '2021-02-06 13:21:58'),
	(56, 13, 2000.00, NULL, 3900.00, 'Gift', '2021-02-08 20:34:13'),
	(58, 15, 2000.00, NULL, 2000.00, 'amount credited', '2021-02-13 10:06:55'),
	(60, 13, 136.50, NULL, 4036.50, 'Annual Interest Credited', '2021-02-13 10:12:00'),
	(61, 15, 70.00, NULL, 2070.00, 'Annual Interest Credited', '2021-02-13 10:12:00'),
	(62, 15, NULL, 100.00, 1970.00, NULL, '2021-02-15 00:07:27'),
	(63, 13, 100.00, NULL, 4136.50, 'Transfer from other acct', '2021-02-15 00:07:27');
/*!40000 ALTER TABLE `account_balance` ENABLE KEYS */;

-- Dumping structure for table bank_app.customer
DROP TABLE IF EXISTS `customer`;
CREATE TABLE IF NOT EXISTS `customer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- Dumping data for table bank_app.customer: ~1 rows (approximately)
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` (`id`, `first_name`, `last_name`, `address`) VALUES
	(4, 'Tom', 'Alter', 'North First street, CA');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;

-- Dumping structure for table bank_app.customer_kyc
DROP TABLE IF EXISTS `customer_kyc`;
CREATE TABLE IF NOT EXISTS `customer_kyc` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL DEFAULT '0',
  `first_name` varchar(100) NOT NULL,
  `middle_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `email_address` varchar(100) DEFAULT NULL,
  `modified_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK__customer` (`customer_id`),
  CONSTRAINT `FK__customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bank_app.customer_kyc: ~0 rows (approximately)
/*!40000 ALTER TABLE `customer_kyc` DISABLE KEYS */;
INSERT INTO `customer_kyc` (`id`, `customer_id`, `first_name`, `middle_name`, `last_name`, `address`, `email_address`, `modified_date`) VALUES
	(5, 4, 'Alter', 'ww12', 'Tom', '123 Old Oaks, Palo Alto', NULL, '2021-02-13 14:39:48');
/*!40000 ALTER TABLE `customer_kyc` ENABLE KEYS */;

-- Dumping structure for table bank_app.employee
DROP TABLE IF EXISTS `employee`;
CREATE TABLE IF NOT EXISTS `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0',
  `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `is_admin` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`username`) USING BTREE,
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table bank_app.employee: ~1 rows (approximately)
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` (`id`, `username`, `password`, `first_name`, `last_name`, `email`, `is_admin`) VALUES
	(6, 'adamsmith', '$2a$10$D1qcoGrRjjvupFaPMhuaGOefG49uVCZWvS0NaiLCgrQOdGcwI/4ty', 'Adam', 'Smith', 'test123@1248test.com', 1),
	(8, 'rupamsharma', '$2a$10$FXYq9PZBsFPPA1cDyfqkCutSyOncEd0Y.0NYgEEz0rsiJKfe2GG2u', 'Rupam', 'Sharma', 'test123@1245test.com', 0);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
