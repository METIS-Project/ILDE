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
-- Allow null values for the columns creduser and credsecret of the table `glueps_learning_environments`
--
ALTER TABLE  `glueps_learning_environments` change  `creduser`  `creduser` VARCHAR(255) NOT NULL default '';
ALTER TABLE  `glueps_learning_environments` change  `credsecret`  `credsecret` VARCHAR(255) NOT NULL default '';
