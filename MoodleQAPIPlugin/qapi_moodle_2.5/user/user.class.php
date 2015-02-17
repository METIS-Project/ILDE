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
 
// Moodle is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Moodle is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Moodle.  If not, see <http://www.gnu.org/licenses/>.

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once($GWS->gwsroot.'/lib/serviceExternalObject.class.php');

/**
 * User Class
 *
 * @package GlueServer
 * @subpackage User
 *
 * @author Javier Enrique Hoyos Torio
 */
class glueserver_user extends ServiceExternalObject{

    function __construct($userrecord) {
        parent::__construct($userrecord);
    }

    public static function get_class_structure(){
        return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT,    'ID of the user', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'auth'			=> new external_value(PARAM_RAW, 	'Authentication type', VALUE_REQUIRED, 'manual', NULL_NOT_ALLOWED),
                'username'      => new external_value(PARAM_RAW,    'Username policy is defined in Moodle security config', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'idnumber'      => new external_value(PARAM_RAW,    'An arbitrary ID code number perhaps from the institution', VALUE_OPTIONAL, '', NULL_NOT_ALLOWED),
                'firstname'     => new external_value(PARAM_TEXT,   'The first name of the user', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'lastname'      => new external_value(PARAM_TEXT,   'The family name of the user', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'email'         => new external_value(PARAM_TEXT,  'A valid and unique email address', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'city'          => new external_value(PARAM_TEXT,   'Home city of the user', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'country'       => new external_value(PARAM_TEXT,   'Home country code of the user, such as AU or CZ', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'lang'          => new external_value(PARAM_TEXT,   'Language code such as "en", must exist on server', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'timemodified'  => new external_value(PARAM_INT,    'Time of last modification in seconds', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'deleted'		=> new external_value(PARAM_INT,	'The user has been deleted or not', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'firstaccess'	=> new external_value(PARAM_INT,	'The first time the user logged in Moodle', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)   
            )
        );
    }
        /*public static function get_class_structure(){
        return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT,    'ID of the user', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'auth'			=> new external_value(PARAM_RAW, 	'Authentication type', VALUE_REQUIRED, 'manual', NULL_NOT_ALLOWED),
                'username'      => new external_value(PARAM_RAW,    'Username policy is defined in Moodle security config', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'idnumber'      => new external_value(PARAM_RAW,    'An arbitrary ID code number perhaps from the institution', VALUE_OPTIONAL, '', NULL_NOT_ALLOWED),
                'firstname'     => new external_value(PARAM_TEXT,   'The first name of the user', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'lastname'      => new external_value(PARAM_TEXT,   'The family name of the user', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'email'         => new external_value(PARAM_EMAIL,  'A valid and unique email address', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'city'          => new external_value(PARAM_TEXT,   'Home city of the user', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'country'       => new external_value(PARAM_TEXT,   'Home country code of the user, such as AU or CZ', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'lang'          => new external_value(PARAM_TEXT,   'Language code such as "en", must exist on server', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'timemodified'  => new external_value(PARAM_INT,    'Time of last modification in seconds', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'deleted'		=> new external_value(PARAM_INT,	'The user has been deleted or not', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'firstaccess'	=> new external_value(PARAM_INT,	'The first time the user logged in Moodle', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                
                'roles' 		=> new external_multiple_structure(
                					new external_single_structure(
                						array(
                							'courseid' => new external_value(PARAM_INT, 'id of course')
                						)
                					)
                				)  
            )
        );
    }*/

}