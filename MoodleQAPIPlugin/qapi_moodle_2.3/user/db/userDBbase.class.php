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
 * User Database Library Class
 *
 * @package GlueServer
 * @subpackage User
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_user_db_base {
     /**
     * Returns an array of the users in Moodle
     *
     *
     * @return array of users registered in Moodle
     */
    public static function glueserver_get_all_users(){
        global $DB;
        return $DB->get_records('user', array());
    }
    
     /**
     * Returns an array of the users and their roles in the course
     *
     *
     * @return array of users registered in Moodle
     */
    public static function glueserver_get_users_courseid($courseid){
		global $DB,$CFG;
		//cx.contextlevel = 50 is used to get only the courses
		//c.sortorder != '1' is used not to include the general course of Moodle
        $sql = "SELECT usr.*, r.id as r_id, r.name as r_name, r.shortname as r_shortname, r.description as r_description, r.sortorder as r_sortorder
                FROM {$CFG->prefix}user usr
                INNER JOIN {$CFG->prefix}role_assignments ra ON ra.userid = usr.id
                INNER JOIN {$CFG->prefix}context cx ON cx.id = ra.contextid AND cx.contextlevel = '50'
                INNER JOIN {$CFG->prefix}course c ON c.id = cx.instanceid
                INNER JOIN {$CFG->prefix}role r ON r.id = ra.roleid
                WHERE c.id = ?
                	AND usr.deleted = 0
                ORDER BY usr.id, r_id";
        $sqlparams = array($courseid);
        $records = $DB->get_recordset_sql($sql, $sqlparams);
        //$records = $DB->get_records_sql($sql, $sqlparams); use the previous function instead of this one because there could be several records with the same usr.id
        foreach($records as $record){
        	//Check if the user information is already stored
        	if (!isset($return_records[$record->id])){
        		$return_records[$record->id] = array();
        		$return_records[$record->id]["user"] = $record;
        	}
        	//Add the role information for the user (a user could have several roles in a course)
        	$role = new stdClass();
        	$role->id = $record->r_id;
        	$role->name = $record->r_name;
        	$role->shortname = $record->r_shortname;
        	$role->description = $record->r_description;
        	$role->sortorder = $record->r_sortorder;
        	$return_records[$record->id]["role"][] = $role;
        }
        return $return_records;
    }
    
     /**
     * Returns an array with the user matching the provided credentials
     *
     *
     * @return array with the user matching the provided credentials. Otherwise, the array will be empty
     */
    public static function glueserver_check_user_credentials($username, $password){
		global $DB,$CFG;
		if (!isset($CFG->passwordsaltmain)){
			//Just in case, $CFG->passwordsaltmain should exist in the config.php file of the Moodle installation
			$encriptedPassword = $password;
		}else{
			$encriptedPassword = $password.$CFG->passwordsaltmain;
		}
		$sql = "SELECT usr.*
				FROM {$CFG->prefix}user usr
				WHERE usr.username=? ".
					"AND usr.password=MD5(?)";
        $sqlparams = array($username, $encriptedPassword);
        return $DB->get_records_sql($sql, $sqlparams);
    }
}