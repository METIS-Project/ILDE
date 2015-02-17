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
 * Group External Library
 *
 * @package GlueServer
 * @subpackage Group
 * @author Javier Enrique Hoyos Torio
 *
 */
 
if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS,$CFG;
require_once($GWS->gwsroot . '/group/group.class.php');
require_once($GWS->gwsroot . '/group/db/groupDB.php');
require_once($GWS->gwsroot . '/user/user.class.php');
require_once($CFG->libdir . '/externallib.php');

class glueserver_group_external extends external_api{
	
	 /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
	public static function get_groups_courseid_parameters(){
        return new external_function_parameters(
            array(
				'courseid'	=> new external_value(PARAM_INT, 'course ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of groups in a course
     * @param integer courseid the course id
     * @return course details
     */
    public static function get_groups_courseid($courseid){
        $groups = glueserver_group_db::glueserver_get_groups_courseid($courseid);
        $returns = array();
        foreach ($groups as $group) {
            $group = new glueserver_group($group);
            $returns[] = $group->get_data();
        }
        return $returns;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_groups_courseid_returns() {
        return new external_multiple_structure(
            glueserver_group::get_class_structure()
        );
    }
    
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
	public static function get_users_groupid_parameters(){
        return new external_function_parameters(
            array(
				'groupid'	=> new external_value(PARAM_INT, 'group ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of users in a group
     * @param integer groupid the group id
     * @return array of users in a group
     */
    public static function get_users_groupid($groupid){
        $users = glueserver_group_db::glueserver_get_users_groupid($groupid);
        $returns = array();
        foreach ($users as $user) {
            $user = new glueserver_user($user);
            $returns[] = $user->get_data();
        }
        return $returns;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_users_groupid_returns() {
        return new external_multiple_structure(
            glueserver_user::get_class_structure()
        );
    }
    
         /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
	public static function get_users_courseid_parameters(){
        return new external_function_parameters(
            array(
				'courseid'	=> new external_value(PARAM_INT, 'course ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of users in a group
     * @param integer groupid the group id
     * @return array of users in a group
     */
    public static function get_users_courseid($courseid){
        $groups = glueserver_group_db::glueserver_get_users_courseid($courseid);
        $returns = array();
        $i = 0;
        foreach ($groups as $group) {
            $server_group = new glueserver_group($group["group"]);
            $returns[$i]["group"] = $server_group->get_data();
            $returns[$i]["user"] = array();
            if (isset($group["user"])){
	            foreach ($group["user"] as $user){
		            $server_user = new glueserver_user($user);
		            $returns[$i]["user"][] = $server_user->get_data();
	            }
            }
            $i++;
        }       
        return $returns;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_users_courseid_returns() {
        return new external_multiple_structure(
            new external_single_structure(
            	array(
					"group" => glueserver_group::get_class_structure(),
					"user" => new external_multiple_structure(glueserver_user::get_class_structure())
            	)
        	)
        );
    }
	
}

?>
