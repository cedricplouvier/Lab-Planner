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
  `comment` varchar(255) DEFAULT NULL,
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
INSERT INTO `device_information` VALUES (44,'vix detraxit elit eget dis ceteros tale cum invidunt quaerendum donec id eum solum congue nobis theophrastus libero consul vis','Manual'),(45,'malesuada dicam eripuit verear congue populo habemus vix nobis torquent reformidans necessitatibus enim fabulas brute habitant feugiat homero scripta molestiae','Maintenance'),(46,'parturient quas platea quaerendum possim consul error dis repudiandae habitant conclusionemque conclusionemque vim eget vero augue ludus alterum iisque suscipiantur','Calibration'),(47,'viverra disputationi iisque leo amet moderatius ex scripta delectus class nulla tritani ex constituam eirmod wisi ne affert possit odio','Safety instruction card'),(48,'tempus eruditi gravida oratio qui appetere tota primis ignota verterem curabitur volutpat nullam oporteat viverra platonem corrumpit periculis errem vero','Manual'),(49,'iisque taciti te graecis ubique eruditi auctor his intellegat novum congue epicuri consectetur bibendum dolor tation graeci habeo ornare disputationi','Maintenance'),(50,'alia cetero odio dolores mentitum tritani doming bibendum doctus molestiae laudem iuvaret harum quisque pertinax docendi malorum odio aptent aliquid','Calibration'),(51,'no ipsum meliore neglegentur dolorem velit has morbi nec ignota dolorem interpretaris interesset eos numquam wisi consectetuer primis nostra sed','Safety instruction card'),(52,'expetendis fames integer varius porro deterruisset antiopam veniam inimicus signiferumque magnis delectus tation neglegentur venenatis graeci viris disputationi mandamus repudiandae','Manual'),(53,'partiendo eros alterum ancillae mel tation ne intellegebat docendi gravida adversarium omittam mei repudiare tempor risus offendit unum sententiae novum','Maintenance'),(54,'unum sententiae qui noster nominavi ex ornatus dictumst ullamcorper porttitor facilisi corrumpit alterum ullamcorper hinc mentitum augue definitionem vulputate persequeris','Calibration'),(55,'detraxit hinc conubia nisl veniam vel persequeris equidem omnesque debet quaeque iudicabit porro et voluptatum contentiones causae melius sollicitudin fastidii','Safety instruction card'),(56,'graeco libris solet rutrum aliquid verear eos sem tortor appetere omittam mattis blandit disputationi reprehendunt adipiscing reformidans delenit euripidis senectus','Manual'),(57,'no numquam iudicabit an quas sem nam atqui moderatius dolorem sodales dui vehicula docendi option fabellas invidunt ceteros ullamcorper altera','Maintenance'),(58,'vis nisl latine habitasse suscipiantur ignota tempus accusata ornatus invidunt integer nascetur vidisse mentitum ullamcorper te esse pretium vivamus salutatus','Calibration'),(59,'falli eleifend feugait lobortis quod senserit natoque eum epicuri sapientem evertitur necessitatibus consectetur offendit nibh vim no aptent animal eirmod','Safety instruction card'),(60,'quo viderer metus utamur sed ac amet civibus deterruisset an legere massa disputationi sit reprehendunt elitr altera veritus nihil quaeque','Manual'),(61,'offendit iudicabit suscipit quem facilis aperiri leo nobis sagittis bibendum detraxit purus noster audire dolore suscipit possim mollis maluisset volutpat','Maintenance'),(62,'eripuit dapibus pharetra cubilia mollis litora dictum alia agam oratio nihil viderer reformidans cetero gubergren ante impetus putent eam vis','Calibration'),(63,'dis elitr similique dolores postulant habitasse litora instructior civibus sociis massa convenire turpis gravida faucibus nec noluisse aliquip vis himenaeos','Safety instruction card'),(64,'simul mei curabitur varius indoctum tristique fabellas quam putent porttitor efficitur maluisset mutat scripta agam habitasse verear placerat mattis luptatum','Manual'),(65,'adipiscing laoreet porro rutrum nonumy purus pertinax erroribus aptent epicuri dignissim ornare ridens nobis tamquam delectus tamquam quot ubique minim','Maintenance'),(66,'error feugiat vocibus hendrerit principes elementum vitae appetere tortor suscipit splendide expetenda pertinacia movet disputationi quod proin utamur persius non','Calibration'),(67,'persius signiferumque ridens cursus eget electram persius commune id feugait et pulvinar ignota risus quam viverra corrumpit lacus convallis consetetur','Safety instruction card'),(68,'no bibendum intellegat pri justo donec feugait sale epicuri cursus suspendisse cu adipiscing oporteat nonumes nominavi nihil homero efficiantur definitiones','Manual'),(69,'constituam principes tincidunt cubilia persius meliore velit molestiae efficiantur utinam inceptos ullamcorper eget periculis ultricies scripta expetendis hac qui a','Maintenance'),(70,'cetero nihil platonem dicunt et simul litora aliquip nobis aeque iusto possit electram condimentum perpetua inimicus solum amet noster tantas','Calibration'),(71,'auctor tacimates vitae equidem dolores erroribus ponderum decore varius inceptos ceteros harum eleifend volutpat et class in mnesarchum dolorem sodales','Safety instruction card'),(72,'tincidunt hinc condimentum putent moderatius affert vero repudiandae qui habitant','Other'),(73,'lacinia quam noluisse donec epicurei condimentum graece porttitor leo constituam graeci veritus voluptatum percipit minim saepe mucius vehicula dignissim offendit','Manual'),(74,'pertinax brute comprehensam nibh reprimique pellentesque voluptatibus quis est auctor adipisci gloriatur voluptatibus ut graece tractatos verterem ancillae gubergren regione','Maintenance'),(75,'doctus ornare lacinia liber nulla ad dolor neque lacinia parturient cursus natum expetenda dicta persius cum detraxit perpetua gravida decore','Calibration'),(76,'interesset elaboraret curabitur consetetur novum verear utamur eam idque deseruisse quaerendum quaerendum moderatius magnis tacimates habitasse gubergren curae aliquam alterum','Safety instruction card'),(77,'mentitum sociosqu ac laudem legimus error blandit nam aperiri phasellus esse atomorum tempor doming metus efficitur vocibus ornare mel deterruisset','Manual'),(78,'vituperatoribus urna qui consectetuer dolor discere eget efficitur phasellus an ullamcorper affert massa ponderum hendrerit liber esse viderer bibendum a','Maintenance'),(79,'mentitum efficiantur patrioque maecenas definitiones mea blandit sem nunc percipit ullamcorper ponderum mazim mediocritatem inimicus velit alienum aeque sadipscing malorum','Calibration'),(80,'ante malorum ultrices euismod gloriatur scelerisque interpretaris harum principes ac tellus pericula mattis constituto propriae amet dignissim sententiae splendide nonumes','Safety instruction card'),(81,'tota eruditi vero dicunt moderatius splendide senserit vehicula voluptaria habitant labores interdum reprehendunt condimentum eu eos lobortis primis elit option','Manual'),(82,'habemus has fuisset tale felis maiestatis idque legere curae tractatos eleifend placerat tempus mea vidisse graeco molestiae mei vestibulum mediocritatem','Maintenance'),(83,'deterruisset quod sapien dis felis fringilla nam errem ne epicurei offendit euripidis mi sea euripidis quo suscipiantur veri consul inciderint','Calibration'),(84,'partiendo enim mei gubergren inceptos maecenas numquam noster turpis detraxit ocurreret option ipsum definitiones mi pretium libris autem epicurei harum','Safety instruction card'),(85,'nisi nisi semper augue propriae egestas electram elaboraret cursus voluptatibus solum atqui porro deseruisse cras mediocrem nam veritus ei voluptatibus','Manual'),(86,'qualisque id taciti varius usu ei iaculis quem noster maiorum porro populo omnesque brute noluisse a fugit qui mauris dolorum','Maintenance'),(87,'interpretaris taciti putent iusto est voluptatum epicuri tacimates aliquet lacus minim pericula adolescens justo mnesarchum detraxit adversarium consetetur dapibus interdum','Calibration'),(88,'habeo gubergren ornare commune altera assueverit comprehensam tantas comprehensam taciti cu blandit dolorem honestatis varius evertitur ligula mus duo pro','Safety instruction card'),(89,'alterum taciti justo mediocrem tempor affert atqui nihil consectetuer liber non quas alterum volumus veri platonem comprehensam movet nonumy definiebas','Manual'),(90,'dui laudem appetere cum magnis sapientem interesset recteque constituam viverra mazim theophrastus prodesset vulputate fuisset electram doming netus sollicitudin cetero','Maintenance'),(91,'graece dictum potenti aliquam eget detracto latine epicurei indoctum dictas potenti quam veniam splendide aptent maiestatis vocibus nunc cursus natum','Calibration'),(92,'mnesarchum intellegat epicurei eu cu dolorem splendide deseruisse explicari fabulas aliquid quisque dictumst curae melius nunc erat nullam curae fuisset','Safety instruction card'),(93,'adolescens veniam viris error curabitur luptatum senserit interpretaris expetendis disputationi error ornare accommodare sadipscing augue risus cu blandit prompta sociis','Manual'),(94,'phasellus integer graeco velit possit cetero ceteros leo simul duo quaeque expetenda singulis adhuc gubergren consetetur suas posse nobis affert','Maintenance'),(95,'fuisset condimentum etiam feugiat potenti massa cubilia eam natoque tale mandamus tota hendrerit idque eruditi quo nec cu vocent ultricies','Calibration'),(96,'aptent dicta referrentur tantas erroribus appareat pri prodesset petentium platea accommodare etiam enim electram quisque inceptos unum sapien aeque dis','Safety instruction card'),(97,'ad quaestio nisi percipit novum utroque invidunt cubilia quaerendum adolescens honestatis dicat intellegat urbanitas iusto wisi minim dictas assueverit tractatos','Manual'),(98,'quot sed quaerendum fusce elaboraret possit est inimicus iisque hac has cursus erroribus cubilia non theophrastus malesuada utamur reque accommodare','Maintenance'),(99,'ad eum volumus quaerendum fastidii falli curae corrumpit sanctus reque auctor sententiae erroribus saperet saperet tritani feugiat epicuri pertinax tincidunt','Calibration'),(100,'vis tantas risus viris senectus graecis conceptam sollicitudin porta vitae alienum delicata vel disputationi sed duis graeco lacus nunc eirmod','Safety instruction card');
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
INSERT INTO `mixture` VALUES (188,'augue meliore penatibus aliquid faucibus fusce accumsan facilisi fusce autem eros brute id corrumpit indoctum quem luptatum conclusionemque mollis ornatus',NULL,NULL,'APT-C'),(189,'mnesarchum movet eirmod mandamus vidisse repudiare tale curabitur intellegat senectus nisl quem donec delectus rhoncus commune augue inciderint nominavi docendi',NULL,NULL,'AB-4C'),(190,'vidisse euismod laudem conceptam quas congue postea elementum senectus felis facilisis vocibus cursus viderer taciti aptent amet scripserit ne appareat',NULL,NULL,'SMA');
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
  `description` varchar(255) DEFAULT NULL,
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
INSERT INTO `own_product` VALUES (134,'2020-05-15 10:09:12.201025','eirmod quas aliquam veniam postulant interpretaris viderer mei tincidunt vim civibus utamur sociosqu iudicabit augue altera intellegat esse pretium nobis',NULL,NULL,5,200,'Gebroken Porfier 6,3/10',5,'fringilla finibus malorum noster tritani erroribus deterruisset qui',1,2000,1,0,'2020-05-15 10:09:12.201025','https://nl.wikipedia.org/wiki/Porfier'),(135,'2020-05-15 10:09:12.218040','antiopam graeco vero ac mel commodo convenire saperet iisque viris quot mentitum tale posse nisi phasellus sale quo explicari quisque',NULL,NULL,5,200,'Gebroken Porfier 4/6,3',5,'indoctum dui congue invenire nonumes nostra alterum sententiae',1,2000,1,0,'2020-05-15 10:09:12.218040','https://nl.wikipedia.org/wiki/Porfier'),(136,'2020-05-15 10:09:12.230051','lobortis senectus varius iisque lacinia graeci pro doctus quot sadipscing mel dis harum maluisset magnis legere legimus nibh deterruisset sanctus',NULL,NULL,5,200,'Gebroken Profier 2/4',5,'pharetra eius dapibus morbi noster intellegat iudicabit sapientem',1,2000,1,0,'2020-05-15 10:09:12.230051','https://nl.wikipedia.org/wiki/Porfier'),(137,'2020-05-15 10:09:12.240059','quaestio honestatis putent iusto maximus option malesuada cras tortor tractatos nominavi tristique minim delenit ridiculus mollis verear omittam recteque vehicula',NULL,NULL,5,200,'Gewassen Kalksteen 0/2',5,'dicam arcu noster pro sagittis ignota eius commune',1,2000,1,0,'2020-05-15 10:09:12.240059','https://nl.wikipedia.org/wiki/Kalksteen'),(138,'2020-05-15 10:09:12.251069','doctus sagittis esse possim ludus scelerisque iuvaret orci accusata nisl hendrerit magna praesent habeo gloriatur est mediocritatem utamur at accommodare',NULL,NULL,5,100,'Alzagri Rond Zand 0/1',5,'ac nostrum volumus aliquam splendide qui inani sapien',1,1000,1,0,'2020-05-15 10:09:12.251069','https://nl.wikipedia.org/wiki/Zand'),(139,'2020-05-15 10:09:12.262080','habitant posuere ornatus fabulas solum efficitur convallis menandri solum postulant aptent gravida interdum veri quam sea explicari equidem montes eius',NULL,NULL,5,100,'Vulstof duras 2a',5,'prompta mollis impetus in definiebas legimus euripidis ubique',1,500,1,0,'2020-05-15 10:09:12.262080','https://nl.wikipedia.org/wiki/Duras'),(140,'2020-05-15 10:09:12.272089','pretium reformidans quam efficitur definiebas expetenda contentiones habemus docendi duis dolorum cum volumus tation delicata ex nunc accusata legimus netus',NULL,NULL,5,25,'Bitumen op aggregaten',5,'facilisi posidonium amet option dictumst repudiandae dicta indoctum',1,200,1,2,'2020-05-15 10:09:12.272089','https://nl.wikipedia.org/wiki/Bitumen'),(141,'2020-05-15 10:09:12.283098','pericula gravida ignota quem neglegentur disputationi minim sale pertinacia convallis dolorum ridens volumus tation diam pellentesque pellentesque himenaeos habitant nunc',NULL,NULL,5,1,'placeholder8',5,'sanctus dico tation imperdiet his docendi commodo ancillae',1,90,1,1,'2020-05-15 10:09:12.283098',NULL);
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
  `mixture_comment` varchar(255) DEFAULT NULL,
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
INSERT INTO `report` VALUES (236,'suscipiantur id offendit discere consectetuer ubique cubilia accumsan ubique honestatis solet iuvaret eos explicari noster eius suas veritus oratio fermentum venenatis tractatos vulputate nullam tortor','Autosaw is broken',37),(237,'laoreet cubilia delenit sea causae verear urna duis eius montes','Fancy Title',36),(238,'dolorem lacinia percipit elementum metus solum lobortis quaestio aeque donec evertitur massa petentium persius explicari atqui suas ante option hendrerit','Sand mixture is not in stock',34),(239,'mus iuvaret quisque curae luptatum nominavi nascetur morbi imperdiet possim qui tortor viris inceptos expetenda debet omittantur meliore vituperatoribus patrioque','Lab was closed yesterday',41),(240,'vituperata placerat eirmod cum atomorum auctor per efficitur sed volumus moderatius saepe aliquet platea vivamus laoreet scripserit suscipit dico malorum appareat antiopam graeci elaboraret ornatus','Placeholder title',42);
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
INSERT INTO `role` VALUES (29,'2020-05-15 10:09:10.612023','Bachelorstudent','2020-05-15 10:09:10.612023'),(30,'2020-05-15 10:09:10.627649','Masterstudent','2020-05-15 10:09:10.627649'),(31,'2020-05-15 10:09:10.643275','Researcher','2020-05-15 10:09:10.643275'),(32,'2020-05-15 10:09:10.658901','Administrator','2020-05-15 10:09:10.658901');
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
  `comment` varchar(255) DEFAULT NULL,
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
INSERT INTO `step_type` VALUES (198,0,0,'No',_binary '\0','Balance',192,102,210),(199,0,0,'No',_binary '\0','Plate Compactor',193,108,210),(200,0,0,'No',_binary '\0','Mixer',192,103,210),(201,0,0,'No',_binary '\0','Gyratory',194,106,209),(202,0,0,'No',_binary '\0','Autosaw',195,101,209),(203,0,0,'No',_binary '\0','Caliper',192,104,209),(204,0,0,'No',_binary '\0','SVM Setup',192,110,209),(205,0,0,'No',_binary '\0','Vacuum Setup',196,112,209),(206,0,0,'No',_binary '\0','Water Bath',192,113,209),(207,0,0,'No',_binary '\0','Wheel Tracking Test',192,114,210),(208,0,0,'No',_binary '\0','Plate Compactor',197,108,210),(212,0,0,'No',_binary '\0','StepBalance',211,102,235),(214,12,0,'At least',_binary '','StepOven',213,107,235),(216,4,0,'Equal',_binary '','StepBig Mixer',215,103,235),(218,4,0,'Equal',_binary '','StepBalance',217,102,235),(220,4,0,'Equal',_binary '','StepGyratory',219,106,235),(222,2,0,'Equal',_binary '','StepAutosaw',221,101,235),(224,1,0,'Equal',_binary '','StepCaliper',223,104,235),(226,1,0,'Equal',_binary '','StepSVM Setup',225,110,235),(228,1,0,'Equal',_binary '','StepVacuum Setup',227,112,235),(230,70,0,'Equal',_binary '','StepWater Bath',229,113,235),(232,4,0,'Equal',_binary '','StepCooling chamber',231,105,235),(234,1,0,'Equal',_binary '','StepUniframe',233,111,235);
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
INSERT INTO `user` VALUES (33,'2020-05-15 10:09:11.358392','cedric.plouvier@student.uantwerpen.be','Cedric','Plouvier','','$2a$10$x83VLTVinhi4XuLDoJFpKupMKl1MZRyxZLZoDGaVUqDPHZlfo5VKO','','20152267','2020-05-15 10:09:11.358392','Cedric'),(34,'2020-05-15 10:09:11.376507','ruben.joosen@student.uantwerpen.be','Ruben','Joosen','','$2a$10$OgdQdGNb3vJ5sX08.MfRfurxaFeViTEtW1tW54LxXbBXlsGDAjOVW','','20164473','2020-05-15 10:09:11.376507','Ruben'),(35,'2020-05-15 10:09:11.386549','jaimie.vranckx@student.uantwerpen.be','Jaimie','Vranckx','','$2a$10$RFjhwslbd6mhJYuJ9we/j.BBHBnrDifVO4xmW.eMdctR87RYjjxFu','','20155797','2020-05-15 10:09:11.386549','Jaimie'),(36,'2020-05-15 10:09:11.388561','mohammad.amir2@student.uantwerpen.be','Ali','Amir','','$2a$10$pfrQVVQVLuBXCNBLUgebO.VRpYMwi5um4uTwAha0nm6QIB9SU9OAi','','20163446','2020-05-15 10:09:11.388561','Ali'),(37,'2020-05-15 10:09:11.396601','timo.nelen@student.uantwerpen.be','Timo','Nelen','','$2a$10$28XA2At0T5T6NfoppUX/yukWsN9coXhKDHyTbdnEdTm94VvWmRL3m','','S0162117','2020-05-15 10:09:11.396601','Timo'),(38,'2020-05-15 10:09:11.406607','ondrej.bures@student.uantwerpen.be','Ondrej','Bures','','$2a$10$Q5jAoDv2I52qUQ90jmA8A.XlzaAS8xg3FbLMbncHp1u3kHI9WUCyO','','20160002','2020-05-15 10:09:11.406607','Ondrej'),(39,'2020-05-15 10:09:11.406607','admin@uantwerpen.be','admin','admin','','$2a$10$tVjeaGy1Yrkuybz2aPlotuMfTnLWzr2aXa5JDK3UHxix9ed3BhLNq','',NULL,'2020-05-15 10:09:11.406607','admin'),(40,'2020-05-15 10:09:11.422237','bachelor@student.uantwerpen.be','Bach','Student','','$2a$10$88nX4KohTPKCRqYbWY7A7eixW3l5IHlDHNKCUY0M9zijUdVgJoqE2','','20170001','2020-05-15 10:09:11.422237','Bachelor'),(41,'2020-05-15 10:09:11.437863','master@student.uantwerpen.be','Mas','Student','','$2a$10$HshgYP2LFHa1XJvhYzr0suMZAC0d4Bx0iGPrs17xJwteFs3UVhYNG','','20160009','2020-05-15 10:09:11.437863','Master'),(42,'2020-05-15 10:09:11.437863','researcher@uantwerpen.be','Researcher','Developper','','$2a$10$3dBMRXPxtACK0Kpo1v.L9OyrMeobkJmIg6zQdc8sUwD4BucL2LM/i','','20100001','2020-05-15 10:09:11.437863','Researcher');
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

-- Dump completed on 2020-05-15 12:10:52
