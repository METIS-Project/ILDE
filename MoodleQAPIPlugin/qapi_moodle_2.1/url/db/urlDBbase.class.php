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
 * User Database Library Class
 *
 * @package GlueServer
 * @subpackage User
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_url_db_base {
     /**
     * Returns an array of the urls of the course
     *	@param integer the course id
     *
     * @return array of urls in the course
     */
    public static function glueserver_get_urls_course($courseid){
		global $DB,$CFG;
        
        $sql = "SELECT url.*
                FROM {$CFG->prefix}url url
                WHERE url.course = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
    
     /**
     * Returns the module info
     *	@param integer the module id
     *
     * @return array containing the module info
     */
    public static function glueserver_get_url_course($urlid, $courseid){
		global $DB,$CFG;
        
        $sql = "SELECT url.*
                FROM {$CFG->prefix}url url
                WHERE url.id = ? AND course = ?";
        $sqlparams = array($urlid, $courseid);
        return $DB->get_records_sql($sql, $sqlparams);	
    }
    
     /**
     * Delete a url from the course
     *  @param integer the url id
     *	@param integer the course id
     *
     * @return array of urls in the course
     */
    public static function glueserver_delete_url_course($urlid, $courseid){
		global $DB,$CFG;
        $sqlparams = array($urlid, $courseid);
        if (count(glueserver_url_db::glueserver_get_url_course($urlid, $courseid))==1){
        	$DB->delete_records_select("url", "id = ? AND course = ?", $sqlparams);
        	//the previous functions always returns true, so we make sure that the url has been really deleted
        	if(count(glueserver_url_db::glueserver_get_url_course($urlid, $courseid))==0){
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        } 
    }
    
     /**
     * Intert a url into the course
     * @return the url id
     */
    public static function glueserver_insert_url_course($course,$name,$intro,$introformat,$externalurl,$display,$displayoptions,$parameters){
    	global $DB,$CFG;
    		
    	$record = new stdClass();
		$record->course = $course;
		$record->name = $name;
		$record->intro = $intro;
		$record->introformat = $introformat;
		$record->externalurl = $externalurl;
		$record->display = $display;
		$record->displayoptions = $displayoptions;
		$record->parameters = $parameters;
		$record->timemodified = time();
		$return_id = $DB->insert_record("url", $record, true);
        return $return_id;
    }
}