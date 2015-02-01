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
 * Quiz Database Library Class
 *
 * @package GlueServer
 * @subpackage Quiz
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_quiz_db_base {
     /**
     * Returns an array of the quizzes of the course
     *	@param integer the course id
     *
     * @return array of quizzes in the course
     */
    public static function glueserver_get_quizzes_course($courseid){
		global $DB,$CFG;
        
        $sql = "SELECT quiz.*
                FROM {$CFG->prefix}quiz quiz
                WHERE quiz.course = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
    
     /**
     * Returns the quiz info
     *	@param quizid int the quiz id
     *	@param courseid int the courseid
     *
     * @return array containing the quiz info
     */
    public static function glueserver_get_quiz_course($quizid, $courseid){
		global $DB,$CFG;
        
        $sql = "SELECT quiz.*
                FROM {$CFG->prefix}quiz quiz
                WHERE quiz.id = ? AND course = ?";
        $sqlparams = array($quizid, $courseid);
        return $DB->get_records_sql($sql, $sqlparams);	
    }
    
     /**
     * Delete a quiz from the course
     *  @param integer the quiz id
     *	@param integer the course id
     *
     * @return array of urls in the course
     */
    public static function glueserver_delete_quiz_course($quizid, $courseid){
		global $DB,$CFG;
        $sqlparams = array($quizid, $courseid);
        if (count(glueserver_quiz_db::glueserver_get_quiz_course($quizid, $courseid))==1){
        	$DB->delete_records_select("quiz", "id = ? AND course = ?", $sqlparams);
        	//the previous functions always returns true, so we make sure that the quiz has been really deleted
        	if(count(glueserver_quiz_db::glueserver_get_quiz_course($quizid, $courseid))==0){
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        } 
    }
    
     /**
     * Intert a quiz into the course
     * @return the url id
     */
    public static function glueserver_insert_quiz_course($course,$name,$intro,$introformat,$timeopen,$timeclose,$preferredbehaviour,$attempts,$timemodified){
    	global $DB,$CFG;
    		
    	$record = new stdClass();
		$record->course = $course;
		$record->name = $name;
		$record->intro = $intro;
		$record->introformat = $introformat;
		$record->timeopen = $timeopen;
		$record->timeclose = $timeclose;
		$record->preferredbehaviour = $preferredbehaviour;
		$record->attempts = $attempts;
		$record->timemodified = time();
		$record->questions = "";
		$return_id = $DB->insert_record("quiz", $record, true);
        return $return_id;
    }
}