-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 40s.h.filess.io:3307
-- Generation Time: Oct 23, 2023 at 04:04 PM
-- Server version: 8.0.29-21
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `swecourtprentiondb_recordfell`
--

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

CREATE TABLE `client` (
  `id` int NOT NULL,
  `name` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `surname` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(45) COLLATE utf8mb4_general_ci NOT NULL,
  `telephone_number` int DEFAULT NULL,
  `is_premium` tinyint(1) NOT NULL,
  `points` int NOT NULL DEFAULT '0',
  `wallet` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `client`
--

INSERT INTO `client` (`id`, `name`, `surname`, `email`, `password`, `telephone_number`, `is_premium`, `points`, `wallet`) VALUES
(160, 'Andrea', 'Innocenti', 'Andreinno2218@gmail.com', 'andre', 0, 1, 20, 160),
(180, 'Kevin', 'Madrigali', 'madrigali541@gmail.com', 'kevin', 0, 1, 10, 180),
(207, 'Nome', 'Cognome', 'mail@email.com', 'mail', 0, 0, 0, 207);

-- --------------------------------------------------------

--
-- Table structure for table `court`
--

CREATE TABLE `court` (
  `type` int DEFAULT NULL,
  `id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `court`
--

INSERT INTO `court` (`type`, `id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 4),
(2, 5),
(2, 6),
(3, 7),
(3, 8),
(4, 9),
(4, 10);

-- --------------------------------------------------------

--
-- Table structure for table `premium_subs`
--

CREATE TABLE `premium_subs` (
  `id` int NOT NULL,
  `client` int NOT NULL,
  `end_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `premium_subs`
--

INSERT INTO `premium_subs` (`id`, `client`, `end_date`) VALUES
(18, 160, '2025-10-15'),
(38, 180, '2024-10-23');

-- --------------------------------------------------------

--
-- Table structure for table `rentingkit_reservation`
--

CREATE TABLE `rentingkit_reservation` (
  `id` int NOT NULL,
  `reservation` int NOT NULL,
  `renting_kit` int NOT NULL,
  `num_of_rents` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rentingkit_reservation`
--

INSERT INTO `rentingkit_reservation` (`id`, `reservation`, `renting_kit`, `num_of_rents`) VALUES
(63, 147, 1, 2),
(64, 148, 1, 4),
(65, 149, 1, 2),
(66, 150, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `renting_kits`
--

CREATE TABLE `renting_kits` (
  `id` int NOT NULL,
  `type` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `price` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `renting_kits`
--

INSERT INTO `renting_kits` (`id`, `type`, `price`) VALUES
(1, 'tennis', 5),
(2, 'padel', 7);

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

CREATE TABLE `reservation` (
  `id` int NOT NULL,
  `date` date DEFAULT NULL,
  `court` int DEFAULT NULL,
  `client` int NOT NULL,
  `time_slot` int NOT NULL,
  `price` float NOT NULL,
  `isPremium` tinyint NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reservation`
--

INSERT INTO `reservation` (`id`, `date`, `court`, `client`, `time_slot`, `price`, `isPremium`) VALUES
(145, '2023-11-11', 1, 160, 5, 27, 1),
(146, '2023-10-30', 10, 160, 6, 30.6, 1),
(147, '2023-11-07', 3, 160, 8, 27, 1),
(148, '2023-11-20', 4, 160, 5, 0, 1),
(149, '2023-11-15', 6, 160, 9, 25.2, 1),
(150, '2023-12-03', 6, 160, 9, 20.7, 1);

-- --------------------------------------------------------

--
-- Table structure for table `time_slots`
--

CREATE TABLE `time_slots` (
  `id` int NOT NULL,
  `start_hour` varchar(5) COLLATE utf8mb4_general_ci NOT NULL,
  `finish_hour` varchar(5) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `time_slots`
--

INSERT INTO `time_slots` (`id`, `start_hour`, `finish_hour`) VALUES
(1, '8', '9'),
(2, '9', '10'),
(3, '10', '11'),
(4, '11', '12'),
(5, '12', '13'),
(6, '13', '14'),
(7, '14', '15'),
(8, '15', '16'),
(9, '16', '17'),
(10, '17', '18'),
(11, '18', '19'),
(12, '19', '20'),
(13, '20', '21'),
(14, '21', '22');

-- --------------------------------------------------------

--
-- Table structure for table `type_of_court`
--

CREATE TABLE `type_of_court` (
  `id` int NOT NULL,
  `type_of_court` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `price` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `type_of_court`
--

INSERT INTO `type_of_court` (`id`, `type_of_court`, `price`) VALUES
(1, 'clay', 20),
(2, 'hard', 18),
(3, 'grass', 22),
(4, 'padel', 20);

-- --------------------------------------------------------

--
-- Table structure for table `wallet`
--

CREATE TABLE `wallet` (
  `id` int NOT NULL,
  `balance` float DEFAULT NULL,
  `client` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wallet`
--

INSERT INTO `wallet` (`id`, `balance`, `client`) VALUES
(160, 9927.1, 160),
(180, 12.8, 180),
(207, 0, 207);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `wallet_idx` (`wallet`);

--
-- Indexes for table `court`
--
ALTER TABLE `court`
  ADD PRIMARY KEY (`id`),
  ADD KEY `type_idx` (`type`);

--
-- Indexes for table `premium_subs`
--
ALTER TABLE `premium_subs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sub_client` (`client`);

--
-- Indexes for table `rentingkit_reservation`
--
ALTER TABLE `rentingkit_reservation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `reservation` (`reservation`),
  ADD KEY `renting_kit` (`renting_kit`);

--
-- Indexes for table `renting_kits`
--
ALTER TABLE `renting_kits`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `client_idx` (`client`),
  ADD KEY `reservation_time_slot` (`time_slot`) USING BTREE;

--
-- Indexes for table `time_slots`
--
ALTER TABLE `time_slots`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `type_of_court`
--
ALTER TABLE `type_of_court`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `wallet`
--
ALTER TABLE `wallet`
  ADD PRIMARY KEY (`id`),
  ADD KEY `client_wallet` (`client`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `client`
--
ALTER TABLE `client`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=220;

--
-- AUTO_INCREMENT for table `premium_subs`
--
ALTER TABLE `premium_subs`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;

--
-- AUTO_INCREMENT for table `rentingkit_reservation`
--
ALTER TABLE `rentingkit_reservation`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT for table `renting_kits`
--
ALTER TABLE `renting_kits`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=151;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `client`
--
ALTER TABLE `client`
  ADD CONSTRAINT `wallet_idx` FOREIGN KEY (`wallet`) REFERENCES `wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `court`
--
ALTER TABLE `court`
  ADD CONSTRAINT `type_of_court` FOREIGN KEY (`type`) REFERENCES `type_of_court` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `premium_subs`
--
ALTER TABLE `premium_subs`
  ADD CONSTRAINT `sub_client` FOREIGN KEY (`client`) REFERENCES `client` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `rentingkit_reservation`
--
ALTER TABLE `rentingkit_reservation`
  ADD CONSTRAINT `renting_kit` FOREIGN KEY (`renting_kit`) REFERENCES `renting_kits` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reservation` FOREIGN KEY (`reservation`) REFERENCES `reservation` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `client_idx` FOREIGN KEY (`client`) REFERENCES `client` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reservation_time_slot` FOREIGN KEY (`time_slot`) REFERENCES `time_slots` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `wallet`
--
ALTER TABLE `wallet`
  ADD CONSTRAINT `client_wallet` FOREIGN KEY (`client`) REFERENCES `client` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

DELIMITER $$
--
-- Events
--
CREATE DEFINER=`swecourtprentiondb_recordfell`@`%` EVENT `update_premium_subs` ON SCHEDULE EVERY 1 HOUR STARTS '2023-10-06 19:00:30' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    DELETE FROM premium_subs WHERE end_date < CURDATE();
  UPDATE client SET is_premium = 0 WHERE ((is_premium = 1) AND (client.id NOT IN (SELECT client FROM premium_subs)));
END$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
