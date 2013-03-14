<?php 
	// keywords
	if($keywords = get_plugin_setting("metatag_manager_keywords", "metatag_manager")){
	?>
		<meta name="keywords" content="<?php echo $keywords; ?>">
	<?php 
	}

	// description
	if($description = get_plugin_setting("metatag_manager_description", "metatag_manager")){
	?>
		<meta name="description" content="<?php echo $description; ?>">
	<?php 
	}
	
	// robots
	if($robots = get_plugin_setting("metatag_manager_robots", "metatag_manager")){
	?>
		<meta name="robots" content="<?php echo $robots; ?>">
	<?php 
	}
	
	//author
	if($author = get_plugin_setting("metatag_manager_author", "metatag_manager")){
	?>
		<meta name="author" content="<?php echo $author; ?>">
	<?php 
	}
?>