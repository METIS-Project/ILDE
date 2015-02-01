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
 * Role External Library
 *
 * @package GlueServer
 * @subpackage Role
 * @author Javier Enrique Hoyos Torio
 *
 */
 
if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS,$CFG;
require_once($GWS->gwsroot . '/enrol/enrol.class.php');
require_once($GWS->gwsroot . '/enrol/db/enrolDB.php');
require_once($GWS->gwsroot . '/course/course.class.php');
require_once($GWS->gwsroot . '/course/db/courseDB.php');
require_once($CFG->libdir . '/externallib.php');

class glueserver_enrol_external extends external_api{
	
	
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_enrols_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of enrols in a course
     *
     * @return details of the enrols
     */
    public static function get_enrols_course($courseid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $enrols = glueserver_enrol_db::glueserver_get_enrols_course($courseid);
        $returns = array();
        foreach ($enrols as $enrol) {
            $enrol = new glueserver_enrol($enrol);
            $returns[] = $enrol->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_enrols_course_returns() {
        return new external_multiple_structure(
            glueserver_enrol::get_class_structure()
        );
    }
    
         /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function delete_enrol_course_parameters() {
        return new external_function_parameters(
            array(
				'enrolid' => new external_value(PARAM_INT, 'enrol id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to delete a enrol in a course
     *
     * @return details of the enrols
     */
    public static function delete_enrol_course($enrolid, $courseid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $result = glueserver_enrol_db::glueserver_delete_enrol_course($enrolid, $courseid);
		return array ('success' => $result);
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function delete_enrol_course_returns() {
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
	public static function set_enrol_parameters(){
        return new external_function_parameters(
            array(
                'enrol'			=> new external_value(PARAM_RAW,	'name of the enrol', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'status'		=> new external_value(PARAM_INT,	'status', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'courseid'   	=> new external_value(PARAM_INT,	'course ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'sortorder'		=> new external_value(PARAM_INT,    'sortorder', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get list of roles of a user in a course
     *
     * @return user details
     */
    public static function set_enrol($enrol,$status,$courseid,$sortorder,$timecreated,$timemodified){
    	switch ($enrol) {
    		//Some checks should be made depending on the enrol type
    		case 'manual': 	break;
    		case 'guest':	break;
    		case 'self': 	break;
    		default:
    			          	throw new moodle_exception('generalexceptionmessage','glueserver_enrol', '','enrol not valid');
                			break;
    	}
        $return = glueserver_enrol_db::glueserver_set_enrol($enrol,$status,$courseid,$sortorder,$timecreated,$timemodified);
        return array ("enrolid" => $return);
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function set_enrol_returns() {
        return new external_single_structure(
        	array(
            		"enrolid" => new external_value(PARAM_INT, 'The id of the enrol just created', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
    
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
	public static function disenroll_course_user_parameters(){
        return new external_function_parameters(
            array(
                'userid'		=> new external_value(PARAM_INT, 'user ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'courseid'   	=> new external_value(PARAM_INT, 'course ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'enrolid'   	=> new external_value(PARAM_INT, 'enrol ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
            )
        );
    }
    
     /**
     *
     * Function to disenroll a user from a course 
     *
     * @return user details
     */
    public static function disenroll_course_user($userid,$courseid,$enrolid){
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $result = glueserver_enrol_db::glueserver_disenroll_course_user($userid,$courseid,$enrolid);
		return array ('success' => $result);
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function disenroll_course_user_returns() {
    	return new external_single_structure(
         	array(
             'success' => new external_value(PARAM_BOOL, 'Returns true on success', VALUE_REQUIRED, '', NULL_NOT_ALLOWED)
         	)
     	);
    }
	
}

?>
