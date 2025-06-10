-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Cze 09, 2025 at 03:48 PM
-- Wersja serwera: 10.4.32-MariaDB
-- Wersja PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sklep_db`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `ceny_produkty`
--

CREATE TABLE `ceny_produkty` (
  `id` int(11) NOT NULL,
  `produkt_id` int(11) NOT NULL,
  `cena_hurtowa` decimal(10,2) NOT NULL,
  `cena_detaliczna` decimal(10,2) NOT NULL,
  `cena_magazynowa` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ceny_produkty`
--

INSERT INTO `ceny_produkty` (`id`, `produkt_id`, `cena_hurtowa`, `cena_detaliczna`, `cena_magazynowa`) VALUES
(1, 1, 67.99, 79.99, 47.99),
(2, 2, 25.49, 29.99, 17.99),
(3, 3, 127.49, 149.99, 89.99),
(4, 4, 169.99, 199.99, 119.99),
(5, 5, 509.99, 599.99, 359.99),
(6, 6, 297.49, 349.99, 209.99),
(7, 7, 637.49, 749.99, 449.99),
(8, 8, 2124.99, 2499.99, 1499.99),
(9, 9, 1104.99, 1299.99, 779.99),
(10, 10, 424.99, 499.99, 299.99),
(11, 11, 76.49, 89.99, 53.99),
(12, 12, 127.49, 149.99, 89.99),
(13, 13, 33.99, 39.99, 23.99),
(14, 14, 5.94, 6.99, 4.19),
(15, 15, 21.24, 24.99, 14.99),
(16, 16, 33.99, 39.99, 23.99),
(17, 17, 21.24, 24.99, 14.99),
(18, 18, 42.49, 49.99, 29.99),
(19, 19, 679.99, 799.99, 479.99),
(20, 20, 110.49, 129.99, 77.99),
(26, 21, 50.99, 59.99, 35.99),
(27, 22, 16.99, 19.99, 11.99),
(28, 23, 21.24, 24.99, 14.99),
(29, 24, 212.49, 249.99, 149.99),
(30, 25, 84.99, 99.99, 59.99),
(31, 26, 110.49, 129.99, 77.99),
(32, 27, 33.99, 39.99, 23.99),
(33, 28, 67.99, 79.99, 47.99),
(34, 29, 50.99, 59.99, 35.99),
(35, 30, 212.49, 249.99, 149.99),
(37, 32, 297.49, 349.99, 209.99),
(38, 33, 127.49, 149.99, 89.99),
(44, 31, 161.49, 189.99, 113.99);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `finanse_sklepu`
--

CREATE TABLE `finanse_sklepu` (
  `id` int(11) NOT NULL,
  `saldo` decimal(10,2) NOT NULL DEFAULT 0.00,
  `ostatnia_aktualizacja` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `finanse_sklepu`
--

INSERT INTO `finanse_sklepu` (`id`, `saldo`, `ostatnia_aktualizacja`) VALUES
(1, 38198.47, '2025-06-05 15:02:23');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `klienci_detaliczni`
--

CREATE TABLE `klienci_detaliczni` (
  `id` int(11) NOT NULL,
  `imie` varchar(100) NOT NULL,
  `nazwisko` varchar(100) NOT NULL,
  `adres` text DEFAULT NULL,
  `telefon` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `suma_zakupow` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `klienci_detaliczni`
--

INSERT INTO `klienci_detaliczni` (`id`, `imie`, `nazwisko`, `adres`, `telefon`, `email`, `suma_zakupow`) VALUES
(1, 'Jan', 'Wójcik', 'Kraków, ul. Floriańska 10', '321654987', 'jan.wojcik@example.com', 829.97),
(2, 'Anna', 'Zielińska', 'Poznań, ul. Słoneczna 7', '741852963', 'anna.z@example.com', 1949.98),
(3, 'Piotr', 'Wiśniewski', 'Wrocław, ul. Radosna 15', '258369147', 'piotr.w@example.com', 999.99),
(4, 'Karolina', 'Lewandowska', 'Gdynia, ul. Spacerowa 5', '369852147', 'karolina.l@example.com', 1399.97),
(5, 'Marek', 'Nowak', 'Warszawa, ul. Krótka 12', '753951486', 'marek.n@example.com', 1249.99),
(6, 'Barbara', 'Kowalczyk', 'Łódź, ul. Główna 25', '852963741', 'barbara.k@example.com', 400.00),
(7, 'Tomasz', 'Mazur', 'Katowice, ul. Silesia 9', '741852369', 'tomasz.m@example.com', 1100.00),
(8, 'Magdalena', 'Dąbrowska', 'Szczecin, ul. Wodna 14', '159357824', 'magdalena.d@example.com', 780.00),
(9, 'Krzysztof', 'Grabowski', 'Opole, ul. Zielona 18', '963258741', 'krzysztof.g@example.com', 560.00),
(10, 'Sylwia', 'Jankowska', 'Rzeszów, ul. Parkowa 7', '852741963', 'sylwia.j@example.com', 940.00),
(11, 'Dominik', 'Kuraś', '37312 Łętownia', '821084822', 'taktak@gmail.com', 1240.00),
(12, 'HEHE', 'test2', 'tak', '123456789', '@test.com', 1204.95),
(14, 'domini', 'takj', 'aders', '123456789', '@gmail.com', 20277.51),
(15, 'Mateusz', 'Jastrzębski', 'Warszawa, ul. Długa 1', '987654321', 'mateusz.j@example.com', 1050.00),
(16, 'Joanna', 'Nowicka', 'Gdańsk, ul. Morska 12', '123789456', 'joanna.n@example.com', 780.50),
(18, 'Dominik', 'Kuraś', '37-312 łętownia', '555444333', '@gmail.comm', 3167.81),
(20, 'Dominik', 'Kuraś', '37-312 tak', '123456789', 'TobedzieTrudne@', 1234.00),
(21, 'Dominik', 'Dominik', 'Dominik', '123456789', '@hehehaha', 4183.92),
(22, 'hehe', 'zamawiam1raz', '373123', '123456789', 'musibyc@', 10174.40),
(23, 'tak', 'tak', 'dfadsa', '123456789', '@hadhsafdrs', 23412.00),
(24, 'haha', 'dzasdsa', 'fd24353tgf', '123456789', '@r4325t4gv46hn5b', 213321.00),
(25, 'dominik', 'testXXX', '37312', '123456789', 'deksaldsa@', 7758.79),
(26, 'xxx', 'hgfhrtb', '321321fcer', '123456789', '@dfsagtb tyb', 4409.94),
(27, 'Dominik', 'Kuras', '37-312 łętownia 473', '731084829', 'Deskalisko@ggmail.com', 5909.86),
(28, 'Dominik', 'Kuraś', '37-312 Łetownia', '731084829', '@', 3431.90);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `klienci_hurtowi`
--

CREATE TABLE `klienci_hurtowi` (
  `id` int(11) NOT NULL,
  `uzytkownik_id` int(11) NOT NULL,
  `imie` varchar(100) NOT NULL,
  `nazwisko` varchar(100) NOT NULL,
  `adres` text DEFAULT NULL,
  `telefon` varchar(20) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `NIP` varchar(20) NOT NULL,
  `nazwa_firmy` varchar(200) NOT NULL,
  `suma_zakupow` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `klienci_hurtowi`
--

INSERT INTO `klienci_hurtowi` (`id`, `uzytkownik_id`, `imie`, `nazwisko`, `adres`, `telefon`, `email`, `NIP`, `nazwa_firmy`, `suma_zakupow`) VALUES
(31, 1, 'Andrzej', 'Nowak', 'Warszawa, ul. Kwiatowa 5', '123456789', 'andrzej.nowak@example.com', '1234567899', 'Nowak Budownictwo', 48621.29),
(32, 2, 'Katarzyna', 'Kowalska', 'Gdańsk, ul. Morska 8', '987654321', 'katarzyna.k@example.com', 'PL9876543210', 'Kowalska Instalacje', 57499.94),
(33, 3, 'Robert', 'Kamiński', 'Wrocław, ul. Robotnicza 11', '789654123', 'robert.k@example.com', 'PL4567891230', 'Kamiński Tech', 54361.19),
(34, 4, 'Monika', 'Wiśniewska', 'Kraków, ul. Przemysłowa 3', '654987321', 'monika.w@example.com', 'PL9873216540', 'Wiśniewska Budowlanka', 48499.97),
(35, 5, 'Tomasz', 'Dąbrowski', 'Poznań, ul. Fabryczna 6', '951753852', 'tomasz.d@example.com', 'PL7539518520', 'Dąbrowski Hydraulika', 47999.99),
(36, 6, 'Anna', 'Zawadzka', 'Katowice, ul. Stalowa 22', '357159468', 'anna.z@example.com', 'PL1593574680', 'Zawadzka Energetyka', 49000.00),
(37, 7, 'Paweł', 'Wróblewski', 'Gdynia, ul. Magazynowa 9', '258147369', 'pawel.w@example.com', 'PL1472583690', 'Wróblewski Logistyka', 46000.00),
(38, 8, 'Ewa', 'Kaczmarek', 'Łódź, ul. Długa 15', '369852741', 'ewa.k@example.com', 'PL8523697410', 'Kaczmarek Chemia', 52000.00),
(39, 9, 'Grzegorz', 'Pawlak', 'Opole, ul. Budowlana 8', '753159486', 'grzegorz.p@example.com', 'PL4867531590', 'Pawlak Materiały', 31000.00),
(40, 10, 'Magda', 'Lis', 'Rzeszów, ul. Metalowa 4', '654321987', 'magda.l@example.com', 'PL9876543211', 'Lis Konstrukcje', 35000.00),
(41, 11, 'SebastianEDYTOWANY', 'Kwiatkowski', 'Łódź, ul. Kwiatowa 25', '654987321', 'sebastian.k@example.com', '3216549870', 'Kwiatkowski Transport', 42000.00),
(42, 13, 'Dominik', 'Kuraś', '37-312 Łętownia', '731084829', 'deskalisko@gmail.com', '1234567890', 'TAK', 1234.00),
(43, 14, 'Andrzej', 'Kowalski', 'Wrocław, ul. Robotnicza 11', '951753852', 'anna.z@example.com.pl', '1472583690', 'HEHETAKTEST', 3333.00);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `pozycje_transakcji`
--

CREATE TABLE `pozycje_transakcji` (
  `id` int(11) NOT NULL,
  `transakcja_id` int(11) NOT NULL,
  `produkt_id` int(11) NOT NULL,
  `ilosc` int(11) NOT NULL,
  `cena_jednostkowa` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pozycje_transakcji`
--

INSERT INTO `pozycje_transakcji` (`id`, `transakcja_id`, `produkt_id`, `ilosc`, `cena_jednostkowa`) VALUES
(67, 56, 1, 1, 79.99),
(68, 56, 2, 2, 29.99),
(69, 56, 11, 1, 89.99),
(70, 57, 4, 1, 199.99),
(71, 57, 5, 1, 599.99),
(72, 58, 3, 1, 149.99),
(73, 58, 12, 1, 149.99),
(74, 59, 6, 1, 349.99),
(75, 59, 2, 1, 29.99),
(76, 59, 1, 1, 79.99),
(77, 60, 7, 1, 749.99),
(87, 61, 8, 5, 2499.99),
(88, 62, 16, 100, 39.99),
(89, 62, 15, 50, 24.99),
(90, 63, 19, 10, 799.99),
(91, 63, 20, 20, 129.99),
(92, 64, 5, 15, 599.99),
(93, 64, 6, 10, 349.99),
(94, 65, 13, 150, 39.99),
(95, 65, 14, 200, 6.99),
(96, 66, 10, 1, 499.99),
(97, 66, 15, 1, 24.99),
(98, 66, 2, 1, 29.99),
(99, 67, 17, 1, 24.99),
(100, 67, 20, 1, 129.99),
(101, 67, 7, 1, 749.99),
(102, 67, 3, 1, 149.99),
(103, 67, 12, 1, 149.99),
(104, 68, 6, 35, 349.99),
(105, 68, 14, 4, 6.99),
(106, 68, 19, 10, 799.99),
(107, 69, 24, 2, 249.99),
(108, 69, 21, 1, 59.99),
(109, 70, 25, 3, 99.99),
(110, 70, 22, 2, 19.99),
(111, 71, 24, 2, 249.99),
(112, 71, 29, 3, 59.99),
(113, 71, 14, 4, 6.99),
(114, 71, 7, 3, 749.99),
(115, 71, 2, 7, 29.99),
(116, 75, 32, 1, 297.49),
(117, 76, 23, 3, 21.24),
(118, 77, 17, 1, 21.24),
(119, 77, 14, 9, 5.94),
(120, 77, 2, 100, 25.49),
(121, 78, 4, 1, 199.99),
(122, 78, 6, 1, 349.99),
(123, 78, 1, 1, 79.99),
(124, 79, 5, 1, 599.99),
(125, 79, 9, 1, 1299.99),
(126, 79, 1, 1, 79.99),
(127, 79, 33, 1, 149.99),
(128, 79, 31, 1, 189.99),
(129, 80, 30, 8, 249.99),
(130, 80, 32, 9, 349.99),
(131, 80, 13, 5, 39.99),
(132, 80, 15, 5, 24.99),
(133, 80, 2, 7, 29.99),
(134, 80, 31, 6, 189.99),
(135, 80, 6, 3, 349.99),
(136, 80, 1, 3, 79.99),
(137, 80, 5, 2, 599.99),
(138, 80, 27, 2, 39.99),
(139, 80, 33, 2, 149.99),
(140, 80, 29, 5, 59.99),
(141, 81, 29, 3, 59.99),
(142, 82, 28, 1, 79.99),
(143, 82, 21, 1, 59.99),
(144, 82, 7, 2, 749.99),
(145, 82, 3, 3, 149.99),
(146, 82, 6, 1, 349.99),
(147, 82, 29, 1, 59.99),
(148, 83, 31, 1, 189.99),
(149, 83, 33, 1, 149.99),
(150, 83, 30, 1, 249.99),
(151, 83, 29, 2, 59.99),
(152, 83, 27, 8, 39.99),
(153, 83, 26, 3, 129.99),
(154, 84, 27, 48, 39.99),
(155, 85, 27, 48, 39.99),
(156, 86, 4, 1, 199.99),
(157, 86, 9, 1, 1299.99),
(158, 86, 8, 1, 2499.99),
(159, 86, 1, 1, 79.99),
(160, 87, 1, 1, 79.99),
(161, 88, 30, 1, 249.99),
(162, 89, 33, 2, 127.49),
(163, 89, 31, 1, 161.49),
(164, 89, 17, 1, 21.24),
(165, 89, 11, 1, 76.49),
(166, 89, 3, 1, 127.49),
(167, 89, 2, 1, 25.49),
(168, 90, 1, 1, 67.99),
(169, 91, 27, 1, 39.99),
(170, 91, 33, 1, 149.99),
(171, 91, 31, 1, 189.99),
(172, 91, 22, 1, 19.99),
(173, 91, 15, 2, 24.99),
(174, 91, 13, 4, 39.99),
(175, 91, 8, 2, 2499.99),
(176, 91, 3, 2, 149.99),
(177, 92, 3, 1, 149.99),
(178, 93, 1, 1, 79.99),
(179, 94, 29, 1, 59.99),
(180, 94, 32, 1, 349.99),
(181, 94, 14, 1, 6.99),
(182, 95, 2, 1, 29.99),
(183, 96, 12, 1, 149.99),
(184, 96, 15, 1, 24.99),
(185, 96, 28, 1, 79.99),
(186, 97, 3, 1, 127.49),
(187, 97, 5, 1, 509.99),
(188, 98, 8, 1, 2124.99),
(189, 99, 8, 1, 2499.99);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `pozycje_zamowienia_magazynowego`
--

CREATE TABLE `pozycje_zamowienia_magazynowego` (
  `id` int(11) NOT NULL,
  `zamowienie_id` int(11) NOT NULL,
  `produkt_id` int(11) NOT NULL,
  `ilosc` int(11) NOT NULL,
  `zrealizowano` int(11) NOT NULL DEFAULT 0,
  `cena_jednostkowa` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pozycje_zamowienia_magazynowego`
--

INSERT INTO `pozycje_zamowienia_magazynowego` (`id`, `zamowienie_id`, `produkt_id`, `ilosc`, `zrealizowano`, `cena_jednostkowa`) VALUES
(1, 2, 7, 15, 0, 749.99),
(2, 3, 11, 100, 0, 89.99),
(3, 4, 15, 15, 0, 24.99),
(4, 5, 6, 1, 0, 209.99),
(5, 6, 18, 7, 0, 29.99),
(6, 7, 21, 10, 0, 59.99),
(7, 7, 22, 25, 0, 19.99),
(8, 7, 25, 15, 0, 99.99),
(9, 8, 29, 10, 0, 35.99),
(10, 9, 14, 50, 0, 4.19),
(11, 10, 11, 55, 0, 53.99),
(12, 11, 14, 100, 0, 4.19),
(13, 12, 6, 3, 0, 209.99),
(14, 13, 14, 55, 0, 4.19),
(15, 14, 27, 150, 0, 23.99);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `produkty`
--

CREATE TABLE `produkty` (
  `id` int(11) NOT NULL,
  `nazwa` varchar(100) NOT NULL,
  `ilosc` int(11) NOT NULL,
  `kategoria` varchar(50) DEFAULT NULL,
  `typ` enum('odziez','elektronika','materialy','sprzet') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produkty`
--

INSERT INTO `produkty` (`id`, `nazwa`, `ilosc`, `kategoria`, `typ`) VALUES
(1, 'Kask ochronny', 41, 'Odzież ochronna', 'odziez'),
(2, 'Rękawice robocze', 83, 'Odzież ochronna', 'odziez'),
(3, 'Kurtka odblaskowa', 21, 'Odzież ochronna', 'odziez'),
(4, 'Buty robocze', 38, 'Odzież ochronna', 'odziez'),
(5, 'Młot udarowy', 16, 'Sprzęt budowlany', 'elektronika'),
(6, 'Wiertarka akumulatorowa', 3, 'Sprzęt budowlany', 'elektronika'),
(7, 'Piła tarczowa', 9, 'Sprzęt budowlany', 'elektronika'),
(8, 'Betoniarka', 10, 'Sprzęt budowlany', 'sprzet'),
(9, 'Kamera termowizyjna', 8, 'Elektronika', 'elektronika'),
(10, 'Laserowy miernik odległości', 24, 'Elektronika', 'elektronika'),
(11, 'Latarka LED', 99, 'Elektronika', 'odziez'),
(12, 'Detektor napięcia', 48, 'Elektronika', 'elektronika'),
(13, 'Cement 25kg', 91, 'Materiały budowlane', 'materialy'),
(14, 'Bloczek betonowy', 109, 'Materiały budowlane', 'materialy'),
(15, 'Gips szpachlowy', 141, 'Materiały budowlane', 'materialy'),
(16, 'Płyta gipsowo-kartonowa', 80, 'Materiały budowlane', 'materialy'),
(17, 'Rura PVC 2m', 197, 'Instalacje i hydraulika', 'sprzet'),
(18, 'Zawór kulowy', 80, 'Instalacje i hydraulika', 'sprzet'),
(19, 'Pompa wodna', 25, 'Instalacje i hydraulika', 'elektronika'),
(20, 'Filtr do wody', 59, 'Instalacje i hydraulika', 'sprzet'),
(21, 'Młotek stolarski', 49, 'Sprzęt ręczny', 'sprzet'),
(22, 'Miara zwijana 5m', 99, 'Sprzęt ręczny', 'sprzet'),
(23, 'Śrubokręt krzyżowy', 77, 'Sprzęt ręczny', 'sprzet'),
(24, 'Wkrętarka elektryczna', 23, 'Elektronika', 'elektronika'),
(25, 'Farba akrylowa 10L', 40, 'Materiały budowlane', 'materialy'),
(26, 'Spodnie robocze', 32, 'Odzież ochronna', 'odziez'),
(27, 'Koszulka odblaskowa', 93, 'Odzież ochronna', 'odziez'),
(28, 'Multimetr cyfrowy', 18, 'Elektronika', 'elektronika'),
(29, 'Czujnik wilgotności', 10, 'Elektronika', 'elektronika'),
(30, 'Siatka zbrojeniowa', 50, 'Materiały budowlane', 'materialy'),
(31, 'Wełna mineralna', 25, 'Materiały budowlane', 'materialy'),
(32, 'Podnośnik hydrauliczny', 49, 'Instalacje i hydraulika', 'sprzet'),
(33, 'Zestaw kluczy nasadowych', 23, 'Instalacje i hydraulika', 'sprzet');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `produkty_backup`
--

CREATE TABLE `produkty_backup` (
  `id` int(11) NOT NULL DEFAULT 0,
  `nazwa` varchar(100) NOT NULL,
  `ilosc` int(11) NOT NULL,
  `kategoria` varchar(50) DEFAULT NULL,
  `typ` enum('ogolny','elektronika','odziez') DEFAULT 'ogolny',
  `waga` decimal(10,2) DEFAULT NULL,
  `gwarancja` int(11) DEFAULT NULL,
  `material` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produkty_backup`
--

INSERT INTO `produkty_backup` (`id`, `nazwa`, `ilosc`, `kategoria`, `typ`, `waga`, `gwarancja`, `material`) VALUES
(1, 'Kask ochronny', 50, 'Odzież ochronna', 'ogolny', 1.20, 24, 'Poliwęglan'),
(2, 'Rękawice robocze', 199, 'Odzież ochronna', 'ogolny', 0.50, 12, 'Kevlar'),
(3, 'Kurtka odblaskowa', 29, 'Odzież ochronna', 'odziez', 1.80, 12, 'Poliester'),
(4, 'Buty robocze', 40, 'Odzież ochronna', 'odziez', 2.50, 24, 'Skóra i guma'),
(5, 'Młot udarowy', 20, 'Sprzęt budowlany', 'elektronika', 4.50, 36, 'Stal'),
(6, 'Wiertarka akumulatorowa', 5, 'Sprzęt budowlany', 'elektronika', 2.80, 24, 'Plastik i stal'),
(7, 'Piła tarczowa', 14, 'Sprzęt budowlany', 'elektronika', 6.00, 36, 'Stal i aluminium'),
(8, 'Betoniarka', 15, 'Sprzęt budowlany', 'ogolny', 120.00, 48, 'Metal'),
(9, 'Kamera termowizyjna', 10, 'Elektronika', 'elektronika', 1.50, 24, 'Plastik i szkło'),
(10, 'Laserowy miernik odległości', 24, 'Elektronika', 'elektronika', 0.80, 24, 'Metal i plastik'),
(11, 'Latarka LED', 100, 'Elektronika', 'ogolny', 0.40, 12, 'Aluminium'),
(12, 'Detektor napięcia', 49, 'Elektronika', 'elektronika', 0.30, 24, 'Plastik'),
(13, 'Cement 25kg', 100, 'Materiały budowlane', 'ogolny', 25.00, 0, 'Mieszanka cementowa'),
(14, 'Bloczek betonowy', 496, 'Materiały budowlane', 'ogolny', 3.00, 0, 'Beton'),
(15, 'Gips szpachlowy', 149, 'Materiały budowlane', 'ogolny', 10.00, 0, 'Gips'),
(16, 'Płyta gipsowo-kartonowa', 80, 'Materiały budowlane', 'ogolny', 15.00, 0, 'Gips i karton'),
(17, 'Rura PVC 2m', 199, 'Instalacje i hydraulika', 'ogolny', 3.00, 0, 'PVC'),
(18, 'Zawór kulowy', 80, 'Instalacje i hydraulika', 'ogolny', 1.20, 24, 'Stal nierdzewna'),
(19, 'Pompa wodna', 25, 'Instalacje i hydraulika', 'elektronika', 8.50, 36, 'Metal'),
(20, 'Filtr do wody', 59, 'Instalacje i hydraulika', 'ogolny', 1.00, 12, 'Plastik i węgiel aktywny');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `produkty_elektronika`
--

CREATE TABLE `produkty_elektronika` (
  `produkt_id` int(11) NOT NULL,
  `moc` varchar(20) DEFAULT NULL,
  `napiecie` varchar(20) DEFAULT NULL,
  `gwarancja` int(11) NOT NULL,
  `waga` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produkty_elektronika`
--

INSERT INTO `produkty_elektronika` (`produkt_id`, `moc`, `napiecie`, `gwarancja`, `waga`) VALUES
(5, '800W', '230V', 36, 4.50),
(6, '450W', '230V', 24, 2.80),
(7, '1500W', '230V', 36, 6.00),
(9, '100W', '230V', 24, 1.50),
(10, '100W', '230V', 24, 0.80),
(12, '100W', '230V', 24, 0.30),
(19, '100W', '230V', 36, 8.50),
(28, '15W', '230V', 24, 0.90),
(29, '5W', '230V', 24, 0.60);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `produkty_materialy`
--

CREATE TABLE `produkty_materialy` (
  `produkt_id` int(11) NOT NULL,
  `waga` decimal(10,2) NOT NULL,
  `jednostka` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produkty_materialy`
--

INSERT INTO `produkty_materialy` (`produkt_id`, `waga`, `jednostka`) VALUES
(13, 25.00, 'kg'),
(14, 3.00, 'kg'),
(15, 10.00, 'kg'),
(16, 15.00, 'kg'),
(30, 10.00, 'm2'),
(31, 15.00, 'm3');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `produkty_odziez`
--

CREATE TABLE `produkty_odziez` (
  `produkt_id` int(11) NOT NULL,
  `material` varchar(50) NOT NULL,
  `rozmiar` varchar(10) NOT NULL,
  `kolor` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produkty_odziez`
--

INSERT INTO `produkty_odziez` (`produkt_id`, `material`, `rozmiar`, `kolor`) VALUES
(1, 'Poliwęglan', 'UNI', 'czarny'),
(2, 'Kevlar', 'UNI', 'czarny'),
(3, 'Poliester', 'UNI', 'czarny'),
(4, 'Skóra i guma', 'UNI', 'czarny'),
(26, 'Bawełna', 'L', 'czarny'),
(27, 'Poliester', 'M', 'żółty');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `produkty_sprzet`
--

CREATE TABLE `produkty_sprzet` (
  `produkt_id` int(11) NOT NULL,
  `material` varchar(50) NOT NULL,
  `gwarancja` int(11) NOT NULL,
  `waga` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produkty_sprzet`
--

INSERT INTO `produkty_sprzet` (`produkt_id`, `material`, `gwarancja`, `waga`) VALUES
(8, 'Metal', 48, 120.00),
(17, 'PVC', 0, 3.00),
(18, 'Stal nierdzewna', 24, 1.20),
(20, 'Plastik i węgiel aktywny', 12, 1.00),
(32, 'Stal nierdzewna', 36, 25.00),
(33, 'Chromowana stal', 24, 5.00);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `transakcje`
--

CREATE TABLE `transakcje` (
  `id` int(11) NOT NULL,
  `klient_hurtowy_id` int(11) DEFAULT NULL,
  `klient_detaliczny_id` int(11) DEFAULT NULL,
  `data` datetime NOT NULL,
  `calkowita_kwota` decimal(10,2) NOT NULL,
  `typ` enum('detaliczny','hurtowy') NOT NULL,
  `metoda_platnosci` enum('gotowka','karta') NOT NULL,
  `data_utworzenia` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transakcje`
--

INSERT INTO `transakcje` (`id`, `klient_hurtowy_id`, `klient_detaliczny_id`, `data`, `calkowita_kwota`, `typ`, `metoda_platnosci`, `data_utworzenia`) VALUES
(56, NULL, 1, '2025-05-01 10:15:00', 329.97, 'detaliczny', 'karta', '2025-05-29 13:39:27'),
(57, NULL, 2, '2025-05-02 11:30:00', 749.98, 'detaliczny', 'gotowka', '2025-05-29 13:39:27'),
(58, NULL, 3, '2025-05-03 14:45:00', 199.99, 'detaliczny', 'karta', '2025-05-29 13:39:27'),
(59, NULL, 4, '2025-05-04 09:20:00', 449.97, 'detaliczny', 'gotowka', '2025-05-29 13:39:27'),
(60, NULL, 5, '2025-05-05 16:10:00', 599.99, 'detaliczny', 'karta', '2025-05-29 13:39:27'),
(61, 31, NULL, '2025-05-01 08:00:00', 12499.95, 'hurtowy', 'karta', '2025-05-29 13:39:51'),
(62, 32, NULL, '2025-05-02 09:30:00', 7499.94, 'hurtowy', 'karta', '2025-05-29 13:39:51'),
(63, 33, NULL, '2025-05-03 10:45:00', 8999.98, 'hurtowy', 'karta', '2025-05-29 13:39:51'),
(64, 34, NULL, '2025-05-04 13:20:00', 10499.97, 'hurtowy', 'karta', '2025-05-29 13:39:51'),
(65, 35, NULL, '2025-05-05 15:00:00', 5999.99, 'hurtowy', 'karta', '2025-05-29 13:39:51'),
(66, NULL, 11, '2025-05-29 21:35:14', 554.97, 'detaliczny', 'karta', '2025-05-29 19:35:14'),
(67, NULL, 12, '2025-05-29 21:40:29', 1204.95, 'detaliczny', 'karta', '2025-05-29 19:40:29'),
(68, NULL, 14, '2025-05-29 22:08:01', 20277.51, 'detaliczny', 'karta', '2025-05-29 20:08:01'),
(69, NULL, 15, '2025-05-31 12:00:00', 1050.00, 'detaliczny', 'karta', '2025-05-31 11:19:55'),
(70, NULL, 16, '2025-05-31 13:30:00', 780.50, 'detaliczny', 'gotowka', '2025-05-31 11:19:55'),
(71, NULL, 18, '2025-05-31 14:12:08', 3167.81, 'detaliczny', 'karta', '2025-05-31 12:12:08'),
(75, 33, NULL, '2025-05-31 19:08:35', 297.49, 'hurtowy', 'karta', '2025-05-31 17:08:35'),
(76, 33, NULL, '2025-05-31 19:10:35', 63.72, 'hurtowy', 'karta', '2025-05-31 17:10:35'),
(77, 31, NULL, '2025-05-31 19:42:43', 2623.70, 'hurtowy', 'karta', '2025-05-31 17:42:43'),
(78, NULL, 21, '2025-05-31 22:46:13', 629.97, 'detaliczny', 'gotowka', '2025-05-31 20:46:13'),
(79, NULL, 21, '2025-05-31 22:46:25', 2319.95, 'detaliczny', 'gotowka', '2025-05-31 20:46:25'),
(80, NULL, 22, '2025-06-01 01:23:07', 10019.43, 'detaliczny', 'gotowka', '2025-05-31 23:23:07'),
(81, NULL, 22, '2025-06-01 01:23:27', 194.97, 'detaliczny', 'karta', '2025-05-31 23:23:27'),
(82, NULL, 25, '2025-06-01 01:51:43', 2524.91, 'detaliczny', 'gotowka', '2025-05-31 23:51:43'),
(83, NULL, 25, '2025-06-01 01:51:59', 1434.84, 'detaliczny', 'karta', '2025-05-31 23:51:59'),
(84, NULL, 25, '2025-06-01 01:52:07', 1934.52, 'detaliczny', 'karta', '2025-05-31 23:52:07'),
(85, NULL, 25, '2025-06-01 01:52:11', 1934.52, 'detaliczny', 'karta', '2025-05-31 23:52:11'),
(86, NULL, 26, '2025-06-01 01:55:13', 4104.96, 'detaliczny', 'gotowka', '2025-05-31 23:55:13'),
(87, NULL, 26, '2025-06-01 01:55:17', 104.99, 'detaliczny', 'gotowka', '2025-05-31 23:55:17'),
(88, NULL, 26, '2025-06-01 01:55:22', 274.99, 'detaliczny', 'gotowka', '2025-05-31 23:55:22'),
(89, 31, NULL, '2025-06-01 01:56:48', 692.18, 'hurtowy', 'gotowka', '2025-05-31 23:56:48'),
(90, 31, NULL, '2025-06-01 01:56:53', 92.99, 'hurtowy', 'gotowka', '2025-05-31 23:56:53'),
(91, NULL, 27, '2025-06-01 03:17:09', 5924.86, 'detaliczny', 'karta', '2025-06-01 01:17:09'),
(92, NULL, 28, '2025-06-01 03:23:17', 174.99, 'detaliczny', 'gotowka', '2025-06-01 01:23:17'),
(93, NULL, 28, '2025-06-01 03:23:21', 104.99, 'detaliczny', 'gotowka', '2025-06-01 01:23:21'),
(94, NULL, 28, '2025-06-01 14:09:50', 441.97, 'detaliczny', 'gotowka', '2025-06-01 12:09:50'),
(95, NULL, 28, '2025-06-01 14:09:58', 54.99, 'detaliczny', 'gotowka', '2025-06-01 12:09:58'),
(96, NULL, 28, '2025-06-05 16:51:43', 269.97, 'detaliczny', 'karta', '2025-06-05 14:51:43'),
(97, 31, NULL, '2025-06-05 17:01:27', 662.48, 'hurtowy', 'gotowka', '2025-06-05 15:01:27'),
(98, 31, NULL, '2025-06-05 17:01:32', 2139.99, 'hurtowy', 'karta', '2025-06-05 15:01:32'),
(99, NULL, 28, '2025-06-05 17:02:23', 2514.99, 'detaliczny', 'karta', '2025-06-05 15:02:23');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `uzytkownicy`
--

CREATE TABLE `uzytkownicy` (
  `id` int(11) NOT NULL,
  `login` varchar(50) NOT NULL,
  `haslo` varchar(255) NOT NULL,
  `typ` enum('admin','hurtowy') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `uzytkownicy`
--

INSERT INTO `uzytkownicy` (`id`, `login`, `haslo`, `typ`) VALUES
(1, 'user', 'user', 'hurtowy'),
(2, 'user2', 'bezpieczneHaslo2', 'hurtowy'),
(3, 'user3', 'bezpieczneHaslo3', 'hurtowy'),
(4, 'user4', 'bezpieczneHaslo4', 'hurtowy'),
(5, 'user5', 'bezpieczneHaslo5', 'hurtowy'),
(6, 'user6', 'bezpieczneHaslo6', 'hurtowy'),
(7, 'user7', 'bezpieczneHaslo7', 'hurtowy'),
(8, 'user8', 'bezpieczneHaslo8', 'hurtowy'),
(9, 'user9', 'bezpieczneHaslo9', 'hurtowy'),
(10, 'user10', 'bezpieczneHaslo10', 'hurtowy'),
(11, 'admin', 'admin', 'admin'),
(12, 'deskalisko@gmail.com', 'pass2907', 'hurtowy'),
(13, 'deskalisko@', 'pass9141', 'hurtowy'),
(14, 'anna.z@example.com.pl', 'pass7928', 'hurtowy'),
(15, '@gmailcommmm', 'pass1894', 'hurtowy');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `zamowienia_magazynowe`
--

CREATE TABLE `zamowienia_magazynowe` (
  `id` int(11) NOT NULL,
  `data_zamowienia` datetime NOT NULL DEFAULT current_timestamp(),
  `data_realizacji` datetime NOT NULL DEFAULT current_timestamp(),
  `dostawca` varchar(200) NOT NULL,
  `uwagi` text DEFAULT NULL,
  `calkowita_kwota` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `zamowienia_magazynowe`
--

INSERT INTO `zamowienia_magazynowe` (`id`, `data_zamowienia`, `data_realizacji`, `dostawca`, `uwagi`, `calkowita_kwota`) VALUES
(1, '2025-05-30 13:40:13', '2025-05-30 13:40:13', 'AUTOSAN', 'szybko!!!', 0.00),
(2, '2025-05-30 13:43:08', '2025-05-30 13:43:08', 'AUTOSAN', 'szybko!!!', 11249.85),
(3, '2025-05-30 13:43:52', '2025-05-30 13:43:52', 'AUTOSAN', 'szybko!!!', 8999.00),
(4, '2025-05-30 13:44:34', '2025-05-30 13:44:34', 'JESJKES', '', 374.85),
(5, '2025-05-30 20:32:45', '2025-05-30 20:32:45', 'JA SAM', 'zadnych', 209.99),
(6, '2025-05-30 21:03:43', '2025-05-30 21:03:43', 'Ja sam', '', 209.93),
(7, '2025-05-31 10:00:00', '2025-05-31 13:20:07', 'BUD-MAT', 'Potrzebne na cito', 1599.97),
(8, '2025-05-31 13:40:58', '2025-05-31 13:40:58', 'Ja Sam', '', 359.90),
(9, '2025-05-31 19:40:54', '2025-05-31 19:40:54', 'BLOKI LEZASK', '', 209.50),
(10, '2025-05-31 19:41:05', '2025-05-31 19:41:05', 'tak', '', 2969.45),
(11, '2025-05-31 19:43:26', '2025-05-31 19:43:26', 'TAK', '', 419.00),
(12, '2025-05-31 19:47:19', '2025-05-31 19:47:19', 'JATEST', '', 629.97),
(13, '2025-05-31 19:47:31', '2025-05-31 19:47:31', 'ZNOWU ja', '', 230.45),
(14, '2025-06-01 01:57:21', '2025-06-01 01:57:21', 'Jaaaa', 'ge3gw', 3598.50);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `ceny_produkty`
--
ALTER TABLE `ceny_produkty`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `produkt_id` (`produkt_id`);

--
-- Indeksy dla tabeli `finanse_sklepu`
--
ALTER TABLE `finanse_sklepu`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `klienci_detaliczni`
--
ALTER TABLE `klienci_detaliczni`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indeksy dla tabeli `klienci_hurtowi`
--
ALTER TABLE `klienci_hurtowi`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uzytkownik_id` (`uzytkownik_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `NIP` (`NIP`);

--
-- Indeksy dla tabeli `pozycje_transakcji`
--
ALTER TABLE `pozycje_transakcji`
  ADD PRIMARY KEY (`id`),
  ADD KEY `transakcja_id` (`transakcja_id`),
  ADD KEY `fk_transakcja_produkt` (`produkt_id`);

--
-- Indeksy dla tabeli `pozycje_zamowienia_magazynowego`
--
ALTER TABLE `pozycje_zamowienia_magazynowego`
  ADD PRIMARY KEY (`id`),
  ADD KEY `zamowienie_id` (`zamowienie_id`),
  ADD KEY `produkt_id` (`produkt_id`);

--
-- Indeksy dla tabeli `produkty`
--
ALTER TABLE `produkty`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `produkty_elektronika`
--
ALTER TABLE `produkty_elektronika`
  ADD PRIMARY KEY (`produkt_id`);

--
-- Indeksy dla tabeli `produkty_materialy`
--
ALTER TABLE `produkty_materialy`
  ADD PRIMARY KEY (`produkt_id`);

--
-- Indeksy dla tabeli `produkty_odziez`
--
ALTER TABLE `produkty_odziez`
  ADD PRIMARY KEY (`produkt_id`);

--
-- Indeksy dla tabeli `produkty_sprzet`
--
ALTER TABLE `produkty_sprzet`
  ADD PRIMARY KEY (`produkt_id`);

--
-- Indeksy dla tabeli `transakcje`
--
ALTER TABLE `transakcje`
  ADD PRIMARY KEY (`id`),
  ADD KEY `klient_hurtowy_id` (`klient_hurtowy_id`),
  ADD KEY `klient_detaliczny_id` (`klient_detaliczny_id`);

--
-- Indeksy dla tabeli `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`);

--
-- Indeksy dla tabeli `zamowienia_magazynowe`
--
ALTER TABLE `zamowienia_magazynowe`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ceny_produkty`
--
ALTER TABLE `ceny_produkty`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `finanse_sklepu`
--
ALTER TABLE `finanse_sklepu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `klienci_detaliczni`
--
ALTER TABLE `klienci_detaliczni`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `klienci_hurtowi`
--
ALTER TABLE `klienci_hurtowi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT for table `pozycje_transakcji`
--
ALTER TABLE `pozycje_transakcji`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=190;

--
-- AUTO_INCREMENT for table `pozycje_zamowienia_magazynowego`
--
ALTER TABLE `pozycje_zamowienia_magazynowego`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `produkty`
--
ALTER TABLE `produkty`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `transakcje`
--
ALTER TABLE `transakcje`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100;

--
-- AUTO_INCREMENT for table `uzytkownicy`
--
ALTER TABLE `uzytkownicy`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `zamowienia_magazynowe`
--
ALTER TABLE `zamowienia_magazynowe`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ceny_produkty`
--
ALTER TABLE `ceny_produkty`
  ADD CONSTRAINT `fk_produkt_cena` FOREIGN KEY (`produkt_id`) REFERENCES `produkty` (`id`);

--
-- Constraints for table `klienci_hurtowi`
--
ALTER TABLE `klienci_hurtowi`
  ADD CONSTRAINT `klienci_hurtowi_ibfk_1` FOREIGN KEY (`uzytkownik_id`) REFERENCES `uzytkownicy` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `pozycje_transakcji`
--
ALTER TABLE `pozycje_transakcji`
  ADD CONSTRAINT `fk_transakcja_produkt` FOREIGN KEY (`produkt_id`) REFERENCES `produkty` (`id`),
  ADD CONSTRAINT `pozycje_transakcji_ibfk_1` FOREIGN KEY (`transakcja_id`) REFERENCES `transakcje` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `pozycje_zamowienia_magazynowego`
--
ALTER TABLE `pozycje_zamowienia_magazynowego`
  ADD CONSTRAINT `fk_zamowienie_produkt` FOREIGN KEY (`produkt_id`) REFERENCES `produkty` (`id`),
  ADD CONSTRAINT `pozycje_zamowienia_magazynowego_ibfk_1` FOREIGN KEY (`zamowienie_id`) REFERENCES `zamowienia_magazynowe` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `produkty_elektronika`
--
ALTER TABLE `produkty_elektronika`
  ADD CONSTRAINT `produkty_elektronika_ibfk_1` FOREIGN KEY (`produkt_id`) REFERENCES `produkty` (`id`);

--
-- Constraints for table `produkty_materialy`
--
ALTER TABLE `produkty_materialy`
  ADD CONSTRAINT `produkty_materialy_ibfk_1` FOREIGN KEY (`produkt_id`) REFERENCES `produkty` (`id`);

--
-- Constraints for table `produkty_odziez`
--
ALTER TABLE `produkty_odziez`
  ADD CONSTRAINT `produkty_odziez_ibfk_1` FOREIGN KEY (`produkt_id`) REFERENCES `produkty` (`id`);

--
-- Constraints for table `produkty_sprzet`
--
ALTER TABLE `produkty_sprzet`
  ADD CONSTRAINT `produkty_sprzet_ibfk_1` FOREIGN KEY (`produkt_id`) REFERENCES `produkty` (`id`);

--
-- Constraints for table `transakcje`
--
ALTER TABLE `transakcje`
  ADD CONSTRAINT `transakcje_ibfk_1` FOREIGN KEY (`klient_hurtowy_id`) REFERENCES `klienci_hurtowi` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `transakcje_ibfk_2` FOREIGN KEY (`klient_detaliczny_id`) REFERENCES `klienci_detaliczni` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
