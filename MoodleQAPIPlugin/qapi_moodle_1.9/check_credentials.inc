<?php

/**
 * @file
 * Class to check if the credentials of the user in Moodle are valid
 *
 * @author Javier E. Hoyos Torio (GSIC/EMIC)
 * @copyright Copyright (c) 2013.
 * @license http://www.gnu.org/copyleft/gpl.html GNU Public License
 * @package quickapi
 */

class Check_Credentials implements Rest_Resource {
    public function __construct() {
    }

    public function lookup($elems) {
        global $CFG;                        // yuck!  Moodle! todo

        $user = $elems[2];            // the username
        $password = $elems[3];		  // the passwword
        $sql = "
		SELECT
		    u.id as uid,
		    u.username,
		    u.auth,
		    u.email,
		    u.firstaccess
		FROM {$CFG->prefix}user u
		WHERE u.username='".$user."'".
			"AND u.password=MD5('".$password.$CFG->passwordsaltmain."')";

        $result = get_records_sql($sql);

        $output = array();
		
		if ($result != null) {
		    foreach ($result as $uid => $user) { 
           		$output[$user->uid] = array($user->uid, $user->username, $user->auth, $user->email, $user->firstaccess);
			}		
		}
							
        return $output;
    }
}
?>
