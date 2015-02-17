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
 * Module Class
 *
 * @package GlueServer
 * @subpackage Course
 *
 * @author Javier Enrique Hoyos Torio
 */
class glueserver_module extends ServiceExternalObject{

    function __construct($modulerecord) {
        parent::__construct($modulerecord);
    }

    public static function get_class_structure() {
        return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT, 'module id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'course'     	=> new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'module'      	=> new external_value(PARAM_INT, 'module', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'instance'      => new external_value(PARAM_INT, 'instance', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'section'      	=> new external_value(PARAM_INT, 'section', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'visible'     	=> new external_value(PARAM_INT, 'visible', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
                'groupmode'     	=> new external_value(PARAM_INT, 'groupmode', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'groupingid'     	=> new external_value(PARAM_INT, 'groupingid', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'groupmembersonly'  => new external_value(PARAM_INT, 'groupmembersonly', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            ), 'A module in a section of a Moodle\'s course'
        );
    }

}