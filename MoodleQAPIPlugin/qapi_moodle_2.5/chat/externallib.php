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
 * Forum External Library
 *
 * @package GlueServer
 * @subpackage Chat
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS;
require_once("$GWS->gwsroot/chat/chat.class.php");
require_once("$GWS->gwsroot/chat/db/chatDB.php");
require_once("$CFG->libdir/externallib.php");

class glueserver_chat_external extends external_api {

     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function get_chats_course_parameters() {
        return new external_function_parameters(
            array(
				'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to get the list of chats in a course
     *
     * @return details of the chats
     */
    public static function get_chats_course($courseid) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $chats = glueserver_chat_db::glueserver_get_chats_course($courseid);
        $returns = array();
        foreach ($chats as $chat) {
            $chat = new glueserver_chat($chat);
            $returns[] = $chat->get_data();
        }
        return $returns;
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_chats_course_returns() {
        return new external_multiple_structure(
            glueserver_chat::get_class_structure()
        );
    }
    
     /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
    public static function delete_chat_course_parameters() {
        return new external_function_parameters(
            array(
                 'chats' => new external_multiple_structure(
                 	new external_single_structure(
                 		array(
							'chatid' => new external_value(PARAM_INT, 'chat id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
							'courseid' => new external_value(PARAM_INT, 'course id', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
            )
        ))));
    }
    
     /**
     *
     * Function to delete a chat in a course
     *
     * @return the result of the deletion
     */
    public static function delete_chat_course($chats) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);  
        $success = 1; 
        foreach($chats as $chat){
        	$chatid = $chat["chatid"];
        	$courseid = $chat["courseid"];   
        	$result = glueserver_chat_db::glueserver_delete_chat_course($chatid, $courseid);
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
    public static function delete_chat_course_returns() {
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
    public static function insert_chat_course_parameters() {
        return new external_function_parameters(
            array(
                'course'		=> new external_value(PARAM_INT, 'id the course', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED),
                'name'      	=> new external_value(PARAM_TEXT,'The chat name', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'intro'   		=> new external_value(PARAM_RAW, 'intro', VALUE_REQUIRED, '', NULL_NOT_ALLOWED),
                'introformat'   => new external_value(PARAM_INT,'introformat', VALUE_DEFAULT, 1, NULL_NOT_ALLOWED),
                'keepdays'		=> new external_value(PARAM_INT, 'keepdays', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'studentlogs'	=> new external_value(PARAM_INT, 'studentlogs', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'chattime'		=> new external_value(PARAM_INT,'chattime', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED),
                'schedule'    	=> new external_value(PARAM_INT,'schedule', VALUE_DEFAULT, 0, NULL_NOT_ALLOWED)
            )
        );
    }
    
     /**
     *
     * Function to insert a chat in a course
     *
     * @return chat details
     */
    public static function insert_chat_course($course,$name,$intro,$introformat,$keepdays,$studentlogs,$chattime,$schedule) {
        $context = get_context_instance(CONTEXT_SYSTEM);
        self::validate_context($context);
        require_capability('moodle/course:view', $context);   
        $result = glueserver_chat_db::glueserver_insert_chat_course($course,$name,$intro,$introformat,$keepdays,$studentlogs,$chattime,$schedule);
		return array ('chatid' => $result);
    }
    
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function insert_chat_course_returns() {
    	return new external_single_structure(
         	array(
             	"chatid" => new external_value(PARAM_INT, 'The id of the chat just created', VALUE_REQUIRED, 0, NULL_NOT_ALLOWED)
         	)
     	);
    }
    
}
