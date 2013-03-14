<?php

    /**
	 * Elgg send a message page
	 *
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
	 * @uses $vars['friends'] This is an array of a user's friends and is used to populate the list of
	 * people the user can message
	 *
	 */
	 
	 //grab the user id to send a message to. This will only happen if a user clicks on the 'send a message'
	 //link on a user's profile or hover-over menu
	 
	 $send_to = get_input('send_to');
	 
	 $template = get_input ('template');
	 
	 $extra = get_input ('extra');
	 
	 $templates = array
	 (
		'edit_request' => array
	 		(
	 			'title' => ("I would like to edit one of your LdS"),
	 			'body' => ("Hi!\n\nI\'ve been looking at your LdS \"{%1}\", and I would like to edit it. Can you give me editing permissions for it?\n\nThanks!")
	 		)
	 );
?>

	<form action="<?php echo $vars['url']; ?>action/messages/send" method="post" name="messageForm">
			
	    <?php
			    
	        //check to see if the message recipient has already been selected
			if($send_to){
    			
    			//get the user object  
    	        $user = get_user($send_to);
    	        /// LdShake change ///
    	        //draw it
    	        ?>
    	        <div style="float:left; padding-top:7px; padding-right: 15px; text-align:right; width: 50px;">
    	        	<?php echo T("To"); ?>: 
    	        </div>
    	        <div style="padding-top:7px">
    	        	<img src="<?php echo $user->getIcon('tiny') ?>" style="vertical-align: middle; margin-right: 5px;"/><strong><?php echo $user->name ?></strong>
    	        </div>
    	        <div style="clear:both; height:20px;"></div>
    	        <?php
    			//set the hidden input field to the recipients guid
    	        echo "<input type=\"hidden\" name=\"send_to\" value=\"{$send_to}\" />";
    	        
    			    
	        }else{
    	          /// LdShake change ///  
        ?>
    	   	<div style="clear:both; height:20px;"></div>	
            <div style="float:left; padding-top:7px; padding-right: 15px; text-align:right; width: 50px;"><?php echo T("To"); ?>:</div>
    	    <select name='send_to' style="padding:5px; border: 1px solid #ccc;">
    	    <?php 
    			$user = get_loggedin_user();
                    if(isadminloggedin()) {
                        $groups = get_entities('group', '', 0 , '', 9999);
                    } else {
                        $groups = get_users_membership($user->getGUID());
                        $owner_groups = get_entities('group', '', $user->getGUID(), '', 9999);
                        foreach ($owner_groups as $og){
                            if(!$og->isMember($user->getGUID()))
                                $groups[] = $og;
                        }
                }


                foreach($groups as $g){

                    //populate the send to box with a user's friends
                    echo "<option value='{$g->guid}'>" . $g->name .' (group)</option>';
                }
				/// LdShake change ///
    	        foreach($vars['friends'] as $friend){
        			   
        	        //populate the send to box with a user's friends
    			    echo "<option value='{$friend->guid}'>" . $friend->name .' ('. $friend->username . ")</option>";
    			        
    		    }
    		        
            ?>
    		</select>
    		<div style="clear:both; height:20px;"></div>
        <?php
    		
	        }//end send_to if statement
		        
	    ?>
	    
		<div style="float:left; padding-top:7px; padding-right: 15px; text-align:right; width: 50px;"><?php echo T("Title"); ?>:</div> <input type='text' name='title' value='' style="width:660px;" />
		<div style="clear:both; height:20px;"></div>
		<div><?php echo T("Message"); ?>
		<textarea class="input-textarea" name="message" style="height: 300px; margin-top: 5px;"></textarea>
		</div>
		<p><input type="submit" class="submit_button" value="<?php echo T("Send"); ?>" /></p>
	
	</form>