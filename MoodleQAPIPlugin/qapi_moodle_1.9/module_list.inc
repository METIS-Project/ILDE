<?php
// $Id$

/**
 * @file
 * Class to return a list of users in a form mappable to external auth system.
 *
 * @author Chris Johnson <christopher.johnson@openband.net>
 * @copyright Copyright (c) 2009.
 * @license http://www.gnu.org/copyleft/gpl.html GNU Public License
 * @package quickapi
 */

class Module_List implements Rest_Resource {
    public function __construct() {
    }

    public function lookup($elems) {
        global $CFG;                        // yuck!  Moodle! todo

 /*      if (isset($elems[1])) {
  *          $whereclause = " WHERE u.auth = '{$elems[1]}' ";
  *      }
  *      else {
  *          $whereclause = '';
  *      }
  */

        $sql = <<<END
SELECT
    m.id,
    m.name,
    m.visible
FROM {$CFG->prefix}modules m
ORDER BY 
    m.id
END;

        $result = get_records_sql($sql);

        $output = array();
        foreach ($result as $mid => $record) {
      
       
            $output[$record->id] = array($record->name, $record->visible);
        } 
        return $output;
    } 
}
?>
