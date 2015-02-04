<?php
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
class glueserver_role extends ServiceExternalObject{

    function __construct($rolerecord) {
        parent::__construct($rolerecord);
    }

    public static function get_class_structure(){
    	return new external_single_structure(
            array(
                'id'            => new external_value(PARAM_INT,    'ID of the role', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'			=> new external_value(PARAM_RAW,	'name of the role', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'shortname'		=> new external_value(PARAM_RAW,	'short name of the role', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'description'   => new external_value(PARAM_RAW,	'description of the role', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'sortorder'		=> new external_value(PARAM_INT,    'value used to order the role in the list', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            )
        );
	}
}
?>