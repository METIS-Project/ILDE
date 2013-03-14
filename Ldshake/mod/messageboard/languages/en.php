<?php

	$english = array(
	
		/**
		 * Menu items and titles
		 */
	
			'messageboard:board' => "Scrapbook",
			'messageboard:messageboard' => "scrapbook",
			'messageboard:viewall' => "View all",
			'messageboard:postit' => "Post it",
			'messageboard:history' => "history",
			'messageboard:none' => "There is nothing on this scrapbook yet",
			'messageboard:num_display' => "Number of scraps to display",
			'messageboard:desc' => "This is a scrapbook that you can put on your profile where other users can comment.",
	
			'messageboard:user' => "%s's scrapbook",
	
			'messageboard:history' => "History",
			
         /**
	     * Message board widget river
	     **/
	        
	        'messageboard:river:annotate' => "%s has had a new comment posted on their scrapbook.",
	        'messageboard:river:create' => "%s added the scrapbook widget.",
	        'messageboard:river:update' => "%s updated their scrapbook widget.",
	        'messageboard:river:added' => "%s posted on",
		    'messageboard:river:messageboard' => "scrapbook",

			
		/**
		 * Status messages
		 */
	
			'messageboard:posted' => "You successfully posted on the scrapbook.",
			'messageboard:deleted' => "You successfully deleted the scrap.",
	
		/**
		 * Email messages
		 */
	
			'messageboard:email:subject' => 'You have a new scrapbook comment!',
			'messageboard:email:body' => "You have a new scrapbook comment from %s. It reads:

			
%s


To view your scrapbook comments, click here:

	%s

To view %s's profile, click here:

	%s

You cannot reply to this email.",
	
		/**
		 * Error messages
		 */
	
			'messageboard:blank' => "Sorry; you need to actually put something in the scrap area before we can save it.",
			'messageboard:notfound' => "Sorry; we could not find the specified item.",
			'messageboard:notdeleted' => "Sorry; we could not delete this scrap.",
			'messageboard:somethingwentwrong' => "Something went wrong when trying to save your message, make sure you actually wrote a scrap.",
	     
			'messageboard:failure' => "An unexpected error occurred when adding your scrap. Please try again.",
	
	);
					
	add_translation("en",$english);

?>