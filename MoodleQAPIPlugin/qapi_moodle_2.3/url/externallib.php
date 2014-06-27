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
 * Url External Library
 *
 * @package GlueServer
 * @subpackage Url
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once("$GWS->gwsroot/url/url.class.php");
require_once("$GWS->gwsroot/url/db/urlDB.php");
require_once("$CFG->libdir/externallib.php");

class glueserver_url_external extends external_api {

     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_urls_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of urls in a course
     *
     * @return details of the urls
     */
    public static function get_urls_course($courseid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $urls = glueserver_url_db::glueserver_get_urls_course($courseid);
        $returns = array();
        foreach ($urls as $url) {
            $url = new glueserver_url($url);
            $returns[] = $url->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_urls_course_returns() {
        return new external_multiple_structure(
            glueserver_url::get_class_structure()
        );
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function delete_url_course_parameters() {
        return new external_function_parameters(
        	array(
            	'urls' => new external_multiple_structure(
                 	new external_single_structure(
                 		array(
							'urlid' => new external_value(PARAM_INT, 'url id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
							'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        ))));
    }
    
     /**
     *
     * Function to get the list of urls in a course
     *
     * @return details of the urls
     */
    public static function delete_url_course($urls) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);
        $success = 1;
        foreach ($urls as $url){
        	$urlid = $url["urlid"];
        	$courseid = $url["courseid"];
        	$result = glueserver_url_db::glueserver_delete_url_course($urlid, $courseid);
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
    public static function delete_url_course_returns() {
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
    public static function insert_url_course_parameters() {
        return new external_function_parameters(
            array(
                'course'		=> new external_value(PARAM_INT, 'id the course', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_TEXT, 'The name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'intro'      	=> new external_value(PARAM_RAW,'Intro', VALUE_REQUIRED, null, NULL_ALLOWED),
                'introformat'   => new external_value(PARAM_INT, 'introformat', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
                'externalurl'   => new external_value(PARAM_TEXT,'externalurl', VALUE_REQUIRED, null, NULL_ALLOWED),
                'display'       => new external_value(PARAM_INT, 'display', VALUE_DEFAULT, 2, NULL_NOT_ALLOWED),
                'displayoptions'=> new external_value(PARAM_TEXT,'displayoptions', VALUE_DEFAULT, null, NULL_ALLOWED),
                'parameters'    => new external_value(PARAM_TEXT,'parameters', VALUE_DEFAULT, null, NULL_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to insert a url in a course
     *
     * @return url details
     */
    public static function insert_url_course($course,$name,$intro,$introformat,$externalurl,$display,$displayoptions,$parameters) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $result = glueserver_url_db::glueserver_insert_url_course($course,$name,$intro,$introformat,$externalurl,$display,$displayoptions,$parameters);
		return array ('urlid' => $result);
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function insert_url_course_returns() {
    	return new external_single_structure(
         	array(
             	"urlid" => new external_value(PARAM_INT, 'The id of the url just created', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
         	)
     	);
    }
    
}
