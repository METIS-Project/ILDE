<?php
// $Id$

/**
 * @file
 * Class to return courses sorted by instructor.
 *
 * @author Chris Johnson <christopher.johnson@openband.net>
 * @copyright Copyright (c) 2008.
 * @license http://www.gnu.org/copyleft/gpl.html GNU Public License
 * @package quickapi
 */

class Course_Instructors implements Rest_Resource {
    public function __construct() {
    }

    public function lookup($elems) {
        global $CFG;                        // yuck!  Moodle! todo

        if (empty($CFG->coursemanager)) {
            return array('error' => 'empty coursemanager');
        }
        // $CFG->coursemanager = comma-delimited list of instructor role id's
        $courseid = $elems[2];            // the course id

        $sql = <<<END
SELECT
    usr.id,
    concat(usr.firstname, ' ', usr.lastname) AS instructor
FROM {$CFG->prefix}course c
    INNER JOIN {$CFG->prefix}context cx ON c.id = cx.instanceid
        AND cx.contextlevel = '50'
    INNER JOIN {$CFG->prefix}role_assignments ra ON cx.id = ra.contextid
    INNER JOIN {$CFG->prefix}role r ON ra.roleid = r.id
    INNER JOIN {$CFG->prefix}user usr ON ra.userid = usr.id
WHERE
    c.id = $courseid
    AND ra.roleid IN ($CFG->coursemanager)
ORDER BY 
    instructor
END;

        $result = get_records_sql($sql);

        $output = array();
        foreach ($result as $uid => $record) {
            $output[$record->id] = $record->instructor;
        }
        return $output;
    }
}
?>
