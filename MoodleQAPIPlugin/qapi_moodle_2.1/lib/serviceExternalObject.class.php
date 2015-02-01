<?php
// This file is part of Moodbile -- http://moodbile.org
//
// Moodbile is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Moodbile is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Moodbile.  If not, see <http://www.gnu.org/licenses/>.

/**
 * External Object Class
 *
 * @package MoodbileServer
 * @subpackage Lib
 * @copyright 2010 Maria José Casañ, Marc Alier, Jordi Piguillem, Nikolas Galanis marc.alier@upc.edu
 * @copyright 2010 Universitat Politecnica de Catalunya - Barcelona Tech http://www.upc.edu
 *
 * @author Jordi Piguillem
 * @author Nikolas Galanis
 * @author Oscar Martinez Llobet
 *
 * @license http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 *
 */

abstract class ServiceExternalObject {

    private $externalObject = array();

    function __construct($record) {
        $struct = $this->get_class_structure();
        foreach ($struct->keys as $key=>$value){
            if (!isset($record->$key) && ($value->allownull != NULL_ALLOWED)) {
                throw new moodle_exception("Missing " . $key . " field");
            }
            if (!isset($record->$key) && ($value->required == VALUE_OPTIONAL)) {
                $record->$key = $value->default;
            }

            // Transform numbers to the correct format instead of strings
            if (($value->type == PARAM_INT) || ($value->type == PARAM_INTEGER) ||
                 ($value->type == PARAM_FLOAT) ||($value->type == PARAM_NUMBER)) {
                settype($record->$key, $value->type);
            }

            $this->externalObject[$key] = $record->$key;
        }
    }

    abstract public static function get_class_structure();

    function get_data(){
        return $this->externalObject;
    }
}