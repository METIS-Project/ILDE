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
 * Role Database Library Class
 *
 * @package GlueServer
 * @subpackage Enrol
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_enrol_db_base {
    
     /**
     * Returns an array of the enrols of the course
     *	@param integer the course id
     *
     * @return array of enrols in the course
     */
    public static function glueserver_get_enrols_course($courseid){
		global $DB,$CFG;
        
        $sql = "SELECT enrol.*
                FROM {$CFG->prefix}enrol enrol
                WHERE enrol.courseid = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
     /**
     * Returns the enrol info
     *	@param enrolid int the enrol id
     *	@param courseid int the courseid
     *
     * @return array containing the enrol info
     */
    public static function glueserver_get_enrol_course($enrolid, $courseid){
		global $DB,$CFG;
        
        $sql = "SELECT enrol.*
                FROM {$CFG->prefix}enrol enrol
                WHERE enrol.id = ? AND enrol.courseid = ?";
        $sqlparams = array($enrolid, $courseid);
        return $DB->get_records_sql($sql, $sqlparams);	
    }
    
     /**
     * Delete a enrol from the course
     *  @param integer the enrol id
     *	@param integer the course id
     *
     * @return array of urls in the course
     */
    public static function glueserver_delete_enrol_course($enrolid, $courseid){
		global $DB,$CFG;
        $sqlparams = array($enrolid, $courseid);
        if (count(glueserver_enrol_db::glueserver_get_enrol_course($enrolid, $courseid))==1){
        	$DB->delete_records_select("enrol", "id = ? AND courseid = ?", $sqlparams);
        	//the previous functions always returns true, so we make sure that the enrol has been really deleted
        	if(count(glueserver_enrol_db::glueserver_get_enrol_course($enrolid, $courseid))==0){
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        } 
    }
    
     /**
     * Returns an array of the roles of a user in a course
     *
     *
     * @return array of courses created in Moodle
     */
    public static function glueserver_set_enrol($enrol,$status,$courseid,$sortorder,$timecreated,$timemodified){
    	global $DB,$CFG;
    		
    	$record = new stdClass();
		$record->enrol = $enrol;
		$record->status = $status;
		$record->courseid = $courseid;
		$record->sortorder = $sortorder;
		$record->timecreated =time();
		$record->timemodified = $record->timecreated;
		$return_id = $DB->insert_record("enrol", $record, true);
        return $return_id;
    }
    
    public static function glueserver_disenroll_course_user($userid,$courseid,$enrolid){
    	global $DB,$CFG;
    	$result = glueserver_course_db_base::glueserver_get_course_context($courseid);
        foreach ($result as $c){
       		$contextid = $c->id;
        }
    	$sqlparams = array($contextid, $userid);
    	//delete the assignments of the user in the course (the roles of the user in the course)
        $DB->delete_records_select("role_assignments", "contextid = ? AND userid = ?", $sqlparams);
        $sqlparams = array($enrolid, $userid);
        //delete the enrolment of the user in the course (the user stop being a member of the course)
        $DB->delete_records_select("user_enrolments", "enrolid = ? AND userid = ?", $sqlparams);
        return true;
    }

}