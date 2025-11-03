-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 03, 2025 at 12:15 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `spring-catalog`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `details`
--

CREATE TABLE `details` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `apartment` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `pincode` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `cardNum` varchar(255) DEFAULT NULL,
  `cardName` varchar(255) DEFAULT NULL,
  `cvv` varchar(255) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `orderDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `orderID` varchar(255) DEFAULT NULL,
  `totalPrice` double DEFAULT NULL,
  `paymentMethod` varchar(255) DEFAULT NULL,
  `paymentStatus` varchar(255) DEFAULT NULL,
  `transactionId` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order_details`
--

CREATE TABLE `order_details` (
  `id` bigint(20) NOT NULL,
  `orderID` varchar(255) NOT NULL,
  `productName` varchar(255) NOT NULL,
  `qty` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` bigint(20) NOT NULL,
  `description` text DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `unum` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `description`, `image`, `name`, `price`, `unum`) VALUES
(1, 'Rich and indulgent chocolate ice cream crafted from premium cocoa.', '/uploads/5b351a81e7ab4535b73bc4df8e23b8ac.jpg', 'Chocolate Bliss', 40, 'IC001'),
(2, 'Tropical mango ice cream made from Alphonso mango pulp.', '/uploads/7957afecdb3147a6a25512f560d4a40a.jpg', 'Mango Magic', 45, 'IC002'),
(3, 'Tangy black currant ice cream with real fruit pulp.', '/uploads/9584d9d1418e423a968bed7c0c6dc8a3.jpg', 'Black Current Burst', 50, 'IC003'),
(4, 'Luscious blueberry ice cream with a tangy berry swirl.', '/uploads/b94797b2973d4601a98b211b8c444270.jpg', 'Blueberry Blast', 55, 'IC004'),
(5, 'Velvety ice cream with caramelized sugar bits and rich butterscotch flavor.', '/uploads/2bfd8089119446dd8d56da6f1e05a791.jpg', 'Butterscotch Crunch', 45, 'IC005'),
(6, 'Aromatic coffee ice cream for caffeine lovers with a creamy finish.', '/uploads/0786cdd7e2bc49da96aa11d5e11d15bc.jpg', 'Coffee Rush', 80, 'IC006'),
(7, 'Crunchy cookie chunks blended with vanilla ice cream — a classic favorite.', '/uploads/8f9acf2f1ea8466498cd70044e3b57bf.jpg', 'Cookies & Cream', 55, 'IC007'),
(8, 'Silky hazelnut ice cream with crunchy roasted nuts.', '/uploads/99aa69398adf4e309cae78603afd8d2f.jpg', 'Hazelnut Heaven', 80, 'IC008'),
(9, 'Nutty pistachio ice cream topped with roasted nut bits.', '/uploads/762f105efbfc4350bc6d8090cb553913.jpg', 'Pistachio Paradise', 70, 'IC009'),
(10, 'Velvety red velvet flavored ice cream with a creamy cheesecake swirl.', '/uploads/f2f76c1720f945a390aa4f45f0e71d3a.jpg', 'Red Velvet Delight', 90, 'IC010'),
(11, 'Perfect balance of sweet and salty in smooth caramel ice cream.', '/uploads/36df258f14a14a68a4825d22570f906a.jpg', 'Salted Caramel', 55, 'IC011'),
(12, 'Refreshing coconut ice cream made from real tender coconut water.', '/uploads/c1964958de184069ae5f6b6fa3494791.jpg', 'Tender Coconut', 50, 'IC012'),
(13, 'Colorful candied fruit mixed into rich vanilla base for a nostalgic treat.', '/uploads/7c28bcf266584f1d8ddd797cc0710b4b.jpg', 'Tutti Frutti', 90, 'IC013'),
(14, 'Classic creamy vanilla ice cream made with fresh milk and real vanilla beans.', '/uploads/fa8c5b6ebfad40fba30b8dc31fc83e30.jpg', 'Vanilla Dream', 40, 'IC014'),
(15, 'Fresh strawberry puree swirled into creamy ice cream for a fruity delight.', '/uploads/f745b66b3b6249e5b510584c4e2b1fa1.jpg', 'Strawberry Swirl', 80, 'IC015'),
(16, 'Cool mint ice cream sprinkled with dark chocolate chips.', '/uploads/a7d9c4b09bd642a4b14b45e644cfbdc9.jpg', 'Mint Choco Chip', 70, 'IC016'),
(17, 'Smooth caramel ice cream loaded with chunks of chocolate fudge.', '/uploads/fe42f12adcb4417b916bf1e58d54e529.jpg', 'Caramel Fudge', 60, 'IC017'),
(18, 'Creamy almond ice cream with caramelized almond bits.', '/uploads/741d2556e12b49509ff948f8bd202e74.jpg', 'Almond Crunch', 65, 'IC018'),
(19, 'Banana-flavored ice cream with chocolate and strawberry ribbons.', '/uploads/70f658e40127415ea6bb626fc0216e2d.jpg', 'Banana Split', 55, 'IC019'),
(20, 'Chocolate ice cream topped with gooey brownie chunks and fudge sauce.', '/uploads/31b4469dcd32418ba0bb06948bfc6668.jpg', 'Brownie Sundae', 50, 'IC020');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `details`
--
ALTER TABLE `details`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_9jteoge4s4sxr69g92d6w7yoh` (`unum`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `details`
--
ALTER TABLE `details`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `order_details`
--
ALTER TABLE `order_details`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
