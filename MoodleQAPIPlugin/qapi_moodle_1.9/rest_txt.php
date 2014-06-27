<?php
// $Id: upcomingcourses.php 7301 2008-11-22 04:56:44Z chris $

/**
 * @file
 * RESTful web service for a variety of Moodle resources.
 *
 * Top-level driver for state transfer of resource representations.
 *
 * @author Chris Johnson <christopher.johnson@openband.net>
 * @copyright Copyright (c) 2008.
 * @license http://www.gnu.org/copyleft/gpl.html GNU Public License
 * @package quickapi
 */

define('QAPI_CLIENT_TAB', 'qapi_client'); // database table name, duplicated in index.php

interface Rest_Resource {
    public function lookup($path_elements);
}

if (!file_exists('../config.php')) {
    header('HTTP/1.1 403 Forbidden');
    header('Status: 403 Forbidden');
    print json_encode(array('error' => 'Moodle not properly installed; config.php missing.'));
    exit;
}

require_once('../config.php');
require_once($CFG->dirroot .'/course/lib.php');

if (empty($SITE)) {
    redirect($CFG->wwwroot .'/'. $CFG->admin .'/index.php');
}

// validate request came from a trusted client system
if (!trusted_client()) {
    header('HTTP/1.1 403 Forbidden');
    header('Status: 403 Forbidden');
    print json_encode(array('error' => 'authentication error for '.$_SERVER['REMOTE_ADDR']));
    exit;
}


// get resource path elements
$elements = explode('/', htmlspecialchars($_SERVER['PATH_INFO'], ENT_QUOTES));  // todo or should this be urldecode()?
array_shift($elements);                 // elements[0] is always empty

// interface Rest_Resource implementations:
// instantiate proper class for type of request
switch ($elements[0]) {
case 'course':
    switch ($elements[1]) {
    case 'user':
        include_once "user_course.inc";         // get this user's courses
        $resource = new User_Course;
        break;

    case 'upcoming':
        include_once "upcoming_course.inc";     // get upcoming courses
        $resource = new Upcoming_Course;
        break;

    case 'instructors':
        include_once "course_instructors.inc";  // get this course's instructors
        $resource = new Course_Instructors;
        break;
	// Modificación de Juan para incluir un listado de los cursos que tiene Moodle
	case 'all':
		include_once "all_courses.inc";    //get all courses
        $resource = new All_Courses;
        break;		
	//Modificación de Juan para incluir un listado de usuarios por curso, indicando si son alumnos o no
	case 'course_id':
		include_once "course_user_list.inc";    //get user from a course
        $resource = new Course_User_List;
        break;	

    default:
        $resource = new Error_Msg_Course;
    }
    break;

case 'userlist':
        include_once 'user_list.inc';           // get users, optionally by auth
        $resource = new User_List;
    break;

//añadido por Juan para listar los módulos
case 'modulelist':
        include_once 'module_list.inc';           // get modules
        $resource = new Module_List;
    break;


case 'debug':
    break;
default:
    $resource = new Error_Msg_Course;
}

$output = $resource->lookup($elements);

/* DEBUG */
if (0) {
    echo "<pre>";
    echo "input:\n";
    print_r($elements);
    echo "output:\n";
    print_r($output);
    echo "</pre>";
}
/* END DEBUG */

print json_encode($output);
exit;

class Error_Msg_Course {
    function lookup($elements) {
        $elements['error'] = 'unrecognized course request';
        return $elements;
        return array('error' => 'unrecognized course request');
    }
}

/**
 * validate a remote IP as a trusted site.
 */
function trusted_client() {
    global $CFG;

    $records = get_records(QAPI_CLIENT_TAB);
    $clientip = $_SERVER['REMOTE_ADDR'];

    foreach ($records as $record) {
        $trustedip = $record->clientip;
        $sharedkey = $record->sharedkey;

        if (empty($trustedip)) {
            return FALSE;
        }
        if ($clientip == $trustedip) {
            return TRUE;
        }
    }
    return FALSE;
}    
?>
