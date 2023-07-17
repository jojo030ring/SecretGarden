-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb 
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema secretgarden
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema secretgarden
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `secretgarden` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;
USE `secretgarden` ;

-- -----------------------------------------------------
-- Table `secretgarden`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `secretgarden`.`users` (
    `user_id` VARCHAR(10) NOT NULL,
    `password` VARCHAR(20) NOT NULL,
    `name` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `phonenum` VARCHAR(45) NOT NULL,
    `regdate` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `secretgarden`.`diary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `secretgarden`.`diary` (
    `diary_id` INT(11) NOT NULL,
    `user_id` VARCHAR(10) NOT NULL,
    `title` VARCHAR(100) NOT NULL,
    `content` TEXT NOT NULL,
    `diary_regdate` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`diary_id`),
    INDEX `fk_diary_users2_idx` (`user_id` ASC),
    CONSTRAINT `fk_diary_users2`
    FOREIGN KEY (`user_id`)
    REFERENCES `secretgarden`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `secretgarden`.`freeboard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `secretgarden`.`freeboard` (
    `freeboard_id` INT(11) NOT NULL,
    `user_id` VARCHAR(10) NOT NULL,
    `title` VARCHAR(100) NOT NULL,
    `content` TEXT NOT NULL,
    `freeboard_regdate` DATETIME NULL DEFAULT NULL,
    PRIMARY KEY (`freeboard_id`),
    INDEX `fk_freeboard_users1_idx` (`user_id` ASC),
    CONSTRAINT `fk_freeboard_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `secretgarden`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
