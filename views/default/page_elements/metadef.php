<?php

	//Pau: Made this section "includeable" in order to be able to build views without the common body header


	/**
	 * Elgg pageshell when logged out
	 * The standard HTML header that displays across the site
	 * 
	 * @package Elgg
	 * @subpackage Core
	 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
	 * @author Curverider Ltd
	 * @copyright Curverider Ltd 2008
	 * @link http://elgg.org/
	 * 
	 * @uses $vars['config'] The site configuration settings, imported
	 * @uses $vars['title'] The page title
	 * @uses $vars['body'] The main content of the page
	 * @uses $vars['messages'] A 2d array of various message registers, passed from system_messages()
	 */
	 
	 // Set title
		if (empty($vars['title'])) {
			$title = $vars['config']->sitename;
		} else if (empty($vars['config']->sitename)) {
			$title = $vars['title'];
		} else {
			$title = $vars['title'] . ' - LdShake';
		}
		
		global $autofeed;
		if (isset($autofeed) && $autofeed == true) {
			$url = $url2 = full_url();
			if (substr_count($url,'?')) {
				$url .= "&view=rss";
			} else {
				$url .= "?view=rss";
			}
			if (substr_count($url2,'?')) {
				$url2 .= "&view=odd";
			} else {
				$url2 .= "?view=opendd";
			}
			$feedref = <<<END
			
	<link rel="alternate" type="application/rss+xml" title="RSS" href="{$url}" />
	<link rel="alternate" type="application/odd+xml" title="OpenDD" href="{$url2}" />
			
END;
		} else {
			$feedref = "";
		}
		
?>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title><?php echo $title; ?></title>
	
<?php

	global $pickerinuse;
	if (isset($pickerinuse) && $pickerinuse == true) {

?>
	
	<!-- only needed on pages where we have friends collections and/or the friends picker-->
	<script type="text/javascript" src="<?php echo $vars['url']; ?>vendors/jquery/jquery-easing.1.2.pack.js"></script>
	<script type="text/javascript" src="<?php echo $vars['url']; ?>vendors/jquery/jquery-easing-compatibility.1.2.pack.js"></script>
	<script type="text/javascript" src="<?php echo $vars['url']; ?>pg/js/friendsPickerv1.js"></script>
	
<?php

	}

	//Pau: We append a checksum of the generated css file in order to break all clients' caches.
	?>
	<link rel="stylesheet" href="<?php echo $vars['url']; ?>_css/css.css?hash=<?php printf("%u", crc32(elgg_view("css")));?>" type="text/css" />
	<link rel="stylesheet" href="<?php echo $vars['url']; ?>mod/lds/autoSuggest/autoSuggest.css" type="text/css" />
	<link rel="stylesheet" href="<?php echo $vars['url']; ?>vendors/jquery/css/ui-lightness/jquery-ui-1.8.6.custom.css" type="text/css" />
	<link rel="icon" type="image/png" href="<?php echo $vars['url']; ?>_graphics/favicon.ico" />
	
<?php 

		echo $feedref;
		//echo elgg_view('metatags',$vars); //Pau... i removed it :S 
	
		//echo elgg_view('page_elements/analytics', $vars);
?>