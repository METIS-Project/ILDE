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
 * User Database Library Class
 *
 * @package GlueServer
 * @subpackage label
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_label_db_base {
     /**
     * Returns an array of the labels of the course
     *	@param int $courseid the course id
     *
     * @return array labels in the course
     */
    public static function glueserver_get_labels_course($courseid){
		global $DB,$CFG;
        
        $sql = "SELECT label.*
                FROM {$CFG->prefix}label label
                WHERE label.course = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
    
     /**
     * Returns the module info
     *	@param integer the module id
     *
     * @return array containing the module info
     */
    public static function glueserver_get_label_course($labelid, $courseid){
		global $DB,$CFG;
        
        $sql = "SELECT label.*
                FROM {$CFG->prefix}label label
                WHERE label.id = ? AND course = ?";
        $sqlparams = array($labelid, $courseid);
        return $DB->get_records_sql($sql, $sqlparams);	
    }
    
     /**
     * Delete a label from the course
     *  @param int $labelid the label id
     *	@param int $courseid the course id
     *
     * @return array of labels in the course
     */
    public static function glueserver_delete_label_course($labelid, $courseid){
		global $DB,$CFG;
        $sqlparams = array($labelid, $courseid);
        //if (count(glueserver_label_db::glueserver_get_label_course($labelid, $courseid))==1){
        if (count(self::glueserver_get_label_course($labelid, $courseid))==1){
        	$DB->delete_records_select("label", "id = ? AND course = ?", $sqlparams);
        	//the previous function always returns true, so we make sure that the label has been really deleted
        	if(count(self::glueserver_get_label_course($labelid, $courseid))==0){
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        } 
    }
    
     /**
     * Insert a label into the course
     * @return the label id
     */
    public static function glueserver_insert_label_course($course,$name,$intro,$introformat){
    	global $DB,$CFG;
    		
    	$record = new stdClass();
		$record->course = $course;
		$record->name = $name;
		$record->intro = $intro;
		$record->introformat = $introformat;
		$record->timemodified = time();
		$return_id = $DB->insert_record("label", $record, true);
        return $return_id;
    }
}