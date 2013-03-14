<?php 

	// keywords
	$keywords = get_input("keywords" , ""); 
	set_plugin_setting("metatag_manager_keywords", $keywords, "metatag_manager");
	
	// description
	$description = get_input("description" , "");
	set_plugin_setting("metatag_manager_description", $description, "metatag_manager");
	
	// robots
	$robots = get_input("robots", "");
	set_plugin_setting("metatag_manager_robots", $robots, "metatag_manager");
	
	//author
	$author = get_input("author", "");
	set_plugin_setting("metatag_manager_author", $author, "metatag_manager");

	system_message(elgg_echo('metatag_manager:settings:save'));
	forward($_SERVER['HTTP_REFERER']);
?>