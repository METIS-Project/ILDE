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
 
// This file is part of Moodbile -- http://moodbile.org
//
// Moodbile is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Moodbile is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Moodbile.  If not, see <http://www.gnu.org/licenses/>.

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once($GWS->gwsroot.'/lib/serviceExternalObject.class.php');

/**
 * Role Class
 *
 * @package GlueServer
 * @subpackage Role
 *
 * @author Javier Enrique Hoyos Torio
 */
class glueserver_enrol extends ServiceExternalObject{

    function __construct($rolerecord) {
        parent::__construct($rolerecord);
    }

    public static function get_class_structure(){
    	return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT,    'ID of the enrol', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'enrol'			=> new external_value(PARAM_RAW,	'name of the enrol', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'status'		=> new external_value(PARAM_INT,	'status', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'courseid'   	=> new external_value(PARAM_INT,	'course ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'sortorder'		=> new external_value(PARAM_INT,    'sortorder', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'timecreated'	=> new external_value(PARAM_INT,    'timecreated', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'timemodified'	=> new external_value(PARAM_INT,    'timemodified', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            ), 'Enrol'
        );
	}
}
?>