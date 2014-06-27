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
 * Chat Database Library Class
 *
 * @package GlueServer
 * @subpackage Chat
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_chat_db_base {
     /**
     * Returns an array of the chats of the course
     *	@param integer the course id
     *
     * @return array of chats in the course
     */
    public static function glueserver_get_chats_course($courseid){
		global $DB,$CFG;
        
        $sql = "SELECT chat.*
                FROM {$CFG->prefix}chat chat
                WHERE chat.course = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
    
     /**
     * Returns the chat info
     *	@param chatid int the chat id
     *	@param courseid int the courseid
     *
     * @return array containing the chat info
     */
    public static function glueserver_get_chat_course($chatid, $courseid){
		global $DB,$CFG;
        
        $sql = "SELECT chat.*
                FROM {$CFG->prefix}chat chat
                WHERE chat.id = ? AND course = ?";
        $sqlparams = array($chatid, $courseid);
        return $DB->get_records_sql($sql, $sqlparams);	
    }
    
     /**
     * Delete a chat from the course
     *  @param integer the chat id
     *	@param integer the course id
     *
     * @return the result of the deletion process
     */
    public static function glueserver_delete_chat_course($chatid, $courseid){
		global $DB,$CFG;
        $sqlparams = array($chatid, $courseid);
        if (count(glueserver_chat_db::glueserver_get_chat_course($chatid, $courseid))==1){
        	$DB->delete_records_select("chat", "id = ? AND course = ?", $sqlparams);
        	//the previous functions always returns true, so we make sure that the chat has been really deleted
        	if(count(glueserver_chat_db::glueserver_get_chat_course($chatid, $courseid))==0){
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        } 
    }
    
     /**
     * Intert a chat into the course
     * @return the chat id
     */
    public static function glueserver_insert_chat_course($course,$name,$intro,$introformat,$keepdays,$studentlogs,$chattime,$schedule){
    	global $DB,$CFG;
    		
    	$record = new stdClass();
		$record->course = $course;
		$record->name = $name;
		$record->intro = $intro;
		$record->introformat = $introformat;
		$record->keepdays = $keepdays;
		$record->studentlogs = $studentlogs;
		$record->chattime = $chattime;
		$record->schedule = $schedule;
		$record->timemodified = time();
		$return_id = $DB->insert_record("chat", $record, true);
        return $return_id;
    }
}