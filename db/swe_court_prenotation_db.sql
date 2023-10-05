-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 05, 2023 at 03:33 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `swe_court_prenotation_db`
--

-- --------------------------------------------------------

--
-- Stand-in structure for view `booked`
-- (See below for the actual view)
--
CREATE TABLE `booked` (
`court` int(11)
,`date` date
,`time_slot` int(11)
);

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

CREATE TABLE `client` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `telephone_number` int(11) DEFAULT NULL,
  `is_premium` tinyint(1) NOT NULL,
  `points` int(11) NOT NULL DEFAULT 0,
  `wallet` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `client`
--

INSERT INTO `client` (`id`, `name`, `surname`, `email`, `password`, `telephone_number`, `is_premium`, `points`, `wallet`) VALUES
(24, 'kev', 'mad', 'madrigali541@gmail.com', 'kev', 123, 0, 0, 24),
(42, 'andrea', 'innocenti', 'andreinno2218@gmail.com', 'andre', 0, 1, 0, 42),
(60, 'mario', 'marini', 'a@s', 'a', 0, 0, 0, 60),
(63, 'a', 'a', 'a@a', 'a', 0, 0, 0, 63);

-- --------------------------------------------------------

--
-- Table structure for table `court`
--

CREATE TABLE `court` (
  `type` int(11) DEFAULT NULL,
  `id` int(11) NOT NULL
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
  `id` int(11) NOT NULL,
  `client` int(11) NOT NULL,
  `end_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `premium_subs`
--

INSERT INTO `premium_subs` (`id`, `client`, `end_date`) VALUES
(2, 42, '2023-10-05');

-- --------------------------------------------------------

--
-- Table structure for table `prices`
--

CREATE TABLE `prices` (
  `id` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `price` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prices`
--

INSERT INTO `prices` (`id`, `type`, `price`) VALUES
(1, 1, 20),
(2, 2, 18),
(3, 3, 22),
(4, 4, 20);

-- --------------------------------------------------------

--
-- Table structure for table `rentingkit_reservation`
--

CREATE TABLE `rentingkit_reservation` (
  `id` int(11) NOT NULL,
  `reservation` int(11) NOT NULL,
  `renting_kit` int(11) NOT NULL,
  `num_of_rents` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rentingkit_reservation`
--

INSERT INTO `rentingkit_reservation` (`id`, `reservation`, `renting_kit`, `num_of_rents`) VALUES
(5, 12, 1, 2),
(6, 13, 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `renting_kits`
--

CREATE TABLE `renting_kits` (
  `id` int(11) NOT NULL,
  `type` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `price` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

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
  `id` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `court` int(11) DEFAULT NULL,
  `client` int(11) DEFAULT NULL,
  `time_slot` int(11) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `time_slots`
--

CREATE TABLE `time_slots` (
  `id` int(11) NOT NULL,
  `start_hour` varchar(5) NOT NULL,
  `finish_hour` varchar(5) NOT NULL
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
  `id` int(11) NOT NULL,
  `type_of_court` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `type_of_court`
--

INSERT INTO `type_of_court` (`id`, `type_of_court`) VALUES
(1, 'clay'),
(2, 'hard'),
(3, 'grass'),
(4, 'padel');

-- --------------------------------------------------------

--
-- Table structure for table `wallet`
--

CREATE TABLE `wallet` (
  `id` int(11) NOT NULL,
  `balance` float DEFAULT NULL,
  `client` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wallet`
--

INSERT INTO `wallet` (`id`, `balance`, `client`) VALUES
(24, 50, 24),
(42, 30, 42),
(60, 0, 60),
(63, 0, 63);

-- --------------------------------------------------------

--
-- Structure for view `booked`
--
DROP TABLE IF EXISTS `booked`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `booked`  AS   (select `reservation`.`court` AS `court`,`reservation`.`date` AS `date`,`reservation`.`time_slot` AS `time_slot` from `reservation` where `reservation`.`court` = 1)  ;

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
-- Indexes for table `prices`
--
ALTER TABLE `prices`
  ADD PRIMARY KEY (`id`),
  ADD KEY `type_idx` (`type`);

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
  ADD KEY `reservation_client` (`time_slot`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;

--
-- AUTO_INCREMENT for table `premium_subs`
--
ALTER TABLE `premium_subs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `rentingkit_reservation`
--
ALTER TABLE `rentingkit_reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `renting_kits`
--
ALTER TABLE `renting_kits`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

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
-- Constraints for table `prices`
--
ALTER TABLE `prices`
  ADD CONSTRAINT `type` FOREIGN KEY (`type`) REFERENCES `type_of_court` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `wallet`
--
ALTER TABLE `wallet`
  ADD CONSTRAINT `client_wallet` FOREIGN KEY (`client`) REFERENCES `client` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

DELIMITER $$
--
-- Events
--
CREATE DEFINER=`root`@`localhost` EVENT `update_premium_subs` ON SCHEDULE EVERY 1 HOUR STARTS '2023-10-05 15:35:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    DELETE FROM premium_subs WHERE end_date < CURDATE();
  UPDATE client SET is_premium = 0 WHERE ((is_premium = 1) AND (client.id NOT IN (SELECT client FROM premium_subs)));
END$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
