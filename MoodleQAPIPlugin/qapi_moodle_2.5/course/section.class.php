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
class glueserver_section extends ServiceExternalObject{

    function __construct($sectionrecord) {
        parent::__construct($sectionrecord);
    }

    public static function get_class_structure() {
        return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT, 'section id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'course'     	=> new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'section'      	=> new external_value(PARAM_INT, 'section number', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_RAW, 'full name of the section', VALUE_REQUIRED, null, NULL_ALLOWED),
                'summary'     	=> new external_value(PARAM_RAW,'summary of the section', VALUE_REQUIRED, null, NULL_ALLOWED),
                'summaryformat' => new external_value(PARAM_INT, 'summary format', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'sequence'      => new external_value(PARAM_TEXT,'sequence of modules', VALUE_DEFAULT, null, NULL_ALLOWED),
                'visible'     	=> new external_value(PARAM_INT, 'visible', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
            ), 'A section in a Moodle\'s course'
        );
    }

}