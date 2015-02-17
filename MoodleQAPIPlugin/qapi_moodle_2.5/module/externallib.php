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
 * Module External Library
 *
 * @package GlueServer
 * @subpackage Module
 * @author Javier Enrique Hoyos Torio
 *
 */
 
if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}
require_once(dirname(__FILE__).'/../config.php');
global $GWS,$CFG;
require_once($GWS->gwsroot . '/module/moodle_module.class.php');
require_once($GWS->gwsroot . '/module/db/moduleDB.php');
require_once($CFG->libdir . '/externallib.php');

class glueserver_module_external extends external_api{
	
	 /**
     * Returns description of method parameters
     * @return external_function_parameters
     */
	public static function get_modules_parameters(){
        return new external_function_parameters(
            array()
        );
    }
    
     /**
     *
     * Function to get list of modules available in Moodle
     *
     * @return modules details
     */
    public static function get_modules(){
        $modules = glueserver_module_db::glueserver_get_modules();
        $returns = array();
        foreach ($modules as $module) {
            $mod = new glueserver_moodle_module($module);
            $returns[] = $mod->get_data();
        }
        return $returns;
    }
    
     /**
     * Returns description of method result value
     * @return external_description
     */
    public static function get_modules_returns() {
        return new external_multiple_structure(
            glueserver_moodle_module::get_class_structure()
        );
    }
	
}

?>
