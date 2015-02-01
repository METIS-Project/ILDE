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
 * Grouping External Library
 *
 * @package GlueServer
 * @subpackage Grouping
 * @author Javier Enrique Hoyos Torio
 *
 */
 
if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS,$CFG;
require_once($GWS->gwsroot . '/grouping/grouping.class.php');
require_once($GWS->gwsroot . '/grouping/db/groupingDB.php');
require_once($CFG->libdir . '/externallib.php');

class glueserver_grouping_external extends external_api{
	
	 /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_groupings_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of groupings in a course
     *
     * @return details of the groupings
     */
    public static function get_groupings_course($courseid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $groupings = glueserver_grouping_db::glueserver_get_groupings_course($courseid);
        $returns = array();
        foreach ($groupings as $grouping) {
            $grouping = new glueserver_grouping($grouping);
            $returns[] = $grouping->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_groupings_course_returns() {
        return new external_multiple_structure(
            glueserver_grouping::get_class_structure()
        );
    }
    
	 /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
	public static function create_grouping_parameters(){
        return new external_function_parameters(
            array(
            	'groupings' => new external_multiple_structure(
            		new external_single_structure(
            			array(
                			'courseid'		=> new external_value(PARAM_INT, 'id of course', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                			'name'			=> new external_value(PARAM_TEXT, 'The grouping\'s name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                			'description'   => new external_value(PARAM_RAW, 'grouping description text', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                			'descriptionformat' => new external_value(PARAM_INT, 'description format', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED)
                		)
                	), 'List of grouping objects. A grouping has a courseid, a name and a description.'
                )
            )
    	);
    }
    
     /**
     *
     * Create grouping in the course
     * @param array $groupings array of grouping description arrays
     * @return array of newly created groupings
     */
    public static function create_grouping($groupings){
 		$gps = array();
 		for ($i=0; $i< count($groupings); $i++){
 			$grouping = $groupings[$i];
        	$grouping_id = glueserver_grouping_db::glueserver_create_grouping($grouping);
        	$grouping['id'] = $grouping_id;
            $gp = new glueserver_grouping((object)$grouping);
            $gps[] = $gp->get_data();
        }
        return $gps;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function create_grouping_returns() {
        return new external_multiple_structure(
            glueserver_grouping::get_class_structure()
        );
    }
	
	
	 /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
	public static function assign_grouping_parameters(){
        return new external_function_parameters(
            array(
            	'groupings_groups' => new external_multiple_structure(
            		new external_single_structure(
            			array(
                			'groupingid'		=> new external_value(PARAM_INT, 'id of the grouping', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                			'groupid'			=> new external_value(PARAM_INT, 'id of the group', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                		)
                	), 'List of grouping_groups. Each group is assigned a grouping'
                )
            )
    	);
    }
    
     /**
     *
     * Assign the group to the grouping
     * @param array $groupings array of grouping description arrays
     * @return array of newly created groupings
     */
    public static function assign_grouping($groupings_groups){
 		$gps = array();
 		for ($i=0; $i< count($groupings_groups); $i++){
 			$gg = $groupings_groups[$i];
        	$gg_id = glueserver_grouping_db::glueserver_assign_grouping($gg);
        	$gg['id'] = $gg_id;
            //$gp = new glueserver_grouping((object)$gg);
            //$gps[] = $gp->get_data();
            $gps[] = $gg;
        }
        return $gps;
    }
    
    /**
     * Returns description of method result value
     * @return external_description
     */
    public static function assign_grouping_returns() {
        return new external_multiple_structure(
            new external_single_structure(
            	array(
                	'id'				=> new external_value(PARAM_INT, 'groupings_groups record id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                	 'groupingid'		=> new external_value(PARAM_INT, 'id of the grouping', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                	 'groupid'			=> new external_value(PARAM_INT, 'id of the group', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
            	), 'List of grouping_groups. Each group is assigned a grouping'
            )
        );
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function delete_grouping_course_parameters() {
        return new external_function_parameters(
            array(
            	'groupings' => new external_multiple_structure(
                 	new external_single_structure(
                 	array(
						'groupingid' => new external_value(PARAM_INT, 'grouping id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
						'courseid' 	 => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        ))));
    }
    
     /**
     *
     * Function to delete a grouping in a course
     *
     * @return details of the groupings
     */
    public static function delete_grouping_course($groupings) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context); 
        $success = 1;
        foreach($groupings as $grouping){
        	$groupingid = $grouping["groupingid"];
        	$courseid = $grouping["courseid"];
        	$result = glueserver_grouping_db::glueserver_delete_grouping_course($groupingid,$courseid);
        	if ($result == 0){
        		$success = 0;
        	}
        }  
		return array ('success' => $success);
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function delete_grouping_course_returns() {
    	return new external_single_structure(
         	array(
             'success' => new external_value(PARAM_BOOL, 'Returns true on success', VALUE_REQUIRED, '', NULL_NOT_ALLOWED)
         	)
     	);
    }
}

?>
