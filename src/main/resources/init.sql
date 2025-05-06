-- 데이터베이스 생성 (없는 경우)
CREATE DATABASE IF NOT EXISTS `authdb`;

-- 데이터베이스 사용
USE `authdb`;

-- 사용자 테이블 생성
CREATE TABLE IF NOT EXISTS `users` (
                                       `user_id` BIGINT NOT NULL AUTO_INCREMENT,
                                       `username` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`user_id`)
    );