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
 * Page Database Library Class
 *
 * @package GlueServer
 * @subpackage Page
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_page_db_base {
     /**
     * Returns an array of the pages of the course
     *	@param integer the course id
     *
     * @return array of pages in the course
     */
    public static function glueserver_get_pages_course($courseid){
		global $DB,$CFG;
        
        $sql = "SELECT page.*
                FROM {$CFG->prefix}page page
                WHERE page.course = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
    
     /**
     * Returns the page info
     *	@param pageid int the page id
     *	@param courseid int the courseid
     *
     * @return array containing the page info
     */
    public static function glueserver_get_page_course($pageid, $courseid){
		global $DB,$CFG;
        
        $sql = "SELECT page.*
                FROM {$CFG->prefix}page page
                WHERE page.id = ? AND course = ?";
        $sqlparams = array($pageid, $courseid);
        return $DB->get_records_sql($sql, $sqlparams);	
    }
    
     /**
     * Delete a page from the course
     *  @param integer the page id
     *	@param integer the course id
     *
     * @return array of pages in the course
     */
    public static function glueserver_delete_page_course($pageid, $courseid){
		global $DB,$CFG;
        $sqlparams = array($pageid, $courseid);
        if (count(glueserver_page_db::glueserver_get_page_course($pageid, $courseid))==1){
        	$DB->delete_records_select("page", "id = ? AND course = ?", $sqlparams);
        	//the previous functions always returns true, so we make sure that the page has been really deleted
        	if(count(glueserver_page_db::glueserver_get_page_course($pageid, $courseid))==0){
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        } 
    }
    
     /**
     * Intert a page into the course
     * @return the url id
     */
    public static function glueserver_insert_page_course($course,$name,$intro,$introformat,$content,$contentformat,$display,$displayoptions){
    	global $DB,$CFG;
    		
    	$record = new stdClass();
		$record->course = $course;
		$record->name = $name;
		$record->intro = $intro;
		$record->introformat = $introformat;
		$record->content = $content;
		$record->contentformat = $contentformat;
		$record->display = $display;
		$record->displayoptions = $displayoptions;
		$record->timemodified = time();
		$return_id = $DB->insert_record("page", $record, true);
        return $return_id;
    }
}