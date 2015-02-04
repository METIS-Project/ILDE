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

/**
 * User External Library
 *
 * @package GlueServer
 * @subpackage User
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once("$GWS->gwsroot/user/user.class.php");
require_once("$GWS->gwsroot/user/db/userDB.php");
require_once("$GWS->gwsroot/role/role.class.php");
require_once("$GWS->gwsroot/role/db/roleDB.php");
require_once("$CFG->libdir/externallib.php");

class glueserver_user_external extends external_api {
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_all_users_parameters() {
        return new external_function_parameters(
            array(
				//if I had any parameters, they would be described here. But I don't have any, so this array is empty.
            )
        );
    }
    
        /**
     *
     * Function to get list of users enrolled in any course
     *
     * @return user details
     */
    public static function get_all_users() {
        $users = glueserver_user_db::glueserver_get_all_users();
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
    public static function get_all_users_returns() {
        return new external_multiple_structure(
            glueserver_user::get_class_structure()
        );
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_users_courseid_parameters() {
        return new external_function_parameters(
            array(
				'courseid'    => new external_value(PARAM_INT, 'course ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of users enrolled in a course
     * @param integer courseid the course id
     * @return user details
     */
    public static function get_users_courseid($courseid) {
        $users = glueserver_user_db::glueserver_get_users_courseid($courseid);
        $returns = array();
        $i = 0;
        foreach ($users as $user) {
        	if (strcmp($user["user"]->username, "guest")!==0){
	            $server_user = new glueserver_user($user["user"]);
	            $returns[$i]["user"] = $server_user->get_data();
	            $returns[$i]["role"] = array();
	            if (isset($user["role"])){
		            foreach ($user["role"] as $role){
			            $server_role = new glueserver_role($role);
			            $returns[$i]["role"][] = $server_role->get_data();
		            }
	            }
	            $i++;
        	}
        }
        return $returns;
    }
    
         /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_users_courseid_returns() {
        return new external_multiple_structure(
            //glueserver_user::get_class_structure()
            new external_single_structure(
            	array(
					"user" => glueserver_user::get_class_structure(),
					"role" => new external_multiple_structure(glueserver_role::get_class_structure())
            	)
        	)
        );
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function check_user_credentials_parameters() {
        return new external_function_parameters(
            array(
				'username'    => new external_value(PARAM_RAW, 'the username in Moodle', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
				'password'    => new external_value(PARAM_RAW, 'the password of the user in Moodle', VALUE_REQUIRED, '', NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to check the credentials of a user in Moodle
     * @param string username the name of the user in Moodle
     * @param string password the password of the user in Moodle
     * @return user details if the credentials provided are correct
     */
    public static function check_user_credentials($username, $password) {
        $users = glueserver_user_db::glueserver_check_user_credentials($username, $password);
        $returns = array();
        foreach ($users as $u) {
            $server_user = new glueserver_user($u);
            $returns[] = $server_user->get_data();
        }
        return $returns;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function check_user_credentials_returns() {
        return new external_multiple_structure(
            glueserver_user::get_class_structure()
        );
    }
    
    
}
