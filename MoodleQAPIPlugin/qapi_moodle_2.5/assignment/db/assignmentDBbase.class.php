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
 * Assignment Database Library Class
 *
 * @package GlueServer
 * @subpackage Assignment
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_assignment_db_base {
     /**
     * Returns an array of the assignments of the course
     *	@param integer the course id
     *
     * @return array of assignments in the course
     */
    public static function glueserver_get_assignments_course($courseid){
		global $DB,$CFG;
        
        $sql = "SELECT assignment.*
                FROM {$CFG->prefix}assignment assignment
                WHERE assignment.course = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
    
     /**
     * Returns the assignment info
     *	@param assignmentid int the assignment id
     *	@param courseid int the courseid
     *
     * @return array containing the assignment info
     */
    public static function glueserver_get_assignment_course($assignmentid, $courseid){
		global $DB,$CFG;
        
        $sql = "SELECT assignment.*
                FROM {$CFG->prefix}assignment assignment
                WHERE assignment.id = ? AND course = ?";
        $sqlparams = array($assignmentid, $courseid);
        return $DB->get_records_sql($sql, $sqlparams);	
    }
    
     /**
     * Delete a assignment from the course
     *  @param assignmentid integer the assignment id
     *	@param courseid integer the course id
     *
     * @return array of urls in the course
     */
    public static function glueserver_delete_assignment_course($assignmentid, $courseid){
		global $DB,$CFG;
        $sqlparams = array($assignmentid, $courseid);
        if (count(glueserver_assignment_db::glueserver_get_assignment_course($assignmentid, $courseid))==1){
        	$DB->delete_records_select("assignment", "id = ? AND course = ?", $sqlparams);
        	//the previous functions always returns true, so we make sure that the assignment has been really deleted
        	if(count(glueserver_assignment_db::glueserver_get_assignment_course($assignmentid, $courseid))==0){
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        } 
    }
    
     /**
     * Intert a assignment into the course
     * @return the url id
     */
    public static function glueserver_insert_assignment_course($course,$name,$intro,$introformat,$assignmenttype,$resubmit,$preventlate,$emailteachers,$grade,$maxbytes){
    	global $DB,$CFG;
    		
    	$record = new stdClass();
		$record->course = $course;
		$record->name = $name;
		$record->intro = $intro;
		$record->introformat = $introformat;
		$record->assignmenttype = $assignmenttype;
		$record->resubmit = $resubmit;
		$record->preventlate = $preventlate;
		$record->emailteachers = $emailteachers;
		$record->grade = $grade;
		$record->maxbytes = $maxbytes;
		$record->timemodified = time();
		$return_id = $DB->insert_record("assignment", $record, true);
        return $return_id;
    }
}