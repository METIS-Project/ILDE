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
 * Course Database Library Class
 *
 * @package GlueServer
 * @subpackage Course
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_course_db_base {
    
     /**
     * Returns an array of the courses in Moodle
     *
     *
     * @return array of courses created in Moodle
     */
    public static function glueserver_get_all_courses(){
        global $DB,$CFG;
        //c.sortorder != '1' is used not to include the general course of Moodle
        $sql = "SELECT c.*
        		FROM {$CFG->prefix}course c
        		WHERE c.sortorder !='1'";
        $sqlparams = array();
        return $DB->get_records_sql($sql, $sqlparams);
    }
    
     /**
     * Returns an array of the courses where the user is enrolled
     *	@param integer the user id
     *  @param integer the role of the user in the course
     *
     * @return array of courses where the user is enrolled
     */
    public static function glueserver_get_courses_userid($userid,$roleid){
		global $DB,$CFG;
		
		if (isset($roleid)){
			$where_role = " AND r.id =$roleid"; 
		}else{
			$where_role = "";
		}
        
		//cx.contextlevel = 50 is used to get only the courses
		//c.sortorder != '1' is used not to include the general course of Moodle
        $sql = "SELECT c.*
                FROM {$CFG->prefix}course c
                INNER JOIN {$CFG->prefix}context cx ON cx.instanceid = c.id AND cx.contextlevel = '50' AND c.sortorder != '1'
                INNER JOIN {$CFG->prefix}role_assignments ra ON ra.contextid = cx.id
                INNER JOIN {$CFG->prefix}user usr ON usr.id = ra.userid
                INNER JOIN {$CFG->prefix}role r ON ra.roleid = r.id
                WHERE usr.id = ?"
                .$where_role;
        $sqlparams = array($userid);
        return $DB->get_records_sql($sql, $sqlparams);
		
    }
    
         /**
     * Returns an array of the courses where the username is enrolled with a role
     *	@param integer the user name
     *  @param integer the role of the user in the course
     *
     * @return array of courses where the user is enrolled
     */
    public static function glueserver_get_courses_username($username,$roleid){
		global $DB,$CFG;
		
		if (isset($roleid)){
			$where_role = " AND r.id =$roleid"; 
		}else{
			$where_role = "";
		}
        
		//cx.contextlevel = 50 is used to get only the courses
		//c.sortorder != '1' is used not to include the general course of Moodle
        $sql = "SELECT c.*
                FROM {$CFG->prefix}course c
                INNER JOIN {$CFG->prefix}context cx ON cx.instanceid = c.id AND cx.contextlevel = '50' AND c.sortorder != '1'
                INNER JOIN {$CFG->prefix}role_assignments ra ON ra.contextid = cx.id
                INNER JOIN {$CFG->prefix}user usr ON usr.id = ra.userid
                INNER JOIN {$CFG->prefix}role r ON ra.roleid = r.id
                WHERE usr.username = ?"
                .$where_role;
        $sqlparams = array($username);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
     /**
     * Returns an array of the courses in which the user is enrolled (with or without a role)
     *	@param integer the user name
     *
     * @return array of courses where the user is enrolled
     */
    public static function glueserver_get_courses_enrolled_username($username){
		global $DB,$CFG;
        $sql = "SELECT c.*
                FROM {$CFG->prefix}course c
                INNER JOIN {$CFG->prefix}enrol e ON e.courseid = c.id
                INNER JOIN {$CFG->prefix}user_enrolments ue ON ue.enrolid = e.id
                INNER JOIN {$CFG->prefix}user u ON u.id = ue.userid
                WHERE u.username = ?";
        $sqlparams = array($username);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
    
  	 /**
     * Returns an array of the sections of the course
     *	@param integer the courseid id
     *
     * @return array of sections in the course
     */
    public static function glueserver_get_sections_course($courseid){
		global $DB,$CFG;
        
        $sql = "SELECT cs.*
                FROM {$CFG->prefix}course_sections cs
                WHERE cs.course = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
    
     /**
     * Returns the module info
     *	@param integer the module id
     *
     * @return array containing the module info
     */
    public static function glueserver_get_module($moduleid){
		global $DB,$CFG;
        
        $sql = "SELECT cm.*
                FROM {$CFG->prefix}course_modules cm
                WHERE cm.id = ?";
        $sqlparams = array($moduleid);
        return $DB->get_records_sql($sql, $sqlparams);	
    }
    
     /**
     * Returns an array of the modules of the course
     *	@param integer the course id
     *	@param integer the module type id
     *
     * @return array of sections in the course
     */
    public static function glueserver_get_modules_course($courseid, $module){
		global $DB,$CFG;
		
		if (isset($module)){
			$where_module = " AND cm.module =$module"; 
		}else{
			$where_module = "";
		}
        
        $sql = "SELECT cm.*
                FROM {$CFG->prefix}course_modules cm
                WHERE cm.course = ?"
                .$where_module;
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
     /**
     * Update the module info
     *	@param array the module info
     *
     * @return array containing the module info
     */
    public static function glueserver_update_module($module){
		global $DB,$CFG;
        
		$return_value = $DB->update_record("course_modules", $module, false);
        return $return_value;
    }
    
    /**
     * Delete a module from the course
     *  @param integer the module id
     *	@param integer the course id
     *
     * @return array of urls in the course
     */
    public static function glueserver_delete_module($moduleid, $courseid){
		global $DB,$CFG;
        if (count(glueserver_course_db::glueserver_get_module($moduleid))==1){
        	$sqlparams = array($moduleid, $courseid);
        	$DB->delete_records_select("course_modules", "id = ? AND course = ?", $sqlparams);
        	//the previous functions always returns true, so we make sure that the module has been really deleted
        	if(count(glueserver_course_db::glueserver_get_module($moduleid))==0){
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        } 
    }
           
     /**
     * Update the section info
     *	@param array the section info
     *
     * @return array containing the section info
     */
    public static function glueserver_update_section($section){
		global $DB,$CFG;
        
		$return_value = $DB->update_record("course_sections", $section, false);
        return $return_value;
    }
    
     /**
     * Insert a module into the course
     * @return the module id
     */
    public static function glueserver_insert_module($course,$module,$instance,$section,$visible,$groupmode,$groupingid,$groupmembersonly){
    	global $DB,$CFG;
    		
    	$record = new stdClass();
		$record->course = $course;
		$record->module = $module;
		$record->instance = $instance;
		$record->section = $section;
		$record->idnumber = "";
		$record->added = time();
		$record->visible = $visible;
		$record->groupmode = $groupmode;
		$record->groupingid = $groupingid;
		$record->groupmembersonly = $groupmembersonly;
		$return_id = $DB->insert_record("course_modules", $record, true);
        return $return_id;
    }
    
    
         /**
     * Returns the context of the course
     *	@param courseid integer the course id
     *
     * @return the context info
     */
    public static function glueserver_get_course_context($courseid){
		global $DB,$CFG;
		//cx.contextlevel = 50 is used to get only the courses
        $sql = "SELECT context.*
                FROM {$CFG->prefix}context context
                WHERE context.instanceid = ? AND context.contextlevel = 50";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);
    }

}