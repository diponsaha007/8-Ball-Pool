-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 21, 2019 at 04:25 AM
-- Server version: 10.1.37-MariaDB
-- PHP Version: 7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `8 ball pool`
--

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

CREATE TABLE `players` (
  `Name` text,
  `Password` text,
  `FacebookID` text,
  `GamesPlayed` int(11) DEFAULT '0',
  `GamesWon` int(11) DEFAULT '0',
  `Money` int(11) DEFAULT '500'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `players`
--

INSERT INTO `players` (`Name`, `Password`, `FacebookID`, `GamesPlayed`, `GamesWon`, `Money`) VALUES
('Apurba', 'apurba23', '100006483736141', 43, 22, 720),
('Munim', 'munim23', '100007030865972', 17, 10, 791),
('Swargo', 'swargo23', '100003141777238', 16, 7, 600),
('Piyal', 'piyal23', '100002637423552', 15, 5, 319),
('Shoumik', 'shoumik23', '100009089527620', 4, 3, 262),
('Zaber', 'zaber23', '100008044450746', 4, 2, 138),
('Najib', 'najib23', '100005877693776', 10, 5, 120),
('Seemanta', 'seemanta23', '100011945597699', 1, 0, 50),
('Apu', 'apu23', '1234', 0, 0, 100);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
