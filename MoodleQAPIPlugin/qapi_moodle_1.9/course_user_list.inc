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
 *
 * Modificación de Juan para listar alumnos matriculados en un curso, indicando si son alumnos o no
 */

class Course_User_List implements Rest_Resource {
    public function __construct() {
    }

    public function lookup($elems) {
        global $CFG;                        // yuck!  Moodle! todo

		
        if (isset($elems[2])) {
            $courseid = urldecode($elems[2]); 
            $whereclause = "AND c.id = $courseid ";
        }
        else {
            $whereclause = '';
        }



        $sql = <<<END
SELECT
    c.id as cid,
    c.fullname,
    u.id as uid,
    u.username,
    u.auth,
    u.email,
    u.firstaccess,
    concat(u.firstname, ' ', u.lastname) AS name,
    ra.contextid,
    r.shortname as role
FROM {$CFG->prefix}course c, {$CFG->prefix}user u, {$CFG->prefix}role_assignments ra, {$CFG->prefix}context cx, {$CFG->prefix}role r
WHERE cx.contextlevel = 50 AND ra.userid = u.id AND cx.id = ra.contextid AND c.id = cx.instanceid AND r.id = ra.roleid AND u.deleted = 0 $whereclause
ORDER BY c.id, u.id
END;

        $result = get_recordset_sql($sql);
//        $result = recordset_to_array($resultset);

        $output = array();
		$i = 1;
 //       foreach ($result as $record) 
		while ($record = rs_fetch_next_record($result))
		{			
//			$output [$i++]= array($record->uid, $record->username, $record->name, $record->email, $record->firstaccess, $record->auth, $record->contextid, $record->fullname, $record->cid, $record->role);
			$output [$i++]= array($record->uid, $record->username, $record->name, $record->email, $record->firstaccess, $record->auth, $record->fullname, $record->cid, $record->role);
			
        }
		rs_close($result);
        return $output;
    }
}
?>
