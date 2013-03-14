<?php

	/**
	 * Elgg add action
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

	require_once(dirname(dirname(__FILE__)) . "/engine/start.php");

	admin_gatekeeper(); // Only admins can make someone an admin
	action_gatekeeper();
	global $CONFIG;

	// Get variables
	$username = get_input('username');
	$password = get_input('password');
	$password2 = get_input('password2');
	$email = get_input('email');
	$name = get_input('name');
	
	$admin = get_input('admin');
	if (is_array($admin)) $admin = $admin[0];
	
	// For now, just try and register the user
	try {
		if (
			(
				(trim($password)!="") &&
				(strcmp($password, $password2)==0) 
			) &&
			($guid = register_user($username, $password, $name, $email, true))
		) {
			$new_user = get_entity($guid);
			if (($guid) && ($admin))
				$new_user->admin = 'yes';
			
			$new_user->admin_created = true;
			$new_user->isnew = '1';

            $vars = array();
            $vars['new_user'] = $new_user;
            $vars['site'] = $CONFIG->site;
            $vars['site_url'] = $CONFIG->url;
            $vars['password'] = $password;

            $body = elgg_view('emails/newmember',$vars);


            notify_user (
                $guid,
                $_SESSION['user']->getGUID(),
                T("Welcome to %1!", $CONFIG->site->name),
                $body,
                array('html' => true)
            );

			
			system_message(sprintf(elgg_echo("adduser:ok"),$CONFIG->sitename));
		} else {
			register_error(elgg_echo("adduser:bad"));
		}
	} catch (RegistrationException $r) {
		register_error($r->getMessage());
	}

	forward($_SERVER['HTTP_REFERER']);
	exit;
?>