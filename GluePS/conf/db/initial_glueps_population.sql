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

-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 19, 2012 at 08:49 PM
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

--
-- Dumping data for table `glueps_learning_environments_installations`
--

INSERT INTO `glueps_learning_environments_installations` (`id`, `name`, `type`, `accessLocation`) VALUES
(1, 'Test Moodle installation in the cloud', 'Moodle', 'http://nikos.tel.uva.es/innpacto/moodle19/'),
(2, 'Workshop Test Moodle installation in the cloud', 'Moodle', 'http://www.gsic.uva.es/glue/moodle/'),
(3, 'Wiki+GLUE de TIC 2.0', 'MediaWiki', 'http://www.gsic.uva.es/TIC2/'),
(4, 'Moodle + GLUE en pandora', 'Moodle', 'http://pandora.tel.uva.es/pruebamoodle/'),
(5, 'MediaWiki en delfos', 'MediaWiki', 'http://delfos.tel.uva.es/wikis/juaase/'),
(6, 'MediaWiki + GLUE en la nube', 'MediaWiki', 'http://www.gsic.uva.es/glue/wiki/'),
(7, 'MediaWiki + GLUE del MASUP', 'MediaWiki', 'http://www.gsic.uva.es/wikis/MASUP/'),
(8, 'Buendia workshops Moodle', 'Moodle', 'http://nikos.tel.uva.es/taller/moodle/'),
(9, 'MediaWiki + GLUE en pandora', 'MediaWiki', 'http://pandora.tel.uva.es/wikis/gluetool/'),
(10, 'GSIC Email server', 'Email', 'http://gsic.uva.es:465');

--
-- Dumping data for table `glueps_users`
--

INSERT INTO `glueps_users` (`ID`, `LOGIN`, `PASSWORD`) VALUES
(1, 'lprisan', 'lprisan');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
