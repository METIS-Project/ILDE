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
 * Url Class
 *
 * @package GlueServer
 * @subpackage Forum
 *
 * @author Javier Enrique Hoyos Torio
 */
class glueserver_forum extends ServiceExternalObject{

    function __construct($forumrecord) {
        parent::__construct($forumrecord);
    }

    public static function get_class_structure(){
        return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT, 'ID of the forum', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'course'		=> new external_value(PARAM_INT, 'id the course', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'type'      	=> new external_value(PARAM_TEXT, 'The forum type', VALUE_DEFAULT, 'general', NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_TEXT,'The forum name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'intro'   		=> new external_value(PARAM_RAW, 'intro', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'introformat'   => new external_value(PARAM_INT,'introformat', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'assessed'		=> new external_value(PARAM_INT, 'assessed', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'assesstimestart'	=> new external_value(PARAM_INT, 'assesstimestart', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'assesstimefinish'	=> new external_value(PARAM_INT,'assesstimefinish', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'maxattachments'    => new external_value(PARAM_INT,'maxattachments', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
                'timemodified'  	=> new external_value(PARAM_INT, 'timemodified', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            )
        );
    }

}