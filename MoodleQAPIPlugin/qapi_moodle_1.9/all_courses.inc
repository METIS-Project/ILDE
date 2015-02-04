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
 * 
 * Modificación de Juan para listar los cursos configurados
 */

class All_Courses implements Rest_Resource {
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
    c.timemodified,
    c.numsections,
	c.visible,
	c.sortorder,
    c.format
FROM {$CFG->prefix}course c
    INNER JOIN {$CFG->prefix}context cx ON c.id = cx.instanceid
        AND cx.contextlevel = '50'
		AND c.visible = '1'
		AND c.sortorder != '0'
ORDER BY c.id
END;

        $result = get_records_sql($sql);

        $output = array();
        foreach ($result as $cid => $course) { 
$DirPath = "&#47course&#47view.php?id=";
           $output[$course->cid] = array($course->fullname,
                                $course->shortname,
                                $course->numsections,
                                $course->summary,
                                $DirPath . $cid,
                                $course->timemodified );
			}
							
        return $output;
    }
}
?>
