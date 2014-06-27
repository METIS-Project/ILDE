<?php
// $Id$

/**
 * @file
 * Class to look up coures starting today or later.
 *
 * @author Chris Johnson <christopher.johnson@openband.net>
 * @copyright Copyright (c) 2008.
 * @license http://www.gnu.org/copyleft/gpl.html GNU Public License
 * @package quickapi
 */

class Upcoming_Course implements Rest_Resource {
    public function __construct() {
    }

    public function lookup($elems) {
    global $CFG;                        // yuck!  Moodle! todo

        $sql = <<<END
SELECT
    c.id as cid, 
    c.fullname,
    c.shortname,
    c.summary,
    from_unixtime(c.startdate, '%Y-%m-%d') as startdate,
    c.timemodified,
    c.numsections,
    c.format
FROM {$CFG->prefix}course c
    INNER JOIN {$CFG->prefix}context cx ON c.id = cx.instanceid
        AND cx.contextlevel = '50'
WHERE 
    startdate > UNIX_TIMESTAMP()
ORDER BY startdate
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
                                'timemodified' => $course->timemodified,
                            );
        }
        return $output;
    }
}
?>
