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
 * Course External Library
 *
 * @package GlueServer
 * @subpackage Course
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once($GWS->gwsroot . '/course/course.class.php');
require_once($GWS->gwsroot . '/course/section.class.php');
require_once($GWS->gwsroot . '/course/module.class.php');
require_once($GWS->gwsroot . '/course/db/courseDB.php');
require_once($CFG->libdir . '/externallib.php');
require_once($CFG->libdir . '/modinfolib.php');


class glueserver_course_external extends external_api {
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_all_courses_parameters() {
        return new external_function_parameters(
            array(
				//if I had any parameters, they would be described here. But I don't have any, so this array is empty.
            )
        );
    }
    
     /**
     *
     * Function to get list of users enrolled in a course
     *
     * @return user details
     */
    public static function get_all_courses() {

        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $courses = glueserver_course_db::glueserver_get_all_courses();
        $returns = array();
        foreach ($courses as $course) {
            $course = new glueserver_course($course);
            $returns[] = $course->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_all_courses_returns() {
        return new external_multiple_structure(
            glueserver_course::get_class_structure()
        );
    }
    
         /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_courses_userid_parameters() {
        return new external_function_parameters(
            array(
				'userid'    => new external_value(PARAM_INT, 'user ID', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
				'roleid'    => new external_value(PARAM_INT, 'role ID', VALUE_OPTIONAL, null, NULL_ALLOWED),
            )
        );
    }
    
     /**
     *
     * Function to get list of courses where a user is enrolled
     *
     * @return user details
     */
    public static function get_courses_userid($userid,$roleid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);
            
        $courses = glueserver_course_db::glueserver_get_courses_userid($userid,$roleid);
        $returns = array();
        foreach ($courses as $course) {
            $course = new glueserver_course($course);
            $returns[] = $course->get_data();
        }
        return $returns;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_courses_userid_returns() {
        return new external_multiple_structure(
            glueserver_course::get_class_structure()
        );
    }
    
             /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_courses_username_parameters() {
        return new external_function_parameters(
            array(
				'username'    => new external_value(PARAM_RAW, 'user name', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
				'roleid'    => new external_value(PARAM_INT, 'role ID', VALUE_OPTIONAL, null, NULL_ALLOWED),
            )
        );
    }
    
     /**
     *
     * Function to get list of courses where a user is enrolled with a role
     *
     * @return user details
     */
    public static function get_courses_username($username,$roleid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);
            
        $courses = glueserver_course_db::glueserver_get_courses_username($username,$roleid);
        $returns = array();
        foreach ($courses as $course) {
            $course = new glueserver_course($course);
            $returns[] = $course->get_data();
        }
        return $returns;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_courses_username_returns() {
        return new external_multiple_structure(
            glueserver_course::get_class_structure()
        );
    }
    
    /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_courses_enrolled_username_parameters() {
        return new external_function_parameters(
            array(
				'username'    => new external_value(PARAM_RAW, 'user name', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of courses in which a user is enrolled (with or without a role)
     *
     * @return the list of courses in which the user is enrolled
     */
    public static function get_courses_enrolled_username($username) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);
            
        $courses = glueserver_course_db::glueserver_get_courses_enrolled_username($username);
        $returns = array();
        foreach ($courses as $course) {
            $course = new glueserver_course($course);
            $returns[] = $course->get_data();
        }
        return $returns;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_courses_enrolled_username_returns() {
        return new external_multiple_structure(
            glueserver_course::get_class_structure()
        );
    }
    
    /**
     * Module related functions
     */
    
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_sections_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get list of sections in a course
     *
     * @return section details
     */
    public static function get_sections_course($courseid) {

        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $sections = glueserver_course_db::glueserver_get_sections_course($courseid);
        $returns = array();
        foreach ($sections as $section) {
        	$section->summary = '<![CDATA['.$section->summary.']]>';//Important!. The summary could contain html tags. We insert that summary between the <![CDATA[ and ]]> tags
            $section = new glueserver_section($section);
            $returns[] = $section->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_sections_course_returns() {
        return new external_multiple_structure(
            glueserver_section::get_class_structure()
        );
    }
    
    
    /**
     *  Module related functions
     */
     
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_module_parameters() {
        return new external_function_parameters(
            array(
				'moduleid' => new external_value(PARAM_INT, 'module id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get a module by its id
     *
     * @return module details
     */
    public static function get_module($moduleid) {
	
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);        
        $module = glueserver_course_db::glueserver_get_module($moduleid); 
        $returns = array();
        foreach ($module as $m){
       		$module = new glueserver_module($m);
       		$returns[] = $module->get_data();
        }
        return $returns[0];
    }   
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_module_returns() {
        return glueserver_module::get_class_structure();
    }
    
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function update_module_parameters() {
        return new external_function_parameters(
            array(
				'module' => glueserver_module::get_class_structure()
            )
        );
    }
    
     /**
     *
     * Function to update a module
     *
     * @return updated module details
     */
    public static function update_module($module) {
	
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);        
        $result = glueserver_course_db::glueserver_update_module($module);
        $courseid = $module['course'];
        //It is necessary to call this Moodle function in order to rebuild the course cache
        rebuild_course_cache($courseid); 
		return array ('success' => $result);
    }   
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function update_module_returns() {
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
    public static function update_section_parameters() {
        return new external_function_parameters(
            array(
				'section' => glueserver_section::get_class_structure()
            )
        );
    }
    
     /**
     *
     * Function to update a section
     *
     * @return updated section details
     */
    public static function update_section($section) {
	
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);        
        $result = glueserver_course_db::glueserver_update_section($section);
        $courseid = $section['course'];
        //It is necessary to call this Moodle function in order to rebuild the course cache
        rebuild_course_cache($courseid);
		return array ('success' => $result);
    }   
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function update_section_returns() {
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
    public static function get_modules_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
				'module'    => new external_value(PARAM_INT, 'module type id', VALUE_OPTIONAL, null, NULL_ALLOWED),
            )
        );
    }
    
     /**
     *
     * Function to get the list of modules in a course
     *
     * @return details of the modules
     */
    public static function get_modules_course($courseid, $module) {

        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $modules = glueserver_course_db::glueserver_get_modules_course($courseid, $module);
        $returns = array();
        foreach ($modules as $module) {
            $module = new glueserver_module($module);
            $returns[] = $module->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_modules_course_returns() {
        return new external_multiple_structure(
            glueserver_module::get_class_structure()
        );
    }
    
         /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function delete_module_parameters() {
        return new external_function_parameters(
            array(
            	'modules' => new external_multiple_structure(
                 	new external_single_structure(
                 	array(
						'moduleid' => new external_value(PARAM_INT, 'module id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
						'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            		)
            	))));
    }
    
     /**
     *
     * Delete a module in a course
     *
     * @return details of the urls
     */
    public static function delete_module($modules) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);
        $success = 1; 
        foreach($modules as $module){
        	$moduleid = $module["moduleid"];
        	$courseid = $module["courseid"];  
        	$result = glueserver_course_db::glueserver_delete_module($moduleid, $courseid);
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
    public static function delete_module_returns() {
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
    public static function insert_module_parameters() {
        return new external_function_parameters(
            array(
                'course'     	=> new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'module'      	=> new external_value(PARAM_INT, 'module type', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'instance'      => new external_value(PARAM_INT, 'instance', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'section'      	=> new external_value(PARAM_INT, 'section', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'visible'     	=> new external_value(PARAM_INT, 'visible', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
                'groupmode'     	=> new external_value(PARAM_INT, 'groupmode', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'groupingid'     	=> new external_value(PARAM_INT, 'groupingid', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'groupmembersonly'  => new external_value(PARAM_INT, 'groupmembersonly', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to insert a module in a course
     *
     * @return module details
     */
    public static function insert_module($course,$module,$instance,$section,$visible,$groupmode,$groupingid,$groupmembersonly) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $result = glueserver_course_db::glueserver_insert_module($course,$module,$instance,$section,$visible,$groupmode,$groupingid,$groupmembersonly);
        //It is necessary to call this Moodle function in order to rebuild the course cache
        rebuild_course_cache($course); 
		return array ('moduleid' => $result);
    }
    
        
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function insert_module_returns() {
    	return new external_single_structure(
         	array(
             	"moduleid" => new external_value(PARAM_INT, 'The id of the module just created', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
         	)
     	);
    }
     

}
