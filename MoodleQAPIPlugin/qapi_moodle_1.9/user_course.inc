<?php
// $Id$

/**
 * @file
 * Class to look up a user's courses.
 *
 * @author Chris Johnson <christopher.johnson@openband.net>
 * @copyright Copyright (c) 2008.
 * @license http://www.gnu.org/copyleft/gpl.html GNU Public License
 * @package quickapi
 */

class User_Course implements Rest_Resource {
    public function __construct() {
    }

    public function lookup($elems) {
        global $CFG;                        // yuck!  Moodle! todo

        $username = urldecode($elems[2]);  // todo  avoid SQL injection but allow all possible legal OpenIDs and/or Drupal usernames?

        // clean name to match Moodle OpenID scheme for usernames in database.
        // from moodle/auth/openid/lib.php, openid_normalize_url_as_username():
        // only needed if username is an openid url.  sometimes it is, sometimes it isn't.
        if (substr($username, 0, 4) == 'http') {
            $username = eregi_replace('[^a-z0-9]', '', $username);
            $username = substr($username, 0, 90); // Keep it within limits of schema
        }


/*
 * Moodle stupidly has no recorded hard end date.  Best it has is "numsections"
 * of week-long sections, it seems.  This may need revision with better info.
 * Hence, end date is calculated as:
 * date_add(from_unixtime(startdate), interval numsections week)
 * Named, add numsections of weeks to the startdate to get end date.
 * 
 * Useful list of courses in which a user is currently enrolled:  defined as
 * courses whose end date has not yet passed.  Once a course has ended, a user
 * is no longer effectively enrolled.  Hence:
 * WHERE now() < date_add(from_unixtime(startdate), interval numsections week)
 *
 * Courses in-progress for a user are further defined as all those courses in
 * which a user is currently enrolled AND the course has started, thus, in
 * addition to the above qualification for enrolled, courses:
 * WHERE startdate < unix_timestamp()
 *
 * Courses "completed" should examine some part of the Moodle database to see
 * if there is some grade or assignment completion or something, but this is
 * difficult to determine, if at all.  Too many variables.  Instead, we define
 * it here as courses in which a user WAS enrolled, i.e. the ending date is in
 * the past, hence:
 * WHERE now() > date_add(from_unixtime(startdate), interval numsections week)
 */

        $end_date_sql = 'date_add(from_unixtime(startdate),
                                  interval numsections week)';
        $status = $elems[3];            // the course-user status
        switch ($status) {
        case 'inprogress':
            // a course is in-progress when it has started AND not yet ended.
            $condition = "startdate < unix_timestamp() AND now() < $end_date_sql ";
            break;
        case 'completed':
            // a student's course is completed when the course has ended.
            $condition = "now() > $end_date_sql";
            break;
        case 'enrolled':
            // a student is enrolled when a course has not yet ended.
        default:
            // default to enrolled
            $condition = "now() < $end_date_sql";
        }

        // Find all courses for a user according to above criteria:
        $sql = <<<END
SELECT
    c.id as cid, 
    c.fullname,
    c.shortname,
    c.summary,
    from_unixtime(c.startdate, '%Y-%m-%d') as startdate,
    c.numsections,
    c.format
FROM {$CFG->prefix}course c
    INNER JOIN {$CFG->prefix}context cx ON c.id = cx.instanceid
        AND cx.contextlevel = '50'
    INNER JOIN {$CFG->prefix}role_assignments ra ON cx.id = ra.contextid
    INNER JOIN {$CFG->prefix}role r ON ra.roleid = r.id
    INNER JOIN {$CFG->prefix}user usr ON ra.userid = usr.id
WHERE usr.username = '$username'
      AND $condition
ORDER BY startdate
END;

        $result = get_records_sql($sql);

        $output = array();
        foreach ($result as $cid => $course) {
            $output[$cid] = array('fullname' => $course->fullname,
                                'summary' => $course->summary,
                                'shortname' => $course->shortname,
                                'numweeks' => $course->numsections,
                                'urlpath' => '/course/view.php?id=' . $cid,
                                'startdate' => $course->startdate,
                            );
        }
        return $output;
    }
}
?>
