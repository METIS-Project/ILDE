<?php
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

require_once(dirname(dirname(__FILE__)).'/config.php');
global $GWS;
require_once($GWS->gwsroot.'/lib/serviceExternalObject.class.php');

/**
 * Label Class
 *
 * @package GlueServer
 * @subpackage label
 *
 * @author Javier Enrique Hoyos Torio
 */
class glueserver_label extends ServiceExternalObject{

    function __construct($labelrecord) {
        parent::__construct($labelrecord);
    }

    public static function get_class_structure(){
        return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT, 'ID of the label', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'course'		=> new external_value(PARAM_INT, 'id of the course', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_RAW, 'The name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'intro'      	=> new external_value(PARAM_RAW,'Intro', VALUE_REQUIRED, null, NULL_ALLOWED),
                'introformat'   => new external_value(PARAM_INT, 'introformat', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
                'timemodified'  => new external_value(PARAM_INT, 'timemodified', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            )
        );
    }

}