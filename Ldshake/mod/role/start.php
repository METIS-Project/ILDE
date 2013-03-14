<?php

    /**
     * fckeditor wysiwyg editor
     * @package ElggFCK
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Don Robertson
	 * @copyright 
	 * @link http://www.robertson.net.nz/
     **/
  
    function role_init() {
			
	    // Load system configuration
		global $CONFIG;

				
     }
     function role_pagesetup()
    	{
	    	echo'puta';
    		if (get_context() == 'admin' && isadminloggedin()) {
    			global $CONFIG;
    			add_submenu_item(elgg_echo('role:admin_title'), $CONFIG->wwwroot . 'mod/role/admin.php');
    		}
    	}
     // Make sure the status initialisation function is called on initialisation
	register_elgg_event_handler('init','system','role_init');
	
?>
