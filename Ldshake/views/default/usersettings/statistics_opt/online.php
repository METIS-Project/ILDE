<?php
	/**
	 * Elgg statistics screen showing online users.
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

	$user = $_SESSION['user'];
	
	$logged_in = 0;
	$log = get_system_log($_SESSION['user']->guid, "login", "ElggUser", "user");
	if ($log)
		$logged_in=$log[0]->time_created;
	
?>
<div class="usersettings_statistics">
	<h3><?php echo elgg_echo('usersettings:statistics:yourdetails'); ?></h3>
	
	<table>
		<tr class="odd"><td class="column_one"><?php echo elgg_echo('usersettings:statistics:label:name'); ?></td><td><?php echo $user->name; ?></td></tr>
		<tr class="even"><td class="column_one"><?php echo elgg_echo('usersettings:statistics:label:email'); ?></td><td><?php echo $user->email; ?></td></tr>
		<tr class="odd"><td class="column_one"><?php echo elgg_echo('usersettings:statistics:label:membersince'); ?></td><td><span class="utc-time"><?php echo $user->time_created; ?></span></td></tr>
		<tr class="even"><td class="column_one"><?php echo elgg_echo('usersettings:statistics:label:lastlogin'); ?></td><td><span class="utc-time"><?php echo $logged_in; ?></span></td></tr>
	</table>
</div>