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
-- Table structure for table `aprod_tag`
--

DROP TABLE IF EXISTS `aprod_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aprod_tag` (
  `aprod_id` bigint NOT NULL,
  `atag_id` bigint NOT NULL,
  KEY `FKjig076rjvteqmss4sarj4di2u` (`atag_id`),
  KEY `FKjnwj7yt4uoddhwaoln368o0t2` (`aprod_id`),
  CONSTRAINT `FKjig076rjvteqmss4sarj4di2u` FOREIGN KEY (`atag_id`) REFERENCES `own_tag` (`id`),
  CONSTRAINT `FKjnwj7yt4uoddhwaoln368o0t2` FOREIGN KEY (`aprod_id`) REFERENCES `own_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aprod_tag`
--

LOCK TABLES `aprod_tag` WRITE;
/*!40000 ALTER TABLE `aprod_tag` DISABLE KEYS */;
INSERT INTO `aprod_tag` VALUES (134,132),(135,130),(136,130),(137,132),(138,131),(139,133),(140,133),(141,133);
/*!40000 ALTER TABLE `aprod_tag` ENABLE KEYS */;
UNLOCK TABLES;

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
  KEY `FKbqqjioaaqcik8crkffmbkalfp` (`product_id`),
  CONSTRAINT `FKbqqjioaaqcik8crkffmbkalfp` FOREIGN KEY (`product_id`) REFERENCES `own_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `composition`
--

LOCK TABLES `composition` WRITE;
/*!40000 ALTER TABLE `composition` DISABLE KEYS */;
INSERT INTO `composition` VALUES (169,46.67,134),(170,10.1,135),(171,8.29,136),(172,23.89,137),(173,2.45,138),(174,8.6,139),(175,6.5,140),(176,20.72,134),(177,14.2,135),(178,21.24,136),(179,23.84,137),(180,12.91,138),(181,7.1,139),(182,6.5,140),(183,61.7,134),(184,11.2,135),(185,17.8,137),(186,9.3,138),(187,6.9,140);
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
  `direction_type` varchar(255) DEFAULT NULL,
  `hours` int DEFAULT NULL,
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
INSERT INTO `continuity` VALUES (191,'After',0,0,'No'),(192,'After',0,0,'No'),(193,'After',8,0,'Soft (at least)'),(194,'After',24,0,'Hard'),(195,'After',4,0,'Soft (at least)'),(196,'After',0,0,'Hard'),(197,'After',24,0,'Soft (at most)'),(211,'After',0,0,'No'),(213,'Before',4,0,'Hard'),(215,'Before',4,0,'Hard'),(217,'Before',4,0,'Hard'),(219,'After',24,0,'Hard'),(221,'After',12,0,'Soft (at least)'),(223,'After',0,0,'No'),(225,'After',0,0,'No'),(227,'After',0,0,'No'),(229,'After',0,0,'Hard'),(231,'After',0,0,'Hard'),(233,'After',0,0,'Hard');
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
  `comment` varchar(10000) DEFAULT NULL,
  `devicename` varchar(255) NOT NULL,
  `device_type` bigint DEFAULT NULL,
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
INSERT INTO `device` VALUES (115,NULL,'Autosaw 1',101),(116,NULL,'Autosaw 2',101),(117,NULL,'Uniframe 1',111),(118,NULL,'Vacuum Setup 1',112),(119,NULL,'Water Bath 1',113),(120,NULL,'Wheel Tracking Test 1',114),(121,'Perfect oven to bake a pizza in your spare times','Oven 1',107),(122,NULL,'Oven 2',107),(123,NULL,'Oven 3',107),(124,NULL,'Gyratory 1',106),(125,NULL,'Caliper 1',104),(126,NULL,'SVM Setup 1',110),(127,NULL,'Big mixer 1',103),(128,NULL,'Cooling chamber 1',105),(129,NULL,'Balance 1',102);
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
  `information` varchar(10000) DEFAULT NULL,
  `information_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_information`
--

LOCK TABLES `device_information` WRITE;
/*!40000 ALTER TABLE `device_information` DISABLE KEYS */;
INSERT INTO `device_information` VALUES (44,'elit dolore in duo litora explicari civibus sit vocibus semper conceptam cetero postulant commune molestie ligula nihil lorem omnesque repudiandae','Manual'),(45,'veniam evertitur nostrum persius ex dictum parturient sapientem intellegat hac agam tincidunt condimentum elaboraret regione vim congue mutat graecis autem','Maintenance'),(46,'delenit tibique integer viris referrentur magnis semper potenti voluptatum tempus velit delenit condimentum ornare faucibus porta maximus nascetur vivendo omnesque','Calibration'),(47,'intellegebat libris ad suspendisse qui corrumpit suscipit iaculis proin dolor parturient usu sed possim posse conubia turpis gravida persius equidem','Safety instruction card'),(48,'nostrum tota nonumy tractatos aperiri nibh evertitur neque interpretaris volumus etiam dolores dolorum senectus nullam mollis efficitur constituam platonem facilisi','Manual'),(49,'conubia dicam tantas non voluptaria facilis reque vocent corrumpit evertitur dolorum cetero verterem sociosqu mazim cursus invidunt saepe mutat sociis','Maintenance'),(50,'leo nunc id saperet reprehendunt ceteros quod sodales ludus erat lacinia massa tractatos cum urbanitas has minim sociis sapientem id','Calibration'),(51,'viverra vix vituperatoribus tibique platea consectetur posidonium moderatius imperdiet vituperata dissentiunt massa platonem verterem elementum amet posidonium percipit adipiscing quaestio','Safety instruction card'),(52,'vim graeco nisl option ex fames quis utinam non intellegat efficitur proin habeo voluptatum enim salutatus pharetra eripuit agam nostrum','Manual'),(53,'aliquet platonem reformidans integer suscipit causae utamur ferri convallis postulant natoque definitionem sociosqu commodo necessitatibus te pretium hac ipsum ullamcorper','Maintenance'),(54,'ei inimicus rutrum libris tempor tempor quaeque sociosqu tamquam movet epicuri auctor litora elaboraret ceteros nominavi ludus luptatum velit metus','Calibration'),(55,'torquent similique deserunt accumsan lobortis prompta maluisset vulputate affert vitae disputationi quam platea interpretaris litora maluisset impetus sapien affert ut','Safety instruction card'),(56,'risus nullam vel nostrum hac pellentesque omittam molestie elitr dicit possim doctus quaerendum aliquet mazim splendide aptent maiestatis reprimique ubique','Manual'),(57,'brute sonet epicurei tantas cetero ludus ultrices magna te accumsan delectus volutpat mazim adipiscing neglegentur per dolorem pri eos dolor','Maintenance'),(58,'curabitur commodo sociosqu novum error dui gravida mazim tota pertinax suscipit porro purus accusata posuere noluisse vivendo reprehendunt no saperet','Calibration'),(59,'ceteros sapientem dictumst ex ligula ante augue sapientem libero noster metus alia graeco tota tale sale mediocritatem wisi pertinacia lorem','Safety instruction card'),(60,'aenean dui dicunt orci nunc indoctum fames verterem cubilia purus repudiandae molestiae quas pro suscipiantur rutrum elit penatibus erat maluisset','Manual'),(61,'dissentiunt errem persius cubilia harum viris fabellas venenatis brute pellentesque oporteat quod dis porro cu melius minim moderatius option ne','Maintenance'),(62,'sea cras nisl in vis ea fugit unum partiendo an posuere veniam parturient inani quem melius referrentur periculis cetero appetere','Calibration'),(63,'maiestatis maluisset massa epicuri ferri splendide semper similique natum senserit eros commodo accommodare moderatius corrumpit interesset pri metus aliquam assueverit','Safety instruction card'),(64,'qualisque pericula aliquip invidunt lacinia etiam quam tamquam egestas prompta voluptatibus augue ignota ei reprimique ignota volutpat porttitor aliquid quis','Manual'),(65,'habeo unum audire odio ferri sapientem fuisset viris necessitatibus altera a venenatis feugiat ancillae nostrum quaerendum fusce equidem sit tantas','Maintenance'),(66,'pertinacia dictum idque nihil nobis cubilia lacus lacus diam potenti nullam nec vocibus legere molestiae utinam referrentur assueverit neglegentur parturient','Calibration'),(67,'veritus mutat aliquid libero porro alterum libero noluisse animal tincidunt eripuit animal harum non quam habitasse qui quam habemus montes','Safety instruction card'),(68,'inimicus nobis commune iisque elitr expetendis est eleifend dicunt mi ridens tritani urna laudem eros eu constituto ornare expetenda postulant','Manual'),(69,'veri movet definiebas ornatus nostra pulvinar eros feugait porttitor luctus vivamus pharetra brute commune inceptos dapibus auctor ornare iaculis fringilla','Maintenance'),(70,'natoque ac quisque oporteat mutat primis mediocritatem elitr omittantur eos movet proin ludus sententiae vituperatoribus offendit dissentiunt ullamcorper veritus signiferumque','Calibration'),(71,'volutpat dolor montes voluptaria ligula populo offendit rhoncus idque oporteat falli accumsan ne justo verterem tacimates dicant nostra graece habitant','Safety instruction card'),(72,'ornare eget signiferumque felis mazim epicuri cursus aliquip tibique postea','Other'),(73,'est expetenda periculis vis causae referrentur electram iriure posidonium honestatis vero hendrerit lacus malorum fermentum nonumes elaboraret epicurei quaerendum enim','Manual'),(74,'ad convenire morbi platonem prodesset euismod finibus efficiantur interpretaris signiferumque ignota tempor diam populo similique posidonium ornatus putent an definitionem','Maintenance'),(75,'principes commodo nisi ceteros animal propriae luptatum utroque omittam saperet utamur quot feugiat fastidii an doming sententiae elit erroribus aenean','Calibration'),(76,'non tractatos imperdiet fuisset verterem mauris cum te pharetra epicurei constituam libris ponderum dolor nisl mel erroribus nisi inani ornatus','Safety instruction card'),(77,'fugit postulant nullam homero iuvaret legimus volumus melius fermentum suspendisse accommodare nostra utamur eum viverra moderatius euismod donec himenaeos scripta','Manual'),(78,'vim saepe unum viverra tempor ius periculis euismod nec consetetur sed quem scripserit possim eloquentiam ignota conceptam hinc feugiat suspendisse','Maintenance'),(79,'efficiantur alienum deseruisse vocibus vocibus ocurreret pericula hendrerit ornatus morbi adolescens voluptaria iisque aeque justo reque ante consectetur feugiat error','Calibration'),(80,'luptatum elementum docendi libris vim salutatus prompta mea metus libris finibus a mi fabulas nec nisi disputationi discere te conclusionemque','Safety instruction card'),(81,'harum cras penatibus oratio option utamur ignota mediocrem proin splendide noster iriure prodesset has sapientem unum prompta aeque scripta meliore','Manual'),(82,'cras sed sem duo quam mentitum justo explicari altera libris regione iusto prompta condimentum integer conceptam rhoncus gravida constituto senectus','Maintenance'),(83,'dolorum urna quem quas instructior neque mentitum meliore labores semper ex quot contentiones tation prompta repudiandae quot vituperatoribus explicari nascetur','Calibration'),(84,'affert nominavi nostrum tincidunt quo efficitur massa molestie deseruisse nunc labores ludus oporteat vis est reformidans adolescens erroribus tempor ea','Safety instruction card'),(85,'fermentum viverra mi aliquet ocurreret vituperatoribus dolore quidam aperiri lorem ponderum metus deseruisse veritus eu varius nonumes oporteat definitionem graece','Manual'),(86,'purus dicunt oratio eros quod nascetur agam persequeris nisi eius fugit ius suscipiantur elaboraret fastidii homero mus comprehensam postea vivamus','Maintenance'),(87,'curabitur adipisci docendi eam ac delicata rutrum expetenda viris facilis curae alia causae nunc necessitatibus fusce fastidii deseruisse pericula repudiandae','Calibration'),(88,'proin curae vivamus fabulas tamquam putent donec ante conclusionemque lobortis pretium enim hendrerit convallis saperet ligula partiendo qualisque inciderint lorem','Safety instruction card'),(89,'sollicitudin omnesque cum verear sodales mauris tamquam reprehendunt nisl posuere causae vehicula primis diam prompta putent gubergren eleifend iusto parturient','Manual'),(90,'interdum consectetur propriae sociosqu omnesque arcu eirmod aeque vel vivamus posidonium cursus viverra ponderum tantas quaerendum fugit rutrum intellegat penatibus','Maintenance'),(91,'aenean utroque epicuri mattis consequat oratio causae non salutatus definitionem vituperatoribus facilis comprehensam maecenas fastidii oratio pericula tristique senserit vel','Calibration'),(92,'ponderum ei graeco mutat eos lacinia accusata saepe facilis dicam nostra graece oporteat natum scelerisque qui euripidis tation porro lobortis','Safety instruction card'),(93,'maluisset vim id persequeris expetendis quisque aliquip tractatos sit facilis fames scripserit omnesque habemus theophrastus sale voluptatum persecuti ornare inceptos','Manual'),(94,'posse habitant auctor laoreet eam cubilia duo constituto tantas eu malorum pretium viderer consectetur solum regione idque efficitur eos dicta','Maintenance'),(95,'ultricies error convenire fusce dis platonem pellentesque natoque nonumy penatibus feugait minim ea arcu placerat his his rutrum splendide definitionem','Calibration'),(96,'qui epicuri dui liber an liber posuere porta natum potenti non altera cu disputationi fames inciderint amet detraxit aenean mediocritatem','Safety instruction card'),(97,'nostra porro posidonium nam gubergren sagittis accusata sale invenire rutrum verear viris mattis at pertinax tincidunt ultrices condimentum vitae iriure','Manual'),(98,'maluisset utinam ne posidonium accumsan feugiat impetus fusce interpretaris cubilia lorem eleifend intellegat bibendum fastidii convenire fabulas etiam tellus ex','Maintenance'),(99,'instructior nulla hendrerit viverra urbanitas patrioque alia sententiae dolores verterem in quas appareat vero meliore suscipiantur atomorum atomorum eam antiopam','Calibration'),(100,'torquent nostra brute suspendisse aliquam senserit vivendo eget vis pharetra sententiae sem eirmod necessitatibus possim accommodare alia primis eirmod nec','Safety instruction card');
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
INSERT INTO `device_information_files` VALUES (68,'placeholder.pdf'),(70,'placeholder.xlsx'),(70,'placeholder.jpg');
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
INSERT INTO `device_type` VALUES (101,'#FF00FF',NULL,'Autosaw',_binary ''),(102,'#FFAFAF',NULL,'Balance',_binary '\0'),(103,'#00FF00',NULL,'Big Mixer',_binary ''),(104,'#000000',NULL,'Caliper',_binary ''),(105,'#FFFF00',NULL,'Cooling chamber',_binary ''),(106,'#00FFFF',NULL,'Gyratory',_binary '\0'),(107,'#404040','Oven.jpg','Oven',_binary ''),(108,'#FF0000',NULL,'Plate Compactor',_binary '\0'),(109,'#FFC800',NULL,'Small Mixer',_binary ''),(110,'#808080',NULL,'SVM Setup',_binary '\0'),(111,'#FFFFFF',NULL,'Uniframe',_binary '\0'),(112,'#0000FF',NULL,'Vacuum Setup',_binary ''),(113,'#222222',NULL,'Water Bath',_binary '\0'),(114,'#C0C0C0',NULL,'Wheel Tracking Test',_binary '');
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
INSERT INTO `device_type_information` VALUES (101,44),(101,45),(101,46),(101,47),(102,48),(102,49),(102,50),(102,51),(103,52),(103,53),(103,54),(103,55),(104,56),(104,57),(104,58),(104,59),(105,60),(105,61),(105,62),(105,63),(106,64),(106,65),(106,66),(106,67),(107,68),(107,69),(107,70),(107,71),(107,72),(108,73),(108,74),(108,75),(108,76),(109,77),(109,78),(109,79),(109,80),(110,81),(110,82),(110,83),(110,84),(111,85),(111,86),(111,87),(111,88),(112,89),(112,90),(112,91),(112,92),(113,93),(113,94),(113,95),(113,96),(114,97),(114,98),(114,99),(114,100);
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
  `start_date` varchar(255) DEFAULT NULL,
  `experiment_type` bigint DEFAULT NULL,
  `user` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nq75g55ruqg0n08lyc1fypusb` (`experimentname`),
  KEY `FKs579bbur70ivx5qq54w15vdrt` (`experiment_type`),
  KEY `FKfx471e4koh0yk2ljfqy89x51b` (`user`),
  CONSTRAINT `FKfx471e4koh0yk2ljfqy89x51b` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FKs579bbur70ivx5qq54w15vdrt` FOREIGN KEY (`experiment_type`) REFERENCES `experiment_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `experiment`
--

LOCK TABLES `experiment` WRITE;
/*!40000 ALTER TABLE `experiment` DISABLE KEYS */;
INSERT INTO `experiment` VALUES (275,'2020-05-08','experiment1','2020-05-04',209,40),(276,'2020-05-08','experiment2','2020-05-04',209,40),(277,'2020-05-08','experiment3','2020-05-04',209,40),(278,'2020-05-18','experiment4','2020-05-11',235,40);
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
  `is_fixed_type` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lh0y7nv05g20guqjmolnxqibo` (`expname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `experiment_type`
--

LOCK TABLES `experiment_type` WRITE;
/*!40000 ALTER TABLE `experiment_type` DISABLE KEYS */;
INSERT INTO `experiment_type` VALUES (209,'ITSR',_binary ''),(210,'Wheel Tracking Test',_binary ''),(235,'ITSR (Difficult)',_binary '');
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
INSERT INTO `hibernate_sequence` VALUES (281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281),(281);
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
INSERT INTO `mix_comp` VALUES (188,169),(188,170),(188,171),(188,172),(188,173),(188,174),(188,175),(189,176),(189,177),(189,178),(189,179),(189,180),(189,181),(189,182),(190,183),(190,184),(190,185),(190,186),(190,187);
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
  KEY `FK4biifw751e4029g6y1kqct5ns` (`tag_id`),
  KEY `FK1aluqforr05pfrvwfe1jkucbf` (`prod_id`),
  CONSTRAINT `FK1aluqforr05pfrvwfe1jkucbf` FOREIGN KEY (`prod_id`) REFERENCES `mixture` (`id`),
  CONSTRAINT `FK4biifw751e4029g6y1kqct5ns` FOREIGN KEY (`tag_id`) REFERENCES `own_tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mix_tag`
--

LOCK TABLES `mix_tag` WRITE;
/*!40000 ALTER TABLE `mix_tag` DISABLE KEYS */;
INSERT INTO `mix_tag` VALUES (188,131),(189,130),(190,133);
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
  `description` varchar(10000) DEFAULT NULL,
  `document` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mixture`
--

LOCK TABLES `mixture` WRITE;
/*!40000 ALTER TABLE `mixture` DISABLE KEYS */;
INSERT INTO `mixture` VALUES (188,'risus euripidis maluisset elaboraret populo viverra veniam ignota blandit suas fames signiferumque quaeque partiendo gloriatur sit posuere vivamus diam nonumes',NULL,NULL,'APT-C'),(189,'nihil persequeris alienum turpis aliquid sagittis definitiones nam periculis dicam feugait legimus metus melius volumus patrioque vero decore nominavi mutat',NULL,NULL,'AB-4C'),(190,'aeque deserunt antiopam suavitate interesset constituam nam veritus class ridiculus velit nam expetenda solum suavitate aeque orci rhoncus his ius',NULL,NULL,'SMA');
/*!40000 ALTER TABLE `mixture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `office_hours`
--

DROP TABLE IF EXISTS `office_hours`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `office_hours` (
  `id` bigint NOT NULL,
  `end_hour` int DEFAULT NULL,
  `end_minute` int DEFAULT NULL,
  `start_hour` int DEFAULT NULL,
  `start_minute` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `office_hours`
--

LOCK TABLES `office_hours` WRITE;
/*!40000 ALTER TABLE `office_hours` DISABLE KEYS */;
INSERT INTO `office_hours` VALUES (279,17,0,9,0);
/*!40000 ALTER TABLE `office_hours` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `own_product`
--

DROP TABLE IF EXISTS `own_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `own_product` (
  `id` bigint NOT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `description` varchar(10000) DEFAULT NULL,
  `document` varchar(255) DEFAULT NULL,
  `image_name` varchar(255) DEFAULT NULL,
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
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `own_product`
--

LOCK TABLES `own_product` WRITE;
/*!40000 ALTER TABLE `own_product` DISABLE KEYS */;
INSERT INTO `own_product` VALUES (134,'2020-05-16 13:17:47.952643','auctor honestatis dolore eleifend brute convallis erat tale intellegebat fermentum sumo decore sanctus graeci menandri voluptatum auctor nibh mel aptent',NULL,NULL,5,200,'Gebroken Porfier 6,3/10',5,'volutpat libris referrentur commune mandamus nam singulis fabellas',1,2000,1,0,'2020-05-16 13:17:47.952643','https://nl.wikipedia.org/wiki/Porfier'),(135,'2020-05-16 13:17:47.969658','in tristique porta nobis dicta percipit aliquip prodesset consul porta iisque simul euripidis interpretaris splendide sem eam dolorem deseruisse detracto',NULL,NULL,5,200,'Gebroken Porfier 4/6,3',5,'possit simul a est dis melius instructior volumus',1,2000,1,0,'2020-05-16 13:17:47.969658','https://nl.wikipedia.org/wiki/Porfier'),(136,'2020-05-16 13:17:47.982670','justo feugait nihil similique cu repudiare dolorum has litora euripidis scripserit nonumy periculis felis dictum et amet legimus animal ubique',NULL,NULL,5,200,'Gebroken Profier 2/4',5,'phasellus alienum non reprehendunt legere ludus quot gubergren',1,2000,1,0,'2020-05-16 13:17:47.982670','https://nl.wikipedia.org/wiki/Porfier'),(137,'2020-05-16 13:17:47.993680','porro aeque urna gloriatur tempus sapien integer menandri quas blandit agam mus aliquam adversarium graeci discere fusce maximus quas fastidii',NULL,NULL,5,200,'Gewassen Kalksteen 0/2',5,'sem condimentum luptatum iudicabit iriure ignota consectetuer felis',1,2000,1,0,'2020-05-16 13:17:47.993680','https://nl.wikipedia.org/wiki/Kalksteen'),(138,'2020-05-16 13:17:48.004689','dictum finibus sea turpis scelerisque vitae antiopam viverra blandit facilisis aliquam nascetur est efficitur pericula vidisse commodo sollicitudin fuisset est',NULL,NULL,5,100,'Alzagri Rond Zand 0/1',5,'scripserit atqui malesuada ultrices tempor maluisset solet montes',1,1000,1,0,'2020-05-16 13:17:48.004689','https://nl.wikipedia.org/wiki/Zand'),(139,'2020-05-16 13:17:48.016700','no alienum salutatus mediocrem pertinax vim tincidunt ante inciderint volutpat quot mandamus class suspendisse habeo nisi aptent doctus suscipit dicat',NULL,NULL,5,100,'Vulstof duras 2a',5,'ante atqui senectus facilisis eu tale reformidans option',1,500,1,0,'2020-05-16 13:17:48.016700','https://nl.wikipedia.org/wiki/Duras'),(140,'2020-05-16 13:17:48.026710','solum sollicitudin sociosqu petentium vero sem consectetuer velit qualisque elit meliore tempus harum solet viderer deseruisse urbanitas unum doctus dolorum',NULL,NULL,5,25,'Bitumen op aggregaten',5,'consetetur nobis mandamus explicari adhuc atomorum tellus platea',1,200,1,2,'2020-05-16 13:17:48.026710','https://nl.wikipedia.org/wiki/Bitumen'),(141,'2020-05-16 13:17:48.037721','elitr invidunt mentitum option vehicula epicuri persequeris putent pro imperdiet dicant ponderum quis intellegebat suspendisse pertinax quod dapibus saepe voluptatibus',NULL,NULL,5,1,'placeholder8',5,'gubergren dicam verterem unum porttitor eirmod id torquent',1,90,1,1,'2020-05-16 13:17:48.037721',NULL);
/*!40000 ALTER TABLE `own_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `own_tag`
--

DROP TABLE IF EXISTS `own_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `own_tag` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `own_tag`
--

LOCK TABLES `own_tag` WRITE;
/*!40000 ALTER TABLE `own_tag` DISABLE KEYS */;
INSERT INTO `own_tag` VALUES (130,'Aggregates'),(131,'Bitumen'),(132,'Consumables'),(133,'Other');
/*!40000 ALTER TABLE `own_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `piece_of_mixture`
--

DROP TABLE IF EXISTS `piece_of_mixture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `piece_of_mixture` (
  `id` bigint NOT NULL,
  `mixture_amount` double DEFAULT NULL,
  `mixture_comment` varchar(10000) DEFAULT NULL,
  `mixture` bigint DEFAULT NULL,
  `pieces_of_mixture` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1x55m4wpvdqeyeq2xd12qxfwi` (`mixture`),
  KEY `FK2al6m40tjs05ejd0prq1xrkn0` (`pieces_of_mixture`),
  CONSTRAINT `FK1x55m4wpvdqeyeq2xd12qxfwi` FOREIGN KEY (`mixture`) REFERENCES `mixture` (`id`),
  CONSTRAINT `FK2al6m40tjs05ejd0prq1xrkn0` FOREIGN KEY (`pieces_of_mixture`) REFERENCES `experiment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `piece_of_mixture`
--

LOCK TABLES `piece_of_mixture` WRITE;
/*!40000 ALTER TABLE `piece_of_mixture` DISABLE KEYS */;
INSERT INTO `piece_of_mixture` VALUES (271,6.6,'comment 1',188,275),(272,6.6,'comment 2',189,276),(273,6.6,'comment 3',190,277),(274,6.6,'comment 4',190,277);
/*!40000 ALTER TABLE `piece_of_mixture` ENABLE KEYS */;
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
INSERT INTO `privilege` VALUES (1,NULL,'logon'),(2,NULL,'Planning - Book step/experiment'),(3,NULL,'Planning - Delete step/experiment own'),(4,NULL,'Planning - Delete step/experiment own/promotor'),(5,NULL,'Planning - Delete step/experiment all'),(6,NULL,'Planning - Adjust step/experiment own'),(7,NULL,'Planning - Adjust step/experiment own/promotor'),(8,NULL,'Planning - Adjust step/experiment all'),(9,NULL,'Planning - Make new step'),(10,NULL,'Planning - Make new experiment'),(11,NULL,'Planning - Start within office hours'),(12,NULL,'Planning - Start outside office hours'),(13,NULL,'Planning - Overview'),(14,NULL,'Stock - Aggregates + Bitumen Read only - Basic'),(15,NULL,'Stock - Aggregates + Bitumen Read only - Advanced'),(16,NULL,'Stock - Aggregates + Bitumen Modify - Advanced'),(17,NULL,'Stock - Modify - All'),(18,NULL,'Stock - Consumables + Other Read only - Advanced'),(19,NULL,'User Management'),(20,NULL,'Database - Read only - Basic'),(21,NULL,'Database - Modify - Advanced'),(22,NULL,'Database - Modify - All'),(23,NULL,'Reports - Modify - Advanced'),(24,NULL,'Reports - Modify - All'),(25,NULL,'Device - Read only - Basic'),(26,NULL,'Device - Modify - All'),(27,NULL,'Console Access'),(28,NULL,'Statistics Access');
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
INSERT INTO `relation` VALUES (43,'testrelation',42);
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
INSERT INTO `relation_students` VALUES (43,40),(43,41);
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
  `description` varchar(10000) DEFAULT NULL,
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
INSERT INTO `report` VALUES (236,'per utamur habemus dignissim pharetra quem altera laudem facilis gubergren scripserit sollicitudin dicat sapien definiebas te dico sagittis convenire arcu libris salutatus qualisque elementum recteque','Autosaw is broken',37),(237,'porro nonumy quo varius lectus ceteros nascetur idque lacus potenti','Fancy Title',36),(238,'semper mucius fringilla appareat inceptos persecuti docendi euripidis quot euismod dico luctus neque persequeris quis atqui sale lectus vivamus postulant','Sand mixture is not in stock',34),(239,'eleifend at petentium viris feugait interpretaris ei senserit dolorem deterruisset civibus viverra metus elitr reque eleifend morbi bibendum hinc dicunt','Lab was closed yesterday',41),(240,'iisque invenire salutatus platonem consectetuer vituperatoribus nisi dolor noluisse recteque ligula no sollicitudin felis nam regione ridens nonumes mediocrem nunc discere appetere suavitate vel quo','Placeholder title',42);
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
INSERT INTO `role` VALUES (29,'2020-05-16 13:17:46.220067','Bachelorstudent','2020-05-16 13:17:46.220067'),(30,'2020-05-16 13:17:46.239085','Masterstudent','2020-05-16 13:17:46.239085'),(31,'2020-05-16 13:17:46.253098','Researcher','2020-05-16 13:17:46.253098'),(32,'2020-05-16 13:17:46.268111','Administrator','2020-05-16 13:17:46.268111');
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
INSERT INTO `role_priv` VALUES (29,1),(29,2),(29,3),(29,6),(29,11),(29,13),(29,14),(29,20),(29,25),(30,1),(30,2),(30,3),(30,6),(30,11),(30,12),(30,13),(30,15),(30,20),(30,25),(31,1),(31,2),(31,4),(31,7),(31,9),(31,11),(31,12),(31,13),(31,16),(31,18),(31,21),(31,23),(31,25),(32,1),(32,2),(32,5),(32,8),(32,9),(32,10),(32,11),(32,12),(32,13),(32,17),(32,19),(32,22),(32,24),(32,26),(32,27),(32,28);
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
  `amount` double DEFAULT NULL,
  `comment` varchar(10000) DEFAULT NULL,
  `end` varchar(255) DEFAULT NULL,
  `end_hour` varchar(255) DEFAULT NULL,
  `start` varchar(255) DEFAULT NULL,
  `start_hour` varchar(255) DEFAULT NULL,
  `device` bigint NOT NULL,
  `mixture` bigint DEFAULT NULL,
  `step_type` bigint DEFAULT NULL,
  `user` bigint DEFAULT NULL,
  `steps` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbjpgjvkmorq369n1wewp0b2ru` (`device`),
  KEY `FKbaniedwf24b10x6561gt03cl9` (`mixture`),
  KEY `FKfc9261cm96ps5woi4fvykh7c0` (`step_type`),
  KEY `FK8fmmi01oonsj8t8wi1ck080aj` (`user`),
  KEY `FKeisgfur2ym0p809svrfxxyjkr` (`steps`),
  CONSTRAINT `FK8fmmi01oonsj8t8wi1ck080aj` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FKbaniedwf24b10x6561gt03cl9` FOREIGN KEY (`mixture`) REFERENCES `mixture` (`id`),
  CONSTRAINT `FKbjpgjvkmorq369n1wewp0b2ru` FOREIGN KEY (`device`) REFERENCES `device` (`id`),
  CONSTRAINT `FKeisgfur2ym0p809svrfxxyjkr` FOREIGN KEY (`steps`) REFERENCES `experiment` (`id`),
  CONSTRAINT `FKfc9261cm96ps5woi4fvykh7c0` FOREIGN KEY (`step_type`) REFERENCES `step_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `step`
--

LOCK TABLES `step` WRITE;
/*!40000 ALTER TABLE `step` DISABLE KEYS */;
INSERT INTO `step` VALUES (142,0,'','2020-03-18','12:00','2020-03-18','11:00',115,NULL,NULL,33,NULL),(143,0,'','2020-03-17','18:00','2020-03-17','08:00',121,NULL,NULL,33,NULL),(144,0,'','2020-03-16','16:00','2020-03-16','14:00',115,NULL,NULL,36,NULL),(145,0,'','2020-03-15','18:00','2020-03-15','16:00',121,NULL,NULL,36,NULL),(146,0,'','2020-03-19','18:00','2020-03-19','13:00',123,NULL,NULL,37,NULL),(147,0,'','2020-04-10','18:00','2020-04-10','13:00',123,NULL,NULL,40,NULL),(148,0,'','2020-04-11','18:00','2020-04-11','13:00',123,NULL,NULL,40,NULL),(149,0,'','2020-04-12','18:00','2020-04-12','13:00',123,NULL,NULL,41,NULL),(150,0,'','2020-04-13','18:00','2020-04-13','13:00',123,NULL,NULL,41,NULL),(151,0,'','2020-05-18','13:00','2020-05-18','11:00',115,NULL,NULL,33,NULL),(152,0,'','2020-06-17','19:00','2020-06-17','08:00',115,NULL,NULL,33,NULL),(153,0,'','2020-07-16','16:00','2020-07-16','11:00',115,NULL,NULL,36,NULL),(154,0,'','2020-08-15','18:00','2020-08-15','12:00',115,NULL,NULL,36,NULL),(155,0,'','2020-09-19','18:00','2020-09-19','14:00',115,NULL,NULL,37,NULL),(156,0,'','2020-10-16','16:00','2020-10-16','11:00',115,NULL,NULL,36,NULL),(157,0,'','2020-11-15','18:00','2020-11-15','12:00',115,NULL,NULL,36,NULL),(158,0,'','2020-12-19','18:00','2020-12-19','14:00',115,NULL,NULL,37,NULL),(159,0,'','2020-04-10','18:00','2020-04-10','09:00',116,NULL,NULL,40,NULL),(160,0,'','2020-04-11','20:00','2020-04-11','13:00',117,NULL,NULL,40,NULL),(161,0,'','2020-04-12','15:00','2020-04-12','13:00',118,NULL,NULL,41,NULL),(162,0,'','2020-04-13','18:00','2020-04-13','11:00',119,NULL,NULL,41,NULL),(163,0,'','2019-05-18','20:00','2019-05-18','10:00',116,NULL,NULL,33,NULL),(164,0,'','2020-01-15','18:00','2020-01-13','10:00',122,NULL,NULL,33,NULL),(165,0,'','2020-03-15','18:00','2020-02-13','10:00',122,NULL,NULL,33,NULL),(166,0,'','2020-06-15','18:00','2020-04-13','10:00',122,NULL,NULL,33,NULL),(167,0,'','2020-12-15','18:00','2020-09-13','10:00',122,NULL,NULL,33,NULL),(168,0,'','2021-12-30','20:00','2021-01-01','10:00',116,NULL,NULL,33,NULL),(241,0,'','2020-05-04','12:00','2020-05-04','11:00',124,NULL,NULL,40,275),(242,0,'','2020-05-05','13:00','2020-05-05','12:00',115,NULL,NULL,40,275),(243,0,'','2020-05-06','12:00','2020-05-06','11:00',125,NULL,NULL,40,275),(244,0,'','2020-05-07','10:00','2020-05-07','09:00',126,NULL,NULL,40,275),(245,0,'','2020-05-08','11:00','2020-05-08','10:00',118,NULL,NULL,40,275),(246,0,'','2020-05-08','12:00','2020-05-08','11:00',119,NULL,NULL,40,275),(247,0,'','2020-05-04','14:00','2020-05-04','13:00',124,NULL,NULL,40,276),(248,0,'','2020-05-05','15:00','2020-05-05','14:00',115,NULL,NULL,40,276),(249,0,'','2020-05-06','14:00','2020-05-06','13:00',125,NULL,NULL,40,276),(250,0,'','2020-05-07','12:00','2020-05-07','11:00',126,NULL,NULL,40,276),(251,0,'','2020-05-08','13:00','2020-05-08','12:00',118,NULL,NULL,40,276),(252,0,'','2020-05-08','14:00','2020-05-08','13:00',119,NULL,NULL,40,276),(253,0,'','2020-05-04','15:00','2020-05-04','14:00',124,NULL,NULL,40,277),(254,0,'','2020-05-05','16:00','2020-05-05','15:00',115,NULL,NULL,40,277),(255,0,'','2020-05-06','16:00','2020-05-06','15:00',125,NULL,NULL,40,277),(256,0,'','2020-05-07','15:00','2020-05-07','14:00',126,NULL,NULL,40,277),(257,0,'','2020-05-08','15:00','2020-05-08','14:00',118,NULL,NULL,40,277),(258,0,'','2020-05-08','16:00','2020-05-08','15:00',119,NULL,NULL,40,277),(259,0,'','2020-05-11','11:00','2020-05-11','10:00',129,NULL,NULL,40,278),(260,0,'','2020-05-12','13:00','2020-05-11','11:00',121,NULL,NULL,40,278),(261,0,'','2020-05-12','13:00','2020-05-12','09:00',127,NULL,NULL,40,278),(262,0,'','2020-05-12','13:00','2020-05-12','09:00',129,NULL,NULL,40,278),(263,0,'','2020-05-12','13:00','2020-05-12','09:00',124,NULL,NULL,40,278),(264,0,'','2020-05-13','15:00','2020-05-13','13:00',115,NULL,NULL,40,278),(265,0,'','2020-05-14','11:00','2020-05-14','10:00',125,NULL,NULL,40,278),(266,0,'','2020-05-14','12:00','2020-05-14','11:00',126,NULL,NULL,40,278),(267,0,'','2020-05-15','13:00','2020-05-15','12:00',118,NULL,NULL,40,278),(268,0,'','2020-05-18','11:00','2020-05-15','13:00',119,NULL,NULL,40,278),(269,0,'','2020-05-18','15:00','2020-05-18','11:00',128,NULL,NULL,40,278),(270,0,'','2020-05-18','16:00','2020-05-18','15:00',117,NULL,NULL,40,278);
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
  `fixed_time_hours` int DEFAULT NULL,
  `fixed_time_minutes` int DEFAULT NULL,
  `fixed_time_type` varchar(255) DEFAULT NULL,
  `has_fixed_length` bit(1) DEFAULT NULL,
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
INSERT INTO `step_type` VALUES (198,1,0,'No',_binary '\0','Balance',192,102,210),(199,1,0,'No',_binary '\0','Plate Compactor',193,108,210),(200,1,0,'No',_binary '\0','Mixer',192,103,210),(201,1,0,'No',_binary '\0','Gyratory',194,106,209),(202,1,0,'No',_binary '\0','Autosaw',195,101,209),(203,1,0,'No',_binary '\0','Caliper',192,104,209),(204,1,0,'No',_binary '\0','SVM Setup',192,110,209),(205,1,0,'No',_binary '\0','Vacuum Setup',196,112,209),(206,1,0,'No',_binary '\0','Water Bath',192,113,209),(207,1,0,'No',_binary '\0','Wheel Tracking Test',192,114,210),(208,1,0,'No',_binary '\0','Plate Compactor',197,108,210),(212,1,0,'No',_binary '\0','StepBalance',211,102,235),(214,12,0,'At least',_binary '','StepOven',213,107,235),(216,4,0,'Equal',_binary '','StepBig Mixer',215,103,235),(218,4,0,'Equal',_binary '','StepBalance',217,102,235),(220,4,0,'Equal',_binary '','StepGyratory',219,106,235),(222,2,0,'Equal',_binary '','StepAutosaw',221,101,235),(224,1,0,'Equal',_binary '','StepCaliper',223,104,235),(226,1,0,'Equal',_binary '','StepSVM Setup',225,110,235),(228,1,0,'Equal',_binary '','StepVacuum Setup',227,112,235),(230,70,0,'Equal',_binary '','StepWater Bath',229,113,235),(232,4,0,'Equal',_binary '','StepCooling chamber',231,105,235),(234,1,0,'Equal',_binary '','StepUniframe',233,111,235);
/*!40000 ALTER TABLE `step_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stocklvl`
--

DROP TABLE IF EXISTS `stocklvl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stocklvl` (
  `own_product_id` bigint NOT NULL,
  `product_stock_history` double DEFAULT NULL,
  `product_stock_history_key` varchar(255) NOT NULL,
  PRIMARY KEY (`own_product_id`,`product_stock_history_key`),
  CONSTRAINT `FKl8t8hwpr2jr9flrbv13oyt88v` FOREIGN KEY (`own_product_id`) REFERENCES `own_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stocklvl`
--

LOCK TABLES `stocklvl` WRITE;
/*!40000 ALTER TABLE `stocklvl` DISABLE KEYS */;
INSERT INTO `stocklvl` VALUES (134,500,'2019-11'),(134,220,'2019-12'),(134,100,'2020-01'),(134,200,'2020-02'),(134,100,'2020-03'),(134,250,'2020-04'),(134,2000,'2020-05'),(135,1000,'2019-11'),(135,200,'2019-12'),(135,600,'2020-01'),(135,500,'2020-02'),(135,550,'2020-03'),(135,200,'2020-04'),(135,2000,'2020-05'),(136,500,'2019-11'),(136,220,'2019-12'),(136,100,'2020-01'),(136,200,'2020-02'),(136,100,'2020-03'),(136,250,'2020-04'),(136,2000,'2020-05'),(137,500,'2019-11'),(137,220,'2019-12'),(137,100,'2020-01'),(137,200,'2020-02'),(137,100,'2020-03'),(137,250,'2020-04'),(137,2000,'2020-05'),(138,500,'2019-11'),(138,220,'2019-12'),(138,100,'2020-01'),(138,200,'2020-02'),(138,100,'2020-03'),(138,250,'2020-04'),(138,1000,'2020-05'),(139,500,'2019-11'),(139,220,'2019-12'),(139,100,'2020-01'),(139,200,'2020-02'),(139,100,'2020-03'),(139,250,'2020-04'),(139,500,'2020-05'),(140,500,'2019-11'),(140,220,'2019-12'),(140,100,'2020-01'),(140,200,'2020-02'),(140,100,'2020-03'),(140,250,'2020-04'),(140,200,'2020-05'),(141,500,'2019-11'),(141,220,'2019-12'),(141,100,'2020-01'),(141,200,'2020-02'),(141,100,'2020-03'),(141,250,'2020-04'),(141,90,'2020-05');
/*!40000 ALTER TABLE `stocklvl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_settings`
--

DROP TABLE IF EXISTS `system_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_settings` (
  `id` bigint NOT NULL,
  `office_hours` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qvu3djnpwhfibjsu8r95m2uvr` (`office_hours`),
  CONSTRAINT `FK696pxmq4039gp8d7arg5yyyva` FOREIGN KEY (`office_hours`) REFERENCES `office_hours` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_settings`
--

LOCK TABLES `system_settings` WRITE;
/*!40000 ALTER TABLE `system_settings` DISABLE KEYS */;
INSERT INTO `system_settings` VALUES (280,279);
/*!40000 ALTER TABLE `system_settings` ENABLE KEYS */;
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
INSERT INTO `user` VALUES (33,'2020-05-16 13:17:46.972752','cedric.plouvier@student.uantwerpen.be','Cedric','Plouvier','','$2a$10$xwm57HgSvLwRsnpa0h8I6utLeNpVJ4TBOINDFx4eSJmHKr.FAJBCS','','20152267','2020-05-16 13:17:46.972752','Cedric'),(34,'2020-05-16 13:17:46.990769','ruben.joosen@student.uantwerpen.be','Ruben','Joosen','','$2a$10$ndxzhZsNTWcJXt1eEkLe2u8CUDVZAQuiYLlDwsVt6UP9MKWtC7I0O','','20164473','2020-05-16 13:17:46.990769','Ruben'),(35,'2020-05-16 13:17:47.000777','jaimie.vranckx@student.uantwerpen.be','Jaimie','Vranckx','','$2a$10$KpB.9ZjG78uFu/6EyP3zeOfUdRtzbG7KICK85Q4cz2OsMS0Z29N2u','','20155797','2020-05-16 13:17:47.000777','Jaimie'),(36,'2020-05-16 13:17:47.009785','mohammad.amir2@student.uantwerpen.be','Ali','Amir','','$2a$10$CEdrahIlPehkyCBpim1/KOZQoJXSZvR4XHkbSLp5oKxh6pq6UR6aO','','20163446','2020-05-16 13:17:47.009785','Ali'),(37,'2020-05-16 13:17:47.018793','timo.nelen@student.uantwerpen.be','Timo','Nelen','','$2a$10$ljb4cKSKCjnNJxxwVTumCOMr0R7YVNg7lbxzm1XSqg/sXHOMbFro6','','S0162117','2020-05-16 13:17:47.018793','Timo'),(38,'2020-05-16 13:17:47.033807','ondrej.bures@student.uantwerpen.be','Ondrej','Bures','','$2a$10$FnOOkhvXgeC/YJ4GvFe43uhhwgn3INg/VzOYcm4xeXII10oBvzZOK','','20160002','2020-05-16 13:17:47.033807','Ondrej'),(39,'2020-05-16 13:17:47.043816','admin@uantwerpen.be','admin','admin','','$2a$10$EgHy0pDbmQdRXBgOQckqaugUfp67xh8/5bABYPToO1irMpyWIt6fq','',NULL,'2020-05-16 13:17:47.043816','admin'),(40,'2020-05-16 13:17:47.052824','bachelor@student.uantwerpen.be','Bach','Student','','$2a$10$0l1PS7mORlFsllkn7sbv5uvpVyzViLSsOB0srAKGEZ/b3lXLUHN0m','','20170001','2020-05-16 13:17:47.052824','Bachelor'),(41,'2020-05-16 13:17:47.064836','master@student.uantwerpen.be','Mas','Student','','$2a$10$P5OIkGrpGMjpi4noC0gNQeIPiTtGFjNRXzPzLQQHA/oyFI9BbS7rO','','20160009','2020-05-16 13:17:47.064836','Master'),(42,'2020-05-16 13:17:47.072843','researcher@uantwerpen.be','Researcher','Developper','','$2a$10$RcQ7LkiVlgi5Yec5/vEPwOxDL61uPh40P5ObkW78QMc2kpBm4KHJq','','20100001','2020-05-16 13:17:47.072843','Researcher');
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
INSERT INTO `user_role` VALUES (40,29),(41,30),(42,31),(33,32),(34,32),(35,32),(36,32),(37,32),(38,32),(39,32);
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

-- Dump completed on 2020-05-16 15:19:06
