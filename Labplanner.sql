CREATE DATABASE  IF NOT EXISTS `lab_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `lab_db`;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: lab_db
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `composition`
--

DROP TABLE IF EXISTS `composition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `composition` (
  `id` bigint NOT NULL,
  `amount` double DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9kvv9xsvv66sjlwl0y2kdrg9o` (`product_id`),
  CONSTRAINT `FK9kvv9xsvv66sjlwl0y2kdrg9o` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `composition`
--

LOCK TABLES `composition` WRITE;
/*!40000 ALTER TABLE `composition` DISABLE KEYS */;
INSERT INTO `composition` VALUES (144,46.67,127),(145,10.1,128),(146,8.29,129),(147,23.89,130),(148,2.45,131),(149,8.6,132),(150,6.5,133),(151,20.72,127),(152,14.2,128),(153,21.24,129),(154,23.84,130),(155,12.91,131),(156,7.1,132),(157,6.5,133),(158,61.7,127),(159,11.2,128),(160,17.8,130),(161,9.3,131),(162,6.9,133);
/*!40000 ALTER TABLE `composition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `continuity`
--

DROP TABLE IF EXISTS `continuity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `continuity` (
  `id` bigint NOT NULL,
  `hour` int DEFAULT NULL,
  `minutes` int DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `continuity`
--

LOCK TABLES `continuity` WRITE;
/*!40000 ALTER TABLE `continuity` DISABLE KEYS */;
INSERT INTO `continuity` VALUES (166,0,0,'No'),(167,8,0,'Soft'),(168,24,0,'Hard'),(169,12,0,'Soft'),(170,0,0,'Hard'),(171,24,0,'Soft');
/*!40000 ALTER TABLE `continuity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device` (
  `id` bigint NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `date_created` datetime(6) NOT NULL,
  `devicename` varchar(255) NOT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  `device_type` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hwss20jivqe4qnfknr42athne` (`devicename`),
  KEY `FK44jyff85yjwhv24kcxsquyt9e` (`device_type`),
  CONSTRAINT `FK44jyff85yjwhv24kcxsquyt9e` FOREIGN KEY (`device_type`) REFERENCES `device_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
INSERT INTO `device` VALUES (114,NULL,'2020-04-14 15:39:31.146013','Autosaw 1','2020-04-14 15:39:31.146013',100),(115,NULL,'2020-04-14 15:39:31.156022','Autosaw 2','2020-04-14 15:39:31.156022',100),(116,NULL,'2020-04-14 15:39:31.165030','Uniframe 1','2020-04-14 15:39:31.165030',110),(117,NULL,'2020-04-14 15:39:31.176039','Vacuum Setup 1','2020-04-14 15:39:31.176039',111),(118,NULL,'2020-04-14 15:39:31.186049','Water Bath 1','2020-04-14 15:39:31.186049',112),(119,NULL,'2020-04-14 15:39:31.195057','Wheel Tracking Test 1','2020-04-14 15:39:31.195057',113),(120,'Perfect oven to bake a pizza in your spare times','2020-04-14 15:39:31.207068','Oven 1','2020-04-14 15:39:31.207068',106),(121,NULL,'2020-04-14 15:39:31.220080','Oven 2','2020-04-14 15:39:31.220080',106),(122,NULL,'2020-04-14 15:39:31.229088','Oven 3','2020-04-14 15:39:31.229088',106);
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_information`
--

DROP TABLE IF EXISTS `device_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_information` (
  `id` bigint NOT NULL,
  `information` varchar(255) DEFAULT NULL,
  `information_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_information`
--

LOCK TABLES `device_information` WRITE;
/*!40000 ALTER TABLE `device_information` DISABLE KEYS */;
INSERT INTO `device_information` VALUES (43,'taciti offendit elaboraret atomorum necessitatibus mucius recteque orci decore tristique impetus novum cetero alia ubique iusto quisque graeci nullam fastidii','Manual'),(44,'idque tamquam prompta melius efficiantur commodo veritus fusce honestatis lacinia epicurei quem platea dissentiunt ultrices aliquid ferri habemus senectus laoreet','Maintenance'),(45,'cum congue sanctus ex expetendis mutat convallis pharetra dolores graece melius docendi fusce appetere nibh has salutatus ligula libero tempor','Calibration'),(46,'tristique habeo velit senserit quaestio ridiculus quas corrumpit ea auctor laoreet expetenda venenatis quaeque quam integer propriae minim scripserit aperiri','Safety instruction card'),(47,'proin augue rutrum disputationi sem cum phasellus sagittis consetetur eget sanctus omnesque epicuri orci eripuit eruditi posse egestas instructior parturient','Manual'),(48,'per diam falli alienum debet nisi etiam ludus metus equidem scripta intellegebat necessitatibus affert te honestatis legere dicat pri viderer','Maintenance'),(49,'definitionem eros iuvaret ludus ceteros quod lectus deseruisse persequeris falli eos tota reprehendunt mollis detraxit saepe commune odio proin pellentesque','Calibration'),(50,'equidem proin inimicus porta ornare adipiscing curabitur vel molestiae convallis ancillae donec idque delectus rhoncus delicata massa egestas nonumes inani','Safety instruction card'),(51,'no eripuit constituto docendi te tellus condimentum omittam unum nonumes appareat postea delenit mel semper pretium inciderint inceptos eloquentiam sapien','Manual'),(52,'ne quo penatibus verterem suscipit deterruisset leo dolorem porta suscipiantur ignota deserunt cetero fringilla hinc ultrices veri lectus odio eos','Maintenance'),(53,'eget possim euripidis propriae quaestio mandamus repudiandae enim movet viverra minim ei dicam diam definitionem scripserit discere assueverit fermentum maiorum','Calibration'),(54,'usu reprehendunt pertinacia dictas indoctum porttitor autem gravida habemus pertinacia theophrastus nibh fabellas voluptatibus sollicitudin suspendisse consectetur atqui commodo dolores','Safety instruction card'),(55,'fames suscipit referrentur diam interesset iuvaret recteque malesuada tempor oratio neque contentiones conclusionemque putent tantas ad phasellus diam fusce efficiantur','Manual'),(56,'bibendum non consequat dictumst sapientem vituperata erroribus sollicitudin deterruisset vel vis in veri ludus porta vocent wisi persequeris eirmod reque','Maintenance'),(57,'dicit lacinia habitasse docendi eloquentiam mnesarchum veritus audire volumus vituperata habitant duo detracto dis malorum quas nam referrentur tamquam quo','Calibration'),(58,'alia maecenas his tristique hac dissentiunt impetus epicurei cursus euismod dapibus expetenda tellus ullamcorper sumo nunc enim ludus senectus lacus','Safety instruction card'),(59,'animal brute ligula inceptos aperiri vidisse rutrum semper velit libero disputationi tritani populo possim sit omnesque magna quidam vituperatoribus interpretaris','Manual'),(60,'rutrum praesent vero repudiare vitae instructior referrentur tempor maiorum nunc saperet ignota conclusionemque viderer sociis qualisque vivamus constituam fuisset viderer','Maintenance'),(61,'mnesarchum persequeris lectus legimus ultrices explicari alia mediocrem dictum molestie tamquam luptatum veniam quaeque deterruisset pellentesque tale netus singulis hac','Calibration'),(62,'eum quas proin scripserit malesuada menandri pulvinar alienum elitr omittantur iisque eius sodales quaestio quaerendum montes aeque consul mazim non','Safety instruction card'),(63,'scelerisque scelerisque maiorum inani atomorum feugait euripidis risus ancillae atqui erat integer graecis pericula cubilia quam rutrum nullam honestatis pericula','Manual'),(64,'quam cras consequat possit natoque vituperata pri wisi elitr pro viris tritani dicunt sapien verterem deseruisse interdum malorum ignota ligula','Maintenance'),(65,'mnesarchum augue legere maluisset dicit iisque accusata consectetur praesent tortor solet maximus dolorum etiam commodo commodo fugit vis novum maluisset','Calibration'),(66,'alterum doctus consectetur tantas epicuri inani odio sententiae porttitor singulis neque parturient usu eget gubergren neque vituperatoribus dolor fastidii civibus','Safety instruction card'),(67,'legere parturient conubia prompta dolores gravida lacinia lectus porro venenatis consequat intellegat viderer natum definitionem convallis conclusionemque legere doming maiestatis','Manual'),(68,'habitant neque quaeque facilisis conclusionemque unum porttitor suspendisse diam non civibus sed enim propriae elit eloquentiam primis verterem noluisse ornare','Maintenance'),(69,'iriure mandamus fugit persius wisi iriure ipsum viverra torquent aliquet voluptatibus vis mel ullamcorper quas reformidans mutat suspendisse non reformidans','Calibration'),(70,'comprehensam cursus nec intellegat dictumst egestas maecenas adolescens ridens venenatis partiendo mei at signiferumque oporteat unum nec fusce deseruisse convenire','Safety instruction card'),(71,'vix singulis proin simul duis corrumpit vestibulum blandit interesset doming','Other'),(72,'deterruisset theophrastus maluisset eos cum invenire habitant elementum deterruisset senserit sea sea repudiare omittam mus utamur pericula deserunt atomorum accusata','Manual'),(73,'te principes a dicam sociis instructior ocurreret integer litora regione audire volumus orci curabitur signiferumque dui signiferumque repudiandae mazim tellus','Maintenance'),(74,'habemus verterem netus penatibus mauris cetero tractatos liber propriae urna luctus torquent ornare class commune vehicula ius animal iriure vis','Calibration'),(75,'sollicitudin maluisset tristique explicari erroribus risus neque vivamus numquam duis vivamus tibique detracto usu doctus viris libero wisi eloquentiam eam','Safety instruction card'),(76,'putent mazim ligula animal disputationi ut autem maiestatis adolescens impetus posuere mea sanctus duo integer affert penatibus decore rhoncus orci','Manual'),(77,'pertinax mnesarchum numquam movet idque omnesque praesent posidonium maluisset honestatis gloriatur ubique sententiae viris quisque mollis aliquip usu imperdiet epicuri','Maintenance'),(78,'fames a his pri utamur molestie definitiones adipiscing conubia splendide persecuti dis nullam impetus latine libero labores constituam alienum utamur','Calibration'),(79,'iuvaret molestie hendrerit pertinax ornatus latine graece praesent fuisset eum liber vituperata maximus tota delectus altera putent idque fabulas enim','Safety instruction card'),(80,'pretium prodesset voluptatibus aliquam fabulas tempor hendrerit possim ubique nibh inceptos primis gloriatur consul falli sanctus cursus dui sumo simul','Manual'),(81,'appetere vero adhuc magna enim populo fames consetetur nostrum congue euripidis postea mei latine deterruisset iusto pharetra epicurei inceptos dico','Maintenance'),(82,'duo postulant idque torquent reprehendunt aptent fringilla constituam in eos lacus legimus nonumy dicam docendi dicant tota velit autem duo','Calibration'),(83,'tincidunt rutrum massa ultricies natoque graeco vivendo patrioque tortor affert deserunt dolores te doming solet diam definitiones putent ridens pericula','Safety instruction card'),(84,'quo delenit iisque intellegebat utinam sociis alia solum adipiscing habitant malorum ullamcorper ex utamur tempus deseruisse dissentiunt utinam sollicitudin signiferumque','Manual'),(85,'at molestiae litora patrioque habeo fusce dissentiunt potenti similique posse necessitatibus detracto eirmod petentium tempus eros tritani sanctus commodo quem','Maintenance'),(86,'nonumy molestie libris euripidis ne partiendo laudem primis sumo commune prompta mazim curabitur lacinia postulant enim habitasse primis sociis vis','Calibration'),(87,'cras cu augue indoctum aliquid postea pulvinar odio tantas facilisis pellentesque novum electram persecuti ornare scelerisque ridiculus diam fabellas turpis','Safety instruction card'),(88,'orci reque lacus lorem antiopam voluptatibus class iisque salutatus vocibus menandri blandit prodesset himenaeos vis aeque lacus ut dignissim cetero','Manual'),(89,'molestiae similique nobis himenaeos reque lacinia conubia mus gloriatur turpis enim posse mea causae ante dolores accumsan sit signiferumque singulis','Maintenance'),(90,'molestie indoctum vel nunc simul harum mea et pertinacia nobis inceptos neglegentur audire suavitate ocurreret utroque suscipit ludus habitasse doctus','Calibration'),(91,'esse utamur doming proin nec quas iusto evertitur fuisset porro atomorum viverra viverra arcu habeo vero quot habitant luctus suas','Safety instruction card'),(92,'viris tibique persius natum ceteros platea aliquip sodales affert non accusata vocibus fastidii cras mattis fusce detracto ludus quidam brute','Manual'),(93,'vivendo taciti ullamcorper pulvinar fermentum rhoncus tincidunt commodo omittam libris congue vocent honestatis ornatus propriae pertinacia recteque dicant maiorum ancillae','Maintenance'),(94,'vis repudiandae platonem invenire tristique ferri class augue comprehensam periculis habitasse falli mollis tamquam nihil scripserit alterum ipsum omnesque reprehendunt','Calibration'),(95,'deserunt persecuti invidunt percipit maiorum netus affert aperiri error habeo harum consectetuer habitant tota bibendum regione dicant ei himenaeos vocibus','Safety instruction card'),(96,'fermentum agam metus minim omittantur condimentum falli volumus movet labores utroque dolore ornare volumus atqui iriure tritani posuere habemus dicam','Manual'),(97,'euripidis vivendo possim falli enim debet iuvaret cu dui conubia numquam periculis homero justo odio constituam donec splendide accusata mediocritatem','Maintenance'),(98,'esse altera contentiones dico reque graeco ceteros option verear nominavi metus alterum fugit etiam vel est eam pri iisque postea','Calibration'),(99,'quas aliquid reprehendunt justo deserunt nisi aeque expetendis persequeris sanctus accusata dolores litora animal efficitur dictumst idque ocurreret accommodare morbi','Safety instruction card');
/*!40000 ALTER TABLE `device_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_information_files`
--

DROP TABLE IF EXISTS `device_information_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_information_files` (
  `device_information_id` bigint NOT NULL,
  `files` varchar(255) DEFAULT NULL,
  KEY `FKl67849funbrnv8ekkj81bheuk` (`device_information_id`),
  CONSTRAINT `FKl67849funbrnv8ekkj81bheuk` FOREIGN KEY (`device_information_id`) REFERENCES `device_information` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_information_files`
--

LOCK TABLES `device_information_files` WRITE;
/*!40000 ALTER TABLE `device_information_files` DISABLE KEYS */;
INSERT INTO `device_information_files` VALUES (67,'placeholder.pdf'),(69,'placeholder.xlsx'),(69,'placeholder.jpg');
/*!40000 ALTER TABLE `device_information_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_type`
--

DROP TABLE IF EXISTS `device_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_type` (
  `id` bigint NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `device_picture_name` varchar(255) DEFAULT NULL,
  `device_type_name` varchar(255) DEFAULT NULL,
  `overnightuse` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_type`
--

LOCK TABLES `device_type` WRITE;
/*!40000 ALTER TABLE `device_type` DISABLE KEYS */;
INSERT INTO `device_type` VALUES (100,'#FF00FF',NULL,'Autosaw',_binary ''),(101,'#FFAFAF',NULL,'Balance',_binary '\0'),(102,'#00FF00',NULL,'Big Mixer',_binary ''),(103,'#000000',NULL,'Caliper',_binary ''),(104,'#FFFF00',NULL,'Cooling chamber',_binary ''),(105,'#00FFFF',NULL,'Gyratory',_binary '\0'),(106,'#404040','Oven.jpg','Oven',_binary ''),(107,'#FF0000',NULL,'Plate Compactor',_binary '\0'),(108,'#FFC800',NULL,'Small Mixer',_binary ''),(109,'#808080',NULL,'SVM Setup',_binary '\0'),(110,'#FFFFFF',NULL,'Uniframe',_binary '\0'),(111,'#0000FF',NULL,'Vacuum Setup',_binary ''),(112,'#222222',NULL,'Water Bath',_binary '\0'),(113,'#C0C0C0',NULL,'Wheel Tracking Test',_binary '');
/*!40000 ALTER TABLE `device_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_type_information`
--

DROP TABLE IF EXISTS `device_type_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_type_information` (
  `device_type_id` bigint NOT NULL,
  `device_information_id` bigint NOT NULL,
  KEY `FKj8flc6v6w5c5c3lcpseyy7weh` (`device_information_id`),
  KEY `FKa7wwo70oxrbl6wen2ew6hae6c` (`device_type_id`),
  CONSTRAINT `FKa7wwo70oxrbl6wen2ew6hae6c` FOREIGN KEY (`device_type_id`) REFERENCES `device_type` (`id`),
  CONSTRAINT `FKj8flc6v6w5c5c3lcpseyy7weh` FOREIGN KEY (`device_information_id`) REFERENCES `device_information` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_type_information`
--

LOCK TABLES `device_type_information` WRITE;
/*!40000 ALTER TABLE `device_type_information` DISABLE KEYS */;
INSERT INTO `device_type_information` VALUES (100,43),(100,44),(100,45),(100,46),(101,47),(101,48),(101,49),(101,50),(102,51),(102,52),(102,53),(102,54),(103,55),(103,56),(103,57),(103,58),(104,59),(104,60),(104,61),(104,62),(105,63),(105,64),(105,65),(105,66),(106,67),(106,68),(106,69),(106,70),(106,71),(107,72),(107,73),(107,74),(107,75),(108,76),(108,77),(108,78),(108,79),(109,80),(109,81),(109,82),(109,83),(110,84),(110,85),(110,86),(110,87),(111,88),(111,89),(111,90),(111,91),(112,92),(112,93),(112,94),(112,95),(113,96),(113,97),(113,98),(113,99);
/*!40000 ALTER TABLE `device_type_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `experiment`
--

DROP TABLE IF EXISTS `experiment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `experiment` (
  `id` bigint NOT NULL,
  `end_date` varchar(255) DEFAULT NULL,
  `experimentname` varchar(255) NOT NULL,
  `exp_mixture_comment` varchar(255) DEFAULT NULL,
  `start_date` varchar(255) DEFAULT NULL,
  `experiment_type` bigint NOT NULL,
  `exp_mixture` bigint DEFAULT NULL,
  `user` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nq75g55ruqg0n08lyc1fypusb` (`experimentname`),
  UNIQUE KEY `UK_3o2yprnfvt704q8vwnvq80u93` (`user`),
  KEY `FKs579bbur70ivx5qq54w15vdrt` (`experiment_type`),
  KEY `FKc3dxyte3anppa9b2jwdj7phr7` (`exp_mixture`),
  CONSTRAINT `FKc3dxyte3anppa9b2jwdj7phr7` FOREIGN KEY (`exp_mixture`) REFERENCES `mixture` (`id`),
  CONSTRAINT `FKfx471e4koh0yk2ljfqy89x51b` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FKs579bbur70ivx5qq54w15vdrt` FOREIGN KEY (`experiment_type`) REFERENCES `experiment_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `experiment`
--

LOCK TABLES `experiment` WRITE;
/*!40000 ALTER TABLE `experiment` DISABLE KEYS */;
/*!40000 ALTER TABLE `experiment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `experiment_type`
--

DROP TABLE IF EXISTS `experiment_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `experiment_type` (
  `id` bigint NOT NULL,
  `expname` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lh0y7nv05g20guqjmolnxqibo` (`expname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `experiment_type`
--

LOCK TABLES `experiment_type` WRITE;
/*!40000 ALTER TABLE `experiment_type` DISABLE KEYS */;
INSERT INTO `experiment_type` VALUES (183,'ITSR'),(184,'Wheel Tracking Test');
/*!40000 ALTER TABLE `experiment_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (222),(222),(222),(222),(222),(222),(222),(222),(222),(222),(222),(222),(222),(222),(222),(222),(222);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mix_comp`
--

DROP TABLE IF EXISTS `mix_comp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mix_comp` (
  `prod_id` bigint NOT NULL,
  `comp_id` bigint NOT NULL,
  KEY `FK96flvqop1qr7pmsag5nj2wwtc` (`comp_id`),
  KEY `FK505hv3obei62v840qvc02n89k` (`prod_id`),
  CONSTRAINT `FK505hv3obei62v840qvc02n89k` FOREIGN KEY (`prod_id`) REFERENCES `mixture` (`id`),
  CONSTRAINT `FK96flvqop1qr7pmsag5nj2wwtc` FOREIGN KEY (`comp_id`) REFERENCES `composition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mix_comp`
--

LOCK TABLES `mix_comp` WRITE;
/*!40000 ALTER TABLE `mix_comp` DISABLE KEYS */;
INSERT INTO `mix_comp` VALUES (163,144),(163,145),(163,146),(163,147),(163,148),(163,149),(163,150),(164,151),(164,152),(164,153),(164,154),(164,155),(164,156),(164,157),(165,158),(165,159),(165,160),(165,161),(165,162);
/*!40000 ALTER TABLE `mix_comp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mix_tag`
--

DROP TABLE IF EXISTS `mix_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mix_tag` (
  `prod_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  KEY `FK227jmh17n0t7ioej20ou86mp` (`tag_id`),
  KEY `FK1aluqforr05pfrvwfe1jkucbf` (`prod_id`),
  CONSTRAINT `FK1aluqforr05pfrvwfe1jkucbf` FOREIGN KEY (`prod_id`) REFERENCES `mixture` (`id`),
  CONSTRAINT `FK227jmh17n0t7ioej20ou86mp` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mix_tag`
--

LOCK TABLES `mix_tag` WRITE;
/*!40000 ALTER TABLE `mix_tag` DISABLE KEYS */;
INSERT INTO `mix_tag` VALUES (163,124),(164,123),(165,126);
/*!40000 ALTER TABLE `mix_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mixture`
--

DROP TABLE IF EXISTS `mixture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mixture` (
  `id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mixture`
--

LOCK TABLES `mixture` WRITE;
/*!40000 ALTER TABLE `mixture` DISABLE KEYS */;
INSERT INTO `mixture` VALUES (163,'habitant ornare consetetur proin detracto consequat reque saperet platea vitae mei error graece quaeque mandamus ei inciderint accommodare quas gravida','APT-C'),(164,'eleifend sed ultrices ludus magna referrentur nisi phasellus vero iudicabit luptatum oporteat eruditi ne dolore consul facilisi adhuc inani fugit','AB-4C'),(165,'nam sadipscing reformidans sea menandri pharetra posuere iuvaret noster maecenas sadipscing expetendis torquent no cum fuisset primis urna solet sociis','SMA');
/*!40000 ALTER TABLE `mixture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `privilege`
--

DROP TABLE IF EXISTS `privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `privilege` (
  `id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `privilege`
--

LOCK TABLES `privilege` WRITE;
/*!40000 ALTER TABLE `privilege` DISABLE KEYS */;
INSERT INTO `privilege` VALUES (1,NULL,'logon'),(2,NULL,'Planning - Book step/experiment'),(3,NULL,'Planning - Delete step/experiment own'),(4,NULL,'Planning - Delete step/experiment own/promotor'),(5,NULL,'Planning - Delete step/experiment all'),(6,NULL,'Planning - Adjust step/experiment own'),(7,NULL,'Planning - Adjust step/experiment own/promotor'),(8,NULL,'Planning - Adjust step/experiment all'),(9,NULL,'Planning - Make new step'),(10,NULL,'Planning - Make new experiment'),(11,NULL,'Planning - Start within office hours'),(12,NULL,'Planning - Start outside office hours'),(13,NULL,'Planning - Overview'),(14,NULL,'Stock - Aggregates + Bitumen Read only - Basic'),(15,NULL,'Stock - Aggregates + Bitumen Read only - Advanced'),(16,NULL,'Stock - Aggregates + Bitumen Modify - Advanced'),(17,NULL,'Stock - Modify - All'),(18,NULL,'Stock - Consumables + Other Read only - Advanced'),(19,NULL,'User Management'),(20,NULL,'Database - Read only - Basic'),(21,NULL,'Database - Modify - Advanced'),(22,NULL,'Database - Modify - All'),(23,NULL,'Reports - Modify - Advanced'),(24,NULL,'Reports - Modify - All'),(25,NULL,'Device - Read only - Basic'),(26,NULL,'Device - Modify - All'),(27,NULL,'Console Access'),(190,NULL,'logon'),(191,NULL,'Planning - Book step/experiment'),(192,NULL,'Planning - Delete step/experiment own'),(193,NULL,'Planning - Delete step/experiment own/promotor'),(194,NULL,'Planning - Delete step/experiment all'),(195,NULL,'Planning - Adjust step/experiment own'),(196,NULL,'Planning - Adjust step/experiment own/promotor'),(197,NULL,'Planning - Adjust step/experiment all'),(198,NULL,'Planning - Make new step'),(199,NULL,'Planning - Make new experiment'),(200,NULL,'Planning - Start within office hours'),(201,NULL,'Planning - Start outside office hours'),(202,NULL,'Planning - Overview'),(203,NULL,'Stock - Aggregates + Bitumen Read only - Basic'),(204,NULL,'Stock - Aggregates + Bitumen Read only - Advanced'),(205,NULL,'Stock - Aggregates + Bitumen Modify - Advanced'),(206,NULL,'Stock - Modify - All'),(207,NULL,'Stock - Consumables + Other Read only - Advanced'),(208,NULL,'User Management'),(209,NULL,'Database - Read only - Basic'),(210,NULL,'Database - Modify - Advanced'),(211,NULL,'Database - Modify - All'),(212,NULL,'Reports - Modify - Advanced'),(213,NULL,'Reports - Modify - All'),(214,NULL,'Device - Read only - Basic'),(215,NULL,'Device - Modify - All'),(216,NULL,'Console Access');
/*!40000 ALTER TABLE `privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_tag`
--

DROP TABLE IF EXISTS `prod_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prod_tag` (
  `prod_id` bigint NOT NULL,
  `tag_id` bigint NOT NULL,
  KEY `FKhwn5v1wc38ws4aidyivqyina7` (`tag_id`),
  KEY `FKbdqqro8ksqyr0yv9722q9k569` (`prod_id`),
  CONSTRAINT `FKbdqqro8ksqyr0yv9722q9k569` FOREIGN KEY (`prod_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKhwn5v1wc38ws4aidyivqyina7` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_tag`
--

LOCK TABLES `prod_tag` WRITE;
/*!40000 ALTER TABLE `prod_tag` DISABLE KEYS */;
INSERT INTO `prod_tag` VALUES (127,125),(128,123),(129,123),(130,125),(131,124),(132,126),(133,126),(134,126);
/*!40000 ALTER TABLE `prod_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `last_updated_by_id` bigint DEFAULT NULL,
  `low_stock_level` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `product_creator_id` bigint DEFAULT NULL,
  `properties_url` varchar(255) DEFAULT NULL,
  `reserved_stock_level` double DEFAULT NULL,
  `stock_level` double DEFAULT NULL,
  `unit_cost` double DEFAULT NULL,
  `units` int DEFAULT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (127,'2020-04-14 15:39:31.288142','te esse iaculis tristique morbi adipiscing omittam cubilia oporteat vim sapientem invenire viverra hendrerit atqui fabulas minim offendit verterem quem','locatie2',5,200,'placeholder1',5,'dignissim prompta viverra neque natoque mea quidam quisque',1,2000,1,0,'2020-04-14 15:39:31.288142'),(128,'2020-04-14 15:39:31.306158','eros patrioque quis efficitur simul eius veri tristique et periculis nonumy veri singulis dignissim tale patrioque veniam ut fusce vulputate','locatie2',5,200,'placeholder2',5,'mucius elementum ocurreret regione graece sollicitudin condimentum eius',1,2000,1,0,'2020-04-14 15:39:31.306158'),(129,'2020-04-14 15:39:31.316167','eum tristique labores massa meliore feugiat a euismod elaboraret luctus dolores vituperatoribus minim elementum solum periculis assueverit solet solum antiopam','locatie2',5,200,'placeholder3',5,'brute indoctum vocent ceteros conubia consetetur labores deterruisset',1,2000,1,0,'2020-04-14 15:39:31.316167'),(130,'2020-04-14 15:39:31.326176','qui efficiantur maluisset elitr facilisis conubia partiendo persequeris graece iaculis repudiandae veniam hac leo cursus augue legere porttitor a pharetra','locatie2',5,200,'placeholder4',5,'commodo dicta theophrastus curae condimentum tacimates ante quaeque',1,2000,1,0,'2020-04-14 15:39:31.326176'),(131,'2020-04-14 15:39:31.339188','aliquid netus nihil moderatius nonumes utroque his erat turpis fugit eum pellentesque adipiscing iuvaret nulla noluisse reprehendunt suavitate maiorum te','locatie2',5,100,'placeholder5',5,'detraxit velit sit felis possit vocibus mutat posidonium',1,1000,1,0,'2020-04-14 15:39:31.339188'),(132,'2020-04-14 15:39:31.349197','antiopam quaerendum ornare ceteros pericula ludus fuisset qui ubique omnesque brute numquam sale nullam regione ac maecenas pretium dissentiunt pretium','locatie2',5,100,'placeholder6',5,'facilis latine tractatos mentitum non alienum adolescens senectus',1,500,1,0,'2020-04-14 15:39:31.349197'),(133,'2020-04-14 15:39:31.358205','cum nec salutatus dicit potenti conubia nihil eruditi affert singulis laudem suscipit animal erat option orci nibh tation veniam his','locatie2',5,25,'placeholder7',5,'movet atqui intellegebat mandamus eget quo alienum tortor',1,200,1,2,'2020-04-14 15:39:31.358205'),(134,'2020-04-14 15:39:31.370217','verterem erat integer persius mea convallis quam sagittis eloquentiam noluisse varius felis constituam dissentiunt sit meliore utinam tantas eripuit evertitur','locatie2',5,1,'placeholder8',5,'class ius congue percipit necessitatibus deterruisset interpretaris feugiat',1,90,1,1,'2020-04-14 15:39:31.370217');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relation`
--

DROP TABLE IF EXISTS `relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `relation` (
  `id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `user` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKwk32b822yg2pvcikbpcouomk` (`user`),
  CONSTRAINT `FKwk32b822yg2pvcikbpcouomk` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relation`
--

LOCK TABLES `relation` WRITE;
/*!40000 ALTER TABLE `relation` DISABLE KEYS */;
INSERT INTO `relation` VALUES (42,'testrelation',41);
/*!40000 ALTER TABLE `relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relation_students`
--

DROP TABLE IF EXISTS `relation_students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `relation_students` (
  `relation_id` bigint NOT NULL,
  `students_id` bigint NOT NULL,
  PRIMARY KEY (`relation_id`,`students_id`),
  KEY `FKdkc21pcf4pt4yp5w1coa3e2mv` (`students_id`),
  CONSTRAINT `FKdkc21pcf4pt4yp5w1coa3e2mv` FOREIGN KEY (`students_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKjnaci9bd2a0t3nowijadv24d4` FOREIGN KEY (`relation_id`) REFERENCES `relation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relation_students`
--

LOCK TABLES `relation_students` WRITE;
/*!40000 ALTER TABLE `relation_students` DISABLE KEYS */;
INSERT INTO `relation_students` VALUES (42,39),(42,40);
/*!40000 ALTER TABLE `relation_students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `creator_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8s7jq27vqk69s4mjyyy29x2a8` (`creator_id`),
  CONSTRAINT `FK8s7jq27vqk69s4mjyyy29x2a8` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` VALUES (185,'solet conubia semper graeco mattis postulant nisl causae conclusionemque solum theophrastus tempus diam antiopam quaeque posuere dis menandri constituto suavitate quas porro quem scripserit invenire','Autosaw is broken',36),(186,'graeco meliore ipsum ceteros noster aenean definiebas blandit nec sadipscing','Fancy Title',35),(187,'decore torquent te persequeris eum cetero civibus ex idque utroque maximus legimus maluisset duis ornatus nulla eum equidem fusce nobis usu laudem instructior saepe contentiones esse dolores facilis discere ludus','Sand mixture is not in stock',33),(188,'quisque himenaeos graeco urbanitas prompta ponderum inani latine mnesarchum erat phasellus pharetra est ne rhoncus ipsum debet viverra mandamus tale','Lab was closed yesterday',40),(189,'mazim dolore curae per fermentum equidem possim delenit atomorum duis te maximus fames maluisset invidunt orci leo necessitatibus amet egestas reformidans vero patrioque erroribus inani','Placeholder title',41);
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL,
  `date_created` datetime(6) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (28,'2020-04-14 15:39:30.143374','Bachelorstudent','2020-04-14 15:39:30.144376'),(29,'2020-04-14 15:39:30.165397','Masterstudent','2020-04-14 15:39:30.165397'),(30,'2020-04-14 15:39:30.182411','Researcher','2020-04-14 15:39:30.182411'),(31,'2020-04-14 15:39:30.197425','Administrator','2020-04-14 15:39:30.197425'),(217,'2020-04-14 15:39:52.827038','Bachelorstudent','2020-04-14 15:39:52.827038'),(218,'2020-04-14 15:39:52.850059','Masterstudent','2020-04-14 15:39:52.850059'),(219,'2020-04-14 15:39:52.866074','Researcher','2020-04-14 15:39:52.866074'),(220,'2020-04-14 15:39:52.884090','Administrator','2020-04-14 15:39:52.884090');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_priv`
--

DROP TABLE IF EXISTS `role_priv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_priv` (
  `role_id` bigint NOT NULL,
  `priv_id` bigint NOT NULL,
  KEY `FKik91xk99ju5473vmij7auesf9` (`priv_id`),
  KEY `FKbrkt9wy3wi314sii2asw7adqt` (`role_id`),
  CONSTRAINT `FKbrkt9wy3wi314sii2asw7adqt` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKik91xk99ju5473vmij7auesf9` FOREIGN KEY (`priv_id`) REFERENCES `privilege` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_priv`
--

LOCK TABLES `role_priv` WRITE;
/*!40000 ALTER TABLE `role_priv` DISABLE KEYS */;
INSERT INTO `role_priv` VALUES (28,1),(28,2),(28,3),(28,6),(28,11),(28,13),(28,14),(28,20),(28,25),(29,1),(29,2),(29,3),(29,6),(29,11),(29,12),(29,13),(29,15),(29,20),(29,25),(30,1),(30,2),(30,4),(30,7),(30,9),(30,11),(30,12),(30,13),(30,16),(30,18),(30,21),(30,23),(30,25),(31,1),(31,2),(31,5),(31,8),(31,9),(31,10),(31,11),(31,12),(31,13),(31,17),(31,19),(31,22),(31,24),(31,26),(31,27),(217,190),(217,191),(217,192),(217,195),(217,200),(217,202),(217,203),(217,209),(217,214),(218,190),(218,191),(218,192),(218,195),(218,200),(218,201),(218,202),(218,204),(218,209),(218,214),(219,190),(219,191),(219,193),(219,196),(219,198),(219,200),(219,201),(219,202),(219,205),(219,207),(219,210),(219,212),(219,214),(220,190),(220,191),(220,194),(220,197),(220,198),(220,199),(220,200),(220,201),(220,202),(220,206),(220,208),(220,211),(220,213),(220,215),(220,216);
/*!40000 ALTER TABLE `role_priv` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `step`
--

DROP TABLE IF EXISTS `step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `step` (
  `id` bigint NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `end` varchar(255) DEFAULT NULL,
  `end_hour` varchar(255) DEFAULT NULL,
  `start` varchar(255) DEFAULT NULL,
  `start_hour` varchar(255) DEFAULT NULL,
  `device` bigint NOT NULL,
  `step_type` bigint DEFAULT NULL,
  `user` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbjpgjvkmorq369n1wewp0b2ru` (`device`),
  KEY `FKfc9261cm96ps5woi4fvykh7c0` (`step_type`),
  KEY `FK8fmmi01oonsj8t8wi1ck080aj` (`user`),
  CONSTRAINT `FK8fmmi01oonsj8t8wi1ck080aj` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FKbjpgjvkmorq369n1wewp0b2ru` FOREIGN KEY (`device`) REFERENCES `device` (`id`),
  CONSTRAINT `FKfc9261cm96ps5woi4fvykh7c0` FOREIGN KEY (`step_type`) REFERENCES `step_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `step`
--

LOCK TABLES `step` WRITE;
/*!40000 ALTER TABLE `step` DISABLE KEYS */;
INSERT INTO `step` VALUES (135,'','2020-03-18','12:00','2020-03-18','11:00',114,NULL,32),(136,'','2020-03-17','18:00','2020-03-17','08:00',120,NULL,32),(137,'','2020-03-16','16:00','2020-03-16','14:00',114,NULL,35),(138,'','2020-03-15','18:00','2020-03-15','16:00',120,NULL,35),(139,'','2020-03-19','18:00','2020-03-19','13:00',122,NULL,36),(140,'','2020-04-10','18:00','2020-04-10','13:00',122,NULL,39),(141,'','2020-04-11','18:00','2020-04-11','13:00',122,NULL,39),(142,'','2020-04-12','18:00','2020-04-12','13:00',122,NULL,40),(143,'','2020-04-13','18:00','2020-04-13','13:00',122,NULL,40);
/*!40000 ALTER TABLE `step` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `step_type`
--

DROP TABLE IF EXISTS `step_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `step_type` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `continuity` bigint DEFAULT NULL,
  `device_type` bigint NOT NULL,
  `step_types` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9e1cejgv1grwkgouqir44i8lo` (`continuity`),
  KEY `FK1432pb0xq8sn6ifh1u59tknci` (`device_type`),
  KEY `FKowvtq8hvrlwieiqldknjkr760` (`step_types`),
  CONSTRAINT `FK1432pb0xq8sn6ifh1u59tknci` FOREIGN KEY (`device_type`) REFERENCES `device_type` (`id`),
  CONSTRAINT `FK9e1cejgv1grwkgouqir44i8lo` FOREIGN KEY (`continuity`) REFERENCES `continuity` (`id`),
  CONSTRAINT `FKowvtq8hvrlwieiqldknjkr760` FOREIGN KEY (`step_types`) REFERENCES `experiment_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `step_type`
--

LOCK TABLES `step_type` WRITE;
/*!40000 ALTER TABLE `step_type` DISABLE KEYS */;
INSERT INTO `step_type` VALUES (172,'Balance',166,101,184),(173,'Plate Compactor',167,107,184),(174,'Mixer',166,102,184),(175,'Gyratory',168,105,183),(176,'Autosaw',169,100,183),(177,'Caliper',166,103,183),(178,'SVM Setup',166,109,183),(179,'Vacuum Setup',170,111,183),(180,'Water Bath',166,112,183),(181,'Wheel Tracking Test',166,113,184),(182,'Plate Compactor',171,107,184);
/*!40000 ALTER TABLE `step_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (123,'Aggregates'),(124,'Bitumen'),(125,'Consumables'),(126,'Other');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `date_created` datetime(6) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `ua_number` varchar(255) DEFAULT NULL,
  `updated_on` datetime(6) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (32,'2020-04-14 15:39:30.217395','','Cedric','Plouvier','','PW','','20152267','2020-04-14 15:39:30.217395','Cedric'),(33,'2020-04-14 15:39:30.234412','','Ruben','Joosen','','PW','','20164473','2020-04-14 15:39:30.234412','Ruben'),(34,'2020-04-14 15:39:30.248426','','Jaimie','Vranckx','','PW','','20155797','2020-04-14 15:39:30.248426','Jaimie'),(35,'2020-04-14 15:39:30.258433','','Ali','Amir','','PW','','20163446','2020-04-14 15:39:30.258433','Ali'),(36,'2020-04-14 15:39:30.271446','','Timo','Nelen','','PW','','S0162117','2020-04-14 15:39:30.271446','Timo'),(37,'2020-04-14 15:39:30.282455','','Ondrej','Bures','','PW','','20160002','2020-04-14 15:39:30.282455','Ondrej'),(38,'2020-04-14 15:39:30.294465','','admin','admin','','admin','',NULL,'2020-04-14 15:39:30.294465','admin'),(39,'2020-04-14 15:39:30.307477','','Bach','Student','','PW','','20170001','2020-04-14 15:39:30.307477','Bachelor'),(40,'2020-04-14 15:39:30.318488','','Mas','Student','','PW','','20160009','2020-04-14 15:39:30.318488','Master'),(41,'2020-04-14 15:39:30.328496','','Researcher','Developper','','PW','','20100001','2020-04-14 15:39:30.328496','Researcher');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (39,28),(40,29),(41,30),(32,31),(33,31),(34,31),(35,31),(36,31),(37,31),(38,31);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-14 17:46:58
