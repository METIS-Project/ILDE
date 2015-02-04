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

class Instructor_Course implements Rest_Resource {
    public function __construct() {
    }

    public function lookup($elems) {
        global $CFG;                        // yuck!  Moodle! todo

        if (empty($CFG->coursemanager)) {
            return array('error' => 'empty coursemanager');
        }
        // $CFG->coursemanager = comma-delimited list of instructor role ids

        $sql = <<<END
SELECT
    c.id as cid,
    c.fullname,
    c.shortname,
    from_unixtime(c.startdate, '%Y-%m-%d') as startdate,
    c.numsections,
    c.format,
    r.name,
    concat(usr.firstname, ' ', usr.lastname) AS instructor
FROM {$CFG->prefix}course c
    INNER JOIN {$CFG->prefix}context cx ON c.id = cx.instanceid
        AND cx.contextlevel = '50'
    INNER JOIN {$CFG->prefix}role_assignments ra ON cx.id = ra.contextid
    INNER JOIN {$CFG->prefix}role r ON ra.roleid = r.id
    INNER JOIN {$CFG->prefix}user usr ON ra.userid = usr.id
WHERE
    ra.roleid IN ($CFG->coursemanager)
ORDER BY 
    usr.username
END;

        $result = get_records_sql($sql);

        $output = array();
        foreach ($result as $cid => $course) {
            $output[$cid] = array('fullname' => $course->fullname,
                                'shortname' => $course->shortname,
                                'numweeks' => $course->numsections,
                                'summary' => $course->summary,
                                'urlpath' => '/course/view.php?id=' . $cid,
                                'startdate' => $course->startdate,
                                'role' => $course->name,
                                'instructor' => $course->instructor,
                            );
        }
        return $output;
    }
}
?>
