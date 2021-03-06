-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 19, 2012 at 08:48 PM
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `glueps`
--

-- --------------------------------------------------------

--
-- Table structure for table `glueps_deploy_versions`
--

DROP TABLE IF EXISTS `glueps_deploy_versions`;
CREATE TABLE IF NOT EXISTS `glueps_deploy_versions` (
  `DEPLOYID` varchar(255) NOT NULL,
  `VERSION` INT(11) NOT NULL,
  `VALID` TINYINT(1) NOT NULL DEFAULT '1',
  `UNDOALERT` TINYINT(1) NOT NULL DEFAULT '0',
  `NEXT` INT(11) NULL DEFAULT NULL,
  `XMLFILE` longblob,
  PRIMARY KEY (`DEPLOYID`,`VERSION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `glueps_deploys`
--

DROP TABLE IF EXISTS `glueps_deploys`;
CREATE TABLE IF NOT EXISTS `glueps_deploys` (
  `ID` varchar(255) NOT NULL,
  `DESIGNID` varchar(255) DEFAULT NULL,
  `USERID` bigint(20) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `COMPLETE` TINYINT(1) NOT NULL DEFAULT '0',
  `STATIC` varchar(255),
  `LIVE` varchar(255),
  `XMLFILE` longblob,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `glueps_designs`
--

DROP TABLE IF EXISTS `glueps_designs`;
CREATE TABLE IF NOT EXISTS `glueps_designs` (
  `ID` varchar(255) NOT NULL,
  `USERID` bigint(20) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `TYPE` varchar(255) NOT NULL,
  `XMLFILE` longblob,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------

--
-- Table structure for table `glueps_learning_environments`
--


DROP TABLE IF EXISTS `glueps_learning_environments`;
CREATE TABLE IF NOT EXISTS `glueps_learning_environments` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `accessLocation` varchar(255) NOT NULL,
  `userid` varchar(255) NOT NULL,
  `creduser` varchar(255) NOT NULL default '',
  `credsecret`varchar(255) NOT NULL default '',
  `installation` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `glueps_learning_environments_installations`
--

DROP TABLE IF EXISTS `glueps_learning_environments_installations`;
CREATE TABLE IF NOT EXISTS `glueps_learning_environments_installations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `accessLocation` varchar(255) NOT NULL,
  `PARAMETERS` varchar(4096) default '',
  `sectype` bigint(20) not NULL default 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

-- --------------------------------------------------------

--
-- Table structure for table `glueps_sectokens`
--
DROP TABLE IF EXISTS `glueps_sectokens`;
CREATE TABLE IF NOT EXISTS `glueps_sectokens` (
  `id` varchar(255) NOT NULL,
  `sectoken` varchar(120) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------


--
-- Table structure for table `glueps_users`
--

DROP TABLE IF EXISTS `glueps_users`;
CREATE TABLE IF NOT EXISTS `glueps_users` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LOGIN` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------


--
-- Table structure for table `glueps_oauth_tokens`
--

DROP TABLE IF EXISTS `glueps_oauth_tokens`;
CREATE TABLE IF NOT EXISTS `glueps_oauth_tokens` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `leid` bigint(20) NOT NULL,
  `accessToken` varchar(255) NOT NULL,
  `tokenType` varchar(255) DEFAULT NULL,
  `created` bigint(10) NOT NULL,
  `expiration`bigint(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FOREIGN KEY (`leid`) REFERENCES `glueps_learning_environments`(`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
