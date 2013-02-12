<?php

/**
 * Elgg FCKeditor admin page
 * 
 * @package fckeditor
 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
 * @author Don Robertson <don@robertson.net.nz>
 * @copyright 
 * @link http://www.robertson.net.nz/
 * 
 */

if ($vars['always_sync'] == 'yes') {
	$checked = 'checked="checked"';
} else {
	$checked = '';
}
/*
$default_server = $vars['default_server'];
$greenlist = $vars['greenlist'];
$yellowlist = $vars['yellowlist'];
$redlist = $vars['redlist'];

// replace this with the correct URL once the core Elgg 1.0 action system is working

$action = $CONFIG->wwwroot.'mod/openid_client/actions/admin.php';

$default_server_title = elgg_echo('openid_client:default_server_title');
$default_server_instructions1 = elgg_echo('openid_client:default_server_instructions1');
$default_server_instructions2 = elgg_echo('openid_client:default_server_instructions2');
$server_sync_title = elgg_echo('openid_client:server_sync_title');
$server_sync_instructions = elgg_echo('openid_client:server_sync_instructions');
$server_sync_label = elgg_echo('openid_client:server_sync_label');
$lists_title = elgg_echo('openid_client:lists_title');

$lists_instruction1 = elgg_echo('openid_client:lists_instruction1');
$lists_instruction2 = elgg_echo('openid_client:lists_instruction2');
$lists_instruction3 = elgg_echo('openid_client:lists_instruction3');
$lists_instruction4 = elgg_echo('openid_client:lists_instruction4');
$lists_instruction5 = elgg_echo('openid_client:lists_instruction5');
$lists_instruction6 = elgg_echo('openid_client:lists_instruction6');

$green_list_title = elgg_echo('openid_client:green_list_title');
$yellow_list_title = elgg_echo('openid_client:yellow_list_title');
$red_list_title = elgg_echo('openid_client:red_list_title');

$ok_button_label = elgg_echo('openid_client:ok_button_label');

$body = <<<END
<div class="admin_statistics">
<form action="$action" method="post">
<input type="hidden" name ="action" value="submit_form" />
<h3>$default_server_title</h3>
<p>$default_server_instructions1</p>
<p>$default_server_instructions2</p>
<br />
<input type="text" size="60" name="default_server" value="$default_server" />
<h3>$server_sync_title</h3>
<p>$server_sync_instructions</p>
<br />
<input type="checkbox" name="always_sync" value="yes" $checked />
$server_sync_label
<h3>$lists_title</h3>
<p>$lists_instruction1</p>
<p>$lists_instruction2</p>
<p>$lists_instruction3</p>
<p>$lists_instruction4</p>
<p>$lists_instruction5</p>
<p>$lists_instruction6</p>
<h3>$green_list_title</h3>
<textarea name="greenlist" rows="5" cols="60">$greenlist</textarea>
<h3>$yellow_list_title</h3>
<textarea name="yellowlist" rows="5" cols="60">$yellowlist</textarea>
<h3>$red_list_title</h3>
<textarea name="redlist" rows="5" cols="60">$redlist</textarea>
<br /><br />
<input type="submit" name="submit" value="$ok_button_label" />
</form>
</div>
END;
*/
$body = <<<END
<h1>TO DO</h1>
<p>Setasdf asdf asdf asdf asdf .php file</p>
END;
print $body;

?>
