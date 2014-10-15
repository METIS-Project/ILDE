<?php
/**
 * Admin user validation.
 *
 * @package Elgg
 * @subpackage Core
 * @license http://www.gnu.org/licenses/old-licenses/gpl-2.0.html GNU Public License version 2
 * @author Curverider Ltd
 * @copyright Curverider Ltd 2008
 * @link http://elgg.org/
 */

require_once(dirname(dirname(dirname(dirname(__FILE__)))) . "/engine/start.php");
global $CONFIG;

// block non-admin users
admin_gatekeeper();
action_gatekeeper();

// Get the user
$guid = get_input('guid');

$access_status = access_get_show_hidden_status();
access_show_hidden_entities(true);
$obj = get_entity($guid);

if ( ($obj instanceof ElggUser) && ($obj->canEdit()))
{
    if(set_user_validation_status($obj->guid, true, 'admin') && $obj->enable()) {
        $obj->save();
        system_message(T('The user has been validated.'));

        //notify_user($obj->guid, $CONFIG->site->guid, elgg_echo('email:resetpassword:subject'), sprintf(elgg_echo('email:resetpassword:body'), $obj->name, $password, $obj->username), NULL, 'email');
    } else
        register_error(T('Error validating the user.'));
}
else
    register_error(T('Error validating the user.'));

access_show_hidden_entities($access_status);

forward($_SERVER['HTTP_REFERER']);
exit;
?>