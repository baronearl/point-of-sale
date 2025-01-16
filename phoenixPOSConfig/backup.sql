-- MySQL dump 10.13  Distrib 5.6.37, for osx10.6 (i386)
--
-- Host: localhost    Database: pos
-- ------------------------------------------------------
-- Server version	5.6.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `pos`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `pos` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `pos`;

--
-- Table structure for table `expenditure`
--

DROP TABLE IF EXISTS `expenditure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expenditure` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `amount` varchar(255) NOT NULL,
  `comment` varchar(1000) NOT NULL,
  `date` varchar(255) NOT NULL,
  `sales_person` varchar(255) DEFAULT NULL,
  `synced` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expenditure`
--

LOCK TABLES `expenditure` WRITE;
/*!40000 ALTER TABLE `expenditure` DISABLE KEYS */;
INSERT INTO `expenditure` VALUES (1,'3000','For personal use','31-08-2019','baronearl',0);
/*!40000 ALTER TABLE `expenditure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `part_payment_history`
--

DROP TABLE IF EXISTS `part_payment_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `part_payment_history` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) NOT NULL,
  `amount` varchar(255) NOT NULL,
  `date` varchar(255) NOT NULL,
  `synced` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `part_payment_history`
--

LOCK TABLES `part_payment_history` WRITE;
/*!40000 ALTER TABLE `part_payment_history` DISABLE KEYS */;
INSERT INTO `part_payment_history` VALUES (1,'Okoro Mmadu','5000','26-08-2019',0);
/*!40000 ALTER TABLE `part_payment_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `sku` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `quantity` int(11) DEFAULT '0',
  `synced` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Manchester United','ManUJersey','50000.0',-121,0),(2,'Shirts','Shirts4500','4500.0',-34,0);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sales` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(1000) NOT NULL,
  `phone` varchar(1000) DEFAULT NULL,
  `date` varchar(1000) NOT NULL,
  `total` varchar(1000) DEFAULT NULL,
  `sales_person` varchar(1000) DEFAULT NULL,
  `synced` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,'Okafor Chukwuma','','25-08-2019','250000.0','baronearl',1),(2,'Okafor Chukwuma','','25-08-2019','40000.0','baronearl',1),(3,'Baron Earl','','26-08-2019','95200.0','abonima',0),(4,'Okoro Mmadu','','26-08-2019','250000.0','baronearl',0),(5,'Okoro Mmadu','','31-08-2019','18000.0','baronearl',0),(6,'Susan Kado','','31-08-2019','151000.0','abonima',0),(7,'Okoro Chinaza','','31-08-2019','116000.0','baronearl',0);
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_items`
--

DROP TABLE IF EXISTS `sales_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sales_items` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `sales_id` int(255) NOT NULL,
  `quantity` int(255) NOT NULL,
  `price` varchar(1000) NOT NULL,
  `item` varchar(255) NOT NULL,
  `total` varchar(255) NOT NULL,
  `credit` varchar(255) NOT NULL DEFAULT 'No',
  `synced` int(255) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_items`
--

LOCK TABLES `sales_items` WRITE;
/*!40000 ALTER TABLE `sales_items` DISABLE KEYS */;
INSERT INTO `sales_items` VALUES (1,1,20,'5000','Manchester','100000','No',1),(2,2,20,'4000','Manchester','40000','Yes',1),(3,1,20,'5000','Manchester','100000','No',1),(4,1,20,'5000','Manchester','100000','No',1),(6,3,78,'900.0','Manchester United - ManUJersey','70200.0','No',0),(7,4,50,'5000.0','Manchester United - ManUJersey','250000.0','No',0),(8,5,3,'6000.0','Manchester United - ManUJersey','18000.0','Yes',0),(9,6,30,'5000.0','Shirts - Shirts4500','150000.0','No',0),(10,6,5,'200.0','Manchester United - ManUJersey','1000.0','Yes',0),(11,7,5,'5000.0','Manchester United - ManUJersey','25000.0','Yes',0),(12,7,10,'7500.0','Manchester United - ManUJersey','75000.0','No',0),(13,7,4,'4000.0','Shirts - Shirts4500','16000.0','Yes',0);
/*!40000 ALTER TABLE `sales_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `settings` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `company_name` varchar(1000) DEFAULT NULL,
  `address` varchar(1000) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES (1,'{Company Name}','{Company Address}','{Company Phone}');
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_history`
--

DROP TABLE IF EXISTS `stock_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_history` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) NOT NULL,
  `quantity` int(255) NOT NULL,
  `date` varchar(255) NOT NULL,
  `synced` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_history`
--

LOCK TABLES `stock_history` WRITE;
/*!40000 ALTER TABLE `stock_history` DISABLE KEYS */;
INSERT INTO `stock_history` VALUES (1,'Manchester United - ManUJersey',100,'26-08-2019',0);
/*!40000 ALTER TABLE `stock_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_to_delete`
--

DROP TABLE IF EXISTS `table_to_delete`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_to_delete` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `type_id` int(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_to_delete`
--

LOCK TABLES `table_to_delete` WRITE;
/*!40000 ALTER TABLE `table_to_delete` DISABLE KEYS */;
INSERT INTO `table_to_delete` VALUES (1,'2',0),(2,'sales_items',5);
/*!40000 ALTER TABLE `table_to_delete` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_to_update`
--

DROP TABLE IF EXISTS `table_to_update`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_to_update` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `type_id` int(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_to_update`
--

LOCK TABLES `table_to_update` WRITE;
/*!40000 ALTER TABLE `table_to_update` DISABLE KEYS */;
INSERT INTO `table_to_update` VALUES (1,'sales_items',7);
/*!40000 ALTER TABLE `table_to_update` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(255) NOT NULL AUTO_INCREMENT,
  `fname` varchar(255) NOT NULL,
  `lname` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL DEFAULT 'sales',
  `gender` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Okafor','Chukwuma','baronearl','d9e3c025693eaa3797af61c2a31fb647aabf60c5096f2e757c1178f914ae24f7c1989ea501da95daa6014b221ec5ec7b868573be88a2adc77cbdea6270f56cbb','superadmin','male'),(2,'Obumneme','Aniekwe','abonima','d9e3c025693eaa3797af61c2a31fb647aabf60c5096f2e757c1178f914ae24f7c1989ea501da95daa6014b221ec5ec7b868573be88a2adc77cbdea6270f56cbb','sales','male'),(3,'root','user','root','8d93e07f41866c1049be26e369db123d136ef0a57c7c75be2b89d9029bf6384ee2cbf8df60907b641bb437d23c07d79bb03a37aa2ee42781b3948b0e687e4ad6','superadmin','male'),(4,'Okoro','Rufus','frendzy','d9e3c025693eaa3797af61c2a31fb647aabf60c5096f2e757c1178f914ae24f7c1989ea501da95daa6014b221ec5ec7b868573be88a2adc77cbdea6270f56cbb','superadmin','male');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-01 11:12:11
