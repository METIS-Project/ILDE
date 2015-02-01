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
 * Module Database Library Class
 *
 * @package GlueServer
 * @subpackage Module
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

abstract class glueserver_module_db_base {
    
     /**
     * Returns an array of the modules of Moodle
     *
     *
     * @return array of modules in Moodle
     */
    public static function glueserver_get_modules(){
        global $DB,$CFG;
        
        $sql = "SELECT m.*
                FROM {$CFG->prefix}modules m";
        $sqlparams = array();
        return $DB->get_records_sql($sql, $sqlparams);
    }

}