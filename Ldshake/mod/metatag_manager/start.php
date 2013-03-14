<?php 
	// init
	function metatag_manager_init(){
		
		// extend metatags
		extend_view("metatags", "metatag_manager/metatags");
		
		// extend metatags
		extend_view("admin/site", "metatag_manager/settings");
	}
	
	// Default event handlers for plugin functionality
	register_elgg_event_handler('init', 'system', 'metatag_manager_init');
	
	// admin only actions
	register_action("metatag_manager/save", false, $CONFIG->pluginspath . "metatag_manager/actions/save.php", true);
?>