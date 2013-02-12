<?php

require_once(dirname(dirname(dirname(__FILE__))) . "/engine/start.php");

admin_gatekeeper();
	
set_context('admin');
set_page_owner($_SESSION['guid']);
$body = elgg_view("role/forms/admin");
 
 $title = elgg_view_title(elgg_echo('role:admin_title'));
 
 $body = elgg_view_layout("two_column_left_sidebar", '', $title . $body);
echo'hola0';
 page_draw($title,$body);
    
?>
