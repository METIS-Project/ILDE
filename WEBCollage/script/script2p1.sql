SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `wic2p1` ;
CREATE SCHEMA IF NOT EXISTS `wic2p1` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `wic2p1` ;

-- -----------------------------------------------------
-- Table `wic2p1`.`users`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wic2p1`.`users` (
  `username` VARCHAR(100) NOT NULL ,
  `password` TEXT NOT NULL ,
  `email` TEXT NOT NULL ,
  PRIMARY KEY (`username`) )
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `wic2p1`.`documents`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wic2p1`.`documents` (
  `docid` INT(11) NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(100) NOT NULL ,
  `visibility` ENUM('public','visible','private') NOT NULL DEFAULT 'private' ,
  `username` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`docid`) ,
  INDEX `fk_documents_users` (`username` ASC) ,
  CONSTRAINT `fk_documents_users`
    FOREIGN KEY (`username` )
    REFERENCES `wic2p1`.`users` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `wic2p1`.`lms`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wic2p1`.`lms` (
  `idlms` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(100) NULL ,
  `description` VARCHAR(100) NULL ,
  PRIMARY KEY (`idlms`) )
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `wic2p1`.`lms_installation`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wic2p1`.`lms_installation` (
  `idlms_installation` INT(11) NOT NULL AUTO_INCREMENT ,
  `instance_identifier` TEXT NULL ,
  `title` VARCHAR(100) NOT NULL ,
  `user` TEXT NULL ,
  `pass` TEXT NULL ,
  `others` TEXT NULL ,
  `idlms` INT(11) NOT NULL ,
  `username` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`idlms_installation`) ,
  INDEX `fk_lms_lms_types` (`idlms` ASC) ,
  INDEX `fk_lms_installation_users1` (`username` ASC) ,
  CONSTRAINT `fk_lms_lms_types`
    FOREIGN KEY (`idlms` )
    REFERENCES `wic2p1`.`lms` (`idlms` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_lms_installation_users1`
    FOREIGN KEY (`username` )
    REFERENCES `wic2p1`.`users` (`username` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `wic2p1`.`versions`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `wic2p1`.`versions` (
  `docid` INT(11) NOT NULL ,
  `version` INT(11) NOT NULL ,
  `valid` TINYINT(1) NOT NULL DEFAULT '1' ,
  `next` INT(11) NULL DEFAULT NULL ,
  `time` INT(11) NOT NULL ,
  `design` TEXT NULL DEFAULT NULL ,
  `instance` TEXT NULL DEFAULT NULL ,
  `todo_design` TEXT NULL DEFAULT NULL ,
  `todo_instance` TEXT NULL DEFAULT NULL ,
  `actions` TEXT NULL DEFAULT NULL ,
  `idlms_installation` INT(11) NULL ,
  PRIMARY KEY (`docid`, `version`) ,
  INDEX `fk_versions_documents` (`docid` ASC) ,
  INDEX `fk_design_versions_lms_installation1` (`idlms_installation` ASC) ,
  CONSTRAINT `fk_versions_documents`
    FOREIGN KEY (`docid` )
    REFERENCES `wic2p1`.`documents` (`docid` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_design_versions_lms_installation1`
    FOREIGN KEY (`idlms_installation` )
    REFERENCES `wic2p1`.`lms_installation` (`idlms_installation` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `wic2p1`.`lms`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `wic2p1`;
INSERT INTO `wic2p1`.`lms` (`idlms`, `name`, `description`) VALUES (1, 'File', 'Obtiene el listado de participantes desde un fichero');
INSERT INTO `wic2p1`.`lms` (`idlms`, `name`, `description`) VALUES (2, 'Test adapter', 'Obtiene el listado de participantes desde un adaptador de prueba');
INSERT INTO `wic2p1`.`lms` (`idlms`, `name`, `description`) VALUES (3, 'PA adapter', 'Obtiene el listado de participantes desde el adaptador PA');
INSERT INTO `wic2p1`.`lms` (`idlms`, `name`, `description`) VALUES (4, 'Glue!PS', 'Obtiene el listado de participantes desde Glue!PS');

COMMIT;
