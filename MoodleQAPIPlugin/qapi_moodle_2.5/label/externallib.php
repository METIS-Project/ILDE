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
 * Label External Library
 *
 * @package GlueServer
 * @subpackage label
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(dirname(__FILE__)).'/config.php');
global $GWS;
require_once("$GWS->gwsroot/label/label.class.php");
require_once("$GWS->gwsroot/label/db/labelDB.php");
require_once("$CFG->libdir/externallib.php");

class glueserver_label_external extends external_api {

     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_labels_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of labels in a course
     * @param int $courseid The identifier of the course in Moodle
     * @return array details of the labels
     */
    public static function get_labels_course($courseid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $labels = glueserver_label_db::glueserver_get_labels_course($courseid);
        $returns = array();
        foreach ($labels as $label) {
            $label_obj = new glueserver_label($label);
            $returns[] = $label_obj->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_labels_course_returns() {
        return new external_multiple_structure(
            glueserver_label::get_class_structure()
        );
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function delete_label_course_parameters() {
        return new external_function_parameters(
        	array(
            	'labels' => new external_multiple_structure(
                 	new external_single_structure(
                 		array(
							'labelid' => new external_value(PARAM_INT, 'label id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
							'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        ))));
    }
    
     /**
     *
     * Function to delete a list of labels in a course
     * @param $labels the labels to be deleted
     * @return @array the result of the deletion
     */
    public static function delete_label_course($labels) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);
        $success = 1;
        foreach ($labels as $label){
        	$labelid = $label["labelid"];
        	$courseid = $label["courseid"];
        	$result = glueserver_label_db::glueserver_delete_label_course($labelid, $courseid);
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
    public static function delete_label_course_returns() {
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
    public static function insert_label_course_parameters() {
        return new external_function_parameters(
            array(
                'course'		=> new external_value(PARAM_INT, 'id the course', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_TEXT, 'The name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'intro'      	=> new external_value(PARAM_RAW,'Intro', VALUE_REQUIRED, null, NULL_ALLOWED),
                'introformat'   => new external_value(PARAM_INT, 'introformat', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to insert a label in a course
     * @param $course int the identifier of the course
     * @param $name the label name
     * @param $intro
     * @param $introformat
     * @return array label details
     */
    public static function insert_label_course($course,$name,$intro,$introformat) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $result = glueserver_label_db::glueserver_insert_label_course($course,$name,$intro,$introformat);
		return array ('labelid' => $result);
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function insert_label_course_returns() {
    	return new external_single_structure(
         	array(
             	"labelid" => new external_value(PARAM_INT, 'The id of the label just created', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
         	)
     	);
    }
    
}
