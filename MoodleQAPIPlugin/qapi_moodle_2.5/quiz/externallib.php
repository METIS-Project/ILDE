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
 * Quiz External Library
 *
 * @package GlueServer
 * @subpackage Quiz
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once("$GWS->gwsroot/quiz/quiz.class.php");
require_once("$GWS->gwsroot/quiz/db/quizDB.php");
require_once("$CFG->libdir/externallib.php");

class glueserver_quiz_external extends external_api {

     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_quizzes_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of quizzes in a course
     *
     * @return details of the quizzes
     */
    public static function get_quizzes_course($courseid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $quizzes = glueserver_quiz_db::glueserver_get_quizzes_course($courseid);
        $returns = array();
        foreach ($quizzes as $quiz) {
            $quiz = new glueserver_quiz($quiz);
            $returns[] = $quiz->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_quizzes_course_returns() {
        return new external_multiple_structure(
            glueserver_quiz::get_class_structure()
        );
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function delete_quiz_course_parameters() {
        return new external_function_parameters(
        	array(
            	'quizzes' => new external_multiple_structure(
                 	new external_single_structure(
                 		array(
							'quizid' => new external_value(PARAM_INT, 'quiz id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
							'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        ))));
    }
    
     /**
     *
     * Function to delete a quiz in a course
     *
     * @return details of the quizzes
     */
    public static function delete_quiz_course($quizzes) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $success = 1;
        foreach ($quizzes as $quiz){
        	$quizid = $quiz["quizid"];
        	$courseid = $quiz["courseid"];
        	        $result = glueserver_quiz_db::glueserver_delete_quiz_course($quizid, $courseid); 
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
    public static function delete_quiz_course_returns() {
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
    public static function insert_quiz_course_parameters() {
        return new external_function_parameters(
            array(
                'course'		=> new external_value(PARAM_INT, 'id the course', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_TEXT,'The quiz name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'intro'   		=> new external_value(PARAM_RAW, 'intro', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'introformat'   => new external_value(PARAM_INT,'introformat', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'timeopen'		=> new external_value(PARAM_INT, 'timeopen', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'timeclose'		=> new external_value(PARAM_INT, 'timeclose', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'preferredbehaviour'	=> new external_value(PARAM_TEXT,'preferredbehaviour', VALUE_DEFAULT, '', NULL_NOT_ALLOWED),
                'attempts'    	=> new external_value(PARAM_INT,'attempts', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to insert a quiz in a course
     *
     * @return quiz details
     */
    public static function insert_quiz_course($course,$name,$intro,$introformat,$timeopen,$timeclose,$preferredbehaviour,$attempts,$timemodified) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $result = glueserver_quiz_db::glueserver_insert_quiz_course($course,$name,$intro,$introformat,$timeopen,$timeclose,$preferredbehaviour,$attempts,$timemodified);
		return array ('quizid' => $result);
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function insert_quiz_course_returns() {
    	return new external_single_structure(
         	array(
             	"quizid" => new external_value(PARAM_INT, 'The id of the quiz just created', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
         	)
     	);
    }
    
}
