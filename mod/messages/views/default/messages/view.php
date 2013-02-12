<?php

	/**
	 * Elgg messages view page
	 *
     * @file modified by Interactive Technologies Group (GTI)
     * @authors (alpha. order) Abenia, P., Carralero, M.A., Chacón, J., Hernández-Leo, D., Moreno, P..
     * @Copyright (C) 2009-2012, Universitat Pompeu Fabra, Barcelona.
     * @based on:
	 * @package ElggMessages
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd <info@elgg.com>
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.com/
	 * 
	 * @uses $vars['entity'] An array of messages to view
	 * @uses $vars['page_view'] This is the page the messages are being accessed from; inbox or sentbox
	 * 
	 */
	 
	// If there are any messages to view, view them
	if (isloggedin())
    if (is_array($vars['entity']) && sizeof($vars['entity']) > 0) {
    		
?>
    <div id="messages" /><!-- start the main messages wrapper div -->
            
<?php
    		
    		// get the correct display for the inbox view
    		if($vars['page_view'] == "inbox") {
        		
    			foreach($vars['entity'] as $message) {
    				if ($message->owner_guid == $vars['user']->guid
    					|| $message->toID == $vars['user']->guid) {
        			
        			//make sure to only display the messages that have not been 'deleted' (1 = deleted)
    				if($message->hiddenFrom != 1){
        				
        				// check to see if the message has been read, if so, get the correct background color
        			    if($message->readYet == 1){
                            echo "<div class=\"message_read\" />";
                        }else{
                            echo "<div class=\"message_notread\" />";
                        }

                        $group = get_entity($message->groupReply);

                        $sender_group_name = "";
                        if($group instanceof ElggGroup) {

                                $sender_group_name = ", " . $group->name;
                        }

                        //set the table
        				echo "<table width=\"100%\" cellspacing='0'><tr>";       
                        //get the icon of the user who owns the message
        				echo "<td width='200px'>" . elgg_view("profile/icon",array('entity' => $message->getOwnerEntity(), 'size' => 'tiny')) . "<div class='msgsender'><b>" . $message->getOwnerEntity()->name . $sender_group_name . "</b><br /><small>" . friendly_time($message->time_created) . "</small></div></td>";
        				//display the message title
    				    echo "<td><div class='msgsubject'><a href=\"{$message->getURL()}\">" . $message->title . "</a></div></td>";
    				    //display the link to 'delete'
    				    
    				    echo "<td width='70px'>";
    				    echo "<div class='delete_msg'>" . elgg_view("output/confirmlink", array(
																'href' => $vars['url'] . "action/messages/delete?message_id=" . $message->getGUID() . "&type=inbox",
																'text' =>T("Delete message"),
																'confirm' => T("Are you sure you want to delete this message?"),
															)) . "</div>";								
															
		                echo "</td></tr></table>";
		                echo "</div>"; // close the message background div
		                
    			    }//end of hiddenFrom if statement
    				} // end of user check 
    				
    			}//end of for each loop
    			
			}//end of inbox if statement
			
			// get the correct display for the sentbox view
			if($vars['page_view'] == "sent") {
    			
    			foreach($vars['entity'] as $message) {
        			
        			//make sure to only display the messages that have not been 'deleted' (1 = deleted)
    				if($message->hiddenFrom != 1){
        				
        				//get the correct user entity
        				$user = get_entity($message->toID);

                        $init_user = null;
                        if($user instanceof ElggGroup) {
                            $init_user = get_entity($message->nonMemberGroupReply);

                            if($init_user)
                                $init_user_name = ", " . $init_user->name;
                        }

        				echo "<table width=\"100%\" cellspacing='0'><tr>";
        				
        				//get the icon for the user the message was sent to

        				echo "<tr><td width='200px'>" . elgg_view("profile/icon",array('entity' => $user, 'size' => 'tiny')) . "<div class='msgsender'><b>" . $user->name . " " . $init_user_name . "</b><br /><small>" . friendly_time($message->time_created) . "</small></div></td>";
                        //echo "<tr><td width='200px'>" . $user->getIcon('tiny') . "<div class='msgsender'><b>" . $user->name . " " . $init_user_name . "</b><br /><small>" . friendly_time($message->time_created) . "</small></div></td>";
        				//display the message title
    				    echo "<td><div class='msgsubject'><a href=\"{$message->getURL()}?type=sent\">" . $message->title . "</a></div></td>";
        				//display the link to 'delete'
        				
        				echo "<td width='70px'>";
							echo "<div class='delete_msg'>" . elgg_view("output/confirmlink", array(
							'href' => $vars['url'] . "action/messages/delete?message_id=" . $message->getGUID() . "&type=inbox",
							'text' => T("Delete message"),
							'confirm' => T("Are you sure you want to delete this message?"),
						)) . "</div>";
 						echo "</td></tr></table>";
						
    			    }//close hiddeTo if statement
    				
    			}//close foreach
    			
			}//close page_view sent if statement
			
			echo "</div>"; // close the main messages wrapper div
			
    } else {
        
    	"<p class='messages_nomessage_message'>" . T("There are no messages to display.") . "</p>";
    		
	}//end of the first if statement
?>