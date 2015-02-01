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
 * Forum External Library
 *
 * @package GlueServer
 * @subpackage Forum
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once("$GWS->gwsroot/forum/forum.class.php");
require_once("$GWS->gwsroot/forum/db/forumDB.php");
require_once("$CFG->libdir/externallib.php");

class glueserver_forum_external extends external_api {

     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_forums_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of forums in a course
     *
     * @return details of the forums
     */
    public static function get_forums_course($courseid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $forums = glueserver_forum_db::glueserver_get_forums_course($courseid);
        $returns = array();
        foreach ($forums as $forum) {
            $forum = new glueserver_forum($forum);
            $returns[] = $forum->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_forums_course_returns() {
        return new external_multiple_structure(
            glueserver_forum::get_class_structure()
        );
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function delete_forum_course_parameters() {
        return new external_function_parameters(
        	array(
                 'forums' => new external_multiple_structure(
                 	new external_single_structure(
                 		array(
							'forumid' => new external_value(PARAM_INT, 'forum id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
							'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        ))));
    }
    
     /**
     *
     * Function to delete a forum in a course
     *
     * @return details of the urls
     */
    public static function delete_forum_course($forums) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);  
        $success = 1;
        foreach ($forums as $forum){
        	$forumid = $forum["forumid"];
        	$courseid = $forum["courseid"];
        	$result = glueserver_forum_db::glueserver_delete_forum_course($forumid, $courseid); 
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
    public static function delete_forum_course_returns() {
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
    public static function insert_forum_course_parameters() {
        return new external_function_parameters(
            array(
                'course'		=> new external_value(PARAM_INT, 'id the course', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'type'      	=> new external_value(PARAM_TEXT, 'The forum type', VALUE_DEFAULT, 'general', NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_TEXT,'The forum name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'intro'   		=> new external_value(PARAM_RAW, 'intro', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'introformat'   => new external_value(PARAM_INT,'introformat', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'assessed'		=> new external_value(PARAM_INT, 'assessed', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'assesstimestart'	=> new external_value(PARAM_INT, 'assesstimestart', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'assesstimefinish'	=> new external_value(PARAM_INT,'assesstimefinish', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'maxattachments'    => new external_value(PARAM_INT,'maxattachments', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to insert a forum in a course
     *
     * @return forum details
     */
    public static function insert_forum_course($course,$type,$name,$intro,$introformat,$assessed,$assesstimestart,$assesstimefinish,$maxattachments) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $result = glueserver_forum_db::glueserver_insert_forum_course($course,$type,$name,$intro,$introformat,$assessed,$assesstimestart,$assesstimefinish,$maxattachments);
		return array ('forumid' => $result);
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function insert_forum_course_returns() {
    	return new external_single_structure(
         	array(
             	"forumid" => new external_value(PARAM_INT, 'The id of the forum just created', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
         	)
     	);
    }
    
}
