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
 * Role Database Library Class
 *
 * @package GlueServer
 * @subpackage Role
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_role_db_base {
    
     /**
     * Returns an array of the roles of a user in a course
     *
     *
     * @return array of courses created in Moodle
     */
    public static function glueserver_get_roles_userid($userid, $courseid){
        global $DB,$CFG;
        
		//cx.contextlevel = 50 is used to get only the courses
		//c.sortorder != '1' is used not to include the general course of Moodle
		//r.id = 1 is the manager rol in moodle 2.0 (admin role in moodle 1.9)
        $sql = "SELECT r.*
                FROM {$CFG->prefix}role r
                INNER JOIN {$CFG->prefix}role_assignments ra ON ra.roleid = r.id
                INNER JOIN {$CFG->prefix}context cx ON cx.id = ra.contextid AND cx.contextlevel = '50'
                INNER JOIN {$CFG->prefix}course c ON c.id = cx.instanceid AND c.sortorder != '1'
                INNER JOIN {$CFG->prefix}user usr ON usr.id = ra.userid
                WHERE usr.id = ? AND c.id = ?";
        $sqlparams = array($userid,$courseid);
        return $DB->get_records_sql($sql, $sqlparams);
    }
    
     /**
     * Returns an array of the global roles of a user
     *
     *
     * @return array of global roles of a user 
     */
    public static function glueserver_get_global_roles_username($username){
        global $DB,$CFG;
        
		//cx.contextlevel = 10 is used to get only the global roles
		//cx.instanceid = '0' since the role is not related to an specific course
        $sql = "SELECT r.*
                FROM {$CFG->prefix}role r
                INNER JOIN {$CFG->prefix}role_assignments ra ON ra.roleid = r.id
                INNER JOIN {$CFG->prefix}context cx ON cx.id = ra.contextid AND cx.contextlevel = '10' AND cx.instanceid = '0'
                INNER JOIN {$CFG->prefix}user usr ON usr.id = ra.userid
                WHERE usr.username = ?";
        $sqlparams = array($username);
        return $DB->get_records_sql($sql, $sqlparams);
    }

}