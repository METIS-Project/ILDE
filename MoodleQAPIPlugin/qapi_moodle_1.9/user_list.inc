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
 * Modificaci�n de Juan <juanmunoz@gsic.uva.es>
 * Version 2: acceso a lista de usuarios NO ELIMINADOS
 */

class User_List implements Rest_Resource {
    public function __construct() {
    }

    public function lookup($elems) {
        global $CFG;                        // yuck!  Moodle! todo

        if (isset($elems[1])) {
            $whereclause = " WHERE u.auth = '{$elems[1]}' ";
        }
        else {
 //  Quito esta l�nea y pongo la siguiente para obtener �nicamente los alumnos no eliminados. Esto no est� tenido en cuenta en la autentificaci�n, ya que no se usa
 //           $whereclause = '';

            $whereclause = " WHERE u.deleted = 0 ";
        }

        $sql = <<<END
SELECT
    u.id,
    u.username,
    u.auth,
    u.email,
    u.firstaccess,
    concat(u.firstname, ' ', u.lastname) AS name
FROM {$CFG->prefix}user u
    $whereclause
ORDER BY 
    u.id
END;

        $result = get_records_sql($sql);

        $output = array();
        foreach ($result as $uid => $record) {
            if ($record->auth == 'openid') {
                // reverse mapping of OpenIDs made by auth/openid/lib.php,
                // openid_normalize_url_as_username()
            }
            $output[$record->id] = array($record->username, $record->name, $record->email, $record->firstaccess, $record->auth);
        }
        return $output;
    }
}
?>