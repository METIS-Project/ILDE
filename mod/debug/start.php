<?php

/**
  * Helper plug-in for debugging
  * @author: Pau Moreno
  * @created: 30-09-2010
  */
 
//When set to 1, displys the contents of the $CONFIG and $vars vars in the footer of each page.
//The code that prints this info is located in the /views/canvas/layouts/two_column_left_sidebar.php of this module.
define ('DEBUG_SHOW_FOOTER_INFO', 0);
  
function debug_init ()
{
	//We don't want error messages to go to a log, we want them to be displayed on the browser, so we set the error handling back to the default behaviour
	//(It was Elgg that changed that in its bootstrap process)
	restore_error_handler ();
	//error_reporting(E_ALL);
	error_reporting(E_ALL ^ E_NOTICE);
	ini_set("display_errors", 1);
	
	//We want the /lds/something urls to be ours
	register_page_handler('debug','debug_page_handler');
	
	extend_view('page_elements/jsarea', 'debug/dumpinfo', 99);
}

function debug_page_handler ($page)
{
	//Nothing here will be exposed to non-logged users, so go away!
	gatekeeper();
	
	//Sub_controller dispatcher
	if (function_exists("debug_exec_{$page[0]}"))
	{
		set_context("debug_exec_{$page[0]}");
		call_user_func("debug_exec_{$page[0]}", $page);
	}
	else
		die ('404'); //TODO
}

//Pretty print of a print_r (preserving line breaks)
function debug_print ($var, $escapehtml = false)
{
	echo "<pre>";
	echo ($escapehtml) ? htmlspecialchars(print_r ($var, true)) : print_r ($var, true);
	echo "</pre>";
}

//Short & handy alias:
function dprint ($var, $escapehtml = false)
{
	debug_print($var, $escapehtml);
}

function debug_exec_display ($params)
{
	$guid = $params[1];
	
	$vars['entity'] = get_entity($guid);
	//dprint($vars['entity']);
	if ($vars['entity']->enabled == false)
	{
		$vars['enable_disabled'] = 'enabled'; // ;D
		//We want to see ALL entities (including the disabled ones)
		$access_status = access_get_show_hidden_status();
		access_show_hidden_entities(true);
	}
	
	$vars['entity'] = get_entity($guid);
	$vars['metadata'] = get_metadata_for_entity($guid);

	echo elgg_view('debug/display',$vars);
	
	if ($vars['enable_disabled'] == 'enabled')
	{
		//Restore normal behaviour.
		access_show_hidden_entities($access_status);
	}
}

register_elgg_event_handler('init','system','debug_init');

?>