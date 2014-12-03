--
-- Script only to update an old existing glueps database  
-- If you have just executed the create_glueps_database.sql script you do not need to execute this script

--
-- Add new columns for table `glueps_deploys`
--
ALTER TABLE `glueps_deploys` add `NAME` varchar(255) NOT NULL;
ALTER TABLE `glueps_deploys` add `COMPLETE` TINYINT(1) NOT NULL DEFAULT '0';
ALTER TABLE `glueps_deploys` add `STATIC` varchar(255);
ALTER TABLE `glueps_deploys` add `LIVE` varchar(255);

--
-- Add new columns for table `glueps_designs`
--
ALTER TABLE `glueps_designs` add `NAME` varchar(255) NOT NULL;
ALTER TABLE `glueps_designs` add `TYPE` varchar(255) NOT NULL;
--
-- Add new column for table `glueps_learning_environment_installations`
--
ALTER TABLE `glueps_learning_environments_installations` add `PARAMETERS` varchar(4096) default '';
--
-- default values for the columns creduser and credsecret of the table `glueps_learning_environments`
--
ALTER TABLE  `glueps_learning_environments` change  `creduser`  `creduser` VARCHAR(255) NOT NULL default '';
ALTER TABLE  `glueps_learning_environments` change  `credsecret`  `credsecret` VARCHAR(255) NOT NULL default '';
--
-- Add a new column for the installation of each learning environment
-- Add a new column for the security types of the learning environment installations 
--
ALTER TABLE `glueps_learning_environments` add `installation` bigint(20) NOT NULL;
ALTER TABLE `glueps_learning_environments_installations` add `sectype` bigint(20) not NULL default 1;

--
-- Add a new column 'showAR' for enabling/disabling the AR display options in the frontend
--
ALTER TABLE `glueps_learning_environments` add `showAR` TINYINT(1) NOT NULL DEFAULT '0';
--
-- Add a new column 'showVG' for enabling/disabling the VG display options in the frontend
--
ALTER TABLE `glueps_learning_environments` add `showVG` TINYINT(1) NOT NULL DEFAULT '0';

ALTER TABLE `glueps_learning_environments` drop `type`;
ALTER TABLE `glueps_learning_environments` drop `accessLocation`;

ALTER TABLE `glueps_learning_environments_installations` add `leType` bigint(20) not NULL;
ALTER TABLE `glueps_learning_environments_installations` drop `type`;

-- --------------------------------------------------------


--
-- Table structure for table `glueps_asynchronous_operations`
--

DROP TABLE IF EXISTS `glueps_asynchronous_operations`;
CREATE TABLE IF NOT EXISTS `glueps_asynchronous_operations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operation` varchar(255) NOT NULL UNIQUE,
  `status` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `resource` varchar(255) DEFAULT NULL,
  `started`  timestamp DEFAULT CURRENT_TIMESTAMP,
  `finished`  timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

ALTER TABLE `glueps_asynchronous_operations` add `file` longblob;

ALTER TABLE `glueps_deploy_versions` add `SAVED` TINYINT(1) NOT NULL DEFAULT '0';