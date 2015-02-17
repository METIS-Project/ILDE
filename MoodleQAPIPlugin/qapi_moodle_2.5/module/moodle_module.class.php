<?php
/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of MoodleQAPIPlugin for Moodle 2.5.x
 * 
 * MoodleQAPIPlugin for Moodle 2.5.x is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * MoodleQAPIPlugin for Moodle 2.5.x is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/


if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once($GWS->gwsroot.'/lib/serviceExternalObject.class.php');

/**
 * Module Class
 *
 * @package GlueServer
 * @subpackage Moodle Module
 *
 * @author Javier Enrique Hoyos Torio
 */
class glueserver_moodle_module extends ServiceExternalObject{

    function __construct($modulerecord) {
        parent::__construct($modulerecord);
    }

    public static function get_class_structure(){
    	return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT,    'ID of the module', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'			=> new external_value(PARAM_RAW,	'name of the module', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'version'		=> new external_value(PARAM_INT,	'version of the module', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'cron'   		=> new external_value(PARAM_INT,	'cron', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'lastcron'		=> new external_value(PARAM_INT,    'lastcron', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'search'		=> new external_value(PARAM_RAW,    'search', VALUE_DEFAULT, '', NULL_NOT_ALLOWED),
                'visible'		=> new external_value(PARAM_INT, 	'1 if the module is shown, otherwise 0', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED)               
            )
        );
	}
}
?>