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
 * Page Class
 *
 * @package GlueServer
 * @subpackage Page
 *
 * @author Javier Enrique Hoyos Torio
 */
class glueserver_page extends ServiceExternalObject{

    function __construct($pagerecord) {
        parent::__construct($pagerecord);
    }

    public static function get_class_structure(){
        return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT, 'ID of the page', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'course'		=> new external_value(PARAM_INT, 'id the course', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_TEXT,'The page name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'intro'   		=> new external_value(PARAM_RAW, 'intro', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'introformat'   => new external_value(PARAM_INT,'introformat', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
				'content'		=> new external_value(PARAM_RAW, 'content', VALUE_DEFAULT, NULL, NULL_ALLOWED),
				'contentformat'	=> new external_value(PARAM_INT, 'content format', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
				'display'		=> new external_value(PARAM_INT, 'display', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
				'displayoptions'=> new external_value(PARAM_RAW, 'displayoptions', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'timemodified' 	=> new external_value(PARAM_INT, 'timemodified', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            )
        );
    }

}