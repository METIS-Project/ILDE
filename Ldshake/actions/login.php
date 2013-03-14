<?php

    /**
	 * Elgg login action
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
	 
	// Safety first
		action_gatekeeper();

    // Get username and password
    
        $username = get_input('username');
        $password = get_input("password");
        $persistent = get_input("persistent", false);
        
    // If all is present and correct, try to log in  
    	$result = false;          
        if (!empty($username) && !empty($password)) {
        	if ($user = authenticate($username,$password)) {
        		$result = login($user, $persistent);
        	}
        }
        
    // Set the system_message as appropriate
        /// LdShake change ///
        if ($result) {
            //Pau: Isn't it obvious?
        	//system_message(elgg_echo('loginok'));
            if ($_SESSION['last_forward_from'])
            {
            	$forward_url = $_SESSION['last_forward_from'];
            	$_SESSION['last_forward_from'] = "";
            	forward($forward_url);
            }
            else
            {
            	$user = get_loggedin_user();
            	
            	//Purges the metastrings: Too difficult to install a CRON in WIN
            	$sql = "DELETE FROM {$CONFIG->dbprefix}metastrings
				        WHERE id NOT IN (SELECT name_id FROM {$CONFIG->dbprefix}metadata)
				        AND id NOT IN (SELECT value_id FROM {$CONFIG->dbprefix}metadata)
				        AND id NOT IN (SELECT name_id FROM {$CONFIG->dbprefix}annotations)
				        AND id NOT IN (SELECT value_id FROM {$CONFIG->dbprefix}annotations)";
				execute_query ($sql, get_db_link('write'));
            	
            	if (!$user->isExpert)
            		forward("pg/lds/firststeps/");
            	else
            		forward("pg/lds/"); //Pau: changed from dashboard.
            }
			/// LdShake change ///
        } else {
            register_error(elgg_echo('loginerror'));
        }
      
?>