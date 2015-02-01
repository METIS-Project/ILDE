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
 * Page External Library
 *
 * @package GlueServer
 * @subpackage Page
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once("$GWS->gwsroot/page/page.class.php");
require_once("$GWS->gwsroot/page/db/pageDB.php");
require_once("$CFG->libdir/externallib.php");

class glueserver_page_external extends external_api {

     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_pages_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of pages in a course
     *
     * @return details of the pages
     */
    public static function get_pages_course($courseid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $pages = glueserver_page_db::glueserver_get_pages_course($courseid);
        $returns = array();
        foreach ($pages as $page) {
            $page = new glueserver_page($page);
            $returns[] = $page->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_pages_course_returns() {
        return new external_multiple_structure(
            glueserver_page::get_class_structure()
        );
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function delete_page_course_parameters() {
        return new external_function_parameters(
            array(
            	'pages' => new external_multiple_structure(
                 	new external_single_structure(
                 		array(
							'pageid' => new external_value(PARAM_INT, 'page id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
							'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        ))));
    }
    
     /**
     *
     * Function to delete a page in a course
     *
     * @return details of the pages
     */
    public static function delete_page_course($pages) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context); 
        $success = 1;
        foreach ($pages as $page){
        	$pageid = $page["pageid"];
        	$courseid = $page["courseid"];
        	$result = glueserver_page_db::glueserver_delete_page_course($pageid, $courseid); 
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
    public static function delete_page_course_returns() {
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
    public static function insert_page_course_parameters() {
        return new external_function_parameters(
            array(
                'course'		=> new external_value(PARAM_INT, 'id the course', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_TEXT,'The page name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'intro'   		=> new external_value(PARAM_RAW, 'intro', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'introformat'   => new external_value(PARAM_INT,'introformat', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
				'content'		=> new external_value(PARAM_RAW, 'content', VALUE_DEFAULT, NULL, NULL_ALLOWED),
				'contentformat'	=> new external_value(PARAM_INT, 'content format', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
				'display'		=> new external_value(PARAM_INT, 'display', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
				'displayoptions'=> new external_value(PARAM_RAW, 'displayoptions', VALUE_REQUIRED, '', NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to insert a page in a course
     *
     * @return page details
     */
    public static function insert_page_course($course,$name,$intro,$introformat,$content,$contentformat,$display,$displayoptions) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $result = glueserver_page_db::glueserver_insert_page_course($course,$name,$intro,$introformat,$content,$contentformat,$display,$displayoptions);
		return array ('pageid' => $result);
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function insert_page_course_returns() {
    	return new external_single_structure(
         	array(
             	"pageid" => new external_value(PARAM_INT, 'The id of the page just created', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
         	)
     	);
    }
    
}
