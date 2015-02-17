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
 * Assignment External Library
 *
 * @package GlueServer
 * @subpackage Assignment
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once("$GWS->gwsroot/assignment/assignment.class.php");
require_once("$GWS->gwsroot/assignment/db/assignmentDB.php");
require_once("$CFG->libdir/externallib.php");

class glueserver_assignment_external extends external_api {

     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_assignments_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of assignments in a course
     *
     * @return details of the assignments
     */
    public static function get_assignments_course($courseid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $assignments = glueserver_assignment_db::glueserver_get_assignments_course($courseid);
        $returns = array();
        foreach ($assignments as $assignment) {
            $assignment = new glueserver_assignment($assignment);
            $returns[] = $assignment->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_assignments_course_returns() {
        return new external_multiple_structure(
            glueserver_assignment::get_class_structure()
        );
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function delete_assignment_course_parameters() {
        return new external_function_parameters(
            array(
            	'assignments' => new external_multiple_structure(
                 	new external_single_structure(
                 	array(
						'assignmentid' => new external_value(PARAM_INT, 'assignment id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
						'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        ))));
    }
    
     /**
     *
     * Function to delete a assignment in a course
     *
     * @return details of the assignments
     */
    public static function delete_assignment_course($assignments) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $success = 1;
        foreach ($assignments as $assignment){
        	$assignmentid = $assignment["assignmentid"];
        	$courseid = $assignment["courseid"];
        	$result = glueserver_assignment_db::glueserver_delete_assignment_course($assignmentid, $courseid);
        	if ($result == 0){
        		$success = $result;
        	}
        }

		return array ('success' => $success);
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function delete_assignment_course_returns() {
    	return new external_single_structure(
         	array(
             'success' => new external_value(PARAM_BOOL, 'Returns true on success', VALUE_REQUIRED, '', NULL_NOT_ALLOWED)
         	)
     	);
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function insert_assignment_course_parameters() {
        return new external_function_parameters(
            array(
                'course'		=> new external_value(PARAM_INT, 'id the course', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_TEXT,'The assignment name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'intro'   		=> new external_value(PARAM_RAW, 'intro', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'introformat'   => new external_value(PARAM_INT,'introformat', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'assignmenttype'=> new external_value(PARAM_TEXT, 'The assignment type', VALUE_DEFAULT, 'uploadsingle', NULL_NOT_ALLOWED),
				'resubmit'		=> new external_value(PARAM_INT,'resubmit', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
				'preventlate'	=> new external_value(PARAM_INT,'preventlate', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
				'emailteachers'	=> new external_value(PARAM_INT,'emailteachers', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'grade'   	 	=> new external_value(PARAM_INT,'grade', VALUE_DEFAULT, 100, NULL_NOT_ALLOWED),
                'maxbytes'   	=> new external_value(PARAM_INT,'maxbytes', VALUE_DEFAULT, 1048576, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to insert a assignment in a course
     *
     * @return assignment details
     */
    public static function insert_assignment_course($course,$name,$intro,$introformat,$assignmenttype,$resubmit,$preventlate,$emailteachers,$grade,$maxbytes) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $result = glueserver_assignment_db::glueserver_insert_assignment_course($course,$name,$intro,$introformat,$assignmenttype,$resubmit,$preventlate,$emailteachers,$grade,$maxbytes);
		return array ('assignmentid' => $result);
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function insert_assignment_course_returns() {
    	return new external_single_structure(
         	array(
             	"assignmentid" => new external_value(PARAM_INT, 'The id of the assignment just created', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
         	)
     	);
    }
    
}
