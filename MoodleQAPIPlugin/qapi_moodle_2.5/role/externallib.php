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

/**
 * Role External Library
 *
 * @package GlueServer
 * @subpackage Role
 * @author Javier Enrique Hoyos Torio
 *
 */
 
if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS,$CFG;
require_once($GWS->gwsroot . '/role/role.class.php');
require_once($GWS->gwsroot . '/role/db/roleDB.php');
require_once($CFG->libdir . '/externallib.php');

class glueserver_role_external extends external_api{
	
	 /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
	public static function get_roles_userid_parameters(){
        return new external_function_parameters(
            array(
				'userid'    => new external_value(PARAM_INT, 'user ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
				'courseid'	=> new external_value(PARAM_INT, 'course ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get list of roles of a user in a course
     *
     * @return user details
     */
    public static function get_roles_userid($userid, $courseid){
        $roles = glueserver_role_db::glueserver_get_roles_userid($userid, $courseid);
        $returns = array();
        foreach ($roles as $role) {
            $role = new glueserver_role($role);
            $returns[] = $role->get_data();
        }
        return $returns;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_roles_userid_returns() {
        return new external_multiple_structure(
            glueserver_role::get_class_structure()
        );
    }
    
    
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
	public static function get_global_roles_username_parameters(){
        return new external_function_parameters(
            array(
				'username'    => new external_value(PARAM_RAW, 'user name', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of global roles of a user
     *
     * @return user details
     */
    public static function get_global_roles_username($username){
        $roles = glueserver_role_db::glueserver_get_global_roles_username($username);
        $returns = array();
        foreach ($roles as $role) {
            $role = new glueserver_role($role);
            $returns[] = $role->get_data();
        }
        return $returns;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_global_roles_username_returns() {
        return new external_multiple_structure(
        		glueserver_role::get_class_structure()
        );
    }
	
}

?>
