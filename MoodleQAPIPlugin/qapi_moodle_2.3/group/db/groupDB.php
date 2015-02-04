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
 * Group Database Version Selector
 *
 * @package GlueServer
 * @subpackage Group
 * @author Javier Enrique Hoyos Torio
 *
 */

if (!defined('MOODLE_INTERNAL')) {
    die('Direct access to this script is forbidden.');    ///  It must be included from a Moodle page
}

// Load Moodle Config
require_once(dirname(__FILE__).'/../../config.php');

// Load GlueWS Config
global $GWS, $CFG;

$moodleversion = substr($CFG->release, 0, 3);
if ($moodleversion == '2.0' or $moodleversion == '2.1' or $moodleversion == '2.2' or $moodleversion == '2.3'){
    require_once($GWS->gwsroot . '/group/db/m20/groupDB.class.php');
} else if ($moodleversion == '1.9'){
    require_once($GWS->gwsroot . '/group/db/m19/groupDB.class.php');
}

