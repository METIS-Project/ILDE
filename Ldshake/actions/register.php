<?php

	/**
	 * Elgg registration action
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

	require_once(dirname(dirname(__FILE__)) . "/engine/start.php");
	global $CONFIG;
	
	action_gatekeeper();

	// Get variables
		$username = get_input('dfgeiyuh544e78gh5g');
		$password = get_input('dfgeyeyrehdfheheeryery4e78gh5g');
		$password2 = get_input('dfgeiyuhfdhteyeryheryerh5g');
		$email = get_input('hdg7867rehg54ht937hg398g4');
		$name = get_input('dfyhr745u5e4h45t3y545euy45hgtr');
		$friend_guid = (int) get_input('friend_guid',0);
		$invitecode = get_input('invitecode');
		
		/// LdShake change ///
		//Pau: We also get the expectatives field data:
		$expectations = get_input('whatsexpectatives');
		
		//Pau: Is it a spammer?
		/*
		require_once($CONFIG->path .'vendors/captcha/recaptchalib.php');
		$privatekey = $CONFIG->reCAPTCHA_private;
		$resp = recaptcha_check_answer ($privatekey,
                                $_SERVER["REMOTE_ADDR"],
                                $_POST["recaptcha_challenge_field"],
                                $_POST["recaptcha_response_field"]);
		*/
		
		$admin = get_input('admin');
		if (is_array($admin)) $admin = $admin[0];

		//if ($resp->is_valid)
		if (true)
		{
			if (!$CONFIG->disable_registration)
			{
		// For now, just try and register the user
		
				try {
                    if(function_exists("ldshake_mode_open_register_validation")) {
                        try {
                            ldshake_mode_open_register_validation();
                        } catch(Exception $e) {
                            throw new RegistrationException($e->getMessage());
                        }
                    }

					if (
						(
							(trim($password)!="") &&
							(strcmp($password, $password2)==0) 
						) &&
						($guid = register_user($username, $password, $name, $email, false, $friend_guid, $invitecode))
					) {
						
						$new_user = get_entity($guid);

                        if(!empty($CONFIG->register_params)) {
                            foreach($CONFIG->register_params as $param_func)
                                $param_func();
                        }
						//Pau: Save extra fields of register form
						$new_user->expectations = $expectations;
                        //$new_user->isnew = '1';
                        $title = T("My first LdS");
                        $description = elgg_view('lds/welcome_lds/welcome_lds_'.$CONFIG->language);

                        if(function_exists("ldshake_mode_open_register_welcome")) {
                            ldshake_mode_open_register_welcome($title, $description);
                        }

                        //Create new document
                        $wl = new LdSObject();
                        $wl->access_id = 2;
                        $wl->editor_type = 'doc';
                        $wl->editor_subtype = 'doc';
                        $wl->title = $title;
                        $wl->owner_guid = $new_user->guid;
                        $wl->container_guid = $new_user->guid;
                        $wl->granularity = '0';
                        $wl->completeness = '0';
                        $wl->all_can_view = "no";
                        $wl->welcome = 1;
                        $wl->save ();

                        $doc = new DocumentObject($wl->guid);
                        $doc->title = T("My first LdS");
                        $doc->description = $description;

                        $doc->owner_guid = $new_user->guid;
                        $doc->container_guid = $wl->guid;
                        $doc->access_id = 2;
                        $doc->save();

						$new_user->save();
						
						if (($guid) && ($admin))
						{
							admin_gatekeeper(); // Only admins can make someone an admin
							$new_user->admin = 'yes';
						}

						// Send user validation request on register only
						request_user_validation($guid);

                        if(function_exists("ldshake_mode_open_register")) {
                            ldshake_mode_open_register($new_user);
                            $new_user->save();
                        }

						if (!$new_user->admin)
							$new_user->disable('new_user');	// Now disable if not an admin

						system_message(sprintf(elgg_echo("registerok"),$CONFIG->sitename));
						
						forward(); // Forward on success, assume everything else is an error...
					} else {
						register_error(elgg_echo("registerbad"));
					}
				} catch (RegistrationException $r) {
					register_error($r->getMessage());
				}
			}
			else
				register_error(elgg_echo('registerdisabled'));
		}
		else
		{
			register_error("The reCAPTCHA wasn't entered correctly. Go back and try it again.");
		}
			
		$qs = explode('?',$_SERVER['HTTP_REFERER']);
		$qs = $qs[0];
		//TODO Pau: Això és cutre, s'hauria de passar per sessió, pq el camp expectatives pot ser molt llarg.
		$qs .= "?u=" . urlencode($username) . "&e=" . urlencode($email) . "&n=" . urlencode($name) . "&friend_guid=" . $friend_guid . "&exp=" . urlencode($expectations);
        if(function_exists("ldshake_mode_register_retry_params")) {
            $qs.= ldshake_mode_register_retry_params();
        }
		/// LdShake change ///
		forward($qs);

?>