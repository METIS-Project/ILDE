<?php
// $Id$

/**
 * @file
 * Class to return courses wherein a user is admin.
 *
 * @author Juan A. Muñoz (GSIC/EMIC)
 * @copyright Copyright (c) 2013.
 * @license http://www.gnu.org/copyleft/gpl.html GNU Public License
 * @package quickapi
 */

class All_Courses_Admin implements Rest_Resource {
    public function __construct() {
    }

    public function lookup($elems) {
        global $CFG;                        // yuck!  Moodle! todo

        if (empty($CFG->coursemanager)) {
            return array('error' => 'empty coursemanager');
        }
        // $CFG->coursemanager = comma-delimited list of instructor role id's
        $user = $elems[2];            // the username

        $sql = <<<END
SELECT
    c.id as cid, 
    c.fullname,
    c.shortname,
    c.summary,
    c.timemodified,
    c.numsections,
    c.visible,
	c.sortorder,
    c.format
FROM {$CFG->prefix}course c
    INNER JOIN {$CFG->prefix}context cx ON c.id = cx.instanceid
        AND cx.contextlevel = '50'
	AND c.sortorder != '0'
    INNER JOIN {$CFG->prefix}role_assignments ra ON cx.id = ra.contextid
    INNER JOIN {$CFG->prefix}role r ON ra.roleid = r.id
    INNER JOIN {$CFG->prefix}user usr ON ra.userid = usr.id
WHERE 
    usr.username="$user"
	AND ra.roleid = '1'	
ORDER BY 
    c.id	

END;

 // To show only visible courses, put before "AND c.sortorder" the next line: 	AND c.visible = '1'
        $result = get_records_sql($sql);

        $output = array();
		
		if ($result != null) {
		        foreach ($result as $cid => $course) { 
$DirPath = "&#47course&#47view.php?id=";
           $output[$course->cid] = array($course->fullname,
                                $course->shortname,
                                $course->numsections,
                                $course->summary,
                                $DirPath . $cid,
                                $course->timemodified );
			}
		
		}

							
        return $output;
    }
}
?>
