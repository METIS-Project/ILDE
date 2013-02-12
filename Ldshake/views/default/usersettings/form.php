<?php

	$form_body = "<div class=\"settings_form\">" . elgg_view("usersettings/user") . "</div>";
	$form_body .= "<p>" . elgg_view('input/submit', array('value' => T("Save"))) . "</p>";

	echo elgg_view('input/form', array('action' => "{$vars['url']}action/usersettings/save", 'body' => $form_body));
?>