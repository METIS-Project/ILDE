<?php
	/**
	 * Elgg index page for web-based applications
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 */

	/**
	 * Start the Elgg engine
	 */
		require_once(dirname(__FILE__) . "/engine/start.php");
		
		/// LdShake change ///
		//Pau: Let the view go to the views folder
		$MyWelcomeMessage = elgg_view('welcome-message');
		/// LdShake change ///

		if (!trigger_plugin_hook('index','system',null,false)) {
	
			/**
		      * Check to see if user is logged in, if not display login form
		      **/
				
				//if (isloggedin()) forward('pg/dashboard/');
			/// LdShake change ///
			if (isloggedin())
			{
				$user = get_loggedin_user();
				if (!$user->isExpert)
					forward("pg/lds/firststeps/");
				else
					forward("pg/lds/"); //Pau: changed from dashboard.
			}
			/// LdShake change ///
	        //Load the front page
	        	global $CONFIG;

				/// LdShake change ///
	        	//$title = elgg_view_title(elgg_echo('content:latest'));
	        	//set_context('search');
		        //$content = list_registered_entities(0,4,true,false,array('object','group'));
		        //set_context('main');
		        global $autofeed;
		        $autofeed = false;
		        
		        //$content = elgg_view_layout('two_column_left_sidebar', '', $MyWelcomeMessage, elgg_view("account/forms/login"));
				
		        //$content = elgg_view_layout('three_column_left_sidebar',  $title . $content, $MyWelcomeMessage, elgg_view("account/forms/login"));

		        //echo page_draw(null, $content);
		        
		        $vars['sysmessages'] = system_messages(null,"");
		        echo elgg_view("home", $vars);
				/// LdShake change ///
		}



?>