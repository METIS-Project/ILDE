<?php	

/*
** Contacts (as opposed to friends)
**
** @author Philip Hart, Centre for Learning and Performance Technology (www.c4lpt.co.uk)
** @copyright Tesserae Ltd 2009
** @link http://www.c4lpt.co.uk/ElggConsultancy.html
** @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
**
*/

function contacts_init()
{
	global $CONFIG;
			
	register_translations($CONFIG->pluginspath . "contacts/languages/");
}

// Initialise this plugin
register_elgg_event_handler('init','system','contacts_init');

?>
