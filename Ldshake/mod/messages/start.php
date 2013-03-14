<?php

	/**
	 * Elgg internal messages plugin
	 * This plugin lets user send each other messages.
	 * 
	 * @package ElggMessages
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd <info@elgg.com>
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.com/
	 */

	/**
	 * Messages initialisation
	 *
	 * These parameters are required for the event API, but we won't use them:
	 * 
	 * @param unknown_type $event
	 * @param unknown_type $object_type
	 * @param unknown_type $object
	 */
	 
	    function messages_init() {
    	    
    	    // Load system configuration
				global $CONFIG;
				
			// Load the language file
				register_translations($CONFIG->pluginspath . "messages/languages/");
				
			//add submenu options
				if (get_context() == "messages") {
					add_submenu_item(T("Inbox"), $CONFIG->wwwroot . "pg/messages/" . $_SESSION['user']->username);
					add_submenu_item(T("Send a message"), $CONFIG->wwwroot . "mod/messages/send.php");
					add_submenu_item(T("Sent messages"), $CONFIG->wwwroot . "mod/messages/sent.php");
				}
				
			// Extend system CSS with our own styles, which are defined in the shouts/css view
				extend_view('css','messages/css');
				
				extend_view('page_elements/jsarea', 'messages/js');
				
			// Extend the elgg topbar
				extend_view('elgg_topbar/extend','messages/topbar');
			
			// Register a page handler, so we can have nice URLs
				register_page_handler('messages','messages_page_handler');
				
			// Register a URL handler for shouts posts
				register_entity_url_handler('messages_url','object','messages');
                register_entity_url_handler('messages_url','object','sent_messages');

	        // Extend hover-over and profile menu	
				extend_view('profile/menu/links','messages/menu');
				
		    // Shares widget
			  //  add_widget_type('messages',elgg_echo("messages:recent"),elgg_echo("messages:widget:description"));
			    
			// Override metadata permissions
			    register_plugin_hook('permissions_check:metadata','object','messages_can_edit_metadata');
			    
		}
		
		/**
		 * Override the canEditMetadata function to return true for messages
		 *
		 */
		function messages_can_edit_metadata($hook_name, $entity_type, $return_value, $parameters) {
			
			$entity = $parameters['entity'];
			if ($entity->getSubtype() == "messages") {
				return true;
			}
			
			return $return_value;
			
		}
		
		/**
		 * messages page handler; allows the use of fancy URLs
		 *
		 * @param array $page From the page_handler function
		 * @return true|false Depending on success
		 */
		function messages_page_handler($page) {
			
			// The first component of a messages URL is the username
			if (isset($page[0])) {
				set_input('username',$page[0]);
			}
			
			// The second part dictates what we're doing
			if (isset($page[1])) {
				switch($page[1]) {
					case "read":		set_input('message',$page[2]);
										@include(dirname(__FILE__) . "/read.php");
										break;
				}
			// If the URL is just 'messages/username', or just 'messages/', load the standard messages index
			} else {
				@include(dirname(__FILE__) . "/index.php");
				return true;
			}
			
			return false;
			
		}

		function messages_url($message) {
			
			global $CONFIG;
			return $CONFIG->url . "pg/messages/" . $message->getOwnerEntity()->username . "/read/" . $message->getGUID();
			
		}
		
    // A simple function to count the number of messages that are unread in a user's inbox
        function count_unread_messages() {
            
            //get the users inbox messages
		    $num_messages = get_entities_from_metadata("toId", $_SESSION['user']->getGUID(), "object", "messages", 0, 10, 0, "", 0, false);
		
		    //set a counter
		    $counter = 0;
		
		    //loop through the inbox and count the number unread
		    if (is_array($num_messages))
		    {
			    foreach($num_messages as $num){
	    		    if($num->readYet == 0)
	    		        $counter++;	   
	    		    
	            }
		    }
            
            return $counter;
            
        }
		
	// Make sure the messages initialisation function is called on initialisation
		register_elgg_event_handler('init','system','messages_init');
		
	// Register actions
		global $CONFIG;
		register_action("messages/send",false,$CONFIG->pluginspath . "messages/actions/send.php");
		register_action("messages/delete",false,$CONFIG->pluginspath . "messages/actions/delete.php");
	 
?>