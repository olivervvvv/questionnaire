--CREATE DATABASE IF NOT EXISTS `questionnaire` ;

CREATE TABLE IF NOT EXISTS `user` (
  `name` varchar(45) NOT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `age` varchar(45) DEFAULT NULL,
  `qn_id` int DEFAULT NULL,
  `q_id` varchar(45) DEFAULT NULL,
  `ans` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
