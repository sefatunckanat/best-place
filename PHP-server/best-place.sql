-- phpMyAdmin SQL Dump
-- version 4.4.13.1deb1
-- http://www.phpmyadmin.net
--
-- Anamakine: localhost
-- Üretim Zamanı: 30 Tem 2016, 23:57:30
-- Sunucu sürümü: 5.6.31-0ubuntu0.15.10.1
-- PHP Sürümü: 5.6.11-1ubuntu3.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `best-place`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `mekan`
--

CREATE TABLE IF NOT EXISTS `mekan` (
  `id` int(11) NOT NULL,
  `isim` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `aciklama` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `picture` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `kategori` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `sehir` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `mekan`
--

INSERT INTO `mekan` (`id`, `isim`, `aciklama`, `picture`, `kategori`, `sehir`) VALUES
(1, 'Çeşme Kafe', 'Kaliteli Zamanlar İçin', 'place/test.jpg', 'kafe', 10),
(2, 'Kaşif Kafe', 'Keşfe Değer', 'place/kasif.jpg', 'kafe', 10),
(3, 'Cafe AS', 'Bırey Dershanesı Arkası (Vasıf Çınar Caddesi) Balıkesir\r\n', 'place/as.jpg', 'kafe', 10),
(4, 'Cafe Cine', 'Eski Kuyumcular cd, 10100 Balıkesir', 'place/cine.jpg', 'kafe', 10),
(5, 'Cafemizz', 'Eski kuyumcular Mah. Mekik sok. No:22/a', 'place/cafemizz.jpg', 'kafe', 10),
(6, 'Metropol Kır Kahvesi', 'Atatürk Kültür Parkı, Balıkesir', 'place/metropol.jpg', 'kafe', 10),
(7, 'Midplus+', 'Eski Kuyumcular Mah. Hacı Gaybi Sok. No:14 Balıkesir', 'place/mid.png', 'kafe', 10),
(8, 'Mega Yıldız Lokantası', 'Akıncılar Mah. Tekin Sok. No:2, 10100 Karesi', 'place/mega.jpg', 'yemek', 10),
(9, 'Ocakbaşı Ali Baba', 'Eski Kuyumcular Mahallesi (Hacı Gaybi Caddesi), 10100 Karesi', 'place/ocak.jpg', 'yemek', 10),
(10, 'Alaturka Fast Food', 'Kaşif Kafe Karşısı', 'place/alaturka.jpg', 'yemek', 10),
(11, 'Ceylin & Duru Çiğköfte Salonu', 'Türk Restoranı', 'place/ceylin.jpg', 'yemek', 10),
(12, 'Mavi Cafe', 'Milli Kuvvetler Caddesi Başı, Balıkesir\r\n', 'place/mavi.jpg', 'kafe', 10),
(13, 'Saffet Çay Evi', 'Yıldırım Mah. (Paşasarayı Sok.), Balıkesir', 'place/saffet.jpg', 'cay', 10),
(14, 'Orange Cafe', 'Ara sokak (Milli Kuvvetler Caddesi), Balıkesir', 'place/orange.jpg', 'kafe', 10),
(15, 'Sarnıç Cafe', 'We Are The Best', 'place/sarnic.jpg', 'cay', 10),
(16, 'Çınaraltı Çay Evi', 'Zağnos Paşa Camii Yanı (Turan Caddesi), 10100 Karesi, Balıkesir', 'place/cinar.jpg', 'cay', 10),
(17, 'Yudum Sofra', 'Eski Kuyumcular Mahallesi Kızılay Caddesi No:25 (6 Eylül), Balıkesir, Balıkesir 10100', 'place/yudum.jpg', 'yemek', 10),
(18, 'Yıldız Aile Lokantası', 'Balıkesir', 'place/yildiz.jpg', 'yemek', 10),
(19, 'Keyif Aile Lokantası', 'Karesi, Balıkesir', 'place/keyif.jpg', 'yemek', 10),
(20, 'Aker Et Restaurant', '6 Eylül Mah. Çiğdem Sok. No:5, 10100 Balıkesir', 'place/aker.jpg', 'yemek', 10),
(21, 'Sığınak Çay Evi', 'Dedeler dedeler', 'place/siginak.jpg', 'cay', 10),
(22, 'Buhara Çay Evi', 'Zağnos Paşa Caddesi, Karesi, Balıkesir', 'place/buhara.jpg', 'cay', 10),
(23, 'Şadırvan Vitamin', 'H.ilbey mah (Anafartalar Caddesi), 10050 Balıkesir', 'place/vitamin.jpg', 'cay', 10),
(24, 'Cafe Garden Limon', 'İzmir yolu 2.km, Balıkesir', 'place/limon.jpg', 'cay', 10),
(25, 'Yücel Çay Evi', 'Başsaran Sk. No:1, Çiviciler Cd., Yıldırım (Çiviciler), 10100 Balıkesir', 'place/yucel.jpg', 'cay', 10),
(26, 'Diriliş Çay Evi', 'Yıldırım Mahallesi, Cumhuriyet Caddesi, Alacamescit Yanı, Karesi/BALIKESİR (Cumhuriyet Caddesi), 10020 Karesi, Balıkesir', 'place/dirilis.jpg', 'cay', 10);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `olaylar`
--

CREATE TABLE IF NOT EXISTS `olaylar` (
  `id` int(11) NOT NULL,
  `tarih` varchar(20) COLLATE utf8_turkish_ci NOT NULL,
  `kimin` varchar(3) COLLATE utf8_turkish_ci NOT NULL,
  `olay` varchar(255) COLLATE utf8_turkish_ci NOT NULL,
  `kim` varchar(3) COLLATE utf8_turkish_ci NOT NULL,
  `mekan` varchar(3) COLLATE utf8_turkish_ci NOT NULL,
  `sys_tarih` varchar(12) COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `olaylar`
--

INSERT INTO `olaylar` (`id`, `tarih`, `kimin`, `olay`, `kim`, `mekan`, `sys_tarih`) VALUES
(73, '10-06-2016 09:59', '22', 'enpahali', '22', '2', '201606100959'),
(75, '10-06-2016 10:01', '22', 'enucuz', '22', '25', '201606101001'),
(76, '10-06-2016 10:01', '22', 'enkalabalik', '22', '21', '201606101001'),
(78, '10-06-2016 10:04', '20', 'enkalite', '20', '4', '201606101004'),
(79, '10-06-2016 10:04', '22', 'enkotu', '22', '4', '201606101004'),
(80, '10-06-2016 12:31', '20', 'enpahali', '20', '3', '201606101231'),
(81, '10-06-2016 12:53', '20', 'enrahat', '20', '15', '201606101253'),
(82, '10-06-2016 12:56', '20', 'enpahali', '20', '7', '201606101256'),
(83, '10-06-2016 14:27', '20', 'enrahat', '20', '6', '201606101427'),
(84, '10-06-2016 14:27', '20', 'enkotu', '20', '5', '201606101427'),
(85, '10-06-2016 14:32', '26', 'enucuz', '26', '24', '201606101432'),
(86, '10-06-2016 14:33', '26', 'enucuz', '26', '25', '201606101433'),
(87, '10-06-2016 14:33', '26', 'enguzel', '26', '21', '201606101433'),
(88, '10-06-2016 18:05', '20', 'enpahali', '20', '8', '201606101805'),
(89, '11-06-2016 02:05', '26', 'enrahat', '26', '21', '201606110205'),
(90, '11-06-2016 11:57', '22', 'followers', '20', '-1', '201606111157'),
(91, '11-06-2016 11:57', '24', 'followers', '20', '-1', '201606111157'),
(92, '30-07-2016 23:52', '20', 'enguzel', '20', '4', '201607302352'),
(93, '30-07-2016 23:52', '20', 'followers', '24', '-1', '201607302352');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `sehir`
--

CREATE TABLE IF NOT EXISTS `sehir` (
  `kod` int(2) NOT NULL,
  `ad` varchar(15) COLLATE utf8_turkish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `sehir`
--

INSERT INTO `sehir` (`kod`, `ad`) VALUES
(1, 'Adana'),
(10, 'Balıkesir'),
(16, 'Bursa'),
(34, 'İstanbul'),
(35, 'İzmir');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `server`
--

CREATE TABLE IF NOT EXISTS `server` (
  `id` int(11) NOT NULL,
  `status` varchar(1) COLLATE utf8_turkish_ci NOT NULL,
  `message` varchar(30) COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `server`
--

INSERT INTO `server` (`id`, `status`, `message`) VALUES
(1, '1', 'Sunucumuz aktif');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `takip`
--

CREATE TABLE IF NOT EXISTS `takip` (
  `id` int(11) NOT NULL,
  `kim` varchar(3) COLLATE utf8_turkish_ci NOT NULL,
  `kimi` varchar(3) COLLATE utf8_turkish_ci NOT NULL,
  `tarih` varchar(20) COLLATE utf8_turkish_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `takip`
--

INSERT INTO `takip` (`id`, `kim`, `kimi`, `tarih`) VALUES
(39, '20', '22', '11-06-2016 11:57'),
(40, '20', '24', '11-06-2016 11:57'),
(41, '20', '26', '11-06-2016 11:57'),
(42, '24', '20', '03.03.2013 03:03');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `uye`
--

CREATE TABLE IF NOT EXISTS `uye` (
  `id` int(6) NOT NULL,
  `username` varchar(30) COLLATE utf8_turkish_ci DEFAULT NULL,
  `name` varchar(30) COLLATE utf8_turkish_ci DEFAULT NULL,
  `mail` varchar(30) COLLATE utf8_turkish_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_turkish_ci DEFAULT NULL,
  `city` int(2) DEFAULT NULL,
  `bio` varchar(140) COLLATE utf8_turkish_ci DEFAULT NULL,
  `deviceID` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  `profile_pic` varchar(140) CHARACTER SET latin1 DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_turkish_ci;

--
-- Tablo döküm verisi `uye`
--

INSERT INTO `uye` (`id`, `username`, `name`, `mail`, `password`, `city`, `bio`, `deviceID`, `profile_pic`) VALUES
(20, 'sefatunckanat', 'Sefa', 'sefa@mail.com', '827ccb0eea8a706c4c34a16891f84e7b', 10, '20 / Balıkesir Üniversitesi', 'e8935f701eb008c8', 'profile_picture/335606.jpg'),
(22, 'fatihcilasin', 'Fatih Ç', 'fatih@mail.com', '827ccb0eea8a706c4c34a16891f84e7b', 10, 'Beşiktaş', '-1', 'profile_picture/fati.jpg'),
(24, 'mstfylmz19', 'Mustafa Yılmaz', 'eboue@hotmail.com', '0bc9c554bad6d20949782eb0467bda51', 10, 'Yaş 20', 'c6bebdb035c4df6c', 'profile_picture/177603.jpg'),
(26, 'Yusup', 'Yusup Narkuvatov', 'narku@mail.ru', 'f31dafbd60ceb38cd1830c11ea0aefcf', 10, 'Uzaklardan gelen', 'faaf374abbe9df1b', NULL);

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `mekan`
--
ALTER TABLE `mekan`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `olaylar`
--
ALTER TABLE `olaylar`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `sehir`
--
ALTER TABLE `sehir`
  ADD PRIMARY KEY (`kod`);

--
-- Tablo için indeksler `server`
--
ALTER TABLE `server`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `takip`
--
ALTER TABLE `takip`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `uye`
--
ALTER TABLE `uye`
  ADD PRIMARY KEY (`id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `mekan`
--
ALTER TABLE `mekan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=27;
--
-- Tablo için AUTO_INCREMENT değeri `olaylar`
--
ALTER TABLE `olaylar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=94;
--
-- Tablo için AUTO_INCREMENT değeri `takip`
--
ALTER TABLE `takip`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=43;
--
-- Tablo için AUTO_INCREMENT değeri `uye`
--
ALTER TABLE `uye`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=27;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
