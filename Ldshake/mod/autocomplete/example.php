<?php
	require_once(dirname(dirname(dirname(__FILE__))) . "/engine/start.php");

	global $CONFIG;
	
	$title = elgg_echo("Example");
	
	$body = elgg_view_title($title);
	
	$groups_count = get_entities('group','',0,'',10,0,true); 
	$groups = get_entities('group','',0,'',$groups_count,0);
	$body .=  elgg_view('input/autocomplete', array('entities' => $groups));
	
    
    $body = elgg_view_layout('one_column', $body);
	
	// Finally draw the page
	page_draw($title, $body);
?>