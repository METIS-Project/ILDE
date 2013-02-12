<?php

	/**
	 * Elgg send a message page
	 * 
	 * @package ElggMessages
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd <info@elgg.com>
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.com/
	 */


	function osorti(&$array, $props)
	{
	    if(!is_array($props))
	        $props = array($props => true);
	       
	    usort($array, function($a, $b) use ($props) {
	        foreach($props as $prop => $ascending)
	        {
	            if($a->$prop != $b->$prop)
	            {
	                if($ascending)
	                    return mb_strtolower($a->$prop) > mb_strtolower($b->$prop) ? 1 : -1;
	                else
	                    return mb_strtolower($b->$prop) > mb_strtolower($a->$prop) ? 1 : -1;
	            }
	        }
	        return -1; //if all props equal       
	    });   
	}

	// Load Elgg engine
		require_once(dirname(dirname(dirname(__FILE__))) . "/engine/start.php");
		
	// If we're not logged in, forward to the front page
		gatekeeper(); // if (!isloggedin()) forward();
		
    // Get the current page's owner
		$page_owner = page_owner_entity();
		if ($page_owner === false || is_null($page_owner)) {
			$page_owner = $_SESSION['user'];
			set_page_owner($page_owner->getGUID());
		} 
		
    // Get the users friends; this is used in the drop down to select who to send the message to
         $friends = $_SESSION['user']->getFriends('', 9999);
         osorti($friends, 'name');
        
    // Set the page title
	    $area2 = elgg_view_title(T("Send a message"));
        
    // Get the send form
		$area2 .= elgg_view("messages/forms/message",array('friends' => $friends));

	// Format
		$body = elgg_view_layout("two_column_left_sidebar", '', $area2);
		
	// Draw page
		page_draw(sprintf(T("Send a message"),$page_owner->name),$body);
		
?>