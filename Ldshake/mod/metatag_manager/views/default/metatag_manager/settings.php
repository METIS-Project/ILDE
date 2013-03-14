<?php 

	$action = $vars['url'] . "action/metatag_manager/save";

	// keywords
	$keywords = get_plugin_setting("metatag_manager_keywords", "metatag_manager");
	// description
	$description = get_plugin_setting("metatag_manager_description", "metatag_manager");
	// robots
	$robots = get_plugin_setting("metatag_manager_robots", "metatag_manager");
	//author
	$author = get_plugin_setting("metatag_manager_author", "metatag_manager");

	// building form
	$formdata .= "<label>" . elgg_echo("metatag_manager:settings:keywords") . "</label>";
	$formdata .= elgg_view("input/plaintext" , array("internalname" => "keywords", "value" => $keywords));
	
	$formdata .= "<label>" . elgg_echo("metatag_manager:settings:description") . "</label>";
	$formdata .= elgg_view("input/plaintext" , array("internalname" => "description", "value" => $description));
	
	$formdata .= "<label>" . elgg_echo("metatag_manager:settings:robots") . "</label>";
	$formdata .= elgg_view("input/text" , array("internalname" => "robots", "value" => $robots));
	
	$formdata .= "<label>" . elgg_echo("metatag_manager:settings:author") . "</label>";
	$formdata .= elgg_view("input/text" , array("internalname" => "author", "value" => $author));
	
	$formdata .= elgg_view("input/submit" , array("value" => elgg_echo("save")));
	
	$form = elgg_view("input/form", array("body" => $formdata, "action"=>$action));
	
	
?>
<div class='contentWrapper'>
	<h3 class='settings'><?php echo elgg_echo("metatag_manager:settings:title"); ?></h3>
	<?php echo $form;?>
</div>