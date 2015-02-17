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
 * User Database Class for Moodle 1.9
 *
 * @package GlueServer
 * @subpackage User
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

require_once($CFG->dirroot . '/lib/password_compat/lib/password.php'); // We include this library in order to be able to call the password_hash function
require_once(dirname(__FILE__).'/../userDBbase.class.php');

class glueserver_user_db extends glueserver_user_db_base{
	
	
	  /* Returns an array with the user matching the provided credentials
     *
     *
     * @return array with the user matching the provided credentials. Otherwise, the array will be empty
     */
    public static function glueserver_check_user_credentials($username, $password){
    	$credentials = parent::glueserver_check_user_credentials($username, $password);
    	if (count($credentials)==0){//Check if the credentials are stored using the Moodle 2.5 way instead
			global $DB, $CFG;
			$db_password = glueserver_user_db::get_bd_password_field($username);
			if ($db_password!=null){
				$password_hash = glueserver_user_db::build_password_hash($password, $db_password);
			}else{
				$password_hash = "";
			}
			$sql = "SELECT usr.* FROM {$CFG->prefix}user usr ".
					"WHERE usr.username=? AND usr.password=?";
			$sqlparams = array($username, $password_hash);
			$credentials = $DB->get_records_sql($sql, $sqlparams);
    	}
        return $credentials;
    }
    
    private static function get_bd_password_field($username){    	
        global $DB, $CFG;
    	$sql = "SELECT usr.* FROM {$CFG->prefix}user usr WHERE usr.username=?";
        $sqlparams = array($username);
        $users = $DB->get_records_sql($sql, $sqlparams);
        if (count($users)>0){
           $user = current($users); //get the first (and the only) user
           return $user->password;
        }
        return null;
    }
	
	private static function build_password_hash($password, $db_password_field){
        $items = explode('$', $db_password_field);
        $hashing_algorithm_id = $items[1];
        $hashing_algorithm_cost = $items[2];
        $salt = substr($items[3],0,22);
        $hash = substr($items[3],22);
        $pass_hash = password_hash($password, PASSWORD_BCRYPT, array("cost" => $hashing_algorithm_cost, "salt" => $salt));
        return $pass_hash;
	}
        
        
}
