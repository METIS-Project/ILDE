/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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
