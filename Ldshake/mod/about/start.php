<?php

/**
  * Helper plug-in for the about section
  * @author: Pau Moreno
  * @created: 30-09-2010
  */
  
function about_init ()
{
	//We want the /about/something urls to be ours
	register_page_handler('about','about_page_handler');
	
	extend_view('css','about/css');
}

function about_page_handler ($page)
{
	$body = elgg_view('about/contents');
	$body = elgg_view_layout("one_column", $body);
	page_draw('About LdShake', $body);
}

register_elgg_event_handler('init','system','about_init');

?>