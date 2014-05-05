<?php
	/**
	 * Elgg notifications user preference save action.
	 *
     * @file modified by Interactive Technologies Group (GTI)
     * @authors (alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P..
     * @Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
     * @based on:
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

	// Method
	$method = get_input('method');
	$form_disable_user_notifications = get_input('disable_user_notifications');
	gatekeeper();

    $user = get_loggedin_user();
    $disable_user_notifications = null;
    $result = false;

switch($form_disable_user_notifications) {
    case 'yes':
        $disable_user_notifications = null;
        $result = true;
        break;
    case 'no':
        $disable_user_notifications = true;
        $result = true;
        break;
    default:
        $disable_user_notifications = true;
}

if($result) {
    $user->disable_user_notifications = $disable_user_notifications;
    $user->save();
    system_message(elgg_echo('notifications:usersettings:save:ok'));
} else {
    register_error(elgg_echo('notifications:usersettings:save:fail'));
}

/*
	$result = false;
	foreach ($method as $k => $v)
	{
		$result = set_user_notification_setting($_SESSION['user']->guid, $k, ($v == 'yes') ? true : false);
		
		if (!$result)
		{
			//register_error(elgg_echo('notifications:usersettings:save:fail'));
			//forward($_SERVER['HTTP_REFERER']);
			
			//exit;
		}
	}
	*/
	//else
		//register_error(elgg_echo('notifications:usersettings:save:fail'));
	
	//forward($_SERVER['HTTP_REFERER']);