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
global $GWS;
require_once($GWS->gwsroot.'/lib/serviceExternalObject.class.php');

/**
 * Course Class
 *
 * @package GlueServer
 * @subpackage Course
 *
 * @author Javier Enrique Hoyos Torio
 */
class glueserver_course extends ServiceExternalObject{

    function __construct($courserecord) {
        parent::__construct($courserecord);
    }

    public static function get_class_structure() {
        return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT,            'course id number', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'idnumber'      => new external_value(PARAM_RAW,            'id number', VALUE_OPTIONAL, '', NULL_NOT_ALLOWED),
                'category'      => new external_value(PARAM_INT,            'course category id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'fullname'      => new external_value(PARAM_TEXT,           'full name of the course', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'shortname'     => new external_value(PARAM_TEXT,           'short name of the course', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'summary'       => new external_value(PARAM_RAW,            'course description', VALUE_OPTIONAL, null, NULL_ALLOWED),
                'format'        => new external_value(PARAM_ALPHANUMEXT,    'course format: weeks, topics, social, site,..', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
                'startdate'     => new external_value(PARAM_INT,            'timestamp for course start', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'groupmode'     => new external_value(PARAM_INT,            'no group, separate, visible', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'lang'          => new external_value(PARAM_ALPHANUMEXT,    'forced course language', VALUE_OPTIONAL, '', NULL_NOT_ALLOWED),
                'timecreated'   => new external_value(PARAM_INT,            'timestamp of course creation', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'timemodified'  => new external_value(PARAM_INT,            'timestamp of course last modification', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'showgrades'    => new external_value(PARAM_INT,            '1 if grades are shown, otherwise 0', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
                //'numsections'   => new external_value(PARAM_INT, 			'the number of sections in the course', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
                'visible' 		=> new external_value(PARAM_INT, 			'1 if the course is shown, otherwise 0', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
                'sortorder'		=> new external_value(PARAM_INT,			'value used to order the course in the list', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            ), 'Course'
        );
    }

}