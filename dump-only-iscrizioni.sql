-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: projectdb
-- ------------------------------------------------------
-- Server version	8.0.41

-- Dump per creare lo schema del db e popolarlo con Studenti, Docenti, Corsi e Appelli
-- Successivamente aggiunge Iscrizioni_corsi e aclune iscrizioni ad appelli tutte senza voto nè pubblicazione

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
-- Table structure for table `appelli`
--

DROP TABLE IF EXISTS `appelli`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appelli` (
  `data` date NOT NULL,
  `nome_corso` varchar(256) NOT NULL,
  PRIMARY KEY (`data`,`nome_corso`),
  KEY `nome_corso` (`nome_corso`),
  CONSTRAINT `appelli_ibfk_1` FOREIGN KEY (`nome_corso`) REFERENCES `corsi` (`nome`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appelli`
--

LOCK TABLES `appelli` WRITE;
/*!40000 ALTER TABLE `appelli` DISABLE KEYS */;
INSERT INTO `appelli` VALUES ('2025-06-03','Actuarial Science'),('2025-07-12','Actuarial Science'),('2025-09-09','Actuarial Science'),('2025-06-03','Agricultural Science'),('2025-07-08','Agricultural Science'),('2025-09-06','Agricultural Science'),('2025-06-03','Anthropology'),('2025-07-15','Anthropology'),('2025-06-13','Archaeology'),('2025-07-06','Archaeology'),('2025-08-27','Archaeology'),('2025-06-04','Art History'),('2025-07-06','Art History'),('2025-08-30','Art History'),('2025-06-17','Astronomy'),('2025-07-19','Astronomy'),('2025-09-11','Astronomy'),('2025-06-24','Biochemistry'),('2025-07-14','Biochemistry'),('2025-09-05','Biochemistry'),('2025-06-07','Biology'),('2025-07-20','Biology'),('2025-09-08','Biology'),('2025-06-11','Business Management'),('2025-07-02','Business Management'),('2025-08-28','Business Management'),('2025-06-05','Chemistry'),('2025-07-17','Chemistry'),('2025-09-06','Chemistry'),('2025-06-09','Communication Studies'),('2025-07-21','Communication Studies'),('2025-09-04','Communication Studies'),('2025-06-06','Computer Science'),('2025-07-14','Computer Science'),('2025-09-10','Computer Science'),('2025-07-13','Criminal Justice'),('2025-07-30','Criminal Justice'),('2025-09-10','Criminal Justice'),('2025-07-16','Criminology'),('2025-08-28','Criminology'),('2025-09-06','Criminology'),('2025-07-20','Culinary Arts'),('2025-09-02','Culinary Arts'),('2025-09-12','Culinary Arts'),('2025-06-08','Dance'),('2025-07-19','Dance'),('2025-09-04','Dance'),('2025-07-17','Dental Hygiene'),('2025-07-30','Dental Hygiene'),('2025-09-07','Dental Hygiene'),('2025-06-21','Dietetics'),('2025-07-09','Dietetics'),('2025-06-25','Economics'),('2025-07-24','Education'),('2025-09-04','Education'),('2025-09-13','Education'),('2025-06-07','Engineering'),('2025-07-15','Engineering'),('2025-09-04','Engineering'),('2025-06-07','Environmental Science'),('2025-07-21','Environmental Science'),('2025-09-02','Environmental Science'),('2025-06-10','Ethnic Studies'),('2025-07-03','Ethnic Studies'),('2025-08-28','Ethnic Studies'),('2025-06-14','Fashion Design'),('2025-07-18','Fashion Design'),('2025-09-02','Fashion Design'),('2025-06-18','Film Studies'),('2025-07-07','Film Studies'),('2025-09-08','Film Studies'),('2025-07-23','Foreign Languages'),('2025-09-01','Foreign Languages'),('2025-09-14','Foreign Languages'),('2025-06-15','Forensic Science'),('2025-06-11','Gender Studies'),('2025-07-20','Gender Studies'),('2025-09-11','Gender Studies'),('2025-06-04','Geography'),('2025-07-06','Geography'),('2025-08-29','Geography'),('2025-06-15','Gerontology'),('2025-07-02','Gerontology'),('2025-08-27','Gerontology'),('2025-06-29','Graphic Design'),('2025-07-01','Graphic Design'),('2025-06-12','Health Sciences'),('2025-07-22','Health Sciences'),('2025-09-11','Health Sciences'),('2025-06-04','History'),('2025-07-11','History'),('2025-09-06','History'),('2025-06-28','Horticulture'),('2025-07-27','Horticulture'),('2025-09-11','Horticulture'),('2025-06-19','Hospitality Management'),('2025-07-07','Hospitality Management'),('2025-09-07','Hospitality Management'),('2025-06-13','Human Resources'),('2025-07-03','Human Resources'),('2025-08-28','Human Resources'),('2025-06-22','Industrial Design'),('2025-07-21','Industrial Design'),('2025-09-04','Industrial Design'),('2025-06-23','Interior Design'),('2025-07-10','Interior Design'),('2025-09-14','Interior Design'),('2025-06-08','International Relations'),('2025-07-26','International Relations'),('2025-09-02','International Relations'),('2025-07-03','Journalism'),('2025-07-16','Journalism'),('2025-08-27','Journalism'),('2025-07-09','Kinesiology'),('2025-07-29','Kinesiology'),('2025-09-01','Kinesiology'),('2025-06-11','Legal Studies'),('2025-07-25','Legal Studies'),('2025-09-08','Legal Studies'),('2025-06-15','Library Science'),('2025-07-04','Library Science'),('2025-08-29','Library Science'),('2025-06-12','Linguistics'),('2025-07-05','Linguistics'),('2025-08-27','Linguistics'),('2025-06-20','Literature'),('2025-07-25','Literature'),('2025-09-02','Literature'),('2025-06-19','Marine Biology'),('2025-07-18','Marine Biology'),('2025-09-05','Marine Biology'),('2025-06-16','Marketing'),('2025-07-13','Marketing'),('2025-09-09','Marketing'),('2025-07-11','Mathematics'),('2025-09-02','Mathematics'),('2025-09-08','Mathematics'),('2025-06-08','Media Studies'),('2025-07-18','Media Studies'),('2025-09-03','Media Studies'),('2025-06-24','Meteorology'),('2025-07-01','Meteorology'),('2025-08-27','Meteorology'),('2025-06-17','Military Science'),('2025-07-16','Military Science'),('2025-09-07','Military Science'),('2025-07-03','Music Theory'),('2025-07-16','Music Theory'),('2025-09-01','Music Theory'),('2025-06-12','Neuroscience'),('2025-07-21','Neuroscience'),('2025-09-09','Neuroscience'),('2025-06-19','Nursing'),('2025-07-10','Nursing'),('2025-09-05','Nursing'),('2025-07-14','Nutrition'),('2025-07-26','Nutrition'),('2025-09-04','Nutrition'),('2025-07-08','Occupational Therapy'),('2025-07-21','Occupational Therapy'),('2025-09-06','Occupational Therapy'),('2025-06-28','Optometry'),('2025-07-12','Optometry'),('2025-09-09','Optometry'),('2025-06-26','Paralegal Studies'),('2025-07-09','Paralegal Studies'),('2025-08-29','Paralegal Studies'),('2025-06-23','Peace and Conflict Studies'),('2025-07-18','Peace and Conflict Studies'),('2025-09-10','Peace and Conflict Studies'),('2025-06-25','Physical Therapy'),('2025-07-08','Physical Therapy'),('2025-08-31','Physical Therapy'),('2025-06-23','Physics'),('2025-07-01','Physics'),('2025-09-10','Physics'),('2025-06-27','Pilates Instruction'),('2025-07-11','Pilates Instruction'),('2025-08-31','Pilates Instruction'),('2025-06-20','Political Science'),('2025-07-17','Political Science'),('2025-09-04','Political Science'),('2025-06-27','Psychology'),('2025-07-22','Psychology'),('2025-09-01','Psychology'),('2025-06-24','Public Health'),('2025-07-08','Public Health'),('2025-08-31','Public Health'),('2025-07-05','Public Relations'),('2025-07-28','Public Relations'),('2025-09-03','Public Relations'),('2025-06-16','Quantitative Analysis'),('2025-07-25','Quantitative Analysis'),('2025-09-14','Quantitative Analysis'),('2025-07-04','Radiology'),('2025-07-26','Radiology'),('2025-09-08','Radiology'),('2025-07-17','Religious Studies'),('2025-09-03','Religious Studies'),('2025-09-10','Religious Studies'),('2025-07-11','Robotics'),('2025-07-25','Robotics'),('2025-09-12','Robotics'),('2025-06-27','Social Work'),('2025-07-04','Social Work'),('2025-08-30','Social Work'),('2025-07-20','Sociology'),('2025-08-28','Sociology'),('2025-09-07','Sociology'),('2025-07-23','Sports Management'),('2025-09-05','Sports Management'),('2025-09-12','Sports Management'),('2025-06-30','Statistics'),('2025-07-24','Statistics'),('2025-09-12','Statistics'),('2025-07-03','Sustainability Studies'),('2025-07-24','Sustainability Studies'),('2025-09-07','Sustainability Studies'),('2025-07-05','Sustainable Agriculture'),('2025-07-27','Sustainable Agriculture'),('2025-09-06','Sustainable Agriculture'),('2025-07-08','Taxation'),('2025-07-19','Taxation'),('2025-09-05','Taxation'),('2025-07-10','Theater Arts'),('2025-07-29','Theater Arts'),('2025-09-03','Theater Arts'),('2025-06-20','Theology'),('2025-07-01','Theology'),('2025-08-30','Theology'),('2025-06-28','Tourism Management'),('2025-07-20','Tourism Management'),('2025-09-11','Tourism Management'),('2025-07-28','Urban Forestry'),('2025-09-01','Urban Forestry'),('2025-09-14','Urban Forestry'),('2025-06-29','Urban Planning'),('2025-07-19','Urban Planning'),('2025-09-11','Urban Planning'),('2025-06-05','Urban Studies'),('2025-07-12','Urban Studies'),('2025-09-07','Urban Studies'),('2025-07-02','Veterinary Science'),('2025-07-23','Veterinary Science'),('2025-09-05','Veterinary Science'),('2025-07-01','Viticulture'),('2025-07-23','Viticulture'),('2025-09-05','Viticulture'),('2025-07-07','Web Development'),('2025-07-27','Web Development'),('2025-09-12','Web Development'),('2025-07-14','Wildlife Conservation'),('2025-07-28','Wildlife Conservation'),('2025-09-03','Wildlife Conservation'),('2025-06-09','Wine Studies'),('2025-07-24','Wine Studies'),('2025-09-03','Wine Studies'),('2025-07-13','Youth Development'),('2025-07-30','Youth Development'),('2025-09-08','Youth Development'),('2025-07-10','Zoology'),('2025-07-29','Zoology'),('2025-09-03','Zoology'),('2025-07-13','Zumba Instruction'),('2025-09-01','Zumba Instruction'),('2025-09-12','Zumba Instruction');
/*!40000 ALTER TABLE `appelli` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `corsi`
--

DROP TABLE IF EXISTS `corsi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `corsi` (
  `nome` varchar(256) NOT NULL,
  `codice_docente` int NOT NULL,
  PRIMARY KEY (`nome`),
  KEY `codice_docente` (`codice_docente`),
  CONSTRAINT `corsi_ibfk_1` FOREIGN KEY (`codice_docente`) REFERENCES `docenti` (`codice_docente`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `corsi`
--

LOCK TABLES `corsi` WRITE;
/*!40000 ALTER TABLE `corsi` DISABLE KEYS */;
INSERT INTO `corsi` VALUES ('Anthropology',20108865),('Biology',20108865),('Business Management',20108865),('Forensic Science',20108865),('Nursing',20108865),('Peace and Conflict Studies',20108865),('Social Work',20108865),('Urban Studies',20108865),('Wine Studies',20108865),('Archaeology',24034276),('Astronomy',24034276),('Dietetics',24034276),('Economics',24034276),('Graphic Design',24034276),('Music Theory',24034276),('Occupational Therapy',24034276),('Robotics',24034276),('Wildlife Conservation',24034276),('Dental Hygiene',25839574),('Foreign Languages',25839574),('History',25839574),('International Relations',25839574),('Linguistics',25839574),('Marketing',25839574),('Political Science',25839574),('Public Health',25839574),('Tourism Management',25839574),('Viticulture',25839574),('Computer Science',31125296),('Ethnic Studies',31125296),('Fashion Design',31125296),('Film Studies',31125296),('Industrial Design',31125296),('Paralegal Studies',31125296),('Statistics',31125296),('Sustainable Agriculture',31125296),('Zoology',31125296),('Actuarial Science',40207464),('Engineering',40207464),('Gender Studies',40207464),('Gerontology',40207464),('Hospitality Management',40207464),('Interior Design',40207464),('Psychology',40207464),('Radiology',40207464),('Taxation',40207464),('Criminal Justice',42217896),('Criminology',42217896),('Culinary Arts',42217896),('Education',42217896),('Geography',42217896),('Media Studies',42217896),('Neuroscience',42217896),('Quantitative Analysis',42217896),('Theology',42217896),('Biochemistry',58875613),('Horticulture',58875613),('Journalism',58875613),('Kinesiology',58875613),('Mathematics',58875613),('Nutrition',58875613),('Religious Studies',58875613),('Sociology',58875613),('Sports Management',58875613),('Urban Forestry',58875613),('Agricultural Science',59801220),('Environmental Science',59801220),('Legal Studies',59801220),('Library Science',59801220),('Marine Biology',59801220),('Physics',59801220),('Pilates Instruction',59801220),('Sustainability Studies',59801220),('Theater Arts',59801220),('Zumba Instruction',59801220),('Art History',77620182),('Dance',77620182),('Health Sciences',77620182),('Information Technology',77620182),('Literature',77620182),('Meteorology',77620182),('Optometry',77620182),('Public Relations',77620182),('Yoga Studies',77620182),('Youth Development',77620182),('Chemistry',90789707),('Communication Studies',90789707),('Human Resources',90789707),('Military Science',90789707),('Philosophy',90789707),('Physical Therapy',90789707),('Urban Planning',90789707),('Veterinary Science',90789707),('Web Development',90789707),('Xenobiology',90789707);
/*!40000 ALTER TABLE `corsi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `docenti`
--

DROP TABLE IF EXISTS `docenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docenti` (
  `codice_docente` int NOT NULL,
  `nome` varchar(256) NOT NULL,
  `cognome` varchar(256) NOT NULL,
  `email` varchar(256) NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`codice_docente`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `docenti_chk_1` CHECK ((`codice_docente` > 0)),
  CONSTRAINT `docenti_chk_2` CHECK (((`codice_docente` > 10000000) and (`codice_docente` < 99999999)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `docenti`
--

LOCK TABLES `docenti` WRITE;
/*!40000 ALTER TABLE `docenti` DISABLE KEYS */;
INSERT INTO `docenti` VALUES (20108865,'Demetris','Hatchman','dhatchman0@mail.com','rhzbrgbi'),(24034276,'Pauletta','Le Breton','plebreton8@mail.com','tjyegllc'),(25839574,'Cherish','Pacquet','cpacquet6@mail.com','mwtglvhb'),(31125296,'Webb','Bannon','wbannon3@mail.com','eldrbpas'),(40207464,'Yorker','Botte','ybotte2@mail.com','wcekixky'),(42217896,'Loraine','Roundtree','lroundtree5@mail.com','lvjcrnbt'),(58875613,'Agnese','Pitt','apitt4@mail.com','xrjpqrzh'),(59801220,'Jefferson','Glowach','jglowach7@mail.com','mvqmdtel'),(77620182,'Ellynn','Bolliver','ebolliver1@mail.com','wcionclf'),(90789707,'Dimitri','Stebbins','dstebbins9@mail.com','rltfaonk');
/*!40000 ALTER TABLE `docenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iscrizioni`
--

DROP TABLE IF EXISTS `iscrizioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `iscrizioni` (
  `nome_corso` varchar(256) NOT NULL,
  `data_appello` date NOT NULL,
  `matricola_studente` int NOT NULL,
  `voto` varchar(16) DEFAULT NULL,
  `stato_pubblicazione` varchar(16) NOT NULL,
  PRIMARY KEY (`nome_corso`,`data_appello`,`matricola_studente`),
  KEY `matricola_studente` (`matricola_studente`,`nome_corso`),
  CONSTRAINT `iscrizioni_ibfk_1` FOREIGN KEY (`nome_corso`, `data_appello`) REFERENCES `appelli` (`nome_corso`, `data`) ON UPDATE CASCADE,
  CONSTRAINT `iscrizioni_ibfk_2` FOREIGN KEY (`matricola_studente`) REFERENCES `studenti` (`matricola`) ON UPDATE CASCADE,
  CONSTRAINT `iscrizioni_ibfk_3` FOREIGN KEY (`matricola_studente`, `nome_corso`) REFERENCES `iscrizioni_corsi` (`matricola_studente`, `nome_corso`),
  CONSTRAINT `iscrizioni_chk_1` CHECK (((`stato_pubblicazione` = _utf8mb4'non inserito') or (`stato_pubblicazione` = _utf8mb4'inserito') or (`stato_pubblicazione` = _utf8mb4'pubblicato') or (`stato_pubblicazione` = _utf8mb4'rifiutato') or (`stato_pubblicazione` = _utf8mb4'verbalizzato')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iscrizioni`
--

LOCK TABLES `iscrizioni` WRITE;
/*!40000 ALTER TABLE `iscrizioni` DISABLE KEYS */;
INSERT INTO `iscrizioni` VALUES ('Anthropology','2025-06-03',109416,NULL,'non inserito'),('Anthropology','2025-06-03',142357,NULL,'non inserito'),('Anthropology','2025-06-03',152033,NULL,'non inserito'),('Anthropology','2025-06-03',165432,NULL,'non inserito'),('Anthropology','2025-06-03',176543,NULL,'non inserito'),('Anthropology','2025-06-03',187654,NULL,'non inserito'),('Anthropology','2025-06-03',190426,NULL,'non inserito'),('Anthropology','2025-06-03',198306,NULL,'non inserito'),('Anthropology','2025-06-03',201537,NULL,'non inserito'),('Anthropology','2025-06-03',959570,NULL,'non inserito'),('Anthropology','2025-07-15',142357,NULL,'non inserito'),('Anthropology','2025-07-15',176543,NULL,'non inserito'),('Anthropology','2025-07-15',190426,NULL,'non inserito'),('Anthropology','2025-07-15',201537,NULL,'non inserito'),('Anthropology','2025-07-15',209417,NULL,'non inserito'),('Anthropology','2025-07-15',210527,NULL,'non inserito'),('Art History','2025-08-30',176543,NULL,'non inserito'),('Biology','2025-06-07',142357,NULL,'non inserito'),('Biology','2025-06-07',187654,NULL,'non inserito'),('Biology','2025-06-07',198306,NULL,'non inserito'),('Biology','2025-06-07',210527,NULL,'non inserito'),('Biology','2025-07-20',109416,NULL,'non inserito'),('Biology','2025-07-20',176543,NULL,'non inserito'),('Biology','2025-07-20',190426,NULL,'non inserito'),('Biology','2025-07-20',201537,NULL,'non inserito'),('Biology','2025-07-20',959570,NULL,'non inserito'),('Biology','2025-09-08',152033,NULL,'non inserito'),('Business Management','2025-06-11',109416,NULL,'non inserito'),('Business Management','2025-06-11',187654,NULL,'non inserito'),('Business Management','2025-06-11',959570,NULL,'non inserito'),('Business Management','2025-07-02',142357,NULL,'non inserito'),('Business Management','2025-07-02',190426,NULL,'non inserito'),('Business Management','2025-07-02',201537,NULL,'non inserito'),('Business Management','2025-07-02',210527,NULL,'non inserito'),('Business Management','2025-08-28',152033,NULL,'non inserito'),('Business Management','2025-08-28',165432,NULL,'non inserito'),('Business Management','2025-08-28',198306,NULL,'non inserito'),('Business Management','2025-08-28',209417,NULL,'non inserito'),('Computer Science','2025-07-14',109416,NULL,'non inserito'),('Computer Science','2025-07-14',190426,NULL,'non inserito'),('Computer Science','2025-07-14',210527,NULL,'non inserito'),('Computer Science','2025-09-10',152033,NULL,'non inserito'),('Computer Science','2025-09-10',198306,NULL,'non inserito'),('Dance','2025-07-19',165432,NULL,'non inserito'),('Dance','2025-07-19',209417,NULL,'non inserito'),('Film Studies','2025-09-08',187654,NULL,'non inserito'),('Film Studies','2025-09-08',198306,NULL,'non inserito'),('Forensic Science','2025-06-15',109416,NULL,'non inserito'),('Forensic Science','2025-06-15',142357,NULL,'non inserito'),('Forensic Science','2025-06-15',152033,NULL,'non inserito'),('Forensic Science','2025-06-15',176543,NULL,'non inserito'),('Forensic Science','2025-06-15',198306,NULL,'non inserito'),('Forensic Science','2025-06-15',201537,NULL,'non inserito'),('Forensic Science','2025-06-15',209417,NULL,'non inserito'),('Forensic Science','2025-06-15',959570,NULL,'non inserito'),('Gender Studies','2025-07-20',165432,NULL,'non inserito'),('Gender Studies','2025-07-20',210527,NULL,'non inserito'),('Gender Studies','2025-09-11',187654,NULL,'non inserito'),('Gender Studies','2025-09-11',209417,NULL,'non inserito'),('Interior Design','2025-07-10',209417,NULL,'non inserito'),('Literature','2025-09-02',210527,NULL,'non inserito'),('Marketing','2025-07-13',201537,NULL,'non inserito'),('Marketing','2025-09-09',152033,NULL,'non inserito'),('Marketing','2025-09-09',187654,NULL,'non inserito'),('Media Studies','2025-07-18',209417,NULL,'non inserito'),('Media Studies','2025-09-03',176543,NULL,'non inserito'),('Neuroscience','2025-09-09',190426,NULL,'non inserito'),('Nursing','2025-06-19',109416,NULL,'non inserito'),('Nursing','2025-07-10',142357,NULL,'non inserito'),('Nursing','2025-07-10',152033,NULL,'non inserito'),('Nursing','2025-07-10',959570,NULL,'non inserito'),('Nutrition','2025-07-14',190426,NULL,'non inserito'),('Nutrition','2025-07-14',959570,NULL,'non inserito'),('Nutrition','2025-09-04',165432,NULL,'non inserito'),('Nutrition','2025-09-04',209417,NULL,'non inserito'),('Optometry','2025-09-09',187654,NULL,'non inserito'),('Peace and Conflict Studies','2025-07-18',109416,NULL,'non inserito'),('Peace and Conflict Studies','2025-07-18',190426,NULL,'non inserito'),('Peace and Conflict Studies','2025-07-18',201537,NULL,'non inserito'),('Peace and Conflict Studies','2025-07-18',210527,NULL,'non inserito'),('Peace and Conflict Studies','2025-09-10',142357,NULL,'non inserito'),('Peace and Conflict Studies','2025-09-10',176543,NULL,'non inserito'),('Peace and Conflict Studies','2025-09-10',959570,NULL,'non inserito'),('Psychology','2025-09-01',165432,NULL,'non inserito'),('Psychology','2025-09-01',209417,NULL,'non inserito'),('Social Work','2025-06-27',109416,NULL,'non inserito'),('Social Work','2025-06-27',187654,NULL,'non inserito'),('Social Work','2025-06-27',198306,NULL,'non inserito'),('Social Work','2025-07-04',142357,NULL,'non inserito'),('Social Work','2025-07-04',165432,NULL,'non inserito'),('Social Work','2025-07-04',210527,NULL,'non inserito'),('Social Work','2025-07-04',959570,NULL,'non inserito'),('Social Work','2025-08-30',176543,NULL,'non inserito'),('Social Work','2025-08-30',190426,NULL,'non inserito'),('Social Work','2025-08-30',201537,NULL,'non inserito'),('Statistics','2025-09-12',152033,NULL,'non inserito'),('Statistics','2025-09-12',201537,NULL,'non inserito'),('Urban Studies','2025-07-12',109416,NULL,'non inserito'),('Urban Studies','2025-07-12',165432,NULL,'non inserito'),('Urban Studies','2025-07-12',187654,NULL,'non inserito'),('Urban Studies','2025-07-12',190426,NULL,'non inserito'),('Urban Studies','2025-07-12',198306,NULL,'non inserito'),('Urban Studies','2025-07-12',201537,NULL,'non inserito'),('Urban Studies','2025-07-12',210527,NULL,'non inserito'),('Urban Studies','2025-07-12',959570,NULL,'non inserito'),('Urban Studies','2025-09-07',142357,NULL,'non inserito'),('Urban Studies','2025-09-07',176543,NULL,'non inserito'),('Wine Studies','2025-06-09',109416,NULL,'non inserito'),('Wine Studies','2025-06-09',198306,NULL,'non inserito'),('Wine Studies','2025-07-24',142357,NULL,'non inserito'),('Wine Studies','2025-07-24',187654,NULL,'non inserito'),('Wine Studies','2025-07-24',190426,NULL,'non inserito'),('Wine Studies','2025-07-24',210527,NULL,'non inserito'),('Wine Studies','2025-09-03',165432,NULL,'non inserito'),('Wine Studies','2025-09-03',176543,NULL,'non inserito'),('Wine Studies','2025-09-03',201537,NULL,'non inserito'),('Wine Studies','2025-09-03',209417,NULL,'non inserito'),('Wine Studies','2025-09-03',959570,NULL,'non inserito'),('Zoology','2025-09-03',198306,NULL,'non inserito');
/*!40000 ALTER TABLE `iscrizioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iscrizioni_corsi`
--

DROP TABLE IF EXISTS `iscrizioni_corsi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `iscrizioni_corsi` (
  `nome_corso` varchar(256) NOT NULL,
  `matricola_studente` int NOT NULL,
  PRIMARY KEY (`nome_corso`,`matricola_studente`),
  KEY `matricola_studente` (`matricola_studente`),
  CONSTRAINT `iscrizioni_corsi_ibfk_1` FOREIGN KEY (`nome_corso`) REFERENCES `corsi` (`nome`) ON UPDATE CASCADE,
  CONSTRAINT `iscrizioni_corsi_ibfk_2` FOREIGN KEY (`matricola_studente`) REFERENCES `studenti` (`matricola`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `iscrizioni_corsi`
--

LOCK TABLES `iscrizioni_corsi` WRITE;
/*!40000 ALTER TABLE `iscrizioni_corsi` DISABLE KEYS */;
INSERT INTO `iscrizioni_corsi` VALUES ('Anthropology',109416),('Biology',109416),('Business Management',109416),('Computer Science',109416),('Forensic Science',109416),('Nursing',109416),('Peace and Conflict Studies',109416),('Social Work',109416),('Urban Studies',109416),('Wine Studies',109416),('Anthropology',142357),('Biology',142357),('Business Management',142357),('Forensic Science',142357),('Nursing',142357),('Peace and Conflict Studies',142357),('Social Work',142357),('Urban Studies',142357),('Wine Studies',142357),('Anthropology',152033),('Biology',152033),('Business Management',152033),('Computer Science',152033),('Film Studies',152033),('Forensic Science',152033),('Marketing',152033),('Nursing',152033),('Statistics',152033),('Anthropology',165432),('Business Management',165432),('Dance',165432),('Gender Studies',165432),('Nutrition',165432),('Psychology',165432),('Social Work',165432),('Urban Studies',165432),('Wine Studies',165432),('Anthropology',176543),('Art History',176543),('Biology',176543),('Forensic Science',176543),('Media Studies',176543),('Peace and Conflict Studies',176543),('Social Work',176543),('Urban Studies',176543),('Wine Studies',176543),('Anthropology',187654),('Biology',187654),('Business Management',187654),('Film Studies',187654),('Gender Studies',187654),('Marketing',187654),('Optometry',187654),('Social Work',187654),('Urban Studies',187654),('Wine Studies',187654),('Anthropology',190426),('Biology',190426),('Business Management',190426),('Computer Science',190426),('Neuroscience',190426),('Nutrition',190426),('Peace and Conflict Studies',190426),('Social Work',190426),('Urban Studies',190426),('Wine Studies',190426),('Anthropology',198306),('Biology',198306),('Business Management',198306),('Computer Science',198306),('Film Studies',198306),('Forensic Science',198306),('Social Work',198306),('Urban Studies',198306),('Wine Studies',198306),('Zoology',198306),('Anthropology',201537),('Biology',201537),('Business Management',201537),('Forensic Science',201537),('Marketing',201537),('Peace and Conflict Studies',201537),('Social Work',201537),('Statistics',201537),('Urban Studies',201537),('Wine Studies',201537),('Anthropology',209417),('Business Management',209417),('Dance',209417),('Forensic Science',209417),('Gender Studies',209417),('Interior Design',209417),('Media Studies',209417),('Nutrition',209417),('Psychology',209417),('Wine Studies',209417),('Anthropology',210527),('Biology',210527),('Business Management',210527),('Computer Science',210527),('Gender Studies',210527),('Literature',210527),('Peace and Conflict Studies',210527),('Social Work',210527),('Urban Studies',210527),('Wine Studies',210527),('Anthropology',959570),('Biology',959570),('Business Management',959570),('Forensic Science',959570),('Nursing',959570),('Nutrition',959570),('Peace and Conflict Studies',959570),('Social Work',959570),('Urban Studies',959570),('Wine Studies',959570);
/*!40000 ALTER TABLE `iscrizioni_corsi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studenti`
--

DROP TABLE IF EXISTS `studenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studenti` (
  `matricola` int NOT NULL,
  `nome` varchar(256) NOT NULL,
  `cognome` varchar(256) NOT NULL,
  `email` varchar(256) NOT NULL,
  `password` varchar(32) NOT NULL,
  `corso_laurea` varchar(256) NOT NULL,
  PRIMARY KEY (`matricola`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `studenti_chk_1` CHECK ((`matricola` > 0)),
  CONSTRAINT `studenti_chk_2` CHECK (((`matricola` > 100000) and (`matricola` < 999999)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studenti`
--

LOCK TABLES `studenti` WRITE;
/*!40000 ALTER TABLE `studenti` DISABLE KEYS */;
INSERT INTO `studenti` VALUES (109416,'Elijah','Harris','elijah.harris@yahoo.com','rF9kPm4T','Master of Business Administration'),(142357,'Liam','Smith','liam.smith@gmail.com','pT7kNr4Q','Master of Computer Science'),(152033,'Loralyn','Vautin','lvautin5@mediafire.com','htrgafqn','Bachelor of Economics'),(165432,'Charles','Bailey','charles.bailey@live.com','uK7wFm4T','Bachelor of Economics'),(176543,'Aurora','Evans','aurora.evans@gmail.com','wT8gNs2Q','Bachelor of Arts'),(187654,'Henry','Nelson','henry.nelson@university.edu','mK4vXy3N','Master of Engineering'),(190426,'Ethan','Davis','ethan.davis@hotmail.com','oK3rQj6P','Master of Engineering'),(198306,'Zoey','Lee','zoey.lee@mail.com','wB8dKj4Q','Master of Computer Science'),(201537,'Lucas','Miller','lucas.miller@yahoo.com','qE9fGj4T','Master of Business Administration'),(209417,'Penelope','Walker','penelope.walker@alumni.org','yN2tLf6R','Bachelor of Psychology'),(210527,'Jacob','Martin','jacob.martin@university.edu','uN6gVc8W','Master of Engineering'),(273849,'Noah','Johnson','noah_johnson@hotmail.com','xY3vTm8L','Bachelor of Economics'),(276266,'Sorcha','McGeady','smcgeady4@php.net','hgdowlky','Doctor of Physical Therapy'),(276543,'Ruby','Rivera','ruby.rivera@college.edu','yE5bVn9X','Master of Engineering'),(276940,'Hillary','Marushak','hmarushak7@gnu.org','ghdhoiuk','Master of Aerospace Engineering'),(287654,'Anthony','Edwards','anthony.edwards@college.edu','iK7bVt5R','Master of Business Administration'),(298765,'Lily','Carter','lily.carter@mail.com','vJ8qGh5P','Bachelor of Psychology'),(309876,'Sebastian','Mitchell','sebastian.mitchell@live.com','yF6dLp2Q','Bachelor of Business'),(310528,'Riley','Hall','riley.hall@outlook.com','zF4gPm8L','Master of Business Administration'),(312648,'Mason','Wilson','mason.wilson@student.edu','zB7wHs1N','Master of Computer Science'),(321638,'Mila','Thompson','mila.thompson@mail.com','dT5rQx3Y','Master of Computer Science'),(383085,'Ken','Lenoir','klenoir8@mlb.com','ldberpbf','Juris Doctor'),(387654,'Andrew','Cooper','andrew.cooper@outlook.com','kT8qNp3Y','Bachelor of Arts'),(394651,'Emma','Williams','emma.williams@yahoo.com','uW2dLp6R','Master of Engineering'),(398765,'Ellie','Collins','ellie.collins@mail.com','eW3dKn4Y','Bachelor of Psychology'),(409876,'Isaac','Stewart','isaac.stewart@hotmail.com','mN5rQt8V','Master of Computer Science'),(410987,'Nora','Perez','nora.perez@gmail.com','kT9gVr7L','Master of Computer Science'),(415263,'Olivia','Brown','olivia.brown@university.edu','rV8gFq3H','Bachelor of Science'),(421639,'Layla','Allen','layla.allen@gmail.com','vJ3sYk2X','Bachelor of Business'),(423759,'Sophia','Moore','sophia.moore@university.edu','yD2nGu5R','Bachelor of Psychology'),(432749,'Aria','Garcia','aria.garcia@alumni.org','eW1sLb6P','Bachelor of Psychology'),(465549,'Elisa','Seignior','eseignior1@tamu.edu','ddgpzwpj','Doctor of Optometry'),(498765,'Penelope','Richardson','penelope.richardson@mail.com','vF6rLt8K','Bachelor of Business'),(509876,'Eli','Cox','eli.cox@university.edu','mP4tVj6R','Master of Computer Science'),(510987,'Stella','Morris','stella.morris@outlook.com','zF6qWb3P','Bachelor of Economics'),(521098,'Daniel','Roberts','daniel.roberts@yahoo.com','uW5bCj3V','Bachelor of Economics'),(532740,'Luna','Young','luna.young@hotmail.com','kM5tRb6N','Master of Engineering'),(534861,'Charlotte','Taylor','charlotte.taylor@mail.com','vJ4qKn8L','Bachelor of Economics'),(536472,'Ava','Jones','ava.jones@mail.com','tZ5nPk9S','Master of Business Administration'),(543851,'Ella','Martinez','ella.martinez@live.com','iK7nRe8Q','Bachelor of Business'),(610987,'Madeline','Ward','madeline.ward@gmail.com','sJ3qLb7T','Bachelor of Psychology'),(621098,'David','Rogers','david.rogers@alumni.org','tJ4gNy7W','Master of Engineering'),(632109,'Zoe','Turner','zoe.turner@outlook.com','sN3tLf8R','Master of Business Administration'),(643210,'Nathan','Scott','nathan.scott@college.edu','hG7vKs2R','Bachelor of Arts'),(645972,'Amelia','Anderson','amelia.anderson@alumni.org','wH6sPe2X','Master of Engineering'),(654962,'Scarlett','Robinson','scarlett.robinson@gmail.com','oC3vPf2L','Master of Business Administration'),(657183,'Isabella','Garcia','isabella.garcia@alumni.org','mN4bCe7V','Master of Computer Science'),(721098,'Aaron','Peterson','aaron.peterson@alumni.org','oW5gFk9Y','Bachelor of Economics'),(732109,'Paisley','Reed','paisley.reed@university.edu','xL9vGt2K','Bachelor of Psychology'),(743210,'Joseph','Phillips','joseph.phillips@mail.com','qP4nXy6K','Bachelor of Psychology'),(754321,'Avery','Lopez','avery.lopez@gmail.com','xT3fBn6W','Master of Computer Science'),(756183,'Harper','Thomas','harper.thomas@outlook.com','kM5rLt3V','Master of Computer Science'),(762314,'Dyna','Screase','dscrease3@xinhuanet.com','byrbzoxk','Master of Architecture'),(765073,'Victoria','Clark','victoria.clark@hotmail.com','tM4yXs1V','Master of Engineering'),(768294,'Mia','Martinez','mia.martinez@live.com','jF6sKy2W','Bachelor of Psychology'),(832109,'Naomi','Gray','naomi.gray@live.com','pB6nRj4W','Master of Engineering'),(843210,'Thomas','Cook','thomas.cook@mail.com','rC2pXj6N','Bachelor of Business'),(854321,'Hazel','Campbell','hazel.campbell@university.edu','nR6fLp9T','Bachelor of Business'),(865432,'Levi','Hill','levi.hill@hotmail.com','pL5nRq8T','Bachelor of Economics'),(867294,'Eleanor','Jackson','eleanor.jackson@gmail.com','sQ8bGy5N','Bachelor of Business'),(876184,'Grace','Rodriguez','grace.rodriguez@yahoo.com','nJ6pTk7R','Bachelor of Psychology'),(879305,'Amelia','Rodriguez','amelia.rodriguez@gmail.com','nL1pVt8X','Bachelor of Economics'),(889850,'Harlin','Camolli','hcamolli2@vkontakte.ru','wtffcica','Master of Library Science'),(919865,'Leeann','Conahy','lconahy0@gnu.org','hknkgztv','Master of Social Work'),(954321,'Lucy','Morgan','lucy.morgan@gmail.com','nD3tLb5V','Master of Business Administration'),(959570,'Daphna','Bates','dbates9@who.int','chkzwvlv','Master of Nutrition'),(965432,'Julian','Parker','julian.parker@alumni.org','oL2vFm7X','Master of Engineering'),(976543,'Camila','Adams','camila.adams@alumni.org','rD6pKt9Y','Master of Business Administration'),(978305,'Logan','White','logan.white@hotmail.com','pL2dTk7R','Bachelor of Psychology'),(987295,'Chloe','Lewis','chloe.lewis@university.edu','mV5rGh3P','Bachelor of Business'),(989601,'Jen','Marrion','jmarrion6@deliciousdays.com','akvenrli','Bachelor of Nursing');
/*!40000 ALTER TABLE `studenti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verbali`
--

DROP TABLE IF EXISTS `verbali`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verbali` (
  `codice` char(36) NOT NULL,
  `data_creazione` date NOT NULL,
  `ora_creazione` time NOT NULL,
  `nome_corso` varchar(256) NOT NULL,
  `data_appello` date NOT NULL,
  PRIMARY KEY (`codice`),
  KEY `nome_corso` (`nome_corso`,`data_appello`),
  CONSTRAINT `verbali_ibfk_1` FOREIGN KEY (`nome_corso`, `data_appello`) REFERENCES `appelli` (`nome_corso`, `data`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verbali`
--

LOCK TABLES `verbali` WRITE;
/*!40000 ALTER TABLE `verbali` DISABLE KEYS */;
/*!40000 ALTER TABLE `verbali` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verbalizzazioni`
--

DROP TABLE IF EXISTS `verbalizzazioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verbalizzazioni` (
  `codice_verbale` char(36) NOT NULL,
  `matricola_studente` int NOT NULL,
  PRIMARY KEY (`codice_verbale`,`matricola_studente`),
  KEY `matricola_studente` (`matricola_studente`),
  CONSTRAINT `verbalizzazioni_ibfk_1` FOREIGN KEY (`codice_verbale`) REFERENCES `verbali` (`codice`) ON UPDATE CASCADE,
  CONSTRAINT `verbalizzazioni_ibfk_2` FOREIGN KEY (`matricola_studente`) REFERENCES `studenti` (`matricola`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verbalizzazioni`
--

LOCK TABLES `verbalizzazioni` WRITE;
/*!40000 ALTER TABLE `verbalizzazioni` DISABLE KEYS */;
/*!40000 ALTER TABLE `verbalizzazioni` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-11 16:47:44
