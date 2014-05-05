<?php
	/**
	 * User settings for notifications.
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

	global $NOTIFICATION_HANDLERS;
	$notification_settings = get_user_notification_settings(page_owner());
	
?>
	<h3><?php echo T("Notification settings"); ?></h3>
	
	<p><?php echo T("Please specify you want to receive notifications by e-mail."); ?></p>

<?php

    if (!get_loggedin_user()->disable_user_notifications) {
        $val = "yes";
    } else {
        $val = "no";
    }
    echo elgg_view('input/radio',array('internalname' => "disable_user_notifications", 'options' => array(elgg_echo('option:yes') => 'yes',elgg_echo('option:no') => 'no'), 'value' => $val));
