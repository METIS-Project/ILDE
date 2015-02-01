<?php
// Moodle is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Moodle is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Moodle.  If not, see <http://www.gnu.org/licenses/>.

/**
 * Glue server plugin external functions and service definitions.
 *
 * @package GlueServer
 * @subpackage db
 *
 * @author Javier Enrique Hoyos Torio
 *
 */

$functions = array(

    // === course related functions ===
    'gws_course_get_all_courses'=> array(
        'classname'   => 'glueserver_course_external',
        'methodname'  => 'get_all_courses',
        'classpath'   => 'local/glueserver/course/externallib.php',
        'description' => 'Returns the details of all the courses of the Moodle installation',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_get_courses_userid'=> array(
        'classname'   => 'glueserver_course_external',
        'methodname'  => 'get_courses_userid',
        'classpath'   => 'local/glueserver/course/externallib.php',
        'description' => 'Returns the details of all the courses of a user by the id',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_get_courses_username'=> array(
        'classname'   => 'glueserver_course_external',
        'methodname'  => 'get_courses_username',
        'classpath'   => 'local/glueserver/course/externallib.php',
        'description' => 'Returns the details of all the courses of a user by the username',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_get_sections_course'=> array(
        'classname'   => 'glueserver_course_external',
        'methodname'  => 'get_sections_course',
        'classpath'   => 'local/glueserver/course/externallib.php',
        'description' => 'Returns the details of all the sections in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_get_module'=> array(
        'classname'   => 'glueserver_course_external',
        'methodname'  => 'get_module',
        'classpath'   => 'local/glueserver/course/externallib.php',
        'description' => 'Returns the details of a module in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_get_modules_course'=> array(
        'classname'   => 'glueserver_course_external',
        'methodname'  => 'get_modules_course',
        'classpath'   => 'local/glueserver/course/externallib.php',
        'description' => 'Returns the details of all the modules in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_update_module'=> array(
        'classname'   => 'glueserver_course_external',
        'methodname'  => 'update_module',
        'classpath'   => 'local/glueserver/course/externallib.php',
        'description' => 'Update a module in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_update_section'=> array(
        'classname'   => 'glueserver_course_external',
        'methodname'  => 'update_section',
        'classpath'   => 'local/glueserver/course/externallib.php',
        'description' => 'Update a section in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_insert_module'=> array(
        'classname'   => 'glueserver_course_external',
        'methodname'  => 'insert_module',
        'classpath'   => 'local/glueserver/course/externallib.php',
        'description' => 'Inserts a module in the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_delete_module'=> array(
        'classname'   => 'glueserver_course_external',
        'methodname'  => 'delete_module',
        'classpath'   => 'local/glueserver/course/externallib.php',
        'description' => 'Deletes a module from the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
     // === url related functions ===
	'gws_course_get_urls_course'=> array(
        'classname'   => 'glueserver_url_external',
        'methodname'  => 'get_urls_course',
        'classpath'   => 'local/glueserver/url/externallib.php',
        'description' => 'Returns the details of all the urls in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_insert_url_course'=> array(
        'classname'   => 'glueserver_url_external',
        'methodname'  => 'insert_url_course',
        'classpath'   => 'local/glueserver/url/externallib.php',
        'description' => 'Inserts a resource of tipe URL into the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
   	'gws_course_delete_url_course'=> array(
        'classname'   => 'glueserver_url_external',
        'methodname'  => 'delete_url_course',
        'classpath'   => 'local/glueserver/url/externallib.php',
        'description' => 'Deletes a resource of tipe URL from the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    // === forum related functions ===
	'gws_course_get_forums_course'=> array(
        'classname'   => 'glueserver_forum_external',
        'methodname'  => 'get_forums_course',
        'classpath'   => 'local/glueserver/forum/externallib.php',
        'description' => 'Returns the details of all the forums in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_insert_forum_course'=> array(
        'classname'   => 'glueserver_forum_external',
        'methodname'  => 'insert_forum_course',
        'classpath'   => 'local/glueserver/forum/externallib.php',
        'description' => 'Inserts a resource of tipe Forum into the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
   	'gws_course_delete_forum_course'=> array(
        'classname'   => 'glueserver_forum_external',
        'methodname'  => 'delete_forum_course',
        'classpath'   => 'local/glueserver/forum/externallib.php',
        'description' => 'Deletes a resource of type Forum from the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
     // === chat related functions ===
	'gws_course_get_chats_course'=> array(
        'classname'   => 'glueserver_chat_external',
        'methodname'  => 'get_chats_course',
        'classpath'   => 'local/glueserver/chat/externallib.php',
        'description' => 'Returns the details of all the chats in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_insert_chat_course'=> array(
        'classname'   => 'glueserver_chat_external',
        'methodname'  => 'insert_chat_course',
        'classpath'   => 'local/glueserver/chat/externallib.php',
        'description' => 'Inserts a resource of type chat into the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
   	'gws_course_delete_chat_course'=> array(
        'classname'   => 'glueserver_chat_external',
        'methodname'  => 'delete_chat_course',
        'classpath'   => 'local/glueserver/chat/externallib.php',
        'description' => 'Deletes a resource of type chat from the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    // === quiz related functions ===
	'gws_course_get_quizzes_course'=> array(
        'classname'   => 'glueserver_quiz_external',
        'methodname'  => 'get_quizzes_course',
        'classpath'   => 'local/glueserver/quiz/externallib.php',
        'description' => 'Returns the details of all the quizzes in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view'
    ),
    
    'gws_course_insert_quiz_course'=> array(
        'classname'   => 'glueserver_quiz_external',
        'methodname'  => 'insert_quiz_course',
        'classpath'   => 'local/glueserver/quiz/externallib.php',
        'description' => 'Inserts a resource of type quiz into the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view'
    ),
     
   	'gws_course_delete_quiz_course'=> array(
        'classname'   => 'glueserver_quiz_external',
        'methodname'  => 'delete_quiz_course',
        'classpath'   => 'local/glueserver/quiz/externallib.php',
        'description' => 'Deletes a resource of type quiz from the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view'
    ),
    
    // === assignment related functions ===
	'gws_course_get_assignments_course'=> array(
        'classname'   => 'glueserver_assignment_external',
        'methodname'  => 'get_assignments_course',
        'classpath'   => 'local/glueserver/assignment/externallib.php',
        'description' => 'Returns the details of all the assignments in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_insert_assignment_course'=> array(
        'classname'   => 'glueserver_assignment_external',
        'methodname'  => 'insert_assignment_course',
        'classpath'   => 'local/glueserver/assignment/externallib.php',
        'description' => 'Inserts a resource of tipe Assignment into the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
   	'gws_course_delete_assignment_course'=> array(
        'classname'   => 'glueserver_assignment_external',
        'methodname'  => 'delete_assignment_course',
        'classpath'   => 'local/glueserver/assignment/externallib.php',
        'description' => 'Deletes a resource of type Assignment from the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    // === page related functions ===
	'gws_course_get_pages_course'=> array(
        'classname'   => 'glueserver_page_external',
        'methodname'  => 'get_pages_course',
        'classpath'   => 'local/glueserver/page/externallib.php',
        'description' => 'Returns the details of all the pages in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_course_insert_page_course'=> array(
        'classname'   => 'glueserver_page_external',
        'methodname'  => 'insert_page_course',
        'classpath'   => 'local/glueserver/page/externallib.php',
        'description' => 'Inserts a resource of type Page into the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
   	'gws_course_delete_page_course'=> array(
        'classname'   => 'glueserver_page_external',
        'methodname'  => 'delete_page_course',
        'classpath'   => 'local/glueserver/page/externallib.php',
        'description' => 'Deletes a resource of type Page from the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    // === group related functions ===
    'gws_group_get_groups_courseid'=> array(
        'classname'   => 'glueserver_group_external',
        'methodname'  => 'get_groups_courseid',
        'classpath'   => 'local/glueserver/group/externallib.php',
        'description' => 'Returns the details of all the groups of a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_group_get_users_groupid'=> array(
        'classname'   => 'glueserver_group_external',
        'methodname'  => 'get_users_groupid',
        'classpath'   => 'local/glueserver/group/externallib.php',
        'description' => 'Returns the details of all the users in a group',
        'type'        => 'read',
        'capabilities'=> '',
    ),
    
    'gws_group_get_users_courseid'=> array(
        'classname'   => 'glueserver_group_external',
        'methodname'  => 'get_users_courseid',
        'classpath'   => 'local/glueserver/group/externallib.php',
        'description' => 'Returns the details of all the groups of a course and the users in each group',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    // === grouping related functions ===
    'gws_course_get_groupings_course'=> array(
        'classname'   => 'glueserver_grouping_external',
        'methodname'  => 'get_groupings_course',
        'classpath'   => 'local/glueserver/grouping/externallib.php',
        'description' => 'Returns the details of all the groupings in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_grouping_create_grouping'=> array(
        'classname'   => 'glueserver_grouping_external',
        'methodname'  => 'create_grouping',
        'classpath'   => 'local/glueserver/grouping/externallib.php',
        'description' => 'Create groupings in a course',
        'type'        => 'read',
        'capabilities'=> ''
    ),
    
    'gws_grouping_assign_grouping'=> array(
        'classname'   => 'glueserver_grouping_external',
        'methodname'  => 'assign_grouping',
        'classpath'   => 'local/glueserver/grouping/externallib.php',
        'description' => 'assign a group to a grouping',
        'type'        => 'read',
        'capabilities'=> ''
    ),
    
    'gws_grouping_delete_grouping_course'=> array(
        'classname'   => 'glueserver_grouping_external',
        'methodname'  => 'delete_grouping_course',
        'classpath'   => 'local/glueserver/grouping/externallib.php',
        'description' => 'Delete a grouping',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),

    // === user related functions ===
    'gws_user_get_all_users'=> array(
        'classname'   => 'glueserver_user_external',
        'methodname'  => 'get_all_users',
        'classpath'   => 'local/glueserver/user/externallib.php',
        'description' => 'Returns the details of all the users of the Moodle installation',
        'type'        => 'read',
        'capabilities'=> ''
    ),
    
    'gws_user_get_users_courseid'=> array(
        'classname'   => 'glueserver_user_external',
        'methodname'  => 'get_users_courseid',
        'classpath'   => 'local/glueserver/user/externallib.php',
        'description' => 'Returns the details of all the users of a course',
        'type'        => 'read',
        'capabilities'=> ''
    ),
    
    'gws_user_check_user_credentials'=> array(
        'classname'   => 'glueserver_user_external',
        'methodname'  => 'check_user_credentials',
        'classpath'   => 'local/glueserver/user/externallib.php',
        'description' => 'Returns the details of the user matching these credentials',
        'type'        => 'read',
        'capabilities'=> ''
    ),
    
    // === role related functions ===
    'gws_role_get_roles_userid'=> array(
        'classname'   => 'glueserver_role_external',
        'methodname'  => 'get_roles_userid',
        'classpath'   => 'local/glueserver/role/externallib.php',
        'description' => 'Returns the details of all the roles of a user in a course',
        'type'        => 'read',
        'capabilities'=> ''
    ),
    
    // === enrol related functions ===
    'gws_enrol_get_enrols_course'=> array(
        'classname'   => 'glueserver_enrol_external',
        'methodname'  => 'get_enrols_course',
        'classpath'   => 'local/glueserver/enrol/externallib.php',
        'description' => 'Returns the details of all the enrols in a course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_enrol_set_enrol'=> array(
        'classname'   => 'glueserver_enrol_external',
        'methodname'  => 'set_enrol',
        'classpath'   => 'local/glueserver/enrol/externallib.php',
        'description' => 'Add the enrol methods',
        'type'        => 'read',
        'capabilities'=> ''
    ),
    
    'gws_course_delete_enrol_course'=> array(
        'classname'   => 'glueserver_enrol_external',
        'methodname'  => 'delete_enrol_course',
        'classpath'   => 'local/glueserver/enrol/externallib.php',
        'description' => 'Deletes a resource of type Enrol from the course',
        'type'        => 'read',
        'capabilities'=> 'moodle/course:view',
    ),
    
    'gws_enrol_disenroll_course_user'=> array(
        'classname'   => 'glueserver_enrol_external',
        'methodname'  => 'disenroll_course_user',
        'classpath'   => 'local/glueserver/enrol/externallib.php',
        'description' => 'Disenroll a user from the course',
        'type'        => 'read',
        'capabilities'=> ''
    ),
    
        // === module related functions ===
    'gws_module_get_modules'=> array(
        'classname'   => 'glueserver_module_external',
        'methodname'  => 'get_modules',
        'classpath'   => 'local/glueserver/module/externallib.php',
        'description' => 'Returns the details of all the modules in Moodle',
        'type'        => 'read',
        'capabilities'=> '',
    )
    
);

$functionlist = array();
foreach ($functions as $key=>$value) {
    $functionlist[] = $key;
}

$services = array(
   'Glue web service'  => array(  		//the name of the webservice
        'functions' => $functionlist,	//web service functions of this service
        'enabled' => 1,					//if enabled, the service can be reachable on a default installation
        'restrictedusers' => 0,			//if enabled, the Moodle administrator must link some user to this service
    ),
);

