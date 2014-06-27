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
 * Modificación de Juan para listar los grupos y sus miembros de un curso. 
 */

class Course_Group_List implements Rest_Resource {
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
	g.id as gid,
	g.courseid,
	g.name as gname,
	gm.id as gmid,
	gm.groupid,
	gm.userid
FROM {$CFG->prefix}course c, {$CFG->prefix}user u, {$CFG->prefix}groups g, {$CFG->prefix}groups_members gm
WHERE u.id = gm.userid AND u.deleted = 0 AND g.courseid = c.id AND g.id = gm.groupid $whereclause
ORDER BY c.id, g.id, gm.id
END;

        $result = get_recordset_sql($sql);
//        $result = recordset_to_array($resultset);

        $output = array();
		$i = 1;
 //       foreach ($result as $record) 
		while ($record = rs_fetch_next_record($result))
		{			
//			$output [$i++]= array($record->uid, $record->username, $record->name, $record->email, $record->firstaccess, $record->auth, $record->contextid, $record->fullname, $record->cid, $record->role);
			$output [$i++]= array($record->gid, $record->gname, $record->gmid, $record->uid, $record->username, $record->name, $record->email, $record->firstaccess, $record->auth, $record->fullname, $record->cid);
			
        }
		rs_close($result);
        return $output;
    }
}
?>
