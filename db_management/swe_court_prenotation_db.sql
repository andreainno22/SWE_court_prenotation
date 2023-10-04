-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: sql11.freemysqlhosting.net
-- Generation Time: Oct 04, 2023 at 12:07 PM
-- Server version: 5.5.62-0ubuntu0.14.04.1
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sql11650722`
--

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
  `points` int(11) NOT NULL DEFAULT '0',
  `wallet` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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



--
-- Table structure for table `prices`
--

CREATE TABLE `prices` (
  `id` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `price` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `renting_kits`
--

CREATE TABLE `renting_kits` (
  `id` int(11) NOT NULL,
  `type` varchar(6) CHARACTER SET utf8mb4 NOT NULL,
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
  `id` int(11) NOT NULL,
  `date` date DEFAULT NULL,
  `court` int(11) DEFAULT NULL,
  `client` int(11) DEFAULT NULL,
  `time_slot` int(11) NOT NULL,
  `price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `time_slots`
--

CREATE TABLE `time_slots` (
  `id` int(11) NOT NULL,
  `start_hour` varchar(5) NOT NULL,
  `finish_hour` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `wallet`
--

INSERT INTO `wallet` (`id`, `balance`, `client`) VALUES
(24, 50, 24),
(42, 30, 42),
(60, 0, 60),
(63, 0, 63);

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
-- Indexes for table `pma__bookmark`
--

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
  ADD KEY `reservation_client` (`time_slot`),
  ADD KEY `price` (`price`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT for table `pma__bookmark`
--

-- AUTO_INCREMENT for table `renting_kits`
--
ALTER TABLE `renting_kits`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `court`
--
ALTER TABLE `court`
  ADD CONSTRAINT `type_of_court` FOREIGN KEY (`type`) REFERENCES `type_of_court` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `prices`
--
ALTER TABLE `prices`
  ADD CONSTRAINT `type` FOREIGN KEY (`type`) REFERENCES `type_of_court` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `price` FOREIGN KEY (`price`) REFERENCES `prices` (`id`);

--
-- Constraints for table `wallet`
--
ALTER TABLE `wallet`
  ADD CONSTRAINT `client_wallet` FOREIGN KEY (`client`) REFERENCES `client` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
