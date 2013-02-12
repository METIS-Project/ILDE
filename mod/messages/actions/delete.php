<?php

    /**
	 * Elgg delete a message action page
	 * It is worth noting that due to the nature of a messaging system and the fact 2 people access
	 * the same message, messages don't actually delete, they are just removed from view for the user who deletes
	 * 
	 * @package ElggMessages
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd <info@elgg.com>
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.com/
	 */
	 
	// Need to be logged in to do this
	    gatekeeper();
 
    // grab details sent from the form
        $message_id = get_input('message_id');
        $type = get_input('type'); // sent message or inbox
        
    // get the message object
        $message = get_entity($message_id);
        
    // Make sure we actually have permission to edit and that the object is of sub-type messages
		if ($message->getSubtype() == "messages" || $message->getSubtype() == "sent_messages") {
    		
    		    //check to see if it is a sent message to be deleted
			    if($type == 'sent'){
    			    $message->hiddenTo = 1; // setting to 1 equals being delete
    			    // Success message
				    system_message(T("Your message was successfully deleted."));
				    forward("mod/messages/sent.php");
			    }else{
    			    $message->hiddenFrom = 1; // setting to 1 equals being deleted
    			    $message->readYet = 1; // if the user deletes, set the readYet field to read so it doesn't show up on their unread messages. 
    			    // Success message
				    system_message(elgg_echo("messages:deleted"));
    			    forward("mod/messages/?username=" . $_SESSION['user']->username);
			    }
            
        }else{
            
            // display the error message
            register_error(elgg_echo("messages:nopremission"));
			forward("mod/messages/?username=" . $_SESSION['user']->username);
			
		}
                 
    
?>