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
 * Grouping Database Library Class
 *
 * @package GlueServer
 * @subpackage Grouping
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_grouping_db_base {
     /**
     * Returns an array of the groupings of the course
     *	@param integer the course id
     *
     * @return array of groupings in the course
     */
    public static function glueserver_get_groupings_course($courseid){
		global $DB,$CFG;
        
        $sql = "SELECT groupings.*
                FROM {$CFG->prefix}groupings groupings
                WHERE groupings.courseid = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);		
    }
    
     /**
     * Create a grouping
     *
     *
     * @return the id of the grouping created
     */
    public static function glueserver_create_grouping($grouping){
    	global $DB,$CFG;	
	    $record = new stdClass();
		$record->courseid = $grouping['courseid'];
		$record->name = trim($grouping['name']);
		$record->description = $grouping['$description'];
		if (isset($grouping['$descriptionformat'])){
			$record->descriptionformat = $grouping['$descriptionformat'];
		}else{
			$record->descriptionformat = 1;
		}
		$record->timecreated =time();
		$record->timemodified = $record->timecreated;
		$return_id = $DB->insert_record("groupings", $record, true);
        return $return_id;
    }
    
     /**
     * Returns the grouping info
     *	@param grouping int the grouping id
     *
     * @return array containing the grouping info
     */
    public static function glueserver_get_grouping($groupingid,$courseid){
		global $DB,$CFG;
        
        $sql = "SELECT groupings.*
                FROM {$CFG->prefix}groupings groupings
                WHERE groupings.id = ? AND groupings.courseid = ?";
        $sqlparams = array($groupingid,$courseid);
        return $DB->get_records_sql($sql, $sqlparams);	
    }
    
         /**
     * Delete a grouping
     *	@param integer the grouping id
     *
     * @return boolean indicating if the grouping has been deleted properly
     */
    public static function glueserver_delete_grouping_course($groupingid,$courseid){
		global $DB,$CFG;
        $sqlparams = array($groupingid);
        if (count(glueserver_grouping_db::glueserver_get_grouping($groupingid,$courseid))==1){
        	$DB->delete_records_select("groupings", "id = ?", $sqlparams);
        	//the previous functions always returns true, so we make sure that the grouping has been really deleted
        	if(count(glueserver_grouping_db::glueserver_get_grouping($groupingid,$courseid))==0){
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        } 
    }
    
     /**
     * Assign a group to a grouping
     * @return the id of the grouping_group created
     */
    public static function glueserver_assign_grouping($grouping_group){
    	global $DB,$CFG;	
	    $record = new stdClass();
		$record->groupingid = $grouping_group['groupingid'];
		$record->groupid = trim($grouping_group['groupid']);
		$record->timeadded = time();
		$return_id = $DB->insert_record("groupings_groups", $record, true);
        return $return_id;
    }

}