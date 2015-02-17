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
 * Role Database Library Class
 *
 * @package GlueServer
 * @subpackage Role
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_group_db_base {
    
     /**
     * Returns an array of the roles of a user in a course
     *
     *
     * @return array of courses created in Moodle
     */
    public static function glueserver_get_groups_courseid($courseid){
        global $DB,$CFG;
        
        $sql = "SELECT g.*
                FROM {$CFG->prefix}groups g
                WHERE g.courseid = ?";
        $sqlparams = array($courseid);
        return $DB->get_records_sql($sql, $sqlparams);
    }
    
     /**
     * Returns an array of the users in a group
     *
     *
     * @return array of courses created in Moodle
     */
    public static function glueserver_get_users_groupid($groupid){
        global $DB,$CFG;
        
        $sql = "SELECT usr.*
                FROM {$CFG->prefix}user usr
                INNER JOIN {$CFG->prefix}groups_members gm ON gm.userid = usr.id
                WHERE gm.groupid = ?
                		AND usr.deleted = 0";
        $sqlparams = array($groupid);
        return $DB->get_records_sql($sql, $sqlparams);
    }
    
    
    public static function glueserver_get_users_courseid($courseid){
    	global $DB,$CFG;

        $sql = "SELECT usr.*, g.id as g_id, g.courseid as g_courseid, g.name as g_name, g.description as g_description
                FROM {$CFG->prefix}user usr
                INNER JOIN {$CFG->prefix}groups_members gm ON gm.userid = usr.id
                INNER JOIN {$CFG->prefix}groups g ON g.id = gm.groupid
                INNER JOIN {$CFG->prefix}course c ON c.id = g.courseid
                WHERE g.courseid = ?
                	AND usr.deleted = 0
                ORDER BY usr.id, g_id";               
        $sqlparams = array($courseid);
        $records = $DB->get_recordset_sql($sql, $sqlparams);
        //$records = $DB->get_records_sql($sql, $sqlparams); use the previous function instead of this one because there could be several records with the same usr.id
        foreach($records as $record){
        	//Check if the group information is already stored
        	if (!isset($return_records[$record->g_id])){
        		$return_records[$record->g_id] = array();
        		$group = new stdClass();
        		$group->id = $record->g_id;
        		$group->courseid = $record->g_courseid;
        		$group->name = $record->g_name;
        		$group->description = $record->g_description;
        		$return_records[$record->g_id]["group"] = $group;
        	}
        	//Add the information for the user in the group
        	$return_records[$record->g_id]["user"][] = $record;
        }
        
        $records = glueserver_group_db_base::glueserver_get_groups_courseid($courseid);
        foreach($records as $record){
        	//Check if the group information is already stored
        	if (!isset($return_records[$record->id])){
        		$return_records[$record->id] = array();
        		$group = new stdClass();
        		$group->id = $record->id;
        		$group->courseid = $record->courseid;
        		$group->name = $record->name;
        		$group->description = $record->description;
        		$return_records[$record->id]["group"] = $group;
        	}
        }
        return $return_records;
    	
    	
    }
    

}