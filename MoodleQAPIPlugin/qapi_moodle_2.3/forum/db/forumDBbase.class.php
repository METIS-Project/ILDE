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
 * Forum Database Library Class
 *
 * @package GlueServer
 * @subpackage Forum
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_forum_db_base {
     /**
     * Returns an array of the forums of the course
     *	@param integer the course id
     *
     * @return array of forums in the course
     */
    public static function glueserver_get_forums_course($courseid){
		global $DB,$CFG;
        
        $sql = "SELECT forum.*
                FROM {$CFG->prefix}forum forum
                WHERE forum.course = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
    
     /**
     * Returns the forum info
     *	@param forumid int the forum id
     *	@param courseid int the courseid
     *
     * @return array containing the forum info
     */
    public static function glueserver_get_forum_course($forumid, $courseid){
		global $DB,$CFG;
        
        $sql = "SELECT forum.*
                FROM {$CFG->prefix}forum forum
                WHERE forum.id = ? AND course = ?";
        $sqlparams = array($forumid, $courseid);
        return $DB->get_records_sql($sql, $sqlparams);	
    }
    
     /**
     * Delete a forum from the course
     *  @param integer the forum id
     *	@param integer the course id
     *
     * @return array of urls in the course
     */
    public static function glueserver_delete_forum_course($forumid, $courseid){
		global $DB,$CFG;
        $sqlparams = array($forumid, $courseid);
        if (count(glueserver_forum_db::glueserver_get_forum_course($forumid, $courseid))==1){
        	$DB->delete_records_select("forum", "id = ? AND course = ?", $sqlparams);
        	//the previous functions always returns true, so we make sure that the forum has been really deleted
        	if(count(glueserver_forum_db::glueserver_get_forum_course($forumid, $courseid))==0){
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        } 
    }
    
     /**
     * Intert a forum into the course
     * @return the url id
     */
    public static function glueserver_insert_forum_course($course,$type,$name,$intro,$introformat,$assessed,$assesstimestart,$assesstimefinish,$maxattachments){
    	global $DB,$CFG;
    		
    	$record = new stdClass();
		$record->course = $course;
		$record->type = $type;
		$record->name = $name;
		$record->intro = $intro;
		$record->introformat = $introformat;
		$record->assessed = $assessed;
		$record->assesstimestart = $assesstimestart;
		$record->assesstimefinish = $assesstimefinish;
		$record->maxattachments = $maxattachments;
		$record->timemodified = time();
		$return_id = $DB->insert_record("forum", $record, true);
        return $return_id;
    }
}